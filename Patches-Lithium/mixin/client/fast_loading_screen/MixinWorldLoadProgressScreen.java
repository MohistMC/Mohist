package me.jellysquid.mods.lithium.mixin.client.fast_loading_screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.jellysquid.mods.lithium.common.util.math.Color4;
import net.minecraft.client.gui.screen.WorldLoadProgressScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.listener.TrackingChunkStatusListener;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;

import java.util.IdentityHashMap;

/**
 * Re-implements the loading screen with considerations to reduce draw calls and other sources of overhead. This can
 * improve world load times on slower processors with very few cores.
 */
@Mixin(WorldLoadProgressScreen.class)
public class MixinWorldLoadProgressScreen {
    @Mutable
    @Shadow
    @Final
    private static Object2IntMap<ChunkStatus> COLORS;

    private static IdentityHashMap<ChunkStatus, Color4> COLORS_FAST;

    private static final Color4 NULL_STATUS_COLOR = Color4.fromRGBA(-16777216);
    private static final Color4 DEFAULT_STATUS_COLOR = Color4.fromRGBA(-16772609);

    /**
     * This implementation differs from vanilla's in the following key ways.
     * - All tiles are batched together in one draw call, reducing CPU overhead by an order of magnitudes.
     * - Identity hashing is used for faster ChunkStatus -> Color lookup.
     * - Colors are stored in unpacked RGBA format so conversion is not necessary every tile draw
     *
     * @reason Significantly optimized implementation.
     * @author JellySquid
     */
    @Overwrite
    public static void drawProgress(TrackingChunkStatusListener tracker, int mapX, int mapY, int mapScale, int mapPadding) {
        if (COLORS_FAST == null) {
            COLORS_FAST = new IdentityHashMap<>(COLORS.size());
            COLORS_FAST.put(null, NULL_STATUS_COLOR);
            COLORS.object2IntEntrySet()
                    .forEach(entry -> COLORS_FAST.put(entry.getKey(), Color4.fromRGBA(entry.getIntValue() | -16777216)));
        }

        Tessellator tessellator = Tessellator.getInstance();

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        int centerSize = tracker.getDiameter();
        int size = tracker.func_219523_d();

        int tileSize = mapScale + mapPadding;

        if (mapPadding != 0) {
            int mapRenderCenterSize = centerSize * tileSize - mapPadding;
            int radius = mapRenderCenterSize / 2 + 1;

            addRect(buffer, mapX - radius, mapY - radius, mapX - radius + 1, mapY + radius, DEFAULT_STATUS_COLOR);
            addRect(buffer, mapX + radius - 1, mapY - radius, mapX + radius, mapY + radius, DEFAULT_STATUS_COLOR);
            addRect(buffer, mapX - radius, mapY - radius, mapX + radius, mapY - radius + 1, DEFAULT_STATUS_COLOR);
            addRect(buffer, mapX - radius, mapY + radius - 1, mapX + radius, mapY + radius, DEFAULT_STATUS_COLOR);
        }

        int mapRenderSize = size * tileSize - mapPadding;
        int mapStartX = mapX - mapRenderSize / 2;
        int mapStartY = mapY - mapRenderSize / 2;

        ChunkStatus prevStatus = null;
        Color4 prevColor = NULL_STATUS_COLOR;

        for (int x = 0; x < size; ++x) {
            int tileX = mapStartX + x * tileSize;

            for (int z = 0; z < size; ++z) {
                int tileY = mapStartY + z * tileSize;

                ChunkStatus status = tracker.getStatus(x, z);
                Color4 color;

                if (prevStatus == status) {
                    color = prevColor;
                } else {
                    color = COLORS_FAST.get(status);

                    prevStatus = status;
                    prevColor = color;
                }

                addRect(buffer, tileX, tileY, tileX + mapScale, tileY + mapScale, color);
            }
        }

        tessellator.draw();

        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }

    private static void addRect(BufferBuilder buffer, double x1, double y1, double x2, double y2, Color4 color) {
        buffer.pos(x1, y2, 0.0D).color(color.r, color.g, color.b, color.a).endVertex();
        buffer.pos(x2, y2, 0.0D).color(color.r, color.g, color.b, color.a).endVertex();
        buffer.pos(x2, y1, 0.0D).color(color.r, color.g, color.b, color.a).endVertex();
        buffer.pos(x1, y1, 0.0D).color(color.r, color.g, color.b, color.a).endVertex();
    }
}
