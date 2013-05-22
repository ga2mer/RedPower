package com.eloraam.redpower.control;

import com.eloraam.redpower.core.IHandleGuiEvent;
import com.eloraam.redpower.core.Packet212GuiEvent;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerCPU extends Container implements IHandleGuiEvent
{
    private TileCPU tileCPU;
    int byte0 = 0;
    int byte1 = 0;
    int rbaddr = 0;
    boolean isrun = false;

    public ContainerCPU(IInventory var1, TileCPU var2)
    {
        this.tileCPU = var2;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileCPU.isUseableByPlayer(var1);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer var1, int var2)
    {
        return null;
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

            if (this.tileCPU.byte0 != this.byte0)
            {
                var2.sendProgressBarUpdate(this, 0, this.tileCPU.byte0);
            }

            if (this.tileCPU.byte1 != this.byte1)
            {
                var2.sendProgressBarUpdate(this, 1, this.tileCPU.byte1);
            }

            if (this.tileCPU.rbaddr != this.rbaddr)
            {
                var2.sendProgressBarUpdate(this, 2, this.tileCPU.rbaddr);
            }

            if (this.tileCPU.isRunning() != this.isrun)
            {
                var2.sendProgressBarUpdate(this, 3, this.tileCPU.isRunning() ? 1 : 0);
            }
        }

        this.byte0 = this.tileCPU.byte0;
        this.byte1 = this.tileCPU.byte1;
        this.rbaddr = this.tileCPU.rbaddr;
        this.isrun = this.tileCPU.isRunning();
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.tileCPU.byte0 = var2;
                break;

            case 1:
                this.tileCPU.byte1 = var2;
                break;

            case 2:
                this.tileCPU.rbaddr = var2;
                break;

            case 3:
                this.tileCPU.sliceCycles = var2 > 0 ? 0 : -1;
        }
    }

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        try
        {
            switch (var1.eventId)
            {
                case 1:
                    this.tileCPU.byte0 = var1.getByte();
                    break;

                case 2:
                    this.tileCPU.byte1 = var1.getByte();
                    break;

                case 3:
                    this.tileCPU.rbaddr = var1.getByte();
                    break;

                case 4:
                    this.tileCPU.warmBootCPU();
                    break;

                case 5:
                    this.tileCPU.haltCPU();
                    break;

                case 6:
                    this.tileCPU.coldBootCPU();
            }
        }
        catch (IOException var3)
        {
            ;
        }
    }
}
