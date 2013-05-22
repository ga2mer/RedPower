package com.eloraam.redpower.core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockCoverable extends BlockMultipart
{
    public BlockCoverable(int var1, Material var2)
    {
        super(var1, var2);
    }

    public boolean isBlockSolidOnSide(World var1, int var2, int var3, int var4, ForgeDirection var5)
    {
        TileCoverable var6 = (TileCoverable)CoreLib.getTileEntity(var1, var2, var3, var4, TileCoverable.class);
        return var6 == null ? false : var6.isSideNormal(var5.ordinal());
    }

    public float getExplosionResistance(Entity var1, World var2, int var3, int var4, int var5, double var6, double var8, double var10)
    {
        Vec3 var12 = Vec3.createVectorHelper(var6, var8, var10);
        Vec3 var13 = Vec3.createVectorHelper((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D);
        Block var14 = Block.blocksList[var2.getBlockId(var3, var4, var5)];

        if (var14 == null)
        {
            return 0.0F;
        }
        else
        {
            MovingObjectPosition var15 = var14.collisionRayTrace(var2, var3, var4, var5, var12, var13);

            if (var15 == null)
            {
                return var14.getExplosionResistance(var1);
            }
            else
            {
                TileCoverable var16 = (TileCoverable)CoreLib.getTileEntity(var2, var3, var4, var5, TileCoverable.class);

                if (var16 == null)
                {
                    return var14.getExplosionResistance(var1);
                }
                else
                {
                    float var17 = var16.getExplosionResistance(var15.subHit, var15.sideHit, var1);
                    return var17 < 0.0F ? var14.getExplosionResistance(var1) : var17;
                }
            }
        }
    }
}
