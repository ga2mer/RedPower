package com.eloraam.redpower.control;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderCustomBlock;
import com.eloraam.redpower.core.RenderModel;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderIOExpander extends RenderCustomBlock
{
    RenderContext context = new RenderContext();
    protected RenderModel modelModem = RenderModel.loadModel("/eloraam/control/modem.obj");

    public RenderIOExpander(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileIOExpander var7 = (TileIOExpander)CoreLib.getTileEntity(var2, var3, var4, var5, TileIOExpander.class);

        if (var7 != null)
        {
            this.context.setDefaults();
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.setOrientation(0, var7.Rotation);
            this.context.readGlobalLights(var2, var3, var4, var5);
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            this.context.bindTexture("/eloraam/control/control1.png");
            this.context.bindModelOffset(this.modelModem, 0.5D, 0.5D, 0.5D);
            this.context.renderModelGroup(1, 1 + (CoreLib.rotToSide(var7.Rotation) & 1));
            this.context.renderModelGroup(2, 1 + (var7.WBuf & 15));
            this.context.renderModelGroup(3, 1 + (var7.WBuf >> 4 & 15));
            this.context.renderModelGroup(4, 1 + (var7.WBuf >> 8 & 15));
            this.context.renderModelGroup(5, 1 + (var7.WBuf >> 12 & 15));
            this.context.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        this.context.bindTexture("/eloraam/control/control1.png");
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        this.context.useNormal = true;
        this.context.setOrientation(0, 3);
        this.context.bindModelOffset(this.modelModem, 0.5D, 0.5D, 0.5D);
        this.context.renderModelGroup(1, 1);
        this.context.renderModelGroup(2, 1);
        this.context.renderModelGroup(3, 1);
        this.context.renderModelGroup(4, 1);
        this.context.renderModelGroup(5, 1);
        this.context.useNormal = false;
        var3.draw();
        this.context.unbindTexture();
    }
}
