package com.eloraam.redpower.core;

import com.eloraam.redpower.core.MachineLib$FilterMap;
import com.eloraam.redpower.core.MachineLib$SubInventory;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class MachineLib
{
    public static IInventory getInventory(World var0, WorldCoord var1)
    {
        IInventory var2 = (IInventory)CoreLib.getTileEntity(var0, var1, IInventory.class);

        if (!(var2 instanceof TileEntityChest))
        {
            return var2;
        }
        else
        {
            TileEntityChest var3 = (TileEntityChest)CoreLib.getTileEntity(var0, var1.x - 1, var1.y, var1.z, TileEntityChest.class);

            if (var3 != null)
            {
                return new InventoryLargeChest("Large chest", var3, var2);
            }
            else
            {
                var3 = (TileEntityChest)CoreLib.getTileEntity(var0, var1.x + 1, var1.y, var1.z, TileEntityChest.class);

                if (var3 != null)
                {
                    return new InventoryLargeChest("Large chest", var2, var3);
                }
                else
                {
                    var3 = (TileEntityChest)CoreLib.getTileEntity(var0, var1.x, var1.y, var1.z - 1, TileEntityChest.class);

                    if (var3 != null)
                    {
                        return new InventoryLargeChest("Large chest", var3, var2);
                    }
                    else
                    {
                        var3 = (TileEntityChest)CoreLib.getTileEntity(var0, var1.x, var1.y, var1.z + 1, TileEntityChest.class);
                        return (IInventory)(var3 != null ? new InventoryLargeChest("Large chest", var2, var3) : var2);
                    }
                }
            }
        }
    }

    public static IInventory getSideInventory(World var0, WorldCoord var1, int var2, boolean var3)
    {
        IInventory var4 = getInventory(var0, var1);

        if (var4 == null)
        {
            return null;
        }
        else if (var4 instanceof ISidedInventory)
        {
            ISidedInventory var5 = (ISidedInventory)var4;
            int var6 = var5.getStartInventorySide(ForgeDirection.getOrientation(var2));
            int var7 = var5.getSizeInventorySide(ForgeDirection.getOrientation(var2));
            return new MachineLib$SubInventory(var4, var6, var7);
        }
        else
        {
            return var4;
        }
    }

    public static boolean addToInventoryCore(World var0, ItemStack var1, WorldCoord var2, int var3, boolean var4)
    {
        IInventory var5 = getInventory(var0, var2);

        if (var5 == null)
        {
            return false;
        }
        else
        {
            int var6 = 0;
            int var7 = var5.getSizeInventory();

            if (var5 instanceof ISidedInventory)
            {
                ISidedInventory var8 = (ISidedInventory)var5;
                var6 = var8.getStartInventorySide(ForgeDirection.getOrientation(var3));
                var7 = var8.getSizeInventorySide(ForgeDirection.getOrientation(var3));
            }

            return addToInventoryCore(var5, var1, var6, var7, var4);
        }
    }

    public static boolean addToInventoryCore(IInventory var0, ItemStack var1, int var2, int var3, boolean var4)
    {
        int var5;
        ItemStack var6;

        for (var5 = var2; var5 < var2 + var3; ++var5)
        {
            var6 = var0.getStackInSlot(var5);

            if (var6 == null)
            {
                if (!var4)
                {
                    return true;
                }
            }
            else if (var1.isItemEqual(var6) && ItemStack.areItemStackTagsEqual(var1, var6))
            {
                int var7 = Math.min(var6.getMaxStackSize(), var0.getInventoryStackLimit());
                var7 -= var6.stackSize;

                if (var7 > 0)
                {
                    int var8 = Math.min(var7, var1.stackSize);

                    if (!var4)
                    {
                        return true;
                    }

                    var6.stackSize += var8;
                    var0.setInventorySlotContents(var5, var6);
                    var1.stackSize -= var8;

                    if (var1.stackSize == 0)
                    {
                        return true;
                    }
                }
            }
        }

        if (!var4)
        {
            return false;
        }
        else
        {
            for (var5 = var2; var5 < var2 + var3; ++var5)
            {
                var6 = var0.getStackInSlot(var5);

                if (var6 == null)
                {
                    if (var0.getInventoryStackLimit() >= var1.stackSize)
                    {
                        var0.setInventorySlotContents(var5, var1);
                        return true;
                    }

                    var0.setInventorySlotContents(var5, var1.splitStack(var0.getInventoryStackLimit()));
                }
            }

            return false;
        }
    }

    public static boolean addToInventory(World var0, ItemStack var1, WorldCoord var2, int var3)
    {
        return addToInventoryCore(var0, var1, var2, var3, true);
    }

    public static boolean canAddToInventory(World var0, ItemStack var1, WorldCoord var2, int var3)
    {
        return addToInventoryCore(var0, var1, var2, var3, false);
    }

    public static void ejectItem(World var0, WorldCoord var1, ItemStack var2, int var3)
    {
        var1 = var1.copy();
        var1.step(var3);
        EntityItem var4 = new EntityItem(var0, (double)var1.x + 0.5D, (double)var1.y + 0.5D, (double)var1.z + 0.5D, var2);
        var4.motionX = 0.0D;
        var4.motionY = 0.0D;
        var4.motionZ = 0.0D;

        switch (var3)
        {
            case 0:
                var4.motionY = -0.3D;
                break;

            case 1:
                var4.motionY = 0.3D;
                break;

            case 2:
                var4.motionZ = -0.3D;
                break;

            case 3:
                var4.motionZ = 0.3D;
                break;

            case 4:
                var4.motionX = -0.3D;
                break;

            default:
                var4.motionX = 0.3D;
        }

        var4.delayBeforeCanPickup = 10;
        var0.spawnEntityInWorld(var4);
    }

    public static boolean handleItem(World var0, ItemStack var1, WorldCoord var2, int var3)
    {
        WorldCoord var4 = var2.copy();
        var4.step(var3);

        if (var1.stackSize == 0)
        {
            return true;
        }
        else if (TubeLib.addToTubeRoute(var0, var1, var2, var4, var3 ^ 1))
        {
            return true;
        }
        else if (addToInventory(var0, var1, var4, (var3 ^ 1) & 63))
        {
            return true;
        }
        else
        {
            TileEntity var5 = (TileEntity)CoreLib.getTileEntity(var0, var4, TileEntity.class);

            if (!(var5 instanceof IInventory) && !(var5 instanceof ITubeConnectable))
            {
                if (var0.isBlockSolidOnSide(var4.x, var4.y, var4.z, ForgeDirection.getOrientation(var3 ^ 1)))
                {
                    return false;
                }
                else
                {
                    ejectItem(var0, var2, var1, var3);
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }

    public static boolean handleItem(World var0, TubeItem var1, WorldCoord var2, int var3)
    {
        WorldCoord var4 = var2.copy();
        var4.step(var3);

        if (var1.item.stackSize == 0)
        {
            return true;
        }
        else if (TubeLib.addToTubeRoute(var0, var1, var2, var4, var3 ^ 1))
        {
            return true;
        }
        else if (addToInventory(var0, var1.item, var4, (var3 ^ 1) & 63))
        {
            return true;
        }
        else
        {
            TileEntity var5 = (TileEntity)CoreLib.getTileEntity(var0, var4, TileEntity.class);

            if (!(var5 instanceof IInventory) && !(var5 instanceof ITubeConnectable))
            {
                if (var0.isBlockSolidOnSide(var4.x, var4.y, var4.z, ForgeDirection.getOrientation(var3 ^ 1)))
                {
                    return false;
                }
                else
                {
                    ejectItem(var0, var2, var1.item, var3);
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }

    public static boolean addToRandomInventory(World var0, ItemStack var1, int var2, int var3, int var4)
    {
        return false;
    }

    public static int compareItem(ItemStack var0, ItemStack var1)
    {
        if (var0.itemID != var1.itemID)
        {
            return var0.itemID - var1.itemID;
        }
        else if (var0.getItemDamage() == var1.getItemDamage())
        {
            return 0;
        }
        else if (var0.getItem().getHasSubtypes())
        {
            return var0.getItemDamage() - var1.getItemDamage();
        }
        else
        {
            int var2 = var0.getItemDamage() <= 1 ? -1 : (var0.getItemDamage() == var0.getMaxDamage() - 1 ? 1 : 0);
            int var3 = var1.getItemDamage() <= 1 ? -1 : (var1.getItemDamage() == var1.getMaxDamage() - 1 ? 1 : 0);
            return var2 - var3;
        }
    }

    public static MachineLib$FilterMap makeFilterMap(ItemStack[] var0)
    {
        return new MachineLib$FilterMap(var0);
    }

    public static MachineLib$FilterMap makeFilterMap(ItemStack[] var0, int var1, int var2)
    {
        ItemStack[] var3 = new ItemStack[var2];
        System.arraycopy(var0, var1, var3, 0, var2);
        return new MachineLib$FilterMap(var3);
    }

    public static int[] genMatchCounts(MachineLib$FilterMap var0)
    {
        int[] var1 = new int[var0.filter.length];

        for (int var2 = 0; var2 < var0.filter.length; ++var2)
        {
            ItemStack var3 = var0.filter[var2];

            if (var3 != null && var3.stackSize != 0)
            {
                ArrayList var4 = (ArrayList)var0.map.get(var3);
                Integer var6;

                if (var4 != null && ((Integer)var4.get(0)).intValue() == var2)
                {
                    for (Iterator var5 = var4.iterator(); var5.hasNext(); var1[var2] += var0.filter[var6.intValue()].stackSize)
                    {
                        var6 = (Integer)var5.next();
                    }
                }
            }
        }

        return var1;
    }

    public static int decMatchCount(MachineLib$FilterMap var0, int[] var1, ItemStack var2)
    {
        ArrayList var3 = (ArrayList)var0.map.get(var2);

        if (var3 == null)
        {
            return 0;
        }
        else
        {
            int var4 = ((Integer)var3.get(0)).intValue();
            int var5 = Math.min(var1[var4], var2.stackSize);
            var1[var4] -= var5;
            return var5;
        }
    }

    public static int getMatchCount(MachineLib$FilterMap var0, int[] var1, ItemStack var2)
    {
        ArrayList var3 = (ArrayList)var0.map.get(var2);

        if (var3 == null)
        {
            return 0;
        }
        else
        {
            int var4 = ((Integer)var3.get(0)).intValue();
            int var5 = Math.min(var1[var4], var2.stackSize);
            return var5;
        }
    }

    public static boolean isMatchEmpty(int[] var0)
    {
        for (int var1 = 0; var1 < var0.length; ++var1)
        {
            if (var0[var1] > 0)
            {
                return false;
            }
        }

        return true;
    }

    public static void decMatchCounts(MachineLib$FilterMap var0, int[] var1, IInventory var2, int var3, int var4)
    {
        for (int var5 = var3; var5 < var3 + var4; ++var5)
        {
            ItemStack var6 = var2.getStackInSlot(var5);

            if (var6 != null && var6.stackSize != 0)
            {
                decMatchCount(var0, var1, var6);
            }
        }
    }

    public static boolean matchOneStack(MachineLib$FilterMap var0, IInventory var1, int var2, int var3, int var4)
    {
        ItemStack var5 = var0.filter[var4];
        int var6 = var5 == null ? 1 : var5.stackSize;

        for (int var7 = var2; var7 < var2 + var3; ++var7)
        {
            ItemStack var8 = var1.getStackInSlot(var7);

            if (var8 != null && var8.stackSize != 0)
            {
                if (var5 == null)
                {
                    return true;
                }

                if (compareItem(var5, var8) == 0)
                {
                    int var9 = Math.min(var8.stackSize, var6);
                    var6 -= var9;

                    if (var6 <= 0)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static int matchAnyStack(MachineLib$FilterMap var0, IInventory var1, int var2, int var3)
    {
        int[] var4 = new int[var0.filter.length];

        for (int var5 = var2; var5 < var2 + var3; ++var5)
        {
            ItemStack var6 = var1.getStackInSlot(var5);

            if (var6 != null && var6.stackSize != 0)
            {
                ArrayList var7 = (ArrayList)var0.map.get(var6);

                if (var7 != null)
                {
                    Iterator var8 = var7.iterator();

                    while (var8.hasNext())
                    {
                        Integer var9 = (Integer)var8.next();
                        int var10001 = var9.intValue();
                        var4[var10001] += var6.stackSize;

                        if (var4[var9.intValue()] >= var0.filter[var9.intValue()].stackSize)
                        {
                            return var9.intValue();
                        }
                    }
                }
            }
        }

        return -1;
    }

    public static int matchAnyStackCol(MachineLib$FilterMap var0, IInventory var1, int var2, int var3, int var4)
    {
        int[] var5 = new int[5];

        for (int var6 = var2; var6 < var2 + var3; ++var6)
        {
            ItemStack var7 = var1.getStackInSlot(var6);

            if (var7 != null && var7.stackSize != 0)
            {
                ArrayList var8 = (ArrayList)var0.map.get(var7);

                if (var8 != null)
                {
                    Iterator var9 = var8.iterator();

                    while (var9.hasNext())
                    {
                        Integer var10 = (Integer)var9.next();

                        if ((var10.intValue() & 7) == var4)
                        {
                            int var11 = var10.intValue() >> 3;
                            var5[var11] += var7.stackSize;

                            if (var5[var11] >= var0.filter[var10.intValue()].stackSize)
                            {
                                return var10.intValue();
                            }
                        }
                    }
                }
            }
        }

        return -1;
    }

    public static boolean matchAllCol(MachineLib$FilterMap var0, IInventory var1, int var2, int var3, int var4)
    {
        int[] var5 = new int[5];

        for (int var6 = var2; var6 < var2 + var3; ++var6)
        {
            ItemStack var7 = var1.getStackInSlot(var6);

            if (var7 != null && var7.stackSize != 0)
            {
                ArrayList var8 = (ArrayList)var0.map.get(var7);

                if (var8 != null)
                {
                    int var9 = var7.stackSize;
                    Iterator var10 = var8.iterator();

                    while (var10.hasNext())
                    {
                        Integer var11 = (Integer)var10.next();

                        if ((var11.intValue() & 7) == var4)
                        {
                            int var12 = var11.intValue() >> 3;
                            int var13 = Math.min(var9, var0.filter[var11.intValue()].stackSize - var5[var12]);
                            var5[var12] += var13;
                            var9 -= var13;

                            if (var9 == 0)
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }

        boolean var14 = false;

        for (int var15 = 0; var15 < 5; ++var15)
        {
            ItemStack var16 = var0.filter[var15 * 8 + var4];

            if (var16 != null && var16.stackSize != 0)
            {
                var14 = true;

                if (var16.stackSize > var5[var15])
                {
                    return false;
                }
            }
        }

        return var14;
    }

    public static boolean emptyInventory(IInventory var0, int var1, int var2)
    {
        for (int var3 = var1; var3 < var1 + var2; ++var3)
        {
            ItemStack var4 = var0.getStackInSlot(var3);

            if (var4 != null && var4.stackSize != 0)
            {
                return false;
            }
        }

        return true;
    }

    public static ItemStack collectOneStack(IInventory var0, int var1, int var2, ItemStack var3)
    {
        ItemStack var4 = null;
        int var5 = var3 == null ? 1 : var3.stackSize;

        for (int var6 = var1; var6 < var1 + var2; ++var6)
        {
            ItemStack var7 = var0.getStackInSlot(var6);

            if (var7 != null && var7.stackSize != 0)
            {
                if (var3 == null)
                {
                    var0.setInventorySlotContents(var6, (ItemStack)null);
                    return var7;
                }

                if (compareItem(var3, var7) == 0)
                {
                    int var8 = Math.min(var7.stackSize, var5);

                    if (var4 == null)
                    {
                        var4 = var0.decrStackSize(var6, var8);
                    }
                    else
                    {
                        var0.decrStackSize(var6, var8);
                        var4.stackSize += var8;
                    }

                    var5 -= var8;

                    if (var5 <= 0)
                    {
                        break;
                    }
                }
            }
        }

        return var4;
    }

    public static ItemStack collectOneStackFuzzy(IInventory var0, int var1, int var2, ItemStack var3)
    {
        ItemStack var4 = null;
        int var5 = var3 == null ? 1 : var3.getItem().getItemStackLimit();

        for (int var6 = var1; var6 < var1 + var2; ++var6)
        {
            ItemStack var7 = var0.getStackInSlot(var6);

            if (var7 != null && var7.stackSize != 0)
            {
                if (var3 == null)
                {
                    var0.setInventorySlotContents(var6, (ItemStack)null);
                    return var7;
                }

                if (compareItem(var3, var7) == 0)
                {
                    int var8 = Math.min(var7.stackSize, var5);

                    if (var4 == null)
                    {
                        var4 = var0.decrStackSize(var6, var8);
                    }
                    else
                    {
                        var0.decrStackSize(var6, var8);
                        var4.stackSize += var8;
                    }

                    var5 -= var8;

                    if (var5 <= 0)
                    {
                        break;
                    }
                }
            }
        }

        return var4;
    }
}
