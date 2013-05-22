package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockMachinePanel extends BlockMultipart
{
    public BlockMachinePanel(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(2.0F);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }

    public int getLightValue(IBlockAccess var1, int var2, int var3, int var4)
    {
        TileMachinePanel var5 = (TileMachinePanel)CoreLib.getTileEntity(var1, var2, var3, var4, TileMachinePanel.class);
        return var5 == null ? 0 : var5.getLightValue();
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

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
    }

    public String getTextureFile()
    {
        return "/eloraam/machine/machine1.png";
    }
}
