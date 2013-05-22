package com.eloraam.redpower.core;

import com.eloraam.redpower.core.MachineLib$FilterMap;
import com.eloraam.redpower.core.TubeLib$RouteFinder;
import com.eloraam.redpower.core.TubeLib$WorldRoute;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TubeLib$InRouteFinder extends TubeLib$RouteFinder
{
    MachineLib$FilterMap filterMap;
    int subFilt = -1;

    public TubeLib$InRouteFinder(World var1, MachineLib$FilterMap var2)
    {
        super(var1);
        this.filterMap = var2;
    }

    public void addPoint(WorldCoord var1, int var2, int var3, int var4)
    {
        IInventory var5 = MachineLib.getInventory(this.worldObj, var1);

        if (var5 == null)
        {
            super.addPoint(var1, var2, var3, var4);
        }
        else
        {
            int var6 = (var3 ^ 1) & 63;
            int var7 = 0;
            int var8 = var5.getSizeInventory();

            if (var5 instanceof ISidedInventory)
            {
                ISidedInventory var9 = (ISidedInventory)var5;
                var7 = var9.getStartInventorySide(ForgeDirection.getOrientation(var6));
                var8 = var9.getSizeInventorySide(ForgeDirection.getOrientation(var6));
            }

            if (this.filterMap.size() == 0)
            {
                if (!MachineLib.emptyInventory(var5, var7, var8))
                {
                    TubeLib$WorldRoute var12 = new TubeLib$WorldRoute(var1, 0, var6, var4);
                    var12.solved = true;
                    this.scanpos.add(var12);
                }
                else
                {
                    super.addPoint(var1, var2, var3, var4);
                }
            }
            else
            {
                int var11 = -1;

                if (this.subFilt < 0)
                {
                    var11 = MachineLib.matchAnyStack(this.filterMap, var5, var7, var8);
                }
                else if (MachineLib.matchOneStack(this.filterMap, var5, var7, var8, this.subFilt))
                {
                    var11 = this.subFilt;
                }

                if (var11 < 0)
                {
                    super.addPoint(var1, var2, var3, var4);
                }
                else
                {
                    TubeLib$WorldRoute var10 = new TubeLib$WorldRoute(var1, var11, var6, var4);
                    var10.solved = true;
                    this.scanpos.add(var10);
                }
            }
        }
    }

    public void setSubFilt(int var1)
    {
        this.subFilt = var1;
    }

    public int getResultSide()
    {
        return this.result.side;
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
