package site.siredvin.peripheralexpansion.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.shared.util.NBTUtil
import site.siredvin.peripheralexpansion.common.blockentities.ItemReaderBlockEntity
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.BlockEntityPeripheralOwner
import site.siredvin.peripheralium.extra.plugins.ItemStoragePlugin
import site.siredvin.peripheralium.util.representation.LuaRepresentation

class ItemReaderPeripheral(blockEntity: ItemReaderBlockEntity): OwnedPeripheral<BlockEntityPeripheralOwner<ItemReaderBlockEntity>>(
    TYPE, BlockEntityPeripheralOwner(blockEntity)
)  {
    companion object {
        val TYPE = "itemReader"
    }

    init {
        addPlugin(ItemStoragePlugin(blockEntity.level!!, blockEntity.inventoryStorage))
    }

    override val isEnabled: Boolean
        get() = true

    @LuaFunction(mainThread = true)
    fun itemAnalyze(): Map<String, Any> {
        if (!peripheralOwner.tileEntity.hasStoredStack)
            return emptyMap()
        val data = LuaRepresentation.forItemStack(peripheralOwner.tileEntity.storedStack)
        val tagData = NBTUtil.toLua(peripheralOwner.tileEntity.storedStack.tag)
        if (tagData != null)
            data["nbt"] = tagData
        return data
    }
}