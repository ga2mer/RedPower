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

public class RenderAccel extends RenderCustomBlock
{
    protected RenderModel model = RenderModel.loadModel("/eloraam/machine/accel.obj");
    protected RenderContext context = new RenderContext();

    public RenderAccel(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileAccel var7 = (TileAccel)CoreLib.getTileEntity(var2, var3, var4, var5, TileAccel.class);

        if (var7 != null)
        {
            this.context.setDefaults();
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.setOrientation(var7.Rotation, 0);
            this.context.readGlobalLights(var2, var3, var4, var5);

            if (var7.Charged)
            {
                this.context.setBrightness(15728880);
            }
            else
            {
                this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            }

            this.context.bindTexture("/eloraam/machine/machine1.png");
            this.context.bindModelOffset(this.model, 0.5D, 0.5D, 0.5D);
            this.context.renderModelGroup(0, 0);
            this.context.renderModelGroup(1, 1 + (var7.Charged ? 1 : 0));

            if (var7.Charged)
            {
                this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            }

            var7.recache();

            if ((var7.conCache & 1) > 0)
            {
                this.context.renderModelGroup(2, 2);
            }

            if ((var7.conCache & 2) > 0)
            {
                this.context.renderModelGroup(2, 1);
            }

            if ((var7.conCache & 4) > 0)
            {
                this.context.renderModelGroup(3, 2);
            }

            if ((var7.conCache & 8) > 0)
            {
                this.context.renderModelGroup(3, 1);
            }

            this.context.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.setOrientation(2, 0);
        this.context.bindTexture("/eloraam/machine/machine1.png");
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        this.context.useNormal = true;
        this.context.bindModelOffset(this.model, 0.5D, 0.5D, 0.5D);
        this.context.renderModelGroup(0, 0);
        this.context.renderModelGroup(1, 1);
        this.context.useNormal = false;
        var3.draw();
        this.context.unbindTexture();
    }
}
