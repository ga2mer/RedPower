package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.Packet211TileDesc;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileLogicAdv$LogicAdvModule
{
    final TileLogicAdv this$0;

    public TileLogicAdv$LogicAdvModule(TileLogicAdv var1)
    {
        this.this$0 = var1;
    }

    public abstract void updatePowerState();

    public abstract void tileTick();

    public abstract int getSubType();

    public abstract int getPoweringMask(int var1);

    public void updateCurrentStrength() {}

    public abstract void readFromNBT(NBTTagCompound var1);

    public abstract void writeToNBT(NBTTagCompound var1);

    public void readFromPacket(Packet211TileDesc var1) throws IOException {}

    public void writeToPacket(Packet211TileDesc var1) {}
}
