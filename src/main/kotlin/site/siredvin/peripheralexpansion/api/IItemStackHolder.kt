package site.siredvin.peripheralexpansion.api

import net.minecraft.world.item.ItemStack

interface IItemStackHolder {
    val storedStack: ItemStack
}