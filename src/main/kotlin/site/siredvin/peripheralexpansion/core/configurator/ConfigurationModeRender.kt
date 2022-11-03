package site.siredvin.peripheralexpansion.core.configurator

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.core.BlockPos

interface ConfigurationModeRender {
    fun render(source: BlockPos, context: WorldRenderContext)
}