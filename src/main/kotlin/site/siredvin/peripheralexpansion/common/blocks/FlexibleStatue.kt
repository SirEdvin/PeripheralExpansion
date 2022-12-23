package site.siredvin.peripheralexpansion.common.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import site.siredvin.peripheralexpansion.common.blockentities.FlexibleStatueBlockEntity
import site.siredvin.peripheralexpansion.common.setup.Blocks
import site.siredvin.peripheralium.common.blocks.BaseNBTBlock
import site.siredvin.peripheralium.util.BlockUtil

class FlexibleStatue() :
    BaseNBTBlock<FlexibleStatueBlockEntity>(false, BlockUtil.decoration().dynamicShape()) {
    companion object {
        val CONFIGURED: BooleanProperty = BooleanProperty.create("configured")
    }

    init {
        registerDefaultState(getStateDefinition().any().setValue(CONFIGURED, false))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(CONFIGURED)
    }

    override fun createItemStack(): ItemStack {
        return ItemStack(Blocks.FLEXIBLE_STATUE.asItem())
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return FlexibleStatueBlockEntity(blockPos, blockState)
    }

    protected fun getDefaultShape(world: BlockGetter, pos: BlockPos): VoxelShape? {
        val blockEntity = world.getBlockEntity(pos) as FlexibleStatueBlockEntity?
        if (blockEntity != null) {
            return blockEntity.blockShape
        }
        return null
    }

    override fun getShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext
    ): VoxelShape {
        return getDefaultShape(blockGetter, blockPos) ?: super.getShape(blockState, blockGetter, blockPos, collisionContext)
    }

    override fun useShapeForLightOcclusion(blockState: BlockState): Boolean {
        return true
    }

    override fun getLightBlock(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Int {
        val blockEntity = blockGetter.getBlockEntity(blockPos)
        return if (blockEntity is FlexibleStatueBlockEntity) blockEntity.lightLevel else 0
    }
}