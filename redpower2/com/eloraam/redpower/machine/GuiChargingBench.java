package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiChargingBench extends GuiContainer
{
    TileChargingBench tileCB;

    public GuiChargingBench(InventoryPlayer var1, TileChargingBench var2)
    {
        super(new ContainerChargingBench(var1, var2));
        this.tileCB = var2;
        this.ySize = 186;
    }

    public GuiChargingBench(Container var1)
    {
        super(var1);
        this.ySize = 186;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Charging Bench", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/machine/charging.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/machine/charging.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var8 = this.tileCB.getMaxStorage();
        int var7 = this.tileCB.getChargeScaled(48);
        this.drawTexturedModalRect(var5 + 21, var6 + 78 - var7, 176, 48 - var7, 5, var7);

        if (this.tileCB.cond.Charge > 600)
        {
            this.drawTexturedModalRect(var5 + 22, var6 + 22, 197, 8, 3, 6);
        }

        if (this.tileCB.cond.Charge > 600 && this.tileCB.Storage < var8)
        {
            this.drawTexturedModalRect(var5 + 32, var6 + 51, 197, 0, 10, 8);
        }

        var7 = this.tileCB.getStorageScaled(48);
        this.drawTexturedModalRect(var5 + 48, var6 + 78 - var7, 181, 48 - var7, 16, var7);

        if (this.tileCB.Storage == var8)
        {
            this.drawTexturedModalRect(var5 + 53, var6 + 22, 200, 8, 6, 6);
        }
    }
}
