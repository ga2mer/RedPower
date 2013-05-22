package com.eloraam.redpower.core;

import net.minecraft.block.Block;

public abstract class RenderCovers extends RenderCustomBlock
{
    protected static int[][] coverTextures = CoverLib.coverTextures;
    protected CoverRenderer coverRenderer;
    protected RenderContext context = new RenderContext();

    public RenderCovers(Block var1)
    {
        super(var1);
        this.coverRenderer = new CoverRenderer(this.context);
    }

    public void renderCovers(int var1, short[] var2)
    {
        this.coverRenderer.render(var1, var2);
    }
}
