package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.WorldCoord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCustomLeaves extends BlockLeaves
{
    public BlockCustomLeaves(int var1)
    {
        super(var1);
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
        this.setLightOpacity(1);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        this.graphicsLevel = !Block.leaves.isOpaqueCube();
        return !this.graphicsLevel;
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        this.graphicsLevel = !Block.leaves.isOpaqueCube();
        return super.shouldSideBeRendered(var1, var2, var3, var4, var5);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        this.graphicsLevel = !Block.leaves.isOpaqueCube();
        return 48 + (this.graphicsLevel ? 0 : 1);
    }

    public void onBlockRemoval(World var1, int var2, int var3, int var4)
    {
        updateLeaves(var1, var2, var3, var4, 1);
    }

    public static void updateLeaves(World var0, int var1, int var2, int var3, int var4)
    {
        if (var0.checkChunksExist(var1 - var4 - 1, var2 - var4 - 1, var3 - var4 - 1, var1 + var4 + 1, var2 + var4 + 1, var3 + var4 + 1))
        {
            for (int var5 = -var4; var5 <= var4; ++var5)
            {
                for (int var6 = -var4; var6 <= var4; ++var6)
                {
                    for (int var7 = -var4; var7 <= var4; ++var7)
                    {
                        if (var0.getBlockId(var1 + var5, var2 + var6, var3 + var7) == RedPowerWorld.blockLeaves.blockID)
                        {
                            int var8 = var0.getBlockMetadata(var1 + var5, var2 + var6, var3 + var7);
                            var0.setBlock(var1 + var5, var2 + var6, var3 + var7, var8 | 8);
                        }
                    }
                }
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (!CoreLib.isClient(var1))
        {
            int var6 = var1.getBlockMetadata(var2, var3, var4);

            if ((var6 & 8) != 0 && (var6 & 4) <= 0)
            {
                HashMap var7 = new HashMap();
                LinkedList var8 = new LinkedList();
                WorldCoord var9 = new WorldCoord(var2, var3, var4);
                WorldCoord var10 = var9.copy();
                var8.addLast(var9);
                var7.put(var9, Integer.valueOf(4));

                while (var8.size() > 0)
                {
                    var9 = (WorldCoord)var8.removeFirst();
                    Integer var11 = (Integer)var7.get(var9);

                    if (var11 != null)
                    {
                        for (int var12 = 0; var12 < 6; ++var12)
                        {
                            var10.set(var9);
                            var10.step(var12);

                            if (!var7.containsKey(var10))
                            {
                                int var13 = var1.getBlockId(var10.x, var10.y, var10.z);

                                if (var13 == RedPowerWorld.blockLogs.blockID)
                                {
                                    var1.setBlock(var2, var3, var4, var6 & -9);
                                    return;
                                }

                                if (var11.intValue() != 0 && var13 == this.blockID)
                                {
                                    var7.put(var10, Integer.valueOf(var11.intValue() - 1));
                                    var8.addLast(var10);
                                }
                            }
                        }
                    }
                }

                this.dropBlockAsItem(var1, var2, var3, var4, var6, 0);
                var1.setBlock(var2, var3, var4, 0);
            }
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return RedPowerWorld.blockPlants.blockID;
    }

    public int quantityDropped(int var1, int var2, Random var3)
    {
        return var3.nextInt(20) != 0 ? 0 : 1;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return 1;
    }

    public boolean isLeaves(World var1, int var2, int var3, int var4)
    {
        return true;
    }

    public void addCreativeItems(ArrayList var1)
    {
        var1.add(new ItemStack(this, 1, 0));
    }

    public String getTextureFile()
    {
        return "/eloraam/world/world1.png";
    }
}
