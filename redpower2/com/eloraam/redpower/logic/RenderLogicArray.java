package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.RenderModel;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.logic.RenderLogic$TorchPos;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderLogicArray extends RenderLogic
{
    RenderModel model = RenderModel.loadModel("/eloraam/logic/arraycells.obj");
    private static RenderLogic$TorchPos[] torchMapInvert = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.0D, -0.25D, 0.0D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapNonInv = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.0D, -0.25D, 0.0D, 0.7D), new RenderLogic$TorchPos(-0.188D, -0.25D, 0.219D, 0.7D)};

    public RenderLogicArray(Block var1)
    {
        super(var1);
    }

    protected int getTorchState(TileLogic var1)
    {
        int var2 = var1.getExtendedMetadata();

        switch (var2)
        {
            case 1:
                return var1.Powered ? 1 : 0;

            case 2:
                return var1.Powered ? 1 : 2;

            default:
                return 0;
        }
    }

    protected int getInvTorchState(int var1)
    {
        return var1 == 514 ? 2 : 0;
    }

    protected RenderLogic$TorchPos[] getTorchVectors(TileLogic var1)
    {
        int var2 = var1.getExtendedMetadata();

        switch (var2)
        {
            case 1:
                return torchMapInvert;

            case 2:
                return torchMapNonInv;

            default:
                return null;
        }
    }

    protected RenderLogic$TorchPos[] getInvTorchVectors(int var1)
    {
        switch (var1)
        {
            case 513:
                return torchMapInvert;

            case 514:
                return torchMapNonInv;

            default:
                return null;
        }
    }

    public static int getFacingDir(int var0, int var1)
    {
        short var2;

        switch (var0 >> 2)
        {
            case 0:
                var2 = 13604;
                break;

            case 1:
                var2 = 13349;
                break;

            case 2:
                var2 = 20800;
                break;

            case 3:
                var2 = 16720;
                break;

            case 4:
                var2 = 8496;
                break;

            default:
                var2 = 12576;
        }

        int var3 = var2 >> ((var0 + var1 & 3) << 2);
        var3 &= 7;
        return var3;
    }

    private boolean isArrayTopwire(IBlockAccess var1, WorldCoord var2, int var3, int var4)
    {
        var2 = var2.coordStep(var4);
        TileLogicArray var5 = (TileLogicArray)CoreLib.getTileEntity(var1, var2, TileLogicArray.class);

        if (var5 == null)
        {
            return false;
        }
        else
        {
            int var6 = var5.getTopwireMask();
            var6 &= RedPowerLib.getConDirMask(var4);
            var6 = (var6 & 1431655765) << 1 | (var6 & 715827882) >> 1;
            var6 &= var3;
            return var6 > 0;
        }
    }

    protected void renderWorldPart(IBlockAccess var1, TileLogic var2)
    {
        if (var2 instanceof TileLogicArray)
        {
            TileLogicArray var3 = (TileLogicArray)var2;
            int var4 = var2.getExtendedMetadata();
            this.context.bindTexture("/eloraam/logic/array1.png");
            this.context.bindModelOffset(this.model, 0.5D, 0.5D, 0.5D);
            this.context.setTint(1.0F, 1.0F, 1.0F);
            this.context.renderModelGroup(0, 0);

            switch (var4)
            {
                case 0:
                    this.context.renderModelGroup(1, 1);
                    this.context.setTint(0.3F + 0.7F * ((float)var3.PowerVal1 / 255.0F), 0.0F, 0.0F);
                    this.context.renderModelGroup(2, 1);
                    this.context.setTint(0.3F + 0.7F * ((float)var3.PowerVal2 / 255.0F), 0.0F, 0.0F);
                    this.context.renderModelGroup(3, 1);
                    break;

                case 1:
                    this.context.renderModelGroup(1, 2 + (var3.PowerVal1 > 0 ? 1 : 0));
                    this.context.renderModelGroup(5, 0);
                    this.context.setTint(0.3F + 0.7F * ((float)var3.PowerVal1 / 255.0F), 0.0F, 0.0F);
                    this.context.renderModelGroup(2, 2);
                    this.context.setTint(0.3F + 0.7F * ((float)var3.PowerVal2 / 255.0F), 0.0F, 0.0F);
                    this.context.renderModelGroup(3, 2);
                    break;

                case 2:
                    this.context.renderModelGroup(1, 4 + (var3.PowerVal1 > 0 ? 1 : 0) + (var3.Powered ? 0 : 2));
                    this.context.renderModelGroup(5, 0);
                    this.context.setTint(0.3F + 0.7F * ((float)var3.PowerVal1 / 255.0F), 0.0F, 0.0F);
                    this.context.renderModelGroup(2, 2);
                    this.context.setTint(0.3F + 0.7F * ((float)var3.PowerVal2 / 255.0F), 0.0F, 0.0F);
                    this.context.renderModelGroup(3, 2);
            }

            int var5 = getFacingDir(var3.Rotation, 1);
            int var6 = var3.getTopwireMask();
            WorldCoord var7 = new WorldCoord(var2);
            this.context.renderModelGroup(4, (this.isArrayTopwire(var1, var7, var6, var5) ? 0 : 1) + (this.isArrayTopwire(var1, var7, var6, var5 ^ 1) ? 0 : 2));
            this.context.unbindTexture();
        }
    }

    protected void renderInvPart(int var1)
    {
        this.context.bindTexture("/eloraam/logic/array1.png");
        Tessellator var2 = Tessellator.instance;
        var2.startDrawingQuads();
        this.context.useNormal = true;
        this.context.bindModelOffset(this.model, 0.5D, 0.5D, 0.5D);
        this.context.setTint(1.0F, 1.0F, 1.0F);
        this.context.renderModelGroup(0, 0);

        switch (var1)
        {
            case 512:
                this.context.renderModelGroup(1, 1);
                this.context.setTint(0.3F, 0.0F, 0.0F);
                this.context.renderModelGroup(2, 1);
                this.context.renderModelGroup(3, 1);
                this.context.renderModelGroup(4, 3);
                break;

            case 513:
                this.context.renderModelGroup(1, 2);
                this.context.renderModelGroup(5, 0);
                this.context.setTint(0.3F, 0.0F, 0.0F);
                this.context.renderModelGroup(2, 2);
                this.context.renderModelGroup(3, 2);
                this.context.renderModelGroup(4, 3);
                break;

            case 514:
                this.context.renderModelGroup(1, 6);
                this.context.renderModelGroup(5, 0);
                this.context.setTint(0.3F, 0.0F, 0.0F);
                this.context.renderModelGroup(2, 2);
                this.context.renderModelGroup(3, 2);
                this.context.renderModelGroup(4, 3);
        }

        this.context.useNormal = false;
        var2.draw();
        this.context.unbindTexture();
    }
}
