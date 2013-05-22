package com.eloraam.redpower.lighting;

import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderCustomBlock;
import com.eloraam.redpower.core.RenderLib;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderLamp extends RenderCustomBlock
{
    int[] lightColors = new int[] {16777215, 12608256, 11868853, 7308529, 12566272, 7074048, 15812213, 5460819, 9671571, 34695, 6160576, 1250240, 5187328, 558848, 10620678, 2039583};
    RenderContext context = new RenderContext();

    public RenderLamp(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        boolean var7 = ((BlockLamp)this.block).lit;
        this.context.setPos((double)var3, (double)var4, (double)var5);
        this.context.setOrientation(0, 0);
        this.context.readGlobalLights(var2, var3, var4, var5);

        if (MinecraftForgeClient.getRenderPass() != 1)
        {
            float var9 = this.block.getBlockBrightness(var2, var3, var4, var5);

            if (var7)
            {
                var9 = 1.0F;
            }

            this.context.startWorldRender(var1);
            this.context.bindTexture("/eloraam/lighting/lighting1.png");
            this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.context.setTexFlags(0);
            this.context.setupBox();
            this.context.transform();

            if (var7)
            {
                this.context.setTint(var9, var9, var9);
                this.context.setLocalLights(1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                this.context.setTex(32 + var6);
                this.context.doMappingBox(63);
                this.context.doLightLocal(63);
                this.context.renderFlat(63);
            }
            else
            {
                this.context.setTint(1.0F, 1.0F, 1.0F);
                this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
                this.context.setTex(16 + var6);
                this.context.renderGlobFaces(63);
            }

            this.context.unbindTexture();
            this.context.endWorldRender();
        }
        else if (var7)
        {
            RenderLib.bindTexture("/eloraam/lighting/lighting1.png", 1);
            int var8 = this.lightColors[var6];
            this.context.setTint((float)(var8 >> 16) / 255.0F, (float)(var8 >> 8 & 255) / 255.0F, (float)(var8 & 255) / 255.0F);
            this.context.setLocalLights(1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            this.context.setSize(-0.05D, -0.05D, -0.05D, 1.05D, 1.05D, 1.05D);
            this.context.setupBox();
            this.context.transform();
            this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.context.doMappingBox(63);
            this.context.doLightLocal(63);
            this.context.renderAlpha(63, 0.5F);
            RenderLib.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        Tessellator var3 = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        boolean var4 = ((BlockLamp)this.block).lit;
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.setTint(1.0F, 1.0F, 1.0F);
        this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);

        if (var4)
        {
            this.context.setTex(32 + var2);
        }
        else
        {
            this.context.setTex(16 + var2);
        }

        this.context.setOrientation(0, 0);
        this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        this.context.calcBounds();
        this.context.bindTexture("/eloraam/lighting/lighting1.png");
        var3.startDrawingQuads();
        this.context.useNormal = true;
        this.context.renderFaces(63);
        this.context.useNormal = false;
        var3.draw();
        this.context.unbindTexture();
    }
}
