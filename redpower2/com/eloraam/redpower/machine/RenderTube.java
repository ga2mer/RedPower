package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderCovers;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.TubeLib;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderTube extends RenderCovers
{
    int[] paintColors = new int[] {16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583};

    public RenderTube(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        boolean var7 = false;
        TileTube var8 = (TileTube)CoreLib.getTileEntity(var2, var3, var4, var5, TileTube.class);

        if (var8 != null)
        {
            this.context.exactTextureCoordinates = true;
            this.context.setTexFlags(55);
            this.context.setTint(1.0F, 1.0F, 1.0F);
            this.context.setPos((double)var3, (double)var4, (double)var5);

            if (var8.CoverSides > 0)
            {
                this.context.readGlobalLights(var2, var3, var4, var5);
                this.renderCovers(var8.CoverSides, var8.Covers);
            }

            int var10 = TubeLib.getConnections(var2, var3, var4, var5);
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            RenderLib.bindTexture("/eloraam/machine/machine1.png");

            if (var6 == 10)
            {
                this.renderCenterBlock(var10, 76, 77);
            }
            else if (var6 == 11)
            {
                if (this.renderMagFins(var10))
                {
                    this.renderCenterBlock(var10, 23, 22);
                }
                else
                {
                    this.renderCenterBlock(var10, 21, 22);
                }
            }
            else
            {
                this.renderCenterBlock(var10, 64, 65);
            }

            if (var8.paintColor > 0)
            {
                int var9 = this.paintColors[var8.paintColor - 1];
                this.context.setTint((float)(var9 >> 16) / 255.0F, (float)(var9 >> 8 & 255) / 255.0F, (float)(var9 & 255) / 255.0F);

                if (var6 == 10)
                {
                    this.renderBlockPaint(var10, 78);
                }
                else
                {
                    this.renderBlockPaint(var10, 66);
                }
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
        this.context.useNormal = true;

        if (var2 >> 8 == 10)
        {
            this.context.setTex(65, 65, 76, 76, 76, 76);
        }
        else if (var2 >> 8 == 11)
        {
            this.renderMagFins(3);
            this.context.setTex(22, 22, 21, 21, 21, 21);
        }
        else
        {
            this.context.setTex(65, 65, 64, 64, 64, 64);
        }

        this.context.renderBox(63, 0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
        this.context.renderBox(63, 0.7400000095367432D, 0.9900000095367432D, 0.7400000095367432D, 0.25999999046325684D, 0.009999999776482582D, 0.25999999046325684D);
        var3.draw();
        RenderLib.unbindTexture();
        this.context.useNormal = false;
    }

    void doubleBox(int var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        int var8 = var1 << 1 & 42 | var1 >> 1 & 21;
        this.context.renderBox(var1, (double)var2, (double)var3, (double)var4, (double)var5, (double)var6, (double)var7);
        this.context.renderBox(var8, (double)var5, (double)var6, (double)var7, (double)var2, (double)var3, (double)var4);
    }

    public boolean renderMagFins(int var1)
    {
        if (var1 == 3)
        {
            this.context.setTexFlags(0);
            this.context.setTex(25, 25, 24, 24, 24, 24);
            this.context.renderBox(63, 0.125D, 0.125D, 0.125D, 0.875D, 0.375D, 0.875D);
            this.context.renderBox(63, 0.125D, 0.625D, 0.125D, 0.875D, 0.875D, 0.875D);
            return true;
        }
        else if (var1 == 12)
        {
            this.context.setTexFlags(147492);
            this.context.setTex(24, 24, 25, 25, 24, 24);
            this.context.renderBox(63, 0.125D, 0.125D, 0.125D, 0.875D, 0.875D, 0.375D);
            this.context.renderBox(63, 0.125D, 0.125D, 0.625D, 0.875D, 0.875D, 0.875D);
            return true;
        }
        else if (var1 == 48)
        {
            this.context.setTexFlags(2304);
            this.context.setTex(24, 24, 24, 24, 25, 25);
            this.context.renderBox(63, 0.125D, 0.125D, 0.125D, 0.375D, 0.875D, 0.875D);
            this.context.renderBox(63, 0.625D, 0.125D, 0.125D, 0.875D, 0.875D, 0.875D);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void renderCenterBlock(int var1, int var2, int var3)
    {
        if (var1 == 0)
        {
            this.context.setTex(var3);
            this.doubleBox(63, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
        }
        else if (var1 == 3)
        {
            this.context.setTexFlags(1773);
            this.context.setTex(var3, var3, var2, var2, var2, var2);
            this.doubleBox(60, 0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
        }
        else if (var1 == 12)
        {
            this.context.setTexFlags(184365);
            this.context.setTex(var2, var2, var3, var3, var2, var2);
            this.doubleBox(51, 0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 1.0F);
        }
        else if (var1 == 48)
        {
            this.context.setTexFlags(187200);
            this.context.setTex(var2, var2, var2, var2, var3, var3);
            this.doubleBox(15, 0.0F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
        }
        else
        {
            this.context.setTex(var3);
            this.doubleBox(63 ^ var1, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);

            if ((var1 & 1) > 0)
            {
                this.context.setTexFlags(1773);
                this.context.setTex(var3, var3, var2, var2, var2, var2);
                this.doubleBox(60, 0.25F, 0.0F, 0.25F, 0.75F, 0.25F, 0.75F);
            }

            if ((var1 & 2) > 0)
            {
                this.context.setTexFlags(1773);
                this.context.setTex(var3, var3, var2, var2, var2, var2);
                this.doubleBox(60, 0.25F, 0.75F, 0.25F, 0.75F, 1.0F, 0.75F);
            }

            if ((var1 & 4) > 0)
            {
                this.context.setTexFlags(184365);
                this.context.setTex(var2, var2, var3, var3, var2, var2);
                this.doubleBox(51, 0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.25F);
            }

            if ((var1 & 8) > 0)
            {
                this.context.setTexFlags(184365);
                this.context.setTex(var2, var2, var3, var3, var2, var2);
                this.doubleBox(51, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F, 1.0F);
            }

            if ((var1 & 16) > 0)
            {
                this.context.setTexFlags(187200);
                this.context.setTex(var2, var2, var2, var2, var3, var3);
                this.doubleBox(15, 0.0F, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F);
            }

            if ((var1 & 32) > 0)
            {
                this.context.setTexFlags(187200);
                this.context.setTex(var2, var2, var2, var2, var3, var3);
                this.doubleBox(15, 0.75F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
            }
        }
    }

    public void renderBlockPaint(int var1, int var2)
    {
        if (var1 != 0)
        {
            if (var1 == 3)
            {
                this.context.setTexFlags(1773);
                this.context.setTex(0, 0, var2 + 1, var2 + 1, var2 + 1, var2 + 1);
                this.doubleBox(60, 0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
            }
            else if (var1 == 12)
            {
                this.context.setTexFlags(184365);
                this.context.setTex(var2 + 1, var2 + 1, 0, 0, var2 + 1, var2 + 1);
                this.doubleBox(51, 0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 1.0F);
            }
            else if (var1 == 48)
            {
                this.context.setTexFlags(187200);
                this.context.setTex(var2 + 1, var2 + 1, var2 + 1, var2 + 1, 0, 0);
                this.doubleBox(15, 0.0F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
            }
            else
            {
                this.context.setTex(var2);
                this.doubleBox(63 ^ var1, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);

                if ((var1 & 1) > 0)
                {
                    this.context.setTexFlags(1773);
                    this.context.setTex(var2, var2, var2 + 1, var2 + 1, var2 + 1, var2 + 1);
                    this.doubleBox(60, 0.25F, 0.0F, 0.25F, 0.75F, 0.25F, 0.75F);
                }

                if ((var1 & 2) > 0)
                {
                    this.context.setTexFlags(1773);
                    this.context.setTex(var2, var2, var2 + 1, var2 + 1, var2 + 1, var2 + 1);
                    this.doubleBox(60, 0.25F, 0.75F, 0.25F, 0.75F, 1.0F, 0.75F);
                }

                if ((var1 & 4) > 0)
                {
                    this.context.setTexFlags(184365);
                    this.context.setTex(var2 + 1, var2 + 1, var2, var2, var2 + 1, var2 + 1);
                    this.doubleBox(51, 0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.25F);
                }

                if ((var1 & 8) > 0)
                {
                    this.context.setTexFlags(184365);
                    this.context.setTex(var2 + 1, var2 + 1, var2, var2, var2 + 1, var2 + 1);
                    this.doubleBox(51, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F, 1.0F);
                }

                if ((var1 & 16) > 0)
                {
                    this.context.setTexFlags(187200);
                    this.context.setTex(var2 + 1, var2 + 1, var2 + 1, var2 + 1, var2, var2);
                    this.doubleBox(15, 0.0F, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F);
                }

                if ((var1 & 32) > 0)
                {
                    this.context.setTexFlags(187200);
                    this.context.setTex(var2 + 1, var2 + 1, var2 + 1, var2 + 1, var2, var2);
                    this.doubleBox(15, 0.75F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
                }
            }
        }
    }
}
