package com.eloraam.redpower.core;

import com.eloraam.redpower.core.TubeLib$RouteFinder;
import com.eloraam.redpower.core.TubeLib$WorldRoute;
import net.minecraft.world.World;

public class TubeLib$RequestRouteFinder extends TubeLib$RouteFinder
{
    TubeItem tubeItem;

    public TubeLib$RequestRouteFinder(World var1, TubeItem var2)
    {
        super(var1);
        this.tubeItem = var2;
    }

    public void addPoint(WorldCoord var1, int var2, int var3, int var4)
    {
        ITubeRequest var5 = (ITubeRequest)CoreLib.getTileEntity(this.worldObj, var1, ITubeRequest.class);

        if (var5 != null)
        {
            if (var5.requestTubeItem(this.tubeItem, false))
            {
                TubeLib$WorldRoute var8 = new TubeLib$WorldRoute(var1, 0, var3, var4);
                var8.solved = true;
                this.scanpos.add(var8);
            }
        }
        else
        {
            ITubeConnectable var6 = (ITubeConnectable)CoreLib.getTileEntity(this.worldObj, var1, ITubeConnectable.class);

            if (var6 != null)
            {
                int var7 = (var3 ^ 1) & 255;

                if (var6.tubeItemCanEnter(var7, 0, this.tubeItem))
                {
                    if (var6.canRouteItems())
                    {
                        if (!this.scanmap.contains(var1))
                        {
                            this.scanmap.add(var1);
                            this.scanpos.add(new TubeLib$WorldRoute(var1, var2, var7, var4 + var6.tubeWeight(var7, 0)));
                        }
                    }
                }
            }
        }
    }

    public WorldCoord getResultPoint()
    {
        return super.getResultPoint();
    }

    public int find(WorldCoord var1, int var2)
    {
        return super.find(var1, var2);
    }
}
