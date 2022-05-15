package site.siredvin.peripheralexpansion.util

import net.fabricmc.loader.api.FabricLoader
import site.siredvin.peripheralexpansion.PeripheralExpansion
import java.lang.InstantiationException
import java.lang.IllegalAccessException
import java.lang.ClassNotFoundException
import java.lang.Exception
import java.util.*

object Platform {
    fun maybeLoadIntegration(modid: String, path: String = "Integration"): Optional<Any> {
        val modPresent = FabricLoader.getInstance().allMods.stream().anyMatch { it.metadata.id == modid }
        if (modPresent) {
            PeripheralExpansion.LOGGER.info("Loading integration for $modid")
            return maybeLoadIntegration("${modid}.$path")
        } else {
            PeripheralExpansion.LOGGER.info("Mod $modid is not present, skip loading integration")
        }
        return Optional.empty()
    }

    private fun maybeLoadIntegration(path: String): Optional<Any> {
        return try {
            val clazz = Class.forName(PeripheralExpansion::class.java.getPackage().name + ".integrations." + path)
            Optional.of(clazz.newInstance())
        } catch (ignored: InstantiationException) {
            PeripheralExpansion.LOGGER.info("Exception when loading integration $ignored")
            Optional.empty()
        } catch (ignored: IllegalAccessException) {
            PeripheralExpansion.LOGGER.info("Exception when loading integration $ignored")
            Optional.empty()
        } catch (ignored: ClassNotFoundException) {
            PeripheralExpansion.LOGGER.info("Exception when loading integration $ignored")
            Optional.empty()
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }
}