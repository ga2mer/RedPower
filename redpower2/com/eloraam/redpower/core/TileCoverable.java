package com.eloraam.redpower.core;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public abstract class TileCoverable extends TileMultipart implements ICoverable, IMultipart
{
    public abstract boolean canAddCover(int var1, int var2);

    public abstract boolean tryAddCover(int var1, int var2);

    public abstract int tryRemoveCover(int var1);

    public abstract int getCover(int var1);

    public abstract int getCoverMask();

    public boolean isSideSolid(int var1)
    {
        int var2 = this.getCoverMask();
        return (var2 & 1 << var1) > 0;
    }

    public boolean isSideNormal(int var1)
    {
        int var2 = this.getCoverMask();

        if ((var2 & 1 << var1) == 0)
        {
            return false;
        }
        else
        {
            int var3 = this.getCover(var1);
            int var4 = var3 >> 8;
            return !CoverLib.isTransparent(var3 & 255) && (var4 < 3 || var4 >= 6 && var4 <= 9);
        }
    }

    public void addCoverableHarvestContents(ArrayList var1)
    {
        if (CoverLib.blockCoverPlate != null)
        {
            for (int var2 = 0; var2 < 29; ++var2)
            {
                int var3 = this.getCover(var2);

                if (var3 >= 0)
                {
                    var1.add(CoverLib.convertCoverPlate(var2, var3));
                }
            }
        }
    }

    public void addHarvestContents(ArrayList var1)
    {
        this.addCoverableHarvestContents(var1);
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        int var3 = this.tryRemoveCover(var2);

        if (var3 >= 0)
        {
            this.dropCover(var2, var3);

            if (this.blockEmpty())
            {
                this.deleteBlock();
            }
        }
    }

    public float getPartStrength(EntityPlayer var1, int var2)
    {
        int var4 = this.getCover(var2);

        if (var4 < 0)
        {
            return 0.0F;
        }
        else
        {
            var4 &= 255;
            float var5 = CoverLib.getMiningHardness(var4);

            if (var5 < 0.0F)
            {
                return 0.0F;
            }
            else
            {
                ItemStack var6 = CoverLib.getItemStack(var4);
                Block var3 = Block.blocksList[var6.itemID];
                int var7 = var6.getItemDamage();
                return 0;
            }
        }
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        int var3 = this.getCover(var2);
        float var4 = CoverLib.getThickness(var2, var3);

        switch (var2)
        {
            case 0:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var4, 1.0F);
                break;

            case 1:
                var1.setBlockBounds(0.0F, 1.0F - var4, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 2:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var4);
                break;

            case 3:
                var1.setBlockBounds(0.0F, 0.0F, 1.0F - var4, 1.0F, 1.0F, 1.0F);
                break;

            case 4:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, var4, 1.0F, 1.0F);
                break;

            case 5:
                var1.setBlockBounds(1.0F - var4, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 6:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, var4, var4, var4);
                break;

            case 7:
                var1.setBlockBounds(0.0F, 0.0F, 1.0F - var4, var4, var4, 1.0F);
                break;

            case 8:
                var1.setBlockBounds(1.0F - var4, 0.0F, 0.0F, 1.0F, var4, var4);
                break;

            case 9:
                var1.setBlockBounds(1.0F - var4, 0.0F, 1.0F - var4, 1.0F, var4, 1.0F);
                break;

            case 10:
                var1.setBlockBounds(0.0F, 1.0F - var4, 0.0F, var4, 1.0F, var4);
                break;

            case 11:
                var1.setBlockBounds(0.0F, 1.0F - var4, 1.0F - var4, var4, 1.0F, 1.0F);
                break;

            case 12:
                var1.setBlockBounds(1.0F - var4, 1.0F - var4, 0.0F, 1.0F, 1.0F, var4);
                break;

            case 13:
                var1.setBlockBounds(1.0F - var4, 1.0F - var4, 1.0F - var4, 1.0F, 1.0F, 1.0F);
                break;

            case 14:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var4, var4);
                break;

            case 15:
                var1.setBlockBounds(0.0F, 0.0F, 1.0F - var4, 1.0F, var4, 1.0F);
                break;

            case 16:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, var4, var4, 1.0F);
                break;

            case 17:
                var1.setBlockBounds(1.0F - var4, 0.0F, 0.0F, 1.0F, var4, 1.0F);
                break;

            case 18:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, var4, 1.0F, var4);
                break;

            case 19:
                var1.setBlockBounds(0.0F, 0.0F, 1.0F - var4, var4, 1.0F, 1.0F);
                break;

            case 20:
                var1.setBlockBounds(1.0F - var4, 0.0F, 0.0F, 1.0F, 1.0F, var4);
                break;

            case 21:
                var1.setBlockBounds(1.0F - var4, 0.0F, 1.0F - var4, 1.0F, 1.0F, 1.0F);
                break;

            case 22:
                var1.setBlockBounds(0.0F, 1.0F - var4, 0.0F, 1.0F, 1.0F, var4);
                break;

            case 23:
                var1.setBlockBounds(0.0F, 1.0F - var4, 1.0F - var4, 1.0F, 1.0F, 1.0F);
                break;

            case 24:
                var1.setBlockBounds(0.0F, 1.0F - var4, 0.0F, var4, 1.0F, 1.0F);
                break;

            case 25:
                var1.setBlockBounds(1.0F - var4, 1.0F - var4, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 26:
                var1.setBlockBounds(0.5F - var4, 0.0F, 0.5F - var4, 0.5F + var4, 1.0F, 0.5F + var4);
                break;

            case 27:
                var1.setBlockBounds(0.5F - var4, 0.5F - var4, 0.0F, 0.5F + var4, 0.5F + var4, 1.0F);
                break;

            case 28:
                var1.setBlockBounds(0.0F, 0.5F - var4, 0.5F - var4, 1.0F, 0.5F + var4, 0.5F + var4);
        }
    }

    public int getSolidPartsMask()
    {
        return this.getCoverMask();
    }

    public int getPartsMask()
    {
        return this.getCoverMask();
    }

    public void dropCover(int var1, int var2)
    {
        ItemStack var3 = CoverLib.convertCoverPlate(var1, var2);

        if (var3 != null)
        {
            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var3);
        }
    }

    public float getExplosionResistance(int var1, int var2, Entity var3)
    {
        int var5 = this.getCover(var1);

        if (var5 < 0)
        {
            return -1.0F;
        }
        else
        {
            var5 &= 255;
            ItemStack var6 = CoverLib.getItemStack(var5);
            return Block.blocksList[var6.itemID].getExplosionResistance(var3);
        }
    }
}
