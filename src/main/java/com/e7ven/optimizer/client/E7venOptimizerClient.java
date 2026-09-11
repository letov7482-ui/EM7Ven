package com.e7ven.optimizer.client;

import com.e7ven.optimizer.performance.FrameTimeMonitor;
import com.e7ven.optimizer.performance.OptimizerController;
import com.e7ven.optimizer.mixin.GameRendererMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class E7venOptimizerClient implements ClientModInitializer {

    private static final RefreshRateManager REFRESH_RATE_MANAGER =
            RefreshRateManager.getInstance();

    private static final FrameTimeMonitor FRAME_TIME_MONITOR =
            GameRendererMixin.e7ven$getFrameTimeMonitor();

    private static final OptimizerController OPTIMIZER =
            new OptimizerController(
                    REFRESH_RATE_MANAGER,
                    FRAME_TIME_MONITOR
            );

    private static int refreshUpdateTimer = 0;

    @Override
    public void onInitializeClient() {

        System.out.println("[E7ven Optimizer] Client initialized.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            /*
             * Обновляем частоту экрана примерно раз в секунду.
             * Это нужно, чтобы изменение окна/монитора
             * автоматически подхватывалось оптимизатором.
             */
            refreshUpdateTimer++;

            if (refreshUpdateTimer >= 20) {
                refreshUpdateTimer = 0;

                REFRESH_RATE_MANAGER.update();
            }

            /*
             * Обновляем состояние оптимизатора.
             */
            OPTIMIZER.update();
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

    public static FrameTimeMonitor getFrameTimeMonitor() {
        return FRAME_TIME_MONITOR;
    }

    public static OptimizerController getOptimizer() {
        return OPTIMIZER;
    }
}
