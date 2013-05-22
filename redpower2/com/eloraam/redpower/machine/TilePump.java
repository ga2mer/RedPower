package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.FluidBuffer;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.IPipeConnectable;
import com.eloraam.redpower.core.PipeLib;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TilePump$1;
import com.eloraam.redpower.machine.TilePump$PumpBuffer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TilePump extends TileMachinePanel implements IPipeConnectable, IBluePowerConnectable
{
    TilePump$PumpBuffer inbuf = new TilePump$PumpBuffer(this);
    TilePump$PumpBuffer outbuf = new TilePump$PumpBuffer(this);
    BluePowerEndpoint cond = new TilePump$1(this);
    public int ConMask = -1;
    public byte PumpTick = 0;

    public int getPipeConnectableSides()
    {
        return 12 << (((this.Rotation ^ 1) & 1) << 1);
    }

    public int getPipeFlangeSides()
    {
        return 12 << (((this.Rotation ^ 1) & 1) << 1);
    }

    public int getPipePressure(int var1)
    {
        int var2 = CoreLib.rotToSide(this.Rotation);
        return !this.Active ? 0 : (var1 == var2 ? 1000 : (var1 == ((var2 ^ 1) & 255) ? -1000 : 0));
    }

    public FluidBuffer getPipeBuffer(int var1)
    {
        int var2 = CoreLib.rotToSide(this.Rotation);
        return var1 == var2 ? this.outbuf : (var1 == ((var2 ^ 1) & 255) ? this.inbuf : null);
    }

    public int getConnectableMask()
    {
        return 3 << ((this.Rotation & 1) << 1) | 17895680;
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

    public int getExtendedID()
    {
        return 1;
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = (int)Math.floor((double)(var3.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;

        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 16777215, 63))
        {
            if (!this.Powered)
            {
                this.Powered = true;
                this.dirtyBlock();
            }
        }
        else
        {
            this.Powered = false;
            this.dirtyBlock();
        }
    }

    private void pumpFluid()
    {
        if (this.inbuf.Type != 0)
        {
            int var1 = Math.min(this.inbuf.getLevel(), this.outbuf.getMaxLevel() - this.outbuf.getLevel());
            var1 = Math.min(var1, this.inbuf.getLevel() + this.inbuf.Delta);

            if (var1 > 0)
            {
                if (this.inbuf.Type == this.outbuf.Type || this.outbuf.Type == 0)
                {
                    this.outbuf.addLevel(this.inbuf.Type, var1);
                    this.inbuf.addLevel(this.inbuf.Type, -var1);
                }
            }
        }
    }

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

                if (this.PumpTick >= 16)
                {
                    this.PumpTick = 0;
                }
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
            int var1 = CoreLib.rotToSide(this.Rotation);
            PipeLib.movePipeLiquid(this.worldObj, this, new WorldCoord(this), 3 << (var1 & -2));
            boolean var2 = this.Active;

            if (this.Active)
            {
                ++this.PumpTick;

                if (this.PumpTick == 8)
                {
                    this.cond.drawPower(10000.0D);
                    this.pumpFluid();
                }

                if (this.PumpTick >= 16)
                {
                    this.PumpTick = 0;
                    this.Active = false;
                }

                this.cond.drawPower(200.0D);
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

                if (this.Active != var2)
                {
                    this.updateBlock();
                }
            }
        }
    }

    public void onTileTick()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (!this.Powered)
            {
                this.Active = false;
                this.updateBlock();
            }
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.cond.readFromNBT(var1);
        this.inbuf.readFromNBT(var1, "inb");
        this.outbuf.readFromNBT(var1, "outb");
        this.PumpTick = var1.getByte("ptk");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.cond.writeToNBT(var1);
        this.inbuf.writeToNBT(var1, "inb");
        this.outbuf.writeToNBT(var1, "outb");
        var1.setByte("ptk", this.PumpTick);
    }
}
