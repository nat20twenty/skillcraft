package org.nattwenty.skillcraft.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "restoreFrom", at = @At(value = "RETURN"))
    void restoreFrom(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        ServerPlayer player =  (ServerPlayer)(Object) this;
        player.giveExperienceLevels(serverPlayer.experienceLevel);
    }
}
