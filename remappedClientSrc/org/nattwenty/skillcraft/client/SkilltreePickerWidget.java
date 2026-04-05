package org.nattwenty.skillcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.joml.Vector4i;

public class SkilltreePickerWidget extends AbstractWidget {
    private final Identifier icon_path;
    private final Identifier bg_path;
    private final MutableComponent name;
    private final int treeVal;
    private final Vector4i bounds;


    public SkilltreePickerWidget(int x, int y, int width, int height, Identifier icon_path, Identifier bg_path, MutableComponent name, int treeVal, Vector4i bounds) {
        super(x, y, width, height, null);
        this.icon_path = icon_path;
        this.bg_path = bg_path;
        this.name = name;
        this.treeVal = treeVal;
        this.bounds = bounds;
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Font renderer = Minecraft.getInstance().font;
        int center_x = getX() + Math.round(width/2f);
        int size_offset = Math.round((width - 16)/2f);
        int y_offset = 0;

        //If any level of the perk is unlocked, use the clicked icon.
        float v = 0.0f;
        if (SkillCraftPerkScreen.getSelected() == treeVal) {
            v = 24.0f;
            y_offset = height/16;
        }

        context.blit(
                RenderPipelines.GUI_TEXTURED,
                bg_path,
                getX(),
                getY() - height/16 + y_offset,
                0.0f,
                v,
                width,
                height,
                width,
                height,
                width,
                height*2
        );
        context.blit(
                RenderPipelines.GUI_TEXTURED,
                icon_path,
                getX()+size_offset,
                getY()+size_offset - height/8 + + y_offset,
                0.0f,
                0.0f,
                16,
                16,
                16,
                16
        );

        if (isHovered()) {
            context.drawString(renderer, name, center_x - renderer.width(name)/2, getY() + height + 1, 0xF0F0F0FF, true);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        return;
    }

    @Override
    public void onClick(MouseButtonEvent click, boolean doubled) {
        SkillCraftPerkScreen.setSelected(this.treeVal);
        SkillCraftPerkScreen.setOffset_x(0d);
        SkillCraftPerkScreen.setOffset_y(0d);
        SkillCraftPerkScreen.setOffset_bounds(this.bounds);
    }
}
