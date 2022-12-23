package site.siredvin.peripheralexpansion.util

import net.minecraft.nbt.ByteArrayTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.Tag
import java.awt.Color
import java.io.*

object PENBTUtil {
    fun serialize(data: QuadList): Tag? {
        val byteStream = ByteArrayOutputStream()
        val stream: ObjectOutputStream
        try {
            stream = ObjectOutputStream(byteStream)
            stream.writeObject(data)
            return ByteArrayTag(byteStream.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun serializer(color: Color): Tag {
        return IntTag.valueOf(color.rgb)
    }

    fun readQuadList(data: ByteArray): QuadList? {
        val byteStream = ByteArrayInputStream(data)
        val stream: ObjectInputStream
        try {
            stream = ObjectInputStream(byteStream)
            return stream.readObject() as QuadList
        } catch (e: InvalidClassException) {
            // just do nothing
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun readColor(value: Int): Color {
        return Color(value)
    }
}