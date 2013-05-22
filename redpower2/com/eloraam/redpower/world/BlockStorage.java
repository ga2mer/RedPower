package com.eloraam.redpower.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class BlockStorage extends Block
{
    public BlockStorage(int var1)
    {
        super(var1, Material.iron);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundMetalFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        return 80 + var2;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
    }

    public String getTextureFile()
    {
        return "/eloraam/world/world1.png";
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 <= 5; ++var4)
        {
            var3.add(new ItemStack(this, 1, var4));
        }
    }
}
