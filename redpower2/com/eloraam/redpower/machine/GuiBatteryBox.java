package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiBatteryBox extends GuiContainer
{
    TileBatteryBox tileBB;

    public GuiBatteryBox(InventoryPlayer var1, TileBatteryBox var2)
    {
        super(new ContainerBatteryBox(var1, var2));
        this.tileBB = var2;
        this.ySize = 170;
    }

    public GuiBatteryBox(Container var1)
    {
        super(var1);
        this.ySize = 170;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Battery Box", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/machine/batbox.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/batbox.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var8 = this.tileBB.getMaxStorage();
        int var7 = this.tileBB.getChargeScaled(48);
        this.drawTexturedModalRect(var5 + 71, var6 + 73 - var7, 176, 48 - var7, 5, var7);

        if (this.tileBB.Charge > 600)
        {
            this.drawTexturedModalRect(var5 + 72, var6 + 17, 197, 16, 3, 6);
        }

        if (this.tileBB.Charge > 900 && this.tileBB.Storage < var8)
        {
            this.drawTexturedModalRect(var5 + 82, var6 + 37, 197, 0, 10, 8);
        }

        if (this.tileBB.Charge < 800 && this.tileBB.Storage > 0)
        {
            this.drawTexturedModalRect(var5 + 82, var6 + 55, 197, 8, 10, 8);
        }

        var7 = this.tileBB.getStorageScaled(48);
        this.drawTexturedModalRect(var5 + 98, var6 + 73 - var7, 181, 48 - var7, 16, var7);

        if (this.tileBB.Storage == var8)
        {
            this.drawTexturedModalRect(var5 + 103, var6 + 17, 200, 16, 6, 6);
        }
    }
}
