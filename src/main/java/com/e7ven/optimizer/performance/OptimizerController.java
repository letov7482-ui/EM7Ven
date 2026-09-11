package com.e7ven.optimizer.performance;

import com.e7ven.optimizer.client.RefreshRateManager;

public final class OptimizerController {

    private static final double MIN_TARGET_FRAME_TIME = 4.0;
    private static final double MAX_TARGET_FRAME_TIME = 100.0;

    private static final double WARNING_MULTIPLIER = 1.10;
    private static final double CRITICAL_MULTIPLIER = 1.35;

    private final RefreshRateManager refreshRateManager;
    private final FrameTimeMonitor frameTimeMonitor;

    private boolean optimizationEnabled = true;

    private double targetFrameTime;
    private double frameTimeError;
    private double stabilityScore;

    private Status status = Status.STABLE;

    public OptimizerController(
            RefreshRateManager refreshRateManager,
            FrameTimeMonitor frameTimeMonitor
    ) {
        this.refreshRateManager = refreshRateManager;
        this.frameTimeMonitor = frameTimeMonitor;

        updateTarget();
    }

    /**
     * Вызывается периодически для обновления состояния оптимизатора.
     */
    public void update() {
        if (!optimizationEnabled) {
            status = Status.DISABLED;
            return;
        }

        updateTarget();

        double currentFrameTime = frameTimeMonitor.getCurrentFrameTime();

        if (currentFrameTime <= 0.0) {
            status = Status.WAITING;
            return;
        }

        frameTimeError = currentFrameTime - targetFrameTime;

        updateStatus();
        calculateStabilityScore();
    }

    private void updateTarget() {
        double refreshRate = refreshRateManager.getRefreshRate();

        if (refreshRate <= 0.0) {
            refreshRate = 60.0;
        }

        targetFrameTime = 1000.0 / refreshRate;

        targetFrameTime = Math.max(
                MIN_TARGET_FRAME_TIME,
                Math.min(MAX_TARGET_FRAME_TIME, targetFrameTime)
        );
    }

    private void updateStatus() {
        double currentFrameTime = frameTimeMonitor.getCurrentFrameTime();

        if (currentFrameTime <= targetFrameTime) {
            status = Status.STABLE;
            return;
        }

        if (currentFrameTime <= targetFrameTime * WARNING_MULTIPLIER) {
            status = Status.WARNING;
            return;
        }

        status = Status.CRITICAL;
    }

    private void calculateStabilityScore() {
        double average = frameTimeMonitor.getAverageFrameTime();
        double worst = frameTimeMonitor.getWorstFrameTime();

        if (average <= 0.0 || worst <= 0.0) {
            stabilityScore = 100.0;
            return;
        }

        /*
         * Насколько средний Frame Time близок к цели.
         */
        double averageRatio = targetFrameTime / average;

        /*
         * Штраф за большие скачки Frame Time.
         */
        double spikeRatio = targetFrameTime / worst;

        double averageScore = clamp(
                averageRatio * 100.0,
                0.0,
                100.0
        );

        double spikeScore = clamp(
                spikeRatio * 100.0,
                0.0,
                100.0
        );

        /*
         * Средний Frame Time важнее одиночного спайка.
         */
        stabilityScore =
                averageScore * 0.7
                        + spikeScore * 0.3;
    }

    private double clamp(
            double value,
            double min,
            double max
    ) {
        return Math.max(min, Math.min(max, value));
    }

    public RefreshRateManager getRefreshRateManager() {
        return refreshRateManager;
    }

    public FrameTimeMonitor getFrameTimeMonitor() {
        return frameTimeMonitor;
    }

    public double getTargetFrameTime() {
        return targetFrameTime;
    }

    public double getFrameTimeError() {
        return frameTimeError;
    }

    public double getStabilityScore() {
        return stabilityScore;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isOptimizationEnabled() {
        return optimizationEnabled;
    }

    public void setOptimizationEnabled(boolean enabled) {
        this.optimizationEnabled = enabled;

        if (!enabled) {
            status = Status.DISABLED;
        }
    }

    public String getStatusText() {
        return switch (status) {
            case STABLE -> "STABLE";
            case WARNING -> "WARNING";
            case CRITICAL -> "CRITICAL";
            case WAITING -> "WAITING";
            case DISABLED -> "DISABLED";
        };
    }

    public String getStabilityText() {
        return String.format(
                "%.0f%%",
                stabilityScore
        );
    }

    public String getTargetFrameTimeText() {
        return String.format(
                "%.2f ms",
                targetFrameTime
        );
    }

    public String getFrameTimeErrorText() {
        return String.format(
                "%+.2f ms",
                frameTimeError
        );
    }

    public enum Status {
        STABLE,
        WARNING,
        CRITICAL,
        WAITING,
        DISABLED
    }
}
