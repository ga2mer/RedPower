package com.eloraam.redpower.core;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IMicroPlacement
{
    boolean onPlaceMicro(ItemStack var1, EntityPlayer var2, World var3, WorldCoord var4, int var5);

    String getMicroName(int var1, int var2);

    void addCreativeItems(int var1, CreativeTabs var2, List var3);
}
