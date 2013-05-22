package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerEndpoint;
import net.minecraft.tileentity.TileEntity;

class TileBlueFurnace$1 extends BluePowerEndpoint
{
    final TileBlueFurnace this$0;

    TileBlueFurnace$1(TileBlueFurnace var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }
}
