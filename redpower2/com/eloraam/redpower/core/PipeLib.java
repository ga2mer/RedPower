package com.eloraam.redpower.core;

import com.eloraam.redpower.core.PipeLib$1;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class PipeLib
{
    private static HashMap fluidByItem = new HashMap();
    private static HashMap fluidByBlock = new HashMap();
    private static HashMap fluidByID = new HashMap();

    private static boolean isConSide(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        TileEntity var5 = var0.getBlockTileEntity(var1, var2, var3);

        if (var5 instanceof IPipeConnectable)
        {
            IPipeConnectable var8 = (IPipeConnectable)var5;
            int var9 = var8.getPipeConnectableSides();
            return (var9 & 1 << var4) > 0;
        }
        else if (var5 instanceof ITankContainer)
        {
            ITankContainer var6 = (ITankContainer)var5;
            ILiquidTank var7 = var6.getTank(ForgeDirection.getOrientation(var4), (LiquidStack)null);
            return var7 != null;
        }
        else
        {
            return false;
        }
    }

    public static int getConnections(IBlockAccess var0, int var1, int var2, int var3)
    {
        IPipeConnectable var4 = (IPipeConnectable)CoreLib.getTileEntity(var0, var1, var2, var3, IPipeConnectable.class);

        if (var4 == null)
        {
            return 0;
        }
        else
        {
            int var5 = 0;
            int var6 = var4.getPipeConnectableSides();

            if ((var6 & 1) > 0 && isConSide(var0, var1, var2 - 1, var3, 1))
            {
                var5 |= 1;
            }

            if ((var6 & 2) > 0 && isConSide(var0, var1, var2 + 1, var3, 0))
            {
                var5 |= 2;
            }

            if ((var6 & 4) > 0 && isConSide(var0, var1, var2, var3 - 1, 3))
            {
                var5 |= 4;
            }

            if ((var6 & 8) > 0 && isConSide(var0, var1, var2, var3 + 1, 2))
            {
                var5 |= 8;
            }

            if ((var6 & 16) > 0 && isConSide(var0, var1 - 1, var2, var3, 5))
            {
                var5 |= 16;
            }

            if ((var6 & 32) > 0 && isConSide(var0, var1 + 1, var2, var3, 4))
            {
                var5 |= 32;
            }

            return var5;
        }
    }

    public static int getFlanges(IBlockAccess var0, WorldCoord var1, int var2)
    {
        int var3 = 0;

        for (int var4 = 0; var4 < 6; ++var4)
        {
            if ((var2 & 1 << var4) != 0)
            {
                WorldCoord var5 = var1.copy();
                var5.step(var4);
                TileEntity var6 = var0.getBlockTileEntity(var5.x, var5.y, var5.z);

                if (var6 != null)
                {
                    if (var6 instanceof IPipeConnectable)
                    {
                        IPipeConnectable var7 = (IPipeConnectable)var6;

                        if ((var7.getPipeFlangeSides() & 1 << (var4 ^ 1)) > 0)
                        {
                            var3 |= 1 << var4;
                        }
                    }

                    if (var6 instanceof ITankContainer)
                    {
                        ITankContainer var9 = (ITankContainer)var6;
                        ILiquidTank var8 = var9.getTank(ForgeDirection.getOrientation(var4 ^ 1), (LiquidStack)null);

                        if (var8 != null)
                        {
                            var3 |= 1 << var4;
                        }
                    }
                }
            }
        }

        return var3;
    }

    public static Integer getPressure(World var0, WorldCoord var1, int var2)
    {
        TileEntity var3 = var0.getBlockTileEntity(var1.x, var1.y, var1.z);

        if (var3 == null)
        {
            return null;
        }
        else if (var3 instanceof IPipeConnectable)
        {
            IPipeConnectable var7 = (IPipeConnectable)var3;
            return Integer.valueOf(var7.getPipePressure(var2));
        }
        else if (var3 instanceof ITankContainer)
        {
            ITankContainer var4 = (ITankContainer)var3;
            ILiquidTank var5 = var4.getTank(ForgeDirection.getOrientation(var2), (LiquidStack)null);

            if (var5 == null)
            {
                return null;
            }
            else
            {
                int var6 = var5.getTankPressure();
                return var6 > 0 ? Integer.valueOf(100) : (var6 < 0 ? Integer.valueOf(-100) : Integer.valueOf(0));
            }
        }
        else
        {
            return null;
        }
    }

    public static void registerVanillaFluid(int var0, int var1)
    {
        Block var2 = Block.blocksList[var0];

        if (var2 != null)
        {
            FluidClassVanilla var3 = new FluidClassVanilla(var0, var0, var1, null, 0);
            fluidByItem.put(Arrays.asList(new Integer[] {Integer.valueOf(var0), Integer.valueOf(0)}), var3);
            fluidByBlock.put(Integer.valueOf(var0), var3);
            fluidByBlock.put(Integer.valueOf(var1), var3);
            fluidByID.put(Integer.valueOf(var0), var3);
        }
    }

    public static void registerForgeFluid(String var0, LiquidStack var1)
    {
        System.out.printf("Fluid registration: %s\n", new Object[] {var0});
        Item var2 = Item.itemsList[var1.itemID];

        if (var2 != null)
        {
            int var3 = var1.itemID + (var1.itemMeta << 16);
            FluidClassItem var4 = new FluidClassItem(var3, var1.itemID, var1.itemMeta, null, 0);
            fluidByID.put(Integer.valueOf(var3), var4);
            fluidByItem.put(Arrays.asList(new Integer[] {Integer.valueOf(var1.itemID), Integer.valueOf(var1.itemMeta)}), var4);
        }
    }

    public static void registerFluids()
    {
        PipeLib$1 var0 = new PipeLib$1();
        Iterator var1 = LiquidDictionary.getLiquids().entrySet().iterator();

        while (var1.hasNext())
        {
            Entry var2 = (Entry)var1.next();
            registerForgeFluid((String)var2.getKey(), (LiquidStack)var2.getValue());
        }
    }

    public static int getLiquidId(World var0, WorldCoord var1)
    {
        int var2 = var0.getBlockId(var1.x, var1.y, var1.z);
        FluidClass var3 = (FluidClass)fluidByBlock.get(Integer.valueOf(var2));
        return var3 == null ? 0 : var3.getFluidId(var0, var1);
    }

    public static FluidClass getLiquidClass(int var0)
    {
        return (FluidClass)fluidByID.get(Integer.valueOf(var0));
    }

    public static FluidClass getLiquidClass(LiquidStack var0)
    {
        return (FluidClass)fluidByItem.get(Arrays.asList(new Integer[] {Integer.valueOf(var0.itemID), Integer.valueOf(var0.itemMeta)}));
    }

    public static void movePipeLiquid(World var0, IPipeConnectable var1, WorldCoord var2, int var3)
    {
        for (int var4 = 0; var4 < 6; ++var4)
        {
            if ((var3 & 1 << var4) != 0)
            {
                WorldCoord var5 = var2.coordStep(var4);
                TileEntity var6 = var0.getBlockTileEntity(var5.x, var5.y, var5.z);

                if (var6 != null)
                {
                    int var9;
                    int var13;

                    if (var6 instanceof IPipeConnectable)
                    {
                        IPipeConnectable var7 = (IPipeConnectable)var6;
                        int var8 = var1.getPipePressure(var4);
                        var9 = var7.getPipePressure(var4 ^ 1);

                        if (var8 < var9)
                        {
                            continue;
                        }

                        FluidBuffer var10 = var1.getPipeBuffer(var4);

                        if (var10 == null)
                        {
                            continue;
                        }

                        int var11 = var10.getLevel();
                        var11 += var10.Delta;

                        if (var10.Type == 0 || var11 <= 0)
                        {
                            continue;
                        }

                        FluidBuffer var12 = var7.getPipeBuffer(var4 ^ 1);

                        if (var12 == null)
                        {
                            continue;
                        }

                        var13 = var12.getLevel();

                        if (var12.Type != 0 && var12.Type != var10.Type)
                        {
                            continue;
                        }

                        int var14 = Math.max(var8 > var9 ? 25 : 0, (var11 - var13) / 2);
                        var14 = Math.min(Math.min(var14, var12.getMaxLevel() - var13), var11);

                        if (var14 <= 0)
                        {
                            continue;
                        }

                        var10.addLevel(var10.Type, -var14);
                        var12.addLevel(var10.Type, var14);
                    }

                    if (var6 instanceof ITankContainer)
                    {
                        ITankContainer var18 = (ITankContainer)var6;
                        ILiquidTank var19 = var18.getTank(ForgeDirection.getOrientation(var4 ^ 1), (LiquidStack)null);

                        if (var19 != null)
                        {
                            var9 = var19.getTankPressure();
                            var9 = var9 > 0 ? 100 : (var9 < 0 ? -100 : 0);
                            int var20 = var1.getPipePressure(var4);
                            FluidBuffer var22 = var1.getPipeBuffer(var4);

                            if (var22 != null)
                            {
                                int var21 = var22.getLevel();
                                var21 += var22.Delta;
                                var13 = 0;
                                LiquidStack var23 = var19.getLiquid();
                                FluidClass var15 = null;

                                if (var23 != null && var23.amount > 0)
                                {
                                    var15 = getLiquidClass(var23);

                                    if (var15 == null)
                                    {
                                        continue;
                                    }

                                    var13 = var19.getLiquid().amount;

                                    if (var22.Type != 0 && var22.Type != var15.getFluidId())
                                    {
                                        continue;
                                    }
                                }

                                int var16;

                                if (var20 < var9 && var13 > 0)
                                {
                                    var16 = Math.max(25, (var13 - var21) / 2);
                                    var16 = Math.min(Math.min(var16, var22.getMaxLevel() - var21), var13);

                                    if (var16 > 0)
                                    {
                                        LiquidStack var17 = var19.drain(var16, true);
                                        var22.addLevel(var15.getFluidId(), var17.amount);
                                    }
                                }
                                else if (var20 > var9 && var22.Type != 0 && var21 > 0)
                                {
                                    var16 = Math.max(25, (var21 - var13) / 2);
                                    var16 = Math.min(Math.min(var16, var19.getCapacity() - var13), var21);

                                    if (var16 > 0)
                                    {
                                        var15 = getLiquidClass(var22.Type);
                                        var16 = var19.fill(var15.getLiquidStack(var16), true);
                                        var22.addLevel(var22.Type, -var16);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
