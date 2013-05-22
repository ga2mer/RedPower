package com.eloraam.redpower.base;

import com.eloraam.redpower.core.BlockCoverable;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import com.eloraam.redpower.core.IMicroPlacement;
import com.eloraam.redpower.core.RedPowerLib;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;

public class BlockMicro extends BlockCoverable
{
    public BlockMicro(int var1)
    {
        super(var1, CoreLib.materialRedpower);
        this.setHardness(0.1F);
        this.setCreativeTab(CreativeExtraTabs.tabWires);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return !RedPowerLib.isSearching();
    }

    public boolean canConnectRedstone(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        if (RedPowerLib.isSearching())
        {
            return false;
        }
        else
        {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            return var6 == 1 || var6 == 2;
        }
    }

    public void registerPlacement(int var1, IMicroPlacement var2)
    {
        ((ItemMicro)Item.itemsList[this.blockID]).registerPlacement(var1, var2);
    }
}
