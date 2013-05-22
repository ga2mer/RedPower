package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.CoreLib;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LogicProxy implements IGuiHandler
{
    public void registerRenderers() {}

    public Object getClientGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new GuiCounter(var2.inventory, (TileLogicStorage)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileLogicStorage.class));

            case 2:
                return new GuiTimer(var2.inventory, (TileLogicPointer)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileLogicPointer.class));

            default:
                return null;
        }
    }

    public Object getServerGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new ContainerCounter(var2.inventory, (TileLogicStorage)CoreLib.getTileEntity(var3, var4, var5, var6, TileLogicStorage.class));

            case 2:
                return new ContainerTimer(var2.inventory, (TileLogicPointer)CoreLib.getTileEntity(var3, var4, var5, var6, TileLogicPointer.class));

            default:
                return null;
        }
    }
}
