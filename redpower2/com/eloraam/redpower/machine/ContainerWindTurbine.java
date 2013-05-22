package com.eloraam.redpower.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWindTurbine extends Container
{
    int windSpeed;
    private TileWindTurbine tileWT;

    public ContainerWindTurbine(IInventory var1, TileWindTurbine var2)
    {
        this.tileWT = var2;
        this.addSlotToContainer(new Slot(var2, 0, 80, 35));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(var1, var3, 8 + var3 * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileWT.isUseableByPlayer(var1);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer var1, int var2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(var2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (var2 < 1)
            {
                if (!this.mergeItemStack(var5, 1, 37, true))
                {
                    return null;
                }
            }
            else
            {
                Slot var6 = (Slot)this.inventorySlots.get(0);
                ItemStack var7 = var6.getStack();

                if (var7 != null && var7.stackSize != 0)
                {
                    return null;
                }

                var6.putStack(var5.splitStack(1));
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(var1, var5);
        }

        return var3;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);

            if (this.windSpeed != this.tileWT.windSpeed)
            {
                var2.sendProgressBarUpdate(this, 0, this.tileWT.windSpeed);
            }
        }

        this.windSpeed = this.tileWT.windSpeed;
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.tileWT.windSpeed = var2;

            default:
        }
    }
}
