package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiManager extends GuiContainer
{
    static int[] paintColors = new int[] {16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583};
    TileManager manager;

    public GuiManager(InventoryPlayer var1, TileManager var2)
    {
        super(new ContainerManager(var1, var2));
        this.manager = var2;
        this.ySize = 186;
    }

    public GuiManager(Container var1)
    {
        super(var1);
        this.ySize = 186;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Manager", 68, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/machine/manager.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/manager.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7 = this.manager.cond.getChargeScaled(48);
        this.drawTexturedModalRect(var5 + 17, var6 + 76 - var7, 176, 48 - var7, 5, var7);
        var7 = this.manager.cond.getFlowScaled(48);
        this.drawTexturedModalRect(var5 + 24, var6 + 76 - var7, 176, 48 - var7, 5, var7);

        if (this.manager.cond.Charge > 600)
        {
            this.drawTexturedModalRect(var5 + 18, var6 + 20, 181, 0, 3, 6);
        }

        if (this.manager.cond.Flow == -1)
        {
            this.drawTexturedModalRect(var5 + 25, var6 + 20, 184, 0, 3, 6);
        }

        this.drawTexturedModalRect(var5 + 153, var6 + 37, 191, 14 * this.manager.mode, 14, 14);

        if (this.manager.color > 0)
        {
            this.rect(var5 + 158, var6 + 78, 4, 4, paintColors[this.manager.color - 1]);
        }
        else
        {
            this.drawTexturedModalRect(var5 + 158, var6 + 78, 187, 0, 4, 4);
        }

        String var8 = String.format("%d", new Object[] {Integer.valueOf(this.manager.priority)});
        this.fontRenderer.drawStringWithShadow(var8, var5 + 160 - this.fontRenderer.getStringWidth(var8) / 2, var6 + 58, 16777215);
    }

    void sendMode()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 1;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.manager.mode);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    void sendColor()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 2;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.manager.color);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    void sendPriority()
    {
        if (this.mc.theWorld.isRemote)
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 3;
            var1.windowId = this.inventorySlots.windowId;
            var1.addUVLC((long)this.manager.priority);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    protected void changeColor(boolean var1)
    {
        if (var1)
        {
            ++this.manager.color;

            if (this.manager.color > 16)
            {
                this.manager.color = 0;
            }
        }
        else
        {
            --this.manager.color;

            if (this.manager.color < 0)
            {
                this.manager.color = 16;
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

        if (var4 >= 154 && var4 <= 165)
        {
            if (var5 >= 38 && var5 <= 50)
            {
                if (var3 == 0)
                {
                    ++this.manager.mode;

                    if (this.manager.mode > 1)
                    {
                        this.manager.mode = 0;
                    }
                }
                else
                {
                    --this.manager.mode;

                    if (this.manager.mode < 0)
                    {
                        this.manager.mode = 1;
                    }
                }

                this.sendMode();
            }

            if (var5 >= 56 && var5 <= 68)
            {
                if (var3 == 0)
                {
                    ++this.manager.priority;

                    if (this.manager.priority > 9)
                    {
                        this.manager.priority = 0;
                    }
                }
                else
                {
                    --this.manager.priority;

                    if (this.manager.priority < 0)
                    {
                        this.manager.priority = 9;
                    }
                }

                this.sendPriority();
            }

            if (var5 >= 74 && var5 <= 86)
            {
                this.changeColor(var3 == 0);
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
