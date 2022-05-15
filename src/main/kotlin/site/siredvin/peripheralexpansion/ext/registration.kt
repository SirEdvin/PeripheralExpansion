package site.siredvin.peripheralexpansion.ext

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import site.siredvin.peripheralexpansion.PeripheralExpansion
import site.siredvin.peripheralium.common.items.DescriptiveBlockItem
import site.siredvin.peripheralium.common.setup.Blocks
import site.siredvin.peripheralium.common.setup.Items

fun <T: Item> T.register(name: String): T {
    Registry.register(Registry.ITEM, ResourceLocation(PeripheralExpansion.MOD_ID, name), this)
    return this
}

fun <T: Block> T.register(name: String, itemFactory: (Block) -> (Item) = { block -> DescriptiveBlockItem(block, Item.Properties()) }): T {
    Registry.register(Registry.BLOCK, ResourceLocation(PeripheralExpansion.MOD_ID, name), this)
    Registry.register(Registry.ITEM, ResourceLocation(PeripheralExpansion.MOD_ID, name), itemFactory(this))
    return this
}

fun <T: BlockEntityType<*>> T.register(name: String): T {
    Registry.register(Registry.BLOCK_ENTITY_TYPE, ResourceLocation(PeripheralExpansion.MOD_ID, name), this)
    return this
}
