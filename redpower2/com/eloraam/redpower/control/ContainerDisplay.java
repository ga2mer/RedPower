package com.eloraam.redpower.control;

import com.eloraam.redpower.control.ContainerDisplay$RLECompressor;
import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.IHandleGuiEvent;
import com.eloraam.redpower.core.Packet212GuiEvent;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerDisplay extends Container implements IHandleGuiEvent
{
    private TileDisplay tileDisplay;
    private byte[] screen = new byte[4000];
    int cursx = 0;
    int cursy = 0;
    int cursmode = 0;

    void decompress(byte[] var1, byte[] var2)
    {
        int var3 = 0;
        int var4 = 0;

        while (var4 < var1.length)
        {
            if (var3 >= var2.length)
            {
                return;
            }

            int var5 = var1[var4++] & 255;

            if ((var5 & 128) == 0)
            {
                var3 += var5 & 127;
            }
            else
            {
                int var6;
                int var7;

                if (var5 == 255)
                {
                    if (var4 + 2 > var1.length)
                    {
                        return;
                    }

                    var6 = Math.min(var1[var4] & 255, var2.length - var3);

                    for (var7 = 0; var7 < var6; ++var7)
                    {
                        var2[var3 + var7] = var1[var4 + 1];
                    }

                    var3 += var6;
                    var4 += 2;
                }
                else
                {
                    var6 = Math.min(Math.min(var5 & 127, var2.length - var3), var1.length - var4);

                    for (var7 = 0; var7 < var6; ++var7)
                    {
                        var2[var3 + var7] = var1[var4 + var7];
                    }

                    var3 += var6;
                    var4 += var6;
                }
            }
        }
    }

    public ContainerDisplay(IInventory var1, TileDisplay var2)
    {
        this.tileDisplay = var2;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileDisplay.isUseableByPlayer(var1);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer var1, int var2)
    {
        return null;
    }

    byte[] getDisplayRLE()
    {
        ContainerDisplay$RLECompressor var1 = new ContainerDisplay$RLECompressor(this);

        for (int var2 = 0; var2 < 4000; ++var2)
        {
            var1.addByte(this.tileDisplay.screen[var2], this.screen[var2] != this.tileDisplay.screen[var2]);
            this.screen[var2] = this.tileDisplay.screen[var2];
        }

        var1.flush();
        return var1.getByteArray();
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        byte[] var1 = this.getDisplayRLE();

        for (int var2 = 0; var2 < this.crafters.size(); ++var2)
        {
            ICrafting var3 = (ICrafting)this.crafters.get(var2);

            if (this.tileDisplay.cursX != this.cursx)
            {
                var3.sendProgressBarUpdate(this, 0, this.tileDisplay.cursX);
            }

            if (this.tileDisplay.cursY != this.cursy)
            {
                var3.sendProgressBarUpdate(this, 1, this.tileDisplay.cursY);
            }

            if (this.tileDisplay.cursMode != this.cursmode)
            {
                var3.sendProgressBarUpdate(this, 2, this.tileDisplay.cursMode);
            }

            if (var1 != null)
            {
                Packet212GuiEvent var4 = new Packet212GuiEvent();
                var4.eventId = 2;
                var4.windowId = this.windowId;
                var4.addByteArray(var1);
                var4.encode();
                CoreProxy.sendPacketToCrafting(var3, var4);
            }
        }

        this.cursx = this.tileDisplay.cursX;
        this.cursy = this.tileDisplay.cursY;
        this.cursmode = this.tileDisplay.cursMode;
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.tileDisplay.cursX = var2;
                return;

            case 1:
                this.tileDisplay.cursY = var2;
                return;

            case 2:
                this.tileDisplay.cursMode = var2;
                return;

            default:
        }
    }

    public void handleGuiEvent(Packet212GuiEvent var1)
    {
        try
        {
            switch (var1.eventId)
            {
                case 1:
                    this.tileDisplay.pushKey((byte)var1.getByte());
                    this.tileDisplay.dirtyBlock();
                    break;

                case 2:
                    byte[] var2 = var1.getByteArray();
                    this.decompress(var2, this.tileDisplay.screen);
            }
        }
        catch (IOException var3)
        {
            ;
        }
    }
}
