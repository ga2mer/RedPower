package com.eloraam.redpower.base;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.IRedbusConnectable;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiBusId extends GuiContainer
{
    IRedbusConnectable rbConn;

    public GuiBusId(InventoryPlayer var1, IRedbusConnectable var2)
    {
        super(new ContainerBusId(var1, var2));
        this.rbConn = var2;
        this.ySize = 81;
        this.xSize = 123;
    }

    public GuiBusId(Container var1)
    {
        super(var1);
        this.ySize = 81;
        this.xSize = 123;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Set Bus Id", 32, 6, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/base/idgui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/base/idgui.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7 = this.rbConn.rbGetAddr();

        for (int var8 = 0; var8 < 8; ++var8)
        {
            if ((var7 & 1 << var8) != 0)
            {
                this.drawTexturedModalRect(var5 + 16 + var8 * 12, var6 + 25, 123, 0, 8, 16);
            }
        }

        this.drawCenteredString(this.fontRenderer, String.format("ID: %d", new Object[] {Integer.valueOf(var7)}), this.width / 2, var6 + 60, -1);
    }

    void sendAddr()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 1;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.rbConn.rbGetAddr());
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        int var4 = var1 - (this.width - this.xSize) / 2;
        int var5 = var2 - (this.height - this.ySize) / 2;

        if (var5 >= 25 && var5 <= 41)
        {
            for (int var6 = 0; var6 < 8; ++var6)
            {
                if (var4 >= 16 + var6 * 12 && var4 <= 24 + var6 * 12)
                {
                    this.rbConn.rbSetAddr(this.rbConn.rbGetAddr() ^ 1 << var6);
                    this.sendAddr();
                    return;
                }
            }
        }

        super.mouseClicked(var1, var2, var3);
    }
}
