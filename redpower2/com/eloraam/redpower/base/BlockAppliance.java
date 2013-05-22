package com.eloraam.redpower.base;

import com.eloraam.redpower.core.BlockExtended;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockAppliance extends BlockExtended
{
    public BlockAppliance(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(2.0F);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }

    public int getLightValue(IBlockAccess var1, int var2, int var3, int var4)
    {
        TileAppliance var5 = (TileAppliance)CoreLib.getTileEntity(var1, var2, var3, var4, TileAppliance.class);
        return var5 == null ? super.getLightValue(var1, var2, var3, var4) : var5.getLightValue();
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return true;
    }

    public boolean isACube()
    {
        return true;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return true;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
    }
}
