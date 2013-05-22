package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerEndpoint;
import net.minecraft.tileentity.TileEntity;

class TileTransformer$1 extends BluePowerEndpoint
{
    final TileTransformer this$0;

    TileTransformer$1(TileTransformer var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }
}
