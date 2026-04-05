package org.nattwenty.skillcraft;

import org.joml.Vector4i;

import java.util.ArrayList;
import net.minecraft.resources.Identifier;

public class Skilltree {
    private String treeName;
    private final Identifier iconPath;
    private final Identifier backgroundPath;
    private ArrayList<SkillCraftPerk> perks;
    private final Vector4i offset_bounds;

    public Skilltree(String treeName, Identifier iconPath, Identifier backgroundPath, Vector4i offset_bounds) {
        this.iconPath = iconPath;
        this.backgroundPath = backgroundPath;
        this.treeName = treeName;
        this.perks = new ArrayList<>();
        this.offset_bounds = offset_bounds;
    }

    public void clearPerks() {this.perks.clear();}
    public void addPerk(SkillCraftPerk perk) {this.perks.add(perk);}
    public String getTreeName() {return this.treeName;}
    public Identifier getIconPath() {return this.iconPath;}
    public Identifier getBackgroundPath() {return this.backgroundPath;}
    public Vector4i getOffset_bounds() {return this.offset_bounds;}

    public ArrayList<SkillCraftPerk> getPerks() {return this.perks;}
}
