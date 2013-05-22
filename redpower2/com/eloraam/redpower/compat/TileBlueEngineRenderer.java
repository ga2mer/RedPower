package com.eloraam.redpower.compat;

import com.eloraam.redpower.core.Matrix3;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderModel;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileBlueEngineRenderer extends TileEntitySpecialRenderer
{
    RenderContext context = new RenderContext();
    protected RenderModel modelSlide;
    protected RenderModel modelGear;

    public TileBlueEngineRenderer()
    {
        this.context.setDefaults();
        this.modelSlide = RenderModel.loadModel("/eloraam/compat/btengine2.obj");
        this.modelGear = RenderModel.loadModel("/eloraam/compat/btengine3.obj");
        this.modelSlide.scale(0.0625D);
        this.modelGear.scale(0.0625D);
    }

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        TileBlueEngine var9 = (TileBlueEngine)var1;
        Tessellator var10 = Tessellator.instance;
        this.bindTextureByName("/eloraam/compat/compat1.png");
        int var11 = var1.worldObj.getLightBrightnessForSkyBlocks(var1.xCoord, var1.yCoord, var1.zCoord, 0);
        var10.startDrawingQuads();
        var10.setBrightness(var11);

        if (var9.Active)
        {
            var8 += (float)var9.PumpTick;

            if (var9.PumpSpeed > 0)
            {
                var8 /= (float)var9.PumpSpeed;
            }
        }
        else
        {
            var8 = 0.0F;
        }

        this.context.useNormal = true;
        this.context.setPos(var2, var4, var6);
        this.context.setOrientation(var9.Rotation, 0);
        this.context.setRelPos(0.0D, 0.1875D * (0.5D - 0.5D * Math.cos(Math.PI * (double)var8)), 0.0D);
        this.context.bindModelOffset(this.modelSlide, 0.5D, 0.5D, 0.5D);
        this.context.renderModelGroup(0, 0);
        this.context.basis = Matrix3.getRotY((Math.PI / 2D) * (double)var8).multiply(this.context.basis);
        this.context.setRelPos(0.5D, 0.34375D, 0.5D);
        this.context.bindModelOffset(this.modelGear, 0.5D, 0.5D, 0.5D);
        this.context.renderModelGroup(0, 0);
        var10.draw();
    }
}
