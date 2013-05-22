package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.WorldCoord;

public class TileGrate$FluidCoord implements Comparable
{
    public WorldCoord wc;
    public int dist;

    public TileGrate$FluidCoord(WorldCoord var1, int var2)
    {
        this.wc = var1;
        this.dist = var2;
    }

    public int compareTo(Object var1)
    {
        TileGrate$FluidCoord var2 = (TileGrate$FluidCoord)var1;
        return this.wc.y == var2.wc.y ? this.dist - var2.dist : this.wc.y - var2.wc.y;
    }
}
