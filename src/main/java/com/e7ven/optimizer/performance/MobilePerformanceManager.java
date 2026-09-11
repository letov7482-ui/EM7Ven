package com.e7ven.optimizer.performance;

public final class MobilePerformanceManager {

    public enum Mode {
        BALANCED,
        PERFORMANCE
    }

    private static final double BALANCED_MIN_SCALE = 0.70;
    private static final double PERFORMANCE_MIN_SCALE = 0.55;

    private static final double MAX_SCALE = 1.00;

    private Mode mode = Mode.BALANCED;

    private boolean enabled = false;

    private double recommendedScale = MAX_SCALE;

    private int stableTicks = 0;

    public void update(
            double currentFrameTime,
            double targetFrameTime,
            double currentScale
    ) {
        if (!enabled) {
            return;
        }

        if (currentFrameTime <= 0.0
                || targetFrameTime <= 0.0) {
            return;
        }

        double ratio =
                currentFrameTime / targetFrameTime;

        double minimumScale =
                getMinimumScale();

        /*
         * Сильный Frame Time spike.
         */
        if (ratio >= 1.35) {

            stableTicks = 0;

            recommendedScale =
                    Math.max(
                            minimumScale,
                            currentScale - 0.10
                    );

            return;
        }

        /*
         * Производительность немного ниже цели.
         */
        if (ratio >= 1.10) {

            stableTicks = 0;

            recommendedScale =
                    Math.max(
                            minimumScale,
                            currentScale - 0.05
                    );

            return;
        }

        /*
         * Производительность стабильная.
         */
        if (ratio <= 0.90) {

            stableTicks++;

            /*
             * Восстанавливаем качество медленно.
             */
            if (stableTicks >= 60) {

                stableTicks = 0;

                recommendedScale =
                        Math.min(
                                MAX_SCALE,
                                currentScale + 0.05
                        );
            }

            return;
        }

        stableTicks = 0;
    }

    private double getMinimumScale() {

        return switch (mode) {

            case BALANCED ->
                    BALANCED_MIN_SCALE;

            case PERFORMANCE ->
                    PERFORMANCE_MIN_SCALE;
        };
    }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;

        if (!enabled) {
            recommendedScale = MAX_SCALE;
            stableTicks = 0;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setMode(Mode mode) {

        if (mode == null) {
            return;
        }

        this.mode = mode;

        if (recommendedScale < getMinimumScale()) {
            recommendedScale =
                    getMinimumScale();
        }
    }

    public Mode getMode() {
        return mode;
    }

    public double getRecommendedScale() {
        return recommendedScale;
    }

    public String getModeText() {
        return mode.name();
    }

    public String getScaleText() {

        return String.format(
                "%.0f%%",
                recommendedScale * 100.0
        );
    }

    public void reset() {

        recommendedScale = MAX_SCALE;
        stableTicks = 0;
    }
}
