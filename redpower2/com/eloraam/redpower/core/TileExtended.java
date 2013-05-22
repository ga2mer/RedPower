package com.eloraam.redpower.core;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public abstract class TileExtended extends TileEntity
{
    protected long timeSched = -1L;

    public void onBlockNeighborChange(int var1) {}

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3) {}

    public void onBlockRemoval() {}

    public int isBlockStrongPoweringTo(int var1)
    {
        return 0;
    }

    public int isBlockWeakPoweringTo(int var1)
    {
        return this.isBlockStrongPoweringTo(var1);
    }

    public boolean onBlockActivated(EntityPlayer var1)
    {
        return false;
    }

    public void onEntityCollidedWithBlock(Entity var1) {}

    public AxisAlignedBB getCollisionBoundingBox()
    {
        return null;
    }

    public void onTileTick() {}

    public int getExtendedID()
    {
        return 0;
    }

    public abstract int getBlockID();

    public int getExtendedMetadata()
    {
        return 0;
    }

    public void setExtendedMetadata(int var1) {}

    public void addHarvestContents(ArrayList var1)
    {
        var1.add(new ItemStack(this.getBlockID(), 1, this.getExtendedID()));
    }

    public void scheduleTick(int var1)
    {
        long var2 = this.worldObj.getWorldTime() + (long)var1;

        if (this.timeSched <= 0L || this.timeSched >= var2)
        {
            this.timeSched = var2;
            this.dirtyBlock();
        }
    }

    public boolean isTickRunnable()
    {
        return this.timeSched >= 0L && this.timeSched <= this.worldObj.getWorldTime();
    }

    public boolean isTickScheduled()
    {
        return this.timeSched >= 0L;
    }

    public void updateBlockChange()
    {
        RedPowerLib.updateIndirectNeighbors(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.getBlockID());
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public void updateBlock()
    {
        int var1 = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public void dirtyBlock()
    {
        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public void breakBlock()
    {
        ArrayList var1 = new ArrayList();
        this.addHarvestContents(var1);
        Iterator var2 = var1.iterator();

        while (var2.hasNext())
        {
            ItemStack var3 = (ItemStack)var2.next();
            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var3);
        }

        this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, 0);
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.timeSched >= 0L)
            {
                long var1 = this.worldObj.getWorldTime();

                if (this.timeSched > var1 + 1200L)
                {
                    this.timeSched = var1 + 1200L;
                }
                else if (this.timeSched <= var1)
                {
                    this.timeSched = -1L;
                    this.onTileTick();
                    this.dirtyBlock();
                }
            }
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.timeSched = var1.getLong("sched");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setLong("sched", this.timeSched);
    }
}
