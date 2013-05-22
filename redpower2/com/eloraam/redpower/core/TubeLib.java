package com.eloraam.redpower.core;

import com.eloraam.redpower.core.TubeLib$OutRouteFinder;
import java.util.Arrays;
import java.util.HashSet;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TubeLib
{
    private static HashSet tubeClassMapping = new HashSet();

    public static void addCompatibleMapping(int var0, int var1)
    {
        tubeClassMapping.add(Arrays.asList(new Integer[] {Integer.valueOf(var0), Integer.valueOf(var1)}));
        tubeClassMapping.add(Arrays.asList(new Integer[] {Integer.valueOf(var1), Integer.valueOf(var0)}));
    }

    public static boolean isCompatible(int var0, int var1)
    {
        return var0 == var1 || tubeClassMapping.contains(Arrays.asList(new Integer[] {Integer.valueOf(var0), Integer.valueOf(var1)}));
    }

    private static boolean isConSide(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5)
    {
        TileEntity var6 = var0.getBlockTileEntity(var1, var2, var3);

        if (isCompatible(var4, 0) && var6 instanceof IInventory)
        {
            if (!(var6 instanceof ISidedInventory))
            {
                return true;
            }

            ISidedInventory var7 = (ISidedInventory)var6;

            if (var7.getSizeInventorySide(ForgeDirection.getOrientation(var5)) > 0)
            {
                return true;
            }
        }

        if (var6 instanceof ITubeConnectable)
        {
            ITubeConnectable var9 = (ITubeConnectable)var6;

            if (!isCompatible(var4, var9.getTubeConClass()))
            {
                return false;
            }
            else
            {
                int var8 = var9.getTubeConnectableSides();
                return (var8 & 1 << var5) > 0;
            }
        }
        else
        {
            return false;
        }
    }

    public static int getConnections(IBlockAccess var0, int var1, int var2, int var3)
    {
        ITubeConnectable var4 = (ITubeConnectable)CoreLib.getTileEntity(var0, var1, var2, var3, ITubeConnectable.class);

        if (var4 == null)
        {
            return 0;
        }
        else
        {
            int var5 = 0;
            int var6 = var4.getTubeConClass();
            int var7 = var4.getTubeConnectableSides();

            if ((var7 & 1) > 0 && isConSide(var0, var1, var2 - 1, var3, var6, 1))
            {
                var5 |= 1;
            }

            if ((var7 & 2) > 0 && isConSide(var0, var1, var2 + 1, var3, var6, 0))
            {
                var5 |= 2;
            }

            if ((var7 & 4) > 0 && isConSide(var0, var1, var2, var3 - 1, var6, 3))
            {
                var5 |= 4;
            }

            if ((var7 & 8) > 0 && isConSide(var0, var1, var2, var3 + 1, var6, 2))
            {
                var5 |= 8;
            }

            if ((var7 & 16) > 0 && isConSide(var0, var1 - 1, var2, var3, var6, 5))
            {
                var5 |= 16;
            }

            if ((var7 & 32) > 0 && isConSide(var0, var1 + 1, var2, var3, var6, 4))
            {
                var5 |= 32;
            }

            return var5;
        }
    }

    public static int findRoute(World var0, WorldCoord var1, TubeItem var2, int var3, int var4)
    {
        TubeLib$OutRouteFinder var5 = new TubeLib$OutRouteFinder(var0, var2, var4);
        return var5.find(var1, var3);
    }

    public static int findRoute(World var0, WorldCoord var1, TubeItem var2, int var3, int var4, int var5)
    {
        TubeLib$OutRouteFinder var6 = new TubeLib$OutRouteFinder(var0, var2, var4);
        var6.startDir = var5;
        return var6.find(var1, var3);
    }

    public static boolean addToTubeRoute(World var0, ItemStack var1, WorldCoord var2, WorldCoord var3, int var4)
    {
        return addToTubeRoute(var0, new TubeItem(0, var1), var2, var3, var4);
    }

    public static boolean addToTubeRoute(World var0, TubeItem var1, WorldCoord var2, WorldCoord var3, int var4)
    {
        ITubeConnectable var5 = (ITubeConnectable)CoreLib.getTileEntity(var0, var3, ITubeConnectable.class);

        if (var5 == null)
        {
            return false;
        }
        else
        {
            var1.mode = 1;
            int var6 = findRoute(var0, var2, var1, 1 << (var4 ^ 1), 1);
            return var6 < 0 ? false : var5.tubeItemEnter(var4, 0, var1);
        }
    }

    static
    {
        addCompatibleMapping(0, 17);
        addCompatibleMapping(17, 18);

        for (int var0 = 0; var0 < 16; ++var0)
        {
            addCompatibleMapping(0, 1 + var0);
            addCompatibleMapping(17, 1 + var0);
            addCompatibleMapping(17, 19 + var0);
            addCompatibleMapping(18, 19 + var0);
        }
    }
}
