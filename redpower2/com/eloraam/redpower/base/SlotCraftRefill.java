package com.eloraam.redpower.base;

import com.eloraam.redpower.core.CoreLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

public class SlotCraftRefill extends SlotCrafting
{
    IInventory allSlots;
    IInventory craftingMatrix;
    ContainerAdvBench eventHandler;

    public SlotCraftRefill(EntityPlayer var1, IInventory var2, IInventory var3, IInventory var4, ContainerAdvBench var5, int var6, int var7, int var8)
    {
        super(var1, var2, var3, var6, var7, var8);
        this.allSlots = var4;
        this.craftingMatrix = var2;
        this.eventHandler = var5;
    }

    private int findMatch(ItemStack var1)
    {
        for (int var2 = 0; var2 < 18; ++var2)
        {
            ItemStack var3 = this.allSlots.getStackInSlot(10 + var2);

            if (var3 != null && var3.stackSize != 0 && CoreLib.matchItemStackOre(var1, var3))
            {
                return 10 + var2;
            }
        }

        return -1;
    }

    public boolean isLastUse()
    {
        int var1 = 0;
        int var2;
        ItemStack var3;

        for (var2 = 0; var2 < 9; ++var2)
        {
            var3 = this.allSlots.getStackInSlot(var2);

            if (var3 == null)
            {
                var1 |= 1 << var2;
            }
            else if (!var3.isStackable())
            {
                var1 |= 1 << var2;
            }
            else if (var3.stackSize > 1)
            {
                var1 |= 1 << var2;
            }
        }

        if (var1 == 511)
        {
            return false;
        }
        else
        {
            for (var2 = 0; var2 < 18; ++var2)
            {
                var3 = this.allSlots.getStackInSlot(10 + var2);

                if (var3 != null && var3.stackSize != 0)
                {
                    int var4 = var3.stackSize;

                    for (int var5 = 0; var5 < 9; ++var5)
                    {
                        if ((var1 & 1 << var5) <= 0)
                        {
                            ItemStack var6 = this.allSlots.getStackInSlot(var5);

                            if (var6 != null && CoreLib.matchItemStackOre(var6, var3))
                            {
                                var1 |= 1 << var5;
                                --var4;

                                if (var4 == 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            return var1 != 511;
        }
    }

    public void onPickupFromSlot(EntityPlayer var1, ItemStack var2)
    {
        ItemStack[] var3 = this.eventHandler.getPlanItems();
        ItemStack[] var4 = new ItemStack[9];

        for (int var5 = 0; var5 < 9; ++var5)
        {
            ItemStack var6 = this.allSlots.getStackInSlot(var5);

            if (var6 == null)
            {
                var4[var5] = null;
            }
            else
            {
                var4[var5] = var6.copy();
            }
        }

        boolean var12 = this.isLastUse();
        int var13;

        if (var3 != null)
        {
            for (var13 = 0; var13 < 9; ++var13)
            {
                if (var4[var13] == null && var3[var13] != null)
                {
                    int var7 = this.findMatch(var3[var13]);

                    if (var7 >= 0)
                    {
                        ItemStack var8 = this.allSlots.getStackInSlot(var7);

                        if (var8 != null)
                        {
                            this.allSlots.decrStackSize(var7, 1);

                            if (var8.getItem().hasContainerItem())
                            {
                                ItemStack var9 = var8.getItem().getContainerItemStack(var8);
                                this.allSlots.setInventorySlotContents(var7, var9);
                            }
                        }
                    }
                }
            }
        }

        super.onPickupFromSlot(var1, var2);

        if (var12)
        {
            this.eventHandler.onCraftMatrixChanged(this.craftingMatrix);
        }
        else
        {
            boolean var15 = false;

            for (int var16 = 0; var16 < 9; ++var16)
            {
                if (var4[var16] != null)
                {
                    ItemStack var10 = this.allSlots.getStackInSlot(var16);

                    if (var3 == null || var3[var16] == null)
                    {
                        if (var10 != null)
                        {
                            if (!CoreLib.matchItemStackOre(var10, var4[var16]) && var4[var16].getItem().hasContainerItem())
                            {
                                ItemStack var11 = var4[var16].getItem().getContainerItemStack(var4[var16]);

                                if (var11 != null && var11.getItem().itemID == var10.getItem().itemID)
                                {
                                    var13 = this.findMatch(var4[var16]);

                                    if (var13 >= 0)
                                    {
                                        ItemStack var14 = this.allSlots.getStackInSlot(var13);
                                        this.allSlots.setInventorySlotContents(var13, var10);
                                        this.allSlots.setInventorySlotContents(var16, var14);
                                        var15 = true;
                                    }
                                }
                            }
                        }
                        else
                        {
                            var13 = this.findMatch(var4[var16]);

                            if (var13 >= 0)
                            {
                                this.allSlots.getStackInSlot(var13);
                                this.allSlots.setInventorySlotContents(var16, this.allSlots.decrStackSize(var13, 1));
                                var15 = true;
                            }
                        }
                    }
                }
            }

            this.eventHandler.onCraftMatrixChanged(this.craftingMatrix);
        }
    }
}
