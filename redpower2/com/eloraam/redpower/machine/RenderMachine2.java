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

public class RenderMachine2 extends RenderCustomBlock
{
    protected RenderContext context = new RenderContext();

    public RenderMachine2(Block var1)
    {
        super(var1);
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileMachine var7 = (TileMachine)CoreLib.getTileEntity(var2, var3, var4, var5, TileMachine.class);

        if (var7 != null)
        {
            this.context.setDefaults();
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.setPos((double)var3, (double)var4, (double)var5);
            this.context.readGlobalLights(var2, var3, var4, var5);
            int var8;

            if (var6 == 0)
            {
                var8 = 201 + (var7.Charged ? 1 : 0) + (var7.Active ? 2 : 0);
                int var9 = 199 + (var7.Charged ? 1 : 0);
                this.context.setTex(198, 197, var9, var9, var8, var8);
            }
            else if (var6 == 1)
            {
                var8 = 210 + (var7.Charged ? 4 : 0) + (var7.Active ? 1 : 0) + (!var7.Delay && !var7.Powered ? 0 : 2);
                this.context.setTex(209, 208, var8, var8, var8, var8);
            }

            this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.context.setupBox();
            this.context.transform();
            this.context.orientTextures(var7.Rotation);
            RenderLib.bindTexture("/eloraam/machine/machine1.png");
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
        RenderLib.bindTexture("/eloraam/machine/machine1.png");
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();

        if (var2 == 0)
        {
            this.context.setTex(198, 197, 199, 199, 201, 201);
        }
        else if (var2 == 1)
        {
            this.context.setTex(209, 208, 210, 210, 210, 210);
        }

        this.context.renderBox(63, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        var3.draw();
        RenderLib.unbindTexture();
        this.context.useNormal = false;
    }
}
