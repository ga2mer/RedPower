package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.logic.TileLogicAdv$LogicAdvModule;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;

public class TileLogicAdv$LogicAdvXcvr extends TileLogicAdv$LogicAdvModule
{
    public int State1;
    public int State2;
    public int State1N;
    public int State2N;

    final TileLogicAdv this$0;

    public TileLogicAdv$LogicAdvXcvr(TileLogicAdv var1)
    {
        super(var1);
        this.this$0 = var1;
        this.State1 = 0;
        this.State2 = 0;
        this.State1N = 0;
        this.State2N = 0;
    }

    public void updatePowerState()
    {
        int var1 = RedPowerLib.getRotPowerState(this.this$0.worldObj, this.this$0.xCoord, this.this$0.yCoord, this.this$0.zCoord, 5, this.this$0.Rotation, 0);

        if (var1 != this.this$0.PowerState)
        {
            this.this$0.PowerState = var1;
            this.this$0.updateBlock();
            this.this$0.scheduleTick(2);
        }
    }

    public void tileTick()
    {
        this.this$0.Powered = (this.this$0.PowerState & 1) > 0;
        this.this$0.Active = (this.this$0.PowerState & 4) > 0;
        int var1 = this.State1N;
        int var2 = this.State2N;

        if (this.this$0.Deadmap == 0)
        {
            if (!this.this$0.Powered)
            {
                var1 = 0;
            }

            if (!this.this$0.Active)
            {
                var2 = 0;
            }
        }
        else
        {
            if (!this.this$0.Powered)
            {
                var2 = 0;
            }

            if (!this.this$0.Active)
            {
                var1 = 0;
            }
        }

        boolean var3 = this.State1 != var1 || this.State2 != var2;
        this.State1 = var1;
        this.State2 = var2;

        if (var3)
        {
            this.this$0.updateBlock();
            RedPowerLib.updateCurrent(this.this$0.worldObj, this.this$0.xCoord, this.this$0.yCoord, this.this$0.zCoord);
        }

        this.updatePowerState();
        this.updateCurrentStrength();
    }

    public int getSubType()
    {
        return 0;
    }

    public int getPoweringMask(int var1)
    {
        int var2 = 0;

        if (var1 >= 1 && var1 <= 16)
        {
            --var1;

            if ((this.State1 >> var1 & 1) > 0)
            {
                var2 |= 8;
            }

            if ((this.State2 >> var1 & 1) > 0)
            {
                var2 |= 2;
            }

            return RedPowerLib.mapRotToCon(var2, this.this$0.Rotation);
        }
        else
        {
            return 0;
        }
    }

    public void updateCurrentStrength()
    {
        if (!this.this$0.isTickRunnable())
        {
            this.State1N = this.State2;
            this.State2N = this.State1;

            for (int var1 = 0; var1 < 16; ++var1)
            {
                short var2 = (short)RedPowerLib.updateBlockCurrentStrength(this.this$0.worldObj, this.this$0, this.this$0.xCoord, this.this$0.yCoord, this.this$0.zCoord, RedPowerLib.mapRotToCon(2, this.this$0.Rotation), 2 << var1);
                short var3 = (short)RedPowerLib.updateBlockCurrentStrength(this.this$0.worldObj, this.this$0, this.this$0.xCoord, this.this$0.yCoord, this.this$0.zCoord, RedPowerLib.mapRotToCon(8, this.this$0.Rotation), 2 << var1);

                if (var2 > 0)
                {
                    this.State1N |= 1 << var1;
                }

                if (var3 > 0)
                {
                    this.State2N |= 1 << var1;
                }
            }

            this.this$0.dirtyBlock();

            if (this.State1N != this.State1 || this.State2N != this.State2)
            {
                this.this$0.scheduleTick(2);
            }
        }
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        this.State1 = var1.getInteger("s1");
        this.State2 = var1.getInteger("s2");
        this.State1N = var1.getInteger("s1n");
        this.State2N = var1.getInteger("s2n");
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        var1.setInteger("s1", this.State1);
        var1.setInteger("s2", this.State2);
        var1.setInteger("s1n", this.State1N);
        var1.setInteger("s2n", this.State2N);
    }

    public void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.State1 = (int)var1.getUVLC();
        this.State2 = (int)var1.getUVLC();
    }

    public void writeToPacket(Packet211TileDesc var1)
    {
        var1.addUVLC((long)this.State1);
        var1.addUVLC((long)this.State2);
    }
}
