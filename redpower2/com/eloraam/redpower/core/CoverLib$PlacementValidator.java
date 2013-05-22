package com.eloraam.redpower.core;

class CoverLib$PlacementValidator
{
    public int sidemask = 0;
    public int cornermask = 0;
    public int fillcornermask = 0;
    public int hollowcornermask = 0;
    public int thickfaces = 0;
    public int covm;
    public short[] covs;
    public int[] quanta = new int[29];

    public CoverLib$PlacementValidator(int var1, short[] var2)
    {
        this.covm = var1;
        this.covs = var2;
    }

    public boolean checkThickFace(int var1)
    {
        for (int var3 = 0; var3 < 6; ++var3)
        {
            if ((this.covm & 1 << var3) != 0 && this.covs[var3] >> 8 == var1)
            {
                int var2 = CoverLib.coverToCornerMask(var3);

                if ((this.fillcornermask & var2) > 0)
                {
                    return false;
                }

                this.fillcornermask |= var2;
                this.sidemask |= CoverLib.coverToStripMask(var3);
            }
        }

        return true;
    }

    public boolean checkThickSide(int var1)
    {
        for (int var3 = 0; var3 < 12; ++var3)
        {
            if ((this.covm & 1 << var3 + 14) != 0 && this.covs[var3 + 14] >> 8 == var1)
            {
                int var2 = CoverLib.stripToCornerMask(var3);

                if ((this.fillcornermask & var2) > 0)
                {
                    return false;
                }

                this.fillcornermask |= var2;
                this.sidemask |= 1 << var3;
            }
        }

        return true;
    }

    public boolean checkThickCorner(int var1)
    {
        for (int var3 = 0; var3 < 8; ++var3)
        {
            if ((this.covm & 1 << var3 + 6) != 0 && this.covs[var3 + 6] >> 8 == var1)
            {
                int var2 = 1 << var3;

                if ((this.fillcornermask & var2) == var2)
                {
                    return false;
                }

                this.fillcornermask |= var2;
            }
        }

        return true;
    }

    public boolean checkFace(int var1)
    {
        for (int var3 = 0; var3 < 6; ++var3)
        {
            if ((this.covm & 1 << var3) != 0 && this.covs[var3] >> 8 == var1)
            {
                int var2 = CoverLib.coverToCornerMask(var3);

                if ((this.fillcornermask & var2) == var2)
                {
                    return false;
                }

                this.cornermask |= var2;
                this.sidemask |= CoverLib.coverToStripMask(var3);
            }
        }

        return true;
    }

    public boolean checkSide(int var1)
    {
        for (int var3 = 0; var3 < 12; ++var3)
        {
            if ((this.covm & 1 << var3 + 14) != 0 && this.covs[var3 + 14] >> 8 == var1)
            {
                int var2 = CoverLib.stripToCornerMask(var3);

                if ((this.fillcornermask & var2) == var2)
                {
                    return false;
                }

                if ((this.sidemask & 1 << var3) > 0)
                {
                    return false;
                }

                this.cornermask |= var2;
                this.sidemask |= 1 << var3;
            }
        }

        return true;
    }

    public boolean checkCorner(int var1)
    {
        for (int var3 = 0; var3 < 8; ++var3)
        {
            if ((this.covm & 1 << var3 + 6) != 0 && this.covs[var3 + 6] >> 8 == var1)
            {
                int var2 = 1 << var3;

                if ((this.cornermask & var2) == var2)
                {
                    return false;
                }

                this.cornermask |= var2;
            }
        }

        return true;
    }

    public boolean checkHollow(int var1)
    {
        for (int var3 = 0; var3 < 6; ++var3)
        {
            if ((this.covm & 1 << var3) != 0 && this.covs[var3] >> 8 == var1)
            {
                int var2 = CoverLib.coverToCornerMask(var3);

                if ((this.cornermask & var2) > 0)
                {
                    return false;
                }

                this.cornermask |= var2;
                this.hollowcornermask |= var2;
                var2 = CoverLib.coverToStripMask(var3);

                if ((this.sidemask & var2) > 0)
                {
                    return false;
                }

                this.sidemask |= var2;
            }
        }

        return true;
    }

    public boolean checkHollowCover(int var1)
    {
        int var3 = 0;
        int var4 = 0;

        for (int var5 = 0; var5 < 6; ++var5)
        {
            if ((this.covm & 1 << var5) != 0 && this.covs[var5] >> 8 == var1)
            {
                int var2 = CoverLib.coverToCornerMask(var5);

                if ((this.cornermask & var2) > 0)
                {
                    return false;
                }

                var3 |= var2;
                var2 = CoverLib.coverToStripMask(var5);

                if ((this.sidemask & var2) > 0)
                {
                    return false;
                }

                var4 |= var2;
            }
        }

        this.cornermask |= var3;
        this.sidemask |= var4;
        return true;
    }

    public void calcQuanta()
    {
        for (int var1 = 0; var1 < 29; ++var1)
        {
            if ((this.covm & 1 << var1) == 0)
            {
                this.quanta[var1] = 0;
            }
            else
            {
                this.quanta[var1] = CoverLib.getThicknessQuanta(var1, this.covs[var1]);
            }
        }
    }

    private boolean checkOverlap(int var1, int var2, int var3, int var4)
    {
        var1 = this.quanta[var1];
        var2 = this.quanta[var2];
        var3 = this.quanta[var3];
        var4 = this.quanta[var4];
        return var1 + var2 > 8 || var1 + var3 > 8 || var1 + var4 > 8 || var2 + var3 > 8 || var2 + var4 > 8 || var3 + var4 > 8;
    }

    public boolean checkImpingement()
    {
        int var1;

        for (var1 = 0; var1 < 6; var1 += 2)
        {
            if (this.quanta[var1] + this.quanta[var1 + 1] > 8)
            {
                return false;
            }
        }

        if (this.checkOverlap(14, 15, 22, 23))
        {
            return false;
        }
        else if (this.checkOverlap(16, 17, 24, 25))
        {
            return false;
        }
        else if (this.checkOverlap(18, 19, 20, 22))
        {
            return false;
        }
        else if (this.checkOverlap(6, 7, 8, 9))
        {
            return false;
        }
        else if (this.checkOverlap(10, 11, 12, 13))
        {
            return false;
        }
        else if (this.checkOverlap(6, 8, 10, 12))
        {
            return false;
        }
        else if (this.checkOverlap(7, 9, 11, 13))
        {
            return false;
        }
        else if (this.checkOverlap(6, 7, 10, 11))
        {
            return false;
        }
        else if (this.checkOverlap(8, 9, 12, 13))
        {
            return false;
        }
        else
        {
            int var2;
            int var3;
            int var4;
            int var5;

            for (var1 = 0; var1 < 6; ++var1)
            {
                var2 = this.quanta[var1];

                if (var2 != 0)
                {
                    var3 = CoverLib.coverToCornerMask(var1);
                    var4 = CoverLib.coverToStripMask(var1);
                    var5 = CoverLib.coverToStripMask(var1 ^ 1);
                    int var6;
                    int var7;

                    for (var6 = 0; var6 < 8; ++var6)
                    {
                        var7 = this.quanta[6 + var6];

                        if ((var3 & 1 << var6) == 0)
                        {
                            if (var2 + var7 > 8)
                            {
                                return false;
                            }
                        }
                        else if (var7 > 0 && var7 < var2)
                        {
                            return false;
                        }
                    }

                    for (var6 = 0; var6 < 12; ++var6)
                    {
                        var7 = this.quanta[14 + var6];

                        if ((var5 & 1 << var6) > 0)
                        {
                            if (var2 + var7 > 8)
                            {
                                return false;
                            }
                        }
                        else if ((var4 & 1 << var6) > 0 && var7 > 0 && var7 < var2)
                        {
                            return false;
                        }
                    }
                }
            }

            for (var1 = 0; var1 < 12; ++var1)
            {
                var2 = this.quanta[14 + var1];

                if (var2 != 0)
                {
                    var3 = CoverLib.stripToCornerMask(var1);

                    for (var4 = 0; var4 < 8; ++var4)
                    {
                        var5 = this.quanta[6 + var4];

                        if ((var3 & 1 << var4) == 0)
                        {
                            if (var2 + var5 > 8)
                            {
                                return false;
                            }
                        }
                        else if (var5 > 0 && var5 < var2)
                        {
                            return false;
                        }
                    }
                }
            }

            for (var1 = 0; var1 < 3; ++var1)
            {
                var2 = this.quanta[26 + var1];

                if (var2 != 0)
                {
                    for (var3 = 0; var3 < 8; ++var3)
                    {
                        var4 = this.quanta[6 + var3];

                        if (var2 + var4 > 4)
                        {
                            return false;
                        }
                    }

                    for (var3 = 0; var3 < 12; ++var3)
                    {
                        var4 = this.quanta[14 + var3];

                        if (var2 + var4 > 4)
                        {
                            return false;
                        }
                    }

                    for (var3 = 0; var3 < 6; ++var3)
                    {
                        if (var3 >> 1 != var1 && this.quanta[var3] + var2 > 4)
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    public boolean checkPlacement(int var1, boolean var2)
    {
        this.calcQuanta();

        if (!this.checkImpingement())
        {
            return false;
        }
        else if (!this.checkThickFace(9))
        {
            return false;
        }
        else if (!this.checkThickSide(6))
        {
            return false;
        }
        else if (!this.checkThickCorner(6))
        {
            return false;
        }
        else if (!this.checkThickFace(8))
        {
            return false;
        }
        else if (!this.checkThickSide(5))
        {
            return false;
        }
        else if (!this.checkThickCorner(5))
        {
            return false;
        }
        else if (!this.checkThickFace(7))
        {
            return false;
        }
        else if (!this.checkThickSide(4))
        {
            return false;
        }
        else if (!this.checkThickCorner(4))
        {
            return false;
        }
        else if (this.cornermask > 0 && var1 > 0)
        {
            return false;
        }
        else if (!this.checkThickFace(2))
        {
            return false;
        }
        else if (!this.checkThickSide(2))
        {
            return false;
        }
        else if (!this.checkThickCorner(2))
        {
            return false;
        }
        else
        {
            this.cornermask = this.fillcornermask;

            if (!this.checkFace(6))
            {
                return false;
            }
            else if (!this.checkSide(3))
            {
                return false;
            }
            else if (!this.checkCorner(3))
            {
                return false;
            }
            else
            {
                if ((this.covm & 469762048) > 0)
                {
                    if (var2)
                    {
                        return false;
                    }

                    if (var1 > 0)
                    {
                        return false;
                    }
                }

                int var3;

                for (var3 = 0; var3 < 6; ++var3)
                {
                    if ((var1 & 1 << var3) != 0 && (this.cornermask & CoverLib.coverToCornerMask(var3)) > 0)
                    {
                        return false;
                    }
                }

                if (!this.checkFace(1))
                {
                    return false;
                }
                else if (!this.checkSide(1))
                {
                    return false;
                }
                else if (!this.checkCorner(1))
                {
                    return false;
                }
                else if (var2 && (this.cornermask > 0 || this.sidemask > 0))
                {
                    return false;
                }
                else if (!this.checkHollow(13))
                {
                    return false;
                }
                else if (!this.checkHollow(12))
                {
                    return false;
                }
                else if (!this.checkHollow(11))
                {
                    return false;
                }
                else if (!this.checkHollow(10))
                {
                    return false;
                }
                else if (!this.checkHollow(5))
                {
                    return false;
                }
                else
                {
                    for (var3 = 0; var3 < 6; ++var3)
                    {
                        if ((var1 & 1 << var3) != 0 && (this.hollowcornermask & CoverLib.coverToCornerMask(var3)) > 0)
                        {
                            return false;
                        }
                    }

                    if (!this.checkHollow(4))
                    {
                        return false;
                    }
                    else if (!this.checkHollowCover(3))
                    {
                        return false;
                    }
                    else if (!this.checkFace(0))
                    {
                        return false;
                    }
                    else if (!this.checkSide(0))
                    {
                        return false;
                    }
                    else if (!this.checkCorner(0))
                    {
                        return false;
                    }
                    else
                    {
                        for (var3 = 0; var3 < 12; ++var3)
                        {
                            if ((this.covm & 1 << var3 + 14) != 0)
                            {
                                int var4 = CoverLib.stripToCoverMask(var3);

                                if ((var1 & var4) == var4)
                                {
                                    return false;
                                }
                            }
                        }

                        return true;
                    }
                }
            }
        }
    }
}
