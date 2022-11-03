package site.siredvin.peripheralexpansion.computercraft.peripheral

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.block.state.properties.Property
import site.siredvin.peripheralexpansion.PeripheralExpansion
import site.siredvin.peripheralexpansion.common.blockentities.FlexibleRealityAnchorTileEntity
import site.siredvin.peripheralexpansion.common.blockentities.RealityForgerBlockEntity
import site.siredvin.peripheralexpansion.common.blocks.FlexibleRealityAnchor
import site.siredvin.peripheralexpansion.common.setup.Blocks
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.BlockEntityPeripheralOwner
import site.siredvin.peripheralium.util.radiusCorrect
import site.siredvin.peripheralium.util.representation.LuaInterpretation
import site.siredvin.peripheralium.util.representation.LuaRepresentation
import site.siredvin.peripheralium.util.world.ScanUtils
import java.util.*

class RealityForgerPeripheral(blockEntity: RealityForgerBlockEntity
) : OwnedPeripheral<BlockEntityPeripheralOwner<RealityForgerBlockEntity>>(TYPE, BlockEntityPeripheralOwner(blockEntity)) {

    companion object {
        const val TYPE = "reality_forger"
        private val FLAG_MAPPING = mapOf(
            "playerPassable" to FlexibleRealityAnchor.PLAYER_PASSABLE,
            "lightPassable" to FlexibleRealityAnchor.LIGHT_PASSABLE,
            "skyLightPassable" to FlexibleRealityAnchor.SKY_LIGHT_PASSABLE,
            "invisible" to FlexibleRealityAnchor.INVISIBLE,
        )
    }

    override val isEnabled: Boolean
        get() = true

    val interactionRadius: Int
        get() = 8

//     Please, don't blame me for this untyped garbage code
//     If you can handle it better, you're welcome
    @Throws(LuaException::class)
    private fun applyBlockAttrs(state: BlockState, blockAttrs: Map<*, *>): BlockState {
        var changeableState: BlockState = state
        blockAttrs.forEach { attr ->
            val property = state.properties.find { it.name.equals(attr.key) }
                ?: throw LuaException(String.format("Unknown property name %s", attr.key))
            when (property) {
                is EnumProperty -> {
                    val value = attr.value.toString().lowercase()
                    val targetValue = property.getPossibleValues().find { it.toString().lowercase() == value }
                        ?: throw LuaException(
                            java.lang.String.format(
                                "Incorrect value %s, only %s is allowed",
                                attr.key,
                                property.getPossibleValues().joinToString(", ")
                            )
                        )
                    // AAAAAAAAAAAAAAAAAA
                    // WHAT THE HELL IS THIS CASTING
                    // YOU TELL ME
                    val trickedProperty = property as Property<Comparable<Any>>
                    changeableState = changeableState.setValue(trickedProperty, targetValue as Comparable<Any>)
                }

                is BooleanProperty -> {
                    if (attr.value !is Boolean)
                        throw LuaException(String.format("Incorrect value %s, should be boolean", attr.key))
                    changeableState = changeableState.setValue(property, attr.value as Boolean)
                }

                is IntegerProperty -> {
                    if (attr.value !is Number)
                        throw LuaException(String.format("Incorrect value %s, should be boolean", attr.key))
                    changeableState = changeableState.setValue(property, (attr.value as Number).toInt())
                }
            }
        }
        return changeableState
    }

    @Throws(LuaException::class)
    private fun findBlock(table: Map<*, *>): BlockState? {
        if (table.containsKey("block")) {
            val blockID = table["block"].toString()
            // TODO: add blacklist
//            if (ProgressivePeripheralsConfig.realityForgerBlacklist.contains(blockID)) throw LuaException("You cannot use this block, is blacklisted")
            val block = Registry.BLOCK.get(ResourceLocation(blockID)) ?: throw LuaException(String.format("Cannot find block %s", table["block"]))
            var targetState = block.defaultBlockState()
            if (table.containsKey("attrs")) {
                val blockAttrs = table["attrs"] as? Map<*, *> ?: throw LuaException("attrs should be a table")
                targetState = applyBlockAttrs(targetState, blockAttrs)
            }
            return targetState
        }
        return null
    }

    private fun forgeRealityTileEntity(
        realityMirror: FlexibleRealityAnchorTileEntity,
        targetState: BlockState,
        appearanceTable: Map<*, *>,
    ) {
        appearanceTable.forEach {
            if (it.value is Boolean) {
                val targetProperty = FLAG_MAPPING[it.key.toString()]
                if (targetProperty != null)
                    realityMirror.setBooleanStateValue(targetProperty, it.value as Boolean)
            } else if (it.key == "lightLevel" && it.value is Number) {
                realityMirror.lightLevel = (it.value as Number).toInt()
            }
        }
        realityMirror.setMimic(targetState)
    }

    @LuaFunction(mainThread = true)
    fun detectAnchors(): List<Map<String, Any>> {
        val data = mutableListOf<Map<String, Any>>()
        ScanUtils.traverseBlocks(level!!, pos, interactionRadius, {blockState, pos ->
            val blockEntity = level!!.getBlockEntity(pos)
            if (blockEntity is FlexibleRealityAnchorTileEntity) {
                data.add(LuaRepresentation.forBlockPos(pos, Direction.EAST, this.pos))
            }
            if (blockState.`is`(Blocks.FLEXIBLE_REALITY_ANCHOR)) {
                PeripheralExpansion.LOGGER.warn(blockEntity)
            }
        })
        return data
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun forgeRealityPiece(arguments: IArguments): MethodResult {
        val targetPosition = LuaInterpretation.asBlockPos(pos, arguments.getTable(0))
        if (!radiusCorrect(pos, targetPosition, interactionRadius))
        return MethodResult.of(null, "Block are too far away")
        val appearanceTable = arguments.getTable(1)
        val entity = level!!.getBlockEntity(targetPosition) as? FlexibleRealityAnchorTileEntity
            ?: return MethodResult.of(false, "Incorrect coordinates")
        val targetState = findBlock(appearanceTable) ?: return MethodResult.of(null, "Cannot find valid block to transform to")
        forgeRealityTileEntity(entity, targetState, appearanceTable)
        return MethodResult.of(true)
    }


//    @LuaFunction(mainThread = true)
//    @Throws(LuaException::class)
//    fun forgeRealityPieces(arguments: IArguments): MethodResult {
//        val center: BlockPos = getPos()
//        val world: World = getWorld()
//        val poses: MutableList<BlockPos> = ArrayList<BlockPos>()
//        for (value in arguments.getTable(0).values) {
//            if (value !is Map<*, *>) throw LuaException("First argument should be list of block positions")
//            poses.add(LuaUtils.convertToBlockPos(center, value as Map<*, *>?))
//        }
//        val entities: MutableList<FlexibleRealityAnchorTileEntity> = ArrayList<FlexibleRealityAnchorTileEntity>()
//        for (pos in poses) {
//            if (!CheckUtils.radiusCorrect(center, pos, _getInteractionRadius())) return MethodResult.of(
//                null,
//                "One of blocks are too far away"
//            )
//            val entity: TileEntity = world.getBlockEntity(pos) as? FlexibleRealityAnchorTileEntity
//                ?: return MethodResult.of(
//                    false,
//                    java.lang.String.format("Incorrect coordinates (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ())
//                )
//            entities.add(entity as FlexibleRealityAnchorTileEntity)
//        }
//        val table = arguments.getTable(1)
//        val blockFindResult: Pair<Boolean, BlockState> = findBlock(table)
//        entities.forEach(Consumer<FlexibleRealityAnchorTileEntity> { entity: FlexibleRealityAnchorTileEntity ->
//            forgeRealityTileEntity(
//                entity,
//                blockFindResult.getRight(),
//                table,
//                blockFindResult.getLeft()
//            )
//        })
//        return MethodResult.of(true)
//    }
//
    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun forgeReality(arguments: IArguments): MethodResult {
        val table = arguments.getTable(0)
        val targetState = findBlock(table) ?: return MethodResult.of(null, "Hm, I need to implement change without block")
        if (level == null)
            return MethodResult.of(null, "Level is not loaded, what?")
        ScanUtils.traverseBlocks(level!!, pos, interactionRadius, {blockState, blockPos ->
            val blockEntity = level!!.getBlockEntity(blockPos)
            if (blockEntity is FlexibleRealityAnchorTileEntity) {
                forgeRealityTileEntity(blockEntity, targetState, table)
            }
        })
        return MethodResult.of(true)
    }
}