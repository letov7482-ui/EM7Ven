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
    private final StutterController stutterController;
    private final RefreshRateProfileManager refreshRateProfileManager;
    private final MobilePerformanceManager mobilePerformanceManager;

    private PerformanceManager() {

        refreshRateManager =
                RefreshRateManager.getInstance();

        frameTimeMonitor =
                GameRendererMixin.e7ven$getFrameTimeMonitor();

        optimizerController =
                new OptimizerController(
                        refreshRateManager,
                        frameTimeMonitor
                );

        adaptiveResolutionManager =
                new AdaptiveResolutionManager();

        stutterController =
                new StutterController();

        refreshRateProfileManager =
                new RefreshRateProfileManager();

        mobilePerformanceManager =
                new MobilePerformanceManager();
    }

    public static PerformanceManager getInstance() {
        return INSTANCE;
    }

    public void update() {

        optimizerController.update();

        double currentFrameTime =
                frameTimeMonitor.getCurrentFrameTime();

        double averageFrameTime =
                frameTimeMonitor.getAverageFrameTime();

        double targetFrameTime =
                optimizerController.getTargetFrameTime();

        double currentScale =
                adaptiveResolutionManager
                        .getResolutionScale();

        stutterController.update(
                currentFrameTime,
                averageFrameTime
        );

        adaptiveResolutionManager.update(
                currentFrameTime,
                targetFrameTime
        );

        mobilePerformanceManager.update(
                currentFrameTime,
                targetFrameTime,
                currentScale
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

    public StutterController getStutterController() {
        return stutterController;
    }

    public RefreshRateProfileManager getRefreshRateProfileManager() {
        return refreshRateProfileManager;
    }

    public MobilePerformanceManager getMobilePerformanceManager() {
        return mobilePerformanceManager;
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
        return frameTimeMonitor
                .getOnePercentLowFrameTime();
    }

    public double getTargetFrameTime() {
        return optimizerController
                .getTargetFrameTime();
    }

    public double getStabilityScore() {
        return optimizerController
                .getStabilityScore();
    }

    public double getResolutionScale() {
        return adaptiveResolutionManager
                .getResolutionScale();
    }

    public double getMobileRecommendedScale() {
        return mobilePerformanceManager
                .getRecommendedScale();
    }

    public String getMobileModeText() {
        return mobilePerformanceManager
                .getModeText();
    }

    public String getMobileScaleText() {
        return mobilePerformanceManager
                .getScaleText();
    }

    public String getStatusText() {
        return optimizerController
                .getStatusText();
    }

    public boolean isOptimizationEnabled() {
        return optimizerController
                .isOptimizationEnabled();
    }

    public boolean isMobileOptimizationEnabled() {
        return mobilePerformanceManager
                .isEnabled();
    }

    public void setOptimizationEnabled(
            boolean enabled
    ) {

        optimizerController
                .setOptimizationEnabled(enabled);

        adaptiveResolutionManager
                .setEnabled(enabled);

        mobilePerformanceManager
                .setEnabled(enabled);

        if (!enabled) {
            adaptiveResolutionManager.reset();
            mobilePerformanceManager.reset();
        }
    }

    public void setMobileOptimizationEnabled(
            boolean enabled
    ) {

        mobilePerformanceManager
                .setEnabled(enabled);

        if (!enabled) {
            mobilePerformanceManager.reset();
        }
    }

    public void setMobilePerformanceMode(
            MobilePerformanceManager.Mode mode
    ) {

        mobilePerformanceManager
                .setMode(mode);
    }

    public void resetAdaptiveResolution() {

        adaptiveResolutionManager.reset();
        mobilePerformanceManager.reset();
    }
}
