package com.eloraam.redpower.control;

import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.TileCovered;
import com.eloraam.redpower.wiring.RenderWiring;
import com.eloraam.redpower.wiring.TileWiring;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderRibbon extends RenderWiring
{
    public RenderRibbon(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        Tessellator var7 = Tessellator.instance;
        this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
        TileCovered var8 = (TileCovered)var2.getBlockTileEntity(var3, var4, var5);

        if (var8 != null)
        {
            this.context.setPos((double)var3, (double)var4, (double)var5);

            if (var8.CoverSides > 0)
            {
                this.context.setTint(1.0F, 1.0F, 1.0F);
                this.context.readGlobalLights(var2, var3, var4, var5);
                this.renderCovers(var8.CoverSides, var8.Covers);
            }

            TileWiring var9 = (TileWiring)var8;
            int var10 = var9.getConnectionMask();
            int var11 = var9.getExtConnectionMask();
            int var12 = var9.EConEMask;
            var10 |= var11;
            this.context.setTint(1.0F, 1.0F, 1.0F);
            this.setSideTex(1, 2, 1);
            this.setWireSize(0.5F, 0.0625F);
            RenderLib.bindTexture("/eloraam/control/control1.png");
            this.renderWireBlock(var9.ConSides, var10, var11, var12);
            RenderLib.unbindTexture();
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        Tessellator var3 = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        int var4 = var2 >> 8;
        var2 &= 255;
        this.context.setDefaults();
        this.context.setTexFlags(55);
        this.context.setPos(-0.5D, -0.20000000298023224D, -0.5D);
        this.setSideTex(1, 2, 1);
        this.setWireSize(0.5F, 0.0625F);
        this.context.useNormal = true;
        RenderLib.bindTexture("/eloraam/control/control1.png");
        var3.startDrawingQuads();
        this.renderSideWires(127, 0, 0);
        var3.draw();
        RenderLib.unbindTexture();
        this.context.useNormal = false;
    }
}
