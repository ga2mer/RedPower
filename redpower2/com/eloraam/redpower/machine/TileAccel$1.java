package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.TubeFlow;
import com.eloraam.redpower.core.TubeFlow$TubeScheduleContext;
import com.eloraam.redpower.core.TubeItem;
import net.minecraft.tileentity.TileEntity;

class TileAccel$1 extends TubeFlow
{
    final TileAccel this$0;

    TileAccel$1(TileAccel var1)
    {
        this.this$0 = var1;
    }

    public TileEntity getParent()
    {
        return this.this$0;
    }

    public boolean schedule(TubeItem var1, TubeFlow$TubeScheduleContext var2)
    {
        var1.scheduled = true;
        var1.progress = 0;
        var1.side = (byte)(var1.side ^ 1);
        this.this$0.recache();
        var1.power = 0;

        if ((var1.side == this.this$0.Rotation && (this.this$0.conCache & 2) > 0 || var1.side == (this.this$0.Rotation ^ 1) && (this.this$0.conCache & 8) > 0) && this.this$0.cond.getVoltage() >= 60.0D)
        {
            this.this$0.cond.drawPower((double)(100 * var1.item.stackSize));
            var1.power = 255;
        }

        return true;
    }
}
