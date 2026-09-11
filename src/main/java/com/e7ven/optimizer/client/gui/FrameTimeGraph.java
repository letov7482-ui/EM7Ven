package com.e7ven.optimizer.client.gui;

import com.e7ven.optimizer.performance.FrameTimeMonitor;
import net.minecraft.client.gui.DrawContext;

public final class FrameTimeGraph {

    private FrameTimeGraph() {
    }

    public static void render(
            DrawContext context,
            FrameTimeMonitor monitor,
            int left,
            int top,
            int right,
            int bottom,
            double targetFrameTime
    ) {
        if (monitor == null) {
            return;
        }

        double[] history = monitor.getHistoryCopy();

        if (history.length < 2) {
            return;
        }

        int width = right - left;
        int height = bottom - top;

        if (width <= 0 || height <= 0) {
            return;
        }

        /*
         * Фон графика.
         */
        context.fill(
                left,
                top,
                right,
                bottom,
                0xFF080808
        );

        /*
         * Максимальное значение по вертикали.
         *
         * Минимум 16 ms, чтобы график не становился
         * слишком чувствительным при стабильном FPS.
         */
        double maxFrameTime = 16.0;

        for (double value : history) {
            if (value > maxFrameTime) {
                maxFrameTime = value;
            }
        }

        /*
         * Небольшой запас сверху.
         */
        maxFrameTime *= 1.10;

        /*
         * Сетка.
         */
        drawHorizontalLine(
                context,
                left,
                right,
                bottom - 1,
                0x55333333
        );

        drawHorizontalLine(
                context,
                left,
                right,
                top + height / 2,
                0x44333333
        );

        /*
         * Линия целевого Frame Time.
         */
        if (targetFrameTime > 0.0) {

            int targetY = valueToY(
                    targetFrameTime,
                    maxFrameTime,
                    top,
                    bottom
            );

            targetY = clamp(
                    targetY,
                    top,
                    bottom
            );

            drawHorizontalLine(
                    context,
                    left,
                    right,
                    targetY,
                    0xAAFFFFFF
            );
        }

        /*
         * Рисуем сам Frame Time.
         */
        int previousX = left;
        int previousY = valueToY(
                history[0],
                maxFrameTime,
                top,
                bottom
        );

        previousY = clamp(
                previousY,
                top,
                bottom
        );

        for (int i = 1; i < history.length; i++) {

            int x = left
                    + (int) (
                    (double) i
                            / (history.length - 1)
                            * (width - 1)
            );

            int y = valueToY(
                    history[i],
                    maxFrameTime,
                    top,
                    bottom
            );

            y = clamp(
                    y,
                    top,
                    bottom
            );

            drawLine(
                    context,
                    previousX,
                    previousY,
                    x,
                    y,
                    0xFFFFFFFF
            );

            previousX = x;
            previousY = y;
        }
    }

    private static int valueToY(
            double value,
            double maxValue,
            int top,
            int bottom
    ) {
        if (maxValue <= 0.0) {
            return bottom;
        }

        double normalized =
                value / maxValue;

        normalized = Math.max(
                0.0,
                Math.min(1.0, normalized)
        );

        return bottom
                - (int) (
                normalized
                        * (bottom - top - 1)
        );
    }

    private static void drawHorizontalLine(
            DrawContext context,
            int left,
            int right,
            int y,
            int color
    ) {
        context.fill(
                left,
                y,
                right,
                y + 1,
                color
        );
    }

    private static void drawLine(
            DrawContext context,
            int x1,
            int y1,
            int x2,
            int y2,
            int color
    ) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        int error = dx - dy;

        int x = x1;
        int y = y1;

        while (true) {

            context.fill(
                    x,
                    y,
                    x + 1,
                    y + 1,
                    color
            );

            if (x == x2 && y == y2) {
                break;
            }

            int doubleError = error * 2;

            if (doubleError > -dy) {
                error -= dy;
                x += sx;
            }

            if (doubleError < dx) {
                error += dx;
                y += sy;
            }
        }
    }

    private static int clamp(
            int value,
            int min,
            int max
    ) {
        return Math.max(
                min,
                Math.min(max, value)
        );
    }
}
