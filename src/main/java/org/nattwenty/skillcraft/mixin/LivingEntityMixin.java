package org.nattwenty.skillcraft.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapMethod(method = "dropExperience")
    public void dropExperience(ServerLevel serverLevel, Entity entity, Operation<Void> original) {
        if (!(entity instanceof Player)) {original.call(serverLevel, entity);}
    }
}
