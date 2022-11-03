package site.siredvin.peripheralexpansion.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import site.siredvin.peripheralexpansion.common.blockentities.RemoteObserverBlockEntity
import site.siredvin.peripheralexpansion.common.configuration.PeripheralExpansionConfig
import site.siredvin.peripheralium.common.blocks.GenericBlockEntityBlock
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.BlockEntityPeripheralOwner
import site.siredvin.peripheralium.ext.toRelative
import site.siredvin.peripheralium.util.representation.LuaInterpretation
import site.siredvin.peripheralium.util.representation.LuaRepresentation

class RemoteObserverPeripheral(
    blockEntity: RemoteObserverBlockEntity
) : OwnedPeripheral<BlockEntityPeripheralOwner<RemoteObserverBlockEntity>>(TYPE, BlockEntityPeripheralOwner(blockEntity)) {
    companion object {
        const val TYPE = "remote_observer"
    }
    override val isEnabled: Boolean
        get() = PeripheralExpansionConfig.enableRemoteObserver

    override val peripheralConfiguration: MutableMap<String, Any>
        get() {
            val base = super.peripheralConfiguration
            base["maxRange"] = PeripheralExpansionConfig.remoteObserverMaxRange
            return base
        }

    @LuaFunction(mainThread = true)
    fun addPosition(pos: Map<*, *>): MethodResult {
        val targetPos = LuaInterpretation.asBlockPos(peripheralOwner.pos, pos, peripheralOwner.tileEntity.blockState.getValue(GenericBlockEntityBlock.FACING))
        if (!peripheralOwner.tileEntity.isPosApplicable(targetPos))
            return MethodResult.of(false, "Position too far away")
        peripheralOwner.tileEntity.addPosToTrack(targetPos)
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    fun removePosition(pos: Map<*, *>): MethodResult {
        peripheralOwner.tileEntity.removePosToTrack(
            LuaInterpretation.asBlockPos(peripheralOwner.pos, pos, peripheralOwner.tileEntity.blockState.getValue(GenericBlockEntityBlock.FACING))
        )
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    fun getPositions(): List<Map<String, Any>> {
        return peripheralOwner.tileEntity.trackedBlocksView.map {
            LuaRepresentation.forBlockPos(
                it,
                peripheralOwner.tileEntity.blockState.getValue(GenericBlockEntityBlock.FACING),
                peripheralOwner.pos
            )
        }
    }
}