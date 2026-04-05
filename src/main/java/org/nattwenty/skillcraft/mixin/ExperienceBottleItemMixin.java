package org.nattwenty.skillcraft.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.fabric.mixin.transfer.BucketItemMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.nattwenty.skillcraft.SkillCraftPerk;
import org.nattwenty.skillcraft.Skillcraft;
import org.nattwenty.skillcraft.components.SkillcraftComponents;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExperienceBottleItem.class)
public class ExperienceBottleItemMixin {
    @WrapMethod(method = "use")
    private InteractionResult use(Level level, Player player, InteractionHand interactionHand, Operation<InteractionResult> original) {
        if (player.experienceLevel >= SkillcraftComponents.getMaxLevel(player)) {
            return InteractionResult.FAIL;
        }
        ItemStack itemStack = player.getItemInHand(interactionHand);
        level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 0.4F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.0F));
        level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.0F));
        if (level instanceof ServerLevel serverLevel) {
            player.giveExperiencePoints(Skillcraft.DEFAULT_EXPERIENCE_BOTTLE_XP);
        }

        player.awardStat(Stats.ITEM_USED.get(Items.EXPERIENCE_BOTTLE));
        itemStack.consume(1, player);
        return InteractionResult.CONSUME;
    }
}
