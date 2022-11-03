package site.siredvin.peripheralexpansion.common.items

import net.minecraft.nbt.NbtUtils
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.peripheralexpansion.PeripheralExpansion
import site.siredvin.peripheralexpansion.core.configurator.ConfiguratorMode
import site.siredvin.peripheralexpansion.core.configurator.ConfiguratorModeRegistry
import site.siredvin.peripheralium.common.items.DescriptiveItem
import site.siredvin.peripheralium.util.text

class UltimateConfigurator: DescriptiveItem(Properties().stacksTo(1)) {

    companion object {
        const val ACTIVE_MOD_NAME = "activeMod"
        const val ACTIVE_MOD_POS = "activeModPos"
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        val activeMode = getActiveMode(itemStack)
        if (activeMode != null) {
            list.add(text(PeripheralExpansion.MOD_ID, "active_configuration_mode"))
            list.add(activeMode.description)
            list.add(text(PeripheralExpansion.MOD_ID, "configuration_target_block", activeMode.targetBlock.toString()))
        }
    }

    fun getActiveMode(stack: ItemStack): ConfiguratorMode? {
        val data = stack.tag ?: return null
        if (!data.contains(ACTIVE_MOD_NAME))
            return null
        val modeBuilder = ConfiguratorModeRegistry.get(ResourceLocation(data.getString(ACTIVE_MOD_NAME))) ?: return null
        return modeBuilder.build(NbtUtils.readBlockPos(data.getCompound(ACTIVE_MOD_POS)))
    }

    private fun saveActiveMode(stack: ItemStack, mode: ConfiguratorMode) {
        val data = stack.orCreateTag
        data.putString(ACTIVE_MOD_NAME, mode.modeID.toString())
        data.put(ACTIVE_MOD_POS, NbtUtils.writeBlockPos(mode.targetBlock))
    }

    private fun clearActiveMode(stack: ItemStack): ItemStack {
        val data = stack.tag ?: return stack
        data.remove(ACTIVE_MOD_NAME)
        data.remove(ACTIVE_MOD_POS)
        return stack
    }

    private fun tryActivateMode(stack: ItemStack, player: Player, hit: BlockHitResult, level: Level): InteractionResultHolder<ItemStack> {
        if (player.pose == Pose.CROUCHING) {
            val targetState = level.getBlockState(hit.blockPos)
            val possibleModeBuilder = ConfiguratorModeRegistry.get(targetState)
            if (possibleModeBuilder != null) {
                val activeMode = possibleModeBuilder.build(hit.blockPos)
                saveActiveMode(stack, activeMode)
            }
        }
        return InteractionResultHolder.consume(stack)
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (interactionHand == InteractionHand.OFF_HAND)
            return super.use(level, player, interactionHand)
        val itemStack = player.getItemInHand(interactionHand)
        val blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE)
        return if (blockHitResult.type == HitResult.Type.MISS) {
            if (player.pose == Pose.CROUCHING) {
                InteractionResultHolder.consume(clearActiveMode(itemStack))
            } else {
                InteractionResultHolder.pass(itemStack)
            }
        } else {
            val activeMode = getActiveMode(itemStack) ?: return tryActivateMode(itemStack, player, blockHitResult, level)
            return activeMode.onBlockClick(itemStack, player, blockHitResult, level)
        }
    }

    override fun isFoil(itemStack: ItemStack): Boolean {
        return getActiveMode(itemStack) != null
    }
}