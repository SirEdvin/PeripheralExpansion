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

interface ConfiguratorMode {
    val modeID: ResourceLocation
    val targetBlock: BlockPos
    val description: Component
    fun onBlockClick(stack: ItemStack, player: Player, hit: BlockHitResult, level: Level): InteractionResultHolder<ItemStack> {
        return InteractionResultHolder.pass(stack)
    }
    fun onEntityClick(stack: ItemStack, player: Player, hit: EntityHitResult, level: Level): InteractionResultHolder<ItemStack> {
        return InteractionResultHolder.pass(stack)
    }
}