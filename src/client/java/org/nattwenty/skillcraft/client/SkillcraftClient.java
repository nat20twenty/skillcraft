package org.nattwenty.skillcraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.lwjgl.glfw.GLFW;
import org.nattwenty.skillcraft.SkillCraftPerk;
import org.nattwenty.skillcraft.components.SkillcraftComponents;
import org.nattwenty.skillcraft.networking.Networking;
import org.nattwenty.skillcraft.Skillcraft;
import org.nattwenty.skillcraft.networking.packet.ClientRequestPerkC2SPacket;
import org.nattwenty.skillcraft.networking.packet.ClientRequestPrestigeC2SPacket;

public class SkillcraftClient implements ClientModInitializer {
    private KeyMapping openPerkScreen;
    private static int skillPoints = 0;

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
            while (openPerkScreen.isDown()) {
                if (client.player != null) {
                    Screen currentScreen = Minecraft.getInstance().screen;
                    Minecraft.getInstance().setScreen(
                            new SkillCraftPerkScreen()
                    );
                }
            }

            if (client.player != null) {
                Entity player = client.player.asLivingEntity();
                skillPoints = SkillcraftComponents.getPerkPoints(player);
            }
        });
    }

    public static int getPerkPoints() {return skillPoints;}

    public static void requestPerk(SkillCraftPerk perk) {
        ClientRequestPerkC2SPacket payload = new ClientRequestPerkC2SPacket(perk.getSkillName());
        ClientPlayNetworking.send(payload);
    }

    public static void requestPrestige() {
        ClientRequestPrestigeC2SPacket payload = new ClientRequestPrestigeC2SPacket(true);
        ClientPlayNetworking.send(payload);
    }
}
