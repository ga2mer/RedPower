package com.eloraam.redpower.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class MachineLib$SubInventory implements IInventory
{
    IInventory parent;
    int start;
    int size;

    MachineLib$SubInventory(IInventory var1, int var2, int var3)
    {
        this.parent = var1;
        this.start = var2;
        this.size = var3;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.size;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.parent.getStackInSlot(var1 + this.start);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        return this.parent.decrStackSize(var1 + this.start, var2);
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return this.parent.getStackInSlotOnClosing(var1 + this.start);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.parent.setInventorySlotContents(var1 + this.start, var2);
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return this.parent.getInvName();
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return this.parent.getInventoryStackLimit();
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        this.parent.onInventoryChanged();
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.parent.isUseableByPlayer(var1);
    }

    public void openChest() {}

    public void closeChest() {}

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
