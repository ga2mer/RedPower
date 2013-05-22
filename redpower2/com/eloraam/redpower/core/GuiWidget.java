package com.eloraam.redpower.core;

import net.minecraft.client.gui.Gui;

public class GuiWidget extends Gui
{
    public int width;
    public int height;
    public int top;
    public int left;

    public GuiWidget(int var1, int var2, int var3, int var4)
    {
        this.left = var1;
        this.top = var2;
        this.width = var3;
        this.height = var4;
    }

    protected void drawRelRect(int var1, int var2, int var3, int var4, int var5)
    {
        drawRect(var1, var2, var1 + var3, var2 + var4, var5 | 61440);
    }
}
