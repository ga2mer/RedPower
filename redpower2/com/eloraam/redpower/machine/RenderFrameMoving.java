package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.RenderCustomBlock;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderFrameMoving extends RenderCustomBlock
{
    public RenderFrameMoving(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6) {}

    public void renderInvBlock(RenderBlocks var1, int var2) {}
}
