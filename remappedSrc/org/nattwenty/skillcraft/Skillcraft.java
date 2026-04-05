package org.nattwenty.skillcraft;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.nattwenty.skillcraft.networking.Networking;

import java.util.ArrayList;

public class Skillcraft implements ModInitializer {
    public final static String MOD_ID = "skillcraft";

    public final static Identifier DEFAULT_PERK_ICON = Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "textures/widget/perk_icon.png");
    public final static Identifier DEFAULT_PERK_BACKGROUND = Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "textures/widget/perk_background.png");
    public final static Identifier DEFAULT_TREE_BACKGROUND = Identifier.fromNamespaceAndPath(Skillcraft.MOD_ID, "textures/widget/tree_background.png");
    public static ArrayList<Skilltree> skillTreeList = new ArrayList<>();

    @Override
    public void onInitialize() {
        Skilltree skilltree = new Skilltree(
                "skillcraft.perks.test-perk-tree-1-name",
                Identifier.parse("textures/item/brewing_stand.png"),
                DEFAULT_TREE_BACKGROUND,
                new Vector4i(-200, -200, 200, 200)
        );
        initExamplePerks(skilltree);
        skillTreeList.add(skilltree);

        skilltree = new Skilltree(
                "skillcraft.perks.test-perk-tree-2-name",
                Identifier.parse("textures/item/enchanted_book.png"),
                DEFAULT_TREE_BACKGROUND,
                new Vector4i(-100, -100, 100, 100)
        );
        initExamplePerks2(skilltree);
        skillTreeList.add(skilltree);

        Networking.registerC2SPackets();
    }

    public void initExamplePerks(Skilltree skilltree) {
        skilltree.clearPerks();
        SkillCraftPerk perk1 = new SkillCraftPerk(
                "PERK_1",
                "skillcraft.perks.test-1-name",
                1,
                "skillcraft.perks.test-1-description",
                Skillcraft.DEFAULT_PERK_ICON,
                Skillcraft.DEFAULT_PERK_BACKGROUND,
                new Vector2i(0, 0)
        );
        SkillCraftPerk perk2 = new SkillCraftPerk(
                "PERK_2",
                "skillcraft.perks.test-2-name",
                3,
                "skillcraft.perks.test-2-description",
                Identifier.parse("textures/item/apple.png"),
                Skillcraft.DEFAULT_PERK_BACKGROUND,
                new Vector2i(0, 48)
        );
        perk1.addChildPerk(perk2);

        skilltree.addPerk(perk1);
        skilltree.addPerk(perk2);
    }

    public void initExamplePerks2(Skilltree skilltree) {
        skilltree.clearPerks();
        SkillCraftPerk perk1 = new SkillCraftPerk(
                "PERK_3",
                "skillcraft.perks.test-3-name",
                1,
                "skillcraft.perks.test-3-description",
                Identifier.parse("textures/item/blaze_powder.png"),
                Skillcraft.DEFAULT_PERK_BACKGROUND,
                new Vector2i(0, 0)
        );
        skilltree.addPerk(perk1);
    }
}
