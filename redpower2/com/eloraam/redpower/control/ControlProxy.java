package com.eloraam.redpower.control;

import com.eloraam.redpower.core.CoreLib;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ControlProxy implements IGuiHandler
{
    public void registerRenderers() {}

    public Object getClientGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new GuiDisplay(var2.inventory, (TileDisplay)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileDisplay.class));

            case 2:
                return new GuiCPU(var2.inventory, (TileCPU)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileCPU.class));

            default:
                return null;
        }
    }

    public Object getServerGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new ContainerDisplay(var2.inventory, (TileDisplay)CoreLib.getTileEntity(var3, var4, var5, var6, TileDisplay.class));

            case 2:
                return new ContainerCPU(var2.inventory, (TileCPU)CoreLib.getTileEntity(var3, var4, var5, var6, TileCPU.class));

            default:
                return null;
        }
    }
}
