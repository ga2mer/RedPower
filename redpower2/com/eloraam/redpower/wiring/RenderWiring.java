package com.eloraam.redpower.wiring;

import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.RenderCovers;
import net.minecraft.block.Block;

public abstract class RenderWiring extends RenderCovers
{
    private float wireWidth;
    private float wireHeight;
    private int[][] sidetex = new int[7][6];

    public RenderWiring(Block var1)
    {
        super(var1);
    }

    public void setWireSize(float var1, float var2)
    {
        this.wireWidth = var1 * 0.5F;
        this.wireHeight = var2;
    }

    public void renderSideWire(int var1)
    {
        this.context.setLocalLights(0.5F, 1.0F, 0.7F, 0.7F, 0.7F, 0.7F);

        switch (var1)
        {
            case 2:
                this.context.setSize(0.0D, 0.0D, (double)(0.5F - this.wireWidth), (double)(0.5F - this.wireWidth), (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                this.context.calcBounds();
                this.context.renderFaces(54);
                break;

            case 3:
                this.context.setSize((double)(0.5F + this.wireWidth), 0.0D, (double)(0.5F - this.wireWidth), 1.0D, (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                this.context.calcBounds();
                this.context.renderFaces(58);
                break;

            case 4:
                this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, 0.0D, (double)(0.5F + this.wireWidth), (double)this.wireHeight, (double)(0.5F - this.wireWidth));
                this.context.calcBounds();
                this.context.renderFaces(30);
                break;

            case 5:
                this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, (double)(0.5F + this.wireWidth), (double)(0.5F + this.wireWidth), (double)this.wireHeight, 1.0D);
                this.context.calcBounds();
                this.context.renderFaces(46);
        }
    }

    public void setSideTex(int var1, int var2, int var3)
    {
        int var5;

        for (var5 = 0; var5 < 6; ++var5)
        {
            this.sidetex[0][var5] = var5 >> 1 == 0 ? var2 : var3;
        }

        int var4;

        for (var4 = 1; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 6; ++var5)
            {
                this.sidetex[var4][var5] = var5 >> 1 == var4 ? var2 : var1;
            }
        }

        for (var4 = 1; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 6; ++var5)
            {
                this.sidetex[var4 + 2][var5] = var5 >> 1 == var4 ? var2 : var3;
            }
        }

        for (var4 = 0; var4 < 6; ++var4)
        {
            this.sidetex[5][var4] = var1;
            this.sidetex[6][var4] = var1;
        }

        this.sidetex[5][4] = var2;
        this.sidetex[5][5] = var2;
        this.sidetex[6][2] = var2;
        this.sidetex[6][3] = var2;
        this.sidetex[5][0] = var2;
        this.sidetex[6][0] = var2;
        this.context.setTex(this.sidetex);
    }

    public void setSideTexJumbo(int var1, int var2, int var3, int var4, int var5, int var6)
    {
        int var8;

        for (var8 = 0; var8 < 6; ++var8)
        {
            this.sidetex[0][var8] = var8 >> 1 == 0 ? var3 : var4;
        }

        int var7;

        for (var7 = 1; var7 < 3; ++var7)
        {
            for (var8 = 0; var8 < 6; ++var8)
            {
                this.sidetex[var7][var8] = var8 >> 1 == 0 ? var2 : (var8 >> 1 == var7 ? var5 : var1);
            }
        }

        for (var7 = 1; var7 < 3; ++var7)
        {
            for (var8 = 0; var8 < 6; ++var8)
            {
                this.sidetex[var7 + 2][var8] = var8 >> 1 == 0 ? var2 : (var8 >> 1 == var7 ? var5 : var4);
            }
        }

        for (var7 = 0; var7 < 6; ++var7)
        {
            this.sidetex[5][var7] = var2;
            this.sidetex[6][var7] = var2;
        }

        this.sidetex[5][4] = var6;
        this.sidetex[5][5] = var6;
        this.sidetex[6][2] = var6;
        this.sidetex[6][3] = var6;
        this.sidetex[5][0] = var6;
        this.sidetex[6][0] = var6;
        this.context.setTex(this.sidetex);
    }

    public void renderSideWires(int var1, int var2, int var3)
    {
        int var4 = 0;
        int var5 = 0;
        int var6 = 62;
        int var7 = 0;
        int var8 = 0;
        int var9 = 0;
        int var10 = 0;
        byte var15 = 3;
        float var11 = (var2 & 4) == 0 ? 0.0F : this.wireHeight;
        float var12 = (var2 & 8) == 0 ? 1.0F : 1.0F - this.wireHeight;
        float var13 = (var2 & 1) == 0 ? 0.0F : this.wireHeight;
        float var14 = (var2 & 2) == 0 ? 1.0F : 1.0F - this.wireHeight;
        this.context.setLocalLights(0.5F, 1.0F, 0.7F, 0.7F, 0.7F, 0.7F);
        var1 |= var2;

        if ((var1 & 12) == 0)
        {
            var5 |= 62;
            var6 = 0;

            if ((var1 & 1) == 0)
            {
                var13 = 0.26F;
            }

            if ((var1 & 2) == 0)
            {
                var14 = 0.74F;
            }

            var15 = 1;
        }
        else if ((var1 & 3) == 0)
        {
            var4 |= 62;
            var6 = 0;

            if ((var1 & 4) == 0)
            {
                var11 = 0.26F;
            }

            if ((var1 & 8) == 0)
            {
                var12 = 0.74F;
            }

            var15 = 1;
        }
        else
        {
            if ((var1 & 7) == 3)
            {
                var5 |= 28;
                var6 &= -17;
            }
            else
            {
                if ((var1 & 1) > 0)
                {
                    var9 |= 20;
                }

                if ((var1 & 2) > 0)
                {
                    var10 |= 24;
                }
            }

            if ((var1 & 11) == 3)
            {
                var5 |= 44;
                var6 &= -33;
            }
            else
            {
                if ((var1 & 1) > 0)
                {
                    var9 |= 36;
                }

                if ((var1 & 2) > 0)
                {
                    var10 |= 40;
                }
            }

            if ((var1 & 13) == 12)
            {
                var4 |= 52;
                var6 &= -5;
            }
            else
            {
                if ((var1 & 4) > 0)
                {
                    var7 |= 20;
                }

                if ((var1 & 8) > 0)
                {
                    var8 |= 36;
                }
            }

            if ((var1 & 14) == 12)
            {
                var4 |= 56;
                var6 &= -9;
            }
            else
            {
                if ((var1 & 4) > 0)
                {
                    var7 |= 24;
                }

                if ((var1 & 8) > 0)
                {
                    var8 |= 40;
                }
            }

            if ((var1 & 1) > 0)
            {
                var9 |= 2;
                var6 &= -5;
            }

            if ((var1 & 2) > 0)
            {
                var10 |= 2;
                var6 &= -9;
            }

            if ((var1 & 4) > 0)
            {
                var7 |= 2;
                var6 &= -17;
            }

            if ((var1 & 8) > 0)
            {
                var8 |= 2;
                var6 &= -33;
            }

            if ((var1 & 64) > 0)
            {
                var7 |= 1;
                var8 |= 1;
                var9 |= 1;
                var10 |= 1;
                var6 |= 1;
            }
        }

        int var16 = ~((var2 & 12) << 2);
        var4 &= var16;
        var7 &= var16;
        var8 &= var16;
        var16 = ~((var2 & 3) << 2);
        var5 &= var16;
        var9 &= var16;
        var10 &= var16;
        char var17 = 35712;
        int var18 = 217640;
        int var19 = 220032;

        switch (var3)
        {
            case 1:
            case 2:
            case 4:
                var17 = 7512;
                var19 = 220488;

            case 3:
            default:
                if (var4 > 0)
                {
                    this.context.setSize((double)var11, 0.0D, (double)(0.5F - this.wireWidth), (double)var12, (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                    this.context.calcBounds();
                    this.context.setTexFlags(var17);
                    this.context.setTexIndex(var15 + 1);
                    this.context.renderFaces(var4);
                }

                if (var5 > 0)
                {
                    this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, (double)var13, (double)(0.5F + this.wireWidth), (double)this.wireHeight, (double)var14);
                    this.context.calcBounds();
                    this.context.setTexFlags(var18);
                    this.context.setTexIndex(var15);
                    this.context.renderFaces(var5);
                }

                if (var6 > 0)
                {
                    this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, (double)(0.5F - this.wireWidth), (double)(0.5F + this.wireWidth), (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                    this.context.calcBounds();
                    this.context.setTexFlags(var19);
                    this.context.setTexIndex(0);
                    this.context.renderFaces(var6);
                }

                if (var7 > 0)
                {
                    this.context.setSize((double)var11, 0.0D, (double)(0.5F - this.wireWidth), (double)(0.5F - this.wireWidth), (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                    this.context.calcBounds();
                    this.context.setTexFlags(var17);
                    this.context.setTexIndex(var15 + 1);
                    this.context.renderFaces(var7);
                }

                if (var8 > 0)
                {
                    this.context.setSize((double)(0.5F + this.wireWidth), 0.0D, (double)(0.5F - this.wireWidth), (double)var12, (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                    this.context.calcBounds();
                    this.context.setTexFlags(var17);
                    this.context.setTexIndex(var15 + 1);
                    this.context.renderFaces(var8);
                }

                if (var9 > 0)
                {
                    this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, (double)var13, (double)(0.5F + this.wireWidth), (double)this.wireHeight, (double)(0.5F - this.wireWidth));
                    this.context.calcBounds();
                    this.context.setTexFlags(var18);
                    this.context.setTexIndex(var15);
                    this.context.renderFaces(var9);
                }

                if (var10 > 0)
                {
                    this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, (double)(0.5F + this.wireWidth), (double)(0.5F + this.wireWidth), (double)this.wireHeight, (double)var14);
                    this.context.calcBounds();
                    this.context.setTexFlags(var18);
                    this.context.setTexIndex(var15);
                    this.context.renderFaces(var10);
                }

                if (var3 < 2)
                {
                    this.context.setTexFlags(0);
                }
                else
                {
                    if ((var2 & 2) > 0)
                    {
                        this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, (double)(1.0F - this.wireHeight), (double)(0.5F + this.wireWidth), (double)this.wireHeight, 1.0D);
                        this.context.calcBounds();
                        this.context.setTexFlags(73728);
                        this.context.setTexIndex(5);
                        this.context.renderFaces(48);
                    }

                    if ((var2 & 4) > 0)
                    {
                        this.context.setSize(0.0D, 0.0D, (double)(0.5F - this.wireWidth), (double)this.wireHeight, (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                        this.context.calcBounds();

                        if (var3 != 2 && var3 != 4)
                        {
                            this.context.setTexFlags(1728);
                        }
                        else
                        {
                            this.context.setTexFlags(1152);
                        }

                        this.context.setTexIndex(6);
                        this.context.renderFaces(12);
                    }

                    if ((var2 & 8) > 0)
                    {
                        this.context.setSize((double)(1.0F - this.wireHeight), 0.0D, (double)(0.5F - this.wireWidth), 1.0D, (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                        this.context.calcBounds();

                        if (var3 != 2 && var3 != 4)
                        {
                            this.context.setTexFlags(1152);
                        }
                        else
                        {
                            this.context.setTexFlags(1728);
                        }

                        this.context.setTexIndex(6);
                        this.context.renderFaces(12);
                    }

                    this.context.setTexFlags(0);
                }
        }
    }

    public void renderEndCaps(int var1, int var2)
    {
        if (var1 != 0)
        {
            this.context.setTexIndex(5);

            if ((var1 & 1) > 0)
            {
                this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, (double)(1.0F - this.wireHeight), (double)(0.5F + this.wireWidth), (double)this.wireHeight, 1.0D);
                this.context.setRelPos(0.0D, 0.0D, -1.0D);
                this.context.setTexFlags(38444);
                this.context.setLocalLights(0.7F, 1.0F, 0.7F, 1.0F, 0.7F, 0.7F);
                this.context.calcBounds();
                this.context.renderFaces(55);
            }

            if ((var1 & 2) > 0)
            {
                this.context.setSize((double)(0.5F - this.wireWidth), 0.0D, 0.0D, (double)(0.5F + this.wireWidth), (double)this.wireHeight, (double)this.wireHeight);
                this.context.setRelPos(0.0D, 0.0D, 1.0D);
                this.context.setTexFlags(38444);
                this.context.setLocalLights(0.7F, 1.0F, 0.7F, 1.0F, 0.7F, 0.7F);
                this.context.calcBounds();
                this.context.renderFaces(59);
            }

            this.context.setTexIndex(6);

            if ((var1 & 4) > 0)
            {
                this.context.setSize((double)(1.0F - this.wireHeight), 0.0D, (double)(0.5F - this.wireWidth), 1.0D, (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                this.context.setRelPos(-1.0D, 0.0D, 0.0D);

                if (var2 != 2 && var2 != 4)
                {
                    this.context.setTexFlags(3);
                }
                else
                {
                    this.context.setTexFlags(45658);
                }

                this.context.setLocalLights(0.7F, 1.0F, 0.7F, 0.7F, 1.0F, 0.7F);
                this.context.calcBounds();
                this.context.renderFaces(31);
            }

            if ((var1 & 8) > 0)
            {
                this.context.setSize(0.0D, 0.0D, (double)(0.5F - this.wireWidth), (double)this.wireHeight, (double)this.wireHeight, (double)(0.5F + this.wireWidth));
                this.context.setRelPos(1.0D, 0.0D, 0.0D);

                if (var2 != 2 && var2 != 4)
                {
                    this.context.setTexFlags(102977);
                }
                else
                {
                    this.context.setTexFlags(24);
                }

                this.context.setLocalLights(0.7F, 1.0F, 0.7F, 0.7F, 0.7F, 1.0F);
                this.context.calcBounds();
                this.context.renderFaces(47);
            }

            this.context.setRelPos(0.0D, 0.0D, 0.0D);
        }
    }

    public void renderWireBlock(int var1, int var2, int var3, int var4)
    {
        int var5 = 0;
        var3 &= ~var4;

        if ((var1 & 1) > 0)
        {
            var5 |= 1118464;
        }

        if ((var1 & 2) > 0)
        {
            var5 |= 2236928;
        }

        if ((var1 & 4) > 0)
        {
            var5 |= 4456465;
        }

        if ((var1 & 8) > 0)
        {
            var5 |= 8912930;
        }

        if ((var1 & 16) > 0)
        {
            var5 |= 17476;
        }

        if ((var1 & 32) > 0)
        {
            var5 |= 34952;
        }

        if ((var1 & 1) > 0)
        {
            this.context.setOrientation(0, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(var2, 0), RedPowerLib.mapConToLocal(var5, 0), 0);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var4, 0), 0);
        }

        if ((var1 & 2) > 0)
        {
            this.context.setOrientation(1, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(var2, 1), RedPowerLib.mapConToLocal(var5, 1), 1);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var4, 1), 1);
        }

        if ((var1 & 4) > 0)
        {
            this.context.setOrientation(2, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(var2, 2), RedPowerLib.mapConToLocal(var5, 2), 2);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var3, 2) & 14, 2);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var4, 2), 2);
        }

        if ((var1 & 8) > 0)
        {
            this.context.setOrientation(3, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(var2, 3), RedPowerLib.mapConToLocal(var5, 3), 3);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var3, 3) & 14, 3);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var4, 3), 3);
        }

        if ((var1 & 16) > 0)
        {
            this.context.setOrientation(4, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(var2, 4), RedPowerLib.mapConToLocal(var5, 4), 4);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var3, 4) & 14, 4);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var4, 4), 4);
        }

        if ((var1 & 32) > 0)
        {
            this.context.setOrientation(5, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(var2, 5), RedPowerLib.mapConToLocal(var5, 5), 5);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var3, 5) & 14, 5);
            this.renderEndCaps(RedPowerLib.mapConToLocal(var4, 5), 5);
        }
    }

    void setJacketTexFiles(int var1, String var2, int[] var3, int var4)
    {
        String var5 = "/eloraam/wiring/redpower1.png";
        this.context.setTexFiles((var1 & 1) > 0 ? var5 : var2, (var1 & 2) > 0 ? var5 : var2, (var1 & 4) > 0 ? var5 : var2, (var1 & 8) > 0 ? var5 : var2, (var1 & 16) > 0 ? var5 : var2, (var1 & 32) > 0 ? var5 : var2);
        this.context.setTex((var1 & 1) > 0 ? var4 : var3[0], (var1 & 2) > 0 ? var4 : var3[1], (var1 & 4) > 0 ? var4 : var3[2], (var1 & 8) > 0 ? var4 : var3[3], (var1 & 16) > 0 ? var4 : var3[4], (var1 & 32) > 0 ? var4 : var3[5]);
    }

    public void renderCenterBlock(int var1, String var2, int[] var3, int var4)
    {
        if (var1 == 0)
        {
            this.setJacketTexFiles(3, var2, var3, var4);
            this.context.renderBox(63, 0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D);
            this.context.clearTexFiles();
        }
        else if (var1 == 3)
        {
            this.setJacketTexFiles(3, var2, var3, var4);
            this.context.renderBox(63, 0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
            this.context.clearTexFiles();
        }
        else if (var1 == 12)
        {
            this.setJacketTexFiles(12, var2, var3, var4);
            this.context.renderBox(63, 0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 1.0D);
            this.context.clearTexFiles();
        }
        else if (var1 == 48)
        {
            this.setJacketTexFiles(48, var2, var3, var4);
            this.context.renderBox(63, 0.0D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
            this.context.clearTexFiles();
        }
        else
        {
            if (Integer.bitCount(var1) > 1)
            {
                this.context.setTexFile(var2);
                this.context.setTex(var3);
            }
            else
            {
                int var5 = var1;

                if (var1 == 0)
                {
                    var5 = 3;
                }

                var5 = (var5 & 21) << 1 | (var5 & 42) >> 1;
                this.setJacketTexFiles(var5, var2, var3, var4);
            }

            this.context.renderBox(63 ^ var1, 0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D);

            if ((var1 & 1) > 0)
            {
                this.setJacketTexFiles(1, var2, var3, var4);
                this.context.renderBox(61, 0.25D, 0.0D, 0.25D, 0.75D, 0.25D, 0.75D);
            }

            if ((var1 & 2) > 0)
            {
                this.setJacketTexFiles(2, var2, var3, var4);
                this.context.renderBox(62, 0.25D, 0.75D, 0.25D, 0.75D, 1.0D, 0.75D);
            }

            if ((var1 & 4) > 0)
            {
                this.setJacketTexFiles(4, var2, var3, var4);
                this.context.renderBox(55, 0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.25D);
            }

            if ((var1 & 8) > 0)
            {
                this.setJacketTexFiles(8, var2, var3, var4);
                this.context.renderBox(59, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D, 1.0D);
            }

            if ((var1 & 16) > 0)
            {
                this.setJacketTexFiles(16, var2, var3, var4);
                this.context.renderBox(31, 0.0D, 0.25D, 0.25D, 0.25D, 0.75D, 0.75D);
            }

            if ((var1 & 32) > 0)
            {
                this.setJacketTexFiles(32, var2, var3, var4);
                this.context.renderBox(47, 0.75D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
            }

            this.context.clearTexFiles();
        }
    }
}
