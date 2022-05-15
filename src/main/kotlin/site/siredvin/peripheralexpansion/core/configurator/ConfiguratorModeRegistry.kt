package site.siredvin.peripheralexpansion.core.configurator

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.peripheralexpansion.common.setup.Blocks
import java.util.function.Predicate

object ConfiguratorModeRegistry {
    private val REGISTRY = mutableMapOf<ResourceLocation, ConfiguratorModeBuilder>()
    private val CONDITION_REGISTRY = mutableMapOf<Predicate<BlockState>, ResourceLocation>()

    init {
        register(RemoteObserverMode.MODE_ID, { RemoteObserverMode(it) }, {it.`is`(Blocks.REMOTE_OBSERVER)})
    }

    fun register(modeID: ResourceLocation, builder: ConfiguratorModeBuilder, condition: Predicate<BlockState>) {
        REGISTRY[modeID] = builder
        CONDITION_REGISTRY[condition] = modeID
    }

    fun get(modeID: ResourceLocation): ConfiguratorModeBuilder? {
        return REGISTRY[modeID]
    }

    fun get(state: BlockState): ConfiguratorModeBuilder? {
        CONDITION_REGISTRY.forEach { (predicate, modID) ->
            if (predicate.test(state))
                return get(modID)
        }
        return null
    }
}