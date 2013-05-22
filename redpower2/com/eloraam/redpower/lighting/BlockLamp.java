package com.eloraam.redpower.lighting;

import com.eloraam.redpower.RedPowerCore;
import com.eloraam.redpower.RedPowerLighting;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RedPowerLib;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockLamp extends Block
{
    public boolean lit;
    public boolean powered;
    public int onID;
    public int offID;

    public BlockLamp(int var1, boolean var2, boolean var3)
    {
        super(var1, CoreLib.materialRedpower);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setHardness(0.5F);
        this.setCreativeTab(RedPowerLighting.tabLamp);
        this.lit = var2;
        this.powered = var3;
    }

    public boolean canRenderInPass(int var1)
    {
        return true;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
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

    public boolean isACube()
    {
        return true;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return this.offID;
    }

    private void checkPowerState(World var1, int var2, int var3, int var4)
    {
        int var5;

        if (!this.powered && RedPowerLib.isPowered(var1, var2, var3, var4, 16777215, 63))
        {
            var5 = var1.getBlockMetadata(var2, var3, var4);
            var1.setBlockMetadataWithNotify(var2, var3, var4, this.onID, var5);
            var1.markBlockForUpdate(var2, var3, var4);
        }
        else if (this.powered && !RedPowerLib.isPowered(var1, var2, var3, var4, 16777215, 63))
        {
            var5 = var1.getBlockMetadata(var2, var3, var4);
            var1.setBlockMetadataWithNotify(var2, var3, var4, this.offID, var5);
            var1.markBlockForUpdate(var2, var3, var4);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        this.checkPowerState(var1, var2, var3, var4);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        this.checkPowerState(var1, var2, var3, var4);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return RedPowerCore.customBlockModel;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3) {}
}
