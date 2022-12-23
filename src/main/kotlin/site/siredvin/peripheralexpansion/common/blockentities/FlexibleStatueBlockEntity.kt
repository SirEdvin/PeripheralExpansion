package site.siredvin.peripheralexpansion.common.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.VoxelShape
import site.siredvin.peripheralexpansion.common.blocks.FlexibleStatue
import site.siredvin.peripheralexpansion.common.setup.BlockEntityTypes
import site.siredvin.peripheralexpansion.util.PENBTUtil
import site.siredvin.peripheralexpansion.util.QuadList
import site.siredvin.peripheralium.api.peripheral.IOwnedPeripheral
import site.siredvin.peripheralium.common.blockentities.MutableNBTBlockEntity
import kotlin.math.max
import kotlin.math.min

class FlexibleStatueBlockEntity(blockPos: BlockPos, blockState: BlockState):
    MutableNBTBlockEntity<IOwnedPeripheral<*>>(BlockEntityTypes.FLEXIBLE_STATUE, blockPos, blockState) {

    companion object {
        const val BAKED_QUADS_TAG = "bakedQuads"
        const val NAME_TAG = "statueName"
        const val AUTHOR_TAG = "statueAuthor"
        const val LIGHT_LEVEL_TAG = "lightLevel"
    }

    private var pendingState: BlockState? = null
    private var _lightLevel = 0
    private var _bakedQuads: QuadList? = null
    private var _blockShape: VoxelShape? = null
    private var _name: String? = null
    private var _author: String? = null


    var name: String?
        get() = _name
        set(value) {
            _name = value
        }

    var author: String?
        get() = _author
        set(value) {
            _author = value
        }

    var lightLevel: Int
        get() = _lightLevel
        set(value) {
            _lightLevel = max(0, min(value, 15))
        }

    val blockShape: VoxelShape?
        get() = _blockShape

    val bakedQuads: QuadList?
        get() = _bakedQuads

    fun clear(state: BlockState, skipUpdate: Boolean) {
        refreshShape()
        if (!skipUpdate) {
            pushInternalDataChangeToClient(state.setValue(FlexibleStatue.CONFIGURED, false))
        } else {
            if (pendingState == null)
                pendingState = state
            pendingState = pendingState!!.setValue(FlexibleStatue.CONFIGURED, false)
        }
    }

    fun setBakedQuads(bakedQuads: QuadList, state: BlockState, skipUpdate: Boolean) {
        this._bakedQuads = bakedQuads
        refreshShape()
        if (!skipUpdate) {
            pushInternalDataChangeToClient(state.setValue(FlexibleStatue.CONFIGURED, true))
        } else {
            if (pendingState == null) pendingState = state
            pendingState = pendingState!!.setValue(FlexibleStatue.CONFIGURED, true)
        }
    }

    fun refreshShape() {
        _blockShape = if (bakedQuads == null) {
            null
        } else {
            bakedQuads!!.shape
        }
    }

    override fun getPeripheral(side: Direction): IOwnedPeripheral<*>? {
        return null
    }

    override fun createPeripheral(side: Direction): IOwnedPeripheral<*> {
        throw IllegalArgumentException("Shouldn't be called at all")
    }

    override fun loadInternalData(data: CompoundTag, state: BlockState?): BlockState {
        if (data.contains(NAME_TAG))
            _name = data.getString(NAME_TAG)
        if (data.contains(AUTHOR_TAG))
            _author = data.getString(AUTHOR_TAG)
        if (data.contains(LIGHT_LEVEL_TAG))
            _lightLevel = data.getInt(LIGHT_LEVEL_TAG)
        if (data.contains(BAKED_QUADS_TAG)) {
            val newBakedQuads = PENBTUtil.readQuadList(data.getByteArray(BAKED_QUADS_TAG))
            if (_bakedQuads != newBakedQuads) {
                if (newBakedQuads == null) {
                    clear(state ?: blockState, skipUpdate = true)
                } else {
                    setBakedQuads(newBakedQuads, state ?: blockState, skipUpdate = true)
                }
            }
        }
        return state ?: blockState
    }

    override fun saveInternalData(data: CompoundTag): CompoundTag {
        if (_bakedQuads != null) {
            val bakedQuadsData = PENBTUtil.serialize(_bakedQuads!!)
            if (bakedQuadsData != null) {
                data.put(BAKED_QUADS_TAG, bakedQuadsData)
            }
        }
        if (_name != null)
            data.putString(NAME_TAG, _name!!)
        if (_author != null)
            data.putString(AUTHOR_TAG, _author!!)
        if (lightLevel != 0)
            data.putInt(LIGHT_LEVEL_TAG, lightLevel)
        return data
    }
}