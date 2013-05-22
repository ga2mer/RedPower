package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.Matrix3;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderModel;
import com.eloraam.redpower.core.WorldCoord;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileWindTurbineRenderer extends TileEntitySpecialRenderer
{
    RenderModel modelWoodTurbine = RenderModel.loadModel("/eloraam/machine/vawt.obj");
    RenderModel modelWoodWindmill = RenderModel.loadModel("/eloraam/machine/windmill.obj");
    RenderContext context = new RenderContext();

    public TileWindTurbineRenderer()
    {
        this.modelWoodTurbine.scale(0.0625D);
        this.modelWoodWindmill.scale(0.0625D);
        this.context.setDefaults();
    }

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        if (var1 instanceof TileWindTurbine)
        {
            TileWindTurbine var9 = (TileWindTurbine)var1;

            if (var9.hasBlades)
            {
                byte var10 = var9.windmillType;
                Tessellator var11 = Tessellator.instance;
                this.bindTextureByName("/eloraam/machine/vawt.png");
                var11.startDrawingQuads();
                WorldCoord var12 = new WorldCoord(var1);
                var12.step(var9.Rotation ^ 1);
                int var13 = var1.worldObj.getLightBrightnessForSkyBlocks(var12.x, var12.y, var12.z, 0);
                var11.setBrightness(var13);
                this.context.useNormal = true;

                if (var9.hasBrakes)
                {
                    var8 = (float)((double)var8 * 0.1D);
                }

                double var14 = (double)(var8 * (float)var9.speed + (float)var9.phase);

                if (var10 == 2)
                {
                    var14 = -var14;
                }

                this.context.setOrientation(var9.Rotation, 0);
                this.context.basis = Matrix3.getRotY(-4.0E-6D * var14).multiply(this.context.basis);
                this.context.setPos(var2, var4, var6);
                this.context.setRelPos(0.5D, 0.875D, 0.5D);

                switch (var10)
                {
                    case 1:
                        this.context.bindModelOffset(this.modelWoodTurbine, 0.5D, 0.5D, 0.5D);
                        break;

                    case 2:
                        this.context.bindModelOffset(this.modelWoodWindmill, 0.5D, 0.5D, 0.5D);
                        break;

                    default:
                        return;
                }

                this.context.setTint(1.0F, 1.0F, 1.0F);
                this.context.renderModelGroup(0, 0);

                if (var10 == 1)
                {
                    this.context.setTint(1.0F, 1.0F, 1.0F);
                    this.context.renderModelGroup(1, 1);
                    this.context.renderModelGroup(1, 3);
                    this.context.renderModelGroup(1, 5);
                    this.context.setTint(1.0F, 0.1F, 0.1F);
                    this.context.renderModelGroup(1, 2);
                    this.context.renderModelGroup(1, 4);
                    this.context.renderModelGroup(1, 6);
                }
                else
                {
                    this.context.setTint(1.0F, 1.0F, 1.0F);
                    this.context.renderModelGroup(1, 1);
                    this.context.renderModelGroup(1, 3);
                    this.context.setTint(1.0F, 0.1F, 0.1F);
                    this.context.renderModelGroup(1, 2);
                    this.context.renderModelGroup(1, 4);
                }

                var11.draw();
            }
        }
    }
}
