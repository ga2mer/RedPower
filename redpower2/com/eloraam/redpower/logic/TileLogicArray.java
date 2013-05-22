package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRedPowerWiring;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;

public class TileLogicArray extends TileLogic implements IRedPowerWiring
{
    public short PowerVal1 = 0;
    public short PowerVal2 = 0;

    public int getPoweringMask(int var1)
    {
        if (var1 != 0)
        {
            return 0;
        }
        else
        {
            int var2 = 0;

            if (this.PowerVal1 > 0)
            {
                var2 |= RedPowerLib.mapRotToCon(10, this.Rotation);
            }

            if (this.PowerVal2 > 0)
            {
                var2 |= RedPowerLib.mapRotToCon(5, this.Rotation);
            }

            return var2;
        }
    }

    public void updateCurrentStrength()
    {
        this.PowerVal2 = (short)RedPowerLib.updateBlockCurrentStrength(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord, RedPowerLib.mapRotToCon(5, this.Rotation), 1);
        this.PowerVal1 = (short)RedPowerLib.updateBlockCurrentStrength(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord, RedPowerLib.mapRotToCon(10, this.Rotation), 1);
        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public int getCurrentStrength(int var1, int var2)
    {
        return var2 != 0 ? -1 : ((RedPowerLib.mapRotToCon(5, this.Rotation) & var1) > 0 ? this.PowerVal2 : ((RedPowerLib.mapRotToCon(10, this.Rotation) & var1) > 0 ? this.PowerVal1 : -1));
    }

    public int scanPoweringStrength(int var1, int var2)
    {
        if (var2 != 0)
        {
            return 0;
        }
        else
        {
            int var3 = RedPowerLib.mapRotToCon(5, this.Rotation);
            int var4 = RedPowerLib.mapRotToCon(10, this.Rotation);
            return (var3 & var1) > 0 ? (this.Powered ? 255 : (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var3 & var1, 0) ? 255 : 0)) : ((var4 & var1) > 0 ? (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var4 & var1, 0) ? 255 : 0) : 0);
        }
    }

    public int getConnectionMask()
    {
        return RedPowerLib.mapRotToCon(15, this.Rotation);
    }

    public int getExtConnectionMask()
    {
        return 0;
    }

    public int getTopwireMask()
    {
        return RedPowerLib.mapRotToCon(5, this.Rotation);
    }

    private boolean cellWantsPower()
    {
        return this.SubId == 1 ? this.PowerState == 0 : this.PowerState != 0;
    }

    private void updatePowerState()
    {
        this.PowerState = this.PowerVal1 > 0 ? 1 : 0;

        if (this.cellWantsPower() != this.Powered)
        {
            this.scheduleTick(2);
        }
    }

    public int getExtendedID()
    {
        return 2;
    }

    public void onBlockNeighborChange(int var1)
    {
        if (!this.tryDropBlock())
        {
            RedPowerLib.updateCurrent(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

            if (this.SubId != 0)
            {
                if (!this.isTickRunnable())
                {
                    this.updatePowerState();
                }
            }
        }
    }

    public boolean isBlockStrongPoweringTo(int var1, int var2)
    {
        return RedPowerLib.isSearching() ? false : (this.getPoweringMask(0) & RedPowerLib.getConDirMask(var1 ^ 1)) > 0;
    }

    public boolean isBlockWeakPoweringTo(int var1, int var2)
    {
        return RedPowerLib.isSearching() ? false : (this.getPoweringMask(0) & RedPowerLib.getConDirMask(var1 ^ 1)) > 0;
    }

    public void onTileTick()
    {
        if (this.Powered != this.cellWantsPower())
        {
            this.Powered = !this.Powered;
            this.updateBlockChange();
            this.updatePowerState();
        }
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        if (var2 != this.Rotation >> 2)
        {
            super.setPartBounds(var1, var2);
        }
        else
        {
            switch (var2)
            {
                case 0:
                    var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                    break;

                case 1:
                    var1.setBlockBounds(0.0F, 0.15F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 2:
                    var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                    break;

                case 3:
                    var1.setBlockBounds(0.0F, 0.0F, 0.15F, 1.0F, 1.0F, 1.0F);
                    break;

                case 4:
                    var1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
                    break;

                case 5:
                    var1.setBlockBounds(0.15F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.PowerVal1 = (short)(var1.getByte("pv1") & 255);
        this.PowerVal2 = (short)(var1.getByte("pv2") & 255);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("pv1", (byte)this.PowerVal1);
        var1.setByte("pv2", (byte)this.PowerVal2);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);

        if (var1.subId == 6)
        {
            this.PowerVal1 = (short)var1.getByte();
            this.PowerVal2 = (short)var1.getByte();
        }
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        var1.subId = 6;
        var1.addByte(this.PowerVal1);
        var1.addByte(this.PowerVal2);
    }
}
