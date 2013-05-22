package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiTimer extends GuiContainer
{
    private TileLogicPointer tileLogic;
    private GuiButton[] buttons = new GuiButton[6];

    public GuiTimer(InventoryPlayer var1, TileLogicPointer var2)
    {
        super(new ContainerTimer(var1, var2));
        this.xSize = 228;
        this.ySize = 82;
        this.tileLogic = var2;
    }

    public GuiTimer(Container var1)
    {
        super(var1);
        this.xSize = 228;
        this.ySize = 82;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        int var1 = this.xSize - 20;
        int var2 = (this.width - this.xSize) / 2;
        int var3 = (this.height - this.ySize) / 2;
        this.field_94077_p.add(this.buttons[0] = new GuiButton(1, var2 + 10, var3 + 50, var1 / 6, 20, "-10s"));
        this.field_94077_p.add(this.buttons[1] = new GuiButton(2, var2 + 10 + var1 / 6, var3 + 50, var1 / 6, 20, "-1s"));
        this.field_94077_p.add(this.buttons[2] = new GuiButton(3, var2 + 10 + var1 * 2 / 6, var3 + 50, var1 / 6, 20, "-50ms"));
        this.field_94077_p.add(this.buttons[3] = new GuiButton(4, var2 + 10 + var1 * 3 / 6, var3 + 50, var1 / 6, 20, "+50ms"));
        this.field_94077_p.add(this.buttons[4] = new GuiButton(5, var2 + 10 + var1 * 4 / 6, var3 + 50, var1 / 6, 20, "+1s"));
        this.field_94077_p.add(this.buttons[5] = new GuiButton(6, var2 + 10 + var1 * 5 / 6, var3 + 50, var1 / 6, 20, "+10s"));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2) {}

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        FontRenderer var4 = this.mc.fontRenderer;
        int var6 = this.mc.renderEngine.getTexture("/eloraam/logic/timersgui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/logic/timersgui.png");
        int var7 = (this.width - this.xSize) / 2;
        int var8 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var7, var8, 0, 0, this.xSize, this.ySize);
        String var5 = String.format("Timer Interval: %.3fs", new Object[] {Double.valueOf((double)this.tileLogic.getInterval() / 20.0D)});
        this.drawCenteredString(var4, var5, this.width / 2, var8 + 10, -1);
    }

    public void changeInterval(int var1)
    {
        long var2 = this.tileLogic.getInterval() + (long)var1;

        if (var2 < 4L)
        {
            var2 = 4L;
        }

        this.tileLogic.setInterval(var2);

        if (!this.mc.theWorld.isRemote)
        {
            this.tileLogic.updateBlock();
        }
        else
        {
            Packet212GuiEvent var4 = new Packet212GuiEvent();
            var4.eventId = 1;
            var4.windowId = this.inventorySlots.windowId;
            var4.addUVLC(var2);
            var4.encode();
            CoreProxy.sendPacketToServer(var4);
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.enabled)
        {
            switch (var1.id)
            {
                case 1:
                    this.changeInterval(-200);
                    break;

                case 2:
                    this.changeInterval(-20);
                    break;

                case 3:
                    this.changeInterval(-1);
                    break;

                case 4:
                    this.changeInterval(1);
                    break;

                case 5:
                    this.changeInterval(20);
                    break;

                case 6:
                    this.changeInterval(200);
            }
        }
    }
}
