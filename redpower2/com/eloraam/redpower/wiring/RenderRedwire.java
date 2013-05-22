package com.eloraam.redpower.wiring;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.TileCovered;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderRedwire extends RenderWiring
{
    private int[] glowtex = new int[] {5, 5, 5, 5, 5, 5};

    public RenderRedwire(Block var1)
    {
        super(var1);
    }

    static TileRedwire getTileEntity(IBlockAccess var0, int var1, int var2, int var3)
    {
        TileEntity var4 = var0.getBlockTileEntity(var1, var2, var3);
        return !(var4 instanceof TileRedwire) ? null : (TileRedwire)var4;
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {}

    public void renderWorldBlock(RenderBlocks var1, IBlockAccess var2, int var3, int var4, int var5, int var6)
    {
        Tessellator var7 = Tessellator.instance;
        this.context.setBrightness(this.block.getMixedBrightnessForBlock(var2, var3, var4, var5));
        TileCovered var8 = (TileCovered)CoreLib.getTileEntity(var2, var3, var4, var5, TileCovered.class);

        if (var8 != null)
        {
            this.context.setTexFlags(55);
            this.context.setPos((double)var3, (double)var4, (double)var5);

            if (var8.CoverSides > 0)
            {
                this.context.setTint(1.0F, 1.0F, 1.0F);

                if (var1.overrideBlockTexture != null)
                {
                    //this.context.setTex(var1.overrideBlockTexture);
                    this.context.lockTexture = true;
                    this.context.forceFlat = true;
                }

                this.context.readGlobalLights(var2, var3, var4, var5);
                this.renderCovers(var8.CoverSides, var8.Covers);
                this.context.forceFlat = false;
                this.context.lockTexture = false;
            }

            if (var6 != 0)
            {
                TileWiring var9 = (TileWiring)var8;
                int var10 = var9.getConnectionMask();
                int var11 = var9.getExtConnectionMask();
                int var12 = var9.EConEMask;
                var10 |= var11;

                if (var6 == 1)
                {
                    TileRedwire var13 = (TileRedwire)var9;
                    this.context.setTint(0.3F + 0.7F * ((float)var13.PowerState / 255.0F), 0.0F, 0.0F);
                    this.setSideTex(1, 2, 1);
                    this.setWireSize(0.125F, 0.125F);
                }
                else if (var6 == 2)
                {
                    TileInsulatedWire var15 = (TileInsulatedWire)var9;
                    this.context.setTint(1.0F, 1.0F, 1.0F);
                    this.setSideTex(16 + var9.Metadata, (var15.PowerState > 0 ? 48 : 32) + var9.Metadata, 16 + var9.Metadata);
                    this.setWireSize(0.25F, 0.188F);
                }
                else if (var6 == 3)
                {
                    this.context.setTint(1.0F, 1.0F, 1.0F);

                    if (var9.Metadata == 0)
                    {
                        this.setSideTex(3, 4, 3);
                    }
                    else
                    {
                        this.setSideTex(63 + var9.Metadata, 79 + var9.Metadata, 3);
                    }

                    this.setWireSize(0.375F, 0.25F);
                }
                else if (var6 == 5)
                {
                    this.context.setTint(1.0F, 1.0F, 1.0F);

                    if (var9.Metadata == 0)
                    {
                        this.setSideTex(8, 9, 8);
                        this.setWireSize(0.25F, 0.188F);
                    }
                    else if (var9.Metadata == 1)
                    {
                        this.setSideTex(11, 12, 11);
                        this.setWireSize(0.375F, 0.25F);
                    }
                    else if (var9.Metadata == 2)
                    {
                        this.setSideTexJumbo(96, 97, 98, 99, 100, 101);
                        this.setWireSize(0.5F, 0.3125F);
                    }
                }

                RenderLib.setRedPowerTexture();
                this.renderWireBlock(var9.ConSides, var10, var11, var12);
                RenderLib.setDefaultTexture();

                if (var6 == 1 || var6 == 3 || var6 == 5)
                {
                    if ((var9.ConSides & 64) != 0)
                    {
                        this.context.setTexFlags(0);
                        this.context.setOrientation(0, 0);
                        this.context.setTint(1.0F, 1.0F, 1.0F);
                        this.context.setLocalLights(0.5F, 1.0F, 0.7F, 0.7F, 0.7F, 0.7F);
                        int var14;

                        if (var6 == 1)
                        {
                            var14 = ((TileRedwire)var9).PowerState > 0 ? 6 : 5;
                        }
                        else if (var6 == 3)
                        {
                            var14 = 7;
                        }
                        else
                        {
                            var14 = 10;
                        }

                        this.renderCenterBlock(var10 >> 24 | var9.ConSides & 63, CoverLib.coverTextureFiles[var9.CenterPost], coverTextures[var9.CenterPost], var14);
                    }
                }
            }
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
        this.context.setPos(-0.5D, -0.5D, -0.5D);
        float var5;

        switch (var4)
        {
            case 0:
            case 16:
            case 17:
            case 27:
            case 28:
            case 29:
            case 30:
                switch (var4)
                {
                    case 0:
                        var5 = 0.063F;
                        break;

                    case 16:
                        var5 = 0.125F;
                        break;

                    case 17:
                        var5 = 0.25F;
                        break;

                    case 27:
                        var5 = 0.188F;
                        break;

                    case 28:
                        var5 = 0.313F;
                        break;

                    case 29:
                        var5 = 0.375F;
                        break;

                    case 30:
                        var5 = 0.438F;
                        break;

                    default:
                        return;
                }

                this.context.setTex(coverTextures[var2]);
                this.context.setTexFile(CoverLib.coverTextureFiles[var2]);
                this.context.setSize(0.0D, 0.0D, (double)(0.5F - var5), 1.0D, 1.0D, (double)(0.5F + var5));
                this.context.calcBounds();
                var3.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderFaces(63);
                this.context.useNormal = false;
                var3.draw();
                return;

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                this.context.setPos(-0.5D, -0.20000000298023224D, -0.5D);
                this.context.setOrientation(0, 0);

                if (var4 == 1)
                {
                    this.setSideTex(1, 2, 1);
                    this.setWireSize(0.125F, 0.125F);
                    this.context.setTint(1.0F, 0.0F, 0.0F);
                }
                else if (var4 == 2)
                {
                    this.setSideTex(16 + var2, 32 + var2, 16 + var2);
                    this.setWireSize(0.25F, 0.188F);
                }
                else if (var4 == 3)
                {
                    if (var2 == 0)
                    {
                        this.setSideTex(3, 4, 3);
                    }
                    else
                    {
                        this.setSideTex(63 + var2, 79 + var2, 3);
                    }

                    this.setWireSize(0.375F, 0.25F);
                }
                else
                {
                    if (var4 != 5)
                    {
                        return;
                    }

                    if (var2 == 0)
                    {
                        this.setSideTex(8, 9, 8);
                        this.setWireSize(0.25F, 0.188F);
                    }
                    else if (var2 == 1)
                    {
                        this.setSideTex(11, 12, 11);
                        this.setWireSize(0.375F, 0.25F);
                    }
                    else if (var2 == 2)
                    {
                        this.setSideTexJumbo(96, 97, 98, 99, 100, 101);
                        this.setWireSize(0.5F, 0.3125F);
                    }
                }

                this.context.useNormal = true;
                RenderLib.setRedPowerTexture();
                var3.startDrawingQuads();
                this.renderSideWires(127, 0, 0);
                var3.draw();
                RenderLib.setDefaultTexture();
                this.context.useNormal = false;
                return;

            case 18:
            case 19:
            case 20:
            case 35:
            case 36:
            case 37:
            case 38:
                switch (var4)
                {
                    case 18:
                        var5 = 0.063F;
                        break;

                    case 19:
                        var5 = 0.125F;
                        break;

                    case 20:
                        var5 = 0.25F;
                        break;

                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    default:
                        return;

                    case 35:
                        var5 = 0.188F;
                        break;

                    case 36:
                        var5 = 0.313F;
                        break;

                    case 37:
                        var5 = 0.375F;
                        break;

                    case 38:
                        var5 = 0.438F;
                }

                this.context.setTex(coverTextures[var2]);
                this.context.setTexFile(CoverLib.coverTextureFiles[var2]);
                this.context.setSize((double)(0.5F - var5), (double)(0.5F - var5), (double)(0.5F - var5), (double)(0.5F + var5), (double)(0.5F + var5), (double)(0.5F + var5));
                this.context.calcBounds();
                var3.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderFaces(63);
                this.context.useNormal = false;
                var3.draw();
                return;

            case 21:
            case 22:
            case 23:
            case 39:
            case 40:
            case 41:
            case 42:
                switch (var4)
                {
                    case 21:
                        var5 = 0.063F;
                        break;

                    case 22:
                        var5 = 0.125F;
                        break;

                    case 23:
                        var5 = 0.25F;
                        break;

                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    default:
                        return;

                    case 39:
                        var5 = 0.188F;
                        break;

                    case 40:
                        var5 = 0.313F;
                        break;

                    case 41:
                        var5 = 0.375F;
                        break;

                    case 42:
                        var5 = 0.438F;
                }

                this.context.setTex(coverTextures[var2]);
                this.context.setTexFile(CoverLib.coverTextureFiles[var2]);
                this.context.setSize((double)(0.5F - var5), 0.0D, (double)(0.5F - var5), (double)(0.5F + var5), 1.0D, (double)(0.5F + var5));
                this.context.calcBounds();
                var3.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderFaces(63);
                this.context.useNormal = false;
                var3.draw();
                return;

            case 24:
            case 25:
            case 26:
            case 31:
            case 32:
            case 33:
            case 34:
                switch (var4)
                {
                    case 24:
                        var5 = 0.063F;
                        break;

                    case 25:
                        var5 = 0.125F;
                        break;

                    case 26:
                        var5 = 0.25F;
                        break;

                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    default:
                        return;

                    case 31:
                        var5 = 0.188F;
                        break;

                    case 32:
                        var5 = 0.313F;
                        break;

                    case 33:
                        var5 = 0.375F;
                        break;

                    case 34:
                        var5 = 0.438F;
                }

                this.context.setTex(coverTextures[var2]);
                this.context.setTexFile(CoverLib.coverTextureFiles[var2]);
                var3.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderBox(63, 0.0D, 0.0D, (double)(0.5F - var5), 0.25D, 1.0D, (double)(0.5F + var5));
                this.context.renderBox(63, 0.75D, 0.0D, (double)(0.5F - var5), 1.0D, 1.0D, (double)(0.5F + var5));
                this.context.renderBox(15, 0.25D, 0.0D, (double)(0.5F - var5), 0.75D, 0.25D, (double)(0.5F + var5));
                this.context.renderBox(15, 0.25D, 0.75D, (double)(0.5F - var5), 0.75D, 1.0D, (double)(0.5F + var5));
                this.context.useNormal = false;
                var3.draw();
                return;

            case 43:
            case 44:
            case 45:
                switch (var4)
                {
                    case 43:
                        var5 = 0.125F;
                        break;

                    case 44:
                        var5 = 0.25F;
                        break;

                    case 45:
                        var5 = 0.375F;
                        break;

                    default:
                        return;
                }

                this.context.setTex(coverTextures[var2]);
                this.context.setTexFile(CoverLib.coverTextureFiles[var2]);
                this.context.setSize((double)(0.5F - var5), 0.125D, (double)(0.5F - var5), (double)(0.5F + var5), 0.875D, (double)(0.5F + var5));
                this.context.calcBounds();
                var3.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderFaces(63);
                this.context.setSize((double)(0.45F - var5), 0.0D, (double)(0.45F - var5), (double)(0.55F + var5), 0.125D, (double)(0.55F + var5));
                this.context.calcBounds();
                this.context.renderFaces(63);
                this.context.setSize((double)(0.45F - var5), 0.875D, (double)(0.45F - var5), (double)(0.55F + var5), 1.0D, (double)(0.55F + var5));
                this.context.calcBounds();
                this.context.renderFaces(63);
                this.context.useNormal = false;
                var3.draw();
                return;

            case 64:
            case 65:
            case 66:
                this.context.setTex(coverTextures[var2]);
                this.context.setTexFile(CoverLib.coverTextureFiles[var2]);
                var3.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderBox(60, 0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
                this.context.renderBox(15, 0.0D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
                this.context.renderBox(51, 0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 1.0D);
                var3.draw();
                var3.startDrawingQuads();
                this.context.setTexFile("/eloraam/wiring/redpower1.png");
                this.context.setTex(var4 == 66 ? 10 : (var4 == 64 ? 5 : 7));
                this.context.renderBox(3, 0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
                this.context.renderBox(48, 0.0D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
                this.context.renderBox(12, 0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 1.0D);
                var3.draw();
                this.context.clearTexFiles();
                this.context.useNormal = false;
        }
    }
}
