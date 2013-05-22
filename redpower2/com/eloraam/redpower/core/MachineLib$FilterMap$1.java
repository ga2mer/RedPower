package com.eloraam.redpower.core;

import com.eloraam.redpower.core.MachineLib$FilterMap;
import java.util.Comparator;
import net.minecraft.item.ItemStack;

class MachineLib$FilterMap$1 implements Comparator
{
    final MachineLib$FilterMap this$0;

    MachineLib$FilterMap$1(MachineLib$FilterMap var1)
    {
        this.this$0 = var1;
    }

    public int compare(ItemStack var1, ItemStack var2)
    {
        return MachineLib.compareItem(var1, var2);
    }

    public int compare(Object var1, Object var2)
    {
        return this.compare((ItemStack)var1, (ItemStack)var2);
    }
}
