package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.FluidClass;
import com.eloraam.redpower.core.PipeLib;
import com.eloraam.redpower.core.RenderContext;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TilePipeRenderer extends TileEntitySpecialRenderer
{
    RenderContext context = new RenderContext();

    public TilePipeRenderer()
    {
        this.context.setDefaults();
    }

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        TilePipe var9 = (TilePipe)var1;
        int var10 = var9.pipebuf.getLevel();

        if (var9.pipebuf.Type != 0 && var10 > 0)
        {
            float var11 = Math.min(1.0F, (float)var10 / (float)var9.pipebuf.getMaxLevel());
            var9.cacheCon();
            int var12 = var9.ConCache;
            FluidClass var13 = PipeLib.getLiquidClass(var9.pipebuf.Type);

            if (var13 != null)
            {
                Tessellator var14 = Tessellator.instance;
                this.bindTextureByName(var13.getTextureFile());
                int var15 = var1.worldObj.getLightBrightnessForSkyBlocks(var1.xCoord, var1.yCoord, var1.zCoord, 0);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDisable(GL11.GL_LIGHTING);
                var14.startDrawingQuads();
                this.context.setBrightness(var15);
                this.context.setPos(var2, var4, var6);
                this.context.setTex(var13.getTexture());
                float var17;
                float var16;
                float var18;

                if ((var12 & 3) > 0)
                {
                    var16 = 0.5F;
                    var17 = 0.5F;

                    if ((var12 & 1) > 0)
                    {
                        var16 = 0.0F;
                    }

                    if ((var12 & 2) > 0)
                    {
                        var17 = 1.0F;
                    }

                    var18 = 0.124F * var11;
                    this.context.renderBox(60, (double)(0.5F - var18), (double)var16, (double)(0.5F - var18), (double)(0.5F + var18), (double)var17, (double)(0.5F + var18));
                }

                if ((var12 & 12) > 0)
                {
                    var16 = 0.5F;
                    var17 = 0.5F;

                    if ((var12 & 4) > 0)
                    {
                        var16 = 0.0F;
                    }

                    if ((var12 & 8) > 0)
                    {
                        var17 = 1.0F;
                    }

                    var18 = 0.248F * var11;
                    this.context.renderBox(51, 0.37599998712539673D, 0.37599998712539673D, (double)var16, 0.6240000128746033D, (double)(0.376F + var18), (double)var17);
                }

                if ((var12 & 48) > 0)
                {
                    var16 = 0.5F;
                    var17 = 0.5F;

                    if ((var12 & 16) > 0)
                    {
                        var16 = 0.0F;
                    }

                    if ((var12 & 32) > 0)
                    {
                        var17 = 1.0F;
                    }

                    var18 = 0.248F * var11;
                    this.context.renderBox(15, (double)var16, 0.37599998712539673D, 0.37599998712539673D, (double)var17, (double)(0.376F + var18), 0.6240000128746033D);
                }

                var14.draw();
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
    }
}
