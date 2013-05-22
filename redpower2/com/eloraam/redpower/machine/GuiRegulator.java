package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiRegulator extends GuiContainer
{
    static int[] paintColors = new int[] {16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583};
    TileRegulator tileRegulator;

    public GuiRegulator(InventoryPlayer var1, TileRegulator var2)
    {
        super(new ContainerRegulator(var1, var2));
        this.tileRegulator = var2;
        this.xSize = 211;
        this.ySize = 167;
    }

    public GuiRegulator(Container var1)
    {
        super(var1);
        this.xSize = 211;
        this.ySize = 167;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString(this.tileRegulator.getInvName(), 79, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 25, this.ySize - 96 + 3, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/machine/regulator.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/regulator.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        if (this.tileRegulator.color > 0)
        {
            this.rect(var5 + 140, var6 + 60, 4, 4, paintColors[this.tileRegulator.color - 1]);
        }
        else
        {
            this.drawTexturedModalRect(var5 + 140, var6 + 60, 212, 0, 4, 4);
        }

        this.drawTexturedModalRect(var5 + 135, var6 + 19, 216, 14 * this.tileRegulator.mode, 14, 14);
    }

    void sendColor()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 1;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.tileRegulator.color);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    void sendMode()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 2;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.tileRegulator.mode);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    protected void changeColor(boolean var1)
    {
        if (var1)
        {
            ++this.tileRegulator.color;

            if (this.tileRegulator.color > 16)
            {
                this.tileRegulator.color = 0;
            }
        }
        else
        {
            --this.tileRegulator.color;

            if (this.tileRegulator.color < 0)
            {
                this.tileRegulator.color = 16;
            }
        }

        this.sendColor();
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        int var4 = var1 - (this.width - this.xSize) / 2;
        int var5 = var2 - (this.height - this.ySize) / 2;

        if (var4 >= 136 && var4 <= 147)
        {
            if (var5 >= 56 && var5 <= 67)
            {
                this.changeColor(var3 == 0);
                return;
            }

            if (var5 >= 19 && var5 <= 32)
            {
                if (var3 == 0)
                {
                    ++this.tileRegulator.mode;

                    if (this.tileRegulator.mode > 1)
                    {
                        this.tileRegulator.mode = 0;
                    }
                }
                else
                {
                    --this.tileRegulator.mode;

                    if (this.tileRegulator.mode < 0)
                    {
                        this.tileRegulator.mode = 1;
                    }
                }

                this.sendMode();
            }
        }

        super.mouseClicked(var1, var2, var3);
    }

    private void rect(int var1, int var2, int var3, int var4, int var5)
    {
        var3 += var1;
        var4 += var2;
        float var6 = (float)(var5 >> 16 & 255) / 255.0F;
        float var7 = (float)(var5 >> 8 & 255) / 255.0F;
        float var8 = (float)(var5 & 255) / 255.0F;
        Tessellator var9 = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(var6, var7, var8, 1.0F);
        var9.startDrawingQuads();
        var9.addVertex((double)var1, (double)var4, 0.0D);
        var9.addVertex((double)var3, (double)var4, 0.0D);
        var9.addVertex((double)var3, (double)var2, 0.0D);
        var9.addVertex((double)var1, (double)var2, 0.0D);
        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }
}