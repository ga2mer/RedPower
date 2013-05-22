package com.eloraam.redpower.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public interface IMultiblock
{
    void onMultiRemoval(int var1);

    AxisAlignedBB getMultiBounds(int var1);

    float getMultiBlockStrength(int var1, EntityPlayer var2);
}
