package com.eloraam.redpower.core;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class RenderContext
{
    public static final int[][] texRotTable = new int[][] {{0, 1, 2, 3, 4, 5, 0, 112347, 0}, {0, 1, 4, 5, 3, 2, 45, 112320, 27}, {0, 1, 3, 2, 5, 4, 27, 112347, 0}, {0, 1, 5, 4, 2, 3, 54, 112320, 27}, {1, 0, 2, 3, 5, 4, 112347, 112347, 0}, {1, 0, 4, 5, 2, 3, 112374, 112320, 27}, {1, 0, 3, 2, 4, 5, 112320, 112347, 0}, {1, 0, 5, 4, 3, 2, 112365, 112320, 27}, {4, 5, 0, 1, 2, 3, 217134, 1728, 110619}, {3, 2, 0, 1, 4, 5, 220014, 0, 112347}, {5, 4, 0, 1, 3, 2, 218862, 1728, 110619}, {2, 3, 0, 1, 5, 4, 220590, 0, 112347}, {4, 5, 1, 0, 3, 2, 188469, 1728, 110619}, {3, 2, 1, 0, 5, 4, 191349, 0, 112347}, {5, 4, 1, 0, 2, 3, 190197, 1728, 110619}, {2, 3, 1, 0, 4, 5, 191925, 0, 112347}, {4, 5, 3, 2, 0, 1, 2944, 110619, 1728}, {3, 2, 5, 4, 0, 1, 187264, 27, 112320}, {5, 4, 2, 3, 0, 1, 113536, 110619, 1728}, {2, 3, 4, 5, 0, 1, 224128, 27, 112320}, {4, 5, 2, 3, 1, 0, 3419, 110619, 1728}, {3, 2, 4, 5, 1, 0, 187739, 27, 112320}, {5, 4, 3, 2, 1, 0, 114011, 110619, 1728}, {2, 3, 5, 4, 1, 0, 224603, 27, 112320}};
    public Matrix3 basis = new Matrix3();
    public Vector3 localOffset = new Vector3();
    public Vector3 globalOrigin = new Vector3();
    public Vector3 boxSize1 = new Vector3();
    public Vector3 boxSize2 = new Vector3();
    public RenderModel boundModel = null;
    public Vector3[] vertexList;
    private Vector3[] vertexListBox = new Vector3[8];
    public TexVertex[][] cornerList;
    private TexVertex[][] cornerListBox = new TexVertex[6][4];
    private String[] texBinds = null;
    private String[] texBindsBox = new String[6];
    private int[] texIndex;
    private int[] texIndexBox = new int[6];
    private int[][] texIndexList;
    public boolean lockTexture = false;
    public boolean exactTextureCoordinates = false;
    private int texFlags = 0;
    public boolean useNormal = false;
    public boolean forceFlat = false;
    private float tintR = 1.0F;
    private float tintG = 1.0F;
    private float tintB = 1.0F;
    private float tintA = 1.0F;
    public float[] lightLocal;
    private float[] lightLocalBox = new float[6];
    public int[] brightLocal;
    private int[] brightLocalBox = new int[6];
    private int[][][] lightGlobal = new int[3][3][3];
    private float[][][] aoGlobal = new float[3][3][3];
    private float[] lightFlat = new float[6];
    private int globTrans;

    public void setDefaults()
    {
        this.localOffset.set(0.0D, 0.0D, 0.0D);
        this.setOrientation(0, 0);
        this.texFlags = 0;
        this.tintR = 1.0F;
        this.tintG = 1.0F;
        this.tintB = 1.0F;
        this.tintA = 1.0F;
        this.setLocalLights(1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        this.setBrightness(15728880);
    }

    public void setPos(double var1, double var3, double var5)
    {
        this.globalOrigin.set(var1, var3, var5);
    }

    public void setPos(Vector3 var1)
    {
        this.globalOrigin.set(var1);
    }

    public void setRelPos(double var1, double var3, double var5)
    {
        this.localOffset.set(var1, var3, var5);
    }

    public void setRelPos(Vector3 var1)
    {
        this.localOffset.set(var1);
    }

    public void setOrientation(int var1, int var2)
    {
        MathLib.orientMatrix(this.basis, var1, var2);
    }

    public void setSize(double var1, double var3, double var5, double var7, double var9, double var11)
    {
        this.boxSize1.set(var1, var3, var5);
        this.boxSize2.set(var7, var9, var11);
    }

    public void clearTexFiles()
    {
        this.texBinds = null;
        RenderLib.unbindTexture();
    }

    public void setTexFiles(String[] var1)
    {
        this.texBinds = var1;
    }

    public void setTexFile(String var1)
    {
        this.texBinds = null;

        if (var1 == null)
        {
            RenderLib.unbindTexture();
        }
        else
        {
            RenderLib.bindTexture(var1);
        }
    }

    public void setTexFiles(String var1, String var2, String var3, String var4, String var5, String var6)
    {
        if (var1 == null && var2 == null && var3 == null && var4 == null && var5 == null && var6 == null)
        {
            if (this.texBinds != null)
            {
                this.clearTexFiles();
            }
        }
        else
        {
            this.texBinds = this.texBindsBox;
            this.texBinds[0] = var1;
            this.texBinds[1] = var2;
            this.texBinds[2] = var3;
            this.texBinds[3] = var4;
            this.texBinds[4] = var5;
            this.texBinds[5] = var6;
        }
    }

    public void setTexFlags(int var1)
    {
        this.texFlags = var1;
    }

    public void setTex(int var1, int var2, int var3, int var4, int var5, int var6)
    {
        if (!this.lockTexture)
        {
            this.texIndex = this.texIndexBox;
            this.texIndex[0] = var1;
            this.texIndex[1] = var2;
            this.texIndex[2] = var3;
            this.texIndex[3] = var4;
            this.texIndex[4] = var5;
            this.texIndex[5] = var6;
        }
    }

    public void setTex(int var1)
    {
        if (!this.lockTexture)
        {
            this.texIndex = this.texIndexBox;
            this.texIndex[0] = var1;
            this.texIndex[1] = var1;
            this.texIndex[2] = var1;
            this.texIndex[3] = var1;
            this.texIndex[4] = var1;
            this.texIndex[5] = var1;
        }
    }

    public void setTex(int[] var1)
    {
        if (!this.lockTexture)
        {
            this.texIndex = var1;
        }
    }

    public void setTex(int[][] var1)
    {
        if (!this.lockTexture)
        {
            this.texIndexList = var1;
            this.texIndex = var1[0];
        }
    }

    public void setTexIndex(int var1)
    {
        if (this.texIndexList != null)
        {
            this.texIndex = this.texIndexList[var1];
        }
    }

    public void setTexNum(int var1, int var2)
    {
        this.texIndex[var1] = var2;
    }

    public void setTint(float var1, float var2, float var3)
    {
        this.tintR = var1;
        this.tintG = var2;
        this.tintB = var3;
    }

    public void setTintHex(int var1)
    {
        this.tintR = (float)(var1 >> 16) / 255.0F;
        this.tintG = (float)(var1 >> 8 & 255) / 255.0F;
        this.tintB = (float)(var1 & 255) / 255.0F;
    }

    public void setAlpha(float var1)
    {
        this.tintA = var1;
    }

    public void setLocalLights(float var1, float var2, float var3, float var4, float var5, float var6)
    {
        this.lightLocal = this.lightLocalBox;
        this.lightLocal[0] = var1;
        this.lightLocal[1] = var2;
        this.lightLocal[2] = var3;
        this.lightLocal[3] = var4;
        this.lightLocal[4] = var5;
        this.lightLocal[5] = var6;
    }

    public void setLocalLights(float var1)
    {
        this.lightLocal = this.lightLocalBox;

        for (int var2 = 0; var2 < 6; ++var2)
        {
            this.lightLocal[var2] = var1;
        }
    }

    public void setBrightness(int var1)
    {
        this.brightLocal = this.brightLocalBox;

        for (int var2 = 0; var2 < 6; ++var2)
        {
            this.brightLocal[var2] = var1;
        }
    }

    public void startWorldRender(RenderBlocks var1) {}

    public boolean endWorldRender()
    {
        return false;
    }

    public void bindTexture(String var1)
    {
        if (!this.lockTexture)
        {
            RenderLib.bindTexture(var1);
        }
    }

    public void unbindTexture()
    {
        if (!this.lockTexture)
        {
            RenderLib.unbindTexture();
        }
    }

    public void setupBox()
    {
        this.vertexList = this.vertexListBox;
        this.vertexList[0].set(this.boxSize2.x, this.boxSize2.y, this.boxSize1.z);
        this.vertexList[1].set(this.boxSize1.x, this.boxSize2.y, this.boxSize1.z);
        this.vertexList[2].set(this.boxSize1.x, this.boxSize2.y, this.boxSize2.z);
        this.vertexList[3].set(this.boxSize2.x, this.boxSize2.y, this.boxSize2.z);
        this.vertexList[4].set(this.boxSize2.x, this.boxSize1.y, this.boxSize1.z);
        this.vertexList[5].set(this.boxSize1.x, this.boxSize1.y, this.boxSize1.z);
        this.vertexList[6].set(this.boxSize1.x, this.boxSize1.y, this.boxSize2.z);
        this.vertexList[7].set(this.boxSize2.x, this.boxSize1.y, this.boxSize2.z);
    }

    public void transformRotate()
    {
        for (int var1 = 0; var1 < this.vertexList.length; ++var1)
        {
            this.vertexList[var1].add(this.localOffset.x - 0.5D, this.localOffset.y - 0.5D, this.localOffset.z - 0.5D);
            this.basis.rotate(this.vertexList[var1]);
            this.vertexList[var1].add(this.globalOrigin.x + 0.5D, this.globalOrigin.y + 0.5D, this.globalOrigin.z + 0.5D);
        }
    }

    public void transform()
    {
        for (int var1 = 0; var1 < this.vertexList.length; ++var1)
        {
            this.vertexList[var1].add(this.localOffset);
            this.vertexList[var1].add(this.globalOrigin);
        }
    }

    public void setSideUV(int var1, double var2, double var4, double var6, double var8)
    {
        if (!this.exactTextureCoordinates)
        {
            var2 += 0.001D;
            var6 += 0.001D;
            var4 -= 0.001D;
            var8 -= 0.001D;
        }

        var2 *= 0.0625D;
        var4 *= 0.0625D;
        var6 *= 0.0625D;
        var8 *= 0.0625D;
        int var10 = this.texFlags >> var1 * 3;

        if ((var10 & 1) > 0)
        {
            var2 = 0.0625D - var2;
            var4 = 0.0625D - var4;
        }

        if ((var10 & 2) > 0)
        {
            var6 = 0.0625D - var6;
            var8 = 0.0625D - var8;
        }

        int var11 = this.texIndex[var1];
        double var12 = (double)(var11 & 15) * 0.0625D;
        double var14 = (double)(var11 >> 4) * 0.0625D;

        if ((var10 & 4) > 0)
        {
            var2 += var14;
            var4 += var14;
            var6 += var12;
            var8 += var12;
            this.cornerList[var1][0].setUV(var6, var2);
            this.cornerList[var1][1].setUV(var8, var2);
            this.cornerList[var1][2].setUV(var8, var4);
            this.cornerList[var1][3].setUV(var6, var4);
        }
        else
        {
            var2 += var12;
            var4 += var12;
            var6 += var14;
            var8 += var14;
            this.cornerList[var1][0].setUV(var2, var6);
            this.cornerList[var1][1].setUV(var2, var8);
            this.cornerList[var1][2].setUV(var4, var8);
            this.cornerList[var1][3].setUV(var4, var6);
        }
    }

    public void doMappingBox(int var1)
    {
        this.cornerList = this.cornerListBox;
        double var2;
        double var4;
        double var6;
        double var8;

        if ((var1 & 3) > 0)
        {
            var6 = 1.0D - this.boxSize2.x;
            var8 = 1.0D - this.boxSize1.x;

            if ((var1 & 1) > 0)
            {
                var2 = 1.0D - this.boxSize2.z;
                var4 = 1.0D - this.boxSize1.z;
                this.setSideUV(0, var2, var4, var6, var8);
            }

            if ((var1 & 2) > 0)
            {
                var2 = this.boxSize1.z;
                var4 = this.boxSize2.z;
                this.setSideUV(1, var2, var4, var6, var8);
            }
        }

        if ((var1 & 60) != 0)
        {
            var6 = 1.0D - this.boxSize2.y;
            var8 = 1.0D - this.boxSize1.y;

            if ((var1 & 4) > 0)
            {
                var2 = 1.0D - this.boxSize2.x;
                var4 = 1.0D - this.boxSize1.x;
                this.setSideUV(2, var2, var4, var6, var8);
            }

            if ((var1 & 8) > 0)
            {
                var2 = this.boxSize1.x;
                var4 = this.boxSize2.x;
                this.setSideUV(3, var2, var4, var6, var8);
            }

            if ((var1 & 16) > 0)
            {
                var2 = this.boxSize1.z;
                var4 = this.boxSize2.z;
                this.setSideUV(4, var2, var4, var6, var8);
            }

            if ((var1 & 32) > 0)
            {
                var2 = 1.0D - this.boxSize2.z;
                var4 = 1.0D - this.boxSize1.z;
                this.setSideUV(5, var2, var4, var6, var8);
            }
        }
    }

    public void calcBoundsGlobal()
    {
        this.setupBox();
        this.transform();
    }

    public void calcBounds()
    {
        this.setupBox();
        this.transformRotate();
    }

    private void swaptex(int var1, int var2)
    {
        int var3 = this.texIndexBox[var1];
        this.texIndexBox[var1] = this.texIndexBox[var2];
        this.texIndexBox[var2] = var3;
    }

    public void orientTextures(int var1)
    {
        switch (var1)
        {
            case 0:
                break;

            case 1:
                this.swaptex(0, 1);
                this.swaptex(4, 5);
                this.texFlags = 112347;
                break;

            case 2:
                this.swaptex(0, 2);
                this.swaptex(1, 3);
                this.swaptex(0, 4);
                this.swaptex(1, 5);
                this.texFlags = 217134;
                break;

            case 3:
                this.swaptex(0, 3);
                this.swaptex(1, 2);
                this.swaptex(0, 4);
                this.swaptex(1, 5);
                this.texFlags = 188469;
                break;

            case 4:
                this.swaptex(0, 4);
                this.swaptex(1, 5);
                this.swaptex(2, 3);
                this.texFlags = 2944;
                break;

            case 5:
                this.swaptex(0, 5);
                this.swaptex(1, 4);
                this.swaptex(0, 1);
                this.texFlags = 3419;
                break;

            default:
                return;
        }
    }

    public void orientTextureRot(int var1, int var2)
    {
        int var3 = var2 > 1 ? (var2 == 2 ? 3 : 6) : (var2 == 0 ? 0 : 5);
        var3 |= var3 << 3;

        switch (var1)
        {
            case 0:
                this.texFlags = var3;
                break;

            case 1:
                this.swaptex(0, 1);
                this.swaptex(4, 5);
                this.texFlags = 112347 ^ var3;
                break;

            case 2:
                this.swaptex(0, 2);
                this.swaptex(1, 3);
                this.swaptex(0, 4);
                this.swaptex(1, 5);
                this.texFlags = 217134 ^ var3 << 6;
                break;

            case 3:
                this.swaptex(0, 3);
                this.swaptex(1, 2);
                this.swaptex(0, 4);
                this.swaptex(1, 5);
                this.texFlags = 188469 ^ var3 << 6;
                break;

            case 4:
                this.swaptex(0, 4);
                this.swaptex(1, 5);
                this.swaptex(2, 3);
                this.texFlags = 2944 ^ var3 << 12;
                break;

            case 5:
                this.swaptex(0, 5);
                this.swaptex(1, 4);
                this.swaptex(0, 1);
                this.texFlags = 3419 ^ var3 << 12;
                break;

            default:
                return;
        }
    }

    private void swaptexfl(int var1, int var2)
    {
        int var3 = this.texIndexBox[var1];
        this.texIndexBox[var1] = this.texIndexBox[var2];
        this.texIndexBox[var2] = var3;
        var1 *= 3;
        var2 *= 3;
        int var4 = this.texFlags >> var1 & 7;
        int var5 = this.texFlags >> var2 & 7;
        this.texFlags &= ~(7 << var1 | 7 << var2);
        this.texFlags |= var4 << var2 | var5 << var1;
    }

    public void rotateTextures(int var1)
    {
        int var2 = var1 > 1 ? (var1 == 2 ? 3 : 6) : (var1 == 0 ? 0 : 5);
        var2 |= var2 << 3;
        this.texFlags ^= var2;

        switch (var1)
        {
            case 1:
                this.swaptexfl(2, 4);
                this.swaptexfl(3, 4);
                this.swaptexfl(3, 5);
                break;

            case 2:
                this.swaptexfl(2, 3);
                this.swaptexfl(4, 5);
                break;

            case 3:
                this.swaptexfl(2, 5);
                this.swaptexfl(3, 5);
                this.swaptexfl(3, 4);
        }
    }

    public void orientTextureFl(int var1)
    {
        switch (var1)
        {
            case 0:
                break;

            case 1:
                this.swaptexfl(0, 1);
                this.swaptexfl(4, 5);
                this.texFlags ^= 112347;
                break;

            case 2:
                this.swaptexfl(0, 2);
                this.swaptexfl(1, 3);
                this.swaptexfl(0, 4);
                this.swaptexfl(1, 5);
                this.texFlags ^= 217134;
                break;

            case 3:
                this.swaptexfl(0, 3);
                this.swaptexfl(1, 2);
                this.swaptexfl(0, 4);
                this.swaptexfl(1, 5);
                this.texFlags ^= 188469;
                break;

            case 4:
                this.swaptexfl(0, 4);
                this.swaptexfl(1, 5);
                this.swaptexfl(2, 3);
                this.texFlags ^= 2944;
                break;

            case 5:
                this.swaptexfl(0, 5);
                this.swaptexfl(1, 4);
                this.swaptexfl(0, 1);
                this.texFlags ^= 3419;
                break;

            default:
                return;
        }
    }

    public void orientTextureNew(int var1)
    {
        int[] var2 = new int[6];
        System.arraycopy(this.texIndexBox, 0, var2, 0, 6);
        int[] var3 = texRotTable[var1];
        int var4 = 0;
        int var5;

        for (var5 = 0; var5 < 6; ++var5)
        {
            this.texIndexBox[var5] = var2[var3[var5]];
            var4 |= (this.texFlags >> var3[var5] * 3 & 7) << var5 * 3;
        }

        var5 = (var4 & 37449) << 1 | (var4 & 74898) >> 1;
        this.texFlags = var3[6] ^ var4 & var3[7] ^ var5 & var3[8];
    }

    public void flipTextures()
    {
        this.swaptex(0, 1);
        this.swaptex(2, 3);
        this.swaptex(4, 5);
    }

    public void renderBox(int var1, double var2, double var4, double var6, double var8, double var10, double var12)
    {
        this.setSize(var2, var4, var6, var8, var10, var12);
        this.setupBox();
        this.transformRotate();
        this.renderFaces(var1);
    }

    public void doubleBox(int var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14)
    {
        int var16 = var1 << 1 & 42 | var1 >> 1 & 21;
        this.renderBox(var1, var2, var4, var6, var8, var10, var12);
        this.flipTextures();
        this.renderBox(var16, var8 - var14, var10 - var14, var12 - var14, var2 + var14, var4 + var14, var6 + var14);
    }

    public void doLightLocal(int var1)
    {
        for (int var3 = 0; var3 < this.cornerList.length; ++var3)
        {
            if ((var1 & 1 << var3) != 0)
            {
                TexVertex var2 = this.cornerList[var3][0];
                var2.r = this.lightLocal[var3] * this.tintR;
                var2.g = this.lightLocal[var3] * this.tintG;
                var2.b = this.lightLocal[var3] * this.tintB;
                var2.brtex = this.brightLocal[var3];
            }
        }
    }

    public void readGlobalLights(IBlockAccess var1, int var2, int var3, int var4)
    {
        Block var8 = Block.blocksList[var1.getBlockId(var2, var3, var4)];

        if (Minecraft.isAmbientOcclusionEnabled() && !this.forceFlat)
        {
            for (int var5 = 0; var5 < 3; ++var5)
            {
                for (int var6 = 0; var6 < 3; ++var6)
                {
                    for (int var7 = 0; var7 < 3; ++var7)
                    {
                        this.aoGlobal[var5][var6][var7] = var8.getAmbientOcclusionLightValue(var1, var2 + var5 - 1, var3 + var6 - 1, var4 + var7 - 1);
                        this.lightGlobal[var5][var6][var7] = var8.getMixedBrightnessForBlock(var1, var2 + var5 - 1, var3 + var6 - 1, var4 + var7 - 1);
                    }
                }
            }

            int var9 = 0;

            if (Block.canBlockGrass[var1.getBlockId(var2, var3 - 1, var4 - 1)])
            {
                var9 |= 1;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2, var3 - 1, var4 + 1)])
            {
                var9 |= 2;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2 - 1, var3 - 1, var4)])
            {
                var9 |= 4;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2 + 1, var3 - 1, var4)])
            {
                var9 |= 8;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2 - 1, var3, var4 - 1)])
            {
                var9 |= 16;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2 - 1, var3, var4 + 1)])
            {
                var9 |= 32;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2 + 1, var3, var4 - 1)])
            {
                var9 |= 64;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2 + 1, var3, var4 + 1)])
            {
                var9 |= 128;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2, var3 + 1, var4 - 1)])
            {
                var9 |= 256;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2, var3 + 1, var4 + 1)])
            {
                var9 |= 512;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2 - 1, var3 + 1, var4)])
            {
                var9 |= 1024;
            }

            if (Block.canBlockGrass[var1.getBlockId(var2 + 1, var3 + 1, var4)])
            {
                var9 |= 2048;
            }

            this.globTrans = var9;
        }
        else
        {
            this.lightFlat[0] = var8.getBlockBrightness(var1, var2, var3 - 1, var4);
            this.lightFlat[1] = var8.getBlockBrightness(var1, var2, var3 + 1, var4);
            this.lightFlat[2] = var8.getBlockBrightness(var1, var2, var3, var4 - 1);
            this.lightFlat[3] = var8.getBlockBrightness(var1, var2, var3, var4 + 1);
            this.lightFlat[4] = var8.getBlockBrightness(var1, var2 - 1, var3, var4);
            this.lightFlat[5] = var8.getBlockBrightness(var1, var2 + 1, var3, var4);
        }
    }

    public static int blendLight(int var0, int var1, int var2, int var3)
    {
        if (var1 == 0)
        {
            var1 = var0;
        }

        if (var2 == 0)
        {
            var2 = var0;
        }

        if (var3 == 0)
        {
            var3 = var0;
        }

        return var0 + var1 + var2 + var3 >> 2 & 16711935;
    }

    private void lightSmoothFace(int var1)
    {
        int var18 = 0;

        if (this.boxSize1.y > 0.0D)
        {
            var18 |= 1;
        }

        if (this.boxSize2.y < 1.0D)
        {
            var18 |= 2;
        }

        if (this.boxSize1.z > 0.0D)
        {
            var18 |= 4;
        }

        if (this.boxSize2.z < 1.0D)
        {
            var18 |= 8;
        }

        if (this.boxSize1.x > 0.0D)
        {
            var18 |= 16;
        }

        if (this.boxSize2.x < 1.0D)
        {
            var18 |= 32;
        }

        float var7;
        float var8;
        float var9;
        float var6 = var7 = var8 = var9 = this.aoGlobal[1][1][1];
        int var15;
        int var17;
        int var16;
        int var14 = var15 = var16 = var17 = this.lightGlobal[1][1][1];
        float var2;
        float var3;
        float var4;
        float var5;
        int var10;
        int var11;
        int var12;
        int var13;

        switch (var1)
        {
            case 0:
                if ((var18 & 61) <= 0)
                {
                    var2 = var3 = this.aoGlobal[0][0][1];
                    var4 = var5 = this.aoGlobal[2][0][1];
                    var10 = var11 = this.lightGlobal[0][0][1];
                    var12 = var13 = this.lightGlobal[2][0][1];

                    if ((this.globTrans & 5) > 0)
                    {
                        var2 = this.aoGlobal[0][0][0];
                        var10 = this.lightGlobal[0][0][0];
                    }

                    if ((this.globTrans & 6) > 0)
                    {
                        var3 = this.aoGlobal[0][0][2];
                        var11 = this.lightGlobal[0][0][2];
                    }

                    if ((this.globTrans & 9) > 0)
                    {
                        var4 = this.aoGlobal[2][0][0];
                        var12 = this.lightGlobal[2][0][0];
                    }

                    if ((this.globTrans & 10) > 0)
                    {
                        var5 = this.aoGlobal[2][0][2];
                        var13 = this.lightGlobal[2][0][2];
                    }

                    var8 = 0.25F * (this.aoGlobal[1][0][1] + this.aoGlobal[1][0][0] + this.aoGlobal[0][0][1] + var2);
                    var9 = 0.25F * (this.aoGlobal[1][0][1] + this.aoGlobal[1][0][0] + this.aoGlobal[2][0][1] + var4);
                    var7 = 0.25F * (this.aoGlobal[1][0][1] + this.aoGlobal[1][0][2] + this.aoGlobal[0][0][1] + var3);
                    var6 = 0.25F * (this.aoGlobal[1][0][1] + this.aoGlobal[1][0][2] + this.aoGlobal[2][0][1] + var5);
                    var16 = blendLight(this.lightGlobal[1][0][1], this.lightGlobal[1][0][0], this.lightGlobal[0][0][1], var10);
                    var17 = blendLight(this.lightGlobal[1][0][1], this.lightGlobal[1][0][0], this.lightGlobal[2][0][1], var12);
                    var15 = blendLight(this.lightGlobal[1][0][1], this.lightGlobal[1][0][2], this.lightGlobal[0][0][1], var11);
                    var14 = blendLight(this.lightGlobal[1][0][1], this.lightGlobal[1][0][2], this.lightGlobal[2][0][1], var13);
                }

                break;

            case 1:
                if ((var18 & 62) <= 0)
                {
                    var2 = var3 = this.aoGlobal[0][2][1];
                    var4 = var5 = this.aoGlobal[2][2][1];
                    var10 = var11 = this.lightGlobal[0][2][1];
                    var12 = var13 = this.lightGlobal[2][2][1];

                    if ((this.globTrans & 1280) > 0)
                    {
                        var2 = this.aoGlobal[0][2][0];
                        var10 = this.lightGlobal[0][2][0];
                    }

                    if ((this.globTrans & 1536) > 0)
                    {
                        var3 = this.aoGlobal[0][2][2];
                        var11 = this.lightGlobal[0][2][2];
                    }

                    if ((this.globTrans & 2304) > 0)
                    {
                        var4 = this.aoGlobal[2][2][0];
                        var12 = this.lightGlobal[2][2][0];
                    }

                    if ((this.globTrans & 2560) > 0)
                    {
                        var5 = this.aoGlobal[2][2][2];
                        var13 = this.lightGlobal[2][2][2];
                    }

                    var7 = 0.25F * (this.aoGlobal[1][2][1] + this.aoGlobal[1][2][0] + this.aoGlobal[0][2][1] + var2);
                    var6 = 0.25F * (this.aoGlobal[1][2][1] + this.aoGlobal[1][2][0] + this.aoGlobal[2][2][1] + var4);
                    var8 = 0.25F * (this.aoGlobal[1][2][1] + this.aoGlobal[1][2][2] + this.aoGlobal[0][2][1] + var3);
                    var9 = 0.25F * (this.aoGlobal[1][2][1] + this.aoGlobal[1][2][2] + this.aoGlobal[2][2][1] + var5);
                    var15 = blendLight(this.lightGlobal[1][2][1], this.lightGlobal[1][2][0], this.lightGlobal[0][2][1], var10);
                    var14 = blendLight(this.lightGlobal[1][2][1], this.lightGlobal[1][2][0], this.lightGlobal[2][2][1], var12);
                    var16 = blendLight(this.lightGlobal[1][2][1], this.lightGlobal[1][2][2], this.lightGlobal[0][2][1], var11);
                    var17 = blendLight(this.lightGlobal[1][2][1], this.lightGlobal[1][2][2], this.lightGlobal[2][2][1], var13);
                }

                break;

            case 2:
                if ((var18 & 55) <= 0)
                {
                    var2 = var3 = this.aoGlobal[0][1][0];
                    var4 = var5 = this.aoGlobal[2][1][0];
                    var10 = var11 = this.lightGlobal[0][1][0];
                    var12 = var13 = this.lightGlobal[2][1][0];

                    if ((this.globTrans & 17) > 0)
                    {
                        var2 = this.aoGlobal[0][0][0];
                        var10 = this.lightGlobal[0][0][0];
                    }

                    if ((this.globTrans & 272) > 0)
                    {
                        var3 = this.aoGlobal[0][2][0];
                        var11 = this.lightGlobal[0][2][0];
                    }

                    if ((this.globTrans & 65) > 0)
                    {
                        var4 = this.aoGlobal[2][0][0];
                        var12 = this.lightGlobal[2][0][0];
                    }

                    if ((this.globTrans & 320) > 0)
                    {
                        var5 = this.aoGlobal[2][2][0];
                        var13 = this.lightGlobal[2][2][0];
                    }

                    var8 = 0.25F * (this.aoGlobal[1][1][0] + this.aoGlobal[1][0][0] + this.aoGlobal[0][1][0] + var2);
                    var9 = 0.25F * (this.aoGlobal[1][1][0] + this.aoGlobal[1][2][0] + this.aoGlobal[0][1][0] + var3);
                    var7 = 0.25F * (this.aoGlobal[1][1][0] + this.aoGlobal[1][0][0] + this.aoGlobal[2][1][0] + var4);
                    var6 = 0.25F * (this.aoGlobal[1][1][0] + this.aoGlobal[1][2][0] + this.aoGlobal[2][1][0] + var5);
                    var16 = blendLight(this.lightGlobal[1][1][0], this.lightGlobal[1][0][0], this.lightGlobal[0][1][0], var10);
                    var17 = blendLight(this.lightGlobal[1][1][0], this.lightGlobal[1][2][0], this.lightGlobal[0][1][0], var11);
                    var15 = blendLight(this.lightGlobal[1][1][0], this.lightGlobal[1][0][0], this.lightGlobal[2][1][0], var12);
                    var14 = blendLight(this.lightGlobal[1][1][0], this.lightGlobal[1][2][0], this.lightGlobal[2][1][0], var13);
                }

                break;

            case 3:
                if ((var18 & 59) <= 0)
                {
                    var2 = var3 = this.aoGlobal[0][1][2];
                    var4 = var5 = this.aoGlobal[2][1][2];
                    var10 = var11 = this.lightGlobal[0][1][2];
                    var12 = var13 = this.lightGlobal[2][1][2];

                    if ((this.globTrans & 34) > 0)
                    {
                        var2 = this.aoGlobal[0][0][2];
                        var10 = this.lightGlobal[0][0][2];
                    }

                    if ((this.globTrans & 544) > 0)
                    {
                        var3 = this.aoGlobal[0][2][2];
                        var11 = this.lightGlobal[0][2][2];
                    }

                    if ((this.globTrans & 130) > 0)
                    {
                        var4 = this.aoGlobal[2][0][2];
                        var12 = this.lightGlobal[2][0][2];
                    }

                    if ((this.globTrans & 640) > 0)
                    {
                        var5 = this.aoGlobal[2][2][2];
                        var13 = this.lightGlobal[2][2][2];
                    }

                    var7 = 0.25F * (this.aoGlobal[1][1][2] + this.aoGlobal[1][0][2] + this.aoGlobal[0][1][2] + var2);
                    var6 = 0.25F * (this.aoGlobal[1][1][2] + this.aoGlobal[1][2][2] + this.aoGlobal[0][1][2] + var4);
                    var8 = 0.25F * (this.aoGlobal[1][1][2] + this.aoGlobal[1][0][2] + this.aoGlobal[2][1][2] + var3);
                    var9 = 0.25F * (this.aoGlobal[1][1][2] + this.aoGlobal[1][2][2] + this.aoGlobal[2][1][2] + var5);
                    var15 = blendLight(this.lightGlobal[1][1][2], this.lightGlobal[1][0][2], this.lightGlobal[0][1][2], var10);
                    var14 = blendLight(this.lightGlobal[1][1][2], this.lightGlobal[1][2][2], this.lightGlobal[0][1][2], var11);
                    var16 = blendLight(this.lightGlobal[1][1][2], this.lightGlobal[1][0][2], this.lightGlobal[2][1][2], var12);
                    var17 = blendLight(this.lightGlobal[1][1][2], this.lightGlobal[1][2][2], this.lightGlobal[2][1][2], var13);
                }

                break;

            case 4:
                if ((var18 & 31) <= 0)
                {
                    var2 = var3 = this.aoGlobal[0][1][0];
                    var4 = var5 = this.aoGlobal[0][1][2];
                    var10 = var11 = this.lightGlobal[0][1][0];
                    var12 = var13 = this.lightGlobal[0][1][2];

                    if ((this.globTrans & 20) > 0)
                    {
                        var2 = this.aoGlobal[0][0][0];
                        var10 = this.lightGlobal[0][0][0];
                    }

                    if ((this.globTrans & 1040) > 0)
                    {
                        var3 = this.aoGlobal[0][2][0];
                        var11 = this.lightGlobal[0][2][0];
                    }

                    if ((this.globTrans & 36) > 0)
                    {
                        var4 = this.aoGlobal[0][0][2];
                        var12 = this.lightGlobal[0][0][2];
                    }

                    if ((this.globTrans & 1056) > 0)
                    {
                        var5 = this.aoGlobal[0][2][2];
                        var13 = this.lightGlobal[0][2][2];
                    }

                    var7 = 0.25F * (this.aoGlobal[0][1][1] + this.aoGlobal[0][0][1] + this.aoGlobal[0][1][0] + var2);
                    var6 = 0.25F * (this.aoGlobal[0][1][1] + this.aoGlobal[0][2][1] + this.aoGlobal[0][1][0] + var3);
                    var8 = 0.25F * (this.aoGlobal[0][1][1] + this.aoGlobal[0][0][1] + this.aoGlobal[0][1][2] + var4);
                    var9 = 0.25F * (this.aoGlobal[0][1][1] + this.aoGlobal[0][2][1] + this.aoGlobal[0][1][2] + var5);
                    var15 = blendLight(this.lightGlobal[0][1][1], this.lightGlobal[0][0][1], this.lightGlobal[0][1][0], var10);
                    var14 = blendLight(this.lightGlobal[0][1][1], this.lightGlobal[0][2][1], this.lightGlobal[0][1][0], var11);
                    var16 = blendLight(this.lightGlobal[0][1][1], this.lightGlobal[0][0][1], this.lightGlobal[0][1][2], var12);
                    var17 = blendLight(this.lightGlobal[0][1][1], this.lightGlobal[0][2][1], this.lightGlobal[0][1][2], var13);
                }

                break;

            default:
                if ((var18 & 47) <= 0)
                {
                    var2 = var3 = this.aoGlobal[2][1][0];
                    var4 = var5 = this.aoGlobal[2][1][2];
                    var10 = var11 = this.lightGlobal[2][1][0];
                    var12 = var13 = this.lightGlobal[2][1][2];

                    if ((this.globTrans & 72) > 0)
                    {
                        var2 = this.aoGlobal[2][0][0];
                        var10 = this.lightGlobal[2][0][0];
                    }

                    if ((this.globTrans & 2112) > 0)
                    {
                        var3 = this.aoGlobal[2][2][0];
                        var11 = this.lightGlobal[2][2][0];
                    }

                    if ((this.globTrans & 136) > 0)
                    {
                        var4 = this.aoGlobal[2][0][2];
                        var12 = this.lightGlobal[2][0][2];
                    }

                    if ((this.globTrans & 2176) > 0)
                    {
                        var5 = this.aoGlobal[2][2][2];
                        var13 = this.lightGlobal[2][2][2];
                    }

                    var8 = 0.25F * (this.aoGlobal[2][1][1] + this.aoGlobal[2][0][1] + this.aoGlobal[2][1][0] + var2);
                    var9 = 0.25F * (this.aoGlobal[2][1][1] + this.aoGlobal[2][2][1] + this.aoGlobal[2][1][0] + var3);
                    var7 = 0.25F * (this.aoGlobal[2][1][1] + this.aoGlobal[2][0][1] + this.aoGlobal[2][1][2] + var4);
                    var6 = 0.25F * (this.aoGlobal[2][1][1] + this.aoGlobal[2][2][1] + this.aoGlobal[2][1][2] + var5);
                    var16 = blendLight(this.lightGlobal[2][1][1], this.lightGlobal[2][0][1], this.lightGlobal[2][1][0], var10);
                    var17 = blendLight(this.lightGlobal[2][1][1], this.lightGlobal[2][2][1], this.lightGlobal[2][1][0], var11);
                    var15 = blendLight(this.lightGlobal[2][1][1], this.lightGlobal[2][0][1], this.lightGlobal[2][1][2], var12);
                    var14 = blendLight(this.lightGlobal[2][1][1], this.lightGlobal[2][2][1], this.lightGlobal[2][1][2], var13);
                }
        }

        TexVertex var19 = this.cornerList[var1][0];
        float var20 = this.lightLocal[var1] * var6;
        var19.r = var20 * this.tintR;
        var19.g = var20 * this.tintG;
        var19.b = var20 * this.tintB;
        var19.brtex = var14;
        var19 = this.cornerList[var1][1];
        var20 = this.lightLocal[var1] * var7;
        var19.r = var20 * this.tintR;
        var19.g = var20 * this.tintG;
        var19.b = var20 * this.tintB;
        var19.brtex = var15;
        var19 = this.cornerList[var1][2];
        var20 = this.lightLocal[var1] * var8;
        var19.r = var20 * this.tintR;
        var19.g = var20 * this.tintG;
        var19.b = var20 * this.tintB;
        var19.brtex = var16;
        var19 = this.cornerList[var1][3];
        var20 = this.lightLocal[var1] * var9;
        var19.r = var20 * this.tintR;
        var19.g = var20 * this.tintG;
        var19.b = var20 * this.tintB;
        var19.brtex = var17;
    }

    public void doLightSmooth(int var1)
    {
        for (int var2 = 0; var2 < 6; ++var2)
        {
            if ((var1 & 1 << var2) != 0)
            {
                this.lightSmoothFace(var2);
            }
        }
    }

    private void doLightFlat(int var1)
    {
        for (int var2 = 0; var2 < this.cornerList.length; ++var2)
        {
            if ((var1 & 1 << var2) != 0)
            {
                TexVertex var3 = this.cornerList[var2][0];
                var3.r = this.lightFlat[var2] * this.lightLocal[var2] * this.tintR;
                var3.g = this.lightFlat[var2] * this.lightLocal[var2] * this.tintG;
                var3.b = this.lightFlat[var2] * this.lightLocal[var2] * this.tintB;
                var3.brtex = this.brightLocal[var2];
            }
        }
    }

    public void renderFlat(int var1)
    {
        Tessellator var2 = Tessellator.instance;

        for (int var5 = 0; var5 < this.cornerList.length; ++var5)
        {
            if ((var1 & 1 << var5) != 0)
            {
                if (this.texBinds != null)
                {
                    if (this.texBinds[var5] != null)
                    {
                        RenderLib.bindTexture(this.texBinds[var5]);
                    }
                    else
                    {
                        RenderLib.unbindTexture();
                    }

                    var2 = Tessellator.instance;
                }

                TexVertex var3 = this.cornerList[var5][0];
                var2.setColorOpaque_F(var3.r, var3.g, var3.b);
                Vector3 var4;

                if (this.useNormal)
                {
                    var4 = this.vertexList[var3.vtx];
                    var3 = this.cornerList[var5][1];
                    Vector3 var7 = new Vector3(this.vertexList[var3.vtx]);
                    var3 = this.cornerList[var5][2];
                    Vector3 var8 = new Vector3(this.vertexList[var3.vtx]);
                    var7.subtract(var4);
                    var8.subtract(var4);
                    var7.crossProduct(var8);
                    var7.normalize();
                    var2.setNormal((float)var7.x, (float)var7.y, (float)var7.z);
                }
                else
                {
                    var2.setBrightness(var3.brtex);
                }

                for (int var6 = 0; var6 < 4; ++var6)
                {
                    var3 = this.cornerList[var5][var6];
                    var4 = this.vertexList[var3.vtx];
                    var2.addVertexWithUV(var4.x, var4.y, var4.z, var3.u, var3.v);
                }
            }
        }

        if (this.texBinds != null)
        {
            RenderLib.unbindTexture();
        }
    }

    public void renderRangeFlat(int var1, int var2)
    {
        Tessellator var3 = Tessellator.instance;

        for (int var6 = var1; var6 < var2; ++var6)
        {
            if (this.texBinds != null)
            {
                if (this.texBinds[var6] != null)
                {
                    RenderLib.bindTexture(this.texBinds[var6]);
                }
                else
                {
                    RenderLib.unbindTexture();
                }

                var3 = Tessellator.instance;
            }

            TexVertex var4 = this.cornerList[var6][0];
            var3.setColorRGBA_F(var4.r * this.tintR, var4.g * this.tintG, var4.b * this.tintB, this.tintA);
            Vector3 var5;

            if (this.useNormal)
            {
                var5 = this.vertexList[var4.vtx];
                var4 = this.cornerList[var6][1];
                Vector3 var8 = new Vector3(this.vertexList[var4.vtx]);
                var4 = this.cornerList[var6][2];
                Vector3 var9 = new Vector3(this.vertexList[var4.vtx]);
                var8.subtract(var5);
                var9.subtract(var5);
                var8.crossProduct(var9);
                var8.normalize();
                var3.setNormal((float)var8.x, (float)var8.y, (float)var8.z);
            }
            else
            {
                var3.setBrightness(var4.brtex);
            }

            for (int var7 = 0; var7 < 4; ++var7)
            {
                var4 = this.cornerList[var6][var7];
                var5 = this.vertexList[var4.vtx];
                var3.addVertexWithUV(var5.x, var5.y, var5.z, var4.u, var4.v);
            }
        }

        if (this.texBinds != null)
        {
            RenderLib.unbindTexture();
        }
    }

    public void renderAlpha(int var1, float var2)
    {
        Tessellator var3 = Tessellator.instance;

        for (int var6 = 0; var6 < this.cornerList.length; ++var6)
        {
            if ((var1 & 1 << var6) != 0)
            {
                TexVertex var4 = this.cornerList[var6][0];
                var3.setColorRGBA_F(var4.r, var4.g, var4.b, var2);

                if (!this.useNormal)
                {
                    var3.setBrightness(var4.brtex);
                }

                for (int var7 = 0; var7 < 4; ++var7)
                {
                    var4 = this.cornerList[var6][var7];
                    Vector3 var5 = this.vertexList[var4.vtx];
                    var3.addVertexWithUV(var5.x, var5.y, var5.z, var4.u, var4.v);
                }
            }
        }
    }

    public void renderSmooth(int var1)
    {
        Tessellator var2 = Tessellator.instance;

        for (int var5 = 0; var5 < this.cornerList.length; ++var5)
        {
            if ((var1 & 1 << var5) != 0)
            {
                if (this.texBinds != null)
                {
                    if (this.texBinds[var5] != null)
                    {
                        RenderLib.bindTexture(this.texBinds[var5]);
                    }
                    else
                    {
                        RenderLib.unbindTexture();
                    }

                    var2 = Tessellator.instance;
                }

                for (int var6 = 0; var6 < 4; ++var6)
                {
                    TexVertex var3 = this.cornerList[var5][var6];
                    var2.setColorOpaque_F(var3.r, var3.g, var3.b);

                    if (!this.useNormal)
                    {
                        var2.setBrightness(var3.brtex);
                    }

                    Vector3 var4 = this.vertexList[var3.vtx];
                    var2.addVertexWithUV(var4.x, var4.y, var4.z, var3.u, var3.v);
                }
            }
        }

        if (this.texBinds != null)
        {
            RenderLib.unbindTexture();
        }
    }

    public void renderFaces(int var1)
    {
        this.doMappingBox(var1);
        this.doLightLocal(var1);
        this.renderFlat(var1);
    }

    public void renderGlobFaces(int var1)
    {
        this.doMappingBox(var1);
        this.doLightLocal(var1);

        if (Minecraft.isAmbientOcclusionEnabled() && !this.forceFlat)
        {
            this.doLightSmooth(var1);
            this.renderSmooth(var1);
        }
        else
        {
            this.doLightFlat(var1);
            this.renderFlat(var1);
        }
    }

    public void drawPoints(int ... var1)
    {
        Tessellator var2 = Tessellator.instance;
        int[] var4 = var1;
        int var5 = var1.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            int var7 = var4[var6];
            Vector3 var3 = this.vertexList[var7];
            var2.addVertex(var3.x, var3.y, var3.z);
        }
    }

    public void bindModel(RenderModel var1)
    {
        this.vertexList = new Vector3[var1.vertexList.length];

        for (int var2 = 0; var2 < this.vertexList.length; ++var2)
        {
            Vector3 var3 = new Vector3(var1.vertexList[var2]);
            this.basis.rotate(var3);
            var3.add(this.globalOrigin);
            this.vertexList[var2] = var3;
        }

        this.cornerList = var1.texList;
        this.boundModel = var1;
    }

    public void bindModelOffset(RenderModel var1, double var2, double var4, double var6)
    {
        this.vertexList = new Vector3[var1.vertexList.length];

        for (int var8 = 0; var8 < this.vertexList.length; ++var8)
        {
            Vector3 var9 = new Vector3(var1.vertexList[var8]);
            var9.add(this.localOffset.x - var2, this.localOffset.y - var4, this.localOffset.z - var6);
            this.basis.rotate(var9);
            var9.add(var2, var4, var6);
            var9.add(this.globalOrigin);
            this.vertexList[var8] = var9;
        }

        this.cornerList = var1.texList;
        this.boundModel = var1;
    }

    public void renderModelGroup(int var1, int var2)
    {
        for (int var4 = 0; var4 < this.cornerList.length; ++var4)
        {
            TexVertex var3 = this.cornerList[var4][0];
            var3.brtex = this.brightLocal[0];
        }

        this.renderRangeFlat(this.boundModel.groups[var1][var2][0], this.boundModel.groups[var1][var2][1]);
    }

    public void renderModel(RenderModel var1)
    {
        this.bindModel(var1);

        for (int var3 = 0; var3 < this.cornerList.length; ++var3)
        {
            TexVertex var2 = this.cornerList[var3][0];
            var2.brtex = this.brightLocal[0];
        }

        this.renderRangeFlat(0, this.cornerList.length);
    }

    public RenderContext()
    {
        int var1;

        for (var1 = 0; var1 < 8; ++var1)
        {
            this.vertexListBox[var1] = new Vector3();
        }

        int[][] var3 = new int[][] {{7, 6, 5, 4}, {0, 1, 2, 3}, {0, 4, 5, 1}, {2, 6, 7, 3}, {1, 5, 6, 2}, {3, 7, 4, 0}};

        for (var1 = 0; var1 < 6; ++var1)
        {
            for (int var2 = 0; var2 < 4; ++var2)
            {
                this.cornerListBox[var1][var2] = new TexVertex();
                this.cornerListBox[var1][var2].vtx = var3[var1][var2];
            }
        }

        this.setDefaults();
    }
}
