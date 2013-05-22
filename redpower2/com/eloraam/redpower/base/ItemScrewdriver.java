package com.eloraam.redpower.base;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRotatable;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemScrewdriver extends Item
{
    public ItemScrewdriver(int var1)
    {
        super(var1);
        this.setMaxDamage(200);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3)
    {
        var1.damageItem(8, var3);
        return true;
    }

    public boolean onItemUseFirst(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        if (CoreLib.isClient(var3))
        {
            return false;
        }
        else
        {
            boolean var11 = false;

            if (var2 != null && var2.isSneaking())
            {
                var11 = true;
            }

            int var12 = var3.getBlockId(var4, var5, var6);
            int var13 = var3.getBlockMetadata(var4, var5, var6);

            if (var12 != Block.redstoneRepeaterIdle.blockID && var12 != Block.redstoneRepeaterActive.blockID)
            {
                if (var12 == Block.dispenser.blockID)
                {
                    var13 = var13 & 3 ^ var13 >> 2;
                    var13 += 2;
                    var3.setBlock(var4, var5, var6, var13);
                    var1.damageItem(1, var2);
                    return true;
                }
                else if (var12 != Block.pistonBase.blockID && var12 != Block.pistonStickyBase.blockID)
                {
                    IRotatable var14 = (IRotatable)CoreLib.getTileEntity(var3, var4, var5, var6, IRotatable.class);

                    if (var14 == null)
                    {
                        return false;
                    }
                    else
                    {
                        MovingObjectPosition var15 = CoreLib.retraceBlock(var3, var2, var4, var5, var6);

                        if (var15 == null)
                        {
                            return false;
                        }
                        else
                        {
                            int var16 = var14.getPartMaxRotation(var15.subHit, var11);

                            if (var16 == 0)
                            {
                                return false;
                            }
                            else
                            {
                                int var17 = var14.getPartRotation(var15.subHit, var11);
                                ++var17;

                                if (var17 > var16)
                                {
                                    var17 = 0;
                                }

                                var14.setPartRotation(var15.subHit, var11, var17);
                                var1.damageItem(1, var2);
                                return true;
                            }
                        }
                    }
                }
                else
                {
                    ++var13;

                    if (var13 > 5)
                    {
                        var13 = 0;
                    }

                    var3.setBlock(var4, var5, var6, var13);
                    var1.damageItem(1, var2);
                    return true;
                }
            }
            else
            {
                var3.setBlock(var4, var5, var6, var13 & 12 | var13 + 1 & 3);
                var1.damageItem(1, var2);
                return true;
            }
        }
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity var1)
    {
        return 6;
    }

    public String getTextureFile()
    {
        return "/eloraam/base/items1.png";
    }
}
