package com.eloraam.redpower.core;

import com.eloraam.redpower.core.MachineLib$FilterMap$1;
import java.util.ArrayList;
import java.util.TreeMap;
import net.minecraft.item.ItemStack;

public class MachineLib$FilterMap
{
    protected TreeMap map;
    protected ItemStack[] filter;

    public MachineLib$FilterMap(ItemStack[] var1)
    {
        this.filter = var1;
        this.map = new TreeMap(new MachineLib$FilterMap$1(this));

        for (int var2 = 0; var2 < var1.length; ++var2)
        {
            if (var1[var2] != null && var1[var2].stackSize != 0)
            {
                ArrayList var3 = (ArrayList)this.map.get(var1[var2]);

                if (var3 == null)
                {
                    var3 = new ArrayList();
                    this.map.put(var1[var2], var3);
                }

                var3.add(Integer.valueOf(var2));
            }
        }
    }

    public int size()
    {
        return this.map.size();
    }

    public boolean containsKey(ItemStack var1)
    {
        return this.map.containsKey(var1);
    }

    public int firstMatch(ItemStack var1)
    {
        ArrayList var2 = (ArrayList)this.map.get(var1);
        return var2 == null ? -1 : ((Integer)var2.get(0)).intValue();
    }
}
