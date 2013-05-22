package com.eloraam.redpower.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerAdvBench$InventorySubUpdate implements IInventory
{
    int size;
    int start;
    IInventory parent;

    final ContainerAdvBench this$0;

    public ContainerAdvBench$InventorySubUpdate(ContainerAdvBench var1, IInventory var2, int var3, int var4)
    {
        this.this$0 = var1;
        this.parent = var2;
        this.start = var3;
        this.size = var4;
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
        ItemStack var3 = this.parent.decrStackSize(var1 + this.start, var2);

        if (var3 != null)
        {
            this.this$0.onCraftMatrixChanged(this);
        }

        return var3;
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
        this.this$0.onCraftMatrixChanged(this);
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
        this.this$0.onCraftMatrixChanged(this);
        this.parent.onInventoryChanged();
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return false;
    }

    public void openChest() {}

    public void closeChest() {}

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
