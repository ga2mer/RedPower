package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerEndpoint;
import net.minecraft.tileentity.TileEntity;

class TileSorter$1 extends BluePowerEndpoint
{
    final TileSorter this$0;

    TileSorter$1(TileSorter var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }
}
