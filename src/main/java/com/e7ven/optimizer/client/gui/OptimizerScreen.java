package com.e7ven.optimizer.client.gui;

import com.e7ven.optimizer.client.E7venOptimizerClient;
import com.e7ven.optimizer.performance.FrameTimeMonitor;
import com.e7ven.optimizer.performance.PerformanceManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class OptimizerScreen extends Screen {

    private static final int PANEL_WIDTH = 420;
    private static final int PANEL_HEIGHT = 260;

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

        /*
         * Основная панель.
         */
        context.fill(
                left,
                top,
                left + PANEL_WIDTH,
                top + PANEL_HEIGHT,
                0xE0101010
        );

        /*
         * Верхняя часть.
         */
        context.fill(
                left,
                top,
                left + PANEL_WIDTH,
                top + 34,
                0xFF202020
        );

        /*
         * Заголовок.
         */
        context.drawText(
                textRenderer,
                "E7ven FrameTime Optimizer",
                left + 12,
                top + 10,
                0xFFFFFFFF,
                false
        );

        int statsX = left + 16;
        int statsY = top + 52;

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
                statsY + 28
        );

        drawStat(
                context,
                "Target",
                formatMs(
                        performanceManager
                                .getTargetFrameTime()
                ),
                statsX,
                statsY + 56
        );

        drawStat(
                context,
                "Average",
                formatMs(
                        performanceManager
                                .getAverageFrameTime()
                ),
                statsX,
                statsY + 84
        );

        drawStat(
                context,
                "1% Low",
                formatMs(
                        performanceManager
                                .getOnePercentLowFrameTime()
                ),
                statsX,
                statsY + 112
        );

        int secondColumnX = left + 220;

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
                statsY + 28
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
                statsY + 56
        );

        drawStat(
                context,
                "Status",
                performanceManager
                        .getStatusText(),
                secondColumnX,
                statsY + 84
        );

        /*
         * График.
         */
        int graphLeft = left + 16;
        int graphTop = top + 184;
        int graphRight = left + PANEL_WIDTH - 16;
        int graphBottom = top + 238;

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

        /*
         * Значение цели.
         */
        context.drawText(
                textRenderer,
                String.format(
                        "Target %.2f ms",
                        performanceManager
                                .getTargetFrameTime()
                ),
                graphRight - 85,
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
                x + 100,
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
