package com.eloraam.redpower.world;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.world.World;

public class WorldProxy implements IGuiHandler
{
    public void registerRenderers() {}

    public Object getClientGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new GuiSeedBag(var2.inventory, new InventoryBasic("", false,  9));

            default:
                return null;
        }
    }

    public Object getServerGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new ContainerSeedBag(var2.inventory, ItemSeedBag.getBagInventory(var2.inventory.getCurrentItem()), var2.inventory.getCurrentItem());

            default:
                return null;
        }
    }
}
