package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileExtended;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileThermopile$1;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class TileThermopile extends TileExtended implements IBluePowerConnectable
{
    BluePowerConductor cond = new TileThermopile$1(this);
    public int tempHot = 0;
    public int tempCold = 0;
    public int ticks = 0;
    public int ConMask = -1;

    public int getConnectableMask()
    {
        return 1073741823;
    }

    public int getConnectClass(int var1)
    {
        return 64;
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
        return 11;
    }

    public int getBlockID()
    {
        return RedPowerMachine.blockMachine.blockID;
    }

    private void updateTemps()
    {
        int var1 = 0;
        int var2 = 0;
        int var3;
        WorldCoord var4;
        int var5;

        for (var3 = 0; var3 < 6; ++var3)
        {
            var4 = new WorldCoord(this);
            var4.step(var3);
            var5 = this.worldObj.getBlockId(var4.x, var4.y, var4.z);

            if (this.worldObj.isAirBlock(var4.x, var4.y, var4.z))
            {
                if (this.worldObj.provider.isHellWorld)
                {
                    ++var1;
                }
                else
                {
                    ++var2;
                }
            }
            else if (var5 == Block.blockSnow.blockID)
            {
                var2 += 100;
            }
            else if (var5 == Block.ice.blockID)
            {
                var2 += 100;
            }
            else if (var5 == Block.snow.blockID)
            {
                var2 += 50;
            }
            else if (var5 == Block.torchWood.blockID)
            {
                var1 += 5;
            }
            else if (var5 == Block.pumpkinLantern.blockID)
            {
                var1 += 3;
            }
            else if (var5 != Block.waterMoving.blockID && var5 != Block.waterStill.blockID)
            {
                if (var5 != Block.lavaMoving.blockID && var5 != Block.lavaStill.blockID)
                {
                    if (var5 == Block.fire.blockID)
                    {
                        var1 += 25;
                    }
                }
                else
                {
                    var1 += 100;
                }
            }
            else
            {
                var2 += 25;
            }
        }

        if (this.tempHot >= 100 && this.tempCold >= 200)
        {
            for (var3 = 0; var3 < 6; ++var3)
            {
                var4 = new WorldCoord(this);
                var4.step(var3);
                var5 = this.worldObj.getBlockId(var4.x, var4.y, var4.z);

                if ((var5 == Block.lavaMoving.blockID || var5 == Block.lavaStill.blockID) && this.worldObj.rand.nextInt(300) == 0)
                {
                    int var6 = this.worldObj.getBlockMetadata(var4.x, var4.y, var4.z);
                    this.worldObj.setBlockMetadataWithNotify(var4.x, var4.y, var4.z, var6 == 0 ? Block.obsidian.blockID : RedPowerWorld.blockStone.blockID, var6 > 0 ? 1 : 0);
                    break;
                }
            }
        }

        if (this.tempHot >= 100)
        {
            for (var3 = 0; var3 < 6; ++var3)
            {
                if (this.worldObj.rand.nextInt(300) == 0)
                {
                    var4 = new WorldCoord(this);
                    var4.step(var3);
                    var5 = this.worldObj.getBlockId(var4.x, var4.y, var4.z);

                    if (var5 == Block.snow.blockID)
                    {
                        this.worldObj.setBlock(var4.x, var4.y, var4.z, 0);
                        break;
                    }

                    if (var5 == Block.ice.blockID || var5 == Block.blockSnow.blockID)
                    {
                        this.worldObj.setBlock(var4.x, var4.y, var4.z, this.worldObj.provider.isHellWorld ? 0 : Block.waterMoving.blockID);
                        break;
                    }
                }
            }
        }

        this.tempHot = var1;
        this.tempCold = var2;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }

            this.cond.iterate();
            this.dirtyBlock();

            if (this.cond.getVoltage() <= 100.0D)
            {
                ++this.ticks;

                if (this.ticks > 20)
                {
                    this.ticks = 0;
                    this.updateTemps();
                }

                int var1 = Math.min(this.tempHot, this.tempCold);
                this.cond.applyDirect(0.005D * (double)var1);
            }
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.cond.readFromNBT(var1);
        this.tempHot = var1.getShort("hot");
        this.tempCold = var1.getShort("cold");
        this.ticks = var1.getByte("ticks");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.cond.writeToNBT(var1);
        var1.setShort("hot", (short)this.tempHot);
        var1.setShort("cold", (short)this.tempCold);
        var1.setByte("ticks", (byte)this.ticks);
    }
}
