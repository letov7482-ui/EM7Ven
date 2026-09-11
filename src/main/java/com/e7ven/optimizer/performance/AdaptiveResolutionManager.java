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

    private double resolutionScale =
            DEFAULT_SCALE;

    private double previousScale =
            DEFAULT_SCALE;

    private boolean enabled = true;

    private int decisionTimer = 0;
    private int recoveryCounter = 0;

    private boolean scaleChanged = false;

    public void update(
            double currentFrameTime,
            double targetFrameTime
    ) {
        scaleChanged = false;

        if (!enabled) {
            return;
        }

        if (currentFrameTime <= 0.0
                || targetFrameTime <= 0.0) {
            return;
        }

        decisionTimer++;

        if (decisionTimer < DECISION_INTERVAL) {
            return;
        }

        decisionTimer = 0;

        double ratio =
                currentFrameTime
                        / targetFrameTime;

        if (ratio > REDUCE_THRESHOLD) {

            recoveryCounter = 0;

            decreaseScale();

            return;
        }

        if (ratio < INCREASE_THRESHOLD) {

            recoveryCounter++;

            if (recoveryCounter >= RECOVERY_DELAY) {

                recoveryCounter = 0;

                increaseScale();
            }

            return;
        }

        recoveryCounter = 0;
    }

    private void decreaseScale() {

        double newScale =
                resolutionScale
                        - SCALE_STEP;

        newScale =
                Math.max(
                        MIN_SCALE,
                        newScale
                );

        setScale(newScale);
    }

    private void increaseScale() {

        double newScale =
                resolutionScale
                        + SCALE_STEP;

        newScale =
                Math.min(
                        MAX_SCALE,
                        newScale
                );

        setScale(newScale);
    }

    private void setScale(
            double newScale
    ) {
        newScale =
                Math.max(
                        MIN_SCALE,
                        Math.min(
                                MAX_SCALE,
                                newScale
                        )
                );

        if (
                Math.abs(
                        newScale
                                - resolutionScale
                ) < 0.001
        ) {
            return;
        }

        previousScale =
                resolutionScale;

        resolutionScale =
                newScale;

        scaleChanged = true;
    }

    public double getResolutionScale() {
        return resolutionScale;
    }

    public double getPreviousScale() {
        return previousScale;
    }

    public boolean hasScaleChanged() {
        return scaleChanged;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(
            boolean enabled
    ) {

        this.enabled = enabled;

        if (!enabled) {
            reset();
        }
    }

    public void reset() {

        previousScale =
                resolutionScale;

        resolutionScale =
                DEFAULT_SCALE;

        decisionTimer = 0;
        recoveryCounter = 0;

        scaleChanged = true;
    }

    public boolean isAtMinimum() {
        return resolutionScale
                <= MIN_SCALE;
    }

    public boolean isAtMaximum() {
        return resolutionScale
                >= MAX_SCALE;
    }

    public String getScaleText() {

        return String.format(
                "%.0f%%",
                resolutionScale * 100.0
        );
    }

    public String getPreviousScaleText() {

        return String.format(
                "%.0f%%",
                previousScale * 100.0
        );
    }
}
