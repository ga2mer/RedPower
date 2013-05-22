package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

final class CreativeExtraTabs$3 extends CreativeTabs
{
    CreativeExtraTabs$3(int var1, String var2)
    {
        super(var1, var2);
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(RedPowerBase.blockAppliance, 1, 3);
    }
}
