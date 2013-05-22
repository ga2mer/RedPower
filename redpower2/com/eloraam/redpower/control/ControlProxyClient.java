package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerControl;
import com.eloraam.redpower.core.RenderLib;
import net.minecraftforge.client.MinecraftForgeClient;

public class ControlProxyClient extends ControlProxy
{
    public void registerRenderers()
    {
        RenderLib.setRenderer(RedPowerControl.blockBackplane, 0, RenderBackplane.class);
        RenderLib.setRenderer(RedPowerControl.blockBackplane, 1, RenderBackplane.class);
        RenderLib.setRenderer(RedPowerControl.blockPeripheral, 0, RenderDisplay.class);
        RenderLib.setRenderer(RedPowerControl.blockPeripheral, 1, RenderCPU.class);
        RenderLib.setRenderer(RedPowerControl.blockPeripheral, 2, RenderDiskDrive.class);
        RenderLib.setRenderer(RedPowerControl.blockFlatPeripheral, 0, RenderIOExpander.class);
        MinecraftForgeClient.preloadTexture("/eloraam/control/control1.png");
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 12, RenderRibbon.class);
    }
}
