package org.nattwenty.skillcraft.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonInfo;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.nattwenty.skillcraft.Skilltree;

import java.util.ArrayList;

public class PerkLineSegment extends AbstractWidget {
    private Skilltree tree;
    private ArrayList<Vector4i> line_segments;
    private Vector2i screen_center;

    public PerkLineSegment(int x, int y, Skilltree skilltree, ArrayList<Vector4i> line_segments) {
        super(-100, 100, -100, 100, null);
        this.screen_center = new Vector2i(x, y);
        this.tree = skilltree;
        this.line_segments = line_segments;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        if (!this.tree.equals(SkillCraftPerkScreen.getSelectedTree())) {return;}
        int offset_x = (int) SkillCraftPerkScreen.getOffset_x() + screen_center.x;
        int offset_y = (int) SkillCraftPerkScreen.getOffset_y() + screen_center.y;
        for (Vector4i vec : this.line_segments) {
            int x = vec.x - vec.z;
            int y = vec.y - vec.w;
            if (x > y) {
                guiGraphics.hLine(offset_x - vec.x, offset_x - vec.z, offset_y - vec.y, 0xA0F0F0F0);
                guiGraphics.vLine(offset_x - vec.z, offset_y - vec.y, offset_y - vec.w, 0xA0F0F0F0);
            }
            else {
                guiGraphics.vLine(offset_x - vec.x, offset_y - vec.y, offset_y - vec.w, 0xA0F0F0F0);
                guiGraphics.hLine(offset_x - vec.x, offset_x - vec.z, offset_y - vec.w, 0xA0F0F0F0);
            }
        }

        //guiGraphics.vLine(offset_x, offset_y, offset_y+36, 0xFFF00000);
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
