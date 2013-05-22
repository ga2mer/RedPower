package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.ItemPartialCraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPaintCan extends ItemPartialCraft
{
    int color;

    public ItemPaintCan(int var1, int var2)
    {
        super(var1);
        this.color = var2;
        //this.setIconIndex(96 + var2);
        this.setMaxDamage(15);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        for (int var4 = 0; var4 < 9; ++var4)
        {
            ItemStack var5 = var3.inventory.getStackInSlot(var4);

            if (var5 != null && var5.itemID == RedPowerWorld.itemBrushDry.itemID && var5.stackSize == 1)
            {
                var3.inventory.setInventorySlotContents(var4, new ItemStack(RedPowerWorld.itemBrushPaint[this.color]));
                var1.damageItem(1, var3);

                if (var1.stackSize == 0)
                {
                    return new ItemStack(RedPowerWorld.itemPaintCanEmpty);
                }

                return var1;
            }
        }

        return var1;
    }

    public String getTextureFile()
    {
        return "/eloraam/base/items1.png";
    }
}
