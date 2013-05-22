package com.eloraam.redpower.world;

import net.minecraftforge.client.MinecraftForgeClient;

public class WorldProxyClient extends WorldProxy
{
    public void registerRenderers()
    {
        MinecraftForgeClient.preloadTexture("/eloraam/world/world1.png");
        MinecraftForgeClient.preloadTexture("/eloraam/world/worlditems1.png");
    }
}
