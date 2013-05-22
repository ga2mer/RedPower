package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.CoreLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileRelay extends TileEjectBase
{
    public int getExtendedID()
    {
        return 15;
    }

    public void onTileTick()
    {
        super.onTileTick();

        if (!CoreLib.isClient(this.worldObj))
        {
            if (!this.Active)
            {
                if (this.handleExtract())
                {
                    this.Active = true;
                    this.updateBlock();
                    this.scheduleTick(5);
                }
            }
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.isTickScheduled())
        {
            this.scheduleTick(10);
        }
    }

    public boolean onBlockActivated(EntityPlayer var1)
    {
        if (var1.isSneaking())
        {
            return false;
        }
        else if (CoreLib.isClient(this.worldObj))
        {
            return true;
        }
        else
        {
            var1.openGui(RedPowerMachine.instance, 13, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    protected boolean handleExtract()
    {
        for (int var1 = 0; var1 < this.getSizeInventory(); ++var1)
        {
            ItemStack var2 = this.getStackInSlot(var1);

            if (var2 != null && var2.stackSize != 0)
            {
                this.addToBuffer(this.contents[var1]);
                this.setInventorySlotContents(var1, (ItemStack)null);
                this.drainBuffer();
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Relay";
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
