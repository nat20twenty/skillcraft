package org.nattwenty.skillcraft.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.nattwenty.skillcraft.SkillCraftPerk;
import org.nattwenty.skillcraft.Skillcraft;
import org.nattwenty.skillcraft.Skilltree;

public class SkillCraftPerkScreen extends Screen {
    private static final int ELEMENT_HEIGHT = 20;
    private static final int PERK_SIZE = 24;
    private static final int OFFSET_MAX = 1000;

    private static int tree_selected = 0;
    private static double offset_x = 0;
    private static double offset_y = 0;
    private static Vector4i offset_bounds = new Vector4i(10, 10, 10, 10);

    public SkillCraftPerkScreen() {
        super(Component.nullToEmpty(Skillcraft.MOD_ID + "PerkScreen"));
    }

    @Override
    protected void init() {
        initPerks();
        initCloseButton();
    }

    private void initCloseButton() {
        int closeButtonWidth = 100;
        int closeButtonX = width - closeButtonWidth - 4;
        int closeButtonY = height - ELEMENT_HEIGHT - 4;

        Button closeButton = Button.builder(
                Component.nullToEmpty("Close"), button -> minecraft.setScreen(null)
        ).bounds(closeButtonX, closeButtonY, closeButtonWidth, ELEMENT_HEIGHT).build();

        addRenderableWidget(closeButton);
    }

    private void initPerks() {
        Vector2i screen_center = new Vector2i(width/2, height/2);
        int skill_trees = 0;
        if (!Skillcraft.skillTreeList.isEmpty()) {offset_bounds = Skillcraft.skillTreeList.getFirst().getOffset_bounds();}
        for (Skilltree t : Skillcraft.skillTreeList) {
            SkilltreePickerWidget treeWidget = new SkilltreePickerWidget(
                    24 + (skill_trees * 24),
                    0,
                    PERK_SIZE,
                    PERK_SIZE,
                    t.getIconPath(),
                    t.getBackgroundPath(),
                    Component.translatable(t.getTreeName()),
                    skill_trees,
                    t.getOffset_bounds()
            );
            addRenderableWidget(treeWidget);
            for (SkillCraftPerk perk : t.getPerks()) {
                PerkWidget perkWidget = new PerkWidget(
                        screen_center.x - perk.getSkillPosition().x,
                        screen_center.y - perk.getSkillPosition().y,
                        PERK_SIZE,
                        PERK_SIZE,
                        perk.getIconPath(),
                        perk.getBackgroundPath(),
                        Component.translatable(perk.getSkillName(), 0, perk.getSkillMax()),
                        0 == perk.getSkillMax(),
                        perk.getSkillIdentifier(),
                        skill_trees
                );
                perkWidget.setTooltip(Tooltip.create(Component.translatable(perk.getSkillDescription())));
                addRenderableWidget(perkWidget);
            }

            skill_trees++;
        }
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        offset_x += offsetX;
        offset_y += offsetY;
        offset_x = Math.clamp(offset_x, offset_bounds.x, offset_bounds.z);
        offset_y = Math.clamp(offset_y, offset_bounds.y, offset_bounds.w);

        return (offsetX + offsetY) > 0d;
    }

    public static void setSelected(int selected) {tree_selected = selected;}
    public static int getSelected() {return tree_selected;}
    public static double getOffset_x() {return offset_x;}
    public static double getOffset_y() {return offset_y;}
    public static void setOffset_x(double x) {offset_x = x;}
    public static void setOffset_y(double y) {offset_y = y;}
    public static void setOffset_bounds(Vector4i bounds) {offset_bounds = bounds;}
}
