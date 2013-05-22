package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.FractalLib$BlockSnake;
import com.eloraam.redpower.core.Vector3;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenRubberTree extends WorldGenerator
{
    public void putLeaves(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockId(var2, var3, var4);

        if (var5 == 0)
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, RedPowerWorld.blockLeaves.blockID, 0);
        }
    }

    public boolean fillBlock(World var1, int var2, int var3, int var4)
    {
        if (var3 >= 0 && var3 <= 126)
        {
            int var5 = var1.getBlockId(var2, var3, var4);
            Block var6 = Block.blocksList[var5];

            if (var6 != null && var6.isWood(var1, var2, var3, var4))
            {
                return true;
            }
            else if (var6 != null && !var6.isLeaves(var1, var2, var3, var4) && var5 != Block.tallGrass.blockID && var5 != Block.grass.blockID && var5 != Block.vine.blockID)
            {
                return false;
            }
            else
            {
                var1.setBlockMetadataWithNotify(var2, var3, var4, RedPowerWorld.blockLogs.blockID, 0);
                this.putLeaves(var1, var2, var3 - 1, var4);
                this.putLeaves(var1, var2, var3 + 1, var4);
                this.putLeaves(var1, var2, var3, var4 - 1);
                this.putLeaves(var1, var2, var3, var4 + 1);
                this.putLeaves(var1, var2 - 1, var3, var4);
                this.putLeaves(var1, var2 + 1, var3, var4);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        int var9 = var2.nextInt(6) + 25;

        if (var4 >= 1 && var4 + var9 + 2 <= var1.getHeight())
        {
            int var6;
            int var7;
            int var8;

            for (var7 = -1; var7 <= 1; ++var7)
            {
                for (var8 = -1; var8 <= 1; ++var8)
                {
                    var6 = var1.getBlockId(var3 + var7, var4 - 1, var5 + var8);

                    if (var6 != Block.grass.blockID && var6 != Block.dirt.blockID)
                    {
                        return false;
                    }
                }
            }

            byte var10 = 1;
            int var11;

            for (var11 = var4; var11 < var4 + var9; ++var11)
            {
                if (var11 > var4 + 3)
                {
                    var10 = 5;
                }

                for (var7 = var3 - var10; var7 <= var3 + var10; ++var7)
                {
                    for (var8 = var5 - var10; var8 <= var5 + var10; ++var8)
                    {
                        var6 = var1.getBlockId(var7, var11, var8);
                        Block var12 = Block.blocksList[var6];

                        if (var12 != null && !var12.isLeaves(var1, var7, var11, var8) && !var12.isWood(var1, var7, var11, var8) && var6 != Block.tallGrass.blockID && var6 != Block.grass.blockID && var6 != Block.vine.blockID)
                        {
                            return false;
                        }
                    }
                }
            }

            for (var7 = -1; var7 <= 1; ++var7)
            {
                for (var8 = -1; var8 <= 1; ++var8)
                {
                    var1.setBlock(var3 + var7, var4 - 1, var5 + var8, Block.dirt.blockID);
                }
            }

            for (var11 = 0; var11 <= 6; ++var11)
            {
                for (var7 = -1; var7 <= 1; ++var7)
                {
                    for (var8 = -1; var8 <= 1; ++var8)
                    {
                        var1.setBlockMetadataWithNotify(var3 + var7, var4 + var11, var5 + var8, RedPowerWorld.blockLogs.blockID, 1);
                    }
                }

                for (var7 = -1; var7 <= 1; ++var7)
                {
                    if (var2.nextInt(5) == 1 && var1.getBlockId(var3 + var7, var4 + var11, var5 - 2) == 0)
                    {
                        var1.setBlockMetadataWithNotify(var3 + var7, var4 + var11, var5 - 2, Block.vine.blockID, 1);
                    }

                    if (var2.nextInt(5) == 1 && var1.getBlockId(var3 + var7, var4 + var11, var5 + 2) == 0)
                    {
                        var1.setBlockMetadataWithNotify(var3 + var7, var4 + var11, var5 + 2, Block.vine.blockID, 4);
                    }
                }

                for (var8 = -1; var8 <= 1; ++var8)
                {
                    if (var2.nextInt(5) == 1 && var1.getBlockId(var3 - 2, var4 + var11, var5 + var8) == 0)
                    {
                        var1.setBlockMetadataWithNotify(var3 - 2, var4 + var11, var5 + var8, Block.vine.blockID, 8);
                    }

                    if (var2.nextInt(5) == 1 && var1.getBlockId(var3 + 2, var4 + var11, var5 + var8) == 0)
                    {
                        var1.setBlockMetadataWithNotify(var3 + 2, var4 + var11, var5 + var8, Block.vine.blockID, 2);
                    }
                }
            }

            Vector3 var23 = new Vector3();
            Vector3 var24 = new Vector3();
            int var14 = var2.nextInt(100) + 10;
            int var17 = 0;

            while (var17 < var14)
            {
                var24.set((double)var2.nextFloat() - 0.5D, (double)var2.nextFloat(), (double)var2.nextFloat() - 0.5D);
                var24.normalize();
                double var18 = ((double)var14 / 10.0D + 4.0D) * (double)(1.0F + 1.0F * var2.nextFloat());
                var24.x *= var18;
                var24.z *= var18;
                var24.y = var24.y * (double)(var9 - 15) + (double)var14 / 10.0D;

                if (var14 < 8)
                {
                    switch (var14)
                    {
                        case 0:
                            var23.set((double)(var3 - 1), (double)(var4 + 6), (double)(var5 - 1));
                            break;

                        case 1:
                            var23.set((double)(var3 - 1), (double)(var4 + 6), (double)var5);
                            break;

                        case 2:
                            var23.set((double)(var3 - 1), (double)(var4 + 6), (double)(var5 + 1));
                            break;

                        case 3:
                            var23.set((double)var3, (double)(var4 + 6), (double)(var5 + 1));
                            break;

                        case 4:
                            var23.set((double)(var3 + 1), (double)(var4 + 6), (double)(var5 + 1));
                            break;

                        case 5:
                            var23.set((double)(var3 + 1), (double)(var4 + 6), (double)var5);
                            break;

                        case 6:
                            var23.set((double)(var3 + 1), (double)(var4 + 6), (double)(var5 - 1));
                            break;

                        default:
                            var23.set((double)var3, (double)(var4 + 6), (double)(var5 - 1));
                    }
                }
                else
                {
                    var23.set((double)(var3 + var2.nextInt(3) - 1), (double)(var4 + 6), (double)(var5 + var2.nextInt(3) - 1));
                }

                long var20 = var2.nextLong();
                FractalLib$BlockSnake var13 = new FractalLib$BlockSnake(var23, var24, var20);

                while (true)
                {
                    if (var13.iterate())
                    {
                        Vector3 var22 = var13.get();

                        if (this.fillBlock(var1, (int)Math.floor(var22.x), (int)Math.floor(var22.y), (int)Math.floor(var22.z)))
                        {
                            continue;
                        }
                    }

                    ++var17;
                    break;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
