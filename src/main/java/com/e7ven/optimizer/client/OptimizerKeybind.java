package com.e7ven.optimizer.client;

import com.e7ven.optimizer.client.gui.OptimizerScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class OptimizerKeybind {

    private static final String CATEGORY =
            "key.categories.e7venoptimizer";

    private static final String KEY =
            "key.e7venoptimizer.open_gui";

    private static KeyBinding openGuiKey;

    private OptimizerKeybind() {
    }

    public static void register() {

        openGuiKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        KEY,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F8,
                        CATEGORY
                )
        );
    }

    public static void handleInput() {

        if (openGuiKey == null) {
            return;
        }

        while (openGuiKey.wasPressed()) {

            E7venOptimizerClient.openOptimizerScreen();
        }
    }
}
