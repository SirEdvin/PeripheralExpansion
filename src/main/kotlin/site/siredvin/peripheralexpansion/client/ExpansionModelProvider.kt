package site.siredvin.peripheralexpansion.client

import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.minecraft.client.resources.model.UnbakedModel
import net.minecraft.resources.ResourceLocation

class ExpansionModelProvider: ModelResourceProvider {

    companion object {
        val FLEXIBLE_REALITY_ANCHOR = ResourceLocation("peripheralexpansion:block/flexible_reality_anchor")
    }

    override fun loadModelResource(resourceId: ResourceLocation, context: ModelProviderContext): UnbakedModel? {
        if (resourceId == FLEXIBLE_REALITY_ANCHOR)
            return FlexibleRealityAnchorModel()
        return null
    }
}