package com.eloraam.redpower.base;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventorySubCraft extends InventoryCrafting
{
    private Container eventHandler;
    private IInventory parent;

    public InventorySubCraft(Container var1, IInventory var2)
    {
        super(var1, 3, 3);
        this.parent = var2;
        this.eventHandler = var1;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 9;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return var1 >= 9 ? null : this.parent.getStackInSlot(var1);
    }

    /**
     * Returns the itemstack in the slot specified (Top left is 0, 0). Args: row, column
     */
    public ItemStack getStackInRowAndColumn(int var1, int var2)
    {
        if (var1 >= 0 && var1 < 3)
        {
            int var3 = var1 + var2 * 3;
            return this.getStackInSlot(var3);
        }
        else
        {
            return null;
        }
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        ItemStack var3 = this.parent.decrStackSize(var1, var2);

        if (var3 != null)
        {
            this.eventHandler.onCraftMatrixChanged(this);
        }

        return var3;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.parent.setInventorySlotContents(var1, var2);
        this.eventHandler.onCraftMatrixChanged(this);
    }
}
