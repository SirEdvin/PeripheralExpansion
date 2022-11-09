package site.siredvin.peripheralexpansion

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.minecraft.client.renderer.RenderType
import site.siredvin.peripheralexpansion.client.ExpansionModelProvider
import site.siredvin.peripheralexpansion.client.renderer.PedestalTileRenderer
import site.siredvin.peripheralexpansion.common.setup.BlockEntityTypes
import site.siredvin.peripheralexpansion.common.setup.Blocks

object PeripheralExpansionClient: ClientModInitializer {
    override fun onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.FLEXIBLE_REALITY_ANCHOR, RenderType.translucent())
//        WorldRenderEvents.AFTER_TRANSLUCENT.register(UltimateConfiguratorOutlineRender::render)
        ModelLoadingRegistry.INSTANCE.registerResourceProvider { ExpansionModelProvider() }
        BlockEntityRendererRegistry.register(BlockEntityTypes.ITEM_READER) { PedestalTileRenderer() }
    }
}