package org.nattwenty.skillcraft;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.nattwenty.skillcraft.components.SkillcraftComponents;
import org.nattwenty.skillcraft.events.SkillcraftEvents;
import org.nattwenty.skillcraft.networking.Networking;

import java.util.ArrayList;
import java.util.HashMap;

public class Skillcraft implements ModInitializer {
    public final static String MOD_ID = "skillcraft";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public final static Identifier DEFAULT_PERK_ICON = Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "textures/widget/perk_icon.png");
    public final static Identifier DEFAULT_PERK_BACKGROUND = Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "textures/widget/perk_background.png");
    public final static Identifier DEFAULT_TREE_BACKGROUND = Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "textures/widget/tree_background.png");
    public static int DEFAULT_MAX_LEVEL = 100;
    public static int DEFAULT_EXPERIENCE_BOTTLE_XP = 1;
    public static int[] LEVEL_COLORS = {
            0xFFFFCD44,
            0xFF44FF47,
            0xFF44CDFF,
            0xFF444BFF,
            0xFFBD44FF,
            0xFFFF44C7
    };
    public static ArrayList<Skilltree> skillTreeList = new ArrayList<>();

    @Override
    public void onInitialize() {
        Networking.registerC2SPackets();
        registerCommands();
        SkillcraftEvents.register();
        /**
        Skilltree skilltree = new Skilltree(
                "skillcraft.trees.name.test-perk-tree-1",
                Identifier.parse("textures/item/dragon_breath.png"),
                DEFAULT_TREE_BACKGROUND,
                new Vector4i(-200, -200, 200, 200)
        );
        initExamplePerks(skilltree);
        skillTreeList.add(skilltree);

        skilltree = new Skilltree(
                "skillcraft.trees.name.test-perk-tree-2",
                Identifier.parse("textures/item/enchanted_book.png"),
                DEFAULT_TREE_BACKGROUND,
                new Vector4i(-100, -100, 100, 100)
        );
        initExamplePerks2(skilltree);
        skillTreeList.add(skilltree);
        **/
    }

    public void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("perk_points")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("value", IntegerArgumentType.integer())
                    .executes(Skillcraft::setPerkPoints)
                .then(Commands.argument("name", EntityArgument.player())
                    .executes(Skillcraft::setPerkPointsWithName))
                )
            );
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("perk")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("perk", StringArgumentType.string())
                        .suggests(new PerkSuggestionProvider())
                .then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(Skillcraft::setPerkLevels))
                )
            );
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("getperk")
                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                    .then(Commands.argument("perk", StringArgumentType.string())
                            .suggests(new PerkSuggestionProvider())
                            .executes(Skillcraft::getPerkLevels)

                    )
            );
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("max_level")
                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                    .then(Commands.argument("value", IntegerArgumentType.integer())
                            .executes(Skillcraft::modMaxLevel)
                    )
            );
        });
    }

    public static void addSkillTree(Skilltree skillTree) {
        skillTreeList.add(skillTree);
    }

    public static int getPlayerPerk(Entity entity, String key) {
        return SkillcraftComponents.getPerk(entity, key);
    }

    public void initExamplePerks(Skilltree skilltree) {
        skilltree.clearPerks();
        SkillCraftPerk perk1 = new SkillCraftPerk(
                "skillcraft.perks.name.test-1",
                1,
                "skillcraft.perks.description.test-1",
                Skillcraft.DEFAULT_PERK_ICON,
                Skillcraft.DEFAULT_PERK_BACKGROUND,
                new Vector2i(0, 0),
                1
        );
        SkillCraftPerk perk2 = new SkillCraftPerk(
                "skillcraft.perks.name.test-2",
                3,
                "skillcraft.perks.description.test-2",
                Identifier.parse("textures/item/apple.png"),
                Skillcraft.DEFAULT_PERK_BACKGROUND,
                new Vector2i(-16, 48),
                1
        );
        SkillCraftPerk perk3 = new SkillCraftPerk(
                "skillcraft.perks.name.test-4",
                3,
                "skillcraft.perks.description.test-4",
                Identifier.parse("textures/item/diamond_pickaxe.png"),
                Skillcraft.DEFAULT_PERK_BACKGROUND,
                new Vector2i(120, 96),
                3
        );

        perk1.addChildPerk(perk2);
        perk2.addChildPerk(perk3);

        skilltree.addPerk(perk1);
        skilltree.addPerk(perk2);
        skilltree.addPerk(perk3);
    }

    public void initExamplePerks2(Skilltree skilltree) {
        skilltree.clearPerks();
        SkillCraftPerk perk1 = new SkillCraftPerk(
                "skillcraft.perks.name.test-3",
                1,
                "skillcraft.perks.description.test-3",
                Identifier.parse("textures/item/blaze_powder.png"),
                Skillcraft.DEFAULT_PERK_BACKGROUND,
                new Vector2i(0, 0),
                1
        );
        skilltree.addPerk(perk1);
    }

    public static void requestPerk(String perkId, Entity player) {
        int points = SkillcraftComponents.getPerkPoints(player);
        SkillCraftPerk perk = getPerkByName(perkId);
        System.out.println(perk.toString());


        HashMap<String, Integer> map = SkillcraftComponents.getPerks(player);

        if (perk != null && points >= perk.getPointsPerLevel()) {
            int i = 0;
            ArrayList<SkillCraftPerk> parent_perks = perk.getParentPerks();
            while (i < parent_perks.size() &&
                    map.containsKey(parent_perks.get(i).getSkillName()) &&
                    map.get(parent_perks.get(i).getSkillName()) >= parent_perks.get(i).getSkillMax())
            {
                i++;
            }
            if (i < parent_perks.size()) {return;}

            int value = 1;

            if (map.containsKey(perkId) && map.get(perkId) < perk.getSkillMax()) {
                value = map.get(perkId) + 1;
                SkillcraftComponents.modPerkPoints(player, -perk.getPointsPerLevel());
                SkillcraftComponents.setPerkLevel(player, perkId, value);
            }
            else if (!map.containsKey(perkId)) {
                SkillcraftComponents.modPerkPoints(player, -perk.getPointsPerLevel());
                SkillcraftComponents.setPerkLevel(player, perkId, value);
            }
        }

        LOGGER.info("Player " + player.getPlainTextName() + " requested perk id " + perkId + " [" + points + "] ");
    }

    public static void requestPrestige(Entity player) {
        Player p = (Player) player;
        int max_level = SkillcraftComponents.getMaxLevel(player);

        if ((p.experienceLevel >= max_level)) {
            p.experienceLevel = 0;
            p.totalExperience = 0;
            SkillcraftComponents.modMaxLevel(player, Skillcraft.DEFAULT_MAX_LEVEL);
            int perk_points = SkillcraftComponents.getPerkPoints(player);
            SkillcraftComponents.modPerkPoints(player, -perk_points);
            SkillcraftComponents.clearPerks(player);

            int x = max_level / Skillcraft.DEFAULT_MAX_LEVEL;
            MutableComponent message = Component.translatable("skillcraft.chat.prestige-message", p.getPlainTextName(), x);
            Style style = message.getStyle().withBold(true).withColor(LEVEL_COLORS[Math.clamp(x - 1, 0, LEVEL_COLORS.length - 1)]);
            message.setStyle(style);

            for (ServerPlayer pl : player.level().getServer().getPlayerList().getPlayers()) {
                pl.level().playSound(null, pl.getOnPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.NEUTRAL);
                pl.sendSystemMessage(message);
            }
        }
    }

    private static int setPerkPoints(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int value = IntegerArgumentType.getInteger(context, "value");
        Entity player = context.getSource().getPlayer().getLivingEntity();

        SkillcraftComponents.modPerkPoints(player, value);
        int perk_points = SkillcraftComponents.getPerkPoints(player);

        context.getSource().sendSuccess(() -> Component.literal("%s modified their own perk points by: %s [%s]".formatted(player.getPlainTextName(), value, perk_points)), true);
        return 1;
    }
    private static int setPerkPointsWithName(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int value = IntegerArgumentType.getInteger(context, "value");
        ServerPlayer target1 = EntityArgument.getPlayer(context, "name");
        Entity target = target1.getLivingEntity();

        SkillcraftComponents.modPerkPoints(target, value);
        int perk_points = SkillcraftComponents.getPerkPoints(target);

        context.getSource().sendSuccess(() -> Component.literal("%s modified %s's perk points by: %s [%s]".formatted(context.getSource().getTextName(), target.getPlainTextName(), value, perk_points)), true);
        return 1;
    }

    private static int setPerkLevels(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity player = context.getSource().getPlayer().getLivingEntity();
        String key = StringArgumentType.getString(context, "perk");
        int value = IntegerArgumentType.getInteger(context, "value");

        SkillcraftComponents.setPerkLevel(player, key, value);
        context.getSource().sendSuccess(() -> Component.literal("%s set their own perk %s to level %s".formatted(player.getPlainTextName(), key, value)), true);

        return 1;
    }

    public static SkillCraftPerk getPerkByName(String key) {
        for (Skilltree t : skillTreeList) {
            for (SkillCraftPerk perk : t.getPerks()) {
                if (perk.getSkillName().equals(key)) {
                    return perk;
                }
            }
        }

        return null;
    }

    private static int getPerkLevels(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity player = context.getSource().getPlayer().getLivingEntity();
        String key = StringArgumentType.getString(context, "perk");
        int value = SkillcraftComponents.getPerk(player, key);

        context.getSource().sendSuccess(() -> Component.literal("%s's perk %s is at level %s".formatted(player.getPlainTextName(), key, value)), false);

        return 1;
    }

    private static int modMaxLevel(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity player = context.getSource().getPlayer().getLivingEntity();
        int value = IntegerArgumentType.getInteger(context, "value");
        SkillcraftComponents.modMaxLevel(player, value);

        context.getSource().sendSuccess(() -> Component.literal("Modified %s's max level by %s [%s]".formatted(player.getPlainTextName(), value, SkillcraftComponents.getMaxLevel(player))), false);

        return 1;
    }
}
