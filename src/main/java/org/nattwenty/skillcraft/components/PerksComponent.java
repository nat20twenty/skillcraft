package org.nattwenty.skillcraft.components;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.HashMap;

public class PerksComponent implements HashMapComponent, AutoSyncedComponent {
    private HashMap<String, Integer> map;
    private String encoded_data;
    private final Object provider;

    public PerksComponent(Object provider) {
        this.map =  new HashMap<>();
        this.encoded_data = "";
        this.provider = provider;
    }

    @Override
    public HashMap<String, Integer> getMap() {
        return this.map;
    }

    public int getKey(String perk) {
        return this.map.getOrDefault(perk, 0);
    }

    @Override
    public void set(String key, Integer value) {
        this.map.put(key,value);
        hashmapToString();
        SkillcraftComponents.PERKSCOMPONENT.sync(this.provider);
    }

    @Override
    public void remove(String key) {
        this.map.remove(key);
        hashmapToString();
        SkillcraftComponents.PERKSCOMPONENT.sync(this.provider);
    }

    @Override
    public void clear() {
        this.map.clear();
        hashmapToString();
        SkillcraftComponents.PERKSCOMPONENT.sync(this.provider);
    }

    @Override
    public void readData(ValueInput readView) {
        String data = readView.getStringOr("value", "");
        this.stringToHashMap(data);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putString("value", hashmapToString());
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeUtf(hashmapToString());
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        this.stringToHashMap(buf.readUtf());
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return player == this.provider;
    }

    private void stringToHashMap(String data) {
        if (data.isEmpty()) return;
        String[] data_split = data.split(",");

        for (String s : data_split) {
            if (s.length() < 3) continue;
            String[] components = s.split(":");
            String key = components[0].trim();
            Integer value = Integer.valueOf(components[1].trim());
            this.map.put(key,value);
        }
    }

    private String hashmapToString() {
        this.encoded_data = "";
        for (String key : this.map.keySet()) {
            this.encoded_data += key + ":" + this.map.get(key) + ",";
        }

        return this.encoded_data;
    }
}
