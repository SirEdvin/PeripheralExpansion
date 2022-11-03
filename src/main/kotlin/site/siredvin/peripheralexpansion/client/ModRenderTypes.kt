package site.siredvin.peripheralexpansion.client

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderType
import java.util.*


class ModRenderTypes(
    string: String,
    vertexFormat: VertexFormat,
    mode: VertexFormat.Mode,
    i: Int,
    bl: Boolean,
    bl2: Boolean,
    runnable: Runnable,
    runnable2: Runnable
) : RenderType(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2) {

    companion object {
        private val THICK_LINES = LineStateShard(OptionalDouble.of(3.0))
        val OVERLAY_LINES: RenderType = create(
            "overlay_lines",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.LINES,
            256,
            false,
            true,
            CompositeState.builder()
                .setLineState(THICK_LINES)
                .createCompositeState(true)
        )
    }

}