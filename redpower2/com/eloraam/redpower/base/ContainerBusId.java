package com.eloraam.redpower.base;

import com.eloraam.redpower.core.IHandleGuiEvent;
import com.eloraam.redpower.core.IRedbusConnectable;
import com.eloraam.redpower.core.Packet212GuiEvent;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerBusId extends Container implements IHandleGuiEvent
{
    private IRedbusConnectable rbConn;
    int addr = 0;

    public ContainerBusId(IInventory var1, IRedbusConnectable var2)
    {
        this.rbConn = var2;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
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

            if (this.rbConn.rbGetAddr() != this.addr)
            {
                var2.sendProgressBarUpdate(this, 0, this.rbConn.rbGetAddr());
            }
        }

        this.addr = this.rbConn.rbGetAddr();
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.rbConn.rbSetAddr(var2);
                return;

            default:
        }
    }

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        try
        {
            if (var1.eventId != 1)
            {
                return;
            }

            this.rbConn.rbSetAddr(var1.getByte());
        }
        catch (IOException var3)
        {
            ;
        }
    }
}
