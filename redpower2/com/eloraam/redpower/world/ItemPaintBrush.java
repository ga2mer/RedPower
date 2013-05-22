package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IPaintable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemPaintBrush extends Item
{
    int color;

    public ItemPaintBrush(int var1, int var2)
    {
        super(var1);
        this.color = var2;
        //this.setIconIndex(112 + var2);
        this.setMaxStackSize(1);
        this.setMaxDamage(15);
        this.setNoRepair();
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    private boolean itemUseShared(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7)
    {
        IPaintable var8 = (IPaintable)CoreLib.getTileEntity(var3, var4, var5, var6, IPaintable.class);

        if (var8 == null)
        {
            return false;
        }
        else
        {
            MovingObjectPosition var9 = CoreLib.retraceBlock(var3, var2, var4, var5, var6);

            if (var9 == null)
            {
                return false;
            }
            else if (!var8.tryPaint(var9.subHit, var9.sideHit, this.color + 1))
            {
                return false;
            }
            else
            {
                var1.damageItem(1, var2);

                if (var1.stackSize == 0)
                {
                    var1.stackSize = 1;
                    var1.itemID = RedPowerWorld.itemBrushDry.itemID;
                }

                return true;
            }
        }
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return CoreLib.isClient(var3) ? false : (var2.isSneaking() ? false : this.itemUseShared(var1, var2, var3, var4, var5, var6, var7));
    }

    public boolean onItemUseFirst(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return CoreLib.isClient(var3) ? false : (!var2.isSneaking() ? false : this.itemUseShared(var1, var2, var3, var4, var5, var6, var7));
    }

    public String getTextureFile()
    {
        return "/eloraam/base/items1.png";
    }
}
