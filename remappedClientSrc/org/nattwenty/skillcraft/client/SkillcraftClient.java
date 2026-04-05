package org.nattwenty.skillcraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.nattwenty.skillcraft.networking.Networking;
import org.nattwenty.skillcraft.Skillcraft;

public class SkillcraftClient implements ClientModInitializer {
    private KeyMapping openPerkScreen;

    @Override
    public void onInitializeClient() {
        initKeyBindings();
        registerEvents();
        Networking.registerS2CPackets();
    }

    public void initKeyBindings() {
        KeyMapping.Category CATEGORY = new KeyMapping.Category(
                Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "category")
        );

        openPerkScreen = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.skillcraft.open_perk_screen",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_K,
                        CATEGORY
                )
        );
    }

    public void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openPerkScreen.isPressed()) {
                if (client.player != null) {
                    Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                    MinecraftClient.getInstance().setScreen(
                            new SkillCraftPerkScreen()
                    );
                }
            }
        });
    }

    public static boolean requestPerkAdd(String identifier) {
        Minecraft mc = Minecraft.getInstance();
        System.out.println("Client [" + mc.name() + "] requesting perk [" + identifier + "]");
        return false;
    }
}
