package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderCovers;
import com.eloraam.redpower.core.RenderLib;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderFrame extends RenderCovers
{
    public RenderFrame(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        boolean var7 = false;
        TileFrame var8 = (TileFrame)CoreLib.getTileEntity(var2, var3, var4, var5, TileFrame.class);

        if (var8 != null)
        {
            this.context.setDefaults();
            this.context.setTint(1.0F, 1.0F, 1.0F);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.readGlobalLights(var2, var3, var4, var5);
            int var10;

            if (var8.CoverSides > 0)
            {
                short[] var9 = new short[6];

                for (var10 = 0; var10 < 6; ++var10)
                {
                    var9[var10] = var8.Covers[var10];
                    int var11 = var9[var10] >> 8;

                    if (var11 == 1 || var11 == 4)
                    {
                        var9[var10] = (short)(var9[var10] - 256);
                    }
                }

                this.coverRenderer.render(var8.CoverSides, var9);
            }

            this.context.exactTextureCoordinates = true;
            RenderLib.bindTexture("/eloraam/machine/machine1.png", 1);
            this.context.setTex(2);

            for (int var12 = 0; var12 < 6; ++var12)
            {
                var10 = 1 << var12;
                byte var13 = 1;
                this.coverRenderer.start();

                if ((var8.CoverSides & var10) > 0)
                {
                    if ((var8.StickySides & var10) > 0)
                    {
                        var13 = 4;
                    }
                    else
                    {
                        var13 = 2;
                    }
                }
                else
                {
                    var10 |= 1 << (var12 ^ 1);
                    this.context.setTexNum(var12 ^ 1, 1);
                }

                this.context.setTexNum(var12, var13);
                this.coverRenderer.setSize(var12, 0.0625F);
                this.context.calcBoundsGlobal();
                this.context.renderGlobFaces(var10);
            }

            this.context.exactTextureCoordinates = false;
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
        this.context.setTex(1);
        this.doubleBox(63, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.01F);
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
