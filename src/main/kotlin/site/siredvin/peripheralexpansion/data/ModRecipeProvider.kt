package site.siredvin.peripheralexpansion.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmeltingRecipe
import site.siredvin.peripheralium.common.setup.Blocks
import site.siredvin.peripheralium.common.setup.Items
import java.util.function.Consumer

class ModRecipeProvider(dataGenerator: FabricDataGenerator) : FabricRecipeProvider(dataGenerator) {
    override fun generateRecipes(consumer: Consumer<FinishedRecipe>) {
    }
}