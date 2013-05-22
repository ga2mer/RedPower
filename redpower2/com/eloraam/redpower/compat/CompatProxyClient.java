package com.eloraam.redpower.compat;

import com.eloraam.redpower.RedPowerCompat;
import com.eloraam.redpower.core.RenderLib;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class CompatProxyClient extends CompatProxy
{
    public void registerRenderers()
    {
        RenderLib.setRenderer(RedPowerCompat.blockMachineCompat, 0, RenderBlueEngine.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlueEngine.class, new TileBlueEngineRenderer());
        MinecraftForgeClient.preloadTexture("/eloraam/compat/compat1.png");
    }
}
