package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiBlueFurnace extends GuiContainer
{
    TileBlueFurnace furnace;

    public GuiBlueFurnace(InventoryPlayer var1, TileBlueFurnace var2)
    {
        super(new ContainerBlueFurnace(var1, var2));
        this.furnace = var2;
    }

    public GuiBlueFurnace(Container var1)
    {
        super(var1);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Blulectric Furnace", 48, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/machine/btfurnace.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/btfurnace.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7 = this.furnace.getCookScaled(24);
        this.drawTexturedModalRect(var5 + 89, var6 + 34, 176, 0, var7 + 1, 16);
        var7 = this.furnace.cond.getChargeScaled(48);
        this.drawTexturedModalRect(var5 + 25, var6 + 69 - var7, 176, 65 - var7, 5, var7);
        var7 = this.furnace.cond.getFlowScaled(48);
        this.drawTexturedModalRect(var5 + 32, var6 + 69 - var7, 176, 65 - var7, 5, var7);

        if (this.furnace.cond.Charge > 600)
        {
            this.drawTexturedModalRect(var5 + 26, var6 + 13, 181, 17, 3, 6);
        }

        if (this.furnace.cond.Flow == -1)
        {
            this.drawTexturedModalRect(var5 + 33, var6 + 13, 184, 17, 3, 6);
        }
    }
}
