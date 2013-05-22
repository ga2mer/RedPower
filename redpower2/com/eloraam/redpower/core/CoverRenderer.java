package com.eloraam.redpower.core;

public class CoverRenderer
{
    float cx1;
    float cx2;
    float cy1;
    float cy2;
    float cz1;
    float cz2;
    float[] x1 = new float[4];
    float[] x2 = new float[4];
    float[] y1 = new float[4];
    float[] y2 = new float[4];
    float[] z1 = new float[4];
    float[] z2 = new float[4];
    public short[] covs;
    public int covmask;
    public int covmaskt;
    public int covmaskh;
    public int covmasko;
    protected static int[][] coverTextures = CoverLib.coverTextures;
    protected static String[] coverTextureFiles = CoverLib.coverTextureFiles;
    protected RenderContext context;

    public CoverRenderer(RenderContext var1)
    {
        this.context = var1;
    }

    public void start()
    {
        this.cx1 = 0.0F;
        this.cx2 = 1.0F;
        this.cy1 = 0.0F;
        this.cy2 = 1.0F;
        this.cz1 = 0.0F;
        this.cz2 = 1.0F;
    }

    public void startShrink(float var1)
    {
        this.cx1 = var1;
        this.cx2 = 1.0F - var1;
        this.cy1 = var1;
        this.cy2 = 1.0F - var1;
        this.cz1 = var1;
        this.cz2 = 1.0F - var1;
    }

    void sizeHollow(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
            case 1:
                if (var2 == 0)
                {
                    this.context.boxSize2.x = 0.25D;
                }

                if (var2 == 1)
                {
                    this.context.boxSize1.x = 0.75D;
                }

                if (var2 > 1)
                {
                    this.context.boxSize1.x = 0.25D;
                    this.context.boxSize2.x = 0.75D;
                }

                if (var2 == 2)
                {
                    this.context.boxSize2.z = 0.25D;
                }

                if (var2 == 3)
                {
                    this.context.boxSize1.z = 0.75D;
                }

                break;

            case 2:
            case 3:
                if (var2 == 0)
                {
                    this.context.boxSize2.x = 0.25D;
                }

                if (var2 == 1)
                {
                    this.context.boxSize1.x = 0.75D;
                }

                if (var2 > 1)
                {
                    this.context.boxSize1.x = 0.25D;
                    this.context.boxSize2.x = 0.75D;
                }

                if (var2 == 2)
                {
                    this.context.boxSize2.y = 0.25D;
                }

                if (var2 == 3)
                {
                    this.context.boxSize1.y = 0.75D;
                }

                break;

            default:
                if (var2 == 0)
                {
                    this.context.boxSize2.z = 0.25D;
                }

                if (var2 == 1)
                {
                    this.context.boxSize1.z = 0.75D;
                }

                if (var2 > 1)
                {
                    this.context.boxSize1.z = 0.25D;
                    this.context.boxSize2.z = 0.75D;
                }

                if (var2 == 2)
                {
                    this.context.boxSize2.y = 0.25D;
                }

                if (var2 == 3)
                {
                    this.context.boxSize1.y = 0.75D;
                }
        }
    }

    int innerFace(int var1, int var2)
    {
        int var3;

        switch (var1)
        {
            case 0:
            case 1:
                var3 = 67637280;
                break;

            case 2:
            case 3:
                var3 = 16912416;
                break;

            default:
                var3 = 16909320;
        }

        return var3 >> var2 * 8;
    }

    boolean sizeColumnSpoke(int var1, boolean var2, float var3)
    {
        var1 = var1 - 26 + (var2 ? 3 : 0);

        switch (var1)
        {
            case 0:
                this.context.boxSize2.y = 0.5D - (double)var3;
                return 0.5D - (double)var3 > (double)this.cy1;

            case 1:
                this.context.boxSize2.z = 0.5D - (double)var3;
                return 0.5D - (double)var3 > (double)this.cz1;

            case 2:
                this.context.boxSize2.x = 0.5D - (double)var3;
                return 0.5D - (double)var3 > (double)this.cx1;

            case 3:
                this.context.boxSize2.y = (double)this.cy2;
                this.context.boxSize1.y = 0.5D + (double)var3;
                return 0.5D + (double)var3 < (double)this.cy2;

            case 4:
                this.context.boxSize2.z = (double)this.cz2;
                this.context.boxSize1.z = 0.5D + (double)var3;
                return 0.5D + (double)var3 < (double)this.cz2;

            case 5:
                this.context.boxSize2.x = (double)this.cx2;
                this.context.boxSize1.x = 0.5D + (double)var3;
                return 0.5D + (double)var3 < (double)this.cx2;

            default:
                return false;
        }
    }

    public void setSize(int var1, float var2)
    {
        switch (var1)
        {
            case 0:
                this.context.setSize((double)this.cx1, 0.0D, (double)this.cz1, (double)this.cx2, (double)var2, (double)this.cz2);
                this.cy1 = var2;
                break;

            case 1:
                this.context.setSize((double)this.cx1, (double)(1.0F - var2), (double)this.cz1, (double)this.cx2, 1.0D, (double)this.cz2);
                this.cy2 = 1.0F - var2;
                break;

            case 2:
                this.context.setSize((double)this.cx1, (double)this.cy1, 0.0D, (double)this.cx2, (double)this.cy2, (double)var2);
                this.cz1 = var2;
                break;

            case 3:
                this.context.setSize((double)this.cx1, (double)this.cy1, (double)(1.0F - var2), (double)this.cx2, (double)this.cy2, 1.0D);
                this.cz2 = 1.0F - var2;
                break;

            case 4:
                this.context.setSize(0.0D, (double)this.cy1, (double)this.cz1, (double)var2, (double)this.cy2, (double)this.cz2);
                this.cx1 = var2;
                break;

            case 5:
                this.context.setSize((double)(1.0F - var2), (double)this.cy1, (double)this.cz1, 1.0D, (double)this.cy2, (double)this.cz2);
                this.cx2 = 1.0F - var2;
                break;

            case 6:
                this.context.setSize((double)this.cx1, (double)this.cy1, (double)this.cz1, (double)var2, (double)var2, (double)var2);
                this.x1[0] = var2;
                this.y1[0] = var2;
                this.z1[0] = var2;
                break;

            case 7:
                this.context.setSize((double)this.cx1, (double)this.cy1, (double)(1.0F - var2), (double)var2, (double)var2, (double)this.cz2);
                this.x1[1] = var2;
                this.y1[1] = var2;
                this.z2[0] = 1.0F - var2;
                break;

            case 8:
                this.context.setSize((double)(1.0F - var2), (double)this.cy1, (double)this.cz1, (double)this.cx2, (double)var2, (double)var2);
                this.x2[0] = 1.0F - var2;
                this.y1[2] = var2;
                this.z1[1] = var2;
                break;

            case 9:
                this.context.setSize((double)(1.0F - var2), (double)this.cy1, (double)(1.0F - var2), (double)this.cx2, (double)var2, (double)this.cz2);
                this.x2[1] = 1.0F - var2;
                this.y1[3] = var2;
                this.z2[1] = 1.0F - var2;
                break;

            case 10:
                this.context.setSize((double)this.cx1, (double)(1.0F - var2), (double)this.cz1, (double)var2, (double)this.cy2, (double)var2);
                this.x1[2] = var2;
                this.y2[0] = 1.0F - var2;
                this.z1[2] = var2;
                break;

            case 11:
                this.context.setSize((double)this.cx1, (double)(1.0F - var2), (double)(1.0F - var2), (double)var2, (double)this.cy2, (double)this.cz2);
                this.x1[3] = var2;
                this.y2[1] = 1.0F - var2;
                this.z2[2] = 1.0F - var2;
                break;

            case 12:
                this.context.setSize((double)(1.0F - var2), (double)(1.0F - var2), (double)this.cz1, (double)this.cx2, (double)this.cy2, (double)var2);
                this.x2[2] = 1.0F - var2;
                this.y2[2] = 1.0F - var2;
                this.z1[3] = var2;
                break;

            case 13:
                this.context.setSize((double)(1.0F - var2), (double)(1.0F - var2), (double)(1.0F - var2), (double)this.cx2, (double)this.cy2, (double)this.cz2);
                this.x2[3] = 1.0F - var2;
                this.y2[3] = 1.0F - var2;
                this.z2[3] = 1.0F - var2;
                break;

            case 14:
                this.context.setSize((double)this.x1[0], (double)this.cy1, (double)this.cz1, (double)this.x2[0], (double)var2, (double)var2);
                this.z1[0] = Math.max(this.z1[0], var2);
                this.z1[1] = Math.max(this.z1[1], var2);
                this.y1[0] = Math.max(this.y1[0], var2);
                this.y1[2] = Math.max(this.y1[2], var2);
                break;

            case 15:
                this.context.setSize((double)this.x1[1], (double)this.cy1, (double)(1.0F - var2), (double)this.x2[1], (double)var2, (double)this.cz2);
                this.z2[0] = Math.min(this.z2[0], 1.0F - var2);
                this.z2[1] = Math.min(this.z2[1], 1.0F - var2);
                this.y1[1] = Math.max(this.y1[1], var2);
                this.y1[3] = Math.max(this.y1[3], var2);
                break;

            case 16:
                this.context.setSize((double)this.cx1, (double)this.cy1, (double)this.z1[0], (double)var2, (double)var2, (double)this.z2[0]);
                this.x1[0] = Math.max(this.x1[0], var2);
                this.x1[1] = Math.max(this.x1[1], var2);
                this.y1[0] = Math.max(this.y1[0], var2);
                this.y1[1] = Math.max(this.y1[1], var2);
                break;

            case 17:
                this.context.setSize((double)(1.0F - var2), (double)this.cy1, (double)this.z1[1], (double)this.cx2, (double)var2, (double)this.z2[1]);
                this.x2[0] = Math.min(this.x2[0], 1.0F - var2);
                this.x2[1] = Math.min(this.x2[1], 1.0F - var2);
                this.y1[2] = Math.max(this.y1[2], var2);
                this.y1[3] = Math.max(this.y1[3], var2);
                break;

            case 18:
                this.context.setSize((double)this.cx1, (double)this.y1[0], (double)this.cz1, (double)var2, (double)this.y2[0], (double)var2);
                this.x1[0] = Math.max(this.x1[0], var2);
                this.x1[2] = Math.max(this.x1[2], var2);
                this.z1[0] = Math.max(this.z1[0], var2);
                this.z1[2] = Math.max(this.z1[2], var2);
                break;

            case 19:
                this.context.setSize((double)this.cx1, (double)this.y1[1], (double)(1.0F - var2), (double)var2, (double)this.y2[1], (double)this.cz2);
                this.x1[1] = Math.max(this.x1[1], var2);
                this.x1[3] = Math.max(this.x1[3], var2);
                this.z2[0] = Math.min(this.z2[0], 1.0F - var2);
                this.z2[2] = Math.min(this.z2[2], 1.0F - var2);
                break;

            case 20:
                this.context.setSize((double)(1.0F - var2), (double)this.y1[2], (double)this.cz1, (double)this.cx2, (double)this.y2[2], (double)var2);
                this.x2[0] = Math.min(this.x2[0], 1.0F - var2);
                this.x2[2] = Math.min(this.x2[2], 1.0F - var2);
                this.z1[1] = Math.max(this.z1[1], var2);
                this.z1[3] = Math.max(this.z1[3], var2);
                break;

            case 21:
                this.context.setSize((double)(1.0F - var2), (double)this.y1[3], (double)(1.0F - var2), (double)this.cx2, (double)this.y2[3], (double)this.cz2);
                this.x2[1] = Math.min(this.x2[1], 1.0F - var2);
                this.x2[3] = Math.min(this.x2[3], 1.0F - var2);
                this.z2[1] = Math.min(this.z2[1], 1.0F - var2);
                this.z2[3] = Math.min(this.z2[3], 1.0F - var2);
                break;

            case 22:
                this.context.setSize((double)this.x1[2], (double)(1.0F - var2), (double)this.cz1, (double)this.x2[2], (double)this.cy2, (double)var2);
                this.z1[2] = Math.max(this.z1[2], var2);
                this.z1[3] = Math.max(this.z1[3], var2);
                this.y2[0] = Math.min(this.y2[0], 1.0F - var2);
                this.y2[2] = Math.min(this.y2[2], 1.0F - var2);
                break;

            case 23:
                this.context.setSize((double)this.x1[3], (double)(1.0F - var2), (double)(1.0F - var2), (double)this.x2[3], (double)this.cy2, (double)this.cz2);
                this.z2[2] = Math.max(this.z2[2], 1.0F - var2);
                this.z2[3] = Math.max(this.z2[3], 1.0F - var2);
                this.y2[1] = Math.min(this.y2[1], 1.0F - var2);
                this.y2[3] = Math.min(this.y2[3], 1.0F - var2);
                break;

            case 24:
                this.context.setSize((double)this.cx1, (double)(1.0F - var2), (double)this.z1[2], (double)var2, (double)this.cy2, (double)this.z2[2]);
                this.x1[2] = Math.max(this.x1[2], var2);
                this.x1[3] = Math.max(this.x1[3], var2);
                this.y2[0] = Math.min(this.y2[0], 1.0F - var2);
                this.y2[1] = Math.min(this.y2[1], 1.0F - var2);
                break;

            case 25:
                this.context.setSize((double)(1.0F - var2), (double)(1.0F - var2), (double)this.z1[3], (double)this.cx2, (double)this.cy2, (double)this.z2[3]);
                this.x2[2] = Math.min(this.x2[2], 1.0F - var2);
                this.x2[3] = Math.min(this.x2[3], 1.0F - var2);
                this.y2[2] = Math.min(this.y2[2], 1.0F - var2);
                this.y2[3] = Math.min(this.y2[3], 1.0F - var2);
                break;

            case 26:
                this.context.setSize(0.5D - (double)var2, (double)this.cy1, 0.5D - (double)var2, 0.5D + (double)var2, (double)this.cy2, 0.5D + (double)var2);
                break;

            case 27:
                this.context.setSize(0.5D - (double)var2, 0.5D - (double)var2, (double)this.cz1, 0.5D + (double)var2, 0.5D + (double)var2, (double)this.cz2);
                break;

            case 28:
                this.context.setSize((double)this.cx1, 0.5D - (double)var2, 0.5D - (double)var2, (double)this.cx2, 0.5D + (double)var2, 0.5D + (double)var2);
        }
    }

    void setupCorners()
    {
        for (int var1 = 0; var1 < 4; ++var1)
        {
            this.x1[var1] = this.cx1;
            this.y1[var1] = this.cy1;
            this.z1[var1] = this.cz1;
            this.x2[var1] = this.cx2;
            this.y2[var1] = this.cy2;
            this.z2[var1] = this.cz2;
        }
    }

    public void initMasks(int var1, short[] var2)
    {
        this.covmask = var1;
        this.covs = var2;
        this.covmaskt = 0;
        this.covmaskh = 0;
        this.covmasko = 0;

        for (int var3 = 0; var3 < 6; ++var3)
        {
            if ((var1 & 1 << var3) != 0)
            {
                if (CoverLib.isTransparent(this.covs[var3] & 255))
                {
                    this.covmaskt |= 1 << var3;
                }

                if (this.covs[var3] >> 8 > 2)
                {
                    this.covmaskh |= 1 << var3;
                }
            }
        }

        this.covmasko = this.covmask & ~this.covmaskt & ~this.covmaskh;
    }

    public void render(int var1, short[] var2)
    {
        this.initMasks(var1, var2);
        this.start();
        this.renderShell();

        if ((var1 & -64) == 0)
        {
            this.context.clearTexFiles();
        }
        else
        {
            this.renderOthers();
        }
    }

    public void renderShrink(int var1, short[] var2, float var3)
    {
        this.initMasks(var1, var2);
        this.startShrink(var3);
        this.renderShell();

        if ((var1 & -64) == 0)
        {
            this.context.clearTexFiles();
        }
        else
        {
            this.renderOthers();
        }
    }

    private String getTerrain(String var1)
    {
        return var1 == null ? "/terrain.png" : var1;
    }

    public void setTex(int var1)
    {
        this.context.setTex(coverTextures[var1]);
        this.context.setTexFile(this.getTerrain(coverTextureFiles[var1]));
    }

    public void setTex(int var1, int var2, int var3, int var4, int var5, int var6)
    {
        this.context.setTex(coverTextures[var1][0], coverTextures[var2][1], coverTextures[var3][2], coverTextures[var4][3], coverTextures[var5][4], coverTextures[var6][5]);
        this.context.setTexFiles(this.getTerrain(coverTextureFiles[var1]), this.getTerrain(coverTextureFiles[var2]), this.getTerrain(coverTextureFiles[var3]), this.getTerrain(coverTextureFiles[var4]), this.getTerrain(coverTextureFiles[var5]), this.getTerrain(coverTextureFiles[var6]));
    }

    public void renderShell()
    {
        this.context.setOrientation(0, 0);
        this.context.setLocalLights(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);

        if (this.covmasko > 0)
        {
            this.context.setSize(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.setTex(this.covs[0] & 255, this.covs[1] & 255, this.covs[2] & 255, this.covs[3] & 255, this.covs[4] & 255, this.covs[5] & 255);
            this.context.setTexFlags(55);
            this.context.calcBoundsGlobal();
            this.context.renderGlobFaces(this.covmasko);
        }

        int var1 = (this.covmasko | this.covmaskh) & ~this.covmaskt;
        int var2;
        int var3;
        int var4;
        int var5;

        if (var1 > 0)
        {
            for (var2 = 0; var2 < 6; ++var2)
            {
                if ((var1 & 1 << var2) != 0)
                {
                    this.setTex(this.covs[var2] & 255);
                    var3 = this.covs[var2] >> 8;
                    var4 = 1 << (var2 ^ 1) | 63 ^ this.covmasko;

                    if ((var3 < 3 || var3 > 5) && (var3 < 10 || var3 > 13))
                    {
                        this.setSize(var2, CoverLib.getThickness(var2, this.covs[var2]));
                        this.context.calcBoundsGlobal();
                        this.context.renderGlobFaces(var4);
                    }
                    else
                    {
                        for (var5 = 0; var5 < 4; ++var5)
                        {
                            this.setSize(var2, CoverLib.getThickness(var2, this.covs[var2]));
                            this.sizeHollow(var2, var5);
                            this.context.calcBoundsGlobal();
                            this.context.renderGlobFaces(var4 | this.innerFace(var2, var5));
                        }
                    }
                }
            }
        }

        if (this.covmaskt > 0)
        {
            for (var2 = 0; var2 < 6; ++var2)
            {
                if ((this.covmaskt & 1 << var2) != 0)
                {
                    this.setTex(this.covs[var2] & 255);
                    var3 = this.covs[var2] >> 8;
                    var4 = 1 << (var2 ^ 1) | 63 ^ this.covmasko;

                    if ((var3 < 3 || var3 > 5) && (var3 < 10 || var3 > 13))
                    {
                        this.setSize(var2, CoverLib.getThickness(var2, this.covs[var2]));
                        this.context.calcBoundsGlobal();
                        this.context.renderGlobFaces(var4);
                    }
                    else
                    {
                        for (var5 = 0; var5 < 4; ++var5)
                        {
                            this.setSize(var2, CoverLib.getThickness(var2, this.covs[var2]));
                            this.sizeHollow(var2, var5);
                            this.context.calcBoundsGlobal();
                            this.context.renderGlobFaces(var4 | this.innerFace(var2, var5));
                        }
                    }
                }
            }
        }
    }

    public void renderOthers()
    {
        float var1 = 0.0F;
        int var2 = 0;
        int var3 = 0;
        int var4;

        for (var4 = 26; var4 < 29; ++var4)
        {
            if ((this.covmasko & 1 << var4) != 0)
            {
                ++var2;
                float var5 = CoverLib.getThickness(var4, this.covs[var4]);

                if (var5 > var1)
                {
                    var3 = var4;
                    var1 = var5;
                }
            }
        }

        if (var2 > 1)
        {
            this.setTex(this.covs[var3] & 255);
            this.context.setSize(0.5D - (double)var1, 0.5D - (double)var1, 0.5D - (double)var1, 0.5D + (double)var1, 0.5D + (double)var1, 0.5D + (double)var1);
            this.context.calcBoundsGlobal();
            this.context.renderGlobFaces(63);

            for (var4 = 26; var4 < 29; ++var4)
            {
                if ((this.covmasko & 1 << var4) != 0)
                {
                    this.setTex(this.covs[var4] & 255);
                    this.setSize(var4, CoverLib.getThickness(var4, this.covs[var4]));

                    if (this.sizeColumnSpoke(var4, false, var1))
                    {
                        this.context.calcBoundsGlobal();
                        this.context.renderGlobFaces(63);
                    }

                    if (this.sizeColumnSpoke(var4, true, var1))
                    {
                        this.context.calcBoundsGlobal();
                        this.context.renderGlobFaces(63);
                    }
                }
            }
        }
        else if (var2 == 1)
        {
            this.setTex(this.covs[var3] & 255);
            this.setSize(var3, CoverLib.getThickness(var3, this.covs[var3]));
            this.context.calcBoundsGlobal();
            this.context.renderGlobFaces(63 ^ 3 << var3 - 25 & this.covmasko);
        }

        this.setupCorners();

        for (var4 = 6; var4 < 14; ++var4)
        {
            if ((this.covmasko & 1 << var4) != 0)
            {
                this.setSize(var4, CoverLib.getThickness(var4, this.covs[var4]));
                this.context.calcBoundsGlobal();
                this.setTex(this.covs[var4] & 255);
                this.context.renderGlobFaces(63);
            }
        }

        for (var4 = 6; var4 >= 0; --var4)
        {
            for (int var6 = 14; var6 < 26; ++var6)
            {
                if ((this.covmasko & 1 << var6) != 0 && this.covs[var6] >> 8 == var4)
                {
                    this.setSize(var6, CoverLib.getThickness(var6, this.covs[var6]));
                    this.context.calcBoundsGlobal();
                    this.setTex(this.covs[var6] & 255);
                    this.context.renderGlobFaces(63);
                }
            }
        }

        this.context.clearTexFiles();
    }
}
