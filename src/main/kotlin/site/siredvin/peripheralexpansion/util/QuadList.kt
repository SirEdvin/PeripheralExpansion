package site.siredvin.peripheralexpansion.util

import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.io.Serializable

class QuadList(private val list: List<QuadData>): Serializable {
    val shape: VoxelShape
        get() = list.map { it.shape }.reduceOrNull(Shapes::or) ?: Shapes.empty()

    fun toLua(): List<Map<String, Any>> {
        val data: MutableList<Map<String, Any>> = mutableListOf()
        list.forEach {
            data.add(it.toLua())
        }
        return data
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuadList

        if (list != other.list) return false

        return true
    }

    override fun hashCode(): Int {
        return list.hashCode()
    }

    override fun toString(): String {
        return "QuadList(list=$list)"
    }

}