package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerCore;
import java.util.ArrayList;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMultiblock extends BlockContainer
{
    public BlockMultiblock(int var1)
    {
        super(var1, CoreLib.materialRedpower);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return RedPowerCore.nullBlockModel;
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

    public ArrayList getBlockDropped(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        return new ArrayList();
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World var1)
    {
        return null;
    }

    public TileEntity createTileEntity(World var1, int var2)
    {
        switch (var2)
        {
            case 0:
                return new TileMultiblock();

            default:
                return null;
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        TileMultiblock var7 = (TileMultiblock)CoreLib.getTileEntity(var1, var2, var3, var4, TileMultiblock.class);

        if (var7 != null)
        {
            IMultiblock var8 = (IMultiblock)CoreLib.getTileEntity(var1, var7.relayX, var7.relayY, var7.relayZ, IMultiblock.class);

            if (var8 != null)
            {
                var8.onMultiRemoval(var7.relayNum);
            }
        }
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4)
    {
        TileMultiblock var5 = (TileMultiblock)CoreLib.getTileEntity(var1, var2, var3, var4, TileMultiblock.class);

        if (var5 == null)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            IMultiblock var6 = (IMultiblock)CoreLib.getTileEntity(var1, var5.relayX, var5.relayY, var5.relayZ, IMultiblock.class);

            if (var6 != null)
            {
                AxisAlignedBB var7 = var6.getMultiBounds(var5.relayNum);
                int var8 = var5.relayX - var2;
                int var9 = var5.relayY - var3;
                int var10 = var5.relayZ - var4;
                this.setBlockBounds((float)var7.minX + (float)var8, (float)var7.minY + (float)var9, (float)var7.minZ + (float)var10, (float)var7.maxX + (float)var8, (float)var7.maxY + (float)var9, (float)var7.maxZ + (float)var10);
            }
        }
    }

    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    public float getPlayerRelativeBlockHardness(EntityPlayer var1, World var2, int var3, int var4, int var5)
    {
        TileMultiblock var6 = (TileMultiblock)CoreLib.getTileEntity(var2, var3, var4, var5, TileMultiblock.class);

        if (var6 == null)
        {
            return 0.0F;
        }
        else
        {
            IMultiblock var7 = (IMultiblock)CoreLib.getTileEntity(var2, var6.relayX, var6.relayY, var6.relayZ, IMultiblock.class);
            return var7 == null ? 0.0F : var7.getMultiBlockStrength(var6.relayNum, var1);
        }
    }
}
