package site.siredvin.peripheralexpansion.world

import com.google.common.collect.EvictingQueue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.peripheralexpansion.PeripheralExpansion
import java.util.function.Consumer

object BlockStateUpdateEventBus {
    private const val EVENT_QUEUE_MAX_SIZE = 50

    data class BlockStateUpdateEvent(val pos: BlockPos, val previous: BlockState, val current: BlockState)

    private val listenedBlockPos: MutableSet<BlockPos> = mutableSetOf()
    private var _lastEventID: Long = 0L

    private val eventQueue = EvictingQueue.create<Pair<Long, BlockStateUpdateEvent>>(EVENT_QUEUE_MAX_SIZE)

    @get:Synchronized
    val lastEventID: Long
        get() = _lastEventID

    @Synchronized
    fun postDebugLog() {
        PeripheralExpansion.LOGGER.info("Current last event ID: $_lastEventID")
        PeripheralExpansion.LOGGER.info("Current tracked pos: $listenedBlockPos")
    }

    fun addBlockPos(vararg pos: BlockPos) {
        listenedBlockPos.addAll(pos)
    }

    fun addBlockPos(pos: Collection<BlockPos>) {
        listenedBlockPos.addAll(pos)
    }

    fun removeBlockPos(vararg pos: BlockPos) {
        listenedBlockPos.removeAll(pos.toSet())
    }

    fun removeBlockPos(poses: Collection<BlockPos>) {
        listenedBlockPos.removeAll(poses.toSet())
    }

    @Synchronized
    fun putEventIntoQueue(id: Long, data: BlockStateUpdateEvent) {
        eventQueue.add(Pair(id, data))
        _lastEventID++
    }

    @Synchronized
    fun traverseEvents(lastConsumedID: Long, consumer: Consumer<BlockStateUpdateEvent>): Long {
        var consumedTracker = lastConsumedID
        for (message in eventQueue) {
            if (message != null) {
                if (message.first <= consumedTracker)
                    continue
                consumer.accept(message.second)
                consumedTracker = message.first
            }
        }
        return consumedTracker
    }

    fun onBlockStateChange(pos: BlockPos, previous: BlockState, current: BlockState) {
        if (listenedBlockPos.contains(pos)) {
            putEventIntoQueue(lastEventID, BlockStateUpdateEvent(pos, previous, current))
        }
    }
}