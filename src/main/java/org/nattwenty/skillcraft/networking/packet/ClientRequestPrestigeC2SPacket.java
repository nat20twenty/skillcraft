package org.nattwenty.skillcraft.networking.packet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.nattwenty.skillcraft.Skillcraft;

public record ClientRequestPrestigeC2SPacket(boolean emptyValue) implements CustomPacketPayload {
    public static final Identifier CLIENT_REQUEST_PRESTIGE = Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "client_request_prestige");
    public static final CustomPacketPayload.Type<ClientRequestPrestigeC2SPacket> ID = new  CustomPacketPayload.Type<>(CLIENT_REQUEST_PRESTIGE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientRequestPrestigeC2SPacket> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, ClientRequestPrestigeC2SPacket::emptyValue, ClientRequestPrestigeC2SPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}