package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCustomCrops extends BlockFlower
{
    public BlockCustomCrops(int var1)
    {
        super(var1);
        this.setHardness(0.0F);
        this.setStepSound(soundGrassFootstep);
        this.setTickRandomly(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        switch (var2)
        {
            case 0:
                return 64;

            case 1:
                return 65;

            case 2:
                return 66;

            case 3:
                return 67;

            case 4:
                return 68;

            case 5:
                return 69;

            default:
                return 69;
        }
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. Args: blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int var1)
    {
        return var1 == Block.tilledField.blockID;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        float var6 = Math.min(1.0F, 0.1F + 0.25F * (float)var5);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var6, 1.0F);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 6;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return -1;
    }

    public boolean fertilize(World var1, int var2, int var3, int var4)
    {
        if (var1.getBlockLightValue(var2, var3 + 1, var4) < 9)
        {
            return false;
        }
        else
        {
            int var5 = var1.getBlockMetadata(var2, var3, var4);

            if (var5 != 4 && var5 != 5)
            {
                if (var1.getBlockId(var2, var3 - 1, var4) == Block.tilledField.blockID && var1.isAirBlock(var2, var3 + 1, var4))
                {
                    var1.setBlock(var2, var3, var4, 4);
                    var1.setBlockMetadataWithNotify(var2, var3 + 1, var4, this.blockID, 5);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }

    public ArrayList getBlockDropped(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        ArrayList var7 = new ArrayList();
        int var8;

        if (var5 == 4 || var5 == 5)
        {
            var8 = 1 + var1.rand.nextInt(3) + var1.rand.nextInt(1 + var6);

            while (var8-- > 0)
            {
                var7.add(new ItemStack(Item.silk));
            }
        }

        for (var8 = 0; var8 < 3 + var6; ++var8)
        {
            if (var5 == 5)
            {
                var5 = 4;
            }

            if (var1.rand.nextInt(8) <= var5)
            {
                var7.add(new ItemStack(RedPowerWorld.itemSeeds, 1, 0));
            }
        }

        return var7;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        super.updateTick(var1, var2, var3, var4, var5);

        if (var1.getBlockLightValue(var2, var3 + 1, var4) >= 9)
        {
            int var6 = var1.getBlockMetadata(var2, var3, var4);

            if (var6 != 4 && var6 != 5)
            {
                if (var1.getBlockId(var2, var3 - 1, var4) == Block.tilledField.blockID && var1.getBlockMetadata(var2, var3 - 1, var4) != 0 && var1.isAirBlock(var2, var3 + 1, var4))
                {
                    if (var5.nextInt(30) == 0)
                    {
                        var1.setBlock(var2, var3, var4, var6 + 1);

                        if (var6 == 3)
                        {
                            var1.setBlockMetadataWithNotify(var2, var3 + 1, var4, this.blockID, 5);
                        }
                    }
                }
            }
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        return var5 == 5 ? (var1.getBlockId(var2, var3 - 1, var4) != this.blockID ? false : var1.getBlockMetadata(var2, var3 - 1, var4) == 4) : (var1.getBlockId(var2, var3 - 1, var4) != Block.tilledField.blockID ? false : (var5 == 4 ? true : var1.isAirBlock(var2, var3 + 1, var4)));
    }

    public String getTextureFile()
    {
        return "/eloraam/world/world1.png";
    }
}
