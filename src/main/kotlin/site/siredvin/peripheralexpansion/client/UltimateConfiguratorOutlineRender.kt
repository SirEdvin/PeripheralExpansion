package site.siredvin.peripheralexpansion.client

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.Minecraft
import site.siredvin.peripheralexpansion.common.setup.Items
import site.siredvin.peripheralexpansion.core.configurator.ConfigurationModeRenderRegistry

@Environment(EnvType.CLIENT)
object UltimateConfiguratorOutlineRender {
    fun render(context: WorldRenderContext): Boolean {
        val minecraft = Minecraft.getInstance()
        val player = minecraft.player ?: return true
        val mainHandItem = player.mainHandItem
        if (mainHandItem.`is`(Items.ULTIMATE_CONFIGURATOR)) {
            val activeConfiguration = Items.ULTIMATE_CONFIGURATOR.getActiveMode(mainHandItem) ?: return true
            val modeRender = ConfigurationModeRenderRegistry.get(activeConfiguration.modeID) ?: return true
            modeRender.render(activeConfiguration.targetBlock, context)
        }
        return true
    }
}