package com.eloraam.redpower.base;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRedbusConnectable;
import com.eloraam.redpower.core.IRedbusConnectable$Dummy;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.world.World;

public class BaseProxy implements IGuiHandler
{
    public void registerRenderers() {}

    public Object getClientGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new GuiAlloyFurnace(var2.inventory, (TileAlloyFurnace)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileAlloyFurnace.class));

            case 2:
                return new GuiAdvBench(var2.inventory, (TileAdvBench)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileAdvBench.class));

            case 3:
                return new GuiBusId(var2.inventory, new IRedbusConnectable$Dummy());

            case 4:
                return new GuiBag(var2.inventory, new InventoryBasic("", false, 27));

            default:
                return null;
        }
    }

    public Object getServerGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new ContainerAlloyFurnace(var2.inventory, (TileAlloyFurnace)CoreLib.getTileEntity(var3, var4, var5, var6, TileAlloyFurnace.class));

            case 2:
                return new ContainerAdvBench(var2.inventory, (TileAdvBench)CoreLib.getTileEntity(var3, var4, var5, var6, TileAdvBench.class));

            case 3:
                return new ContainerBusId(var2.inventory, (IRedbusConnectable)CoreLib.getTileEntity(var3, var4, var5, var6, IRedbusConnectable.class));

            case 4:
                return new ContainerBag(var2.inventory, ItemBag.getBagInventory(var2.inventory.getCurrentItem()), var2.inventory.getCurrentItem());

            default:
                return null;
        }
    }
}
