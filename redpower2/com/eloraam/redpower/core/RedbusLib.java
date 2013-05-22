package com.eloraam.redpower.core;

import com.eloraam.redpower.core.RedbusLib$RedbusPathfinder;
import net.minecraft.world.IBlockAccess;

public class RedbusLib
{
    public static IRedbusConnectable getAddr(IBlockAccess var0, WorldCoord var1, int var2)
    {
        RedbusLib$RedbusPathfinder var3 = new RedbusLib$RedbusPathfinder(var0, var2);
        var3.addSearchBlocks(var1, 16777215, 0);

        while (var3.iterate())
        {
            ;
        }

        return var3.result;
    }
}
