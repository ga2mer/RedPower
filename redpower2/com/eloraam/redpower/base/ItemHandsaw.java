package com.eloraam.redpower.base;

import com.eloraam.redpower.core.ItemPartialCraft;
import net.minecraft.creativetab.CreativeTabs;

public class ItemHandsaw extends ItemPartialCraft
{
    private int sharp;

    public ItemHandsaw(int var1, int var2)
    {
        super(var1);
        this.sharp = var2;
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    public int getSharpness()
    {
        return this.sharp;
    }

    public String getTextureFile()
    {
        return "/eloraam/base/items1.png";
    }
}
