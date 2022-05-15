package site.siredvin.peripheralexpansion.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import site.siredvin.peripheralexpansion.common.blockentities.RemoteObserverBlockEntity
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.BlockEntityPeripheralOwner
import site.siredvin.peripheralium.util.representation.LuaInterpretation

class RemoteObserverPeripheral(
    blockEntity: RemoteObserverBlockEntity
) : OwnedPeripheral<BlockEntityPeripheralOwner<RemoteObserverBlockEntity>>(TYPE, BlockEntityPeripheralOwner(blockEntity)) {
    companion object {
        const val TYPE = "remote_observer"
    }
    override val isEnabled: Boolean
        get() = true

    @LuaFunction(mainThread = true)
    fun addPos(pos: Map<*, *>) {
        peripheralOwner.tileEntity.addPosToTrack(LuaInterpretation.asBlockPos(peripheralOwner.pos, pos))
    }

    @LuaFunction(mainThread = true)
    fun removePos(pos: Map<*, *>) {
        peripheralOwner.tileEntity.removePosToTrack(LuaInterpretation.asBlockPos(peripheralOwner.pos, pos))
    }
}