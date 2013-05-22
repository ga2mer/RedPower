package com.eloraam.redpower.logic;

import com.eloraam.redpower.RedPowerLogic;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.logic.TileLogicStorage$LogicStorageCounter;
import com.eloraam.redpower.logic.TileLogicStorage$LogicStorageModule;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TileLogicStorage extends TileLogic
{
    TileLogicStorage$LogicStorageModule storage = null;

    public int getExtendedID()
    {
        return 3;
    }

    public void initSubType(int var1)
    {
        super.initSubType(var1);
        this.initStorage();
    }

    public TileLogicStorage$LogicStorageModule getLogicStorage(Class var1)
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
                    this.storage = new TileLogicStorage$LogicStorageCounter(this);
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

    public boolean onPartActivateSide(EntityPlayer var1, int var2, int var3)
    {
        if (var2 != this.Rotation >> 2)
        {
            return false;
        }
        else if (var1.isSneaking())
        {
            return false;
        }
        else if (CoreLib.isClient(this.worldObj))
        {
            return true;
        }
        else
        {
            switch (this.SubId)
            {
                case 0:
                    var1.openGui(RedPowerLogic.instance, 1, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                    return true;

                default:
                    return true;
            }
        }
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
