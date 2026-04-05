package org.nattwenty.skillcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import org.nattwenty.skillcraft.SkillCraftPerk;

public class SkillExperienceWidget extends AbstractWidget {
    public SkillExperienceWidget(int x, int y, int width, int height) {
        super(x, y, width, height, null);
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Font renderer = Minecraft.getInstance().font;
        Component component = Component.translatable("skillcraft.ui.skill-perks-message", SkillCraftPerkScreen.getPerkPoints());

        context.drawString(renderer, component, getX(), getY(), 0xFFF0F0F0, true);

        SkillCraftPerk perk = SkillCraftPerkScreen.getSelectedPerk();
        if (perk != null) {
            context.drawString(renderer, "[-%s]".formatted(perk.getPointsPerLevel()), getX() + 6 + renderer.width(component), getY(), 0xFFF04A4A, true);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        return;
    }

    @Override
    protected boolean isValidClickButton(MouseButtonInfo mouseButtonInfo) {
        return false;
    }
}
