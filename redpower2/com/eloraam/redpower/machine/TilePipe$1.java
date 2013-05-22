package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.FluidBuffer;
import net.minecraft.tileentity.TileEntity;

class TilePipe$1 extends FluidBuffer
{
    final TilePipe this$0;

    TilePipe$1(TilePipe var1)
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
}
