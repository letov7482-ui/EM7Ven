package com.e7ven.optimizer.client.gui;

import com.e7ven.optimizer.client.E7venOptimizerClient;
import com.e7ven.optimizer.performance.FrameTimeMonitor;
import com.e7ven.optimizer.performance.MobilePerformanceManager;
import com.e7ven.optimizer.performance.PerformanceManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class OptimizerScreen extends Screen {

    private static final int PANEL_WIDTH = 460;
    private static final int PANEL_HEIGHT = 300;

    private final PerformanceManager performanceManager;
    private final FrameTimeMonitor frameTimeMonitor;

    public OptimizerScreen() {
        super(Text.literal("E7ven FrameTime Optimizer"));

        performanceManager =
                E7venOptimizerClient.getPerformanceManager();

        frameTimeMonitor =
                performanceManager.getFrameTimeMonitor();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.render(
                context,
                mouseX,
                mouseY,
                delta
        );

        int left =
                (width - PANEL_WIDTH) / 2;

        int top =
                (height - PANEL_HEIGHT) / 2;

        // Main panel
        context.fill(
                left,
                top,
                left + PANEL_WIDTH,
                top + PANEL_HEIGHT,
                0xE0101010
        );

        // Header
        context.fill(
                left,
                top,
                left + PANEL_WIDTH,
                top + 34,
                0xFF202020
        );

        context.drawText(
                textRenderer,
                "E7ven FrameTime Optimizer",
                left + 12,
                top + 10,
                0xFFFFFFFF,
                false
        );

        int statsX = left + 16;
        int statsY = top + 48;

        // LEFT COLUMN

        drawStat(
                context,
                "Refresh Rate",
                performanceManager
                        .getRefreshRateManager()
                        .getRefreshRateText(),
                statsX,
                statsY
        );

        drawStat(
                context,
                "Frame Time",
                formatMs(
                        performanceManager
                                .getCurrentFrameTime()
                ),
                statsX,
                statsY + 24
        );

        drawStat(
                context,
                "Target",
                formatMs(
                        performanceManager
                                .getTargetFrameTime()
                ),
                statsX,
                statsY + 48
        );

        drawStat(
                context,
                "Average",
                formatMs(
                        performanceManager
                                .getAverageFrameTime()
                ),
                statsX,
                statsY + 72
        );

        drawStat(
                context,
                "1% Low",
                formatMs(
                        performanceManager
                                .getOnePercentLowFrameTime()
                ),
                statsX,
                statsY + 96
        );

        // RIGHT COLUMN

        int secondColumnX = left + 235;

        drawStat(
                context,
                "Worst",
                formatMs(
                        performanceManager
                                .getWorstFrameTime()
                ),
                secondColumnX,
                statsY
        );

        drawStat(
                context,
                "Stability",
                String.format(
                        "%.0f%%",
                        performanceManager
                                .getStabilityScore()
                ),
                secondColumnX,
                statsY + 24
        );

        drawStat(
                context,
                "Resolution",
                String.format(
                        "%.0f%%",
                        performanceManager
                                .getResolutionScale()
                                * 100.0
                ),
                secondColumnX,
                statsY + 48
        );

        drawStat(
                context,
                "Status",
                performanceManager
                        .getStatusText(),
                secondColumnX,
                statsY + 72
        );

        drawStat(
                context,
                "Mobile Mode",
                performanceManager
                        .getMobileModeText(),
                secondColumnX,
                statsY + 96
        );

        // Mobile optimization section

        int mobileY = top + 160;

        context.fill(
                left + 12,
                mobileY,
                left + PANEL_WIDTH - 12,
                mobileY + 32,
                0xFF181818
        );

        context.drawText(
                textRenderer,
                "Mobile Optimization",
                left + 20,
                mobileY + 8,
                0xFFFFFFFF,
                false
        );

        MobilePerformanceManager mobileManager =
                performanceManager
                        .getMobilePerformanceManager();

        String mobileStatus =
                mobileManager.isEnabled()
                        ? "ON"
                        : "OFF";

        drawStat(
                context,
                "Enabled",
                mobileStatus,
                left + 20,
                mobileY + 42
        );

        drawStat(
                context,
                "Recommended",
                mobileManager.getScaleText(),
                left + 235,
                mobileY + 42
        );

        // Graph

        int graphLeft = left + 16;
        int graphTop = top + 222;
        int graphRight = left + PANEL_WIDTH - 16;
        int graphBottom = top + 282;

        FrameTimeGraph.render(
                context,
                frameTimeMonitor,
                graphLeft,
                graphTop,
                graphRight,
                graphBottom,
                performanceManager
                        .getTargetFrameTime()
        );

        context.drawText(
                textRenderer,
                "Frame Time",
                graphLeft + 6,
                graphTop + 5,
                0xFFAAAAAA,
                false
        );

        context.drawText(
                textRenderer,
                String.format(
                        "Target %.2f ms",
                        performanceManager
                                .getTargetFrameTime()
                ),
                graphRight - 95,
                graphTop + 5,
                0xFFAAAAAA,
                false
        );
    }

    private void drawStat(
            DrawContext context,
            String name,
            String value,
            int x,
            int y
    ) {
        context.drawText(
                textRenderer,
                name,
                x,
                y,
                0xFFAAAAAA,
                false
        );

        context.drawText(
                textRenderer,
                value,
                x + 105,
                y,
                0xFFFFFFFF,
                false
        );
    }

    private String formatMs(double value) {

        if (value <= 0.0) {
            return "-- ms";
        }

        return String.format(
                "%.2f ms",
                value
        );
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
