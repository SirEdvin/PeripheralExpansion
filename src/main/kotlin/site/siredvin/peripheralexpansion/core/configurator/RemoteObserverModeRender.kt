package site.siredvin.peripheralexpansion.core.configurator

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Matrix4f
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.core.BlockPos
import site.siredvin.peripheralexpansion.PeripheralExpansion
import site.siredvin.peripheralexpansion.client.ModRenderTypes
import site.siredvin.peripheralexpansion.common.blockentities.RemoteObserverBlockEntity
import site.siredvin.peripheralexpansion.common.setup.Items


@Environment(EnvType.CLIENT)
class RemoteObserverModeRender: ConfigurationModeRender {

    private fun blueLine(
        builder: VertexConsumer, positionMatrix: Matrix4f,
        pos: BlockPos,
        dx1: Float, dy1: Float, dz1: Float,
        dx2: Float, dy2: Float, dz2: Float
    ) {
        builder.vertex(positionMatrix, pos.x + dx1, pos.y + dy1, pos.z + dz1)
            .color(0.95f, 0.95f, 0.95f, 1.0f)
            .endVertex()
        builder.vertex(positionMatrix, pos.x + dx2, pos.y + dy2, pos.z + dz2)
            .color(0.95f, 0.95f, 0.95f, 1.0f)
            .endVertex()
    }

    override fun render(source: BlockPos, context: WorldRenderContext) {
        val entity = context.world().getBlockEntity(source)
        if (entity !is RemoteObserverBlockEntity) {
            PeripheralExpansion.LOGGER.error("Remote observer configuration mode renderer process $source which is not remote observer")
            return
        }
        val buffer =  Minecraft.getInstance().renderBuffers().bufferSource()
        val builder = buffer.getBuffer(ModRenderTypes.OVERLAY_LINES)
        context.matrixStack().pushPose()
        val matrix = context.matrixStack().last().pose()
        entity.trackedBlocksView.forEach {
//            LevelRenderer.renderLineBox(context.matrixStack(), builder,
//                it.x.toDouble(), it.y.toDouble(), it.z.toDouble(), it.x + 1.0, it.y + 1.0, it.z + 1.0, 1f, 0.5f, 0.5f, 0.5f)
            blueLine(builder, matrix, it, 0f, 0f, 0f, 1f, 0f, 0f);
            blueLine(builder, matrix, it, 0f, 1f, 0f, 1f, 1f, 0f);
            blueLine(builder, matrix, it, 0f, 0f, 1f, 1f, 0f, 1f);
            blueLine(builder, matrix, it, 0f, 1f, 1f, 1f, 1f, 1f);

            blueLine(builder, matrix, it, 0f, 0f, 0f, 0f, 0f, 1f);
            blueLine(builder, matrix, it, 1f, 0f, 0f, 1f, 0f, 1f);
            blueLine(builder, matrix, it, 0f, 1f, 0f, 0f, 1f, 1f);
            blueLine(builder, matrix, it, 1f, 1f, 0f, 1f, 1f, 1f);

            blueLine(builder, matrix, it, 0f, 0f, 0f, 0f, 1f, 0f);
            blueLine(builder, matrix, it, 1f, 0f, 0f, 1f, 1f, 0f);
            blueLine(builder, matrix, it, 0f, 0f, 1f, 0f, 1f, 1f);
            blueLine(builder, matrix, it, 1f, 0f, 1f, 1f, 1f, 1f);
        }
//        LevelRenderer.renderLineBox()
        context.matrixStack().popPose()
        RenderSystem.setShader(GameRenderer::getPositionColorShader)
        buffer.endBatch(ModRenderTypes.OVERLAY_LINES)
    }
}