package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.TubeLib;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderFrameTube extends RenderTube
{
    public RenderFrameTube(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        boolean var7 = false;
        TileFrameTube var8 = (TileFrameTube)CoreLib.getTileEntity(var2, var3, var4, var5, TileFrameTube.class);

        if (var8 != null)
        {
            this.context.setDefaults();
            this.context.setTint(1.0F, 1.0F, 1.0F);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.readGlobalLights(var2, var3, var4, var5);
            int var10;
            int var11;

            if (var8.CoverSides > 0)
            {
                short[] var9 = new short[6];

                for (var10 = 0; var10 < 6; ++var10)
                {
                    var9[var10] = var8.Covers[var10];
                    var11 = var9[var10] >> 8;

                    if (var11 == 1 || var11 == 4)
                    {
                        var9[var10] = (short)(var9[var10] - 256);
                    }
                }

                this.coverRenderer.render(var8.CoverSides, var9);
            }

            int var13 = TubeLib.getConnections(var2, var3, var4, var5);
            this.context.exactTextureCoordinates = true;
            RenderLib.bindTexture("/eloraam/machine/machine1.png", 1);
            this.context.setTex(2);
            int var14 = var8.CoverSides | var13;

            for (var10 = 0; var10 < 6; ++var10)
            {
                var11 = 1 << var10;
                byte var12 = 1;
                this.coverRenderer.start();

                if ((var14 & var11) > 0)
                {
                    if ((var8.StickySides & var11) > 0)
                    {
                        var12 = 4;
                    }
                    else
                    {
                        var12 = 2;
                    }
                }
                else
                {
                    var11 |= 1 << (var10 ^ 1);
                    this.context.setTexNum(var10 ^ 1, 1);
                }

                this.context.setTexNum(var10, var12);
                this.coverRenderer.setSize(var10, 0.0625F);
                this.context.calcBoundsGlobal();
                this.context.renderGlobFaces(var11);
            }

            this.context.exactTextureCoordinates = false;
            RenderLib.unbindTexture();
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            RenderLib.bindTexture("/eloraam/machine/machine1.png");
            this.renderCenterBlock(var13, 64, 65);

            if (var8.paintColor > 0)
            {
                var10 = this.paintColors[var8.paintColor - 1];
                this.context.setTint((float)(var10 >> 16) / 255.0F, (float)(var10 >> 8 & 255) / 255.0F, (float)(var10 & 255) / 255.0F);
                this.renderBlockPaint(var13, 66);
            }

            RenderLib.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.useNormal = true;
        this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
        RenderLib.bindTexture("/eloraam/machine/machine1.png");
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        this.context.setTex(2, 2, 1, 1, 1, 1);
        this.doubleBox(63, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.01F);
        this.context.setTex(65, 65, 64, 64, 64, 64);
        this.context.renderBox(63, 0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
        this.context.renderBox(63, 0.7400000095367432D, 0.9900000095367432D, 0.7400000095367432D, 0.25999999046325684D, 0.009999999776482582D, 0.25999999046325684D);
        var3.draw();
        RenderLib.unbindTexture();
        this.context.useNormal = false;
    }

    void doubleBox(int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8)
    {
        int var9 = var1 << 1 & 42 | var1 >> 1 & 21;
        this.context.renderBox(var1, (double)var2, (double)var3, (double)var4, (double)var5, (double)var6, (double)var7);
        this.context.renderBox(var9, (double)(var5 - var8), (double)(var6 - var8), (double)(var7 - var8), (double)(var2 + var8), (double)(var3 + var8), (double)(var4 + var8));
    }
}
