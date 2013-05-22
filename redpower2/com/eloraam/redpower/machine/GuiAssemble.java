package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiAssemble extends GuiContainer
{
    TileAssemble assemble;

    public GuiAssemble(InventoryPlayer var1, TileAssemble var2)
    {
        super(new ContainerAssemble(var1, var2));
        this.assemble = var2;
        this.ySize = 195;
    }

    public GuiAssemble(Container var1)
    {
        super(var1);
        this.ySize = 195;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Assembler", 65, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4;

        if (this.assemble.mode == 0)
        {
            var4 = this.mc.renderEngine.getTexture("/eloraam/machine/assembler.png");
        }
        else
        {
            var4 = this.mc.renderEngine.getTexture("/eloraam/machine/assembler2.png");
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/assembler.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(var5 + 152, var6 + 37, 196, 14 * this.assemble.mode, 14, 14);

        if (this.assemble.mode == 0)
        {
            this.drawTexturedModalRect(var5 + 6 + 18 * (this.assemble.select & 7), var6 + 16 + 18 * (this.assemble.select >> 3), 176, 0, 20, 20);

            for (int var7 = 1; var7 < 16; ++var7)
            {
                if ((this.assemble.skipSlots & 1 << var7) != 0)
                {
                    this.drawTexturedModalRect(var5 + 8 + 18 * (var7 & 7), var6 + 18 + 18 * (var7 >> 3), 176, 20, 16, 16);
                }
            }
        }
    }

    void sendMode()
    {
        if (!this.mc.theWorld.isRemote)
        {
            this.assemble.updateBlockChange();
        }
        else
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 1;
            var1.windowId = this.inventorySlots.windowId;
            var1.addByte(this.assemble.mode);
            var1.encode();
            CoreProxy.sendPacketToServer(var1);
        }
    }

    void sendSkip()
    {
        if (!this.mc.theWorld.isRemote)
        {
            this.assemble.updateBlockChange();
        }
        else
        {
            Packet212GuiEvent var1 = new Packet212GuiEvent();
            var1.eventId = 2;
            var1.windowId = this.inventorySlots.windowId;
            var1.addUVLC((long)this.assemble.skipSlots);
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

        if (var4 >= 152 && var5 >= 37 && var4 <= 166 && var5 <= 51)
        {
            if (var3 == 0)
            {
                ++this.assemble.mode;

                if (this.assemble.mode > 1)
                {
                    this.assemble.mode = 0;
                }
            }
            else
            {
                --this.assemble.mode;

                if (this.assemble.mode < 0)
                {
                    this.assemble.mode = 1;
                }
            }

            this.sendMode();
        }
        else
        {
            if (this.assemble.mode == 0 && this.mc.thePlayer.inventory.getItemStack() == null)
            {
                boolean var6 = false;

                for (int var7 = 1; var7 < 16; ++var7)
                {
                    int var8 = 8 + 18 * (var7 & 7);
                    int var9 = 18 + 18 * (var7 >> 3);

                    if (var4 >= var8 && var4 < var8 + 16 && var5 >= var9 && var5 < var9 + 16)
                    {
                        if (this.inventorySlots.getSlot(var7).getHasStack())
                        {
                            break;
                        }

                        this.assemble.skipSlots ^= 1 << var7;
                        var6 = true;
                    }
                }

                if (var6)
                {
                    this.sendSkip();
                    return;
                }
            }

            super.mouseClicked(var1, var2, var3);
        }
    }
}
