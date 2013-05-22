package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.Quat;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.Vector3;
import com.eloraam.redpower.logic.RenderLogic$TorchPos;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderLogicPointer extends RenderLogic
{
    private static RenderLogic$TorchPos[] torchMapSequencer = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.0D, 0.125D, 0.0D, 1.0D), new RenderLogic$TorchPos(0.0D, -0.3D, 0.3D, 0.6D), new RenderLogic$TorchPos(-0.3D, -0.3D, 0.0D, 0.6D), new RenderLogic$TorchPos(0.0D, -0.3D, -0.3D, 0.6D), new RenderLogic$TorchPos(0.3D, -0.3D, 0.0D, 0.6D)};
    private static RenderLogic$TorchPos[] torchMapTimer = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.0D, 0.125D, 0.0D, 1.0D), new RenderLogic$TorchPos(0.3D, -0.3D, 0.0D, 0.6D)};
    private static RenderLogic$TorchPos[] torchMapStateCell = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.0D, 0.125D, 0.25D, 1.0D), new RenderLogic$TorchPos(0.281D, -0.3D, 0.156D, 0.6D)};
    private static RenderLogic$TorchPos[] torchMapStateCell2 = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.0D, 0.125D, -0.25D, 1.0D), new RenderLogic$TorchPos(0.281D, -0.3D, -0.156D, 0.6D)};

    public RenderLogicPointer(Block var1)
    {
        super(var1);
    }

    protected int getTorchState(TileLogic var1)
    {
        int var2 = var1.getExtendedMetadata();

        switch (var2)
        {
            case 0:
                return (var1.Disabled ? 0 : 1) | (var1.Powered && !var1.Disabled ? 2 : 0);

            case 1:
                return 1 | 2 << var1.PowerState & 31;

            case 2:
                return (var1.Active && !var1.Powered && !var1.Disabled ? 1 : 0) | (var1.Active && var1.Powered ? 2 : 0);

            default:
                return 0;
        }
    }

    protected int getInvTorchState(int var1)
    {
        switch (var1)
        {
            case 0:
                return 1;

            case 1:
                return 5;

            case 2:
                return 0;

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
                return torchMapTimer;

            case 1:
                return torchMapSequencer;

            case 2:
                if (var1.Deadmap > 0)
                {
                    return torchMapStateCell2;
                }

                return torchMapStateCell;

            default:
                return null;
        }
    }

    protected RenderLogic$TorchPos[] getInvTorchVectors(int var1)
    {
        switch (var1)
        {
            case 0:
                return torchMapTimer;

            case 1:
                return torchMapSequencer;

            case 2:
                return torchMapStateCell;

            default:
                return null;
        }
    }

    protected void renderWorldPart(IBlockAccess var1, TileLogic var2)
    {
        int var3 = var2.getExtendedMetadata();
        int var4;

        switch (var3)
        {
            case 0:
                var4 = 16 + (var2.PowerState | (var2.Powered ? 5 : 0));
                break;

            case 1:
                if (var2.Deadmap == 1)
                {
                    var4 = 4;
                }
                else
                {
                    var4 = 3;
                }

                break;

            case 2:
                var4 = 32 + ((var2.Deadmap > 0 ? 32 : 0) | var2.PowerState | (var2.Active && var2.Powered ? 8 : 0) | (var2.Active && !var2.Powered && !var2.Disabled ? 0 : 16) | (var2.Active && !var2.Powered ? (var2.Deadmap > 0 ? 1 : 4) : 0));
                break;

            default:
                return;
        }

        this.renderWafer(var4);

        if (var3 == 2)
        {
            if (var2.Deadmap > 0)
            {
                this.renderChip(-0.125D, 0.0D, 0.125D, var2.Active ? 2 : 1);
            }
            else
            {
                this.renderChip(-0.125D, 0.0D, -0.125D, var2.Active ? 2 : 1);
            }
        }
    }

    protected void renderInvPart(int var1)
    {
        switch (var1)
        {
            case 0:
                this.context.setOrientation(0, 1);
                this.renderInvWafer(16);
                break;

            case 1:
                this.renderInvWafer(3);
                break;

            case 2:
                this.context.setOrientation(0, 1);
                this.renderInvWafer(48);
        }

        Tessellator var2 = Tessellator.instance;
        var2.startDrawingQuads();
        var2.setNormal(0.0F, 0.0F, 1.0F);

        if (var1 == 2)
        {
            RenderLib.renderPointer(new Vector3(-0.25D, -0.1D, 0.0D), Quat.aroundAxis(0.0D, 1.0D, 0.0D, 0.0D));
            this.context.useNormal = true;
            this.renderChip(-0.125D, 0.0D, -0.125D, 1);
            this.context.useNormal = false;
        }
        else
        {
            RenderLib.renderPointer(new Vector3(0.0D, -0.1D, 0.0D), Quat.aroundAxis(0.0D, 1.0D, 0.0D, -(Math.PI / 2D)));
        }

        var2.draw();
    }
}
