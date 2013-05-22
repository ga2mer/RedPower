package com.eloraam.redpower.base;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.core.RenderLib;
import net.minecraftforge.client.MinecraftForgeClient;

public class BaseProxyClient extends BaseProxy
{
    public void registerRenderers()
    {
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 0, RenderAlloyFurnace.class);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 3, RenderAdvBench.class);
        MinecraftForgeClient.preloadTexture("/eloraam/base/base1.png");
    }
}
