package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.IRedPowerWiring;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.logic.TileLogicAdv$LogicAdvModule;
import com.eloraam.redpower.logic.TileLogicAdv$LogicAdvXcvr;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TileLogicAdv extends TileLogic implements IRedPowerWiring
{
    TileLogicAdv$LogicAdvModule storage = null;

    public void updateCurrentStrength()
    {
        this.initStorage();
        this.storage.updateCurrentStrength();
    }

    public int getCurrentStrength(int var1, int var2)
    {
        this.initStorage();
        return (this.storage.getPoweringMask(var2) & var1) > 0 ? 255 : -1;
    }

    public int scanPoweringStrength(int var1, int var2)
    {
        return 0;
    }

    public int getConnectionMask()
    {
        return RedPowerLib.mapRotToCon(15, this.Rotation);
    }

    public int getExtConnectionMask()
    {
        return 0;
    }

    public int getConnectClass(int var1)
    {
        int var2 = RedPowerLib.mapRotToCon(10, this.Rotation);
        return (var2 & RedPowerLib.getConDirMask(var1)) > 0 ? 18 : 0;
    }

    public int getExtendedID()
    {
        return 4;
    }

    public void initSubType(int var1)
    {
        this.SubId = var1;
        this.initStorage();
    }

    public TileLogicAdv$LogicAdvModule getLogicStorage(Class var1)
    {
        if (!var1.isInstance(this.storage))
        {
            this.initStorage();
        }

        return this.storage;
    }

    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public int getPartMaxRotation(int var1, boolean var2)
    {
        if (var2)
        {
            switch (this.SubId)
            {
                case 0:
                    return 1;
            }
        }

        return super.getPartMaxRotation(var1, var2);
    }

    public int getPartRotation(int var1, boolean var2)
    {
        if (var2)
        {
            switch (this.SubId)
            {
                case 0:
                    return this.Deadmap;
            }
        }

        return super.getPartRotation(var1, var2);
    }

    public void setPartRotation(int var1, boolean var2, int var3)
    {
        if (var2)
        {
            switch (this.SubId)
            {
                case 0:
                    this.Deadmap = var3;
                    this.updateBlockChange();
                    return;
            }
        }

        super.setPartRotation(var1, var2, var3);
    }

    void initStorage()
    {
        if (this.storage == null || this.storage.getSubType() != this.SubId)
        {
            switch (this.SubId)
            {
                case 0:
                    this.storage = new TileLogicAdv$LogicAdvXcvr(this);
                    break;

                default:
                    this.storage = null;
            }
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        if (!this.tryDropBlock())
        {
            this.initStorage();

            switch (this.SubId)
            {
                case 0:
                    if (this.isTickRunnable())
                    {
                        return;
                    }
                    else
                    {
                        this.storage.updatePowerState();
                    }

                default:
            }
        }
    }

    public void onTileTick()
    {
        this.initStorage();
        this.storage.tileTick();
    }

    public int getPoweringMask(int var1)
    {
        this.initStorage();
        return this.storage.getPoweringMask(var1);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.initStorage();
        this.storage.readFromNBT(var1);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.storage.writeToNBT(var1);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);
        this.initStorage();
        this.storage.readFromPacket(var1);
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        this.storage.writeToPacket(var1);
    }
}
