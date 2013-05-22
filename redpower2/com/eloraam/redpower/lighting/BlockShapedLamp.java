package com.eloraam.redpower.lighting;

import com.eloraam.redpower.RedPowerLighting;
import com.eloraam.redpower.core.BlockExtended;
import com.eloraam.redpower.core.CoreLib;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

public class BlockShapedLamp extends BlockExtended
{
    public BlockShapedLamp(int var1)
    {
        super(var1, CoreLib.materialRedpower);
        this.setHardness(1.0F);
        this.setCreativeTab(RedPowerLighting.tabLamp);
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
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isACube()
    {
        return false;
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

    public int getLightValue(IBlockAccess var1, int var2, int var3, int var4)
    {
        TileShapedLamp var5 = (TileShapedLamp)CoreLib.getTileEntity(var1, var2, var3, var4, TileShapedLamp.class);
        return var5 == null ? 0 : var5.getLightValue();
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4)
    {
        TileShapedLamp var5 = (TileShapedLamp)CoreLib.getTileEntity(var1, var2, var3, var4, TileShapedLamp.class);

        if (var5 == null)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            AxisAlignedBB var6 = var5.getBounds();
            this.setBlockBounds((float)var6.minX, (float)var6.minY, (float)var6.minZ, (float)var6.maxX, (float)var6.maxY, (float)var6.maxZ);
        }
    }
}
