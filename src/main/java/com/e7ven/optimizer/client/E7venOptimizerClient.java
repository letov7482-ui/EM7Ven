package com.e7ven.optimizer.client;

import com.e7ven.optimizer.client.gui.OptimizerScreen;
import com.e7ven.optimizer.performance.PerformanceManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public final class E7venOptimizerClient implements ClientModInitializer {

    private static final RefreshRateManager REFRESH_RATE_MANAGER =
            RefreshRateManager.getInstance();

    private static final PerformanceManager PERFORMANCE_MANAGER =
            PerformanceManager.getInstance();

    private static int refreshUpdateTimer = 0;

    @Override
    public void onInitializeClient() {

        System.out.println(
                "[E7ven Optimizer] Client initialized."
        );

        /*
         * Регистрируем клавишу F8.
         */
        OptimizerKeybind.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            /*
             * Обработка клавиши.
             */
            OptimizerKeybind.handleInput();

            /*
             * Обновляем частоту экрана примерно раз в секунду.
             */
            refreshUpdateTimer++;

            if (refreshUpdateTimer >= 20) {
                refreshUpdateTimer = 0;

                REFRESH_RATE_MANAGER.update();
            }

            /*
             * Обновляем систему оптимизации.
             */
            PERFORMANCE_MANAGER.update();
        });

        System.out.println(
                "[E7ven Optimizer] Display: "
                        + REFRESH_RATE_MANAGER
                        .getRefreshRateText()
        );

        System.out.println(
                "[E7ven Optimizer] Target Frame Time: "
                        + REFRESH_RATE_MANAGER
                        .getTargetFrameTimeText()
        );
    }

    public static void openOptimizerScreen() {

        MinecraftClient client =
                MinecraftClient.getInstance();

        if (client == null) {
            return;
        }

        /*
         * Не открываем экран поверх другого GUI.
         */
        if (client.currentScreen != null) {
            return;
        }

        client.setScreen(
                new OptimizerScreen()
        );
    }

    public static RefreshRateManager getRefreshRateManager() {
        return REFRESH_RATE_MANAGER;
    }

    public static PerformanceManager getPerformanceManager() {
        return PERFORMANCE_MANAGER;
    }
}
