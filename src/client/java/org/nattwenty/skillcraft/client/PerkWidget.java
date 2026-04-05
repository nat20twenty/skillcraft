package org.nattwenty.skillcraft.client;

import javax.swing.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.nattwenty.skillcraft.SkillCraftPerk;
import org.nattwenty.skillcraft.Skilltree;

public class PerkWidget extends AbstractWidget {
    private final SkillCraftPerk perk;
    private final Skilltree tree;


    public PerkWidget(int x, int y, int width, int height, SkillCraftPerk perk, Skilltree tree) {
        super(x, y, width, height, null);
        this.perk = perk;
        this.tree = tree;
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (!SkillCraftPerkScreen.getSelectedTree().equals(tree)) {return;}

        Font renderer = Minecraft.getInstance().font;
        int center_x = getX() + Math.round(width/2f);
        int size_offset = Math.round((width - 16)/2f);
        Component name = Component.translatable(this.perk.getSkillName(), SkillCraftPerkScreen.getPerkLevel(this.perk.getSkillName()), this.perk.getSkillMax());

        //If any level of the perk is unlocked, use the clicked icon.
        float h = 0.0f;
        float v = 0.0f;

        if (SkillCraftPerkScreen.getPerkLevel(this.perk.getSkillName()) >= this.perk.getSkillMax()) {h = 24.0f;}
        if (this.perk.equals(SkillCraftPerkScreen.getSelectedPerk())) {v = 24.0f;}

        context.blit(
            RenderPipelines.GUI_TEXTURED,
            this.perk.getBackgroundPath(),
            getX(),
            getY(),
            h,
            v,
            width,
            height,
            width,
            height,
            width*2,
            height*2
        );
        context.blit(
                RenderPipelines.GUI_TEXTURED,
                this.perk.getIconPath(),
                getX()+size_offset,
                getY()+size_offset,
                0.0f,
                0.0f,
                16,
                16,
                16,
                16
        );
        context.drawString(renderer, name, center_x - renderer.width(name)/2, getY() + height + 1, 0xF0F0F0FF, true);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        return;
    }

    @Override
    public boolean isActive() {
        return this.tree.equals(SkillCraftPerkScreen.getSelectedTree());
    }
    
    @Override
    public boolean isHovered() {
        if (!this.tree.equals(SkillCraftPerkScreen.getSelectedTree())) {return false;}
        return super.isHovered();
    }

    @Override
    public void onClick(MouseButtonEvent click, boolean doubled) {
        SkillCraftPerkScreen.setSelectedPerk(this.perk);
    }

    @Override
    public int getX() {
        return super.getX() + Math.round((float)SkillCraftPerkScreen.getOffset_x());
    }

    @Override
    public int getY() {
        return super.getY() + Math.round((float)SkillCraftPerkScreen.getOffset_y());
    }
}
