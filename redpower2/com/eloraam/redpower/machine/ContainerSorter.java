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

public class ContainerSorter extends Container implements IHandleGuiEvent
{
    public byte[] colors = new byte[8];
    public int column;
    public int charge = 0;
    public int flow = 0;
    public int mode = 0;
    public int defcolor = 0;
    public int automode = 0;
    private TileSorter tileSorter;

    public ContainerSorter(IInventory var1, TileSorter var2)
    {
        this.tileSorter = var2;
        int var3;
        int var4;

        for (var3 = 0; var3 < 5; ++var3)
        {
            for (var4 = 0; var4 < 8; ++var4)
            {
                this.addSlotToContainer(new Slot(var2, var4 + var3 * 8, 26 + 18 * var4, 18 + 18 * var3));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 140 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(var1, var3, 8 + var3 * 18, 198));
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileSorter.isUseableByPlayer(var1);
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

            if (var2 < 40)
            {
                if (!this.mergeItemStack(var5, 40, 76, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 40, false))
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
        int var1;

        for (var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);

            for (int var3 = 0; var3 < 8; ++var3)
            {
                if (this.colors[var3] != this.tileSorter.colors[var3])
                {
                    var2.sendProgressBarUpdate(this, var3, this.tileSorter.colors[var3]);
                }
            }

            if (this.column != this.tileSorter.column)
            {
                var2.sendProgressBarUpdate(this, 8, this.tileSorter.column);
            }

            if (this.charge != this.tileSorter.cond.Charge)
            {
                var2.sendProgressBarUpdate(this, 9, this.tileSorter.cond.Charge);
            }

            if (this.flow != this.tileSorter.cond.Flow)
            {
                var2.sendProgressBarUpdate(this, 10, this.tileSorter.cond.Flow);
            }

            if (this.mode != this.tileSorter.mode)
            {
                var2.sendProgressBarUpdate(this, 11, this.tileSorter.mode);
            }

            if (this.defcolor != this.tileSorter.defcolor)
            {
                var2.sendProgressBarUpdate(this, 12, this.tileSorter.defcolor);
            }

            if (this.automode != this.tileSorter.automode)
            {
                var2.sendProgressBarUpdate(this, 13, this.tileSorter.automode);
            }
        }

        for (var1 = 0; var1 < 8; ++var1)
        {
            this.colors[var1] = this.tileSorter.colors[var1];
        }

        this.column = this.tileSorter.column;
        this.charge = this.tileSorter.cond.Charge;
        this.flow = this.tileSorter.cond.Flow;
        this.mode = this.tileSorter.mode;
        this.defcolor = this.tileSorter.defcolor;
        this.automode = this.tileSorter.automode;
    }

    public void func_20112_a(int var1, int var2)
    {
        this.updateProgressBar(var1, var2);
    }

    public void updateProgressBar(int var1, int var2)
    {
        if (var1 < 8)
        {
            this.tileSorter.colors[var1] = (byte)var2;
        }

        switch (var1)
        {
            case 8:
                this.tileSorter.column = (byte)var2;
                break;

            case 9:
                this.tileSorter.cond.Charge = var2;
                break;

            case 10:
                this.tileSorter.cond.Flow = var2;
                break;

            case 11:
                this.tileSorter.mode = (byte)var2;
                break;

            case 12:
                this.tileSorter.defcolor = (byte)var2;
                break;

            case 13:
                this.tileSorter.automode = (byte)var2;
        }
    }

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        try
        {
            switch (var1.eventId)
            {
                case 1:
                    this.tileSorter.mode = (byte)var1.getByte();
                    this.tileSorter.dirtyBlock();
                    break;

                case 2:
                    byte var2 = (byte)var1.getByte();

                    if (var2 >= 0 && var2 <= 8)
                    {
                        this.tileSorter.colors[var2] = (byte)var1.getByte();
                        this.tileSorter.dirtyBlock();
                    }

                    break;

                case 3:
                    this.tileSorter.defcolor = (byte)var1.getByte();
                    this.tileSorter.dirtyBlock();
                    break;

                case 4:
                    this.tileSorter.automode = (byte)var1.getByte();
                    this.tileSorter.pulses = 0;
                    this.tileSorter.dirtyBlock();
            }
        }
        catch (IOException var4)
        {
            ;
        }
    }
}
