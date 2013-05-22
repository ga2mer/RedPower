package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.FluidClass;
import com.eloraam.redpower.core.PipeLib;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileGrate$FluidCoord;
import com.eloraam.redpower.machine.TileGrate$SimpleComparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class TileGrate$GratePathfinder
{
    WorldCoord startPos;
    HashMap backlink;
    PriorityQueue workset;
    PriorityQueue allset;
    public int fluidID;
    public FluidClass fluidClass;

    final TileGrate this$0;

    public TileGrate$GratePathfinder(TileGrate var1, boolean var2)
    {
        this.this$0 = var1;
        this.backlink = new HashMap();
        this.allset = new PriorityQueue(1024, Collections.reverseOrder());

        if (var2)
        {
            this.workset = new PriorityQueue();
        }
        else
        {
            this.workset = new PriorityQueue(1024, new TileGrate$SimpleComparator());
        }
    }

    public void start(WorldCoord var1, int var2, int var3)
    {
        this.fluidID = var2;
        this.fluidClass = PipeLib.getLiquidClass(this.fluidID);
        this.startPos = var1;

        for (int var4 = 0; var4 < 6; ++var4)
        {
            if ((var3 & 1 << var4) != 0)
            {
                WorldCoord var5 = var1.coordStep(var4);
                this.backlink.put(var5, var1);
                this.workset.add(new TileGrate$FluidCoord(var5, 0));
            }
        }
    }

    public boolean startSuck(WorldCoord var1, int var2)
    {
        this.fluidID = 0;
        this.startPos = var1;

        for (int var3 = 0; var3 < 6; ++var3)
        {
            if ((var2 & 1 << var3) != 0)
            {
                WorldCoord var4 = var1.coordStep(var3);
                this.backlink.put(var4, var1);
                this.workset.add(new TileGrate$FluidCoord(var4, 0));
                int var5 = PipeLib.getLiquidId(this.this$0.worldObj, var4);

                if (var5 != 0)
                {
                    this.fluidID = var5;
                }
            }
        }

        if (this.fluidID == 0)
        {
            return false;
        }
        else
        {
            this.fluidClass = PipeLib.getLiquidClass(this.fluidID);
            return true;
        }
    }

    public boolean isConnected(WorldCoord var1)
    {
        if (var1.compareTo(this.startPos) == 0)
        {
            return true;
        }
        else
        {
            do
            {
                var1 = (WorldCoord)this.backlink.get(var1);

                if (var1 == null)
                {
                    return false;
                }

                if (var1.compareTo(this.startPos) == 0)
                {
                    return true;
                }
            }
            while (this.fluidClass.getFluidId(this.this$0.worldObj, var1) == this.fluidID);

            return false;
        }
    }

    public void stepAdd(TileGrate$FluidCoord var1)
    {
        for (int var2 = 0; var2 < 6; ++var2)
        {
            WorldCoord var3 = var1.wc.coordStep(var2);

            if (!this.backlink.containsKey(var3))
            {
                this.backlink.put(var3, var1.wc);
                this.workset.add(new TileGrate$FluidCoord(var3, var1.dist + 1));
            }
        }
    }

    public void stepMap(TileGrate$FluidCoord var1)
    {
        for (int var2 = 0; var2 < 6; ++var2)
        {
            WorldCoord var3 = var1.wc.coordStep(var2);

            if (this.fluidClass.getFluidId(this.this$0.worldObj, var3) == this.fluidID && !this.backlink.containsKey(var3))
            {
                this.backlink.put(var3, var1.wc);
                this.workset.add(new TileGrate$FluidCoord(var3, var1.dist + 1));
            }
        }
    }

    public int tryDumpFluid(int var1, int var2)
    {
        for (int var3 = 0; var3 < var2; ++var3)
        {
            TileGrate$FluidCoord var4 = (TileGrate$FluidCoord)this.workset.poll();

            if (var4 == null)
            {
                this.this$0.restartPath();
                return var1;
            }

            if (!this.isConnected(var4.wc))
            {
                this.this$0.restartPath();
                return var1;
            }

            if (this.this$0.worldObj.getBlockId(var4.wc.x, var4.wc.y, var4.wc.z) == 0)
            {
                if (this.fluidClass.setFluidLevel(this.this$0.worldObj, var4.wc, var1))
                {
                    this.stepAdd(var4);
                    return 0;
                }
            }
            else if (this.fluidClass.getFluidId(this.this$0.worldObj, var4.wc) == this.fluidID)
            {
                this.stepAdd(var4);
                int var5 = this.fluidClass.getFluidLevel(this.this$0.worldObj, var4.wc);

                if (var5 < 1000)
                {
                    int var6 = Math.min(var5 + var1, 1000);

                    if (this.fluidClass.setFluidLevel(this.this$0.worldObj, var4.wc, var6))
                    {
                        var1 -= var6 - var5;

                        if (var1 == 0)
                        {
                            return 0;
                        }
                    }
                }
            }
        }

        return var1;
    }

    public boolean tryMapFluid(int var1)
    {
        if (this.allset.size() > 32768)
        {
            return true;
        }
        else
        {
            for (int var2 = 0; var2 < var1; ++var2)
            {
                TileGrate$FluidCoord var3 = (TileGrate$FluidCoord)this.workset.poll();

                if (var3 == null)
                {
                    return true;
                }

                if (this.fluidClass.getFluidId(this.this$0.worldObj, var3.wc) == this.fluidID)
                {
                    this.stepMap(var3);
                    int var4 = this.fluidClass.getFluidLevel(this.this$0.worldObj, var3.wc);

                    if (var4 > 0)
                    {
                        this.allset.add(var3);
                    }
                }
            }

            return false;
        }
    }

    public int trySuckFluid(int var1)
    {
        int var2 = 0;

        while (!this.allset.isEmpty())
        {
            TileGrate$FluidCoord var3 = (TileGrate$FluidCoord)this.allset.peek();

            if (!this.isConnected(var3.wc))
            {
                this.this$0.restartPath();
                return var2;
            }

            if (this.fluidClass.getFluidId(this.this$0.worldObj, var3.wc) != this.fluidID)
            {
                this.allset.poll();
            }
            else
            {
                int var4 = this.fluidClass.getFluidLevel(this.this$0.worldObj, var3.wc);

                if (var4 == 0)
                {
                    this.allset.poll();
                }
                else
                {
                    if (var2 + var4 <= var1)
                    {
                        var2 += var4;
                        this.this$0.worldObj.setBlock(var3.wc.x, var3.wc.y, var3.wc.z, 0);
                        this.allset.poll();

                        if (var2 == var1)
                        {
                            return var1;
                        }
                    }

                    if (this.fluidClass.setFluidLevel(this.this$0.worldObj, var3.wc, var1 - var2))
                    {
                        return var1;
                    }
                }
            }
        }

        this.this$0.restartPath();
        return var2;
    }
}
