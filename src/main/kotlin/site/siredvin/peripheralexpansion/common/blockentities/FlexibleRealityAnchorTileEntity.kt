package site.siredvin.peripheralexpansion.common.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BooleanProperty
import site.siredvin.peripheralexpansion.common.blocks.FlexibleRealityAnchor
import site.siredvin.peripheralexpansion.common.configuration.PeripheralExpansionConfig
import site.siredvin.peripheralexpansion.common.setup.BlockEntityTypes
import site.siredvin.peripheralium.common.blockentities.MutableNBTBlockEntity
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import kotlin.math.max
import kotlin.math.min

class FlexibleRealityAnchorTileEntity(blockPos: BlockPos, blockState: BlockState) :
    MutableNBTBlockEntity<OwnedPeripheral<*>>(BlockEntityTypes.FLEXIBLE_REALITY_ANCHOR, blockPos, blockState){

    companion object {
        private val MIMIC_TAG = "mimic"
        private val LIGHT_LEVEL_TAG = "lightLevel"
    }

    private var _mimic: BlockState? = null
    private var _lightLevel = 0
    private var pendingState: BlockState? = null

    val mimic: BlockState?
        get() = this._mimic

    var lightLevel: Int
        get() = this._lightLevel
        set(value) {
            this._lightLevel = max(0, min(value, 15))
        }

    fun setMimic(mimic: BlockState?, state: BlockState? = null, skipUpdate: Boolean = false) {
        val realState = state ?: pendingState ?: blockState
        if (mimic != null) {
            val blockName: ResourceLocation = Registry.BLOCK.getKey(mimic.block)
            if (blockName == Registry.BLOCK.defaultKey || PeripheralExpansionConfig.realityForgerBlockList.contains(blockName.toString()))
                return
        }
        this._mimic = mimic
        if (!skipUpdate) {
            pushInternalDataChangeToClient(realState.setValue(FlexibleRealityAnchor.CONFIGURED, mimic != null))
        } else {
            if (pendingState == null) pendingState = realState
            pendingState = pendingState!!.setValue(FlexibleRealityAnchor.CONFIGURED, mimic != null)
        }
    }

    override fun getPeripheral(side: Direction): OwnedPeripheral<*>? {
        return null
    }

    override fun createPeripheral(side: Direction): OwnedPeripheral<*> {
        throw IllegalCallerException("You should not call this function at all")
    }

    override fun pushInternalDataChangeToClient(state: BlockState?) {
        super.pushInternalDataChangeToClient(state ?: pendingState)
    }

    override fun loadInternalData(data: CompoundTag, state: BlockState?): BlockState {
        if (data.contains(MIMIC_TAG))
            setMimic(NbtUtils.readBlockState(data.getCompound(MIMIC_TAG)), state ?: blockState, true)
        if (data.contains(LIGHT_LEVEL_TAG))
            this.lightLevel = data.getInt(LIGHT_LEVEL_TAG)
        return pendingState!!
    }

    override fun saveInternalData(data: CompoundTag): CompoundTag {
        if (_mimic != null) {
            data.put(MIMIC_TAG, NbtUtils.writeBlockState(_mimic!!))
        }
        if (lightLevel != 0) data.putInt(LIGHT_LEVEL_TAG, lightLevel)
        return data
    }

    fun setBooleanStateValue(stateValue: BooleanProperty, value: Boolean) {
        if (pendingState == null) pendingState = blockState
        pendingState = pendingState!!.setValue(stateValue, value)
    }
}