package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import net.minecraft.block.Block;

public class TileIgniter extends TileMachine
{
    public int getExtendedID()
    {
        return 12;
    }

    private void fireAction()
    {
        WorldCoord var1 = new WorldCoord(this);
        var1.step(this.Rotation ^ 1);

        if (this.Active)
        {
            if (this.worldObj.isAirBlock(var1.x, var1.y, var1.z))
            {
                this.worldObj.setBlock(var1.x, var1.y, var1.z, Block.fire.blockID);
            }
        }
        else
        {
            int var2 = this.worldObj.getBlockId(var1.x, var1.y, var1.z);

            if (var2 == Block.fire.blockID || var2 == Block.portal.blockID)
            {
                this.worldObj.setBlock(var1.x, var1.y, var1.z, 0);
            }
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        if (!RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 16777215, 63))
        {
            if (!this.Powered)
            {
                return;
            }

            this.Powered = false;

            if (this.Delay)
            {
                return;
            }

            this.Active = false;
            this.Delay = true;
            this.fireAction();
        }
        else
        {
            if (this.Powered)
            {
                return;
            }

            this.Powered = true;

            if (this.Delay)
            {
                return;
            }

            if (this.Active)
            {
                return;
            }

            this.Active = true;
            this.Delay = true;
            this.fireAction();
        }

        this.scheduleTick(5);
        this.updateBlock();
    }

    public boolean isOnFire(int var1)
    {
        return this.Rotation != 0 ? false : this.Active;
    }

    public void onTileTick()
    {
        this.Delay = false;

        if (this.Active != this.Powered)
        {
            this.Active = this.Powered;
            this.fireAction();
            this.updateBlock();
        }
    }
}
