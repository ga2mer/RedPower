package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.RedPowerLib;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEject extends TileEjectBase
{
    public int getExtendedID()
    {
        return 14;
    }

    public void onBlockNeighborChange(int var1)
    {
        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 16777215, 63))
        {
            if (!this.Powered)
            {
                this.Powered = true;
                this.dirtyBlock();

                if (!this.Active)
                {
                    this.Active = true;

                    if (this.handleExtract())
                    {
                        this.updateBlock();
                    }
                }
            }
        }
        else
        {
            if (this.Active && !this.isTickScheduled())
            {
                this.scheduleTick(5);
            }

            this.Powered = false;
            this.dirtyBlock();
        }
    }

    protected boolean handleExtract()
    {
        for (int var1 = 0; var1 < this.getSizeInventory(); ++var1)
        {
            ItemStack var2 = this.getStackInSlot(var1);

            if (var2 != null && var2.stackSize != 0)
            {
                this.addToBuffer(this.decrStackSize(var1, 1));
                this.drainBuffer();
                return true;
            }
        }

        return false;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
    }
}
