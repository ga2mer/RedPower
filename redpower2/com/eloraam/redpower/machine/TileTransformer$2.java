package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerEndpoint;
import net.minecraft.tileentity.TileEntity;

class TileTransformer$2 extends BluePowerEndpoint
{
    final TileTransformer this$0;

    TileTransformer$2(TileTransformer var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }

    public double getResistance()
    {
        return 1.0D;
    }

    public double getIndScale()
    {
        return 7.0E-4D;
    }

    public double getCondParallel()
    {
        return 0.005D;
    }

    public double getInvCap()
    {
        return 25.0D;
    }

    protected void computeVoltage()
    {
        this.Vcap = this.this$0.cond.getVoltage() * 100.0D;
        this.Itot = this.this$0.cond.Itot * 0.01D;
        this.It1 = 0.0D;
        this.Icap = 0.0D;
    }

    public void applyCurrent(double var1)
    {
        this.this$0.cond.applyCurrent(var1 * 100.0D);
    }
}
