package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerCore;
import com.eloraam.redpower.core.CoreProxyClient$RenderHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CoreProxyClient extends CoreProxy
{

    public void setupRenderers()
    {
        RedPowerCore.customBlockModel = RenderingRegistry.getNextAvailableRenderId();
        RedPowerCore.nullBlockModel = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RedPowerCore.customBlockModel, new CoreProxyClient$RenderHandler());
        MinecraftForge.EVENT_BUS.register(new RenderHighlight());
    }

    protected void pxySendPacketToServer(Packet var1)
    {
        NetClientHandler var2 = Minecraft.getMinecraft().thePlayer.sendQueue;
        var2.addToSendQueue(var1);
    }

    public void processPacket211(Packet211TileDesc var1, NetHandler var2)
    {
        if (var2 instanceof NetClientHandler)
        {
            NetClientHandler var3 = (NetClientHandler)var2;
            World var4 = var3.getPlayer().worldObj;

            if (var4.blockExists(var1.xCoord, var1.yCoord, var1.zCoord))
            {
                TileEntity var5 = var4.getBlockTileEntity(var1.xCoord, var1.yCoord, var1.zCoord);

                if (var5 instanceof IHandlePackets)
                {
                    ((IHandlePackets)var5).handlePacket(var1);
                    return;
                }
            }
        }
        else
        {
            super.processPacket211(var1, var2);
        }
    }

    public void processPacket212(Packet212GuiEvent var1, NetHandler var2)
    {
        if (var2 instanceof NetClientHandler)
        {
            NetClientHandler var3 = (NetClientHandler)var2;
            EntityPlayer var4 = var3.getPlayer();

            if (var4.openContainer != null && var4.openContainer.windowId == var1.windowId)
            {
                if (var4.openContainer instanceof IHandleGuiEvent)
                {
                    IHandleGuiEvent var5 = (IHandleGuiEvent)var4.openContainer;
                    var5.handleGuiEvent(var1);
                }
            }
        }
        else
        {
            super.processPacket212(var1, var2);
        }
    }
}
