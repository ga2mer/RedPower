package com.eloraam.redpower.base;

import com.eloraam.redpower.RedPowerBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAdvBench$SlotPlan extends Slot
{
    public ContainerAdvBench$SlotPlan(IInventory var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack var1)
    {
        return var1.itemID == RedPowerBase.itemPlanBlank.itemID || var1.itemID == RedPowerBase.itemPlanFull.itemID;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }
}
