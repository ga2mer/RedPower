package com.eloraam.redpower.logic;

import com.eloraam.redpower.RedPowerLogic;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.MathLib;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.Quat;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.Vector3;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TileLogicPointer extends TileLogic implements IPointerTile
{
    private long timestart = 0L;
    public long interval = 40L;

    public void initSubType(int var1)
    {
        super.initSubType(var1);

        switch (var1)
        {
            case 0:
                this.interval = 38L;
                break;

            case 2:
                this.Disabled = true;
        }
    }

    public int getPartMaxRotation(int var1, boolean var2)
    {
        return var2 && (this.SubId == 1 || this.SubId == 2) ? 1 : super.getPartMaxRotation(var1, var2);
    }

    public int getPartRotation(int var1, boolean var2)
    {
        return var2 && (this.SubId == 1 || this.SubId == 2) ? this.Deadmap : super.getPartRotation(var1, var2);
    }

    public void setPartRotation(int var1, boolean var2, int var3)
    {
        if (var2 && (this.SubId == 1 || this.SubId == 2))
        {
            this.Deadmap = var3;
            this.updateBlockChange();
        }
        else
        {
            super.setPartRotation(var1, var2, var3);
        }
    }

    private void timerChange()
    {
        int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 7, this.Rotation, 0);

        if (var1 != this.PowerState)
        {
            this.updateBlock();
        }

        this.PowerState = var1;

        if (this.Powered)
        {
            if (!this.Disabled)
            {
                return;
            }

            if (var1 > 0)
            {
                return;
            }

            this.Powered = false;
            this.Disabled = false;
            this.timestart = this.worldObj.getWorldTime();
            this.updateBlock();
        }
        else if (this.Disabled)
        {
            if (var1 > 0)
            {
                return;
            }

            this.timestart = this.worldObj.getWorldTime();
            this.Disabled = false;
            this.updateBlock();
        }
        else
        {
            if (var1 == 0)
            {
                return;
            }

            this.Disabled = true;
            this.updateBlock();
        }
    }

    private void timerTick()
    {
        int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 7, this.Rotation, 0);

        if (var1 != this.PowerState)
        {
            this.updateBlock();
        }

        this.PowerState = var1;

        if (this.Powered)
        {
            if (this.Disabled)
            {
                if (var1 > 0)
                {
                    this.Powered = false;
                    this.updateBlock();
                    return;
                }

                this.Disabled = false;
                this.Powered = false;
                this.timestart = this.worldObj.getWorldTime();
                this.updateBlock();
                return;
            }

            if (var1 == 0)
            {
                this.Powered = false;
            }
            else
            {
                this.Disabled = true;
                this.scheduleTick(2);
            }

            this.timestart = this.worldObj.getWorldTime();
            this.updateBlockChange();
        }
        else if (this.Disabled)
        {
            if (var1 > 0)
            {
                return;
            }

            this.timestart = this.worldObj.getWorldTime();
            this.Disabled = false;
            this.updateBlock();
        }
        else
        {
            if (var1 == 0)
            {
                return;
            }

            this.Disabled = true;
            this.updateBlock();
        }
    }

    private void timerUpdate()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (!this.Powered && !this.Disabled)
            {
                long var1 = this.worldObj.getWorldTime();

                if (this.interval < 2L)
                {
                    this.interval = 2L;
                }

                if (this.timestart > var1)
                {
                    this.timestart = var1;
                }

                if (this.timestart + this.interval <= var1)
                {
                    this.playSound("random.click", 0.3F, 0.5F, false);
                    this.Powered = true;
                    this.scheduleTick(2);
                    this.updateBlockChange();
                }
            }
        }
    }

    private void sequencerUpdate()
    {
        long var1 = this.worldObj.getWorldTime() + 6000L;
        float var3 = (float)var1 / (float)(this.interval * 4L);
        int var4 = (int)Math.floor((double)(var3 * 4.0F));

        if (this.Deadmap == 1)
        {
            var4 = 3 - var4 & 3;
        }
        else
        {
            var4 = var4 + 3 & 3;
        }

        if (this.PowerState != var4 && !CoreLib.isClient(this.worldObj))
        {
            this.playSound("random.click", 0.3F, 0.5F, false);
            this.PowerState = var4;
            this.updateBlockChange();
        }
    }

    private void stateCellChange()
    {
        int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 7, this.Rotation, 0);

        if (var1 != this.PowerState)
        {
            this.updateBlock();
        }

        this.PowerState = var1;
        boolean var2 = this.Deadmap == 0 ? (var1 & 3) > 0 : (var1 & 6) > 0;

        if (this.Disabled && !var2)
        {
            this.Disabled = false;
            this.timestart = this.worldObj.getWorldTime();
            this.updateBlock();
        }
        else if (!this.Disabled && var2)
        {
            this.Disabled = true;
            this.updateBlock();
        }

        if (!this.Active && !this.Powered && (var1 & 2) > 0)
        {
            this.Powered = true;
            this.updateBlock();
            this.scheduleTick(2);
        }
    }

    private void stateCellTick()
    {
        if (!this.Active && this.Powered)
        {
            this.Powered = false;
            this.Active = true;
            this.timestart = this.worldObj.getWorldTime();
            this.updateBlockChange();
        }
        else if (this.Active && this.Powered)
        {
            this.Powered = false;
            this.Active = false;
            this.updateBlockChange();
        }
    }

    private void stateCellUpdate()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.Active && !this.Powered && !this.Disabled)
            {
                long var1 = this.worldObj.getWorldTime();

                if (this.interval < 2L)
                {
                    this.interval = 2L;
                }

                if (this.timestart > var1)
                {
                    this.timestart = var1;
                }

                if (this.timestart + this.interval <= var1)
                {
                    this.playSound("random.click", 0.3F, 0.5F, false);
                    this.Powered = true;
                    this.scheduleTick(2);
                    this.updateBlockChange();
                }
            }
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        if (!this.tryDropBlock())
        {
            switch (this.SubId)
            {
                case 0:
                    this.timerChange();
                    break;

                case 2:
                    this.stateCellChange();
            }
        }
    }

    public void onTileTick()
    {
        switch (this.SubId)
        {
            case 0:
                this.timerTick();
                break;

            case 2:
                this.stateCellTick();
        }
    }

    public int getPoweringMask(int var1)
    {
        if (var1 != 0)
        {
            return 0;
        }
        else
        {
            switch (this.SubId)
            {
                case 0:
                    if (!this.Disabled && this.Powered)
                    {
                        return RedPowerLib.mapRotToCon(13, this.Rotation);
                    }

                    return 0;

                case 1:
                    return RedPowerLib.mapRotToCon(1 << this.PowerState, this.Rotation);

                case 2:
                    int var2 = (this.Active && this.Powered ? 8 : 0) | (this.Active && !this.Powered ? (this.Deadmap == 0 ? 4 : 1) : 0);
                    return RedPowerLib.mapRotToCon(var2, this.Rotation);

                default:
                    return 0;
            }
        }
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
            return false;
        }
        else
        {
            var1.openGui(RedPowerLogic.instance, 2, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        switch (this.SubId)
        {
            case 0:
                this.timerUpdate();
                break;

            case 1:
                this.sequencerUpdate();
                break;

            case 2:
                this.stateCellUpdate();
        }
    }

    public float getPointerDirection(float var1)
    {
        long var2;
        float var4;

        if (this.SubId == 0)
        {
            if (!this.Powered && !this.Disabled)
            {
                var2 = this.worldObj.getWorldTime();
                var4 = ((float)var2 + var1 - (float)this.timestart) / (float)this.interval;

                if (var4 > 1.0F)
                {
                    var4 = 1.0F;
                }

                return var4 + 0.75F;
            }
            else
            {
                return 0.75F;
            }
        }
        else if (this.SubId == 1)
        {
            var2 = this.worldObj.getWorldTime() + 6000L;
            var4 = ((float)var2 + var1) / (float)(this.interval * 4L);

            if (this.Deadmap == 1)
            {
                var4 = 0.75F - var4;
            }
            else
            {
                var4 += 0.75F;
            }

            return var4;
        }
        else if (this.SubId != 2)
        {
            return 0.0F;
        }
        else
        {
            if (this.Deadmap > 0)
            {
                if (!this.Active || this.Disabled)
                {
                    return 1.0F;
                }

                if (this.Active && this.Powered)
                {
                    return 0.8F;
                }
            }
            else
            {
                if (!this.Active || this.Disabled)
                {
                    return 0.5F;
                }

                if (this.Active && this.Powered)
                {
                    return 0.7F;
                }
            }

            var2 = this.worldObj.getWorldTime();
            var4 = ((float)var2 + var1 - (float)this.timestart) / (float)this.interval;
            return this.Deadmap > 0 ? 1.0F - 0.2F * var4 : 0.5F + 0.2F * var4;
        }
    }

    public Quat getOrientationBasis()
    {
        return MathLib.orientQuat(this.Rotation >> 2, this.Rotation & 3);
    }

    public Vector3 getPointerOrigin()
    {
        return this.SubId == 2 ? (this.Deadmap > 0 ? new Vector3(0.0D, -0.1D, -0.25D) : new Vector3(0.0D, -0.1D, 0.25D)) : new Vector3(0.0D, -0.1D, 0.0D);
    }

    public void setInterval(long var1)
    {
        if (this.SubId == 0)
        {
            this.interval = var1 - 2L;
        }
        else
        {
            this.interval = var1;
        }
    }

    public long getInterval()
    {
        return this.SubId == 0 ? this.interval + 2L : this.interval;
    }

    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
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
        this.interval = var1.getLong("iv");

        if (this.SubId == 0 || this.SubId == 2)
        {
            this.timestart = var1.getLong("ts");
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setLong("iv", this.interval);

        if (this.SubId == 0 || this.SubId == 2)
        {
            var1.setLong("ts", this.timestart);
        }
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);

        if (var1.subId == 2)
        {
            this.interval = var1.getUVLC();

            if (this.SubId == 0 || this.SubId == 2)
            {
                this.timestart = var1.getVLC();
            }
        }
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        var1.subId = 2;
        var1.addUVLC(this.interval);

        if (this.SubId == 0 || this.SubId == 2)
        {
            var1.addVLC(this.timestart);
        }
    }
}
