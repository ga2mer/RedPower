package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.machine.TileSolarPanel$1;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class TileSolarPanel extends TileMachinePanel implements IBluePowerConnectable
{
    BluePowerConductor cond = new TileSolarPanel$1(this);
    public int ConMask = -1;

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;

        if (!this.worldObj.isBlockSolidOnSide(this.xCoord, this.yCoord - 1, this.zCoord, ForgeDirection.UP))
        {
            this.breakBlock();
        }
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    }

    public int getConnectableMask()
    {
        return 16777231;
    }

    public int getConnectClass(int var1)
    {
        return 64;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return this.cond;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }

            this.cond.iterate();
            this.dirtyBlock();

            if (this.cond.getVoltage() <= 100.0D)
            {
                if (this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord, this.zCoord))
                {
                    if (this.worldObj.isDaytime())
                    {
                        if (!this.worldObj.provider.hasNoSky)
                        {
                            this.cond.applyDirect(2.0D);
                        }
                    }
                }
            }
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.cond.readFromNBT(var1);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.cond.writeToNBT(var1);
    }
}
