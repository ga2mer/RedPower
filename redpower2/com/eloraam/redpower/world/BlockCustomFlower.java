package com.eloraam.redpower.world;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockCustomFlower extends BlockFlower
{
    public BlockCustomFlower(int var1, int var2)
    {
        super(var1);
        this.setHardness(0.0F);
        this.setStepSound(soundGrassFootstep);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        switch (var2)
        {
            case 0:
                return 1;

            case 1:
                return 2;

            case 2:
                return 2;

            default:
                return 1;
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        int var6 = var1.getBlockMetadata(var2, var3, var4);

        if (var6 == 1 || var6 == 2)
        {
            if (var1.getBlockLightValue(var2, var3 + 1, var4) >= 9 && var5.nextInt(300) == 0)
            {
                if (var6 == 1)
                {
                    var1.setBlock(var2, var3, var4, 2);
                }
                else if (var6 == 2)
                {
                    this.growTree(var1, var2, var3, var4);
                }
            }
        }
    }

    public void growTree(World var1, int var2, int var3, int var4)
    {
        var1.setBlock(var2, var3, var4, 0);
        WorldGenRubberTree var5 = new WorldGenRubberTree();

        if (!var5.generate(var1, var1.rand, var2, var3, var4))
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, this.blockID, 1);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1 == 2 ? 1 : var1;
    }

    public void addCreativeItems(ArrayList var1)
    {
        for (int var2 = 0; var2 <= 1; ++var2)
        {
            var1.add(new ItemStack(this, 1, var2));
        }
    }

    public String getTextureFile()
    {
        return "/eloraam/world/worlditems1.png";
    }
}
