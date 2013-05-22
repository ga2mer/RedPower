package com.eloraam.redpower.base;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.base.ItemBag$InventoryBag;
import com.eloraam.redpower.core.CoreLib;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBag extends Item
{
    public ItemBag(int var1)
    {
        super(var1);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        //this.setTextureFile("/eloraam/base/items1.png");
        this.setUnlocalizedName("rpBag");
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public static IInventory getBagInventory(ItemStack var0)
    {
        return !(var0.getItem() instanceof ItemBag) ? null : new ItemBag$InventoryBag(var0);
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack var1)
    {
        return 1;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (CoreLib.isClient(var2))
        {
            return var1;
        }
        else if (var3.isSneaking())
        {
            return var1;
        }
        else
        {
            var3.openGui(RedPowerBase.instance, 4, var2, 0, 0, 0);
            return var1;
        }
    }
}
