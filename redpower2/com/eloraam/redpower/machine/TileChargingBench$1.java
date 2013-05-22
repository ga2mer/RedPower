package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerEndpoint;
import net.minecraft.tileentity.TileEntity;

class TileChargingBench$1 extends BluePowerEndpoint
{
    final TileChargingBench this$0;

    TileChargingBench$1(TileChargingBench var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }
}
