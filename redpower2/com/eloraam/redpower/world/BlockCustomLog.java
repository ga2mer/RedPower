package com.eloraam.redpower.world;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockCustomLog extends Block
{
    public BlockCustomLog(int var1)
    {
        super(var1, Material.wood);
        this.setHardness(1.5F);
        this.setStepSound(Block.soundWoodFootstep);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        return var1 < 2 ? 51 : 50;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1 == 1 ? 0 : var1;
    }

    public boolean isWood(World var1, int var2, int var3, int var4)
    {
        return true;
    }

    public void onBlockRemoval(World var1, int var2, int var3, int var4)
    {
        BlockCustomLeaves.updateLeaves(var1, var2, var3, var4, 4);
    }

    public void addCreativeItems(ArrayList var1)
    {
        var1.add(new ItemStack(this, 1, 0));
    }

    public String getTextureFile()
    {
        return "/eloraam/world/world1.png";
    }
}
