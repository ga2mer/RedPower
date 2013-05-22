package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.RenderLib;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class MachineProxyClient extends MachineProxy
{
    public void registerRenderers()
    {
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 0, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 1, RenderBreaker.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 2, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 3, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 4, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 5, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 6, RenderBatteryBox.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 7, RenderMotor.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 8, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 9, RenderWindTurbine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 10, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 11, RenderThermopile.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 12, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 13, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 14, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 15, RenderMachine.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine2, 0, RenderMachine2.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachine2, 1, RenderMachine2.class);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 1, RenderBlueFurnace.class);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 2, RenderBufferChest.class);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 4, RenderBlueFurnace.class);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 5, RenderChargingBench.class);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 7, RenderPipe.class);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 8, RenderTube.class);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 9, RenderRedstoneTube.class);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 10, RenderTube.class);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 11, RenderTube.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 0, RenderSolarPanel.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 1, RenderPump.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 2, RenderAccel.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 3, RenderGrate.class);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 4, RenderTransformer.class);
        RenderLib.setRenderer(RedPowerMachine.blockFrame, 0, RenderFrame.class);
        RenderLib.setRenderer(RedPowerMachine.blockFrame, 1, RenderFrameMoving.class);
        RenderLib.setRenderer(RedPowerMachine.blockFrame, 2, RenderFrameTube.class);
        RenderLib.setRenderer(RedPowerMachine.blockFrame, 3, RenderFrameRedstoneTube.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TileWindTurbine.class, new TileWindTurbineRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePipe.class, new TilePipeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePump.class, new TilePumpRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTube.class, new TileTubeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRedstoneTube.class, new TileTubeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRestrictTube.class, new TileTubeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMagTube.class, new TileTubeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAccel.class, new TileTubeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFrameMoving.class, new TileFrameRenderer());
        MinecraftForgeClient.preloadTexture("/eloraam/machine/machine1.png");
        MinecraftForgeClient.preloadTexture("/eloraam/machine/machine2.png");
        //TextureFXManager.instance().addAnimation(new TextureAnimFX(239, "/eloraam/machine/machine1.png", 2, new int[] {146, 147, 148, 149}));
        //TextureFXManager.instance().addAnimation(new TextureAnimFX(255, "/eloraam/machine/machine1.png", 2, new int[] {151, 152, 153, 154}));
        //MinecraftForgeClient.registerRenderContextHandler("/eloraam/machine/machine1.png", 1, new MachineProxyClient$1(this));
        MinecraftForgeClient.registerItemRenderer(RedPowerMachine.blockMachine.blockID, new ItemRenderMachine());
    }
}
