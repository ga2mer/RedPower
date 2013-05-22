package com.eloraam.redpower.world;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSeedBag$SlotSeeds extends Slot
{
    public ContainerSeedBag$SlotSeeds(IInventory var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack var1)
    {
        return ItemSeedBag.canAdd(this.inventory, var1);
    }
}
