package com.eloraam.redpower.core;

public interface IRedbusConnectable extends IConnectable
{
    int rbGetAddr();

    void rbSetAddr(int var1);

    int rbRead(int var1);

    void rbWrite(int var1, int var2);
}
