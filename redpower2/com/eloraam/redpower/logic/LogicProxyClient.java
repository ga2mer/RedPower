package com.eloraam.redpower.logic;

import com.eloraam.redpower.RedPowerLogic;
import com.eloraam.redpower.core.RenderLib;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class LogicProxyClient extends LogicProxy
{
    public void registerRenderers()
    {
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 0, RenderLogicPointer.class);
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 1, RenderLogicSimple.class);
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 2, RenderLogicArray.class);
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 3, RenderLogicStorage.class);
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 4, RenderLogicAdv.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TileLogicPointer.class, new TilePointerRenderer());
        MinecraftForgeClient.preloadTexture("/eloraam/logic/logic1.png");
        MinecraftForgeClient.preloadTexture("/eloraam/logic/logic2.png");
        MinecraftForgeClient.preloadTexture("/eloraam/logic/array1.png");
        MinecraftForgeClient.preloadTexture("/eloraam/logic/sensor1.png");
    }
}
