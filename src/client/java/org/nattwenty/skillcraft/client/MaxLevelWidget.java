package org.nattwenty.skillcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;

public class MaxLevelWidget extends AbstractWidget {

    public MaxLevelWidget(int i, int j, int k, int l, Component component) {
        super(i, j, k, l, component);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        Font renderer = Minecraft.getInstance().font;
        guiGraphics.drawString(renderer, this.message, this.getX(), this.getY(), 0xFFF0F0F0);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    @Override
    protected boolean isValidClickButton(MouseButtonInfo mouseButtonInfo) {
        return false;
    }
}
