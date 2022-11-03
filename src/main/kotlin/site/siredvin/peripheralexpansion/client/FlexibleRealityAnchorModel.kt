package site.siredvin.peripheralexpansion.client

import com.mojang.datafixers.util.Pair
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.*
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.peripheralexpansion.common.blockentities.FlexibleRealityAnchorTileEntity
import java.util.*
import java.util.function.Function
import java.util.function.Supplier


@Environment(EnvType.CLIENT)
class FlexibleRealityAnchorModel: UnbakedModel, BakedModel, FabricBakedModel {

    override fun getQuads(blockState: BlockState?, direction: Direction?, random: Random): MutableList<BakedQuad> {
        return mutableListOf()
    }

    override fun useAmbientOcclusion(): Boolean {
        return true
    }

    override fun isGui3d(): Boolean {
        return false
    }

    override fun usesBlockLight(): Boolean {
        return false
    }

    override fun isCustomRenderer(): Boolean {
        return false
    }

    override fun getParticleIcon(): TextureAtlasSprite {
        return (Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
            .apply(ResourceLocation("minecraft:block/stone")) as TextureAtlasSprite)
    }

    override fun getTransforms(): ItemTransforms {
        return ModelHelper.MODEL_TRANSFORM_BLOCK
    }

    override fun getOverrides(): ItemOverrides {
        return ItemOverrides.EMPTY
    }

    override fun isVanillaAdapter(): Boolean {
        return false
    }

    override fun emitBlockQuads(
        blockView: BlockAndTintGetter,
        state: BlockState,
        pos: BlockPos,
        randomSupplier: Supplier<Random>,
        context: RenderContext
    ) {
        val entity = blockView.getBlockEntity(pos)
        if (entity !is FlexibleRealityAnchorTileEntity)
            return
        val mimicBlockState = entity.mimic ?: return
        val bakedModel = Minecraft.getInstance().blockRenderer.getBlockModel(mimicBlockState)
        context.fallbackConsumer().accept(bakedModel)
    }

    override fun emitItemQuads(stack: ItemStack?, randomSupplier: Supplier<Random>?, context: RenderContext?) {
        TODO("Not yet implemented")
    }

    override fun getDependencies(): MutableCollection<ResourceLocation> {
        return mutableListOf()
    }

    override fun getMaterials(
        function: Function<ResourceLocation, UnbakedModel>,
        set: MutableSet<Pair<String, String>>
    ): MutableCollection<Material> {
        return mutableListOf()
    }

    override fun bake(
        modelBakery: ModelBakery,
        function: Function<Material, TextureAtlasSprite>,
        modelState: ModelState,
        resourceLocation: ResourceLocation
    ): BakedModel {
        return this
    }
}