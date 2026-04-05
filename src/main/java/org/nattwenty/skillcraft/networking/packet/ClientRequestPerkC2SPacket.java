package org.nattwenty.skillcraft.networking.packet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.nattwenty.skillcraft.Skillcraft;

public record ClientRequestPerkC2SPacket(String perk_id) implements CustomPacketPayload {
    public static final Identifier CLIENT_REQUEST_PERK_ID = Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "client_request_perk");
    public static final CustomPacketPayload.Type<ClientRequestPerkC2SPacket> ID = new  CustomPacketPayload.Type<>(CLIENT_REQUEST_PERK_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientRequestPerkC2SPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ClientRequestPerkC2SPacket::perk_id, ClientRequestPerkC2SPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
