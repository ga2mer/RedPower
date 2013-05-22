package com.eloraam.redpower.base;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAdvBench extends GuiContainer
{
    TileAdvBench bench;

    public GuiAdvBench(InventoryPlayer var1, TileAdvBench var2)
    {
        super(new ContainerAdvBench(var1, var2));
        this.bench = var2;
        this.ySize = 222;
    }

    public GuiAdvBench(Container var1)
    {
        super(var1);
        this.ySize = 222;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        this.fontRenderer.drawString("Project Table", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        int var4 = this.mc.renderEngine.getTexture("/eloraam/base/advbench.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/base/advbench.png");
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        ItemStack var7 = this.inventorySlots.getSlot(9).getStack();
        ItemStack var8 = this.inventorySlots.getSlot(10).getStack();

        if (var7 != null && var8 != null && var7.itemID == RedPowerBase.itemPlanBlank.itemID)
        {
            this.drawTexturedModalRect(var5 + 18, var6 + 55, 176, 0, 14, 14);
        }

        if (var7 != null && var7.itemID == RedPowerBase.itemPlanFull.itemID)
        {
            ContainerAdvBench var9 = (ContainerAdvBench)this.inventorySlots;
            ItemStack[] var10 = ContainerAdvBench.getShadowItems(var7);
            RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            int var11;
            Slot var12;
            int var13;
            int var14;

            for (var11 = 0; var11 < 9; ++var11)
            {
                if (var10[var11] != null)
                {
                    var12 = this.inventorySlots.getSlot(var11);

                    if (var12.getStack() == null)
                    {
                        var13 = var12.xDisplayPosition + var5;
                        var14 = var12.yDisplayPosition + var6;
                        itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, var10[var11], var13, var14);
                        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var10[var11], var13, var14);
                    }
                }
            }

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            this.mc.renderEngine.bindTexture("/eloraam/base/advbench.png");

            for (var11 = 0; var11 < 9; ++var11)
            {
                if (var10[var11] != null)
                {
                    var12 = this.inventorySlots.getSlot(var11);

                    if (var12.getStack() == null)
                    {
                        var13 = var12.xDisplayPosition;
                        var14 = var12.yDisplayPosition;

                        if ((var9.satisfyMask & 1 << var11) > 0)
                        {
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                        }
                        else
                        {
                            GL11.glColor4f(1.0F, 0.1F, 0.1F, 0.6F);
                        }

                        this.drawTexturedModalRect(var5 + var13, var6 + var14, var13, var14, 16, 16);
                    }
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        int var4 = var1 - (this.width - this.xSize) / 2;
        int var5 = var2 - (this.height - this.ySize) / 2;

        if (var4 >= 18 && var5 >= 55 && var4 <= 32 && var5 <= 69)
        {
            ItemStack var6 = this.inventorySlots.getSlot(9).getStack();
            ItemStack var7 = this.inventorySlots.getSlot(10).getStack();

            if (var6 == null || var7 == null || var6.itemID != RedPowerBase.itemPlanBlank.itemID)
            {
                return;
            }

            Packet212GuiEvent var8 = new Packet212GuiEvent();
            var8.eventId = 1;
            var8.windowId = this.inventorySlots.windowId;
            var8.encode();
            CoreProxy.sendPacketToServer(var8);
        }

        super.mouseClicked(var1, var2, var3);
    }
}
