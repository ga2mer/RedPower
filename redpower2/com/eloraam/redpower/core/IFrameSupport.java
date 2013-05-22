package com.eloraam.redpower.core;

import java.io.IOException;
import net.minecraft.world.IBlockAccess;

public interface IFrameSupport
{
    byte[] getFramePacket();

    void handleFramePacket(byte[] var1) throws IOException;

    void onFrameRefresh(IBlockAccess var1);

    void onFramePickup(IBlockAccess var1);

    void onFrameDrop();
}
