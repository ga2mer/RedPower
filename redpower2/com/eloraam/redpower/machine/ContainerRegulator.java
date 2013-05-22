package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.IHandleGuiEvent;
import com.eloraam.redpower.core.Packet212GuiEvent;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRegulator extends Container implements IHandleGuiEvent
{
    private TileRegulator tileRegulator;
    public int color = 0;
    public int mode = 0;

    public ContainerRegulator(IInventory var1, TileRegulator var2)
    {
        this.tileRegulator = var2;
        int var3;
        int var4;

        for (int var5 = 0; var5 < 3; ++var5)
        {
            for (var3 = 0; var3 < 3; ++var3)
            {
                for (var4 = 0; var4 < 3; ++var4)
                {
                    this.addSlotToContainer(new Slot(var2, var4 + var3 * 3 + var5 * 9, 8 + var4 * 18 + var5 * 72, 18 + var3 * 18));
                }
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(var1, var4 + var3 * 9 + 9, 26 + var4 * 18, 86 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(var1, var3, 26 + var3 * 18, 144));
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileRegulator.isUseableByPlayer(var1);
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

            if (var2 < 27)
            {
                if (!this.mergeItemStack(var5, 27, 63, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 9, 18, false))
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

            if (this.color != this.tileRegulator.color)
            {
                var2.sendProgressBarUpdate(this, 0, this.tileRegulator.color);
            }

            if (this.mode != this.tileRegulator.mode)
            {
                var2.sendProgressBarUpdate(this, 1, this.tileRegulator.mode);
            }
        }

        this.color = this.tileRegulator.color;
        this.mode = this.tileRegulator.mode;
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.tileRegulator.color = (byte)var2;
                break;

            case 1:
                this.tileRegulator.mode = (byte)var2;
        }
    }

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        try
        {
            switch (var1.eventId)
            {
                case 1:
                    this.tileRegulator.color = (byte)var1.getByte();
                    this.tileRegulator.dirtyBlock();
                    break;

                case 2:
                    this.tileRegulator.mode = (byte)var1.getByte();
                    this.tileRegulator.dirtyBlock();
                    break;

                default:
                    return;
            }
        }
        catch (IOException var4)
        {
            ;
        }
    }
}
