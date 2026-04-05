package org.nattwenty.skillcraft.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.nattwenty.skillcraft.components.SkillcraftComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @WrapMethod(method = "giveExperiencePoints")
    private void giveExperiencePoints(int i, Operation<Void> original) {
        Player player = ((Player) (Object) this);
        if (player.experienceLevel < SkillcraftComponents.getMaxLevel(player.asLivingEntity())) {original.call(i);}
    }

    @WrapMethod(method = "giveExperienceLevels")
    private void giveExperienceLevels(int i, Operation<Void> original) {
        Player player = ((Player) (Object) this);
        SkillcraftComponents.modPerkPoints(player.asLivingEntity(), i);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL);
        original.call(i);
    }
}
