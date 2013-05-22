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

public class ContainerRetriever extends Container implements IHandleGuiEvent
{
    private TileRetriever tileRetriever;
    public int charge = 0;
    public int flow = 0;
    public int color = 0;
    public int select = 0;
    public int mode = 0;

    public ContainerRetriever(IInventory var1, TileRetriever var2)
    {
        this.tileRetriever = var2;
        int var3;
        int var4;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 3; ++var4)
            {
                this.addSlotToContainer(new Slot(var2, var4 + var3 * 3, 62 + var4 * 18, 17 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
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
        return this.tileRetriever.isUseableByPlayer(var1);
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

            if (var2 < 9)
            {
                if (!this.mergeItemStack(var5, 9, 45, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 9, false))
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

            if (this.charge != this.tileRetriever.cond.Charge)
            {
                var2.sendProgressBarUpdate(this, 0, this.tileRetriever.cond.Charge);
            }

            if (this.flow != this.tileRetriever.cond.Flow)
            {
                var2.sendProgressBarUpdate(this, 1, this.tileRetriever.cond.Flow);
            }

            if (this.color != this.tileRetriever.color)
            {
                var2.sendProgressBarUpdate(this, 2, this.tileRetriever.color);
            }

            if (this.select != this.tileRetriever.select)
            {
                var2.sendProgressBarUpdate(this, 3, this.tileRetriever.select);
            }

            if (this.mode != this.tileRetriever.mode)
            {
                var2.sendProgressBarUpdate(this, 4, this.tileRetriever.mode);
            }
        }

        this.flow = this.tileRetriever.cond.Flow;
        this.charge = this.tileRetriever.cond.Charge;
        this.color = this.tileRetriever.color;
        this.select = this.tileRetriever.select;
        this.mode = this.tileRetriever.mode;
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.tileRetriever.cond.Charge = var2;
                break;

            case 1:
                this.tileRetriever.cond.Flow = var2;
                break;

            case 2:
                this.tileRetriever.color = (byte)var2;
                break;

            case 3:
                this.tileRetriever.select = (byte)var2;
                break;

            case 4:
                this.tileRetriever.mode = (byte)var2;
        }
    }

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        try
        {
            switch (var1.eventId)
            {
                case 1:
                    this.tileRetriever.color = (byte)var1.getByte();
                    this.tileRetriever.dirtyBlock();
                    break;

                case 2:
                    this.tileRetriever.mode = (byte)var1.getByte();
                    this.tileRetriever.dirtyBlock();
            }
        }
        catch (IOException var4)
        {
            ;
        }
    }
}
