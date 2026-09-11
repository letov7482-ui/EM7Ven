package com.e7ven.optimizer.performance;

public final class AdaptiveResolutionManager {

    private static final double DEFAULT_SCALE = 1.0;

    private static final double MIN_SCALE = 0.50;
    private static final double MAX_SCALE = 1.00;

    private static final double SCALE_STEP = 0.05;

    private static final int DECISION_INTERVAL = 20;

    private static final double REDUCE_THRESHOLD = 1.10;
    private static final double INCREASE_THRESHOLD = 0.90;

    private static final int RECOVERY_DELAY = 3;

    private double resolutionScale = DEFAULT_SCALE;

    private boolean enabled = true;

    private int decisionTimer = 0;
    private int recoveryCounter = 0;

    public void update(
            double currentFrameTime,
            double targetFrameTime
    ) {
        if (!enabled) {
            return;
        }

        if (currentFrameTime <= 0.0 || targetFrameTime <= 0.0) {
            return;
        }

        decisionTimer++;

        if (decisionTimer < DECISION_INTERVAL) {
            return;
        }

        decisionTimer = 0;

        double ratio =
                currentFrameTime / targetFrameTime;

        /*
         * Производительность плохая:
         * быстрее реагируем снижением качества.
         */
        if (ratio > REDUCE_THRESHOLD) {

            recoveryCounter = 0;

            decreaseScale();

            return;
        }

        /*
         * Производительность хорошая.
         *
         * Не повышаем качество сразу после одного
         * удачного измерения — ждём несколько циклов.
         */
        if (ratio < INCREASE_THRESHOLD) {

            recoveryCounter++;

            if (recoveryCounter >= RECOVERY_DELAY) {
                recoveryCounter = 0;

                increaseScale();
            }

            return;
        }

        /*
         * Мы находимся в нормальной зоне.
         */
        recoveryCounter = 0;
    }

    private void decreaseScale() {

        resolutionScale -= SCALE_STEP;

        if (resolutionScale < MIN_SCALE) {
            resolutionScale = MIN_SCALE;
        }
    }

    private void increaseScale() {

        resolutionScale += SCALE_STEP;

        if (resolutionScale > MAX_SCALE) {
            resolutionScale = MAX_SCALE;
        }
    }

    public double getResolutionScale() {
        return resolutionScale;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void reset() {

        resolutionScale = DEFAULT_SCALE;

        decisionTimer = 0;
        recoveryCounter = 0;
    }

    public boolean isAtMinimum() {
        return resolutionScale <= MIN_SCALE;
    }

    public boolean isAtMaximum() {
        return resolutionScale >= MAX_SCALE;
    }

    public String getScaleText() {

        return String.format(
                "%.0f%%",
                resolutionScale * 100.0
        );
    }
}
