package site.siredvin.peripheralexpansion.core.configurator

import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import site.siredvin.peripheralexpansion.PeripheralExpansion
import site.siredvin.peripheralium.util.text

class RemoteObserverMode(override val targetBlock: BlockPos): ConfiguratorMode {
    companion object {
        val MODE_ID = ResourceLocation(PeripheralExpansion.MOD_ID, "remote_observer")
    }
    override val modeID: ResourceLocation
        get() = MODE_ID

    override val description: Component
        get() = text(PeripheralExpansion.MOD_ID, "remote_observer_mode")

    override fun onBlockClick(stack: ItemStack, player: Player, hit: BlockHitResult, level: Level): InteractionResultHolder<ItemStack> {
        if (level.isClientSide)
            return InteractionResultHolder.consume(stack)
        PeripheralExpansion.LOGGER.info("Clicked on ${hit.blockPos}")
        return InteractionResultHolder.consume(stack)
    }
}