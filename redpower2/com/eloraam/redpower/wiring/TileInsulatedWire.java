package com.eloraam.redpower.wiring;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRedPowerWiring;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;

public class TileInsulatedWire extends TileWiring implements IRedPowerWiring
{
    public short PowerState = 0;

    public float getWireHeight()
    {
        return 0.188F;
    }

    public int getExtendedID()
    {
        return 2;
    }

    public int isBlockWeakPoweringTo(int var1)
    {
        if (RedPowerLib.isSearching())
        {
            return 0;
        }
        else
        {
            int var2 = RedPowerLib.getConDirMask(var1 ^ 1);
            var2 &= this.getConnectableMask();
            return 0;
        }
    }

    public int getConnectClass(int var1)
    {
        return 2 + this.Metadata;
    }

    public int scanPoweringStrength(int var1, int var2)
    {
        return RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var1, 0) ? 255 : 0;
    }

    public int getCurrentStrength(int var1, int var2)
    {
        return var2 != 0 && var2 != this.Metadata + 1 ? -1 : ((var1 & this.getConnectableMask()) == 0 ? -1 : this.PowerState);
    }

    public void updateCurrentStrength()
    {
        this.PowerState = (short)RedPowerLib.updateBlockCurrentStrength(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord, 16777215, 1 | 2 << this.Metadata);
        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public int getPoweringMask(int var1)
    {
        return this.PowerState == 0 ? 0 : (var1 != 0 && var1 != this.Metadata + 1 ? 0 : this.getConnectableMask());
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
