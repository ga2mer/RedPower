package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerEndpoint;
import net.minecraft.tileentity.TileEntity;

class TileAccel$2 extends BluePowerEndpoint
{
    final TileAccel this$0;

    TileAccel$2(TileAccel var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }
}
