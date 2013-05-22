package com.eloraam.redpower.wiring;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRedPowerWiring;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;

public class TileRedwire extends TileWiring implements IRedPowerWiring
{
    public short PowerState = 0;

    public int getExtendedID()
    {
        return 1;
    }

    public int isBlockStrongPoweringTo(int var1)
    {
        if (RedPowerLib.isSearching())
        {
            return 0;
        }
        else
        {
            int var2 = 15 << ((var1 ^ 1) << 2);
            var2 &= this.getConnectableMask();
            return 0;
        }
    }

    public int isBlockWeakPoweringTo(int var1)
    {
        if (RedPowerLib.isSearching())
        {
            return 0;
        }
        else
        {
            int var2 = 15 << ((var1 ^ 1) << 2);
            var2 |= RedPowerLib.getConDirMask(var1 ^ 1);
            var2 &= this.getConnectableMask();
            return 0;
        }
    }

    public int getConnectClass(int var1)
    {
        return 1;
    }

    public int getConnectableMask()
    {
        if (this.ConaMask >= 0)
        {
            return this.ConaMask;
        }
        else
        {
            int var1 = super.getConnectableMask();

            if ((this.ConSides & 1) > 0)
            {
                var1 |= 16777216;
            }

            if ((this.ConSides & 2) > 0)
            {
                var1 |= 33554432;
            }

            if ((this.ConSides & 4) > 0)
            {
                var1 |= 67108864;
            }

            if ((this.ConSides & 8) > 0)
            {
                var1 |= 134217728;
            }

            if ((this.ConSides & 16) > 0)
            {
                var1 |= 268435456;
            }

            if ((this.ConSides & 32) > 0)
            {
                var1 |= 536870912;
            }

            this.ConaMask = var1;
            return var1;
        }
    }

    public int getCurrentStrength(int var1, int var2)
    {
        return var2 != 0 ? -1 : ((var1 & this.getConnectableMask()) == 0 ? -1 : this.PowerState);
    }

    public int scanPoweringStrength(int var1, int var2)
    {
        return var2 != 0 ? 0 : (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var1, this.ConSides) ? 255 : 0);
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
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        var1.addByte(this.PowerState);
    }
}
