package com.eloraam.redpower.core;

public interface IPipeConnectable
{
    int getPipeConnectableSides();

    int getPipeFlangeSides();

    int getPipePressure(int var1);

    FluidBuffer getPipeBuffer(int var1);
}
