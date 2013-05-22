package com.eloraam.redpower.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemSeedBag$InventorySeedBag implements IInventory
{
    ItemStack bagitem;
    ItemStack[] items;

    ItemSeedBag$InventorySeedBag(ItemStack var1)
    {
        this.bagitem = var1;
        this.unpackInventory();
    }

    void unpackInventory()
    {
        this.items = new ItemStack[9];

        if (this.bagitem.stackTagCompound != null)
        {
            NBTTagList var1 = this.bagitem.stackTagCompound.getTagList("contents");

            for (int var2 = 0; var2 < var1.tagCount(); ++var2)
            {
                NBTTagCompound var3 = (NBTTagCompound)var1.tagAt(var2);
                byte var4 = var3.getByte("Slot");

                if (var4 < 9)
                {
                    this.items[var4] = ItemStack.loadItemStackFromNBT(var3);
                }
            }
        }
    }

    void packInventory()
    {
        if (this.bagitem.stackTagCompound == null)
        {
            this.bagitem.setTagCompound(new NBTTagCompound());
        }

        int var1 = 0;
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < 9; ++var3)
        {
            if (this.items[var3] != null)
            {
                var1 += this.items[var3].stackSize;
                NBTTagCompound var4 = new NBTTagCompound();
                this.items[var3].writeToNBT(var4);
                var4.setByte("Slot", (byte)var3);
                var2.appendTag(var4);
            }
        }

        this.bagitem.stackTagCompound.setTag("contents", var2);
        this.bagitem.setItemDamage(var1 == 0 ? 0 : 577 - var1);
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
        return this.items[var1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.items[var1] == null)
        {
            return null;
        }
        else
        {
            ItemStack var3;

            if (this.items[var1].stackSize <= var2)
            {
                var3 = this.items[var1];
                this.items[var1] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.items[var1].splitStack(var2);

                if (this.items[var1].stackSize == 0)
                {
                    this.items[var1] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.items[var1] == null)
        {
            return null;
        }
        else
        {
            ItemStack var2 = this.items[var1];
            this.items[var1] = null;
            return var2;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.items[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Seed Bag";
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
        return 64;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        this.packInventory();
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    public void openChest() {}

    public void closeChest() {}

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
