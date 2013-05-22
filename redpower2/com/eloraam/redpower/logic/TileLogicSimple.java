package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.RedPowerLib;
import net.minecraft.entity.player.EntityPlayer;

public class TileLogicSimple extends TileLogic
{
    private static final int[] toDead = new int[] {0, 1, 2, 4, 6, 5, 3};
    private static final int[] fromDead = new int[] {0, 1, 2, 6, 3, 5, 4};
    private static final int[] toDeadNot = new int[] {0, 1, 8, 4, 12, 5, 9};
    private static final int[] fromDeadNot = new int[] {0, 1, 0, 0, 3, 5, 0, 0, 2, 6, 0, 0, 4};
    private static final int[] toDeadBuf = new int[] {0, 1, 4, 5};
    private static final int[] fromDeadBuf = new int[] {0, 1, 0, 0, 2, 3};
    private static int[] tickSchedule = new int[] {2, 4, 6, 8, 16, 32, 64, 128, 256};

    public void initSubType(int var1)
    {
        super.initSubType(var1);
    }

    public int getPartMaxRotation(int var1, boolean var2)
    {
        if (var2)
        {
            switch (this.SubId)
            {
                case 0:
                    return 3;

                case 1:
                case 2:
                case 3:
                case 4:
                case 9:
                    return 6;

                case 5:
                case 6:
                case 7:
                case 8:
                case 12:
                case 13:
                case 14:
                default:
                    break;

                case 10:
                    return 3;

                case 11:
                case 15:
                    return 1;

                case 16:
                    return 3;
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

                case 1:
                case 2:
                case 3:
                case 4:
                    return fromDead[this.Deadmap];

                case 5:
                case 6:
                case 7:
                case 8:
                case 12:
                case 13:
                case 14:
                default:
                    break;

                case 9:
                    return fromDeadNot[this.Deadmap];

                case 10:
                    return fromDeadBuf[this.Deadmap];

                case 11:
                case 15:
                case 16:
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
                    this.PowerState = 0;
                    this.Active = false;
                    this.Powered = false;
                    this.updateBlockChange();
                    return;

                case 1:
                case 2:
                case 3:
                case 4:
                    this.Deadmap = toDead[var3];
                    this.updateBlockChange();
                    return;

                case 5:
                case 6:
                case 7:
                case 8:
                case 12:
                case 13:
                case 14:
                default:
                    break;

                case 9:
                    this.Deadmap = toDeadNot[var3];
                    this.updateBlockChange();
                    return;

                case 10:
                    this.Deadmap = toDeadBuf[var3];
                    this.updateBlockChange();
                    return;

                case 11:
                case 15:
                case 16:
                    this.Deadmap = var3;
                    this.updateBlockChange();
                    return;
            }
        }

        super.setPartRotation(var1, var2, var3);
    }

    private void latchUpdatePowerState()
    {
        if (!this.Disabled || this.Active)
        {
            int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 5, this.Rotation, 0);

            if (var1 != this.PowerState)
            {
                this.updateBlock();
            }

            this.PowerState = var1;

            if (!this.isTickRunnable())
            {
                if (this.Active)
                {
                    if (var1 == 5)
                    {
                        this.Disabled = true;
                    }
                    else
                    {
                        this.Disabled = false;
                    }
                }
                else if ((var1 != 1 || !this.Powered) && (var1 != 4 || this.Powered))
                {
                    if (var1 == 5)
                    {
                        this.Active = true;
                        this.Disabled = true;
                        this.Powered = !this.Powered;
                        this.scheduleTick(2);
                        this.updateBlockChange();
                    }
                }
                else
                {
                    this.Powered = !this.Powered;
                    this.Active = true;
                    this.playSound("random.click", 0.3F, 0.5F, false);
                    this.scheduleTick(2);
                    this.updateBlockChange();
                }
            }
        }
    }

    private void latchTick()
    {
        if (this.Active)
        {
            this.Active = false;

            if (this.Disabled)
            {
                this.updateBlockChange();
                this.scheduleTick(2);
            }
            else
            {
                this.updateBlockChange();
            }
        }
        else if (this.Disabled)
        {
            int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 5, this.Rotation, 0);

            if (var1 != this.PowerState)
            {
                this.updateBlock();
            }

            this.PowerState = var1;

            switch (var1)
            {
                case 0:
                    this.Disabled = false;
                    this.Powered = this.worldObj.rand.nextInt(2) == 0;
                    this.updateBlockChange();
                    break;

                case 1:
                    this.Disabled = false;
                    this.Powered = false;
                    this.updateBlockChange();
                    this.playSound("random.click", 0.3F, 0.5F, false);

                case 2:
                case 3:
                default:
                    break;

                case 4:
                    this.Disabled = false;
                    this.Powered = true;
                    this.updateBlockChange();
                    this.playSound("random.click", 0.3F, 0.5F, false);
                    break;

                case 5:
                    this.scheduleTick(4);
            }
        }
    }

    private int latch2NextState()
    {
        if ((this.PowerState & 5) == 0)
        {
            return this.PowerState;
        }
        else
        {
            int var1 = this.PowerState & 5 | 10;

            if (this.Deadmap == 2)
            {
                if ((var1 & 1) > 0)
                {
                    var1 &= -9;
                }

                if ((var1 & 4) > 0)
                {
                    var1 &= -3;
                }
            }
            else
            {
                if ((var1 & 1) > 0)
                {
                    var1 &= -3;
                }

                if ((var1 & 4) > 0)
                {
                    var1 &= -9;
                }
            }

            return var1;
        }
    }

    private void latch2UpdatePowerState()
    {
        int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 5, this.Rotation, 0);
        boolean var2 = false;

        if (var1 != (this.PowerState & 5))
        {
            this.PowerState = this.PowerState & 10 | var1;
            var2 = true;
        }

        int var3 = this.latch2NextState();

        if (var3 != this.PowerState || (this.PowerState & 5) == 0)
        {
            this.scheduleTick(2);
            var2 = true;
        }

        if (var2)
        {
            this.updateBlock();
        }
    }

    private void latchChange()
    {
        if (this.Deadmap < 2)
        {
            this.latchUpdatePowerState();
        }
        else
        {
            if (this.isTickRunnable())
            {
                return;
            }

            this.latch2UpdatePowerState();
        }
    }

    private void latch2Tick()
    {
        boolean var1 = false;

        if (this.PowerState == 0)
        {
            this.PowerState |= this.worldObj.rand.nextInt(2) == 0 ? 1 : 4;
            var1 = true;
        }

        int var2 = this.latch2NextState();

        if (var2 != this.PowerState)
        {
            this.PowerState = var2;
            var1 = true;
        }

        if (var1)
        {
            this.updateBlockChange();
        }

        this.latch2UpdatePowerState();
    }

    private void pulseChange()
    {
        int var1;

        if (this.Active)
        {
            var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 2, this.Rotation, 0);

            if (var1 == 0)
            {
                this.Active = false;
                this.updateBlock();
            }
        }
        else if (!this.Powered)
        {
            var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 2, this.Rotation, 0);

            if (var1 > 0)
            {
                this.Powered = true;
                this.updateBlockChange();
                this.scheduleTick(2);
            }
        }
    }

    private void pulseTick()
    {
        if (this.Powered)
        {
            this.Powered = false;
            int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 2, this.Rotation, 0);

            if (var1 > 0)
            {
                this.Active = true;
            }

            this.updateBlockChange();
        }
    }

    private void toggleUpdatePowerState()
    {
        int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 5, this.Rotation, 0);

        if (var1 != this.PowerState)
        {
            int var2 = 5 & var1 & ~this.PowerState;

            if (var2 == 1 || var2 == 4)
            {
                this.Active = true;
            }

            this.PowerState = var1;
            this.updateBlock();

            if (this.Active)
            {
                this.scheduleTick(2);
            }
        }
    }

    private void toggleTick()
    {
        if (this.Active)
        {
            this.playSound("random.click", 0.3F, 0.5F, false);
            this.Powered = !this.Powered;
            this.Active = false;
            this.updateBlockChange();
        }

        this.toggleUpdatePowerState();
    }

    private void repUpdatePowerState()
    {
        if (!this.Active)
        {
            int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 2, this.Rotation, 0);

            if (var1 != this.PowerState)
            {
                this.updateBlock();
            }

            this.PowerState = var1;
            boolean var2 = this.PowerState > 0;

            if (var2 != this.Powered)
            {
                this.Active = true;
                this.scheduleTick(tickSchedule[this.Deadmap]);
            }
        }
    }

    private void repTick()
    {
        boolean var1 = this.simpleWantsPower();

        if (this.Active)
        {
            this.Powered = !this.Powered;
            this.Active = false;
            this.updateBlockChange();
            this.repUpdatePowerState();
        }
    }

    private void syncChange()
    {
        int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 7, this.Rotation, 0);
        int var2 = var1 & ~this.PowerState;

        if (var1 != this.PowerState)
        {
            this.updateBlock();
        }

        this.PowerState = var1;
        boolean var3 = false;

        if ((var1 & 2) == 2)
        {
            if (!this.Powered && (this.Active || this.Disabled))
            {
                this.Active = false;
                this.Disabled = false;
                var3 = true;
            }
        }
        else
        {
            if ((var2 & 1) > 0 && !this.Active)
            {
                this.Active = true;
                var3 = true;
            }

            if ((var2 & 4) > 0 && !this.Disabled)
            {
                this.Disabled = true;
                var3 = true;
            }
        }

        if (var3)
        {
            this.updateBlock();
            this.scheduleTick(2);
        }
    }

    private void syncTick()
    {
        if (this.Active && this.Disabled && !this.Powered)
        {
            this.Powered = true;
            this.Active = false;
            this.Disabled = false;
            this.scheduleTick(2);
            this.updateBlockChange();
        }
        else if (this.Powered)
        {
            this.Powered = false;
            this.updateBlockChange();
        }
    }

    private void randUpdatePowerState()
    {
        int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 15, this.Rotation, 0);
        int var2 = var1 & ~this.PowerState;

        if (var1 != this.PowerState)
        {
            this.updateBlock();
        }

        this.PowerState = var1;

        if ((var2 & 2) > 0)
        {
            this.scheduleTick(2);
        }
    }

    private void randTick()
    {
        if ((this.PowerState & 2) != 0)
        {
            int var1 = this.worldObj.rand.nextInt(8);
            this.Disabled = (var1 & 1) > 0;
            this.Active = (var1 & 2) > 0;
            this.Powered = (var1 & 4) > 0;
            this.updateBlockChange();
            this.randUpdatePowerState();

            if ((this.PowerState & 2) > 0)
            {
                this.scheduleTick(4);
            }
        }
    }

    private void lightTick()
    {
        int var1 = this.worldObj.getBlockLightValue(this.xCoord, this.yCoord, this.zCoord);
        this.Active = var1 > this.Deadmap * 4;

        if (this.Cover != 7 && this.Cover != 255)
        {
            this.Active = false;
        }

        if (this.Active != this.Powered)
        {
            this.scheduleTick(2);
        }

        this.simpleTick();
    }

    private boolean simpleWantsPower()
    {
        switch (this.SubId)
        {
            case 1:
                return (this.PowerState & 7 & ~this.Deadmap) == 0;

            case 2:
                return (this.PowerState & ~this.Deadmap) > 0;

            case 3:
                return (this.PowerState & 7 | this.Deadmap) < 7;

            case 4:
                return (this.PowerState | this.Deadmap) == 7;

            case 5:
                return this.PowerState == 5 || this.PowerState == 0;

            case 6:
                int var1 = this.PowerState & 5;
                return var1 == 4 || var1 == 1;

            case 7:
            case 8:
            case 12:
            case 13:
            case 14:
            default:
                return false;

            case 9:
                return (this.PowerState & 2) == 0;

            case 10:
                return (this.PowerState & 2) > 0;

            case 11:
                if (this.Deadmap == 0)
                {
                    return (this.PowerState & 3) == 1 || (this.PowerState & 6) == 6;
                }

                return (this.PowerState & 3) == 3 || (this.PowerState & 6) == 4;

            case 15:
                if ((this.PowerState & 2) == 0)
                {
                    return this.Powered;
                }
                else
                {
                    if (this.Deadmap == 0)
                    {
                        return (this.PowerState & 4) == 4;
                    }

                    return (this.PowerState & 1) == 1;
                }

            case 16:
                return this.Active;
        }
    }

    private void simpleUpdatePowerState()
    {
        int var2 = 15;

        switch (this.SubId)
        {
            case 2:
                var2 = 7;

            case 3:
            case 7:
            case 8:
            case 9:
            case 13:
            case 14:
            default:
                break;

            case 4:
                var2 = 7;
                break;

            case 5:
                var2 = 5;
                break;

            case 6:
                var2 = 13;
                break;

            case 10:
                var2 = 7;
                break;

            case 11:
                var2 = 7;
                break;

            case 12:
                var2 = 2;
                break;

            case 15:
                var2 = this.Deadmap == 0 ? 6 : 3;
                break;

            case 16:
                var2 = 8;
        }

        int var1 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2, this.Rotation, 0);

        if (var1 != this.PowerState)
        {
            this.updateBlock();
        }

        this.PowerState = var1;
        boolean var3 = this.simpleWantsPower();

        if (var3 != this.Powered)
        {
            this.scheduleTick(2);
        }
    }

    private void simpleTick()
    {
        boolean var1 = this.simpleWantsPower();

        if (this.Powered && !var1)
        {
            this.Powered = false;
            this.updateBlockChange();
        }
        else if (!this.Powered && var1)
        {
            this.Powered = true;
            this.updateBlockChange();
        }

        this.simpleUpdatePowerState();
    }

    public void onBlockNeighborChange(int var1)
    {
        if (!this.tryDropBlock())
        {
            switch (this.SubId)
            {
                case 0:
                    this.latchChange();
                    break;

                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 9:
                case 10:
                case 11:
                case 15:
                case 16:
                    if (!this.isTickRunnable())
                    {
                        this.simpleUpdatePowerState();
                    }

                    break;

                case 7:
                    this.pulseChange();
                    break;

                case 8:
                    if (!this.isTickRunnable())
                    {
                        this.toggleUpdatePowerState();
                    }

                    break;

                case 12:
                    if (!this.isTickRunnable())
                    {
                        this.repUpdatePowerState();
                    }

                    break;

                case 13:
                    this.syncChange();
                    break;

                case 14:
                    if (!this.isTickRunnable())
                    {
                        this.randUpdatePowerState();
                    }
            }
        }
    }

    public void onTileTick()
    {
        switch (this.SubId)
        {
            case 0:
                if (this.Deadmap < 2)
                {
                    this.latchTick();
                }
                else
                {
                    this.latch2Tick();
                }

                break;

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 10:
            case 11:
            case 15:
                this.simpleTick();
                break;

            case 7:
                this.pulseTick();
                break;

            case 8:
                this.toggleTick();
                break;

            case 12:
                this.repTick();
                break;

            case 13:
                this.syncTick();
                break;

            case 14:
                this.randTick();
                break;

            case 16:
                this.lightTick();
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
                    int var2;

                    if (this.Deadmap > 1)
                    {
                        var2 = this.PowerState & 10;
                    }
                    else if (this.Disabled && !this.Active)
                    {
                        var2 = 0;
                    }
                    else if (this.Active)
                    {
                        var2 = this.Powered ? 4 : 1;
                    }
                    else if (this.Deadmap == 1)
                    {
                        var2 = this.Powered ? 6 : 9;
                    }
                    else
                    {
                        var2 = this.Powered ? 12 : 3;
                    }

                    return RedPowerLib.mapRotToCon(var2, this.Rotation);

                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 11:
                case 12:
                case 13:
                default:
                    return super.getPoweringMask(var1);

                case 8:
                    if (this.Powered)
                    {
                        return RedPowerLib.mapRotToCon(2, this.Rotation);
                    }

                    return RedPowerLib.mapRotToCon(8, this.Rotation);

                case 9:
                case 10:
                    if (this.Powered)
                    {
                        return RedPowerLib.mapRotToCon(13 & ~this.Deadmap, this.Rotation);
                    }

                    return 0;

                case 14:
                    return RedPowerLib.mapRotToCon((this.Active ? 1 : 0) | (this.Disabled ? 4 : 0) | (this.Powered ? 8 : 0), this.Rotation);

                case 15:
                    return RedPowerLib.mapRotToCon(this.Deadmap == 0 ? (this.Powered ? 9 : 0) : (this.Powered ? 12 : 0), this.Rotation);
            }
        }
    }

    public boolean onPartActivateSide(EntityPlayer var1, int var2, int var3)
    {
        switch (this.SubId)
        {
            case 8:
                if (var2 != this.Rotation >> 2)
                {
                    return false;
                }

                this.playSound("random.click", 0.3F, 0.5F, false);

                if (this.Powered)
                {
                    this.Powered = false;
                }
                else
                {
                    this.Powered = true;
                }

                this.updateBlockChange();
                return true;

            case 12:
                if (var2 != this.Rotation >> 2)
                {
                    return false;
                }

                ++this.Deadmap;

                if (this.Deadmap > 8)
                {
                    this.Deadmap = 0;
                }

                this.updateBlockChange();
                return true;

            default:
                return false;
        }
    }

    public int getConnectableMask()
    {
        switch (this.SubId)
        {
            case 1:
            case 2:
            case 3:
            case 4:
                return RedPowerLib.mapRotToCon(8 | 7 & ~this.Deadmap, this.Rotation);

            case 5:
            case 6:
                return RedPowerLib.mapRotToCon(13, this.Rotation);

            case 7:
                return RedPowerLib.mapRotToCon(10, this.Rotation);

            case 8:
            case 11:
            default:
                return super.getConnectableMask();

            case 9:
                return RedPowerLib.mapRotToCon(2 | 13 & ~this.Deadmap, this.Rotation);

            case 10:
                return RedPowerLib.mapRotToCon(10 | 5 & ~this.Deadmap, this.Rotation);

            case 12:
                return RedPowerLib.mapRotToCon(10, this.Rotation);
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (this.SubId == 16 && !this.isTickScheduled())
        {
            this.scheduleTick(8);
        }
    }

    public int getLightValue()
    {
        return this.SubId == 16 ? 0 : super.getLightValue();
    }

    public int getExtendedID()
    {
        return 1;
    }
}
