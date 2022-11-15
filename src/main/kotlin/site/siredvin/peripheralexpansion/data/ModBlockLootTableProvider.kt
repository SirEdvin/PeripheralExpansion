package site.siredvin.peripheralexpansion.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.world.item.Items
import site.siredvin.peripheralexpansion.common.setup.Blocks

class ModBlockLootTableProvider(dataGenerator: FabricDataGenerator) : FabricBlockLootTableProvider(dataGenerator) {
    override fun generateBlockLootTables() {
        dropSelf(Blocks.ITEM_READER_PEDESTAL)
        dropSelf(Blocks.REALITY_FORGER)
        dropSelf(Blocks.REMOTE_OBSERVER)
        dropSelf(Blocks.PERIPHERAL_CASING)
        dropOther(Blocks.FLEXIBLE_REALITY_ANCHOR, Items.AIR)
    }
}