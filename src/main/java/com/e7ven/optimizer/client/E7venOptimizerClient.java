package com.e7ven.optimizer.client;

import net.fabricmc.api.ClientModInitializer;

public final class E7venOptimizerClient implements ClientModInitializer {

    private static final RefreshRateManager REFRESH_RATE_MANAGER =
            RefreshRateManager.getInstance();

    @Override
    public void onInitializeClient() {
        System.out.println("[E7ven Optimizer] Client initialized.");

        REFRESH_RATE_MANAGER.update();

        System.out.println(
                "[E7ven Optimizer] Display: "
                        + REFRESH_RATE_MANAGER.getRefreshRateText()
        );

        System.out.println(
                "[E7ven Optimizer] Target Frame Time: "
                        + REFRESH_RATE_MANAGER.getTargetFrameTimeText()
        );
    }

    public static RefreshRateManager getRefreshRateManager() {
        return REFRESH_RATE_MANAGER;
    }
}
