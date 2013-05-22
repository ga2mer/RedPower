package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.IHandleGuiEvent;
import com.eloraam.redpower.core.Packet212GuiEvent;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerTimer extends Container implements IHandleGuiEvent
{
    long interval = 0L;
    private TileLogicPointer tileLogic;

    public ContainerTimer(IInventory var1, TileLogicPointer var2)
    {
        this.tileLogic = var2;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileLogic.isUseableByPlayer(var1);
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
        long var1 = this.tileLogic.getInterval();

        for (int var3 = 0; var3 < this.crafters.size(); ++var3)
        {
            ICrafting var4 = (ICrafting)this.crafters.get(var3);

            if (var1 != this.interval)
            {
                Packet212GuiEvent var5 = new Packet212GuiEvent();
                var5.eventId = 1;
                var5.windowId = this.windowId;
                var5.addUVLC(var1);
                var5.encode();
                CoreProxy.sendPacketToCrafting(var4, var5);
            }
        }

        this.interval = var1;
    }

    public void updateProgressBar(int var1, int var2) {}

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        try
        {
            switch (var1.eventId)
            {
                case 1:
                    long var2 = var1.getUVLC();
                    this.tileLogic.setInterval(var2);

                    if (this.tileLogic.worldObj != null)
                    {
                        this.tileLogic.updateBlock();
                    }
            }
        }
        catch (IOException var5)
        {
            ;
        }
    }
}
