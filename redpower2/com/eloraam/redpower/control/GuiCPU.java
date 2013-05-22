package com.eloraam.redpower.control;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiCPU extends GuiContainer
{
    TileCPU tileCPU;

    public GuiCPU(InventoryPlayer var1, TileCPU var2)
    {
        super(new ContainerCPU(var1, var2));
        this.tileCPU = var2;
        this.ySize = 145;
        this.xSize = 227;
    }

    public GuiCPU(Container var1)
    {
        super(var1);
        this.ySize = 145;
        this.xSize = 227;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2) {}

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/control/cpugui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/control/cpugui.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7 = this.tileCPU.byte0;
        int var8;

        for (var8 = 0; var8 < 8; ++var8)
        {
            if ((var7 & 1 << var8) != 0)
            {
                this.drawTexturedModalRect(var5 + 14 + var8 * 12, var6 + 57, 227 + (var8 >> 2) * 12, 0, 12, 32);
            }
        }

        var7 = this.tileCPU.byte1;

        for (var8 = 0; var8 < 8; ++var8)
        {
            if ((var7 & 1 << var8) != 0)
            {
                this.drawTexturedModalRect(var5 + 118 + var8 * 12, var6 + 57, 227 + (var8 >> 2) * 12, 0, 12, 32);
            }
        }

        var7 = this.tileCPU.rbaddr;

        for (var8 = 0; var8 < 8; ++var8)
        {
            if ((var7 & 1 << var8) != 0)
            {
                this.drawTexturedModalRect(var5 + 118 + var8 * 12, var6 + 101, 227 + (var8 >> 2) * 12, 0, 12, 32);
            }
        }

        if (this.tileCPU.isRunning())
        {
            this.drawTexturedModalRect(var5 + 102, var6 + 99, 227, 32, 8, 8);
        }
        else
        {
            this.drawTexturedModalRect(var5 + 102, var6 + 112, 227, 32, 8, 8);
        }

        this.drawString(this.fontRenderer, String.format("Disk: %d", new Object[] {Integer.valueOf(this.tileCPU.byte0)}), var5 + 14, var6 + 47, -1);
        this.drawString(this.fontRenderer, String.format("Console: %d", new Object[] {Integer.valueOf(this.tileCPU.byte1)}), var5 + 118, var6 + 47, -1);
        this.drawString(this.fontRenderer, String.format("ID: %d", new Object[] {Integer.valueOf(this.tileCPU.rbaddr)}), var5 + 118, var6 + 91, -1);
        this.drawString(this.fontRenderer, String.format("START", new Object[] {Integer.valueOf(this.tileCPU.rbaddr)}), var5 + 50, var6 + 99, -1);
        this.drawString(this.fontRenderer, String.format("HALT", new Object[] {Integer.valueOf(this.tileCPU.rbaddr)}), var5 + 50, var6 + 112, -1);
        this.drawString(this.fontRenderer, String.format("RESET", new Object[] {Integer.valueOf(this.tileCPU.rbaddr)}), var5 + 50, var6 + 125, -1);
    }

    void sendSimple(int var1, int var2)
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var3 = new Packet212GuiEvent();
            var3.eventId = var1;
            var3.windowId = this.inventorySlots.windowId;
            var3.addByte(var2);
            var3.encode();
            CoreProxy.sendPacketToServer(var3);
        }
    }

    boolean sendEvent(int var1)
    {
        if (!this.mc.theWorld.isRemote)
        {
            return true;
        }
        else
        {
            Packet212GuiEvent var2 = new Packet212GuiEvent();
            var2.eventId = var1;
            var2.windowId = this.inventorySlots.windowId;
            var2.addByte(0);
            var2.encode();
            CoreProxy.sendPacketToServer(var2);
            return false;
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        int var4 = var1 - (this.width - this.xSize) / 2;
        int var5 = var2 - (this.height - this.ySize) / 2;
        int var6;

        if (var5 >= 57 && var5 <= 89)
        {
            for (var6 = 0; var6 < 8; ++var6)
            {
                if (var4 >= 14 + var6 * 12 && var4 <= 26 + var6 * 12)
                {
                    this.tileCPU.byte0 ^= 1 << var6;
                    this.sendSimple(1, this.tileCPU.byte0);
                    return;
                }
            }

            for (var6 = 0; var6 < 8; ++var6)
            {
                if (var4 >= 118 + var6 * 12 && var4 <= 130 + var6 * 12)
                {
                    this.tileCPU.byte1 ^= 1 << var6;
                    this.sendSimple(2, this.tileCPU.byte1);
                    return;
                }
            }
        }

        if (var5 >= 101 && var5 <= 133)
        {
            for (var6 = 0; var6 < 8; ++var6)
            {
                if (var4 >= 118 + var6 * 12 && var4 <= 130 + var6 * 12)
                {
                    this.tileCPU.rbaddr ^= 1 << var6;
                    this.sendSimple(3, this.tileCPU.rbaddr);
                    return;
                }
            }
        }

        if (var4 >= 87 && var4 <= 96)
        {
            if (var5 >= 98 && var5 <= 107)
            {
                if (this.sendEvent(4))
                {
                    this.tileCPU.warmBootCPU();
                }

                return;
            }

            if (var5 >= 111 && var5 <= 120)
            {
                if (this.sendEvent(5))
                {
                    this.tileCPU.haltCPU();
                }

                return;
            }

            if (var5 >= 124 && var5 <= 133)
            {
                if (this.sendEvent(6))
                {
                    this.tileCPU.coldBootCPU();
                }

                return;
            }
        }

        super.mouseClicked(var1, var2, var3);
    }
}
