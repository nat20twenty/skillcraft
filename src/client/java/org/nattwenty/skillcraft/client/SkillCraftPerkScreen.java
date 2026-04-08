package org.nattwenty.skillcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.nattwenty.skillcraft.SkillCraftPerk;
import org.nattwenty.skillcraft.Skillcraft;
import org.nattwenty.skillcraft.Skilltree;
import org.nattwenty.skillcraft.components.SkillcraftComponents;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class SkillCraftPerkScreen extends Screen {
    private static final int ELEMENT_HEIGHT = 20;
    private static final int PERK_SIZE = 24;

    private static double offset_x = 0;
    private static double offset_y = 0;
    private static Vector4i offset_bounds = new Vector4i(10, 10, 10, 10);

    private static HashMap<String, Integer> perk_levels;
    private static int skillPoints;
    private static int max_level;
    private static boolean can_prestiege;
    private static long update_screen = -1;

    private static SkillCraftPerk selected_perk1;
    private static Skilltree selected_tree1;
    private ArrayList<PerkWidget> p_widgets;

    public SkillCraftPerkScreen() {
        super(Component.nullToEmpty(Skillcraft.MOD_ID + "PerkScreen"));
    }

    @Override
    protected void init() {
        p_widgets = new ArrayList<>();
        if (Skillcraft.skillTreeList.isEmpty()) {
            initCloseButton();
            initNoSkillTrees();

            return;
        }
        updatePerkPoints();
        updatePerkLevels();
        updateMaxLevel();
        initPerkLines();
        initPerks();
        initOverlay();
        initCloseButton();
        initPerkButton();
        initPrestigeButton();
        updateTooltips();
    }

    private void initNoSkillTrees() {
        Font renderer = Minecraft.getInstance().font;
        MutableComponent name = Component.translatable("skillcraft.ui.no-skilltrees-message");
        int x = (width - renderer.width(name)) / 2;
        int y = (height - renderer.lineHeight) / 2;

        StringWidget sw = new StringWidget(x, y, renderer.width(name), renderer.lineHeight, name, renderer);
        addRenderableWidget(sw);
    }

    private void initCloseButton() {
        Component name = Component.translatable("skillcraft.button.close");
        int closeButtonWidth = 100;
        int closeButtonX = width - closeButtonWidth - 4;
        int closeButtonY = height - ELEMENT_HEIGHT - 4;

        Button closeButton = Button.builder(
                name, button -> minecraft.setScreen(null)
        ).bounds(closeButtonX, closeButtonY, closeButtonWidth, ELEMENT_HEIGHT).build();

        addRenderableWidget(closeButton);
    }

    private void initPerkButton() {
        int perkButtonX = 4;
        int perkButtonY = height - ELEMENT_HEIGHT - 4;

        Button perkButton = Button.builder(
                Component.nullToEmpty("+"), button -> requestPerk()
        ).bounds(perkButtonX, perkButtonY, ELEMENT_HEIGHT, ELEMENT_HEIGHT).build();

        addRenderableWidget(perkButton);
    }

    public void initPrestigeButton() {
        Font renderer = Minecraft.getInstance().font;
        if (!can_prestiege) {
            Component name = Component.translatable("skillcraft.ui.max-level-message", max_level);
            int mlwWidth = renderer.width(name);
            int mlwX = (width - mlwWidth) / 2;
            int mlwY = height - ELEMENT_HEIGHT - 4;
            MaxLevelWidget mlw = new MaxLevelWidget(mlwX, mlwY, mlwWidth, ELEMENT_HEIGHT, name);

            addRenderableWidget(mlw);
        }
        else {

            Component name = Component.translatable("skillcraft.button.prestige");
            int prestiegeButtonWidth = renderer.width(name) + 20;
            int prestiegeButtonX = (width - prestiegeButtonWidth) / 2;
            int prestiegeButtonY = height - ELEMENT_HEIGHT - 4;

            Button prestiegeButton = Button.builder(
                    name, button ->{requestPrestige();}
            ).bounds(prestiegeButtonX, prestiegeButtonY, prestiegeButtonWidth, ELEMENT_HEIGHT).build();

            addRenderableWidget(prestiegeButton);
        }
    }

    private void initPerkLines() {
        for (Skilltree t : Skillcraft.skillTreeList) {
            ArrayList<Vector4i> perk_lines = new ArrayList<>();
            for (SkillCraftPerk p : t.getPerks()) {
                for (SkillCraftPerk child : p.getChildPerks()) {
                    perk_lines.add(new Vector4i(p.getSkillPosition().x, p.getSkillPosition().y, child.getSkillPosition().x, child.getSkillPosition().y));
                }
            }
            PerkLineSegment pls = new PerkLineSegment(
                    width/2,
                    height/2,
                    t,
                    perk_lines
            );
            addRenderableWidget(pls);
        }
    }

    private void initPerks() {
        Vector2i screen_center = new Vector2i(width/2, height/2);
        selected_tree1 = Skillcraft.skillTreeList.getFirst();
        int skill_trees = 0;
        if (!Skillcraft.skillTreeList.isEmpty()) {offset_bounds = Skillcraft.skillTreeList.getFirst().getOffset_bounds();}
        for (Skilltree t : Skillcraft.skillTreeList) {
            SkilltreePickerWidget treeWidget = new SkilltreePickerWidget(
                    24 + (skill_trees * 24),
                    0,
                    PERK_SIZE,
                    PERK_SIZE,
                    t
            );
            addRenderableWidget(treeWidget);
            for (SkillCraftPerk perk : t.getPerks()) {
                PerkWidget perkWidget = new PerkWidget(
                        screen_center.x - perk.getSkillPosition().x - PERK_SIZE/2,
                        screen_center.y - perk.getSkillPosition().y - PERK_SIZE/2,
                        PERK_SIZE,
                        PERK_SIZE,
                        perk,
                        t
                );
                MutableComponent name = Component.translatable(perk.getSkillName(), perk_levels.get(perk.getSkillName()));
                perkWidget.setTooltip(Tooltip.create(name.append("\n").append(Component.translatable(perk.getSkillDescription()))));
                p_widgets.add(perkWidget);
                addRenderableWidget(perkWidget);
            }
            skill_trees++;
        }
    }

    private void updateTooltips() {
        for (PerkWidget w : p_widgets) {
            w.updateTooltip();
        }
    }

    public void initOverlay() {
        SkillExperienceWidget skillExperienceWidget = new SkillExperienceWidget(
                12 + ELEMENT_HEIGHT,
                height - 4 - (ELEMENT_HEIGHT + Minecraft.getInstance().font.lineHeight)/2,
                0,
                0
        );
        addRenderableWidget(skillExperienceWidget);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        if (update_screen != -1 && Instant.now().getEpochSecond() >= update_screen) {
            updatePerkPoints();
            updatePerkLevels();
            updateMaxLevel();
            updateTooltips();
            update_screen = Instant.now().getEpochSecond() + 10;
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

    public static void setSelectedPerk(SkillCraftPerk perk) {
        selected_perk1 = perk;
    }
    public static SkillCraftPerk getSelectedPerk() {
        return selected_perk1;
    }

    public static Skilltree getSelectedTree() {return selected_tree1;}

    public static void setSelected(Skilltree tree) {selected_tree1 = tree;}
    public static double getOffset_x() {return offset_x;}
    public static double getOffset_y() {return offset_y;}
    public static void setOffset_x(double x) {offset_x = x;}
    public static void setOffset_y(double y) {offset_y = y;}
    public static void setOffset_bounds(Vector4i bounds) {offset_bounds = bounds;}

    public static void updatePerkPoints() {skillPoints = SkillcraftComponents.getPerkPoints(Minecraft.getInstance().player.asLivingEntity());}
    public static void updatePerkLevels() {perk_levels = SkillcraftComponents.getPerks(Minecraft.getInstance().player.asLivingEntity());}
    public static void updateMaxLevel() {
        max_level = SkillcraftComponents.getMaxLevel(Minecraft.getInstance().player.asLivingEntity());
        can_prestiege = (Minecraft.getInstance().player.experienceLevel >= max_level);
    }
    public static int getPerkPoints() {return skillPoints;}
    public static int getPerkLevel(String key) {
        int value = 0;
        if (perk_levels.containsKey(key)) {
            value = perk_levels.get(key);
        }

        return value;
    }
    public static void requestPerk() {
        if (selected_perk1 != null &&skillPoints >= selected_perk1.getPointsPerLevel() && (!perk_levels.containsKey(selected_perk1.getSkillName()) || perk_levels.get(selected_perk1.getSkillName()) < selected_perk1.getSkillMax())) {

            int i = 0;
            ArrayList<SkillCraftPerk> parent_perks = selected_perk1.getParentPerks();
            while (i < parent_perks.size() &&
                    perk_levels.containsKey(parent_perks.get(i).getSkillName()) &&
                    perk_levels.get(parent_perks.get(i).getSkillName()) >= parent_perks.get(i).getSkillMax())
            {
                i++;
            }
            if (i >= parent_perks.size()) {
                int new_level = 1;

                if (perk_levels.containsKey(selected_perk1.getSkillName())) {
                    new_level += perk_levels.get(selected_perk1.getSkillName());
                }

                SkillcraftClient.requestPerk(selected_perk1);
                Minecraft.getInstance().player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 4.0f, 1.0f);
                perk_levels.put(selected_perk1.getSkillName(), new_level);
                skillPoints -= selected_perk1.getPointsPerLevel();
                update_screen = Instant.now().getEpochSecond() + 1;
            }
            else {
                Minecraft.getInstance().player.playSound(SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 4.0f, 1.0f);
            }
        }
        else {
            Minecraft.getInstance().player.playSound(SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 4.0f, 1.0f);
        }
    }

    public static void requestPrestige() {
        if (can_prestiege) {
            SkillcraftClient.requestPrestige();
            Minecraft.getInstance().player.experienceLevel = 0;
            Minecraft.getInstance().player.totalExperience = 0;
            skillPoints = 0;
            perk_levels.clear();
            max_level += Skillcraft.DEFAULT_MAX_LEVEL;
            can_prestiege = false;
            Minecraft.getInstance().setScreen(null);

        }
        update_screen = Instant.now().getEpochSecond() + 2;
    }
}
