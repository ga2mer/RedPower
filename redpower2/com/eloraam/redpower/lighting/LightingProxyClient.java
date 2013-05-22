package com.eloraam.redpower.lighting;

import com.eloraam.redpower.RedPowerLighting;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.lighting.LightingProxyClient$1;
import net.minecraftforge.client.MinecraftForgeClient;

public class LightingProxyClient extends LightingProxy
{
    public void registerRenderers()
    {
        //MinecraftForgeClient.registerItemRenderer("/eloraam/lighting/lighting1.png", 1, new LightingProxyClient$1(this));
        RenderLib.setRenderer(RedPowerLighting.blockLampOff, RenderLamp.class);
        RenderLib.setRenderer(RedPowerLighting.blockLampOn, RenderLamp.class);
        RenderLib.setRenderer(RedPowerLighting.blockInvLampOff, RenderLamp.class);
        RenderLib.setRenderer(RedPowerLighting.blockInvLampOn, RenderLamp.class);
        RenderLib.setDefaultRenderer(RedPowerLighting.blockShapedLamp, 10, RenderShapedLamp.class);
        MinecraftForgeClient.preloadTexture("/eloraam/lighting/lighting1.png");
    }
}
