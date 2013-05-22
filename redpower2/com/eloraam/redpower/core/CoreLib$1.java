package com.eloraam.redpower.core;

import java.util.Comparator;
import net.minecraft.item.ItemStack;

final class CoreLib$1 implements Comparator
{
    public int compare(ItemStack var1, ItemStack var2)
    {
        return CoreLib.compareItemStack(var1, var2);
    }

    public int compare(Object var1, Object var2)
    {
        return this.compare((ItemStack)var1, (ItemStack)var2);
    }
}
