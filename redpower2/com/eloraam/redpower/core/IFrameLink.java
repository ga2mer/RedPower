package com.eloraam.redpower.core;

public interface IFrameLink
{
    boolean isFrameMoving();

    boolean canFrameConnectIn(int var1);

    boolean canFrameConnectOut(int var1);

    WorldCoord getFrameLinkset();
}
