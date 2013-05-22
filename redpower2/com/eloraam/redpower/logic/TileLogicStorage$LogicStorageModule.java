package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.Packet211TileDesc;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileLogicStorage$LogicStorageModule
{
    final TileLogicStorage this$0;

    public TileLogicStorage$LogicStorageModule(TileLogicStorage var1)
    {
        this.this$0 = var1;
    }

    public abstract void updatePowerState();

    public abstract void tileTick();

    public abstract int getSubType();

    public abstract int getPoweringMask(int var1);

    public abstract void readFromNBT(NBTTagCompound var1);

    public abstract void writeToNBT(NBTTagCompound var1);

    public void readFromPacket(Packet211TileDesc var1) throws IOException {}

    public void writeToPacket(Packet211TileDesc var1) {}
}
