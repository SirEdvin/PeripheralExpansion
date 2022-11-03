package site.siredvin.peripheralexpansion.common.setup

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.world.level.block.entity.BlockEntityType
import site.siredvin.peripheralexpansion.common.blockentities.FlexibleRealityAnchorTileEntity
import site.siredvin.peripheralexpansion.common.blockentities.RealityForgerBlockEntity
import site.siredvin.peripheralexpansion.common.blockentities.RemoteObserverBlockEntity
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

    fun doSomething() {

    }
}