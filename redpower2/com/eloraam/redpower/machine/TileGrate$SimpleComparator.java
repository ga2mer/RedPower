package com.eloraam.redpower.machine;

import com.eloraam.redpower.machine.TileGrate$FluidCoord;
import java.util.Comparator;

public class TileGrate$SimpleComparator implements Comparator
{
    int dir;

    public int compare(Object var1, Object var2)
    {
        TileGrate$FluidCoord var3 = (TileGrate$FluidCoord)var1;
        TileGrate$FluidCoord var4 = (TileGrate$FluidCoord)var2;
        return var3.dist - var4.dist;
    }
}
