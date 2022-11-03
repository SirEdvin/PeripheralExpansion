package site.siredvin.peripheralexpansion.common.setup

import site.siredvin.peripheralexpansion.common.blocks.FlexibleRealityAnchor
import site.siredvin.peripheralexpansion.ext.register
import site.siredvin.peripheralium.common.blocks.GenericBlockEntityBlock

object Blocks {
    val REMOTE_OBSERVER = GenericBlockEntityBlock(
        { BlockEntityTypes.REMOTE_OBSERVER }, isRotatable = true, belongToTickingEntity = true
    ).register("remote_observer")

    val REALITY_FORGER = GenericBlockEntityBlock(
        { BlockEntityTypes.REALITY_FORGET }, isRotatable = true, belongToTickingEntity = false
    ).register("reality_forger")

    val FLEXIBLE_REALITY_ANCHOR = FlexibleRealityAnchor().register("flexible_reality_anchor")

    fun doSomething() {

    }
}