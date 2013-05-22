package com.eloraam.redpower.world;

import com.eloraam.redpower.core.IBlockHardness;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockCustomStone extends Block implements IBlockHardness
{
    public BlockCustomStone(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(3.0F);
        this.setResistance(10.0F);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public float getPrototypicalHardness(int var1)
    {
        switch (var1)
        {
            case 0:
                return 1.0F;

            case 1:
                return 2.5F;

            case 2:
                return 1.0F;

            case 3:
                return 2.5F;

            case 4:
                return 2.5F;

            case 5:
                return 2.5F;

            case 6:
                return 2.5F;

            default:
                return 3.0F;
        }
    }

    /**
     * Returns the block hardness at a location. Args: world, x, y, z
     */
    public float getBlockHardness(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        return this.getPrototypicalHardness(var5);
    }

    public float getExplosionResistance(Entity var1, World var2, int var3, int var4, int var5, double var6, double var8, double var10)
    {
        int var12 = var2.getBlockMetadata(var3, var4, var5);

        switch (var12)
        {
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
                return 12.0F;

            case 2:
            default:
                return 6.0F;
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        return 16 + var2;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return this.blockID;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random var1)
    {
        return 1;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1 == 1 ? 3 : (var1 == 6 ? 3 : var1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 <= 6; ++var4)
        {
            var3.add(new ItemStack(this, 1, var4));
        }
    }

    public String getTextureFile()
    {
        return "/eloraam/world/world1.png";
    }
}
