package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BlockExtended;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockMachine extends BlockExtended
{
    public BlockMachine(int var1)
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

    public boolean isBlockNormalCube(World var1, int var2, int var3, int var4)
    {
        return false;
    }

    public boolean isBlockSolidOnSide(World var1, int var2, int var3, int var4, ForgeDirection var5)
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

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        TileMachine var6 = (TileMachine)CoreLib.getTileEntity(var1, var2, var3, var4, TileMachine.class);
        return 0;
    }

    public boolean isFireSource(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        if (var5 != 12)
        {
            return false;
        }
        else
        {
            TileIgniter var7 = (TileIgniter)CoreLib.getTileEntity(var1, var2, var3, var4, TileIgniter.class);
            return var7 == null ? false : var7.isOnFire(var6);
        }
    }

    public String getTextureFile()
    {
        return "/eloraam/machine/machine1.png";
    }
}
