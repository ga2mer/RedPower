package com.eloraam.redpower.control;

import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import com.eloraam.redpower.wiring.MicroPlacementWire;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MicroPlacementRibbon extends MicroPlacementWire
{
    public String getMicroName(int var1, int var2)
    {
        return var1 != 12 && var2 != 0 ? null : "tile.ribbon";
    }

    public void addCreativeItems(int var1, CreativeTabs var2, List var3)
    {
        if (var2 == CreativeExtraTabs.tabWires)
        {
            if (var1 == 12)
            {
                var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 3072));
            }
        }
    }
}
