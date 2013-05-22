package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiItemDetect extends GuiContainer
{
    TileItemDetect tileDetect;

    public GuiItemDetect(InventoryPlayer var1, TileItemDetect var2)
    {
        super(new ContainerItemDetect(var1, var2));
        this.tileDetect = var2;
    }

    public GuiItemDetect(Container var1)
    {
        super(var1);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Item Detector", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/machine/itemdet.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/itemdet.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(var5 + 117, var6 + 54, 176, 14 * this.tileDetect.mode, 14, 14);
    }

    void sendButton(int var1)
    {
        Packet212GuiEvent var2 = new Packet212GuiEvent();
        var2.eventId = 1;
        var2.windowId = this.inventorySlots.windowId;
        var2.addByte(var1);
        var2.encode();
        CoreProxy.sendPacketToServer(var2);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        int var4 = var1 - (this.width - this.xSize) / 2;
        int var5 = var2 - (this.height - this.ySize) / 2;

        if (var4 >= 117 && var5 >= 54 && var4 <= 131 && var5 <= 68)
        {
            if (var3 == 0)
            {
                ++this.tileDetect.mode;

                if (this.tileDetect.mode > 2)
                {
                    this.tileDetect.mode = 0;
                }
            }
            else
            {
                --this.tileDetect.mode;

                if (this.tileDetect.mode < 0)
                {
                    this.tileDetect.mode = 2;
                }
            }

            if (this.mc.theWorld.isRemote)
            {
                this.sendButton(this.tileDetect.mode);
            }
        }

        super.mouseClicked(var1, var2, var3);
    }
}
