package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerConductor;
import net.minecraft.tileentity.TileEntity;

class TileWindTurbine$1 extends BluePowerConductor
{
    final TileWindTurbine this$0;

    TileWindTurbine$1(TileWindTurbine var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }

    public double getInvCap()
    {
        return 0.25D;
    }
}
