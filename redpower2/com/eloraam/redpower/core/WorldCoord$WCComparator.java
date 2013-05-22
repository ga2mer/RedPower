package com.eloraam.redpower.core;

import com.eloraam.redpower.core.WorldCoord$1;
import java.util.Comparator;

public class WorldCoord$WCComparator implements Comparator
{
    int dir;

    private WorldCoord$WCComparator(int var1)
    {
        this.dir = var1;
    }

    public int compare(Object var1, Object var2)
    {
        WorldCoord var3 = (WorldCoord)var1;
        WorldCoord var4 = (WorldCoord)var2;

        switch (this.dir)
        {
            case 0:
                return var3.y - var4.y;

            case 1:
                return var4.y - var3.y;

            case 2:
                return var3.z - var4.z;

            case 3:
                return var4.z - var3.z;

            case 4:
                return var3.x - var4.x;

            default:
                return var4.x - var3.x;
        }
    }

    WorldCoord$WCComparator(int var1, WorldCoord$1 var2)
    {
        this(var1);
    }
}
