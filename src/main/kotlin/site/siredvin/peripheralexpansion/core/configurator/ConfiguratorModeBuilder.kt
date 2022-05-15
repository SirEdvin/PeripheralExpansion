package site.siredvin.peripheralexpansion.core.configurator

import net.minecraft.core.BlockPos

fun interface ConfiguratorModeBuilder {
    fun build(target: BlockPos): ConfiguratorMode
}