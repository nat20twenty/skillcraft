package org.nattwenty.skillcraft.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @WrapMethod(method = "addFreshEntity")
    private boolean addFreshEntity(Entity entity, Operation<Boolean> original) {
        if (!(entity instanceof ExperienceOrb)) {return original.call(entity);}
        return false;
    }
}
