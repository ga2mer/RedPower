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

public class RenderRedstoneTube extends RenderTube
{
    public RenderRedstoneTube(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        boolean var7 = false;
        TileRedstoneTube var8 = (TileRedstoneTube)CoreLib.getTileEntity(var2, var3, var4, var5, TileRedstoneTube.class);

        if (var8 != null)
        {
            this.context.setTint(1.0F, 1.0F, 1.0F);
            this.context.setPos((double)var3, (double)var4, (double)var5);

            if (var8.CoverSides > 0)
            {
                this.context.readGlobalLights(var2, var3, var4, var5);
                this.renderCovers(var8.CoverSides, var8.Covers);
            }

            int var11 = TubeLib.getConnections(var2, var3, var4, var5) | var8.getConnectionMask() >> 24;
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            int var9 = (var8.PowerState + 84) / 85;
            RenderLib.bindTexture("/eloraam/machine/machine1.png");
            this.renderCenterBlock(var11, 68 + var9, 72 + var9);

            if (var8.paintColor > 0)
            {
                int var10 = this.paintColors[var8.paintColor - 1];
                this.context.setTint((float)(var10 >> 16) / 255.0F, (float)(var10 >> 8 & 255) / 255.0F, (float)(var10 & 255) / 255.0F);
                this.renderBlockPaint(var11, 66);
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
        this.context.setTex(72, 72, 68, 68, 68, 68);
        this.context.renderBox(63, 0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
        this.context.renderBox(63, 0.7400000095367432D, 0.9900000095367432D, 0.7400000095367432D, 0.25999999046325684D, 0.009999999776482582D, 0.25999999046325684D);
        var3.draw();
        RenderLib.unbindTexture();
        this.context.useNormal = false;
    }
}
