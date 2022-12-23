package site.siredvin.peripheralexpansion.computercraft.peripheral

import site.siredvin.peripheralexpansion.common.blockentities.StatueWorkbenchBlockEntity
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.BlockEntityPeripheralOwner

class StatueWorkbenchPeripheral(blockEntity: StatueWorkbenchBlockEntity): OwnedPeripheral<BlockEntityPeripheralOwner<StatueWorkbenchBlockEntity>>(
TYPE, BlockEntityPeripheralOwner(blockEntity)
)  {
    companion object {
        const val TYPE = "statueWorkbench"
    }

    override val isEnabled: Boolean
        get() = true
}