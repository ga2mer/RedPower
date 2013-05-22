package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderCustomBlock;
import com.eloraam.redpower.core.RenderModel;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderPump extends RenderCustomBlock
{
    protected RenderModel modelBase = RenderModel.loadModel("/eloraam/machine/pump1.obj");
    protected RenderModel modelSlide = RenderModel.loadModel("/eloraam/machine/pump2.obj");
    protected RenderContext context = new RenderContext();

    public RenderPump(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TilePump var7 = (TilePump)CoreLib.getTileEntity(var2, var3, var4, var5, TilePump.class);

        if (var7 != null)
        {
            this.context.setDefaults();
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.setOrientation(0, var7.Rotation);
            this.context.readGlobalLights(var2, var3, var4, var5);
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            this.context.bindTexture("/eloraam/machine/machine1.png");
            this.context.bindModelOffset(this.modelBase, 0.5D, 0.5D, 0.5D);
            this.context.renderModelGroup(0, 0);
            this.context.renderModelGroup(1, var7.Charged ? (var7.Active ? 3 : 2) : 1);
            this.context.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.bindTexture("/eloraam/machine/machine1.png");
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        this.context.useNormal = true;
        this.context.bindModelOffset(this.modelBase, 0.5D, 0.5D, 0.5D);
        this.context.renderModelGroup(0, 0);
        this.context.renderModelGroup(1, 1);
        this.context.setRelPos(0.375D, 0.0D, 0.0D);
        this.context.bindModelOffset(this.modelSlide, 0.5D, 0.5D, 0.5D);
        this.context.renderModelGroup(0, 0);
        this.context.useNormal = false;
        var3.draw();
        this.context.unbindTexture();
    }
}
