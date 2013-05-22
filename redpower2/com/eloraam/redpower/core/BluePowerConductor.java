package com.eloraam.redpower.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BluePowerConductor
{
    private static int[] dirmap = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 11, 14, 18, 23};
    int imask = 0;
    double[] currents;
    public double Vcap = 0.0D;
    public double Icap = 0.0D;
    public double Veff = 0.0D;
    int lastTick = 0;
    public double It1 = 0.0D;
    public double Itot = 0.0D;

    public abstract TileEntity getParent();

    public abstract double getInvCap();

    public int getChargeScaled(int var1)
    {
        return 0;
    }

    public int getFlowScaled(int var1)
    {
        return 0;
    }

    public double getResistance()
    {
        return 0.01D;
    }

    public double getIndScale()
    {
        return 0.07D;
    }

    public double getCondParallel()
    {
        return 0.5D;
    }

    public void recache(int var1, int var2)
    {
        int var3 = 0;
        int var4;

        for (var4 = 0; var4 < 3; ++var4)
        {
            if ((var1 & RedPowerLib.getConDirMask(var4 * 2)) > 0)
            {
                var3 |= 1 << var4;
            }
        }

        for (var4 = 0; var4 < 12; ++var4)
        {
            if ((var2 & 1 << dirmap[var4]) > 0)
            {
                var3 |= 8 << var4;
            }
        }

        if (this.imask != var3)
        {
            double[] var11 = new double[Integer.bitCount(var3)];
            int var5 = 0;
            int var6 = 0;

            for (int var7 = 0; var7 < 15; ++var7)
            {
                int var8 = 1 << var7;
                double var9 = 0.0D;

                if ((this.imask & var8) > 0)
                {
                    var9 = this.currents[var5++];
                }

                if ((var3 & var8) > 0)
                {
                    var11[var6++] = var9;
                }
            }

            this.currents = var11;
            this.imask = var3;
        }
    }

    protected void computeVoltage()
    {
        this.Itot = 0.5D * this.It1;
        this.It1 = 0.0D;
        this.Vcap += 0.05D * this.Icap * this.getInvCap();
        this.Icap = 0.0D;
    }

    public double getVoltage()
    {
        long var1 = this.getParent().worldObj.getWorldTime();

        if ((var1 & 65535L) == (long)this.lastTick)
        {
            return this.Vcap;
        }
        else
        {
            this.lastTick = (int)(var1 & 65535L);
            this.computeVoltage();
            return this.Vcap;
        }
    }

    public void applyCurrent(double var1)
    {
        this.getVoltage();
        this.Icap += var1;
        this.It1 += Math.abs(var1);
    }

    public void drawPower(double var1)
    {
        double var3 = this.Vcap * this.Vcap - 0.1D * var1 * this.getInvCap();
        double var5 = var3 < 0.0D ? 0.0D : Math.sqrt(var3) - this.Vcap;
        this.applyDirect(20.0D * var5 / this.getInvCap());
    }

    public double getEnergy(double var1)
    {
        double var3 = this.getVoltage();
        double var5 = 0.5D * (var3 * var3 - var1 * var1) / this.getInvCap();
        return var5 < 0.0D ? 0.0D : var5;
    }

    public void applyPower(double var1)
    {
        double var3 = Math.sqrt(this.Vcap * this.Vcap + 0.1D * var1 * this.getInvCap()) - this.Vcap;
        this.applyDirect(20.0D * var3 / this.getInvCap());
    }

    public void applyDirect(double var1)
    {
        this.applyCurrent(var1);
    }

    public void iterate()
    {
        TileEntity var1 = this.getParent();
        World var2 = var1.worldObj;
        this.getVoltage();
        int var3 = this.imask;
        int var4 = 0;

        while (var3 > 0)
        {
            int var5 = Integer.numberOfTrailingZeros(var3);
            var3 &= ~(1 << var5);
            WorldCoord var7 = new WorldCoord(var1);
            int var6;

            if (var5 < 3)
            {
                var6 = var5 * 2;
                var7.step(var6);
            }
            else
            {
                int var8 = dirmap[var5 - 3];
                var7.step(var8 >> 2);
                var6 = WorldCoord.getIndStepDir(var8 >> 2, var8 & 3);
                var7.step(var6);
            }

            IBluePowerConnectable var16 = (IBluePowerConnectable)CoreLib.getTileEntity(var2, var7, IBluePowerConnectable.class);

            if (var16 == null)
            {
                ++var4;
            }
            else
            {
                BluePowerConductor var9 = var16.getBlueConductor(var6 ^ 1);
                double var10 = this.getResistance() + var9.getResistance();
                double var12 = this.currents[var4];
                double var14 = this.Vcap - var9.getVoltage();
                this.currents[var4] += (var14 - var12 * var10) * this.getIndScale();
                var12 += var14 * this.getCondParallel();
                this.applyCurrent(-var12);
                var9.applyCurrent(var12);
                ++var4;
            }
        }
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        this.imask = var1.getInteger("bpim");
        int var2 = Integer.bitCount(this.imask);
        this.currents = new double[var2];
        NBTTagList var3 = var1.getTagList("bpil");

        if (var3.tagCount() == var2)
        {
            for (int var4 = 0; var4 < var2; ++var4)
            {
                NBTTagDouble var5 = (NBTTagDouble)var3.tagAt(var4);
                this.currents[var4] = var5.data;
            }

            this.Vcap = var1.getDouble("vcap");
            this.Icap = var1.getDouble("icap");
            this.Veff = var1.getDouble("veff");
            this.It1 = var1.getDouble("it1");
            this.Itot = var1.getDouble("itot");
            this.lastTick = var1.getInteger("ltk");
        }
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        var1.setInteger("bpim", this.imask);
        int var2 = Integer.bitCount(this.imask);
        NBTTagList var3 = new NBTTagList();

        for (int var4 = 0; var4 < var2; ++var4)
        {
            NBTTagDouble var5 = new NBTTagDouble((String)null, this.currents[var4]);
            var3.appendTag(var5);
        }

        var1.setTag("bpil", var3);
        var1.setDouble("vcap", this.Vcap);
        var1.setDouble("icap", this.Icap);
        var1.setDouble("veff", this.Veff);
        var1.setDouble("it1", this.It1);
        var1.setDouble("itot", this.Itot);
        var1.setInteger("ltk", this.lastTick);
    }
}
