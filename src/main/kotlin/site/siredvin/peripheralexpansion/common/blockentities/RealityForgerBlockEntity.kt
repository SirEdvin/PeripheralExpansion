package site.siredvin.peripheralexpansion.common.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.peripheralexpansion.common.setup.BlockEntityTypes
import site.siredvin.peripheralexpansion.computercraft.peripheral.RealityForgerPeripheral
import site.siredvin.peripheralium.common.blockentities.PeripheralBlockEntity

class RealityForgerBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    PeripheralBlockEntity<RealityForgerPeripheral>(BlockEntityTypes.REALITY_FORGET, blockPos, blockState){
    override fun createPeripheral(side: Direction): RealityForgerPeripheral {
        return RealityForgerPeripheral(this)
    }
}