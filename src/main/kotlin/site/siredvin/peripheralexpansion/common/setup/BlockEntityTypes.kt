package site.siredvin.peripheralexpansion.common.setup

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.world.level.block.entity.BlockEntityType
import site.siredvin.peripheralexpansion.common.blockentities.*
import site.siredvin.peripheralexpansion.ext.register
import site.siredvin.peripheralium.common.blockentities.PeripheralBlockEntity

object BlockEntityTypes {
    val REMOTE_OBSERVER: BlockEntityType<RemoteObserverBlockEntity> = FabricBlockEntityTypeBuilder.create(
        { blockPos, blockState ->  RemoteObserverBlockEntity(blockPos, blockState)}, Blocks.REMOTE_OBSERVER
    ).build().register("remote_observer")
    val REALITY_FORGET: BlockEntityType<*> = FabricBlockEntityTypeBuilder.create(
        { blockPos, blockState ->  RealityForgerBlockEntity(blockPos, blockState)}, Blocks.REALITY_FORGER
    ).build().register("reality_forger")
    val FLEXIBLE_REALITY_ANCHOR: BlockEntityType<FlexibleRealityAnchorTileEntity> = FabricBlockEntityTypeBuilder.create(
        { blockPos, blockState ->  FlexibleRealityAnchorTileEntity(blockPos, blockState)}, Blocks.FLEXIBLE_REALITY_ANCHOR
    ).build().register("flexible_reality_anchor")
    val ITEM_READER = FabricBlockEntityTypeBuilder.create(
        {blockPos, blockState -> ItemReaderBlockEntity(blockPos, blockState)}, Blocks.ITEM_READER_PEDESTAL
    ).build().register("item_reader")
    val STATUE_WORKBENCH = FabricBlockEntityTypeBuilder.create(
        {blockPos, blockState -> StatueWorkbenchBlockEntity(blockPos, blockState)}, Blocks.STATUE_WORKBENCH
    ).build().register("statue_workbench")
    val FLEXIBLE_STATUE = FabricBlockEntityTypeBuilder.create(
        {blockPos, blockState -> FlexibleStatueBlockEntity(blockPos, blockState)}, Blocks.FLEXIBLE_STATUE
    ).build().register("flexible_statue")

    fun doSomething() {

    }
}