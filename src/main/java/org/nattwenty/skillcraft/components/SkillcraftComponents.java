package org.nattwenty.skillcraft.components;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.nattwenty.skillcraft.Skillcraft;

import java.util.HashMap;

import static org.nattwenty.skillcraft.Skillcraft.MOD_ID;

public class SkillcraftComponents implements EntityComponentInitializer {
    public static final ComponentKey<IntComponent> PERKPOINTSCOMPONENT =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(MOD_ID, "perk_points"), IntComponent.class);
    public static final ComponentKey<HashMapComponent> PERKSCOMPONENT =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(MOD_ID, "perks"), HashMapComponent.class);
    public static final ComponentKey<IntComponent> MAXLEVELCOMPONENT =
            ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(MOD_ID, "max_level"), IntComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(PERKPOINTSCOMPONENT, PerkPointsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        entityComponentFactoryRegistry.registerForPlayers(PERKSCOMPONENT, PerksComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        entityComponentFactoryRegistry.registerForPlayers(MAXLEVELCOMPONENT, MaxLevelComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static int getPerkPoints(Entity provider) {
        return PERKPOINTSCOMPONENT.maybeGet(provider).map(IntComponent::getValue).orElse(0);
    }
    public static void modPerkPoints(Entity provider, int mod) {
        PERKPOINTSCOMPONENT.get(provider).modValue(mod);
    }

    public static HashMap<String, Integer> getPerks(Entity provider) {
        return PERKSCOMPONENT.maybeGet(provider).map(HashMapComponent::getMap).orElse(new HashMap<>());
    }

    public static int getPerk(Entity provider, String key) {
        return PERKSCOMPONENT.get(provider).getKey(key);
    }
    public static void setPerkLevel(Entity provider, String key, int value) {
        PERKSCOMPONENT.get(provider).set(key, value);
    }

    public static void clearPerks(Entity provider) {
        PERKSCOMPONENT.get(provider).clear();
    }

    public static int getMaxLevel(Entity provider) {
        return MAXLEVELCOMPONENT.maybeGet(provider).map(IntComponent::getValue).orElse(Skillcraft.DEFAULT_MAX_LEVEL);
    }
    public static void modMaxLevel(Entity provider, int mod) {
        MAXLEVELCOMPONENT.get(provider).modValue(mod);
    }
}
