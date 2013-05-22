package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.machine.TileTransformer$1;
import com.eloraam.redpower.machine.TileTransformer$2;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileTransformer extends TileMachinePanel implements IBluePowerConnectable
{
    BluePowerEndpoint cond = new TileTransformer$1(this);
    BluePowerEndpoint cond2 = new TileTransformer$2(this);
    public int ConMask1 = -1;
    public int ConMask2 = -1;

    public int getPartMaxRotation(int var1, boolean var2)
    {
        return var2 ? 0 : 3;
    }

    public int getPartRotation(int var1, boolean var2)
    {
        return var2 ? 0 : this.Rotation & 3;
    }

    public void setPartRotation(int var1, boolean var2, int var3)
    {
        if (!var2)
        {
            this.Rotation = var3 & 3 | this.Rotation & -4;
            this.updateBlockChange();
        }
    }

    public int getConnectableMask()
    {
        return RedPowerLib.mapRotToCon(5, this.Rotation);
    }

    public int getConnectClass(int var1)
    {
        int var2 = RedPowerLib.mapRotToCon(1, this.Rotation);
        return (var2 & RedPowerLib.getConDirMask(var1)) > 0 ? 64 : 68;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return (RedPowerLib.mapRotToCon(1, this.Rotation) & RedPowerLib.getConDirMask(var1)) > 0 ? this.cond : this.cond2;
    }

    public int getExtendedID()
    {
        return 4;
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = (var2 ^ 1) << 2;
        int var4 = (int)Math.floor((double)(var3.rotationYaw / 90.0F + 0.5F));
        int var5 = (int)Math.floor((double)(var3.rotationPitch / 90.0F + 0.5F));
        var4 &= 3;
        int var6 = this.Rotation >> 2;
        int var7;

        switch (var6)
        {
            case 0:
                var7 = var4;
                break;

            case 1:
                var7 = var4 ^ (var4 & 1) << 1;
                break;

            case 2:
                var7 = (var4 & 1) > 0 ? (var5 > 0 ? 2 : 0) : 1 - var4 & 3;
                break;

            case 3:
                var7 = (var4 & 1) > 0 ? (var5 > 0 ? 2 : 0) : var4 - 1 & 3;
                break;

            case 4:
                var7 = (var4 & 1) == 0 ? (var5 > 0 ? 2 : 0) : var4 - 2 & 3;
                break;

            case 5:
                var7 = (var4 & 1) == 0 ? (var5 > 0 ? 2 : 0) : 2 - var4 & 3;
                break;

            default:
                var7 = 0;
        }

        this.Rotation = var6 << 2 | var7;
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask1 = -1;
        this.ConMask2 = -1;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.ConMask1 < 0)
            {
                int var1 = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.ConMask1 = var1 & RedPowerLib.mapRotToCon(1, this.Rotation);
                this.ConMask2 = var1 & RedPowerLib.mapRotToCon(4, this.Rotation);
                this.cond.recache(this.ConMask1, 0);
                this.cond2.recache(this.ConMask2, 0);
            }

            this.cond.iterate();
            this.cond2.iterate();
            this.dirtyBlock();
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        NBTTagCompound var2 = var1.getCompoundTag("c1");
        this.cond.readFromNBT(var2);
        NBTTagCompound var3 = var1.getCompoundTag("c2");
        this.cond2.readFromNBT(var3);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        NBTTagCompound var2 = new NBTTagCompound();
        this.cond.writeToNBT(var2);
        NBTTagCompound var3 = new NBTTagCompound();
        this.cond2.writeToNBT(var3);
        var1.setTag("c1", var2);
        var1.setTag("c2", var3);
    }
}
