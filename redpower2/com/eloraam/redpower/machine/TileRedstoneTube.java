package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRedPowerWiring;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

public class TileRedstoneTube extends TileTube implements IRedPowerWiring
{
    public short PowerState = 0;
    public int ConMask = -1;

    public int getConnectableMask()
    {
        int var2 = 63;

        for (int var1 = 0; var1 < 6; ++var1)
        {
            if ((this.CoverSides & 1 << var1) > 0 && this.Covers[var1] >> 8 < 3)
            {
                var2 &= ~(1 << var1);
            }
        }

        return var2 << 24;
    }

    public int getConnectionMask()
    {
        if (this.ConMask >= 0)
        {
            return this.ConMask;
        }
        else
        {
            this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
            return this.ConMask;
        }
    }

    public int getExtConnectionMask()
    {
        return 0;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public void onFrameRefresh(IBlockAccess var1)
    {
        if (this.ConMask < 0)
        {
            this.ConMask = RedPowerLib.getConnections(var1, this, this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public int getConnectClass(int var1)
    {
        return 1;
    }

    public int getCurrentStrength(int var1, int var2)
    {
        return var2 != 0 ? -1 : ((var1 & this.getConnectableMask()) == 0 ? -1 : this.PowerState);
    }

    public int scanPoweringStrength(int var1, int var2)
    {
        return var2 != 0 ? 0 : (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var1, this.getConnectionMask()) ? 255 : 0);
    }

    public void updateCurrentStrength()
    {
        this.PowerState = (short)RedPowerLib.updateBlockCurrentStrength(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord, 1073741823, 1);
        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public int getPoweringMask(int var1)
    {
        return var1 == 0 && this.PowerState != 0 ? this.getConnectableMask() : 0;
    }

    public void onBlockNeighborChange(int var1)
    {
        super.onBlockNeighborChange(var1);

        if (this.ConMask >= 0)
        {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }

        this.ConMask = -1;
        RedPowerLib.updateCurrent(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public int getExtendedID()
    {
        return 9;
    }

    public boolean isBlockWeakPoweringTo(int var1, int var2)
    {
        return RedPowerLib.isSearching() ? false : ((this.getConnectionMask() & 16777216 << (var1 ^ 1)) == 0 ? false : (RedPowerLib.isBlockRedstone(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var1 ^ 1) ? this.PowerState > 15 : this.PowerState > 0));
    }

    public boolean tryAddCover(int var1, int var2)
    {
        if (!this.canAddCover(var1, var2))
        {
            return false;
        }
        else
        {
            this.CoverSides |= 1 << var1;
            this.Covers[var1] = (short)var2;
            this.ConMask = -1;
            this.updateBlockChange();
            return true;
        }
    }

    public int tryRemoveCover(int var1)
    {
        if ((this.CoverSides & 1 << var1) == 0)
        {
            return -1;
        }
        else
        {
            this.CoverSides &= ~(1 << var1);
            short var2 = this.Covers[var1];
            this.Covers[var1] = 0;
            this.ConMask = -1;
            this.updateBlockChange();
            return var2;
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.PowerState = (short)(var1.getByte("pwr") & 255);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("pwr", (byte)this.PowerState);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);
        this.PowerState = (short)var1.getByte();
        this.ConMask = -1;
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        var1.addByte(this.PowerState);
    }
}
