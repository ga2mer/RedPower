package com.eloraam.redpower.world;

import com.eloraam.redpower.core.WorldCoord;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.world.World;

public class BlockBrickMossifier extends BlockStoneBrick
{
    public BlockBrickMossifier(int var1)
    {
        super(var1);
        this.setTickRandomly(true);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundStoneFootstep);
        this.setUnlocalizedName("stonebricksmooth");
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        switch (var1.getBlockMetadata(var2, var3, var4))
        {
            case 0:
                this.crackBrick(var1, var2, var3, var4, var5);
                break;

            case 1:
                this.spreadMoss(var1, var2, var3, var4, var5);
        }
    }

    private void crackBrick(World var1, int var2, int var3, int var4, Random var5)
    {
        WorldCoord var6 = new WorldCoord(var2, var3, var4);
        boolean var7 = false;
        boolean var8 = false;

        for (int var9 = 0; var9 < 6; ++var9)
        {
            WorldCoord var10 = var6.coordStep(var9);
            int var11 = var1.getBlockId(var10.x, var10.y, var10.z);

            if (var11 != Block.waterStill.blockID && var11 != Block.waterMoving.blockID)
            {
                if (var11 == Block.lavaStill.blockID || var11 == Block.lavaMoving.blockID)
                {
                    var7 = true;
                }
            }
            else
            {
                var8 = true;
            }
        }

        if (var7 && var8)
        {
            if (var5.nextInt(2) == 0)
            {
                var1.setBlock(var2, var3, var4, 2);
            }
        }
    }

    private void spreadMoss(World var1, int var2, int var3, int var4, Random var5)
    {
        if (var1.isAirBlock(var2, var3 + 1, var4))
        {
            if (!var1.canBlockSeeTheSky(var2, var3 + 1, var4))
            {
                WorldCoord var6 = new WorldCoord(var2, var3, var4);

                for (int var7 = 0; var7 < 4; ++var7)
                {
                    WorldCoord var8 = var6.coordStep(2 + var7);
                    int var9 = var1.getBlockId(var8.x, var8.y, var8.z);
                    int var10 = var9;
                    byte var11 = 0;

                    if (var9 == Block.cobblestone.blockID)
                    {
                        var10 = Block.cobblestoneMossy.blockID;
                    }
                    else
                    {
                        if (var9 != Block.stoneBrick.blockID || var1.getBlockMetadata(var8.x, var8.y, var8.z) != 0)
                        {
                            continue;
                        }

                        var11 = 1;
                    }

                    if (var1.getBlockId(var8.x, var8.y + 1, var8.z) == 0)
                    {
                        if (var1.canBlockSeeTheSky(var8.x, var8.y + 1, var8.z))
                        {
                            return;
                        }

                        boolean var12 = false;

                        for (int var13 = 0; var13 < 4; ++var13)
                        {
                            WorldCoord var14 = var8.coordStep(2 + var13);
                            int var15 = var1.getBlockId(var14.x, var14.y, var14.z);

                            if (var15 == Block.waterStill.blockID || var15 == Block.waterMoving.blockID)
                            {
                                var12 = true;
                                break;
                            }
                        }

                        if (var12 && var5.nextInt(2) == 0)
                        {
                            var1.setBlockMetadataWithNotify(var8.x, var8.y, var8.z, var10, var11);
                        }
                    }
                }
            }
        }
    }
}
