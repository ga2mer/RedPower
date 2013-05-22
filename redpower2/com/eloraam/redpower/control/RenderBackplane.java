package com.eloraam.redpower.control;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderCustomBlock;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderBackplane extends RenderCustomBlock
{
    RenderContext context = new RenderContext();

    public RenderBackplane(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileBackplane var7 = (TileBackplane)CoreLib.getTileEntity(var2, var3, var4, var5, TileBackplane.class);

        if (var7 != null)
        {
            this.context.setDefaults();
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            this.context.bindTexture("/eloraam/control/control1.png");
            this.context.setOrientation(0, var7.Rotation);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);

            if (var6 == 0)
            {
                this.context.setTex(0, 18, 17, 17, 16, 16);
                this.context.renderBox(62, 0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
            }
            else if (var6 == 1)
            {
                this.context.setTex(0, 34, 33, 33, 32, 32);
                this.context.renderBox(62, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            }

            this.context.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        Tessellator var3 = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.useNormal = true;
        this.context.setOrientation(0, 3);
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.bindTexture("/eloraam/control/control1.png");
        this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
        var3.startDrawingQuads();

        if (var2 == 0)
        {
            this.context.setTex(0, 18, 17, 17, 16, 16);
            this.context.renderBox(62, 0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
        }
        else if (var2 == 1)
        {
            this.context.setTex(0, 34, 33, 33, 32, 32);
            this.context.renderBox(62, 0.0D, 0.125D, 0.0D, 1.0D, 1.0D, 1.0D);
        }

        var3.draw();
        this.context.useNormal = false;
        this.context.unbindTexture();
    }
}
