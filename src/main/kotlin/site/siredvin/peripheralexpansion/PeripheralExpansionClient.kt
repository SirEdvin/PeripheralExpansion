package site.siredvin.peripheralexpansion

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import site.siredvin.peripheralexpansion.client.UltimateConfiguratorOutlineRender

object PeripheralExpansionClient: ClientModInitializer {
    override fun onInitializeClient() {
//        WorldRenderEvents.AFTER_TRANSLUCENT.register(UltimateConfiguratorOutlineRender::render)
    }
}