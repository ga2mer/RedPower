package com.eloraam.redpower.core;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class RenderCustomBlock
{
    protected Block block;

    public RenderCustomBlock(Block var1)
    {
        this.block = var1;
    }

    public abstract void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5);

    public abstract void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6);

    public abstract void renderInvBlock(RenderBlocks var1, int var2);
}
