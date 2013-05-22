package com.eloraam.redpower.base;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiAlloyFurnace extends GuiContainer
{
    TileAlloyFurnace furnace;

    public GuiAlloyFurnace(InventoryPlayer var1, TileAlloyFurnace var2)
    {
        super(new ContainerAlloyFurnace(var1, var2));
        this.furnace = var2;
    }

    public GuiAlloyFurnace(Container var1)
    {
        super(var1);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Alloy Furnace", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/base/afurnacegui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/base/afurnacegui.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7;

        if (this.furnace.burntime > 0)
        {
            var7 = this.furnace.getBurnScaled(12);
            this.drawTexturedModalRect(var5 + 17, var6 + 25 + 12 - var7, 176, 12 - var7, 14, var7 + 2);
        }

        var7 = this.furnace.getCookScaled(24);
        this.drawTexturedModalRect(var5 + 107, var6 + 34, 176, 14, var7 + 1, 16);
    }
}
