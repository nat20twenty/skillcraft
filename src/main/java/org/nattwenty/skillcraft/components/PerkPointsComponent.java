package org.nattwenty.skillcraft.components;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class PerkPointsComponent implements IntComponent, AutoSyncedComponent {
    private int value = 0;
    private final Object provider;

    public PerkPointsComponent(Object provider) {
        this.provider = provider;
    }

    @Override public int getValue() {return this.value;}
    @Override public void modValue(int mod) {
        this.value += mod;
        SkillcraftComponents.PERKPOINTSCOMPONENT.sync(this.provider);
    }

    @Override
    public void readData(ValueInput valueInput) {
        this.value = valueInput.getIntOr("value", 0);
    }

    @Override
    public void writeData(ValueOutput valueOutput) {
        valueOutput.putInt("value", this.value);
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeVarInt(this.value);
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        this.value = buf.readVarInt();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return player == this.provider;
    }
}
