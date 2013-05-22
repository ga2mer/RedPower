package com.eloraam.redpower.lighting;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderCustomBlock;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.RenderModel;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderShapedLamp extends RenderCustomBlock
{
    int[] lightColors = new int[] {16777215, 12608256, 11868853, 7308529, 12566272, 7074048, 15812213, 5460819, 9671571, 34695, 6160576, 1250240, 5187328, 558848, 10620678, 2039583};
    int[] lightColorsOff = new int[16];
    RenderContext context = new RenderContext();
    protected RenderModel modelLamp1 = RenderModel.loadModel("/eloraam/lighting/shlamp1.obj");
    protected RenderModel modelLamp2 = RenderModel.loadModel("/eloraam/lighting/shlamp2.obj");

    public RenderShapedLamp(Block var1)
    {
        super(var1);

        for (int var2 = 0; var2 < 16; ++var2)
        {
            int var3 = this.lightColors[var2] & 255;
            int var4 = this.lightColors[var2] >> 8 & 255;
            int var5 = this.lightColors[var2] >> 16 & 255;
            int var6 = (var3 + var4 + var5) / 3;
            var3 = (var3 + 2 * var6) / 5;
            var4 = (var4 + 2 * var6) / 5;
            var5 = (var5 + 2 * var6) / 5;
            this.lightColorsOff[var2] = var3 | var4 << 8 | var5 << 16;
        }
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileShapedLamp var7 = (TileShapedLamp)CoreLib.getTileEntity(var2, var3, var4, var5, TileShapedLamp.class);

        if (var7 != null)
        {
            boolean var8 = var7.Powered != var7.Inverted;
            this.context.setDefaults();
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.setOrientation(var7.Rotation, 0);
            this.context.readGlobalLights(var2, var3, var4, var5);

            switch (var7.Style)
            {
                case 0:
                    this.context.bindModelOffset(this.modelLamp1, 0.5D, 0.5D, 0.5D);
                    break;

                case 1:
                    this.context.bindModelOffset(this.modelLamp2, 0.5D, 0.5D, 0.5D);
            }

            int var9;

            if (MinecraftForgeClient.getRenderPass() != 1)
            {
                var9 = this.block.getMixedBrightnessForBlock(var2, var3, var4, var5);
                this.context.bindTexture("/eloraam/lighting/lighting1.png");
                this.context.setBrightness(var9);
                this.context.renderModelGroup(0, 0);

                if (var8)
                {
                    this.context.setTintHex(this.lightColors[var7.Color & 15]);
                    this.context.setBrightness(15728880);
                }
                else
                {
                    this.context.setTintHex(this.lightColorsOff[var7.Color & 15]);
                }

                this.context.renderModelGroup(1, 0);
                this.context.unbindTexture();
            }
            else if (var8)
            {
                RenderLib.bindTexture("/eloraam/lighting/lighting1.png", 1);
                var9 = this.lightColors[var7.Color & 15];
                this.context.setTint((float)(var9 >> 16) / 255.0F, (float)(var9 >> 8 & 255) / 255.0F, (float)(var9 & 255) / 255.0F);
                this.context.setAlpha(0.3F);
                this.context.renderModelGroup(2, 0);
                RenderLib.unbindTexture();
            }
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        Tessellator var3 = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        boolean var4 = false;
        this.context.setDefaults();
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.bindTexture("/eloraam/lighting/lighting1.png");
        var3.startDrawingQuads();
        this.context.useNormal = true;

        switch (var2 >> 5)
        {
            case 0:
                this.context.bindModelOffset(this.modelLamp1, 0.5D, 0.5D, 0.5D);
                break;

            case 1:
                this.context.bindModelOffset(this.modelLamp2, 0.5D, 0.5D, 0.5D);
        }

        this.context.renderModelGroup(0, 0);

        if ((var2 & 16) > 0)
        {
            this.context.setTintHex(this.lightColors[var2 & 15]);
        }
        else
        {
            this.context.setTintHex(this.lightColorsOff[var2 & 15]);
        }

        this.context.renderModelGroup(1, 0);
        this.context.useNormal = false;
        var3.draw();
        this.context.unbindTexture();
    }
}
