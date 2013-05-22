package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BlockMultipart;

public class TileMagTube extends TileTube
{
    public int getTubeConnectableSides()
    {
        int var2 = 63;

        for (int var1 = 0; var1 < 6; ++var1)
        {
            if ((this.CoverSides & 1 << var1) > 0 && this.Covers[var1] >> 8 < 3)
            {
                var2 &= ~(1 << var1);
            }
        }

        return var2;
    }

    public int getSpeed()
    {
        return 128;
    }

    public int getTubeConClass()
    {
        return 18 + this.paintColor;
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        if (var2 == 29)
        {
            var1.setBlockBounds(0.125F, 0.125F, 0.125F, 0.875F, 0.875F, 0.875F);
        }
        else
        {
            super.setPartBounds(var1, var2);
        }
    }

    public int getExtendedID()
    {
        return 11;
    }
}
