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

public class RenderMachine extends RenderCustomBlock
{
    protected RenderContext context = new RenderContext();

    public RenderMachine(Block var1)
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
                var8 = var7.Active ? 1 : 0;
                this.context.setTex(48, 53 + var8, 56, 56, 55, 55);
            }
            else
            {
                int var9;

                if (var6 == 4)
                {
                    var8 = 96 + (var7.Active ? 1 : 0);
                    var9 = var8 + 2 + (var7.Powered ? 2 : 0);
                    this.context.setTex(102, 103, var8, var8, var9, var9);
                }
                else if (var6 == 5)
                {
                    var8 = var7.Charged ? (var7.Active ? 2 : 1) : 0;
                    var9 = 116 + (var7.Charged ? 1 : 0) + (var7.Active ? 2 : 0);
                    this.context.setTex(113 + var8, 112, var9, var9, var9, var9);
                }
                else if (var6 == 8)
                {
                    var8 = 120 + (var7.Charged ? 1 : 0) + (var7.Delay | var7.Active ? 2 : 0);
                    this.context.setTex(124, 125, var8, var8, var8, var8);
                }
                else if (var6 == 10)
                {
                    var8 = 104 + (var7.Active ? 1 : 0);
                    var9 = var8 + 2 + (var7.Powered ? 2 : 0);
                    this.context.setTex(102, 103, var8, var8, var9, var9);
                }
                else if (var6 == 12)
                {
                    var8 = var7.Active ? 1 : 0;
                    this.context.setTex(48, 164 + var8, 167, 167, 166, 166);
                }
                else if (var6 == 13)
                {
                    var8 = var7.Active ? 1 : 0;
                    this.context.setTex(172 + var8, 168 + var8, 171, 171, 170, 170);
                }
                else if (var6 == 14)
                {
                    var8 = var7.Active ? 1 : 0;
                    this.context.setTex(58, 89, 91 + var8, 91 + var8, 90, 90);
                }
                else if (var6 == 15)
                {
                    var8 = var7.Active ? 1 : 0;
                    this.context.setTex(58, 89, 93 + var8, 93 + var8, 90, 90);
                }
                else
                {
                    var8 = 59 + (var7.Active ? 1 : 0) + (var6 == 3 ? 2 : 0);
                    this.context.setTex(58, 57, var8, var8, var8, var8);
                }
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
            this.context.setTex(48, 53, 56, 56, 55, 55);
        }
        else if (var2 == 2)
        {
            this.context.setTex(58, 57, 59, 59, 59, 59);
        }
        else if (var2 == 4)
        {
            this.context.setTex(102, 103, 98, 98, 96, 96);
        }
        else if (var2 == 5)
        {
            this.context.setTex(113, 112, 117, 117, 117, 117);
        }
        else if (var2 == 8)
        {
            this.context.setTex(124, 125, 120, 120, 120, 120);
        }
        else if (var2 == 10)
        {
            this.context.setTex(102, 103, 106, 106, 104, 104);
        }
        else if (var2 == 12)
        {
            this.context.setTex(48, 164, 167, 167, 166, 166);
        }
        else if (var2 == 13)
        {
            this.context.setTex(172, 168, 171, 171, 170, 170);
        }
        else if (var2 == 14)
        {
            this.context.setTex(58, 89, 91, 91, 90, 90);
        }
        else if (var2 == 15)
        {
            this.context.setTex(58, 89, 93, 93, 90, 90);
        }
        else
        {
            this.context.setTex(58, 57, 61, 61, 61, 61);
        }

        this.context.renderBox(63, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        var3.draw();
        RenderLib.unbindTexture();
        this.context.useNormal = false;
    }
}
