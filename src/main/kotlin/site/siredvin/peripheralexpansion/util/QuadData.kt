package site.siredvin.peripheralexpansion.util

import com.mojang.math.Vector3f
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.awt.Color
import java.io.Serializable

data class QuadData(val x1: Float, val y1: Float, val z1: Float, val x2: Float, val y2: Float, val z2: Float, val color: Color): Serializable {

    constructor(start: Vector3f, end: Vector3f, color: Color) : this(start.x(), start.y(), start.z(), end.x(), end.y(), end.z(), color)

    val minecraftColor: Color
        // For some strange reason, colors order in minecraft vertex are swapper
        // and it not RGB, but BGR
        get() = Color(color.blue, color.green, color.red)

    val start: Vector3f
        get() = Vector3f(x1, y1, z1)

    val end: Vector3f
        get() = Vector3f(x2, y2, z2)

    val uv: Array<Float>
        get() = arrayOf(x1 / 4, z1 / 4, x2 / 4, z2 / 4)

    val shape: VoxelShape
        get() = Shapes.box(
            (x1 / 16).toDouble(), (y1 / 16).toDouble(), (z1 / 16).toDouble(),
            (x2 / 16).toDouble(), (y2 / 16).toDouble(), (z2 / 16).toDouble()
        )

    fun toLua(): Map<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["x1"] = x1
        data["y1"] = y1
        data["z1"] = z1
        data["x2"] = x2
        data["y2"] = y2
        data["z2"] = z2
        data["color"] = color.toString()
        data["colorRGB"] = color.rgb
        return data
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuadData

        if (x1 != other.x1) return false
        if (y1 != other.y1) return false
        if (z1 != other.z1) return false
        if (x2 != other.x2) return false
        if (y2 != other.y2) return false
        if (z2 != other.z2) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x1.hashCode()
        result = 31 * result + y1.hashCode()
        result = 31 * result + z1.hashCode()
        result = 31 * result + x2.hashCode()
        result = 31 * result + y2.hashCode()
        result = 31 * result + z2.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }

    override fun toString(): String {
        return "QuadData(x1=$x1, y1=$y1, z1=$z1, x2=$x2, y2=$y2, z2=$z2, color=$color)"
    }


}