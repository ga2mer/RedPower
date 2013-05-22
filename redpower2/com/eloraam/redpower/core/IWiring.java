package com.eloraam.redpower.core;

public interface IWiring extends IConnectable
{
    int getConnectionMask();

    int getExtConnectionMask();
}
