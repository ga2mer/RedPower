package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.MathLib;
import com.eloraam.redpower.core.PowerLib;
import com.eloraam.redpower.core.Quat;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.Vector3;
import com.eloraam.redpower.logic.RenderLogic$TorchPos;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderLogicSimple extends RenderLogic
{
    private static RenderLogic$TorchPos[] torchMapLatch = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.3D, -0.15D, 0.0D, 0.8D), new RenderLogic$TorchPos(0.3D, -0.15D, 0.0D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapLatch2 = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.281D, -0.15D, -0.0938D, 0.8D), new RenderLogic$TorchPos(0.281D, -0.15D, 0.0938D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapLatch2b = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.281D, -0.15D, 0.0938D, 0.8D), new RenderLogic$TorchPos(0.281D, -0.15D, -0.0938D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapNor = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.094D, -0.25D, 0.031D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapOr = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.094D, -0.25D, 0.031D, 0.7D), new RenderLogic$TorchPos(0.28D, -0.15D, 0.0D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapNand = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.031D, -0.25D, 0.22D, 0.7D), new RenderLogic$TorchPos(-0.031D, -0.25D, 0.0D, 0.7D), new RenderLogic$TorchPos(-0.031D, -0.25D, -0.22D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapAnd = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.031D, -0.25D, 0.22D, 0.7D), new RenderLogic$TorchPos(-0.031D, -0.25D, 0.0D, 0.7D), new RenderLogic$TorchPos(-0.031D, -0.25D, -0.22D, 0.7D), new RenderLogic$TorchPos(0.28D, -0.15D, 0.0D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapXnor = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.031D, -0.25D, 0.22D, 0.7D), new RenderLogic$TorchPos(-0.031D, -0.25D, -0.22D, 0.7D), new RenderLogic$TorchPos(-0.28D, -0.25D, 0.0D, 0.7D), new RenderLogic$TorchPos(0.28D, -0.15D, 0.0D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapXor = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.031D, -0.25D, 0.22D, 0.7D), new RenderLogic$TorchPos(-0.031D, -0.25D, -0.22D, 0.7D), new RenderLogic$TorchPos(-0.28D, -0.25D, 0.0D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapPulse = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.09D, -0.25D, -0.22D, 0.7D), new RenderLogic$TorchPos(-0.09D, -0.25D, 0.22D, 0.7D), new RenderLogic$TorchPos(0.28D, -0.15D, 0.0D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapToggle = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.28D, -0.25D, -0.22D, 0.7D), new RenderLogic$TorchPos(-0.28D, -0.25D, -0.22D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapNot = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.031D, -0.25D, 0.031D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapBuffer = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.281D, -0.15D, 0.031D, 0.8D), new RenderLogic$TorchPos(-0.094D, -0.25D, 0.031D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapMux = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.031D, -0.25D, 0.22D, 0.7D), new RenderLogic$TorchPos(-0.031D, -0.25D, -0.22D, 0.7D), new RenderLogic$TorchPos(-0.156D, -0.25D, 0.031D, 0.7D), new RenderLogic$TorchPos(0.28D, -0.15D, 0.0D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapMux2 = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.031D, -0.25D, 0.22D, 0.7D), new RenderLogic$TorchPos(-0.031D, -0.25D, -0.22D, 0.7D), new RenderLogic$TorchPos(-0.156D, -0.25D, -0.031D, 0.7D), new RenderLogic$TorchPos(0.28D, -0.15D, 0.0D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapRepS = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.313D, -0.25D, -0.125D, 0.7D), new RenderLogic$TorchPos(-0.25D, -0.25D, 0.25D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapSync = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.28D, -0.25D, 0.0D, 0.7D)};
    private static RenderLogic$TorchPos[] torchMapDLatch = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.28D, -0.25D, -0.219D, 0.7D), new RenderLogic$TorchPos(0.031D, -0.25D, -0.219D, 0.7D), new RenderLogic$TorchPos(0.031D, -0.25D, -0.031D, 0.7D), new RenderLogic$TorchPos(0.031D, -0.15D, 0.281D, 0.8D), new RenderLogic$TorchPos(0.281D, -0.15D, -0.094D, 0.8D)};
    private static RenderLogic$TorchPos[] torchMapDLatch2 = new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(-0.28D, -0.25D, 0.219D, 0.7D), new RenderLogic$TorchPos(0.031D, -0.25D, 0.219D, 0.7D), new RenderLogic$TorchPos(0.031D, -0.25D, 0.031D, 0.7D), new RenderLogic$TorchPos(0.031D, -0.15D, -0.281D, 0.8D), new RenderLogic$TorchPos(0.281D, -0.15D, 0.094D, 0.8D)};
    private static final int[] texIdxNor = new int[] {272, 288, 296, 312, 304, 316, 320};
    private static final int[] texIdxOr = new int[] {376, 384, 388, 416, 392, 418, 420};
    private static final int[] texIdxNand = new int[] {336, 352, 360, 324, 368, 328, 332};
    private static final int[] texIdxAnd = new int[] {400, 408, 412, 422, 396, 424, 426};
    private static final int[] texIdxNot = new int[] {432, 448, 456, 472, 464, 476, 428};
    private static final int[] texIdxBuf = new int[] {496, 504, 508, 257};
    private static Quat[] leverPositions = new Quat[2];

    public RenderLogicSimple(Block var1)
    {
        super(var1);
    }

    protected int getTorchState(TileLogic var1)
    {
        int var2 = var1.getExtendedMetadata();
        int var4;

        switch (var2)
        {
            case 0:
                if (var1.Deadmap > 1)
                {
                    return ((var1.PowerState & 2) > 0 ? 1 : 0) | ((var1.PowerState & 8) > 0 ? 2 : 0);
                }
                else
                {
                    if (!var1.Disabled && !var1.Active)
                    {
                        if (var1.Deadmap == 1)
                        {
                            return var1.Powered ? 1 : 2;
                        }

                        return var1.Powered ? 2 : 1;
                    }

                    return 0;
                }

            case 1:
                return var1.Powered ? 1 : 0;

            case 2:
                var4 = var1.PowerState & ~var1.Deadmap;
                return (var4 == 0 ? 1 : 0) | (var1.Powered ? 2 : 0);

            case 3:
                var4 = var1.PowerState | var1.Deadmap;
                return var4 & 7 ^ 7;

            case 4:
                var4 = var1.PowerState | var1.Deadmap;
                return var4 & 7 ^ 7 | (var1.Powered ? 8 : 0);

            case 5:
            case 6:
                byte var3;

                switch (var1.PowerState & 5)
                {
                    case 0:
                        var3 = 4;
                        break;

                    case 1:
                        var3 = 2;
                        break;

                    case 2:
                    case 3:
                    default:
                        var3 = 0;
                        break;

                    case 4:
                        var3 = 1;
                }

                if (var2 == 6)
                {
                    return var3;
                }

                return var3 | (var1.Powered ? 8 : 0);

            case 7:
                return (!var1.Powered && !var1.Active ? 1 : 0) | (!var1.Powered && !var1.Active ? 0 : 2) | (var1.Powered && !var1.Active ? 4 : 0);

            case 8:
                return !var1.Powered ? 1 : 2;

            case 9:
                return var1.Powered ? 1 : 0;

            case 10:
                return (var1.Powered ? 1 : 0) | var1.PowerState & 2;

            case 11:
                if (var1.Deadmap == 0)
                {
                    return (var1.Powered ? 8 : 0) | ((var1.PowerState & 3) == 0 ? 1 : 0) | ((var1.PowerState & 6) == 2 ? 2 : 0) | ((var1.PowerState & 2) == 0 ? 4 : 0);
                }

                return (var1.Powered ? 8 : 0) | ((var1.PowerState & 3) == 2 ? 1 : 0) | ((var1.PowerState & 6) == 0 ? 2 : 0) | ((var1.PowerState & 2) == 0 ? 4 : 0);

            case 12:
                return (var1.Powered ? 1 : 0) | (var1.PowerState == 0 ? 2 : 0);

            case 13:
                return var1.Powered ? 1 : 0;

            case 14:
                return 0;

            case 15:
                if (var1.Deadmap == 0)
                {
                    switch (var1.PowerState & 6)
                    {
                        case 0:
                            return var1.Powered ? 25 : 5;

                        case 1:
                        case 3:
                        default:
                            return var1.Powered ? 24 : 0;

                        case 2:
                            return var1.Powered ? 26 : 2;

                        case 4:
                            return var1.Powered ? 25 : 5;
                    }
                }
                else
                {
                    switch (var1.PowerState & 3)
                    {
                        case 0:
                            return var1.Powered ? 25 : 5;

                        case 1:
                            return var1.Powered ? 25 : 5;

                        case 2:
                            return var1.Powered ? 26 : 2;

                        default:
                            return var1.Powered ? 24 : 0;
                    }
                }

            default:
                return 0;
        }
    }

    protected int getInvTorchState(int var1)
    {
        switch (var1)
        {
            case 256:
            case 257:
            case 258:
                return 1;

            case 259:
            case 260:
                return 7;

            case 261:
                return 12;

            case 262:
                return 4;

            case 263:
            case 264:
            case 265:
                return 1;

            case 266:
                return 2;

            case 267:
                return 12;

            case 268:
                return 1;

            case 269:
                return 0;

            case 270:
                return 0;

            case 271:
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
                if (var1.Deadmap == 2)
                {
                    return torchMapLatch2;
                }
                else
                {
                    if (var1.Deadmap == 3)
                    {
                        return torchMapLatch2b;
                    }

                    return torchMapLatch;
                }

            case 1:
                return torchMapNor;

            case 2:
                return torchMapOr;

            case 3:
                return torchMapNand;

            case 4:
                return torchMapAnd;

            case 5:
                return torchMapXnor;

            case 6:
                return torchMapXor;

            case 7:
                return torchMapPulse;

            case 8:
                return torchMapToggle;

            case 9:
                return torchMapNot;

            case 10:
                return torchMapBuffer;

            case 11:
                if (var1.Deadmap == 0)
                {
                    return torchMapMux;
                }

                return torchMapMux2;

            case 12:
                return new RenderLogic$TorchPos[] {new RenderLogic$TorchPos(0.313D, -0.25D, -0.125D, 0.7D), new RenderLogic$TorchPos(-0.25D + (double)var1.Deadmap * 0.063D, -0.25D, 0.25D, 0.7D)};
            case 13:
                return torchMapSync;

            case 14:
                return null;

            case 15:
                if (var1.Deadmap == 0)
                {
                    return torchMapDLatch;
                }

                return torchMapDLatch2;

            default:
                return null;
        }
    }

    protected RenderLogic$TorchPos[] getInvTorchVectors(int var1)
    {
        switch (var1)
        {
            case 256:
                return torchMapLatch;

            case 257:
                return torchMapNor;

            case 258:
                return torchMapOr;

            case 259:
                return torchMapNand;

            case 260:
                return torchMapAnd;

            case 261:
                return torchMapXnor;

            case 262:
                return torchMapXor;

            case 263:
                return torchMapPulse;

            case 264:
                return torchMapToggle;

            case 265:
                return torchMapNot;

            case 266:
                return torchMapBuffer;

            case 267:
                return torchMapMux;

            case 268:
                return torchMapRepS;

            case 269:
                return torchMapSync;

            case 270:
                return null;

            case 271:
                return torchMapDLatch;

            default:
                return null;
        }
    }

    protected void renderWorldPart(IBlockAccess var1, TileLogic var2)
    {
        int var3 = var2.getExtendedMetadata();
        int var4;
        int var5;

        switch (var3)
        {
            case 0:
                if (var2.Deadmap < 2)
                {
                    var4 = ((var2.PowerState & 1) > 0 ? 1 : 0) | ((var2.PowerState & 4) > 0 ? 2 : 0);

                    if (!var2.Disabled || var2.Active)
                    {
                        var4 |= var2.Powered ? 2 : 1;
                    }

                    var4 += 24 + (var2.Deadmap == 1 ? 4 : 0);
                }
                else
                {
                    var4 = 96 + (var2.Deadmap == 3 ? 16 : 0) + var2.PowerState;
                }

                break;

            case 1:
                var4 = texIdxNor[var2.Deadmap] + PowerLib.cutBits(var2.PowerState | (var2.Powered ? 8 : 0), var2.Deadmap);
                break;

            case 2:
                var4 = texIdxOr[var2.Deadmap] + PowerLib.cutBits(var2.PowerState, var2.Deadmap);
                break;

            case 3:
                var4 = texIdxNand[var2.Deadmap] + PowerLib.cutBits(var2.PowerState | (var2.Powered ? 8 : 0), var2.Deadmap);
                break;

            case 4:
                var4 = texIdxAnd[var2.Deadmap] + PowerLib.cutBits(var2.PowerState, var2.Deadmap);
                break;

            case 5:
                var4 = 128 + (var2.PowerState & 1) + ((var2.PowerState & 4) >> 1);
                break;

            case 6:
                var4 = 132 + ((var2.Powered ? 4 : 0) | (var2.PowerState & 12) >> 1 | var2.PowerState & 1);
                break;

            case 7:
                var4 = 5;

                if (var2.Powered && !var2.Active)
                {
                    var4 = 6;
                }
                else if (!var2.Powered && var2.Active)
                {
                    var4 = 7;
                }

                break;

            case 8:
                var4 = 140 + (var2.PowerState & 1) + (var2.PowerState >> 1 & 2);
                break;

            case 9:
                if (var2.Deadmap == 0)
                {
                    var4 = 432 + (var2.PowerState | (var2.Powered ? 13 : 0));
                }
                else
                {
                    var5 = PowerLib.cutBits(var2.Deadmap, 2);

                    if (var2.Powered)
                    {
                        var4 = 480 + (var5 - 1 << 1) + ((var2.PowerState & 2) >> 1);
                    }
                    else
                    {
                        var4 = texIdxNot[var5] + PowerLib.cutBits(var2.PowerState, var2.Deadmap);
                    }
                }

                break;

            case 10:
                if (var2.Deadmap == 0)
                {
                    var4 = 496 + (var2.PowerState | (var2.Powered ? 5 : 0));
                }
                else
                {
                    var5 = PowerLib.cutBits(var2.Deadmap, 2);

                    if (var2.Powered)
                    {
                        var4 = 256 + (var5 << 1) + ((var2.PowerState & 2) >> 1);
                    }
                    else
                    {
                        var4 = texIdxBuf[var5] + PowerLib.cutBits(var2.PowerState, var2.Deadmap);
                    }
                }

                break;

            case 11:
                var4 = 144 + (var2.Deadmap > 0 ? 8 : 0) + var2.PowerState;
                break;

            case 12:
                var4 = 492 + (var2.PowerState >> 1) + (var2.Powered ? 0 : 2);
                break;

            case 13:
                var4 = 160 + var2.PowerState + (var2.Active ? 8 : 0) + (var2.Disabled ? 16 : 0);
                break;

            case 14:
                var4 = 192 + (var2.PowerState | (var2.Active ? 1 : 0) | (var2.Powered ? 4 : 0) | (var2.Disabled ? 8 : 0));
                break;

            case 15:
                if (var2.Deadmap > 0)
                {
                    var4 = 216 + var2.PowerState + (var2.Powered ? 4 : 0);
                }
                else
                {
                    var4 = 208 + (var2.PowerState >> 1) + (var2.Powered ? 4 : 0);
                }

                break;

            case 16:
                var4 = 513 + (!var2.Powered && var2.PowerState <= 0 ? 0 : 1);
                break;

            default:
                return;
        }

        this.renderWafer(var4);

        if (var3 == 8)
        {
            this.context.setTexFlags(44);
            this.context.setSize(0.25D, 0.0D, 0.5550000071525574D, 0.75D, 0.30000001192092896D, 0.8050000071525574D);
            //this.context.setTex(Block.cobblestone);
            this.context.calcBounds();
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.renderFaces(62);
            Vector3 var6 = new Vector3(0.0D, -0.3D, 0.18D);
            Quat var7 = MathLib.orientQuat(var2.Rotation >> 2, var2.Rotation & 3);
            var7.rotate(var6);
            var6.add(this.context.globalOrigin);
            var7.rightMultiply(leverPositions[var2.Powered ? 1 : 0]);
            //RenderLib.renderSpecialLever(var6, var7, Block.lever.blockIndexInTexture);
        }
        else if (var3 == 13)
        {
            this.renderChip(-0.125D, 0.0D, -0.1875D, var2.Disabled ? 2 : 1);
            this.renderChip(-0.125D, 0.0D, 0.1875D, var2.Active ? 2 : 1);
        }
        else if (var3 == 14)
        {
            this.renderChip(-0.25D, 0.0D, -0.25D, var2.Disabled ? 9 : 8);
            this.renderChip(-0.25D, 0.0D, 0.25D, var2.Active ? 9 : 8);
            this.renderChip(0.125D, 0.0D, 0.0D, var2.Powered ? 9 : 8);
        }
        else if (var3 == 16)
        {
            this.context.bindTexture("/eloraam/logic/sensor1.png");
            this.context.setTexFlags(64);
            var4 = 16 + var2.Deadmap;
            this.context.setTex(var4, var4, 20, 20, var4, var4);
            this.context.renderBox(62, 0.125D, 0.0D, 0.18799999356269836D, 0.625D, 0.18799999356269836D, 0.8130000233650208D);
            this.context.unbindTexture();
        }
    }

    protected void renderInvPart(int var1)
    {
        switch (var1)
        {
            case 256:
                this.renderInvWafer(25);
                break;

            case 257:
                this.renderInvWafer(280);
                break;

            case 258:
                this.renderInvWafer(384);
                break;

            case 259:
                this.renderInvWafer(344);
                break;

            case 260:
                this.renderInvWafer(400);
                break;

            case 261:
                this.renderInvWafer(128);
                break;

            case 262:
                this.renderInvWafer(132);
                break;

            case 263:
                this.renderInvWafer(5);
                break;

            case 264:
                this.renderInvWafer(140);
                break;

            case 265:
                this.renderInvWafer(440);
                break;

            case 266:
                this.renderInvWafer(496);
                break;

            case 267:
                this.renderInvWafer(144);
                break;

            case 268:
                this.renderInvWafer(493);
                break;

            case 269:
                this.renderInvWafer(160);
                break;

            case 270:
                this.renderInvWafer(192);
                break;

            case 271:
                this.renderInvWafer(208);
                break;

            case 272:
                this.renderInvWafer(513);
        }

        Tessellator var2;

        if (var1 == 264)
        {
            var2 = Tessellator.instance;
            var2.startDrawingQuads();
            this.context.useNormal = true;
            this.context.setTexFlags(44);
            this.context.setSize(0.25D, 0.0D, 0.5550000071525574D, 0.75D, 0.30000001192092896D, 0.8050000071525574D);
            //this.context.setTex(Block.cobblestone.blockIndexInTexture);
            this.context.calcBounds();
            this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
            this.context.renderFaces(62);
            this.context.useNormal = false;
            var2.draw();
            var2.startDrawingQuads();
            var2.setNormal(0.0F, 0.0F, 1.0F);
            Vector3 var3 = new Vector3(0.0D, -0.3D, 0.18D);
            Quat var4 = MathLib.orientQuat(0, 3);
            var4.rotate(var3);
            var3.add(this.context.globalOrigin);
            var4.rightMultiply(leverPositions[0]);
            //RenderLib.renderSpecialLever(var3, var4, Block.lever.blockIndexInTexture);
            var2.draw();
        }
        else if (var1 == 269)
        {
            var2 = Tessellator.instance;
            var2.startDrawingQuads();
            this.context.useNormal = true;
            this.renderChip(-0.125D, 0.0D, -0.1875D, 2);
            this.renderChip(-0.125D, 0.0D, 0.1875D, 2);
            this.context.useNormal = false;
            var2.draw();
        }
        else if (var1 == 270)
        {
            var2 = Tessellator.instance;
            var2.startDrawingQuads();
            this.context.useNormal = true;
            this.renderChip(-0.25D, 0.0D, -0.25D, 8);
            this.renderChip(-0.25D, 0.0D, 0.25D, 8);
            this.renderChip(0.125D, 0.0D, 0.0D, 8);
            this.context.useNormal = false;
            var2.draw();
        }
        else if (var1 == 272)
        {
            var2 = Tessellator.instance;
            var2.startDrawingQuads();
            this.context.useNormal = true;
            this.context.bindTexture("/eloraam/logic/sensor1.png");
            this.context.setTex(16, 16, 20, 20, 16, 16);
            this.context.setTexFlags(64);
            this.context.renderBox(62, 0.125D, 0.0D, 0.18799999356269836D, 0.625D, 0.18799999356269836D, 0.8130000233650208D);
            this.context.unbindTexture();
            this.context.useNormal = false;
            var2.draw();
        }
    }

    static
    {
        leverPositions[0] = Quat.aroundAxis(1.0D, 0.0D, 0.0D, 0.8639379797371932D);
        leverPositions[1] = Quat.aroundAxis(1.0D, 0.0D, 0.0D, -0.8639379797371932D);
        leverPositions[0].multiply(MathLib.orientQuat(0, 3));
        leverPositions[1].multiply(MathLib.orientQuat(0, 3));
    }
}
