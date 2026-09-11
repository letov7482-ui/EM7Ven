package com.e7ven.optimizer.performance;

import com.e7ven.optimizer.client.RefreshRateManager;
import com.e7ven.optimizer.mixin.GameRendererMixin;

public final class PerformanceManager {

    private static final PerformanceManager INSTANCE =
            new PerformanceManager();

    private final RefreshRateManager refreshRateManager;
    private final FrameTimeMonitor frameTimeMonitor;
    private final OptimizerController optimizerController;
    private final AdaptiveResolutionManager adaptiveResolutionManager;

    private PerformanceManager() {
        refreshRateManager = RefreshRateManager.getInstance();

        frameTimeMonitor =
                GameRendererMixin.e7ven$getFrameTimeMonitor();

        optimizerController =
                new OptimizerController(
                        refreshRateManager,
                        frameTimeMonitor
                );

        adaptiveResolutionManager =
                new AdaptiveResolutionManager();
    }

    public static PerformanceManager getInstance() {
        return INSTANCE;
    }

    /**
     * Основное обновление системы оптимизации.
     */
    public void update() {

        optimizerController.update();

        adaptiveResolutionManager.update(
                frameTimeMonitor.getCurrentFrameTime(),
                optimizerController.getTargetFrameTime()
        );
    }

    public RefreshRateManager getRefreshRateManager() {
        return refreshRateManager;
    }

    public FrameTimeMonitor getFrameTimeMonitor() {
        return frameTimeMonitor;
    }

    public OptimizerController getOptimizerController() {
        return optimizerController;
    }

    public AdaptiveResolutionManager getAdaptiveResolutionManager() {
        return adaptiveResolutionManager;
    }

    public double getCurrentFrameTime() {
        return frameTimeMonitor.getCurrentFrameTime();
    }

    public double getAverageFrameTime() {
        return frameTimeMonitor.getAverageFrameTime();
    }

    public double getWorstFrameTime() {
        return frameTimeMonitor.getWorstFrameTime();
    }

    public double getOnePercentLowFrameTime() {
        return frameTimeMonitor.getOnePercentLowFrameTime();
    }

    public double getTargetFrameTime() {
        return optimizerController.getTargetFrameTime();
    }

    public double getStabilityScore() {
        return optimizerController.getStabilityScore();
    }

    public double getResolutionScale() {
        return adaptiveResolutionManager.getResolutionScale();
    }

    public String getStatusText() {
        return optimizerController.getStatusText();
    }

    public boolean isOptimizationEnabled() {
        return optimizerController.isOptimizationEnabled();
    }

    public void setOptimizationEnabled(boolean enabled) {
        optimizerController.setOptimizationEnabled(enabled);

        if (!enabled) {
            adaptiveResolutionManager.reset();
        }
    }

    public void resetAdaptiveResolution() {
        adaptiveResolutionManager.reset();
    }
}
