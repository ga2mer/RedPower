package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.world.World;

public class WorldGenVolcano extends WorldGenCustomOre
{
    LinkedList fillStack = new LinkedList();
    HashMap fillStackTest = new HashMap();

    public WorldGenVolcano(int var1, int var2, int var3)
    {
        super(var1, var2, var3);
    }

    private void addBlock(int var1, int var2, int var3, int var4)
    {
        if (var4 > 0)
        {
            List var5 = Arrays.asList(new Integer[] {Integer.valueOf(var1), Integer.valueOf(var3)});
            Integer var6 = (Integer)this.fillStackTest.get(var5);

            if (var6 == null || var4 > var6.intValue())
            {
                this.fillStack.addLast(Arrays.asList(new Integer[] {Integer.valueOf(var1), Integer.valueOf(var2), Integer.valueOf(var3)}));
                this.fillStackTest.put(var5, Integer.valueOf(var4));
            }
        }
    }

    private void searchBlock(int var1, int var2, int var3, int var4, Random var5)
    {
        int var6 = var5.nextInt(16);
        this.addBlock(var1 - 1, var2, var3, (var6 & 1) > 0 ? var4 - 1 : var4);
        this.addBlock(var1 + 1, var2, var3, (var6 & 2) > 0 ? var4 - 1 : var4);
        this.addBlock(var1, var2, var3 - 1, (var6 & 4) > 0 ? var4 - 1 : var4);
        this.addBlock(var1, var2, var3 + 1, (var6 & 8) > 0 ? var4 - 1 : var4);
    }

    public boolean canReplaceId(int var1)
    {
        return var1 == 0 ? true : (var1 != Block.waterMoving.blockID && var1 != Block.waterStill.blockID && var1 != Block.wood.blockID && var1 != Block.leaves.blockID && var1 != Block.vine.blockID && var1 != Block.snow.blockID && var1 != Block.ice.blockID ? (var1 != RedPowerWorld.blockLogs.blockID && var1 != RedPowerWorld.blockLeaves.blockID ? Block.blocksList[var1] instanceof BlockFlower : true) : true);
    }

    public void eatTree(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockId(var2, var3, var4);

        if (var5 == Block.snow.blockID)
        {
            var1.setBlock(var2, var3, var4, 0);
        }
        else if (var5 == Block.wood.blockID || var5 == Block.leaves.blockID || var5 == Block.vine.blockID)
        {
            var1.setBlock(var2, var3, var4, 0);
            this.eatTree(var1, var2, var3 + 1, var4);
        }
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        if (var1.getBlockId(var3, var4, var5) != Block.lavaStill.blockID)
        {
            return false;
        }
        else
        {
            int var7 = var1.getHeightValue(var3, var5);
            int var8;

            for (var8 = Block.lavaMoving.blockID; this.canReplaceId(var1.getBlockId(var3, var7 - 1, var5)); --var7)
            {
                ;
            }

            int var6;

            for (var6 = var4; var6 < var7; ++var6)
            {
                var1.setBlock(var3, var6, var5, var8);
                var1.setBlockMetadataWithNotify(var3 - 1, var6, var5, this.minableBlockId, this.minableBlockMeta);
                var1.setBlockMetadataWithNotify(var3 + 1, var6, var5, this.minableBlockId, this.minableBlockMeta);
                var1.setBlockMetadataWithNotify(var3, var6, var5 - 1, this.minableBlockId, this.minableBlockMeta);
                var1.setBlockMetadataWithNotify(var3, var6, var5 + 1, this.minableBlockId, this.minableBlockMeta);
            }

            int var9 = 3 + var2.nextInt(4);
            int var10 = var2.nextInt(3);
            label69:

            while (this.numberOfBlocks > 0)
            {
                while (this.fillStack.size() == 0)
                {
                    var1.setBlock(var3, var6, var5, var8);
                    this.fillStackTest.clear();
                    this.searchBlock(var3, var6, var5, var9, var2);
                    ++var6;

                    if (var6 > 125)
                    {
                        break label69;
                    }
                }

                List var11 = (List)this.fillStack.removeFirst();
                Integer[] var12 = (Integer[])((Integer[])var11.toArray());
                var1.getBlockId(var12[0].intValue(), 64, var12[2].intValue());

                if (var1.blockExists(var12[0].intValue(), 64, var12[2].intValue()))
                {
                    int var13 = ((Integer)this.fillStackTest.get(Arrays.asList(new Integer[] {var12[0], var12[2]}))).intValue();
                    int var14;

                    for (var14 = var1.getHeightValue(var12[0].intValue(), var12[2].intValue()) + 1; var14 > 0 && this.canReplaceId(var1.getBlockId(var12[0].intValue(), var14 - 1, var12[2].intValue())); --var14)
                    {
                        ;
                    }

                    if (var14 <= var12[1].intValue())
                    {
                        int var15 = var1.getBlockId(var12[0].intValue(), var14, var12[2].intValue());

                        if (this.canReplaceId(var15))
                        {
                            this.eatTree(var1, var12[0].intValue(), var14, var12[2].intValue());
                            var1.setBlockMetadataWithNotify(var12[0].intValue(), var14, var12[2].intValue(), this.minableBlockId, this.minableBlockMeta);

                            if (var12[1].intValue() > var14)
                            {
                                var13 = Math.max(var13, var10);
                            }

                            this.searchBlock(var12[0].intValue(), var14, var12[2].intValue(), var13, var2);
                            --this.numberOfBlocks;
                        }
                    }
                }
            }

            var1.setBlock(var3, var6, var5, var8);

            while (var6 > var7 && var1.getBlockId(var3, var6, var5) == var8)
            {
                var1.markBlockForUpdate(var3, var6, var5);
                var1.notifyBlocksOfNeighborChange(var3, var6, var5, var8);
                var1.scheduledUpdatesAreImmediate = true;
                Block.blocksList[var8].updateTick(var1, var3, var6, var5, var2);
                var1.scheduledUpdatesAreImmediate = false;
                --var6;
            }

            return true;
        }
    }
}
