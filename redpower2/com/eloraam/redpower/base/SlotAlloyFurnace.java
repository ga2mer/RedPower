package com.eloraam.redpower.base;

import com.eloraam.redpower.core.AchieveLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAlloyFurnace extends Slot
{
    private EntityPlayer thePlayer;
    int totalCrafted;

    public SlotAlloyFurnace(EntityPlayer var1, IInventory var2, int var3, int var4, int var5)
    {
        super(var2, var3, var4, var5);
        this.thePlayer = var1;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack var1)
    {
        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int var1)
    {
        if (this.getHasStack())
        {
            this.totalCrafted += Math.min(var1, this.getStack().stackSize);
        }

        return super.decrStackSize(var1);
    }

    public void onPickupFromSlot(EntityPlayer var1, ItemStack var2)
    {
        this.onCrafting(var2);
        super.onPickupFromSlot(var1, var2);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack var1, int var2)
    {
        this.totalCrafted += var2;
        this.onCrafting(var1);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack var1)
    {
        var1.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.totalCrafted);
        this.totalCrafted = 0;
        AchieveLib.onAlloy(this.thePlayer, var1);
    }
}
