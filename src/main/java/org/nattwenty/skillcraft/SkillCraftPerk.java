package org.nattwenty.skillcraft;

import org.joml.Vector2i;

import java.util.ArrayList;
import net.minecraft.resources.Identifier;

public class SkillCraftPerk {
    private final String skillName;
    private final int skillMax;
    private final String skillDescription;
    private final Identifier iconPath;
    private final Identifier backgroundPath;
    private final int pointsPerLevel;

    private final Vector2i skillPosition;
    private ArrayList<SkillCraftPerk> parentPerks;
    private ArrayList<SkillCraftPerk> childPerks;

    public SkillCraftPerk(String skillName, int skillMax, String skillDescription, Identifier iconPath, Identifier backgroundPath, Vector2i skillPosition, int pointsPerLevel) {
        this.skillName = skillName;
        this.skillMax = skillMax;
        this.skillDescription = skillDescription;
        this.iconPath = iconPath;
        this.backgroundPath = backgroundPath;
        this.skillPosition = skillPosition;
        this.parentPerks = new ArrayList<>();
        this.childPerks = new ArrayList<>();
        this.pointsPerLevel = pointsPerLevel;
    }

    public String getSkillName() {return this.skillName;}
    public int getSkillMax() {return this.skillMax;}
    public String getSkillDescription() {return this.skillDescription;}
    public Identifier getIconPath() {return this.iconPath;}
    public Identifier getBackgroundPath() {return this.backgroundPath;}
    public Vector2i getSkillPosition() {return this.skillPosition;}
    public int getPointsPerLevel() {return this.pointsPerLevel;}

    public ArrayList<SkillCraftPerk> getParentPerks() {return this.parentPerks;}
    public ArrayList<SkillCraftPerk> getChildPerks() {return this.childPerks;}

    public void addParentPerk(SkillCraftPerk perk) {this.parentPerks.add(perk);}

    public void addChildPerk(SkillCraftPerk perk) {
        perk.addParentPerk(this);
        this.childPerks.add(perk);
    }
}
