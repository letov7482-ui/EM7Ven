package com.e7ven.optimizer.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public final class RefreshRateManager {

    private static final double DEFAULT_REFRESH_RATE = 60.0;

    private double refreshRate = DEFAULT_REFRESH_RATE;
    private double targetFrameTime = 1000.0 / DEFAULT_REFRESH_RATE;

    private RefreshRateManager() {
    }

    public static RefreshRateManager getInstance() {
        return Holder.INSTANCE;
    }

    public void update() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null) {
            return;
        }

        Window window = client.getWindow();

        if (window == null) {
            return;
        }

        int detectedRefreshRate = window.getRefreshRate();

        if (detectedRefreshRate <= 0) {
            detectedRefreshRate = (int) DEFAULT_REFRESH_RATE;
        }

        refreshRate = detectedRefreshRate;
        targetFrameTime = 1000.0 / refreshRate;
    }

    public double getRefreshRate() {
        return refreshRate;
    }

    public double getTargetFrameTime() {
        return targetFrameTime;
    }

    public double getTargetFrameTimeNanos() {
        return targetFrameTime * 1_000_000.0;
    }

    public boolean isRefreshRate(double rate) {
        return Math.abs(refreshRate - rate) < 0.5;
    }

    public String getRefreshRateText() {
        return String.format("%.0f Hz", refreshRate);
    }

    public String getTargetFrameTimeText() {
        return String.format("%.2f ms", targetFrameTime);
    }

    private static final class Holder {
        private static final RefreshRateManager INSTANCE =
                new RefreshRateManager();
    }
}
