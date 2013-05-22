package com.eloraam.redpower.compat;

import com.eloraam.redpower.core.BlockExtended;
import com.eloraam.redpower.core.ItemExtended;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMachineCompat extends ItemExtended
{
    public ItemMachineCompat(int var1)
    {
        super(var1);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        int var11 = var3.getBlockId(var4, var5, var6);
        int var12 = this.getBlockID();

        if (var11 == Block.snow.blockID)
        {
            var7 = 1;
        }
        else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID)
        {
            switch (var7)
            {
                case 0:
                    --var5;
                    break;

                case 1:
                    ++var5;
                    break;

                case 2:
                    --var6;
                    break;

                case 3:
                    ++var6;
                    break;

                case 4:
                    --var4;
                    break;

                default:
                    ++var4;
            }
        }

        if (var1.stackSize == 0)
        {
            return false;
        }
        else if (!var2.canPlayerEdit(var4, var5, var6, var7, var1))
        {
            return false;
        }
        else if (var5 >= var3.getHeight() - 1)
        {
            return false;
        }
        else if (!var3.canPlaceEntityOnSide(var12, var4, var5, var6, false, var7, var2))
        {
            return false;
        }
        else
        {
            Block var13 = Block.blocksList[var12];

            if (var3.setBlockMetadataWithNotify(var4, var5, var6, var12, this.getMetadata(var1.getItemDamage())))
            {
                if (var3.getBlockId(var4, var5, var6) == var12)
                {
                    BlockExtended var14 = (BlockExtended)Block.blocksList[var12];
                    var14.onBlockPlacedUseful(var3, var4, var5, var6, var7, var2, var1);
                }

                var3.playSoundEffect((double)((float)var4 + 0.5F), (double)((float)var5 + 0.5F), (double)((float)var6 + 0.5F), var13.stepSound.getStepSound(), (var13.stepSound.getVolume() + 1.0F) / 2.0F, var13.stepSound.getPitch() * 0.8F);
                --var1.stackSize;
            }

            return true;
        }
    }
}
