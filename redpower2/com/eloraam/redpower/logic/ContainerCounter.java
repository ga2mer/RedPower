package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.IHandleGuiEvent;
import com.eloraam.redpower.core.Packet212GuiEvent;
import com.eloraam.redpower.logic.TileLogicStorage$LogicStorageCounter;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerCounter extends Container implements IHandleGuiEvent
{
    int Count = 0;
    int CountMax = 0;
    int Inc = 0;
    int Dec = 0;
    private TileLogicStorage tileLogic;

    public ContainerCounter(IInventory var1, TileLogicStorage var2)
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
        TileLogicStorage$LogicStorageCounter var1 = (TileLogicStorage$LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);

        for (int var2 = 0; var2 < this.crafters.size(); ++var2)
        {
            ICrafting var3 = (ICrafting)this.crafters.get(var2);

            if (this.Count != var1.Count)
            {
                var3.sendProgressBarUpdate(this, 0, var1.Count);
            }

            if (this.CountMax != var1.CountMax)
            {
                var3.sendProgressBarUpdate(this, 1, var1.CountMax);
            }

            if (this.Inc != var1.Inc)
            {
                var3.sendProgressBarUpdate(this, 2, var1.Inc);
            }

            if (this.Dec != var1.Dec)
            {
                var3.sendProgressBarUpdate(this, 3, var1.Dec);
            }
        }

        this.Count = var1.Count;
        this.CountMax = var1.CountMax;
        this.Inc = var1.Inc;
        this.Dec = var1.Dec;
    }

    public void updateProgressBar(int var1, int var2)
    {
        TileLogicStorage$LogicStorageCounter var3 = (TileLogicStorage$LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);

        switch (var1)
        {
            case 0:
                var3.Count = var2;
                break;

            case 1:
                var3.CountMax = var2;
                break;

            case 2:
                var3.Inc = var2;
                break;

            case 3:
                var3.Dec = var2;
        }
    }

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        TileLogicStorage$LogicStorageCounter var3 = (TileLogicStorage$LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);

        try
        {
            switch (var1.eventId)
            {
                case 0:
                    var3.Count = (int)var1.getUVLC();
                    this.tileLogic.updateBlock();
                    break;

                case 1:
                    var3.CountMax = (int)var1.getUVLC();
                    this.tileLogic.updateBlock();
                    break;

                case 2:
                    var3.Inc = (int)var1.getUVLC();
                    this.tileLogic.dirtyBlock();
                    break;

                case 3:
                    var3.Dec = (int)var1.getUVLC();
                    this.tileLogic.dirtyBlock();
            }
        }
        catch (IOException var5)
        {
            ;
        }
    }
}
