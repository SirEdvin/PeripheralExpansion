package site.siredvin.peripheralexpansion
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraftforge.api.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import site.siredvin.peripheralexpansion.common.configuration.ConfigHolder
import site.siredvin.peripheralexpansion.common.setup.BlockEntityTypes
import site.siredvin.peripheralexpansion.common.setup.Blocks
import site.siredvin.peripheralexpansion.common.setup.Items
import site.siredvin.peripheralexpansion.world.BlockStateUpdateEventBus


@Suppress("UNUSED")
object PeripheralExpansion: ModInitializer {
    const val MOD_ID = "peripheralexpansion"

    var LOGGER: Logger = LogManager.getLogger(MOD_ID)

    override fun onInitialize() {
        Blocks.doSomething()
        BlockEntityTypes.doSomething()
        Items.doSomething()
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC)
        registerCommands()
    }

    fun registerCommands() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<CommandSourceStack?>, dedicated: Boolean ->
            dispatcher.register(Commands.literal(MOD_ID).then(Commands.literal("state_update_debug").executes {
                BlockStateUpdateEventBus.postDebugLog()
                return@executes 1
            }))
        })
    }
}