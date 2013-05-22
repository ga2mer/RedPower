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

public class RenderDiskDrive extends RenderCustomBlock
{
    RenderContext context = new RenderContext();

    public RenderDiskDrive(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileDiskDrive var7 = (TileDiskDrive)CoreLib.getTileEntity(var2, var3, var4, var5, TileDiskDrive.class);

        if (var7 != null)
        {
            this.context.setDefaults();
            this.context.readGlobalLights(var2, var3, var4, var5);
            this.context.bindTexture("/eloraam/control/control1.png");
            int var8 = var7.Active ? 27 : (var7.hasDisk ? 26 : 25);
            this.context.setTex(23, 29, 28, 28, var8, 22);
            this.context.setTexFlags(512);
            this.context.rotateTextures(var7.Rotation);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.context.setupBox();
            this.context.transform();
            this.context.renderGlobFaces(63);
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
        this.context.setTexFlags(512);
        this.context.setTex(23, 29, 28, 28, 25, 22);
        this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
        var3.startDrawingQuads();
        this.context.renderBox(62, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        var3.draw();
        this.context.useNormal = false;
        this.context.unbindTexture();
    }
}
