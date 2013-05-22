package com.eloraam.redpower.core;

public interface ITubeConnectable
{
    int getTubeConnectableSides();

    int getTubeConClass();

    boolean canRouteItems();

    boolean tubeItemEnter(int var1, int var2, TubeItem var3);

    boolean tubeItemCanEnter(int var1, int var2, TubeItem var3);

    int tubeWeight(int var1, int var2);
}
