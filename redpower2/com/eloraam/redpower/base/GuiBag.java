package com.eloraam.redpower.base;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class GuiBag extends GuiContainer
{
    public GuiBag(InventoryPlayer var1, IInventory var2)
    {
        super(new ContainerBag(var1, var2, (ItemStack)null));
        this.ySize = 167;
    }

    public GuiBag(Container var1)
    {
        super(var1);
        this.ySize = 167;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Canvas Bag", 8, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 94 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/base/baggui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/base/baggui.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
    }
}
