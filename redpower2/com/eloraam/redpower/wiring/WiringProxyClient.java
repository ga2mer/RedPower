package com.eloraam.redpower.wiring;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.core.RenderLib;
import net.minecraftforge.client.MinecraftForgeClient;

public class WiringProxyClient extends WiringProxy
{
    public void registerRenderers()
    {
        RenderLib.setDefaultRenderer(RedPowerBase.blockMicro, 8, RenderRedwire.class);
        MinecraftForgeClient.preloadTexture("/eloraam/wiring/redpower1.png");
    }
}
