package site.siredvin.peripheralexpansion.common.setup

import site.siredvin.peripheralexpansion.ext.register
import site.siredvin.peripheralium.common.blocks.GenericBlockEntityBlock

object Blocks {
    val REMOTE_OBSERVER = GenericBlockEntityBlock(
        { BlockEntityTypes.REMOTE_OBSERVER }, isRotatable = true, belongToTickingEntity = true
    ).register("remote_observer")

    fun doSomething() {

    }
}