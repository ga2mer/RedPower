package com.eloraam.redpower.world;

import com.eloraam.redpower.core.WorldCoord;

public class ItemSeedBag$SpiralSearch
{
    int curs;
    int rem;
    int ln;
    int steps;
    public WorldCoord point;

    public ItemSeedBag$SpiralSearch(WorldCoord var1, int var2)
    {
        this.point = var1;
        this.curs = 0;
        this.rem = 1;
        this.ln = 1;
        this.steps = var2 * var2;
    }

    public boolean again()
    {
        return this.steps > 0;
    }

    public boolean step()
    {
        if (--this.steps == 0)
        {
            return false;
        }
        else
        {
            --this.rem;

            switch (this.curs)
            {
                case 0:
                    this.point.step(2);
                    break;

                case 1:
                    this.point.step(4);
                    break;

                case 2:
                    this.point.step(3);
                    break;

                default:
                    this.point.step(5);
            }

            if (this.rem > 0)
            {
                return true;
            }
            else
            {
                this.curs = this.curs + 1 & 3;
                this.rem = this.ln;

                if ((this.curs & 1) > 0)
                {
                    ++this.ln;
                }

                return true;
            }
        }
    }
}
