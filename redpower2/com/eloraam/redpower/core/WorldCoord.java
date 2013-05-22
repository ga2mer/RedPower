package com.eloraam.redpower.core;

import com.eloraam.redpower.core.WorldCoord$1;
import com.eloraam.redpower.core.WorldCoord$WCComparator;
import java.util.Comparator;
import net.minecraft.tileentity.TileEntity;

public class WorldCoord implements Comparable
{
    public int x;
    public int y;
    public int z;

    public WorldCoord(int var1, int var2, int var3)
    {
        this.x = var1;
        this.y = var2;
        this.z = var3;
    }

    public WorldCoord(TileEntity var1)
    {
        this.x = var1.xCoord;
        this.y = var1.yCoord;
        this.z = var1.zCoord;
    }

    public WorldCoord copy()
    {
        return new WorldCoord(this.x, this.y, this.z);
    }

    public WorldCoord coordStep(int var1)
    {
        switch (var1)
        {
            case 0:
                return new WorldCoord(this.x, this.y - 1, this.z);

            case 1:
                return new WorldCoord(this.x, this.y + 1, this.z);

            case 2:
                return new WorldCoord(this.x, this.y, this.z - 1);

            case 3:
                return new WorldCoord(this.x, this.y, this.z + 1);

            case 4:
                return new WorldCoord(this.x - 1, this.y, this.z);

            default:
                return new WorldCoord(this.x + 1, this.y, this.z);
        }
    }

    public void set(WorldCoord var1)
    {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
    }

    public int squareDist(int var1, int var2, int var3)
    {
        return (var1 - this.x) * (var1 - this.x) + (var2 - this.y) * (var2 - this.y) + (var3 - this.z) * (var3 - this.z);
    }

    public void step(int var1)
    {
        switch (var1)
        {
            case 0:
                --this.y;
                break;

            case 1:
                ++this.y;
                break;

            case 2:
                --this.z;
                break;

            case 3:
                ++this.z;
                break;

            case 4:
                --this.x;
                break;

            default:
                ++this.x;
        }
    }

    public void step(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.y -= var2;
                break;

            case 1:
                this.y += var2;
                break;

            case 2:
                this.z -= var2;
                break;

            case 3:
                this.z += var2;
                break;

            case 4:
                this.x -= var2;
                break;

            default:
                this.x += var2;
        }
    }

    public static int getRightDir(int var0)
    {
        if (var0 < 2)
        {
            return var0;
        }
        else
        {
            switch (var0)
            {
                case 0:
                    return 0;

                case 1:
                    return 1;

                case 2:
                    return 4;

                case 3:
                    return 5;

                case 4:
                    return 3;

                default:
                    return 2;
            }
        }
    }

    public static int getIndStepDir(int var0, int var1)
    {
        switch (var0)
        {
            case 0:
                return var1 + 2;

            case 1:
                return var1 + 2;

            case 2:
                return var1 + (var1 & 2);

            case 3:
                return var1 + (var1 & 2);

            case 4:
                return var1;

            default:
                return var1;
        }
    }

    public void indStep(int var1, int var2)
    {
        this.step(var1);
        this.step(getIndStepDir(var1, var2));
    }

    public int hashCode()
    {
        int var1 = Integer.valueOf(this.x).hashCode();
        int var2 = Integer.valueOf(this.y).hashCode();
        int var3 = Integer.valueOf(this.z).hashCode();
        return var1 + 31 * (var2 + 31 * var3);
    }

    public int compareTo(Object var1)
    {
        WorldCoord var2 = (WorldCoord)var1;
        return this.x == var2.x ? (this.y == var2.y ? this.z - var2.z : this.y - var2.y) : this.x - var2.x;
    }

    public boolean equals(Object var1)
    {
        if (!(var1 instanceof WorldCoord))
        {
            return false;
        }
        else
        {
            WorldCoord var2 = (WorldCoord)var1;
            return this.x == var2.x && this.y == var2.y && this.z == var2.z;
        }
    }

    public static Comparator getCompareDir(int var0)
    {
        return new WorldCoord$WCComparator(var0, (WorldCoord$1)null);
    }
}
