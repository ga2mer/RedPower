package com.eloraam.redpower.wiring;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRedPowerWiring;
import com.eloraam.redpower.core.RedPowerLib;
import net.minecraft.nbt.NBTTagCompound;

public class TileCable extends TileWiring implements IRedPowerWiring
{
    public short[] PowerState = new short[16];

    public float getWireHeight()
    {
        return 0.25F;
    }

    public int getExtendedID()
    {
        return 3;
    }

    public int getConnectClass(int var1)
    {
        return 18 + this.Metadata;
    }

    public int scanPoweringStrength(int var1, int var2)
    {
        return 0;
    }

    public int getCurrentStrength(int var1, int var2)
    {
        return var2 >= 1 && var2 <= 16 ? ((var1 & this.getConnectableMask()) == 0 ? -1 : this.PowerState[var2 - 1]) : -1;
    }

    public void updateCurrentStrength()
    {
        for (int var1 = 0; var1 < 16; ++var1)
        {
            this.PowerState[var1] = (short)RedPowerLib.updateBlockCurrentStrength(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord, 1073741823, 2 << var1);
        }

        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public int getPoweringMask(int var1)
    {
        return var1 >= 1 && var1 <= 16 ? (this.PowerState[var1 - 1] == 0 ? 0 : this.getConnectableMask()) : 0;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        byte[] var2 = var1.getByteArray("pwrs");

        if (var2 != null)
        {
            for (int var3 = 0; var3 < 16; ++var3)
            {
                this.PowerState[var3] = (short)(var2[var3] & 255);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        byte[] var2 = new byte[16];

        for (int var3 = 0; var3 < 16; ++var3)
        {
            var2[var3] = (byte)this.PowerState[var3];
        }

        var1.setByteArray("pwrs", var2);
    }
}
