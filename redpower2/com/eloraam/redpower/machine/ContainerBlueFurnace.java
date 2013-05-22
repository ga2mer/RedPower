package com.eloraam.redpower.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerBlueFurnace extends Container
{
    public int cooktime = 0;
    private TileBlueFurnace tileFurnace;
    public int charge = 0;
    public int flow = 0;

    public ContainerBlueFurnace(InventoryPlayer var1, TileBlueFurnace var2)
    {
        this.tileFurnace = var2;
        this.addSlotToContainer(new Slot(var2, 0, 62, 35));
        this.addSlotToContainer(new SlotFurnace(var1.player, var2, 1, 126, 35));
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
        return this.tileFurnace.isUseableByPlayer(var1);
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

            if (var2 < 2)
            {
                if (!this.mergeItemStack(var5, 2, 38, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 1, false))
            {
                return null;
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

            if (this.cooktime != this.tileFurnace.cooktime)
            {
                var2.sendProgressBarUpdate(this, 0, this.tileFurnace.cooktime);
            }

            if (this.charge != this.tileFurnace.cond.Charge)
            {
                var2.sendProgressBarUpdate(this, 1, this.tileFurnace.cond.Charge);
            }

            if (this.flow != this.tileFurnace.cond.Flow)
            {
                var2.sendProgressBarUpdate(this, 2, this.tileFurnace.cond.Flow & 65535);
                var2.sendProgressBarUpdate(this, 3, this.tileFurnace.cond.Flow >> 16 & 65535);
            }
        }

        this.cooktime = this.tileFurnace.cooktime;
        this.charge = this.tileFurnace.cond.Charge;
        this.flow = this.tileFurnace.cond.Flow;
    }

    public void func_20112_a(int var1, int var2)
    {
        this.updateProgressBar(var1, var2);
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.tileFurnace.cooktime = var2;
                break;

            case 1:
                this.tileFurnace.cond.Charge = var2;
                break;

            case 2:
                this.tileFurnace.cond.Flow = this.tileFurnace.cond.Flow & -65536 | var2 & 65535;
                break;

            case 3:
                this.tileFurnace.cond.Flow = this.tileFurnace.cond.Flow & 65535 | (var2 & 65535) << 16;
        }
    }
}
