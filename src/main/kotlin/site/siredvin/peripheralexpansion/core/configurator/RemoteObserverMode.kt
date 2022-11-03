package site.siredvin.peripheralexpansion.core.configurator

import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import site.siredvin.peripheralexpansion.PeripheralExpansion
import site.siredvin.peripheralexpansion.common.blockentities.RemoteObserverBlockEntity
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
        val entity = level.getBlockEntity(targetBlock)
        if (entity !is RemoteObserverBlockEntity) {
            PeripheralExpansion.LOGGER.error("Remote observer configuration mode renderer process $targetBlock which is not remote observer")
            return InteractionResultHolder.consume(stack)
        }
        if (!entity.isPosApplicable(hit.blockPos)) {
            player.displayClientMessage(text(PeripheralExpansion.MOD_ID, "remote_observer_too_far"), true)
            return InteractionResultHolder.consume(stack)
        }
        if (entity.togglePos(hit.blockPos)) {
            player.displayClientMessage(text(PeripheralExpansion.MOD_ID, "remote_observer_block_track_added"), true)
        } else {
            player.displayClientMessage(text(PeripheralExpansion.MOD_ID, "remote_observer_block_track_removed"), true)
        }
        return InteractionResultHolder.consume(stack)
    }
}