package com.eloraam.redpower.compat;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerProvider;
import com.eloraam.redpower.compat.TileBlueEngine$1;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class TileBlueEngine extends TileMachineCompat implements IBluePowerConnectable
{
    BluePowerEndpoint cond = new TileBlueEngine$1(this);
    public int ConMask = -1;
    public byte PumpTick = 0;
    public byte PumpSpeed = 16;
    private double Flywheel = 0.0D;

    public int getConnectableMask()
    {
        int var1 = RedPowerLib.getConDirMask(this.Rotation ^ 1) | 15 << ((this.Rotation ^ 1) << 2);
        return 16777215 & ~var1 | 16777216 << this.Rotation;
    }

    public int getConnectClass(int var1)
    {
        return 65;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return this.cond;
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;
        int var2 = this.getConnectableMask();

        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2, var2 >> 24))
        {
            if (!this.Powered)
            {
                this.Powered = true;
                this.updateBlock();
            }
        }
        else
        {
            this.Powered = false;
            this.updateBlock();
        }
    }

    protected void deliverPower()
    {
        WorldCoord var1 = new WorldCoord(this);
        var1.step(this.Rotation ^ 1);
        IPowerReceptor var2 = (IPowerReceptor)CoreLib.getTileEntity(this.worldObj, var1, IPowerReceptor.class);

        if (var2 != null)
        {
            IPowerProvider var3 = var2.getPowerProvider();

            if (var3 != null && var3 instanceof PowerProvider)
            {
                double var4 = (double)Math.min((float)var3.getMaxEnergyStored() - var3.getEnergyStored(), (float)var3.getMaxEnergyReceived());
                var4 = Math.min(var4, this.Flywheel);

                if (var4 >= (double)var3.getMinEnergyReceived())
                {
                    this.Flywheel -= var4;
                    var3.receiveEnergy((float)var4, ForgeDirection.getOrientation(this.Rotation));
                }
            }
        }
    }

    public void onTileTick() {}

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (CoreLib.isClient(this.worldObj))
        {
            if (this.Active)
            {
                ++this.PumpTick;

                if (this.PumpTick >= this.PumpSpeed * 2)
                {
                    this.PumpTick = 0;

                    if (this.PumpSpeed > 4)
                    {
                        --this.PumpSpeed;
                    }
                }
            }
            else
            {
                this.PumpTick = 0;
            }
        }
        else
        {
            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }

            this.cond.iterate();
            this.dirtyBlock();
            boolean var1 = this.Active;

            if (this.Active)
            {
                ++this.PumpTick;
                byte var2 = this.PumpTick;

                if (var2 == this.PumpSpeed)
                {
                    this.deliverPower();
                }

                if (var2 >= this.PumpSpeed * 2)
                {
                    this.PumpTick = 0;

                    if (this.PumpSpeed > 4)
                    {
                        --this.PumpSpeed;
                    }

                    this.Active = false;
                }

                if (this.Powered && this.Flywheel < 512.0D)
                {
                    double var3 = Math.min(Math.min(512.0D - this.Flywheel, 32.0D), 0.002D * this.cond.getEnergy(60.0D));
                    this.cond.drawPower(1000.0D * var3);
                    this.Flywheel += var3;
                }

                this.cond.drawPower(50.0D);
            }

            if (this.cond.getVoltage() < 60.0D)
            {
                if (this.Charged && this.cond.Flow == 0)
                {
                    this.Charged = false;
                    this.updateBlock();
                }
            }
            else
            {
                if (!this.Charged)
                {
                    this.Charged = true;
                    this.updateBlock();
                }

                if (this.Charged && this.Powered)
                {
                    this.Active = true;
                }

                if (this.Active != var1)
                {
                    if (this.Active)
                    {
                        this.PumpSpeed = 16;
                    }

                    this.updateBlock();
                }
            }
        }
    }

    public int getExtendedID()
    {
        return 0;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.cond.readFromNBT(var1);
        this.PumpTick = var1.getByte("ptk");
        this.PumpSpeed = var1.getByte("spd");
        this.Flywheel = var1.getDouble("flyw");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.cond.writeToNBT(var1);
        var1.setByte("ptk", this.PumpTick);
        var1.setByte("spd", this.PumpSpeed);
        var1.setDouble("flyw", this.Flywheel);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);
        this.PumpSpeed = (byte)var1.getByte();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        var1.addByte(this.PumpSpeed);
    }
}
