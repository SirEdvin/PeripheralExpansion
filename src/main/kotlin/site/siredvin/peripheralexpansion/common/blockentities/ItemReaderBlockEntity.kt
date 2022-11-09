package site.siredvin.peripheralexpansion.common.blockentities

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.peripheralexpansion.api.IItemStackHolder
import site.siredvin.peripheralexpansion.common.setup.BlockEntityTypes
import site.siredvin.peripheralexpansion.computercraft.peripheral.ItemReaderPeripheral
import site.siredvin.peripheralium.common.blockentities.MutableNBTBlockEntity

class ItemReaderBlockEntity(blockPos: BlockPos, blockState: BlockState) : MutableNBTBlockEntity<ItemReaderPeripheral>(
    BlockEntityTypes.ITEM_READER, blockPos, blockState
), IItemStackHolder {

    companion object {
        private const val STORED_ITEM_STACK_TAG = "storedItemStack"
    }

    private class ExtraSimpleStorage(private val itemReaderBlockEntity: ItemReaderBlockEntity): SimpleContainer(1) {
        override fun setChanged() {
            val side = if(itemReaderBlockEntity.level == null) {
                "ugly"
            }else if (itemReaderBlockEntity.level!!.isClientSide){
                "client"
            } else {
                "server"
            }
            println("Set changed called for ${itemReaderBlockEntity.blockPos} from $side, inventory ${this.getItem(0)}")
            itemReaderBlockEntity.pushInternalDataChangeToClient()
        }
    }

    private val inventory = ExtraSimpleStorage(this)
    val inventoryStorage: InventoryStorage = InventoryStorage.of(inventory, null)

    override val storedStack: ItemStack
        get() {
            val item = inventoryStorage.getSlot(0)
            return item.resource.toStack(item.amount.toInt())
        }

    val hasStoredStack: Boolean
        get() = !inventoryStorage.getSlot(0).isResourceBlank

    override fun createPeripheral(side: Direction): ItemReaderPeripheral {
        return ItemReaderPeripheral(this)
    }

    override fun loadInternalData(data: CompoundTag, state: BlockState?): BlockState {
        val side = if(level == null) {
            "ugly"
        }else if (level!!.isClientSide){
            "client"
        } else {
            "server"
        }
        if (data.contains(STORED_ITEM_STACK_TAG)) {
            val itemList = data.getList(STORED_ITEM_STACK_TAG, 10)
            if (itemList.isEmpty()) {
                inventory.clearContent()
            } else {
                inventory.fromTag(itemList)
            }
        }

        println("Load internal data called for $blockPos from $side, inventory ${this.inventory.getItem(0)}")
        return state ?: blockState
    }

    override fun saveInternalData(data: CompoundTag): CompoundTag {
        data.put(STORED_ITEM_STACK_TAG, inventory.createTag())
        val side = if(level == null) {
            "ugly"
        }else if (level!!.isClientSide){
            "client"
        } else {
            "server"
        }
        println("Save internal data called for $blockPos from $side, inventory ${this.inventory.getItem(0)}")
        return data
    }
}