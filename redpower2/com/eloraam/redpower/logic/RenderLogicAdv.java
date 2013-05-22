package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.RenderModel;
import com.eloraam.redpower.logic.RenderLogic$TorchPos;
import com.eloraam.redpower.logic.TileLogicAdv$LogicAdvXcvr;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderLogicAdv extends RenderLogic
{
    RenderModel modelXcvr = RenderModel.loadModel("/eloraam/logic/busxcvr.obj");

    public RenderLogicAdv(Block var1)
    {
        super(var1);
    }

    protected int getTorchState(TileLogic var1)
    {
        int var2 = var1.getExtendedMetadata();
        return 0;
    }

    protected int getInvTorchState(int var1)
    {
        return 0;
    }

    protected RenderLogic$TorchPos[] getTorchVectors(TileLogic var1)
    {
        int var2 = var1.getExtendedMetadata();
        return null;
    }

    protected RenderLogic$TorchPos[] getInvTorchVectors(int var1)
    {
        return null;
    }

    protected void renderWorldPart(IBlockAccess var1, TileLogic var2)
    {
        int var3 = var2.getExtendedMetadata();
        TileLogicAdv var4 = (TileLogicAdv)var2;

        switch (var3)
        {
            case 0:
                TileLogicAdv$LogicAdvXcvr var5 = (TileLogicAdv$LogicAdvXcvr)var4.getLogicStorage(TileLogicAdv$LogicAdvXcvr.class);
                this.context.bindTexture("/eloraam/logic/array1.png");
                this.context.bindModelOffset(this.modelXcvr, 0.5D, 0.5D, 0.5D);
                this.context.setTint(1.0F, 1.0F, 1.0F);
                boolean var6 = (3552867 >> var2.Rotation & 1) == 0;
                this.context.renderModelGroup(1, 1 + (var6 ? 1 : 0) + (var2.Deadmap == 0 ? 2 : 0));
                this.context.renderModelGroup(2, 1 + ((var2.PowerState & 1) > 0 ? 1 : 0) + ((var2.PowerState & 4) > 0 ? 2 : 0));

                for (int var7 = 0; var7 < 4; ++var7)
                {
                    if (var2.Deadmap == 0)
                    {
                        this.context.renderModelGroup(3 + var7, 1 + (var5.State2 >> 4 * var7 & 15));
                        this.context.renderModelGroup(7 + var7, 1 + (var5.State1 >> 4 * var7 & 15));
                    }
                    else
                    {
                        this.context.renderModelGroup(3 + var7, 1 + (var5.State1 >> 4 * var7 & 15));
                        this.context.renderModelGroup(7 + var7, 1 + (var5.State2 >> 4 * var7 & 15));
                    }
                }

                this.context.unbindTexture();
                return;

            default:
        }
    }

    protected void renderInvPart(int var1)
    {
        switch (var1)
        {
            case 1024:
                this.context.bindTexture("/eloraam/logic/array1.png");
                Tessellator var2 = Tessellator.instance;
                var2.startDrawingQuads();
                this.context.useNormal = true;
                this.context.bindModelOffset(this.modelXcvr, 0.5D, 0.5D, 0.5D);
                this.context.setTint(1.0F, 1.0F, 1.0F);
                this.context.renderModelGroup(1, 1);
                this.context.renderModelGroup(2, 1);

                for (int var3 = 0; var3 < 8; ++var3)
                {
                    this.context.renderModelGroup(3 + var3, 1);
                }

                this.context.useNormal = false;
                var2.draw();
                this.context.unbindTexture();

            default:
        }
    }
}
