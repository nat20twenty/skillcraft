package org.nattwenty.skillcraft.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.Entity;
import org.nattwenty.skillcraft.Skillcraft;
import org.nattwenty.skillcraft.networking.packet.ClientRequestPerkC2SPacket;
import org.nattwenty.skillcraft.networking.packet.ClientRequestPrestigeC2SPacket;

public class Networking {

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(ClientRequestPerkC2SPacket.ID, ClientRequestPerkC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(ClientRequestPrestigeC2SPacket.ID, ClientRequestPrestigeC2SPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ClientRequestPerkC2SPacket.ID, (payload, context) -> {
            String id = payload.perk_id();
            Entity player = context.player().getLivingEntity();

            Skillcraft.requestPerk(id, player);
        });

        ServerPlayNetworking.registerGlobalReceiver(ClientRequestPrestigeC2SPacket.ID, (payload, context) -> {
            Entity player = context.player().getLivingEntity();
            Skillcraft.requestPrestige(player);
        });
    }

    public static void registerS2CPackets() {

    }
}
