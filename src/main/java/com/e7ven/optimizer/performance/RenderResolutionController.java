package com.e7ven.optimizer.performance;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public final class RenderResolutionController {

    private static final int MIN_WIDTH = 320;
    private static final int MIN_HEIGHT = 240;

    private static final int MAX_SCALE_PERCENT = 100;
    private static final int MIN_SCALE_PERCENT = 50;

    private int renderWidth = 0;
    private int renderHeight = 0;

    private int lastWindowWidth = 0;
    private int lastWindowHeight = 0;
    private int lastScalePercent = 100;

    private boolean resolutionChanged = false;

    public void update(
            MinecraftClient client,
            double scale
    ) {
        resolutionChanged = false;

        if (client == null) {
            return;
        }

        Window window = client.getWindow();

        if (window == null) {
            return;
        }

        int windowWidth =
                window.getFramebufferWidth();

        int windowHeight =
                window.getFramebufferHeight();

        if (windowWidth <= 0
                || windowHeight <= 0) {
            return;
        }

        int scalePercent =
                (int) Math.round(
                        scale * 100.0
                );

        scalePercent =
                Math.max(
                        MIN_SCALE_PERCENT,
                        Math.min(
                                MAX_SCALE_PERCENT,
                                scalePercent
                        )
                );

        int newWidth =
                calculateScaledSize(
                        windowWidth,
                        scalePercent
                );

        int newHeight =
                calculateScaledSize(
                        windowHeight,
                        scalePercent
                );

        newWidth =
                Math.max(
                        MIN_WIDTH,
                        newWidth
                );

        newHeight =
                Math.max(
                        MIN_HEIGHT,
                        newHeight
                );

        if (
                newWidth != renderWidth
                        || newHeight != renderHeight
                        || windowWidth != lastWindowWidth
                        || windowHeight != lastWindowHeight
                        || scalePercent != lastScalePercent
        ) {

            renderWidth = newWidth;
            renderHeight = newHeight;

            lastWindowWidth =
                    windowWidth;

            lastWindowHeight =
                    windowHeight;

            lastScalePercent =
                    scalePercent;

            resolutionChanged = true;
        }
    }

    private int calculateScaledSize(
            int originalSize,
            int scalePercent
    ) {
        return Math.max(
                1,
                (int) Math.round(
                        originalSize
                                * scalePercent
                                / 100.0
                )
        );
    }

    public int getRenderWidth() {
        return renderWidth;
    }

    public int getRenderHeight() {
        return renderHeight;
    }

    public int getScalePercent() {
        return lastScalePercent;
    }

    public double getScale() {
        return lastScalePercent / 100.0;
    }

    public boolean hasResolutionChanged() {
        return resolutionChanged;
    }

    public boolean isAtNativeResolution() {
        return lastScalePercent
                >= MAX_SCALE_PERCENT;
    }

    public boolean isReducedResolution() {
        return lastScalePercent
                < MAX_SCALE_PERCENT;
    }

    public void reset() {
        renderWidth = 0;
        renderHeight = 0;

        lastWindowWidth = 0;
        lastWindowHeight = 0;

        lastScalePercent = 100;

        resolutionChanged = false;
    }

    public String getResolutionText() {

        return renderWidth
                + "x"
                + renderHeight;
    }

    public String getScaleText() {

        return String.format(
                "%.0f%%",
                getScale() * 100.0
        );
    }
}
