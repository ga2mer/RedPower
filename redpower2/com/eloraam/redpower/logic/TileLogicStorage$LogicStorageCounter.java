package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.logic.TileLogicStorage$LogicStorageModule;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;

public class TileLogicStorage$LogicStorageCounter extends TileLogicStorage$LogicStorageModule
{
    public int Count;
    public int CountMax;
    public int Inc;
    public int Dec;

    final TileLogicStorage this$0;

    public TileLogicStorage$LogicStorageCounter(TileLogicStorage var1)
    {
        super(var1);
        this.this$0 = var1;
        this.Count = 0;
        this.CountMax = 10;
        this.Inc = 1;
        this.Dec = 1;
    }

    public void updatePowerState()
    {
        int var1 = RedPowerLib.getRotPowerState(this.this$0.worldObj, this.this$0.xCoord, this.this$0.yCoord, this.this$0.zCoord, 5, this.this$0.Rotation, 0);

        if (var1 != this.this$0.PowerState)
        {
            if ((var1 & ~this.this$0.PowerState & 1) > 0)
            {
                this.this$0.Active = true;
            }

            if ((var1 & ~this.this$0.PowerState & 4) > 0)
            {
                this.this$0.Disabled = true;
            }

            this.this$0.PowerState = var1;
            this.this$0.updateBlock();

            if (this.this$0.Active || this.this$0.Disabled)
            {
                this.this$0.scheduleTick(2);
            }
        }
    }

    public void tileTick()
    {
        int var1 = this.Count;

        if (this.this$0.Deadmap > 0)
        {
            if (this.this$0.Active)
            {
                this.Count -= this.Dec;
                this.this$0.Active = false;
            }

            if (this.this$0.Disabled)
            {
                this.Count += this.Inc;
                this.this$0.Disabled = false;
            }
        }
        else
        {
            if (this.this$0.Active)
            {
                this.Count += this.Inc;
                this.this$0.Active = false;
            }

            if (this.this$0.Disabled)
            {
                this.Count -= this.Dec;
                this.this$0.Disabled = false;
            }
        }

        if (this.Count < 0)
        {
            this.Count = 0;
        }

        if (this.Count > this.CountMax)
        {
            this.Count = this.CountMax;
        }

        if (var1 != this.Count)
        {
            this.this$0.updateBlockChange();
            this.this$0.playSound("random.click", 0.3F, 0.5F, false);
        }

        this.updatePowerState();
    }

    public int getSubType()
    {
        return 0;
    }

    public int getPoweringMask(int var1)
    {
        int var2 = 0;

        if (var1 != 0)
        {
            return 0;
        }
        else
        {
            if (this.Count == 0)
            {
                var2 |= 2;
            }

            if (this.Count == this.CountMax)
            {
                var2 |= 8;
            }

            return RedPowerLib.mapRotToCon(var2, this.this$0.Rotation);
        }
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        this.Count = var1.getInteger("cnt");
        this.CountMax = var1.getInteger("max");
        this.Inc = var1.getInteger("inc");
        this.Dec = var1.getInteger("dec");
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        var1.setInteger("cnt", this.Count);
        var1.setInteger("max", this.CountMax);
        var1.setInteger("inc", this.Inc);
        var1.setInteger("dec", this.Dec);
    }

    public void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Count = (int)var1.getUVLC();
        this.CountMax = (int)var1.getUVLC();
    }

    public void writeToPacket(Packet211TileDesc var1)
    {
        var1.addUVLC((long)this.Count);
        var1.addUVLC((long)this.CountMax);
    }
}
