package com.eloraam.redpower.compat;

import com.eloraam.redpower.core.BluePowerEndpoint;
import net.minecraft.tileentity.TileEntity;

class TileBlueEngine$1 extends BluePowerEndpoint
{
    final TileBlueEngine this$0;

    TileBlueEngine$1(TileBlueEngine var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }
}
