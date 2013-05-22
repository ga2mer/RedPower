package com.eloraam.redpower.core;

import com.eloraam.redpower.core.TubeLib$RouteFinder;
import com.eloraam.redpower.core.TubeLib$WorldRoute;
import net.minecraft.world.World;

class TubeLib$OutRouteFinder extends TubeLib$RouteFinder
{
    int state;
    TubeItem tubeItem;

    public TubeLib$OutRouteFinder(World var1, TubeItem var2, int var3)
    {
        super(var1);
        this.state = var3;
        this.tubeItem = var2;
    }

    public void addPoint(WorldCoord var1, int var2, int var3, int var4)
    {
        int var5 = (var3 ^ 1) & 255;

        if (this.state != 3 && this.tubeItem.priority == 0 && MachineLib.canAddToInventory(this.worldObj, this.tubeItem.item, var1, var5))
        {
            TubeLib$WorldRoute var8 = new TubeLib$WorldRoute(var1, var2, var3, var4);
            var8.solved = true;
            this.scanpos.add(var8);
        }
        else
        {
            ITubeConnectable var6 = (ITubeConnectable)CoreLib.getTileEntity(this.worldObj, var1, ITubeConnectable.class);

            if (var6 != null)
            {
                if (var6.tubeItemCanEnter(var5, this.state, this.tubeItem))
                {
                    TubeLib$WorldRoute var7 = new TubeLib$WorldRoute(var1, var2, var5, var4 + var6.tubeWeight(var5, this.state));
                    var7.solved = true;
                    this.scanpos.add(var7);
                }
                else if (var6.tubeItemCanEnter(var5, 0, this.tubeItem))
                {
                    if (var6.canRouteItems())
                    {
                        if (!this.scanmap.contains(var1))
                        {
                            this.scanmap.add(var1);
                            this.scanpos.add(new TubeLib$WorldRoute(var1, var2, var5, var4 + var6.tubeWeight(var5, this.state)));
                        }
                    }
                }
            }
        }
    }
}
