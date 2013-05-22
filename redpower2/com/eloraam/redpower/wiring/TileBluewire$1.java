package com.eloraam.redpower.wiring;

import com.eloraam.redpower.core.BluePowerConductor;
import net.minecraft.tileentity.TileEntity;

class TileBluewire$1 extends BluePowerConductor
{
    final TileBluewire this$0;

    TileBluewire$1(TileBluewire var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }

    public double getInvCap()
    {
        switch (this.this$0.Metadata)
        {
            case 0:
                return 8.0D;

            default:
                return 800.0D;
        }
    }

    public double getResistance()
    {
        switch (this.this$0.Metadata)
        {
            case 0:
                return 0.01D;

            default:
                return 1.0D;
        }
    }

    public double getIndScale()
    {
        switch (this.this$0.Metadata)
        {
            case 0:
                return 0.07D;

            default:
                return 7.0E-4D;
        }
    }

    public double getCondParallel()
    {
        switch (this.this$0.Metadata)
        {
            case 0:
                return 0.5D;

            default:
                return 0.005D;
        }
    }
}
