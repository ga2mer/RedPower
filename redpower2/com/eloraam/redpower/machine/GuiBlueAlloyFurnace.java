package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiBlueAlloyFurnace extends GuiContainer
{
    TileBlueAlloyFurnace furnace;

    public GuiBlueAlloyFurnace(InventoryPlayer var1, TileBlueAlloyFurnace var2)
    {
        super(new ContainerBlueAlloyFurnace(var1, var2));
        this.furnace = var2;
    }

    public GuiBlueAlloyFurnace(Container var1)
    {
        super(var1);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Blulectric Alloy Furnace", 38, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/machine/btafurnace.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/btafurnace.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7 = this.furnace.getCookScaled(24);
        this.drawTexturedModalRect(var5 + 107, var6 + 34, 176, 0, var7 + 1, 16);
        var7 = this.furnace.cond.getChargeScaled(48);
        this.drawTexturedModalRect(var5 + 19, var6 + 69 - var7, 176, 65 - var7, 5, var7);
        var7 = this.furnace.cond.getFlowScaled(48);
        this.drawTexturedModalRect(var5 + 26, var6 + 69 - var7, 176, 65 - var7, 5, var7);

        if (this.furnace.cond.Charge > 600)
        {
            this.drawTexturedModalRect(var5 + 20, var6 + 13, 181, 17, 3, 6);
        }

        if (this.furnace.cond.Flow == -1)
        {
            this.drawTexturedModalRect(var5 + 27, var6 + 13, 184, 17, 3, 6);
        }
    }
}
