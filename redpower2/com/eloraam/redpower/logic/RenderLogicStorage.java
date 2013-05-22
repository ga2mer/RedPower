package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.MathLib;
import com.eloraam.redpower.core.Quat;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.Vector3;
import com.eloraam.redpower.logic.RenderLogic$TorchPos;
import com.eloraam.redpower.logic.TileLogicStorage$LogicStorageCounter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderLogicStorage extends RenderLogic
{
    private static RenderLogic$TorchPos[] torchMapCounter = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.0D, 0.125D, 0.188D, 1.0D), new RenderLogic$TorchPos(0.3D, -0.3D, 0.0D, 0.6000000238418579D), new RenderLogic$TorchPos(-0.3D, -0.3D, 0.0D, 0.6000000238418579D)};

    public RenderLogicStorage(Block var1)
    {
        super(var1);
    }

    protected int getTorchState(TileLogic var1)
    {
        TileLogicStorage var2 = (TileLogicStorage)var1;
        int var3 = var1.getExtendedMetadata();

        switch (var3)
        {
            case 0:
                TileLogicStorage$LogicStorageCounter var5 = (TileLogicStorage$LogicStorageCounter)var2.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);
                return 1 | (var5.Count == var5.CountMax ? 2 : 0) | (var5.Count == 0 ? 4 : 0);

            default:
                return 0;
        }
    }

    protected int getInvTorchState(int var1)
    {
        switch (var1)
        {
            case 768:
                return 5;

            default:
                return 0;
        }
    }

    protected RenderLogic$TorchPos[] getTorchVectors(TileLogic var1)
    {
        int var2 = var1.getExtendedMetadata();

        switch (var2)
        {
            case 0:
                return torchMapCounter;

            default:
                return null;
        }
    }

    protected RenderLogic$TorchPos[] getInvTorchVectors(int var1)
    {
        switch (var1)
        {
            case 768:
                return torchMapCounter;

            default:
                return null;
        }
    }

    protected void renderWorldPart(IBlockAccess var1, TileLogic var2)
    {
        int var3 = var2.getExtendedMetadata();
        TileLogicStorage var4 = (TileLogicStorage)var2;

        switch (var3)
        {
            case 0:
                int var5 = 224 + (var2.Deadmap > 0 ? 4 : 0) + (var2.PowerState & 1) + ((var2.PowerState & 4) >> 1);
                this.renderWafer(var5);

                if (var3 == 0)
                {
                    TileLogicStorage$LogicStorageCounter var7 = (TileLogicStorage$LogicStorageCounter)var4.getLogicStorage(TileLogicStorage$LogicStorageCounter.class);

                    if (var7.CountMax == 0)
                    {
                        var7.CountMax = 1;
                    }

                    float var8 = 0.58F + 0.34F * ((float)var7.Count / (float)var7.CountMax);
                    Vector3 var9 = new Vector3(0.0D, -0.1D, 0.188D);
                    this.context.basis.rotate(var9);
                    var9.add(this.context.globalOrigin);
                    var9.add(0.5D, 0.5D, 0.5D);
                    Quat var10 = Quat.aroundAxis(0.0D, 1.0D, 0.0D, (double)(-var8) * Math.PI * 2.0D);
                    var10.multiply(MathLib.orientQuat(var2.Rotation >> 2, var2.Rotation & 3));
                    RenderLib.renderPointer(var9, var10);
                }

                return;

            default:
        }
    }

    protected void renderInvPart(int var1)
    {
        switch (var1)
        {
            case 768:
                this.renderInvWafer(224);

            default:
                if (var1 == 768)
                {
                    Tessellator var2 = Tessellator.instance;
                    var2.startDrawingQuads();
                    var2.setNormal(0.0F, 0.0F, 1.0F);
                    Vector3 var3 = new Vector3(0.0D, -0.1D, 0.188D);
                    Quat var4 = Quat.aroundAxis(0.0D, 1.0D, 0.0D, 3.64424747816416D);
                    this.context.basis.rotate(var3);
                    var4.multiply(MathLib.orientQuat(0, 1));
                    RenderLib.renderPointer(var3, var4);
                    var2.draw();
                }
        }
    }
}
