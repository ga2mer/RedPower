package com.eloraam.redpower.core;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockMultipart extends BlockExtended
{
    public BlockMultipart(int var1, Material var2)
    {
        super(var1, var2);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        TileMultipart var6 = (TileMultipart)CoreLib.getTileEntity(var1, var2, var3, var4, TileMultipart.class);

        if (var6 == null)
        {
            var1.setBlock(var2, var3, var4, 0);
        }
        else
        {
            var6.onBlockNeighborChange(var5);
        }
    }

    public boolean removeBlockByPlayer(World var1, EntityPlayer var2, int var3, int var4, int var5)
    {
        if (CoreLib.isClient(var1))
        {
            return true;
        }
        else
        {
            MovingObjectPosition var6 = CoreLib.retraceBlock(var1, var2, var3, var4, var5);

            if (var6 == null)
            {
                return false;
            }
            else if (var6.typeOfHit != EnumMovingObjectType.TILE)
            {
                return false;
            }
            else
            {
                TileMultipart var7 = (TileMultipart)CoreLib.getTileEntity(var1, var3, var4, var5, TileMultipart.class);

                if (var7 == null)
                {
                    return false;
                }
                else
                {
                    var7.onHarvestPart(var2, var6.subHit);
                    return false;
                }
            }
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        MovingObjectPosition var10 = CoreLib.retraceBlock(var1, var5, var2, var3, var4);

        if (var10 == null)
        {
            return false;
        }
        else if (var10.typeOfHit != EnumMovingObjectType.TILE)
        {
            return false;
        }
        else
        {
            TileMultipart var11 = (TileMultipart)CoreLib.getTileEntity(var1, var2, var3, var4, TileMultipart.class);
            return var11 == null ? false : var11.onPartActivateSide(var5, var10.subHit, var10.sideHit);
        }
    }

    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    public float getPlayerRelativeBlockHardness(EntityPlayer var1, World var2, int var3, int var4, int var5)
    {
        MovingObjectPosition var6 = CoreLib.retraceBlock(var2, var1, var3, var4, var5);

        if (var6 == null)
        {
            return 0.0F;
        }
        else if (var6.typeOfHit != EnumMovingObjectType.TILE)
        {
            return 0.0F;
        }
        else
        {
            TileMultipart var7 = (TileMultipart)CoreLib.getTileEntity(var1.worldObj, var3, var4, var5, TileMultipart.class);
            return var7 == null ? 0.0F : var7.getPartStrength(var1, var6.subHit);
        }
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void onBlockDestroyedByExplosion(World var1, int var2, int var3, int var4)
    {
        TileMultipart var5 = (TileMultipart)CoreLib.getTileEntity(var1, var2, var3, var4, TileMultipart.class);

        if (var5 != null)
        {
            var5.breakBlock();
        }
    }

    /**
     * if the specified block is in the given AABB, add its collision bounding box to the given list
     */
    public void addCollidingBlockToList(World var1, int var2, int var3, int var4, AxisAlignedBB var5, List var6, Entity var7)
    {
        TileMultipart var8 = (TileMultipart)CoreLib.getTileEntity(var1, var2, var3, var4, TileMultipart.class);

        if (var8 != null)
        {
            int var9 = var8.getSolidPartsMask();

            while (var9 > 0)
            {
                int var10 = Integer.numberOfTrailingZeros(var9);
                var9 &= ~(1 << var10);
                var8.setPartBounds(this, var10);
                super.addCollidingBlockToList(var1, var2, var3, var4, var5, var6, var7);
            }
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3 var5, Vec3 var6)
    {
        TileMultipart var7 = (TileMultipart)CoreLib.getTileEntity(var1, var2, var3, var4, TileMultipart.class);

        if (var7 == null)
        {
            return null;
        }
        else
        {
            int var8 = var7.getPartsMask();
            MovingObjectPosition var10 = null;
            int var12 = -1;
            double var13 = 0.0D;

            while (var8 > 0)
            {
                int var9 = Integer.numberOfTrailingZeros(var8);
                var8 &= ~(1 << var9);
                var7.setPartBounds(this, var9);
                MovingObjectPosition var11 = super.collisionRayTrace(var1, var2, var3, var4, var5, var6);

                if (var11 != null)
                {
                    double var15 = var11.hitVec.squareDistanceTo(var5);

                    if (var10 == null || var15 < var13)
                    {
                        var13 = var15;
                        var10 = var11;
                        var12 = var9;
                    }
                }
            }

            if (var10 == null)
            {
                return null;
            }
            else
            {
                var7.setPartBounds(this, var12);
                var10.subHit = var12;
                return var10;
            }
        }
    }

    public static void removeMultipart(World var0, int var1, int var2, int var3)
    {
        var0.setBlock(var1, var2, var3, 0);
    }

    public static void removeMultipartWithNotify(World var0, int var1, int var2, int var3)
    {
        var0.setBlock(var1, var2, var3, 0);
    }

    protected MovingObjectPosition traceCurrentBlock(World var1, int var2, int var3, int var4, Vec3 var5, Vec3 var6)
    {
        return super.collisionRayTrace(var1, var2, var3, var4, var5, var6);
    }

    public void setPartBounds(World var1, int var2, int var3, int var4, int var5)
    {
        TileMultipart var6 = (TileMultipart)CoreLib.getTileEntity(var1, var2, var3, var4, TileMultipart.class);

        if (var6 == null)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            var6.setPartBounds(this, var5);
        }
    }

    public void computeCollidingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, List var6, TileMultipart var7)
    {
        int var8 = var7.getSolidPartsMask();

        while (var8 > 0)
        {
            int var9 = Integer.numberOfTrailingZeros(var8);
            var8 &= ~(1 << var9);
            var7.setPartBounds(this, var9);
            super.addCollidingBlockToList(var1, var2, var3, var4, var5, var6, (Entity)null);
        }
    }
}
