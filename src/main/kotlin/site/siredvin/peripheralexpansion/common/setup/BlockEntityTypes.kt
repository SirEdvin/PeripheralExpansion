package site.siredvin.peripheralexpansion.common.setup

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.world.level.block.entity.BlockEntityType
import site.siredvin.peripheralexpansion.common.blockentities.RemoteObserverBlockEntity
import site.siredvin.peripheralexpansion.ext.register

object BlockEntityTypes {
    val REMOTE_OBSERVER: BlockEntityType<RemoteObserverBlockEntity> = FabricBlockEntityTypeBuilder.create(
        { blockPos, blockState ->  RemoteObserverBlockEntity(blockPos, blockState)}, Blocks.REMOTE_OBSERVER
    ).build().register("remote_observer")

    fun doSomething() {

    }
}