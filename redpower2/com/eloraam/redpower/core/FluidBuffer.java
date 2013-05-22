package com.eloraam.redpower.core;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class FluidBuffer
{
    public int Type = 0;
    public int Level = 0;
    public int Delta = 0;
    private int lastTick = 0;

    public abstract TileEntity getParent();

    public abstract void onChange();

    public int getMaxLevel()
    {
        return 1000;
    }

    public int getLevel()
    {
        long var1 = this.getParent().worldObj.getWorldTime();

        if ((var1 & 65535L) == (long)this.lastTick)
        {
            return this.Level;
        }
        else
        {
            this.lastTick = (int)(var1 & 65535L);
            this.Level += this.Delta;
            this.Delta = 0;

            if (this.Level == 0)
            {
                this.Type = 0;
            }

            return this.Level;
        }
    }

    public void addLevel(int var1, int var2)
    {
        this.Type = var1;
        this.Delta += var2;
        this.onChange();
    }

    public FluidClass getFluidClass()
    {
        return this.Type != 0 && this.Level != 0 ? PipeLib.getLiquidClass(this.Type) : null;
    }

    public void readFromNBT(NBTTagCompound var1, String var2)
    {
        NBTTagCompound var3 = var1.getCompoundTag(var2);
        this.Type = var3.getInteger("type");
        this.Level = var3.getInteger("lvl");
        this.Delta = var3.getInteger("del");
        this.lastTick = var3.getInteger("ltk");
    }

    public void writeToNBT(NBTTagCompound var1, String var2)
    {
        NBTTagCompound var3 = new NBTTagCompound();
        var3.setInteger("type", (short)this.Type);
        var3.setInteger("lvl", (short)this.Level);
        var3.setInteger("del", (short)this.Delta);
        var3.setShort("lck", (short)this.lastTick);
        var1.setCompoundTag(var2, var3);
    }

    public void writeToPacket(Packet211TileDesc var1)
    {
        var1.addUVLC((long)this.Type);
        var1.addUVLC((long)this.Level);
    }

    public void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Type = (int)var1.getUVLC();
        this.Level = (int)var1.getUVLC();
    }
}
