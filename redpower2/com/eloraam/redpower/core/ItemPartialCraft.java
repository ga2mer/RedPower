package com.eloraam.redpower.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPartialCraft extends Item
{
    private ItemStack emptyItem = null;

    public ItemPartialCraft(int var1)
    {
        super(var1);
        this.setMaxStackSize(1);
        this.setNoRepair();
    }

    public void setEmptyItem(ItemStack var1)
    {
        this.emptyItem = var1;
    }

    public ItemStack getContainerItemStack(ItemStack var1)
    {
        int var2 = var1.getItemDamage();

        if (var2 == var1.getMaxDamage() && this.emptyItem != null)
        {
            return CoreLib.copyStack(this.emptyItem, 1);
        }
        else
        {
            ItemStack var3 = CoreLib.copyStack(var1, 1);
            var3.setItemDamage(var2 + 1);
            return var3;
        }
    }

    /**
     * True if this Item has a container item (a.k.a. crafting result)
     */
    public boolean hasContainerItem()
    {
        return true;
    }

    /**
     * If this returns true, after a recipe involving this item is crafted the container item will be added to the
     * player's inventory instead of remaining in the crafting grid.
     */
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack var1)
    {
        return false;
    }
}
