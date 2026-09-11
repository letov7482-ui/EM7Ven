package com.e7ven.optimizer.client;

import com.e7ven.optimizer.performance.PerformanceManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class E7venOptimizerClient implements ClientModInitializer {

    private static final RefreshRateManager REFRESH_RATE_MANAGER =
            RefreshRateManager.getInstance();

    private static final PerformanceManager PERFORMANCE_MANAGER =
            PerformanceManager.getInstance();

    private static int refreshUpdateTimer = 0;

    @Override
    public void onInitializeClient() {

        System.out.println("[E7ven Optimizer] Client initialized.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            refreshUpdateTimer++;

            /*
             * Обновляем данные монитора примерно раз в секунду.
             */
            if (refreshUpdateTimer >= 20) {
                refreshUpdateTimer = 0;

                REFRESH_RATE_MANAGER.update();
            }

            /*
             * Обновляем всю систему оптимизации.
             */
            PERFORMANCE_MANAGER.update();
        });

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

    public static PerformanceManager getPerformanceManager() {
        return PERFORMANCE_MANAGER;
    }
}
