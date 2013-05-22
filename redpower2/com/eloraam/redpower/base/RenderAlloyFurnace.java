package com.eloraam.redpower.base;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderCustomBlock;
import com.eloraam.redpower.core.RenderLib;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderAlloyFurnace extends RenderCustomBlock
{
    protected RenderContext context = new RenderContext();

    public RenderAlloyFurnace(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        TileAlloyFurnace var6 = (TileAlloyFurnace)CoreLib.getTileEntity(var1, var2, var3, var4, TileAlloyFurnace.class);

        if (var6 != null)
        {
            if (var6.Active)
            {
                float var7 = (float)var2 + 0.5F;
                float var8 = (float)var3 + 0.0F + var5.nextFloat() * 6.0F / 16.0F;
                float var9 = (float)var4 + 0.5F;
                float var10 = 0.52F;
                float var11 = var5.nextFloat() * 0.6F - 0.3F;

                switch (var6.Rotation)
                {
                    case 0:
                        var1.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
                        var1.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
                        break;

                    case 1:
                        var1.spawnParticle("smoke", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                        var1.spawnParticle("flame", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                        break;

                    case 2:
                        var1.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
                        var1.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
                        break;

                    case 3:
                        var1.spawnParticle("smoke", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                        var1.spawnParticle("flame", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileAlloyFurnace var7 = (TileAlloyFurnace)CoreLib.getTileEntity(var2, var3, var4, var5, TileAlloyFurnace.class);

        if (var7 != null)
        {
            this.context.setDefaults();
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            int var8 = var7.Active ? 18 : 17;
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.readGlobalLights(var2, var3, var4, var5);
            this.context.setTex(19, 19, var8, 16, 16, 16);
            this.context.rotateTextures(var7.Rotation);
            this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.context.setupBox();
            this.context.transform();
            RenderLib.bindTexture("/eloraam/base/base1.png");
            this.context.renderGlobFaces(63);
            RenderLib.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.useNormal = true;
        RenderLib.bindTexture("/eloraam/base/base1.png");
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        this.context.setTex(19, 19, 16, 17, 16, 16);
        this.context.renderBox(63, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        var3.draw();
        RenderLib.unbindTexture();
        this.context.useNormal = false;
    }
}
