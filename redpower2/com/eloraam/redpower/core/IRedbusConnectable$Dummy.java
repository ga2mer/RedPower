package com.eloraam.redpower.core;

public class IRedbusConnectable$Dummy implements IRedbusConnectable
{
    private int address;

    public int getConnectableMask()
    {
        return 0;
    }

    public int getConnectClass(int var1)
    {
        return 0;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public int rbGetAddr()
    {
        return this.address;
    }

    public void rbSetAddr(int var1)
    {
        this.address = var1;
    }

    public int rbRead(int var1)
    {
        return 0;
    }

    public void rbWrite(int var1, int var2) {}
}
