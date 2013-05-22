package com.eloraam.redpower.core;

import cpw.mods.fml.common.SidedProxy;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.Item;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CoreProxy
{
    @SidedProxy(
            clientSide = "com.eloraam.redpower.core.CoreProxyClient",
            serverSide = "com.eloraam.redpower.core.CoreProxy"
    )
    public static CoreProxy instance;

    public static void sendPacketToServer(Packet var0)
    {
        instance.pxySendPacketToServer(var0);
    }

    public int getItemIcon(Item var1, int var2)
    {
        return 0;
    }

    public static void sendPacketToPosition(World var0, Packet var1, int var2, int var3)
    {
        if (!var0.isRemote)
        {
            WorldServer var4 = (WorldServer)var0;
            PlayerManager var5 = var4.getPlayerManager();
            List var6 = var0.playerEntities;

            for (int var7 = 0; var7 < var6.size(); ++var7)
            {
                EntityPlayerMP var8 = (EntityPlayerMP)var6.get(var7);

                if (var5.isPlayerWatchingChunk(var8, var2 >> 4, var3 >> 4))
                {
                    var8.playerNetServerHandler.sendPacketToPlayer(var1);
                }
            }
        }
    }

    public static void sendPacketToCrafting(ICrafting var0, Packet var1)
    {
        if (var0 instanceof EntityPlayerMP)
        {
            EntityPlayerMP var2 = (EntityPlayerMP)var0;
            var2.playerNetServerHandler.sendPacketToPlayer(var1);
        }
    }

    public void setupRenderers() {}

    protected void pxySendPacketToServer(Packet var1) {}

    public void processPacket211(Packet211TileDesc var1, NetHandler var2)
    {
        if (var2 instanceof NetServerHandler)
        {
            NetServerHandler var3 = (NetServerHandler)var2;
            EntityPlayerMP var4 = var3.getPlayer();
            World var5 = var4.worldObj;

            if (var5.blockExists(var1.xCoord, var1.yCoord, var1.zCoord))
            {
                TileEntity var6 = var5.getBlockTileEntity(var1.xCoord, var1.yCoord, var1.zCoord);

                if (var6 instanceof IHandlePackets)
                {
                    ((IHandlePackets)var6).handlePacket(var1);
                    return;
                }
            }
        }
    }

    public void processPacket212(Packet212GuiEvent var1, NetHandler var2)
    {
        if (var2 instanceof NetServerHandler)
        {
            NetServerHandler var3 = (NetServerHandler)var2;
            EntityPlayerMP var4 = var3.getPlayer();

            if (var4.openContainer != null && var4.openContainer.windowId == var1.windowId)
            {
                if (var4.openContainer instanceof IHandleGuiEvent)
                {
                    IHandleGuiEvent var5 = (IHandleGuiEvent)var4.openContainer;
                    var5.handleGuiEvent(var1);
                }
            }
        }
    }
}
