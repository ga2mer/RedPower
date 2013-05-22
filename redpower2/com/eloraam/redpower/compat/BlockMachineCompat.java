package com.eloraam.redpower.compat;

import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CreativeExtraTabs;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockMachineCompat extends BlockMultipart
{
    public BlockMachineCompat(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(2.0F);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isACube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isBlockNormalCube(World var1, int var2, int var3, int var4)
    {
        return false;
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
        return "/eloraam/compat/compat1.png";
    }
}
