package com.eloraam.redpower.core;

import com.eloraam.redpower.core.TubeLib$WorldRoute;
import java.util.HashSet;
import java.util.PriorityQueue;
import net.minecraft.world.World;

class TubeLib$RouteFinder
{
    int startDir = 0;
    TubeLib$WorldRoute result;
    World worldObj;
    HashSet scanmap = new HashSet();
    PriorityQueue scanpos = new PriorityQueue();

    public TubeLib$RouteFinder(World var1)
    {
        this.worldObj = var1;
    }

    public void addPoint(WorldCoord var1, int var2, int var3, int var4)
    {
        ITubeConnectable var5 = (ITubeConnectable)CoreLib.getTileEntity(this.worldObj, var1, ITubeConnectable.class);

        if (var5 != null)
        {
            if (var5.canRouteItems())
            {
                if (!this.scanmap.contains(var1))
                {
                    this.scanmap.add(var1);
                    this.scanpos.add(new TubeLib$WorldRoute(var1, var2, var3 ^ 1, var4));
                }
            }
        }
    }

    public int find(WorldCoord var1, int var2)
    {
        for (int var3 = 0; var3 < 6; ++var3)
        {
            if ((var2 & 1 << var3) != 0)
            {
                WorldCoord var4 = var1.copy();
                var4.step(var3);
                this.addPoint(var4, var3, var3, var3 == this.startDir ? 0 : 1);
            }
        }

        while (this.scanpos.size() > 0)
        {
            TubeLib$WorldRoute var7 = (TubeLib$WorldRoute)this.scanpos.poll();

            if (var7.solved)
            {
                this.result = var7;
                return var7.start;
            }

            int var8 = TubeLib.getConnections(this.worldObj, var7.wc.x, var7.wc.y, var7.wc.z);

            for (int var5 = 0; var5 < 6; ++var5)
            {
                if (var5 != var7.side && (var8 & 1 << var5) != 0)
                {
                    WorldCoord var6 = var7.wc.copy();
                    var6.step(var5);
                    this.addPoint(var6, var7.start, var5, var7.weight + 2);
                }
            }
        }

        return -1;
    }

    public WorldCoord getResultPoint()
    {
        return this.result.wc;
    }
}
