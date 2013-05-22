package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.FluidBuffer;
import com.eloraam.redpower.core.FluidClass;
import com.eloraam.redpower.core.IPipeConnectable;
import com.eloraam.redpower.core.PipeLib;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileGrate$1;
import com.eloraam.redpower.machine.TileGrate$GratePathfinder;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

public class TileGrate extends TileMachinePanel implements IPipeConnectable
{
    FluidBuffer gratebuf = new TileGrate$1(this);
    TileGrate$GratePathfinder searchPath = null;
    int searchState = 0;
    int pressure;

    public int getPartMaxRotation(int var1, boolean var2)
    {
        return var2 ? 0 : 5;
    }

    public int getPipeConnectableSides()
    {
        return 1 << this.Rotation;
    }

    public int getPipeFlangeSides()
    {
        return 1 << this.Rotation;
    }

    public int getPipePressure(int var1)
    {
        return this.pressure;
    }

    public FluidBuffer getPipeBuffer(int var1)
    {
        return this.gratebuf;
    }

    public void onFramePickup(IBlockAccess var1)
    {
        this.restartPath();
    }

    public int getExtendedID()
    {
        return 3;
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = this.getFacing(var3);
        this.updateBlockChange();
    }

    public void onBlockNeighborChange(int var1) {}

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (!CoreLib.isClient(this.worldObj))
        {
            if (!this.isTickScheduled())
            {
                this.scheduleTick(5);
            }

            WorldCoord var1 = new WorldCoord(this);
            var1.step(this.Rotation);
            Integer var2 = PipeLib.getPressure(this.worldObj, var1, this.Rotation ^ 1);

            if (var2 != null)
            {
                this.pressure = var2.intValue() - Integer.signum(var2.intValue());
            }

            if (this.searchState == 1)
            {
                this.searchPath.tryMapFluid(400);
            }

            PipeLib.movePipeLiquid(this.worldObj, this, new WorldCoord(this), 1 << this.Rotation);
        }
    }

    public void restartPath()
    {
        this.searchPath = null;
        this.searchState = 0;
    }

    public void onTileTick()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.pressure == 0)
            {
                this.restartPath();
            }
            else if (this.pressure < -100)
            {
                if (this.gratebuf.getLevel() >= this.gratebuf.getMaxLevel())
                {
                    return;
                }

                if (this.searchState == 2)
                {
                    this.restartPath();
                }

                if (this.searchState == 0)
                {
                    this.searchState = 1;
                    this.searchPath = new TileGrate$GratePathfinder(this, false);

                    if (this.gratebuf.Type == 0)
                    {
                        if (!this.searchPath.startSuck(new WorldCoord(this), 63 ^ 1 << this.Rotation))
                        {
                            this.restartPath();
                            return;
                        }
                    }
                    else
                    {
                        this.searchPath.start(new WorldCoord(this), this.gratebuf.Type, 63 ^ 1 << this.Rotation);
                    }
                }

                if (this.searchState == 1)
                {
                    if (!this.searchPath.tryMapFluid(400))
                    {
                        return;
                    }

                    int var1 = this.searchPath.trySuckFluid(this.searchPath.fluidClass.getFluidQuanta());

                    if (var1 == 0)
                    {
                        return;
                    }

                    this.gratebuf.addLevel(this.searchPath.fluidID, var1);
                }
            }
            else if (this.pressure > 100)
            {
                FluidClass var4 = this.gratebuf.getFluidClass();

                if (var4 == null)
                {
                    return;
                }

                int var2 = var4.getFluidQuanta();

                if (var2 == 0)
                {
                    return;
                }

                if (this.gratebuf.getLevel() < var2)
                {
                    return;
                }

                if (this.gratebuf.Type == 0)
                {
                    return;
                }

                if (this.searchState == 1)
                {
                    this.restartPath();
                }

                if (this.searchState == 0)
                {
                    this.searchState = 2;
                    this.searchPath = new TileGrate$GratePathfinder(this, true);
                    this.searchPath.start(new WorldCoord(this), this.gratebuf.Type, 63 ^ 1 << this.Rotation);
                }

                if (this.searchState == 2)
                {
                    int var3 = this.searchPath.tryDumpFluid(var2, 2000);

                    if (var3 != var2)
                    {
                        this.gratebuf.addLevel(this.gratebuf.Type, -var2);
                    }
                }
            }
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.gratebuf.readFromNBT(var1, "buf");
        this.pressure = var1.getShort("pres");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.gratebuf.writeToNBT(var1, "buf");
        var1.setShort("pres", (short)this.pressure);
    }
}
