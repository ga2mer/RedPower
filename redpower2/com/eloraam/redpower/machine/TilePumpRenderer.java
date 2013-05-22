package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderModel;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TilePumpRenderer extends TileEntitySpecialRenderer
{
    RenderContext context = new RenderContext();
    protected RenderModel modelSlide;

    public TilePumpRenderer()
    {
        this.context.setDefaults();
        this.modelSlide = RenderModel.loadModel("/eloraam/machine/pump2.obj");
    }

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        TilePump var9 = (TilePump)var1;
        Tessellator var10 = Tessellator.instance;
        this.bindTextureByName("/eloraam/machine/machine1.png");
        int var11 = var1.worldObj.getLightBrightnessForSkyBlocks(var1.xCoord, var1.yCoord, var1.zCoord, 0);
        var10.startDrawingQuads();
        var10.setBrightness(var11);

        if (var9.Active)
        {
            var8 += (float)var9.PumpTick;

            if (var8 > 8.0F)
            {
                var8 = 16.0F - var8;
            }

            var8 = (float)((double)var8 / 8.0D);
        }
        else
        {
            var8 = 0.0F;
        }

        this.context.useNormal = true;
        this.context.setPos(var2, var4, var6);
        this.context.setOrientation(0, var9.Rotation);
        this.context.setRelPos(0.375D + 0.3125D * (double)var8, 0.0D, 0.0D);
        this.context.bindModelOffset(this.modelSlide, 0.5D, 0.5D, 0.5D);
        this.context.renderModelGroup(0, 0);
        var10.draw();
    }
}
