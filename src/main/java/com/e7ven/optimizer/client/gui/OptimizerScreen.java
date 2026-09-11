package com.e7ven.optimizer.client.gui;

import com.e7ven.optimizer.client.E7venOptimizerClient;
import com.e7ven.optimizer.performance.PerformanceManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class OptimizerScreen extends Screen {

    private static final int PANEL_WIDTH = 420;
    private static final int PANEL_HEIGHT = 260;

    private final PerformanceManager performanceManager;

    public OptimizerScreen() {
        super(Text.literal("E7ven FrameTime Optimizer"));

        performanceManager =
                E7venOptimizerClient.getPerformanceManager();
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
        super.render(context, mouseX, mouseY, delta);

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;

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
         * Верхняя полоса.
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

        /*
         * Основная статистика.
         */
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
                        performanceManager.getCurrentFrameTime()
                ),
                statsX,
                statsY + 28
        );

        drawStat(
                context,
                "Target",
                formatMs(
                        performanceManager.getTargetFrameTime()
                ),
                statsX,
                statsY + 56
        );

        drawStat(
                context,
                "Average",
                formatMs(
                        performanceManager.getAverageFrameTime()
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

        drawStat(
                context,
                "Worst",
                formatMs(
                        performanceManager.getWorstFrameTime()
                ),
                left + 220,
                statsY
        );

        drawStat(
                context,
                "Stability",
                String.format(
                        "%.0f%%",
                        performanceManager.getStabilityScore()
                ),
                left + 220,
                statsY + 28
        );

        drawStat(
                context,
                "Resolution",
                String.format(
                        "%.0f%%",
                        performanceManager
                                .getResolutionScale() * 100.0
                ),
                left + 220,
                statsY + 56
        );

        drawStat(
                context,
                "Status",
                performanceManager.getStatusText(),
                left + 220,
                statsY + 84
        );

        /*
         * Область будущего графика.
         */
        int graphLeft = left + 16;
        int graphTop = top + 184;
        int graphRight = left + PANEL_WIDTH - 16;
        int graphBottom = top + 238;

        context.fill(
                graphLeft,
                graphTop,
                graphRight,
                graphBottom,
                0xFF080808
        );

        context.drawText(
                textRenderer,
                "Frame Time Graph",
                graphLeft + 6,
                graphTop + 5,
                0xFFAAAAAA,
                false
        );

        /*
         * Сам график подключим следующим файлом.
         */
        context.drawText(
                textRenderer,
                "Collecting data...",
                graphLeft + 6,
                graphTop + 25,
                0xFF777777,
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
