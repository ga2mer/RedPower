package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.TubeFlow;
import com.eloraam.redpower.core.TubeFlow$TubeScheduleContext;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.TubeLib;
import net.minecraft.tileentity.TileEntity;

class TileTube$1 extends TubeFlow
{
    final TileTube this$0;

    TileTube$1(TileTube var1)
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
        int var3 = var2.cons & ~(1 << var1.side);

        if (var3 == 0)
        {
            return true;
        }
        else if (Integer.bitCount(var3) == 1)
        {
            var1.side = (byte)Integer.numberOfTrailingZeros(var3);
            return true;
        }
        else if (CoreLib.isClient(this.this$0.worldObj))
        {
            return false;
        }
        else
        {
            if (var1.mode != 3)
            {
                var1.mode = 1;
            }

            var1.side = (byte)TubeLib.findRoute(var2.world, var2.wc, var1, var3, var1.mode, this.this$0.lastDir);
            int var4;

            if (var1.side >= 0)
            {
                var4 = var3 & ~((2 << this.this$0.lastDir) - 1);

                if (var4 == 0)
                {
                    var4 = var3;
                }

                if (var4 == 0)
                {
                    this.this$0.lastDir = 0;
                }
                else
                {
                    this.this$0.lastDir = (byte)Integer.numberOfTrailingZeros(var4);
                }
            }
            else
            {
                if (var1.mode == 1 && var1.priority > 0)
                {
                    var1.priority = 0;
                    var1.side = (byte)TubeLib.findRoute(var2.world, var2.wc, var1, var2.cons, 1);

                    if (var1.side >= 0)
                    {
                        return true;
                    }
                }

                var1.side = (byte)TubeLib.findRoute(var2.world, var2.wc, var1, var2.cons, 2);

                if (var1.side >= 0)
                {
                    var1.mode = 2;
                    return true;
                }

                if (var1.mode == 3)
                {
                    var1.side = (byte)TubeLib.findRoute(var2.world, var2.wc, var1, var2.cons, 1);
                    var1.mode = 1;
                }

                if (var1.side < 0)
                {
                    var1.side = this.this$0.lastDir;
                    var4 = var3 & ~((2 << this.this$0.lastDir) - 1);

                    if (var4 == 0)
                    {
                        var4 = var3;
                    }

                    if (var4 == 0)
                    {
                        this.this$0.lastDir = 0;
                    }
                    else
                    {
                        this.this$0.lastDir = (byte)Integer.numberOfTrailingZeros(var4);
                    }
                }
            }

            return true;
        }
    }

    public boolean handleItem(TubeItem var1, TubeFlow$TubeScheduleContext var2)
    {
        return MachineLib.addToInventory(this.this$0.worldObj, var1.item, var2.dest, (var1.side ^ 1) & 63);
    }
}
