package com.eloraam.redpower.control;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;

public class GuiDisplay extends GuiContainer
{
    TileDisplay disp;

    public GuiDisplay(IInventory var1, TileDisplay var2)
    {
        super(new ContainerDisplay(var1, var2));
        this.xSize = 350;
        this.ySize = 230;
        this.disp = var2;
    }

    void sendKey(int var1)
    {
        Packet212GuiEvent var2 = new Packet212GuiEvent();
        var2.eventId = 1;
        var2.windowId = this.inventorySlots.windowId;
        var2.addByte((byte)var1);
        var2.encode();
        CoreProxy.sendPacketToServer(var2);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        if (var2 == 1)
        {
            this.mc.thePlayer.closeScreen();
        }
        else
        {
            if (var1 == 10)
            {
                var1 = 13;
            }

            int var3 = 0;

            if (isShiftKeyDown())
            {
                var3 |= 64;
            }

            if (isCtrlKeyDown())
            {
                var3 |= 32;
            }

            switch (var2)
            {
                case 199:
                    this.sendKey(132 | var3);
                    break;

                case 200:
                    this.sendKey(128 | var3);
                    break;

                case 201:
                case 202:
                case 204:
                case 206:
                case 209:
                default:
                    if (var1 > 0 && var1 <= 127)
                    {
                        this.sendKey(var1);
                    }

                    break;

                case 203:
                    this.sendKey(130 | var3);
                    break;

                case 205:
                    this.sendKey(131 | var3);
                    break;

                case 207:
                    this.sendKey(133 | var3);
                    break;

                case 208:
                    this.sendKey(129 | var3);
                    break;

                case 210:
                    this.sendKey(134 | var3);
            }
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        FontRenderer var4 = this.mc.fontRenderer;
        int var6 = this.mc.renderEngine.getTexture("/eloraam/control/displaygui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/control/displaygui.png");
        int var7 = (this.width - this.xSize) / 2;
        int var8 = (this.height - this.ySize) / 2;
        this.drawDoubledRect(var7, var8, this.xSize, this.ySize, 0, 0, this.xSize, this.ySize);
        GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);

        for (int var9 = 0; var9 < 50; ++var9)
        {
            for (int var10 = 0; var10 < 80; ++var10)
            {
                int var11 = this.disp.screen[var9 * 80 + var10] & 255;

                if (var10 == this.disp.cursX && var9 == this.disp.cursY)
                {
                    if (this.disp.cursMode == 1)
                    {
                        var11 ^= 128;
                    }

                    if (this.disp.cursMode == 2)
                    {
                        long var12 = this.mc.theWorld.getWorldTime();

                        if ((var12 >> 2 & 1L) > 0L)
                        {
                            var11 ^= 128;
                        }
                    }
                }

                if (var11 != 32)
                {
                    this.drawDoubledRect(var7 + 15 + var10 * 4, var8 + 15 + var9 * 4, 4, 4, 350 + (var11 & 15) * 8, (var11 >> 4) * 8, 8, 8);
                }
            }
        }
    }

    public void drawDoubledRect(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8)
    {
        float var9 = 0.001953125F;
        float var10 = 0.00390625F;
        Tessellator var11 = Tessellator.instance;
        var11.startDrawingQuads();
        var11.addVertexWithUV((double)var1, (double)(var2 + var4), (double)this.zLevel, (double)((float)var5 * var9), (double)((float)(var6 + var8) * var10));
        var11.addVertexWithUV((double)(var1 + var3), (double)(var2 + var4), (double)this.zLevel, (double)((float)(var5 + var7) * var9), (double)((float)(var6 + var8) * var10));
        var11.addVertexWithUV((double)(var1 + var3), (double)var2, (double)this.zLevel, (double)((float)(var5 + var7) * var9), (double)((float)var6 * var10));
        var11.addVertexWithUV((double)var1, (double)var2, (double)this.zLevel, (double)((float)var5 * var9), (double)((float)var6 * var10));
        var11.draw();
    }
}
