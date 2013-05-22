package com.eloraam.redpower;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

final class RedPowerLighting$1 extends CreativeTabs
{
    RedPowerLighting$1(int var1, String var2)
    {
        super(var1, var2);
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(RedPowerLighting.blockLampOn, 1, 0);
    }
}
