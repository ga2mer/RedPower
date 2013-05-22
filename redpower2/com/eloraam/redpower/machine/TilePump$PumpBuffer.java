package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.FluidBuffer;
import net.minecraft.tileentity.TileEntity;

class TilePump$PumpBuffer extends FluidBuffer
{
    final TilePump this$0;

    public TilePump$PumpBuffer(TilePump var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }

    public void onChange()
    {
        this.this$0.dirtyBlock();
    }

    public int getMaxLevel()
    {
        return 1000;
    }
}
