package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.Packet212GuiEvent;
import com.eloraam.redpower.logic.TileLogicStorage$LogicStorageCounter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiCounter extends GuiContainer
{
    private TileLogicStorage tileLogic;
    private GuiButton[] buttons = new GuiButton[18];

    public GuiCounter(InventoryPlayer var1, TileLogicStorage var2)
    {
        super(new ContainerCounter(var1, var2));
        this.xSize = 228;
        this.ySize = 117;
        this.tileLogic = var2;
    }

    public GuiCounter(Container var1)
    {
        super(var1);
        this.xSize = 228;
        this.ySize = 117;
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
        this.field_94077_p.add(this.buttons[0] = new GuiButton(1, var2 + 10, var3 + 20, var1 / 6, 20, "-25"));
        this.field_94077_p.add(this.buttons[1] = new GuiButton(2, var2 + 10 + var1 / 6, var3 + 20, var1 / 6, 20, "-5"));
        this.field_94077_p.add(this.buttons[2] = new GuiButton(3, var2 + 10 + var1 * 2 / 6, var3 + 20, var1 / 6, 20, "-1"));
        this.field_94077_p.add(this.buttons[3] = new GuiButton(4, var2 + 10 + var1 * 3 / 6, var3 + 20, var1 / 6, 20, "+1"));
        this.field_94077_p.add(this.buttons[4] = new GuiButton(5, var2 + 10 + var1 * 4 / 6, var3 + 20, var1 / 6, 20, "+5"));
        this.field_94077_p.add(this.buttons[5] = new GuiButton(6, var2 + 10 + var1 * 5 / 6, var3 + 20, var1 / 6, 20, "+25"));
        this.field_94077_p.add(this.buttons[6] = new GuiButton(7, var2 + 10, var3 + 55, var1 / 6, 20, "-25"));
        this.field_94077_p.add(this.buttons[7] = new GuiButton(8, var2 + 10 + var1 / 6, var3 + 55, var1 / 6, 20, "-5"));
        this.field_94077_p.add(this.buttons[8] = new GuiButton(9, var2 + 10 + var1 * 2 / 6, var3 + 55, var1 / 6, 20, "-1"));
        this.field_94077_p.add(this.buttons[9] = new GuiButton(10, var2 + 10 + var1 * 3 / 6, var3 + 55, var1 / 6, 20, "+1"));
        this.field_94077_p.add(this.buttons[10] = new GuiButton(11, var2 + 10 + var1 * 4 / 6, var3 + 55, var1 / 6, 20, "+5"));
        this.field_94077_p.add(this.buttons[11] = new GuiButton(12, var2 + 10 + var1 * 5 / 6, var3 + 55, var1 / 6, 20, "+25"));
        this.field_94077_p.add(this.buttons[12] = new GuiButton(13, var2 + 10, var3 + 90, var1 / 6, 20, "-25"));
        this.field_94077_p.add(this.buttons[13] = new GuiButton(14, var2 + 10 + var1 / 6, var3 + 90, var1 / 6, 20, "-5"));
        this.field_94077_p.add(this.buttons[14] = new GuiButton(15, var2 + 10 + var1 * 2 / 6, var3 + 90, var1 / 6, 20, "-1"));
        this.field_94077_p.add(this.buttons[15] = new GuiButton(16, var2 + 10 + var1 * 3 / 6, var3 + 90, var1 / 6, 20, "+1"));
        this.field_94077_p.add(this.buttons[16] = new GuiButton(17, var2 + 10 + var1 * 4 / 6, var3 + 90, var1 / 6, 20, "+5"));
        this.field_94077_p.add(this.buttons[17] = new GuiButton(18, var2 + 10 + var1 * 5 / 6, var3 + 90, var1 / 6, 20, "+25"));
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
        TileLogicStorage$LogicStorageCounter var6 = (TileLogicStorage$LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);
        int var7 = this.mc.renderEngine.getTexture("/eloraam/logic/countergui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/eloraam/logic/countergui.png");
        int var8 = (this.width - this.xSize) / 2;
        int var9 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var8, var9, 0, 0, this.xSize, this.ySize);
        String var5 = String.format("Maximum Count: %d", new Object[] {Integer.valueOf(var6.CountMax)});
        this.drawCenteredString(var4, var5, this.width / 2, var9 + 10, -1);
        var5 = String.format("Increment: %d", new Object[] {Integer.valueOf(var6.Inc)});
        this.drawCenteredString(var4, var5, this.width / 2, var9 + 45, -1);
        var5 = String.format("Decrement: %d", new Object[] {Integer.valueOf(var6.Dec)});
        this.drawCenteredString(var4, var5, this.width / 2, var9 + 80, -1);
    }

    public void changeCountMax(int var1)
    {
        TileLogicStorage$LogicStorageCounter var2 = (TileLogicStorage$LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);
        var2.CountMax += var1;

        if (var2.CountMax < 1)
        {
            var2.CountMax = 1;
        }

        if (!this.mc.theWorld.isRemote)
        {
            this.tileLogic.updateBlock();
        }
        else
        {
            Packet212GuiEvent var3 = new Packet212GuiEvent();
            var3.eventId = 1;
            var3.windowId = this.inventorySlots.windowId;
            var3.addUVLC((long)var2.CountMax);
            var3.encode();
            CoreProxy.sendPacketToServer(var3);
        }
    }

    public void changeInc(int var1)
    {
        TileLogicStorage$LogicStorageCounter var2 = (TileLogicStorage$LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);
        var2.Inc += var1;

        if (var2.Inc < 1)
        {
            var2.Inc = 1;
        }

        if (!this.mc.theWorld.isRemote)
        {
            this.tileLogic.updateBlock();
        }
        else
        {
            Packet212GuiEvent var3 = new Packet212GuiEvent();
            var3.eventId = 2;
            var3.windowId = this.inventorySlots.windowId;
            var3.addUVLC((long)var2.Inc);
            var3.encode();
            CoreProxy.sendPacketToServer(var3);
        }
    }

    public void changeDec(int var1)
    {
        TileLogicStorage$LogicStorageCounter var2 = (TileLogicStorage$LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);
        var2.Dec += var1;

        if (var2.Dec < 1)
        {
            var2.Dec = 1;
        }

        if (!this.mc.theWorld.isRemote)
        {
            this.tileLogic.updateBlock();
        }
        else
        {
            Packet212GuiEvent var3 = new Packet212GuiEvent();
            var3.eventId = 3;
            var3.windowId = this.inventorySlots.windowId;
            var3.addUVLC((long)var2.Dec);
            var3.encode();
            CoreProxy.sendPacketToServer(var3);
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
                    this.changeCountMax(-25);
                    break;

                case 2:
                    this.changeCountMax(-5);
                    break;

                case 3:
                    this.changeCountMax(-1);
                    break;

                case 4:
                    this.changeCountMax(1);
                    break;

                case 5:
                    this.changeCountMax(5);
                    break;

                case 6:
                    this.changeCountMax(25);
                    break;

                case 7:
                    this.changeInc(-25);
                    break;

                case 8:
                    this.changeInc(-5);
                    break;

                case 9:
                    this.changeInc(-1);
                    break;

                case 10:
                    this.changeInc(1);
                    break;

                case 11:
                    this.changeInc(5);
                    break;

                case 12:
                    this.changeInc(25);
                    break;

                case 13:
                    this.changeDec(-25);
                    break;

                case 14:
                    this.changeDec(-5);
                    break;

                case 15:
                    this.changeDec(-1);
                    break;

                case 16:
                    this.changeDec(1);
                    break;

                case 17:
                    this.changeDec(5);
                    break;

                case 18:
                    this.changeDec(25);
            }
        }
    }
}
