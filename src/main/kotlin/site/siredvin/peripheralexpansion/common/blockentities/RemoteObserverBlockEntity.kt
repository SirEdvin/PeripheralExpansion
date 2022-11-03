package site.siredvin.peripheralexpansion.common.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.Tag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.peripheralexpansion.common.configuration.PeripheralExpansionConfig
import site.siredvin.peripheralexpansion.common.setup.BlockEntityTypes
import site.siredvin.peripheralexpansion.computercraft.peripheral.RemoteObserverPeripheral
import site.siredvin.peripheralexpansion.world.BlockStateUpdateEventBus
import site.siredvin.peripheralium.api.blockentities.IObservingBlockEntity
import site.siredvin.peripheralium.common.blockentities.MutableNBTBlockEntity
import site.siredvin.peripheralium.common.blocks.GenericBlockEntityBlock
import site.siredvin.peripheralium.util.representation.LuaRepresentation
import site.siredvin.peripheralium.util.representation.stateProperties

class RemoteObserverBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    MutableNBTBlockEntity<RemoteObserverPeripheral>(BlockEntityTypes.REMOTE_OBSERVER, blockPos, blockState), IObservingBlockEntity {

    companion object {
        const val TRACKED_BLOCKS_TAG = "trackedBlocks"
    }

    private var lastConsumedEventID: Long = BlockStateUpdateEventBus.lastEventID
    private val trackedBlocks: MutableList<BlockPos> = mutableListOf()

    private val facing: Direction
        get() = blockState.getValue(GenericBlockEntityBlock.FACING)

    val trackedBlocksView: List<BlockPos>
        get() = trackedBlocks

    override fun createPeripheral(side: Direction): RemoteObserverPeripheral {
        return RemoteObserverPeripheral(this)
    }

    fun isPosApplicable(pos: BlockPos): Boolean {
        return pos.closerThan(this.blockPos, PeripheralExpansionConfig.remoteObserverMaxRange.toDouble())
    }

    /**
     * Returns true if block was added to tracking and false if removed
     */
    fun togglePos(pos: BlockPos): Boolean {
        val blockAdded = if (trackedBlocks.contains(pos)) {
            removePosToTrack(pos)
            false
        } else {
            addPosToTrack(pos)
            true
        }
        pushInternalDataChangeToClient()
        return blockAdded
    }

    fun addPosToTrack(pos: BlockPos) {
        trackedBlocks.add(pos)
        BlockStateUpdateEventBus.addBlockPos(pos)
    }

    fun removePosToTrack(pos: BlockPos) {
        if (trackedBlocks.remove(pos))
            BlockStateUpdateEventBus.removeBlockPos(pos)
    }

    private fun unload() {
        BlockStateUpdateEventBus.removeBlockPos(trackedBlocks)
    }

    override fun saveInternalData(data: CompoundTag): CompoundTag {
        val trackedBlockTag = ListTag()
        trackedBlocks.forEach { trackedBlockTag.add(NbtUtils.writeBlockPos(it)) }
        data.put(TRACKED_BLOCKS_TAG, trackedBlockTag)
        return data
    }

    override fun loadInternalData(data: CompoundTag) {
        if (data.contains(TRACKED_BLOCKS_TAG)) {
            val internalList = data.getList(TRACKED_BLOCKS_TAG, Tag.TAG_COMPOUND.toInt())
            internalList.forEach { addPosToTrack(NbtUtils.readBlockPos(it as CompoundTag)) }
        }
    }

    override fun handleTick(level: Level, pos: BlockPos, state: BlockState) {
        lastConsumedEventID = BlockStateUpdateEventBus.traverseEvents(lastConsumedEventID) {
            connectedComputers.forEach { computer ->
                val previousState = LuaRepresentation.forBlockState(it.previous)
                val currentState = LuaRepresentation.forBlockState(it.current)
                stateProperties(it.previous, previousState)
                stateProperties(it.current, currentState)
                computer.queueEvent(
                    "block_change", LuaRepresentation.forBlockPos(it.pos, facing, blockPos),
                    previousState, currentState
                )
            }
        }
    }

    override fun setRemoved() {
        unload()
        super.setRemoved()
    }

    override fun onChunkUnloaded() {
        unload()
    }

    override fun destroy() {
        unload()
    }
}