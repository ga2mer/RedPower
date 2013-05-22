package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.ItemExtended;
import com.eloraam.redpower.core.RedPowerLib;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLogic extends ItemExtended
{
    public ItemLogic(int var1)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public void placeNoise(World var1, int var2, int var3, int var4, int var5)
    {
        Block var6 = Block.blocksList[var5];
        var1.playSoundEffect((double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), "step.stone", (var6.stepSound.getVolume() + 1.0F) / 2.0F, var6.stepSound.getPitch() * 0.8F);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return var2.isSneaking() ? false : this.itemUseShared(var1, var2, var3, var4, var5, var6, var7);
    }

    public boolean onItemUseFirst(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return CoreLib.isClient(var3) ? false : (!var2.isSneaking() ? false : this.itemUseShared(var1, var2, var3, var4, var5, var6, var7));
    }

    protected boolean tryPlace(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, int var8, int var9)
    {
        int var10 = var1.getItemDamage();
        int var11 = var1.itemID;

        if (var3.setBlock(var4, var5, var6, var11))
        {
            return false;
        }
        if (var3.setBlock(var4, var5, var6, var10 >> 8))
        {
            return false;
        }
        else
        {
            TileLogic var12 = (TileLogic)CoreLib.getTileEntity(var3, var4, var5, var6, TileLogic.class);

            if (var12 == null)
            {
                return false;
            }
            else
            {
                var12.Rotation = var8 << 2 | var9;
                var12.initSubType(var10 & 255);
                return true;
            }
        }
    }

    protected boolean itemUseShared(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7)
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

            case 5:
                ++var4;
        }

        int var8 = var1.itemID;

        if (!var3.canPlaceEntityOnSide(var8, var4, var5, var6, false, var7, var2))
        {
            return false;
        }
        else if (!RedPowerLib.isSideNormal(var3, var4, var5, var6, var7 ^ 1))
        {
            return false;
        }
        else
        {
            int var9 = (int)Math.floor((double)(var2.rotationYaw / 90.0F + 0.5F));
            int var10 = (int)Math.floor((double)(var2.rotationPitch / 90.0F + 0.5F));
            var9 = var9 + 1 & 3;
            int var11 = var7 ^ 1;
            int var12;

            switch (var11)
            {
                case 0:
                    var12 = var9;
                    break;

                case 1:
                    var12 = var9 ^ (var9 & 1) << 1;
                    break;

                case 2:
                    var12 = (var9 & 1) > 0 ? (var10 > 0 ? 2 : 0) : 1 - var9 & 3;
                    break;

                case 3:
                    var12 = (var9 & 1) > 0 ? (var10 > 0 ? 2 : 0) : var9 - 1 & 3;
                    break;

                case 4:
                    var12 = (var9 & 1) == 0 ? (var10 > 0 ? 2 : 0) : var9 - 2 & 3;
                    break;

                case 5:
                    var12 = (var9 & 1) == 0 ? (var10 > 0 ? 2 : 0) : 2 - var9 & 3;
                    break;

                default:
                    var12 = 0;
            }

            if (!this.tryPlace(var1, var2, var3, var4, var5, var6, var7, var11, var12))
            {
                return true;
            }
            else
            {
                this.placeNoise(var3, var4, var5, var6, var8);
                --var1.stackSize;
                var3.markBlockForUpdate(var4, var5, var6);
                RedPowerLib.updateIndirectNeighbors(var3, var4, var5, var6, var8);
                return true;
            }
        }
    }
}
