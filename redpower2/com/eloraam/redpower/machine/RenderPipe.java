package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.PipeLib;
import com.eloraam.redpower.core.RenderCovers;
import com.eloraam.redpower.core.RenderLib;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderPipe extends RenderCovers
{
    public RenderPipe(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        boolean var7 = false;
        TilePipe var8 = (TilePipe)CoreLib.getTileEntity(var2, var3, var4, var5, TilePipe.class);

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

            int var9 = PipeLib.getConnections(var2, var3, var4, var5);
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            RenderLib.bindTexture("/eloraam/machine/machine1.png");
            this.renderCenterBlock(var9, 26, 28);
            var8.cacheFlange();
            this.renderFlanges(var8.Flanges, 27);
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
        this.context.setTex(28, 28, 26, 26, 26, 26);
        this.context.renderBox(60, 0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
        this.context.renderBox(60, 0.6240000128746033D, 0.9990000128746033D, 0.6240000128746033D, 0.37599998712539673D, 0.0010000000474974513D, 0.37599998712539673D);
        this.renderFlanges(3, 27);
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

    public void renderFlanges(int var1, int var2)
    {
        this.context.setTex(var2);

        if ((var1 & 1) > 0)
        {
            this.context.setTexFlags(0);
            this.context.renderBox(63, 0.25D, 0.0D, 0.25D, 0.75D, 0.125D, 0.75D);
        }

        if ((var1 & 2) > 0)
        {
            this.context.setTexFlags(112320);
            this.context.renderBox(63, 0.25D, 0.875D, 0.25D, 0.75D, 1.0D, 0.75D);
        }

        if ((var1 & 4) > 0)
        {
            this.context.setTexFlags(217134);
            this.context.renderBox(63, 0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.125D);
        }

        if ((var1 & 8) > 0)
        {
            this.context.setTexFlags(188469);
            this.context.renderBox(63, 0.25D, 0.25D, 0.875D, 0.75D, 0.75D, 1.0D);
        }

        if ((var1 & 16) > 0)
        {
            this.context.setTexFlags(2944);
            this.context.renderBox(63, 0.0D, 0.25D, 0.25D, 0.125D, 0.75D, 0.75D);
        }

        if ((var1 & 32) > 0)
        {
            this.context.setTexFlags(3419);
            this.context.renderBox(63, 0.875D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
        }
    }

    public void renderCenterBlock(int var1, int var2, int var3)
    {
        if (var1 == 0)
        {
            this.context.setTex(var3);
            this.doubleBox(63, 0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F);
        }
        else if (var1 == 3)
        {
            this.context.setTexFlags(1773);
            this.context.setTex(var3, var3, var2, var2, var2, var2);
            this.doubleBox(60, 0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
        }
        else if (var1 == 12)
        {
            this.context.setTexFlags(184365);
            this.context.setTex(var2, var2, var3, var3, var2, var2);
            this.doubleBox(51, 0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);
        }
        else if (var1 == 48)
        {
            this.context.setTexFlags(187200);
            this.context.setTex(var2, var2, var2, var2, var3, var3);
            this.doubleBox(15, 0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
        }
        else
        {
            this.context.setTex(var3);
            this.doubleBox(63 ^ var1, 0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F);

            if ((var1 & 1) > 0)
            {
                this.context.setTexFlags(1773);
                this.context.setTex(var3, var3, var2, var2, var2, var2);
                this.doubleBox(60, 0.375F, 0.0F, 0.375F, 0.625F, 0.375F, 0.625F);
            }

            if ((var1 & 2) > 0)
            {
                this.context.setTexFlags(1773);
                this.context.setTex(var3, var3, var2, var2, var2, var2);
                this.doubleBox(60, 0.375F, 0.625F, 0.375F, 0.625F, 1.0F, 0.625F);
            }

            if ((var1 & 4) > 0)
            {
                this.context.setTexFlags(184365);
                this.context.setTex(var2, var2, var3, var3, var2, var2);
                this.doubleBox(51, 0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 0.375F);
            }

            if ((var1 & 8) > 0)
            {
                this.context.setTexFlags(184365);
                this.context.setTex(var2, var2, var3, var3, var2, var2);
                this.doubleBox(51, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F, 1.0F);
            }

            if ((var1 & 16) > 0)
            {
                this.context.setTexFlags(187200);
                this.context.setTex(var2, var2, var2, var2, var3, var3);
                this.doubleBox(15, 0.0F, 0.375F, 0.375F, 0.375F, 0.625F, 0.625F);
            }

            if ((var1 & 32) > 0)
            {
                this.context.setTexFlags(187200);
                this.context.setTex(var2, var2, var2, var2, var3, var3);
                this.doubleBox(15, 0.625F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
            }
        }
    }
}
