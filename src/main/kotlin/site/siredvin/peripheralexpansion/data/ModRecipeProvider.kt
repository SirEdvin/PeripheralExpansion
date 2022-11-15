package site.siredvin.peripheralexpansion.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.recipes.FinishedRecipe
import site.siredvin.peripheralexpansion.common.setup.Blocks
import site.siredvin.peripheralexpansion.common.setup.Items
import site.siredvin.peripheralium.data.TweakedShapedRecipeBuilder
import java.util.function.Consumer

class ModRecipeProvider(dataGenerator: FabricDataGenerator) : FabricRecipeProvider(dataGenerator) {
    override fun generateRecipes(consumer: Consumer<FinishedRecipe>) {

        TweakedShapedRecipeBuilder.shaped(Blocks.PERIPHERAL_CASING)
            .define('B', site.siredvin.peripheralium.common.setup.Blocks.PERIPHERALIUM_BLOCK)
            .define('R', net.minecraft.world.item.Items.REDSTONE)
            .define('P', net.minecraft.world.item.Items.ENDER_PEARL)
            .pattern("RPR")
            .pattern(" B ")
            .pattern("R R")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.ULTIMATE_CONFIGURATOR)
            .define('I', site.siredvin.peripheralium.common.setup.Items.PERIPHERALIUM_DUST)
            .define('S', net.minecraft.world.item.Items.STICK)
            .define('D', net.minecraft.world.item.Items.DIAMOND)
            .pattern(" ID")
            .pattern(" SI")
            .pattern("S  ")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Blocks.FLEXIBLE_REALITY_ANCHOR, 64)
            .define('P', Blocks.PERIPHERAL_CASING)
            .define('G', net.minecraft.world.level.block.Blocks.GLASS)
            .define('D', site.siredvin.peripheralium.common.setup.Items.PERIPHERALIUM_DUST)
            .pattern("DGD")
            .pattern("GPG")
            .pattern("DGD")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Blocks.REALITY_FORGER)
            .define('P', Blocks.PERIPHERAL_CASING)
            .define('S', Blocks.FLEXIBLE_REALITY_ANCHOR)
            .define('D', net.minecraft.world.item.Items.DIAMOND)
            .pattern("SDS")
            .pattern("SPS")
            .pattern("SSS")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Blocks.ITEM_READER_PEDESTAL)
            .define('P', Blocks.PERIPHERAL_CASING)
            .define('S', net.minecraft.world.level.block.Blocks.STONE_SLAB)
            .pattern(" P ")
            .pattern(" P ")
            .pattern("SSS")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Blocks.REMOTE_OBSERVER)
            .define('P', Blocks.PERIPHERAL_CASING)
            .define('R', net.minecraft.world.level.block.Blocks.DAYLIGHT_DETECTOR)
            .define('O', net.minecraft.world.level.block.Blocks.OBSERVER)
            .define('S', net.minecraft.world.level.block.Blocks.STONE_SLAB)
            .pattern(" R ")
            .pattern("OPO")
            .pattern("SSS")
            .save(consumer)
    }
}