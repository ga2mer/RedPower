package com.eloraam.redpower.machine;

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

public class RenderGrate extends RenderCustomBlock
{
    protected RenderContext context = new RenderContext();

    public RenderGrate(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileGrate var7 = (TileGrate)CoreLib.getTileEntity(var2, var3, var4, var5, TileGrate.class);

        if (var7 != null)
        {
            this.context.setDefaults();
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.readGlobalLights(var2, var3, var4, var5);
            this.context.setTex(var7.Rotation == 0 ? 45 : 43, var7.Rotation == 1 ? 45 : 43, var7.Rotation == 2 ? 45 : 44, var7.Rotation == 3 ? 45 : 44, var7.Rotation == 4 ? 45 : 44, var7.Rotation == 5 ? 45 : 44);
            RenderLib.bindTexture("/eloraam/machine/machine1.png");
            this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.context.setupBox();
            this.context.transform();
            this.context.renderGlobFaces(63);
            this.context.setTex(var7.Rotation == 1 ? 46 : 43, var7.Rotation == 0 ? 46 : 43, var7.Rotation == 3 ? 46 : 43, var7.Rotation == 2 ? 46 : 43, var7.Rotation == 5 ? 46 : 43, var7.Rotation == 4 ? 46 : 43);
            this.context.setLocalLights(0.3F);
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            this.context.renderBox(63, 0.99D, 0.99D, 0.99D, 0.01D, 0.01D, 0.01D);
            RenderLib.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.useNormal = true;
        RenderLib.bindTexture("/eloraam/machine/machine1.png");
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        this.context.setTex(43, 45, 44, 44, 44, 44);
        this.context.doubleBox(63, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 0.01D);
        var3.draw();
        RenderLib.unbindTexture();
        this.context.useNormal = false;
    }
}
