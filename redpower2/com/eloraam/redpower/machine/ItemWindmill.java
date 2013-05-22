package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWindmill extends Item
{
    public int windmillType;

    public ItemWindmill(int var1, int var2)
    {
        super(var1);
        //this.setIconIndex(177);
        this.setMaxStackSize(1);
        this.setMaxDamage(1000);
        this.setUnlocalizedName("windTurbineWood");
        this.windmillType = var2;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(this, 1, 0));
    }

    public boolean canFaceDirection(int var1)
    {
        switch (this.windmillType)
        {
            case 1:
                return var1 == 0;

            case 2:
                return var1 > 1;

            default:
                return false;
        }
    }

    public ItemStack getBrokenItem()
    {
        switch (this.windmillType)
        {
            case 1:
                return new ItemStack(RedPowerBase.blockMicro, 3, 5905);

            case 2:
                return new ItemStack(RedPowerBase.blockMicro, 1, 5905);

            default:
                return null;
        }
    }

    public String getTextureFile()
    {
        return "/eloraam/base/items1.png";
    }
}
