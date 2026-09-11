package com.e7ven.optimizer.mixin;

import com.e7ven.optimizer.performance.FrameTimeMonitor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    private static final FrameTimeMonitor E7VEN_FRAME_TIME_MONITOR =
            new FrameTimeMonitor();

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void e7ven$beginFrame(
            RenderTickCounter tickCounter,
            boolean tick,
            CallbackInfo ci
    ) {
        E7VEN_FRAME_TIME_MONITOR.beginFrame();
    }

    @Inject(
            method = "render",
            at = @At("RETURN")
    )
    private void e7ven$endFrame(
            RenderTickCounter tickCounter,
            boolean tick,
            CallbackInfo ci
    ) {
        E7VEN_FRAME_TIME_MONITOR.endFrame();
    }

    public static FrameTimeMonitor e7ven$getFrameTimeMonitor() {
        return E7VEN_FRAME_TIME_MONITOR;
    }
}
