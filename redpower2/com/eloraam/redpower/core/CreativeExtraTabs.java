package com.eloraam.redpower.core;

import com.eloraam.redpower.core.CreativeExtraTabs$1;
import com.eloraam.redpower.core.CreativeExtraTabs$2;
import com.eloraam.redpower.core.CreativeExtraTabs$3;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeExtraTabs
{
    public static CreativeTabs tabWires = new CreativeExtraTabs$1(CreativeTabs.getNextID(), "RPWires");
    public static CreativeTabs tabMicros = new CreativeExtraTabs$2(CreativeTabs.getNextID(), "RPMicroblocks");
    public static CreativeTabs tabMachine = new CreativeExtraTabs$3(CreativeTabs.getNextID(), "RPMachines");
}
