package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderCovers;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.Vector3;
import com.eloraam.redpower.logic.RenderLogic$TorchPos;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public abstract class RenderLogic extends RenderCovers
{
    public RenderLogic(Block var1)
    {
        super(var1);
    }

    public void renderCovers(IBlockAccess var1, TileLogic var2)
    {
        if (var2.Cover != 255)
        {
            this.context.setPos((double)var2.xCoord, (double)var2.yCoord, (double)var2.zCoord);
            this.context.readGlobalLights(var1, var2.xCoord, var2.yCoord, var2.zCoord);
            this.renderCover(var2.Rotation, var2.Cover);
        }
    }

    public TileLogic getTileEntity(IBlockAccess var1, int var2, int var3, int var4)
    {
        TileEntity var5 = var1.getBlockTileEntity(var2, var3, var4);
        return !(var5 instanceof TileLogic) ? null : (TileLogic)var5;
    }

    public void setMatrixWorld(int var1, int var2, int var3, int var4)
    {
        this.context.setOrientation(var4 >> 2, var4 & 3);
        this.context.setPos((double)var1, (double)var2, (double)var3);
    }

    public void setMatrixDisplayTick(int var1, int var2, int var3, int var4, Random var5)
    {
        float var6 = (float)var1 + 0.5F + (var5.nextFloat() - 0.5F) * 0.2F;
        float var7 = (float)var2 + 0.7F + (var5.nextFloat() - 0.5F) * 0.2F;
        float var8 = (float)var3 + 0.5F + (var5.nextFloat() - 0.5F) * 0.2F;
        this.context.setOrientation(0, var4);
        this.context.setPos((double)var6, (double)var7, (double)var8);
    }

    public void setMatrixInv()
    {
        this.context.setOrientation(0, 3);
        this.context.setPos(-0.5D, -0.5D, -0.5D);
    }

    public void renderWafer(int var1)
    {
        switch (var1 >> 8)
        {
            case 0:
                this.context.bindTexture("/eloraam/logic/logic1.png");
                break;

            case 1:
                this.context.bindTexture("/eloraam/logic/logic2.png");
                break;

            case 2:
                this.context.bindTexture("/eloraam/logic/sensor1.png");
        }

        var1 &= 255;
        this.context.setRelPos(0.0D, 0.0D, 0.0D);
        this.context.setTint(1.0F, 1.0F, 1.0F);
        this.context.setTexFlags(0);
        this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
        this.context.setTex(0, var1, 0, 0, 0, 0);
        this.context.calcBounds();
        this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
        this.context.renderFaces(62);
        this.context.unbindTexture();
    }

    public void renderInvWafer(int var1)
    {
        this.context.useNormal = true;

        switch (var1 >> 8)
        {
            case 0:
                this.context.bindTexture("/eloraam/logic/logic1.png");
                break;

            case 1:
                this.context.bindTexture("/eloraam/logic/logic2.png");
                break;

            case 2:
                this.context.bindTexture("/eloraam/logic/sensor1.png");
        }

        var1 &= 255;
        Tessellator var2 = Tessellator.instance;
        var2.startDrawingQuads();
        this.context.setTint(1.0F, 1.0F, 1.0F);
        this.context.setTexFlags(0);
        this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
        this.context.setTex(0, var1, 0, 0, 0, 0);
        this.context.calcBounds();
        this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
        this.context.renderFaces(63);
        var2.draw();
        RenderLib.setDefaultTexture();
        this.context.useNormal = false;
    }

    public void renderCover(int var1, int var2)
    {
        if (var2 != 255)
        {
            var1 >>= 2;
            var1 ^= 1;
            short[] var3 = new short[] {(short)0, (short)0, (short)0, (short)0, (short)0, (short)0};
            var3[var1] = (short)var2;
            this.context.setTint(1.0F, 1.0F, 1.0F);
            this.renderCovers(1 << var1, var3);
        }
    }

    public void renderTorch(double var1, double var3, double var5, double var7, int var9)
    {
        this.context.setTexFlags(0);
        this.context.setRelPos(var1, var3, var5);
        this.context.setTex(var9);
        this.context.setLocalLights(1.0F);
        this.context.setTint(1.0F, 1.0F, 1.0F);
        this.context.setSize(0.4375D, 1.0D - var7, 0.0D, 0.5625D, 1.0D, 1.0D);
        this.context.calcBounds();
        this.context.renderFaces(48);
        this.context.setSize(0.0D, 1.0D - var7, 0.4375D, 1.0D, 1.0D, 0.5625D);
        this.context.calcBounds();
        this.context.renderFaces(12);
        this.context.setSize(0.375D, 0.0D, 0.4375D, 0.5D, 1.0D, 0.5625D);
        this.context.setRelPos(var1 + 0.0625D, var3 - 0.375D, var5);
        this.context.calcBounds();
        this.context.setTexFlags(24);
        this.context.renderFaces(2);
        this.context.setRelPos(0.0D, 0.0D, 0.0D);
    }

    public void renderTorchPuff(World var1, String var2, double var3, double var5, double var7)
    {
        Vector3 var9 = new Vector3(var3, var5, var7);
        this.context.basis.rotate(var9);
        var9.add(this.context.globalOrigin);
        var1.spawnParticle(var2, var9.x, var9.y, var9.z, 0.0D, 0.0D, 0.0D);
    }

    public void renderChip(double var1, double var3, double var5, int var7)
    {
        this.context.bindTexture("/eloraam/logic/logic1.png");
        this.context.setTexFlags(0);
        this.context.setRelPos(var1, var3, var5);
        this.context.setTex(var7);
        this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
        this.context.renderBox(62, 0.375D, 0.0625D, 0.375D, 0.625D, 0.1875D, 0.625D);
        this.context.unbindTexture();
    }

    protected int getTorchState(TileLogic var1)
    {
        return 0;
    }

    protected int getInvTorchState(int var1)
    {
        return 0;
    }

    protected RenderLogic$TorchPos[] getTorchVectors(TileLogic var1)
    {
        return null;
    }

    protected RenderLogic$TorchPos[] getInvTorchVectors(int var1)
    {
        return null;
    }

    protected void renderWorldPart(IBlockAccess var1, TileLogic var2) {}

    protected void renderInvPart(int var1) {}

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        TileLogic var6 = (TileLogic)CoreLib.getTileEntity(var1, var2, var3, var4, TileLogic.class);

        if (var6 != null)
        {
            int var7 = this.getTorchState(var6);

            if (var7 != 0)
            {
                this.setMatrixDisplayTick(var2, var3, var4, var6.Rotation, var5);
                RenderLogic$TorchPos[] var8 = this.getTorchVectors(var6);

                if (var8 != null)
                {
                    int var9 = var5.nextInt(var8.length);

                    if ((var7 & 1 << var9) != 0)
                    {
                        this.renderTorchPuff(var1, "reddust", var8[var9].x, var8[var9].y, var8[var9].z);
                    }
                }
            }
        }
    }

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        TileLogic var7 = (TileLogic)CoreLib.getTileEntity(var2, var3, var4, var5, TileLogic.class);

        if (var7 != null)
        {
            this.renderCovers(var2, var7);
            this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
            this.setMatrixWorld(var3, var4, var5, var7.Rotation);
            this.renderWorldPart(var2, var7);
            int var8 = this.getTorchState(var7);
            RenderLogic$TorchPos[] var9 = this.getTorchVectors(var7);

            if (var9 != null)
            {
                for (int var10 = 0; var10 < var9.length; ++var10)
                {
                    this.renderTorch(var9[var10].x, var9[var10].y, var9[var10].z, var9[var10].h, (var8 & 1 << var10) > 0 ? 99 : 115);
                }
            }
        }
    }

    public void renderInvBlock(RenderBlocks var1, int var2)
    {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.setMatrixInv();
        this.renderInvPart(var2);
        GL11.glDisable(GL11.GL_LIGHTING);
        Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        int var4 = this.getInvTorchState(var2);
        RenderLogic$TorchPos[] var5 = this.getInvTorchVectors(var2);

        if (var5 != null)
        {
            for (int var6 = 0; var6 < var5.length; ++var6)
            {
                this.renderTorch(var5[var6].x, var5[var6].y, var5[var6].z, var5[var6].h, (var4 & 1 << var6) > 0 ? 99 : 115);
            }
        }

        var3.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
