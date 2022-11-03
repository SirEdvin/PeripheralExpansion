package site.siredvin.peripheralexpansion.common.configuration

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.peripheralium.api.IConfigHandler

object PeripheralExpansionConfig {

    val enableRemoteObserver: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_REMOTE_OBSERVER.get()

    val remoteObserverMaxRange: Int
        get() = ConfigHolder.COMMON_CONFIG.REMOTE_OBSERVER_MAX_RANGE.get()

    class CommonConfig internal constructor(builder: ForgeConfigSpec.Builder) {

        val ENABLE_REMOTE_OBSERVER: ForgeConfigSpec.BooleanValue
        val REMOTE_OBSERVER_MAX_RANGE: ForgeConfigSpec.IntValue

        init {
            builder.push("peripherals")
            ENABLE_REMOTE_OBSERVER = builder.comment("Enables remote observer peripheral")
                .define("enableRemoteObserver", true)
            builder.pop()
            builder.push("limitations")
            REMOTE_OBSERVER_MAX_RANGE = builder.comment("Defines max range for remote observer to work")
                .defineInRange("remoteObserverMaxRange", 8, 1, 64)
            builder.pop()
        }
    }
}