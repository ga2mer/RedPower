package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiSorter extends GuiContainer
{
    static int[] paintColors = new int[] {16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583};
    TileSorter sorter;

    public GuiSorter(InventoryPlayer var1, TileSorter var2)
    {
        super(new ContainerSorter(var1, var2));
        this.sorter = var2;
        this.ySize = 222;
    }

    public GuiSorter(Container var1)
    {
        super(var1);
        this.ySize = 222;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Sorting Machine", 50, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/machine/sortmachine.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/sortmachine.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        if (this.sorter.mode < 2)
        {
            this.drawTexturedModalRect(var5 + 24 + 18 * this.sorter.column, var6 + 16, 176, 0, 20, 92);
        }

        int var7;

        for (var7 = 0; var7 < 8; ++var7)
        {
            if (this.sorter.colors[var7] > 0)
            {
                this.rect(var5 + 32 + var7 * 18, var6 + 114, 4, 4, paintColors[this.sorter.colors[var7] - 1]);
            }
            else
            {
                this.drawTexturedModalRect(var5 + 32 + var7 * 18, var6 + 114, 187, 92, 4, 4);
            }
        }

        var7 = this.sorter.cond.getChargeScaled(48);
        this.drawTexturedModalRect(var5 + 8, var6 + 68 - var7, 176, 140 - var7, 5, var7);
        var7 = this.sorter.cond.getFlowScaled(48);
        this.drawTexturedModalRect(var5 + 15, var6 + 68 - var7, 176, 140 - var7, 5, var7);

        if (this.sorter.cond.Charge > 600)
        {
            this.drawTexturedModalRect(var5 + 9, var6 + 12, 181, 92, 3, 6);
        }

        if (this.sorter.cond.Flow == -1)
        {
            this.drawTexturedModalRect(var5 + 16, var6 + 12, 184, 92, 3, 6);
        }

        this.drawTexturedModalRect(var5 + 7, var6 + 73, 210, 14 * this.sorter.automode, 14, 14);
        this.drawTexturedModalRect(var5 + 7, var6 + 91, 196, 14 * this.sorter.mode, 14, 14);

        if (this.sorter.mode == 4 || this.sorter.mode == 6)
        {
            this.drawTexturedModalRect(var5 + 7, var6 + 109, 27, 109, 14, 14);

            if (this.sorter.defcolor > 0)
            {
                this.rect(var5 + 12, var6 + 114, 4, 4, paintColors[this.sorter.defcolor - 1]);
            }
            else
            {
                this.drawTexturedModalRect(var5 + 12, var6 + 114, 187, 92, 4, 4);
            }
        }
    }

    void sendMode()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 1;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.sorter.mode);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    void sendAutoMode()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 4;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.sorter.automode);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    void sendColor(int var1)
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var2 = new Packet212GuiEvent();
            var2.eventId = 2;
            var2.windowId = this.inventorySlots.windowId;
            var2.addByte(var1);
            var2.addByte(this.sorter.colors[var1]);
            var2.encode();
            CoreProxy.sendPacketToServer(var2);
        }
    }

    void sendDefColor()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 3;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.sorter.defcolor);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    protected void changeColor(int var1, boolean var2)
    {
        if (var2)
        {
            ++this.sorter.colors[var1];

            if (this.sorter.colors[var1] > 16)
            {
                this.sorter.colors[var1] = 0;
            }
        }
        else
        {
            --this.sorter.colors[var1];

            if (this.sorter.colors[var1] < 0)
            {
                this.sorter.colors[var1] = 16;
            }
        }

        this.sendColor(var1);
    }

    protected void changeDefColor(boolean var1)
    {
        if (var1)
        {
            ++this.sorter.defcolor;

            if (this.sorter.defcolor > 16)
            {
                this.sorter.defcolor = 0;
            }
        }
        else
        {
            --this.sorter.defcolor;

            if (this.sorter.defcolor < 0)
            {
                this.sorter.defcolor = 16;
            }
        }

        this.sendDefColor();
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        int var4 = var1 - (this.width - this.xSize) / 2;
        int var5 = var2 - (this.height - this.ySize) / 2;

        if (var4 <= 21 && var4 >= 7)
        {
            if (var5 <= 105 && var5 >= 91)
            {
                if (var3 == 0)
                {
                    ++this.sorter.mode;

                    if (this.sorter.mode > 6)
                    {
                        this.sorter.mode = 0;
                    }
                }
                else
                {
                    --this.sorter.mode;

                    if (this.sorter.mode < 0)
                    {
                        this.sorter.mode = 6;
                    }
                }

                this.sendMode();
            }

            if (var5 <= 87 && var5 >= 73)
            {
                if (var3 == 0)
                {
                    ++this.sorter.automode;

                    if (this.sorter.automode > 2)
                    {
                        this.sorter.automode = 0;
                    }
                }
                else
                {
                    --this.sorter.automode;

                    if (this.sorter.automode < 0)
                    {
                        this.sorter.automode = 2;
                    }
                }

                this.sendAutoMode();
            }
        }

        if (var5 >= 110 && var5 <= 121)
        {
            for (int var6 = 0; var6 < 8; ++var6)
            {
                if (var4 >= 28 + var6 * 18 && var4 <= 39 + var6 * 18)
                {
                    this.changeColor(var6, var3 == 0);
                    return;
                }
            }

            if ((this.sorter.mode == 4 || this.sorter.mode == 6) && var4 >= 7 && var4 <= 21)
            {
                this.changeDefColor(var3 == 0);
                return;
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
