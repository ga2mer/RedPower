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

public class ContainerManager extends Container implements IHandleGuiEvent
{
    public int charge = 0;
    public int flow = 0;
    public int color = 0;
    public int mode = 0;
    public int priority = 0;
    private TileManager tileManager;

    public ContainerManager(IInventory var1, TileManager var2)
    {
        this.tileManager = var2;
        int var3;
        int var4;

        for (var3 = 0; var3 < 4; ++var3)
        {
            for (var4 = 0; var4 < 6; ++var4)
            {
                this.addSlotToContainer(new Slot(var2, var4 + var3 * 6, 44 + 18 * var4, 18 + 18 * var3));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 104 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(var1, var3, 8 + var3 * 18, 162));
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileManager.isUseableByPlayer(var1);
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

            if (var2 < 24)
            {
                if (!this.mergeItemStack(var5, 24, 60, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 24, false))
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

            if (this.charge != this.tileManager.cond.Charge)
            {
                var2.sendProgressBarUpdate(this, 0, this.tileManager.cond.Charge);
            }

            if (this.flow != this.tileManager.cond.Flow)
            {
                var2.sendProgressBarUpdate(this, 1, this.tileManager.cond.Flow);
            }

            if (this.mode != this.tileManager.mode)
            {
                var2.sendProgressBarUpdate(this, 2, this.tileManager.mode);
            }

            if (this.color != this.tileManager.color)
            {
                var2.sendProgressBarUpdate(this, 3, this.tileManager.color);
            }

            if (this.priority != this.tileManager.priority)
            {
                var2.sendProgressBarUpdate(this, 4, this.tileManager.priority);
            }
        }

        this.charge = this.tileManager.cond.Charge;
        this.flow = this.tileManager.cond.Flow;
        this.mode = this.tileManager.mode;
        this.color = this.tileManager.color;
        this.priority = this.tileManager.priority;
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.tileManager.cond.Charge = var2;
                break;

            case 1:
                this.tileManager.cond.Flow = var2;
                break;

            case 2:
                this.tileManager.mode = (byte)var2;
                break;

            case 3:
                this.tileManager.color = (byte)var2;
                break;

            case 4:
                this.tileManager.priority = var2;
        }
    }

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        try
        {
            switch (var1.eventId)
            {
                case 1:
                    this.tileManager.mode = (byte)var1.getByte();
                    this.tileManager.dirtyBlock();
                    break;

                case 2:
                    this.tileManager.color = (byte)var1.getByte();
                    this.tileManager.dirtyBlock();
                    break;

                case 3:
                    this.tileManager.priority = (int)var1.getUVLC();
                    this.tileManager.dirtyBlock();
            }
        }
        catch (IOException var4)
        {
            ;
        }
    }
}
