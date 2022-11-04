package site.siredvin.peripheralexpansion.common.configuration

import net.minecraft.core.Registry
import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.peripheralexpansion.common.setup.Blocks
import java.util.function.Predicate

object PeripheralExpansionConfig {

    private val DEFAULT_REALITY_FORGER_BLACKLIST: List<String> = listOf(
        Registry.BLOCK.getKey(Blocks.FLEXIBLE_REALITY_ANCHOR).toString()
    )

    val enableRemoteObserver: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_REMOTE_OBSERVER.get()
    val enableRealityForger: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_REALITY_FORGER.get()

    val remoteObserverMaxRange: Int
        get() = ConfigHolder.COMMON_CONFIG.REMOTE_OBSERVER_MAX_RANGE.get()
    val realityForgerMaxRange: Int
        get() = ConfigHolder.COMMON_CONFIG.REALITY_FORGER_MAX_RANGE.get()
    val realityForgerBlockList: List<String>
        get() = ConfigHolder.COMMON_CONFIG.REALITY_FORGER_BLOCKLIST.get()

    class CommonConfig internal constructor(builder: ForgeConfigSpec.Builder) {

        val ENABLE_REMOTE_OBSERVER: ForgeConfigSpec.BooleanValue
        val ENABLE_REALITY_FORGER: ForgeConfigSpec.BooleanValue


        val REMOTE_OBSERVER_MAX_RANGE: ForgeConfigSpec.IntValue
        val REALITY_FORGER_MAX_RANGE: ForgeConfigSpec.IntValue
        val REALITY_FORGER_BLOCKLIST: ForgeConfigSpec.ConfigValue<List<String>>

        init {
            builder.push("peripherals")
            ENABLE_REMOTE_OBSERVER = builder.comment("Enables remote observer peripheral")
                .define("enableRemoteObserver", true)
            ENABLE_REALITY_FORGER = builder.comment("Enables reality forger peripheral")
                .define("enableRealityForger", true)
            builder.pop()
            builder.push("limitations")
            REMOTE_OBSERVER_MAX_RANGE = builder.comment("Defines max range for remote observer to work")
                .defineInRange("remoteObserverMaxRange", 8, 1, 64)
            REALITY_FORGER_MAX_RANGE = builder.comment("Defines max range for reality forger to work")
                .defineInRange("realityForgerMaxRange", 16, 1, 64)
            REALITY_FORGER_BLOCKLIST =
                builder.comment("Any block, that has tweak somehow it own model logic, should be here.")
                    .defineList(
                        "realityForgerBlocklist",
                        DEFAULT_REALITY_FORGER_BLACKLIST
                    ) { true }
            builder.pop()
        }
    }
}