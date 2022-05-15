package site.siredvin.peripheralexpansion.common.configuration

import net.minecraftforge.common.ForgeConfigSpec

object ConfigHolder {
    var COMMON_SPEC: ForgeConfigSpec
    var COMMON_CONFIG: PeripheralExpansionConfig.CommonConfig

    init {
        val (key, value) = ForgeConfigSpec.Builder()
            .configure { builder: ForgeConfigSpec.Builder -> PeripheralExpansionConfig.CommonConfig(builder) }
        COMMON_CONFIG = key
        COMMON_SPEC = value
    }
}