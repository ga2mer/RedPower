package com.eloraam.redpower.core;

import net.minecraft.nbt.NBTTagCompound;

public abstract class BluePowerEndpoint extends BluePowerConductor
{
    public int Charge = 0;
    public int Flow = 0;

    public double getInvCap()
    {
        return 0.25D;
    }

    public int getChargeScaled(int var1)
    {
        return Math.min(var1, var1 * this.Charge / 1000);
    }

    public int getFlowScaled(int var1)
    {
        return Integer.bitCount(this.Flow) * var1 / 32;
    }

    public void iterate()
    {
        super.iterate();
        this.Charge = (int)(this.getVoltage() * 10.0D);
        this.Flow = this.Flow << 1 | (this.Charge >= 600 ? 1 : 0);
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.Charge = var1.getShort("chg");
        this.Flow = var1.getInteger("flw");
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setShort("chg", (short)this.Charge);
        var1.setInteger("flw", this.Flow);
    }
}
