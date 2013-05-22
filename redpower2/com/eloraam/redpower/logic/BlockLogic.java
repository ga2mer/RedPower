package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.BlockCoverable;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import com.eloraam.redpower.core.IRedPowerConnectable;
import com.eloraam.redpower.core.RedPowerLib;
import net.minecraft.world.IBlockAccess;

public class BlockLogic extends BlockCoverable
{
    public BlockLogic(int var1)
    {
        super(var1, CoreLib.materialRedpower);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setHardness(0.1F);
        this.setLightValue(0.625F);
        this.setCreativeTab(CreativeExtraTabs.tabWires);
    }

    public boolean canConnectRedstone(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        if (var5 < 0)
        {
            return false;
        }
        else
        {
            IRedPowerConnectable var6 = (IRedPowerConnectable)CoreLib.getTileEntity(var1, var2, var3, var4, IRedPowerConnectable.class);

            if (var6 == null)
            {
                return false;
            }
            else
            {
                int var7 = RedPowerLib.mapLocalToRot(var6.getConnectableMask(), 2);
                return (var7 & 1 << var5) > 0;
            }
        }
    }

    public int getLightValue(IBlockAccess var1, int var2, int var3, int var4)
    {
        TileLogic var5 = (TileLogic)CoreLib.getTileEntity(var1, var2, var3, var4, TileLogic.class);
        return var5 == null ? super.getLightValue(var1, var2, var3, var4) : var5.getLightValue();
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }
}
