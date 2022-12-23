package site.siredvin.peripheralexpansion.common.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import site.siredvin.peripheralexpansion.common.blockentities.StatueWorkbenchBlockEntity
import site.siredvin.peripheralium.common.blocks.BaseTileEntityBlock
import site.siredvin.peripheralium.util.BlockUtil

class StatueWorkbench : BaseTileEntityBlock<StatueWorkbenchBlockEntity>(
    false,
    BlockUtil.defaultProperties()
) {

    companion object {
        val CONNECTED =  BooleanProperty.create("connected")
    }

    init {
        registerDefaultState(this.stateDefinition.any().setValue(CONNECTED, false))
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return StatueWorkbenchBlockEntity(blockPos, blockState)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(CONNECTED)
    }

    override fun neighborChanged(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        neighbourBlock: Block,
        neighbourPos: BlockPos,
        bl: Boolean
    ) {
        if (blockPos.above().equals(neighbourPos)) {
            // TODO: implement Flexible Statue search logic
        }
        super.neighborChanged(blockState, level, blockPos, neighbourBlock, neighbourPos, bl)
    }

}