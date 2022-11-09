package site.siredvin.peripheralexpansion.common.blocks

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.core.BlockPos
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import site.siredvin.peripheralexpansion.common.blockentities.ItemReaderBlockEntity
import site.siredvin.peripheralium.util.BlockUtil

class ItemReaderPedestal: BasePedestal<ItemReaderBlockEntity>(BlockUtil.defaultProperties()) {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return ItemReaderBlockEntity(blockPos, blockState)
    }

    override fun use(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        player: Player,
        interactionHand: InteractionHand,
        blockHitResult: BlockHitResult
    ): InteractionResult {
        val itemInHand = player.getItemInHand(interactionHand)
        if (!itemInHand.isEmpty) {
            val blockEntity= level.getBlockEntity(blockPos)
            if (blockEntity is ItemReaderBlockEntity) {
                if (!blockEntity.hasStoredStack) {
                    val transaction = Transaction.openOuter()
                    player.setItemInHand(interactionHand, ItemStack.EMPTY)
                    blockEntity.inventoryStorage.insert(ItemVariant.of(itemInHand), itemInHand.count.toLong(), transaction)
                    transaction.commit()
                    return InteractionResult.CONSUME
                }
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult)
    }

    override fun onRemove(blockState: BlockState, level: Level, blockPos: BlockPos, replace: BlockState, bl: Boolean) {
        if (blockState.block !== blockState.block) {
            val blockEntity = level.getBlockEntity(blockPos)
            if (blockEntity is ItemReaderBlockEntity) {
                if (blockEntity.hasStoredStack)
                    Containers.dropItemStack(level, blockPos.x.toDouble(),
                        blockPos.y.toDouble(), blockPos.z.toDouble(), blockEntity.storedStack)
            }
        }
        super.onRemove(blockState, level, blockPos, replace, bl)
    }

    override fun attack(blockState: BlockState, level: Level, blockPos: BlockPos, player: Player) {
        val itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND)
        if (itemInHand.isEmpty) {
            val blockEntity = level.getBlockEntity(blockPos)
            if (blockEntity is ItemReaderBlockEntity) {
                if (blockEntity.hasStoredStack) {
                    val transaction = Transaction.openOuter()
                    val storedStack = blockEntity.storedStack
                    player.setItemInHand(InteractionHand.MAIN_HAND, storedStack)
                    blockEntity.inventoryStorage.extract(ItemVariant.of(storedStack), storedStack.count.toLong(), transaction)
                    transaction.commit()
                }
            }
        }
        super.attack(blockState, level, blockPos, player)
    }
}