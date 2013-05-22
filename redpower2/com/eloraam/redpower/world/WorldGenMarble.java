package com.eloraam.redpower.world;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenMarble extends WorldGenCustomOre
{
    LinkedList fillStack = new LinkedList();
    HashSet fillStackTest = new HashSet();

    public WorldGenMarble(int var1, int var2, int var3)
    {
        super(var1, var2, var3);
    }

    private void addBlock(int var1, int var2, int var3, int var4)
    {
        List var5 = Arrays.asList(new Integer[] {Integer.valueOf(var1), Integer.valueOf(var2), Integer.valueOf(var3)});

        if (!this.fillStackTest.contains(var5))
        {
            this.fillStack.addLast(Arrays.asList(new Integer[] {Integer.valueOf(var1), Integer.valueOf(var2), Integer.valueOf(var3), Integer.valueOf(var4)}));
            this.fillStackTest.add(var5);
        }
    }

    private void searchBlock(World var1, int var2, int var3, int var4, int var5)
    {
        if (var1.getBlockId(var2 - 1, var3, var4) == 0 || var1.getBlockId(var2 + 1, var3, var4) == 0 || var1.getBlockId(var2, var3 - 1, var4) == 0 || var1.getBlockId(var2, var3 + 1, var4) == 0 || var1.getBlockId(var2, var3, var4 - 1) == 0 || var1.getBlockId(var2, var3, var4 + 1) == 0)
        {
            var5 = 6;
        }

        this.addBlock(var2 - 1, var3, var4, var5);
        this.addBlock(var2 + 1, var3, var4, var5);
        this.addBlock(var2, var3 - 1, var4, var5);
        this.addBlock(var2, var3 + 1, var4, var5);
        this.addBlock(var2, var3, var4 - 1, var5);
        this.addBlock(var2, var3, var4 + 1, var5);
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        if (var1.getBlockId(var3, var4, var5) != 0)
        {
            return false;
        }
        else
        {
            int var6;

            for (var6 = var4; var1.getBlockId(var3, var6, var5) != Block.stone.blockID; ++var6)
            {
                if (var6 > 96)
                {
                    return false;
                }
            }

            this.addBlock(var3, var6, var5, 6);

            while (this.fillStack.size() > 0 && this.numberOfBlocks > 0)
            {
                List var7 = (List)this.fillStack.removeFirst();
                Integer[] var8 = (Integer[])((Integer[])var7.toArray());

                if (var1.getBlockId(var8[0].intValue(), var8[1].intValue(), var8[2].intValue()) == Block.stone.blockID)
                {
                    var1.setBlockMetadataWithNotify(var8[0].intValue(), var8[1].intValue(), var8[2].intValue(), this.minableBlockId, this.minableBlockMeta);

                    if (var8[3].intValue() > 0)
                    {
                        this.searchBlock(var1, var8[0].intValue(), var8[1].intValue(), var8[2].intValue(), var8[3].intValue() - 1);
                    }

                    --this.numberOfBlocks;
                }
            }

            return true;
        }
    }
}
