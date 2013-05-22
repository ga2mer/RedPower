package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerEndpoint;
import net.minecraft.tileentity.TileEntity;

class TileRetriever$1 extends BluePowerEndpoint
{
    final TileRetriever this$0;

    TileRetriever$1(TileRetriever var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }
}
