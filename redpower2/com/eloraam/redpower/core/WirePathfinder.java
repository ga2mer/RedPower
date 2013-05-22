package com.eloraam.redpower.core;

import java.util.HashSet;
import java.util.LinkedList;

public abstract class WirePathfinder
{
    HashSet scanmap;
    LinkedList scanpos;

    public void init()
    {
        this.scanmap = new HashSet();
        this.scanpos = new LinkedList();
    }

    public void addSearchBlock(WorldCoord var1)
    {
        if (!this.scanmap.contains(var1))
        {
            this.scanmap.add(var1);
            this.scanpos.addLast(var1);
        }
    }

    private void addIndBl(WorldCoord var1, int var2, int var3)
    {
        var1 = var1.coordStep(var2);
        int var4;

        switch (var2)
        {
            case 0:
                var4 = var3 + 2;
                break;

            case 1:
                var4 = var3 + 2;
                break;

            case 2:
                var4 = var3 + (var3 & 2);
                break;

            case 3:
                var4 = var3 + (var3 & 2);
                break;

            case 4:
                var4 = var3;
                break;

            default:
                var4 = var3;
        }

        var1.step(var4);
        this.addSearchBlock(var1);
    }

    public void addSearchBlocks(WorldCoord var1, int var2, int var3)
    {
        int var4;

        for (var4 = 0; var4 < 6; ++var4)
        {
            if ((var2 & RedPowerLib.getConDirMask(var4)) > 0)
            {
                this.addSearchBlock(var1.coordStep(var4));
            }
        }

        for (var4 = 0; var4 < 6; ++var4)
        {
            for (int var5 = 0; var5 < 4; ++var5)
            {
                if ((var3 & 1 << var4 * 4 + var5) > 0)
                {
                    this.addIndBl(var1, var4, var5);
                }
            }
        }
    }

    public boolean step(WorldCoord var1)
    {
        return false;
    }

    public boolean iterate()
    {
        if (this.scanpos.size() == 0)
        {
            return false;
        }
        else
        {
            WorldCoord var1 = (WorldCoord)this.scanpos.removeFirst();
            return this.step(var1);
        }
    }
}
