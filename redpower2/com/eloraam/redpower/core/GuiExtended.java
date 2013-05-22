package com.eloraam.redpower.core;

import java.util.ArrayList;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiExtended extends GuiContainer
{
    ArrayList widgetList = new ArrayList();

    public GuiExtended(Container var1)
    {
        super(var1);
    }
}
