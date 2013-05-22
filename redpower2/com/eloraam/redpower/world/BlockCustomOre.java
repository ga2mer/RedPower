package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerBase;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCustomOre extends Block
{
    public BlockCustomOre(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
    }

    /**
     * Returns the block hardness at a location. Args: world, x, y, z
     */
    public float getBlockHardness(World var1, int var2, int var3, int var4)
    {
        return 3.0F;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        return 32 + var2;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return var1 >= 3 && var1 != 7 ? this.blockID : RedPowerBase.itemResource.itemID;
    }

    public int quantityDropped(int var1, int var2, Random var3)
    {
        if (var1 == 7)
        {
            return 4 + var3.nextInt(2) + var3.nextInt(var2 + 1);
        }
        else if (var1 < 3)
        {
            int var4 = var3.nextInt(var2 + 2) - 1;

            if (var4 < 0)
            {
                var4 = 0;
            }

            return var4 + 1;
        }
        else
        {
            return 1;
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1 == 7 ? 6 : var1;
    }

    public void addCreativeItems(ArrayList var1)
    {
        for (int var2 = 0; var2 <= 7; ++var2)
        {
            var1.add(new ItemStack(this, 1, var2));
        }
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7)
    {
        super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6, var7);
        byte var8 = 0;
        byte var9 = 0;

        switch (var5)
        {
            case 0:
            case 1:
            case 2:
                var8 = 3;
                var9 = 7;

            case 3:
            case 4:
            case 5:
            case 6:
            default:
                break;

            case 7:
                var8 = 1;
                var9 = 5;
        }

        if (var9 > 0)
        {
            this.dropXpOnBlockBreak(var1, var2, var3, var4, MathHelper.getRandomIntegerInRange(var1.rand, var8, var9));
        }
    }

    public String getTextureFile()
    {
        return "/eloraam/world/world1.png";
    }
}
