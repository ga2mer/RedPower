package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.ITubeConnectable;
import com.eloraam.redpower.core.ITubeFlow;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TubeFlow;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileAccel$1;
import com.eloraam.redpower.machine.TileAccel$2;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileAccel extends TileMachinePanel implements IBluePowerConnectable, ITubeFlow
{
    TubeFlow flow = new TileAccel$1(this);
    BluePowerEndpoint cond = new TileAccel$2(this);
    private boolean hasChanged = false;
    public int ConMask = -1;
    public int conCache = -1;

    public int getTubeConnectableSides()
    {
        return 3 << (this.Rotation & 6);
    }

    public int getTubeConClass()
    {
        return 17;
    }

    public boolean canRouteItems()
    {
        return true;
    }

    public boolean tubeItemEnter(int var1, int var2, TubeItem var3)
    {
        if (var2 != 0)
        {
            return false;
        }
        else if (var1 != this.Rotation && var1 != (this.Rotation ^ 1))
        {
            return false;
        }
        else
        {
            var3.side = (byte)var1;
            this.flow.add(var3);
            this.hasChanged = true;
            this.dirtyBlock();
            return true;
        }
    }

    public boolean tubeItemCanEnter(int var1, int var2, TubeItem var3)
    {
        return var2 == 0;
    }

    public int tubeWeight(int var1, int var2)
    {
        return 0;
    }

    public void addTubeItem(TubeItem var1)
    {
        var1.side = (byte)(var1.side ^ 1);
        this.flow.add(var1);
        this.hasChanged = true;
        this.dirtyBlock();
    }

    public TubeFlow getTubeFlow()
    {
        return this.flow;
    }

    public int getPartMaxRotation(int var1, boolean var2)
    {
        return var2 ? 0 : 5;
    }

    public int getLightValue()
    {
        return this.Charged ? 6 : 0;
    }

    public void recache()
    {
        if (this.conCache < 0)
        {
            WorldCoord var1 = new WorldCoord(this);
            ITubeConnectable var2 = (ITubeConnectable)CoreLib.getTileEntity(this.worldObj, var1.coordStep(this.Rotation), ITubeConnectable.class);
            ITubeConnectable var3 = (ITubeConnectable)CoreLib.getTileEntity(this.worldObj, var1.coordStep(this.Rotation ^ 1), ITubeConnectable.class);
            this.conCache = 0;
            int var4;

            if (var2 != null)
            {
                var4 = var2.getTubeConClass();

                if (var4 < 17)
                {
                    this.conCache |= 1;
                }
                else if (var4 >= 17)
                {
                    this.conCache |= 2;
                }
            }

            if (var3 != null)
            {
                var4 = var3.getTubeConClass();

                if (var4 < 17)
                {
                    this.conCache |= 4;
                }
                else if (var4 >= 17)
                {
                    this.conCache |= 8;
                }
            }
        }
    }

    public int getConnectableMask()
    {
        return 1073741823;
    }

    public int getConnectClass(int var1)
    {
        return 65;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return this.cond;
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        this.flow.onRemove();
        this.breakBlock();
    }

    public int getExtendedID()
    {
        return 2;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (this.flow.update())
        {
            this.hasChanged = true;
        }

        if (this.hasChanged)
        {
            this.hasChanged = false;

            if (!CoreLib.isClient(this.worldObj))
            {
                this.sendItemUpdate();
            }

            this.dirtyBlock();
        }

        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }

            this.cond.iterate();
            this.dirtyBlock();

            if (this.cond.Flow == 0)
            {
                if (this.Charged)
                {
                    this.Charged = false;
                    this.updateBlock();
                    this.updateLight();
                }
            }
            else if (!this.Charged)
            {
                this.Charged = true;
                this.updateBlock();
                this.updateLight();
            }
        }
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = this.getFacing(var3);
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;
        this.conCache = -1;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.cond.readFromNBT(var1);
        this.flow.readFromNBT(var1);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.cond.writeToNBT(var1);
        this.flow.writeToNBT(var1);
    }

    protected void sendItemUpdate()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 10;
        var1.xCoord = this.xCoord;
        var1.yCoord = this.yCoord;
        var1.zCoord = this.zCoord;
        int var2 = this.flow.contents.size();

        if (var2 > 6)
        {
            var2 = 6;
        }

        var1.addUVLC((long)var2);
        Iterator var3 = this.flow.contents.iterator();

        for (int var4 = 0; var4 < var2; ++var4)
        {
            TubeItem var5 = (TubeItem)var3.next();
            var5.writeToPacket(var1);
        }

        var1.encode();
        CoreProxy.sendPacketToPosition(this.worldObj, var1, this.xCoord, this.zCoord);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        if (var1.subId == 10)
        {
            this.flow.contents.clear();
            int var2 = (int)var1.getUVLC();

            for (int var3 = 0; var3 < var2; ++var3)
            {
                this.flow.contents.add(TubeItem.newFromPacket(var1));
            }
        }
        else
        {
            super.readFromPacket(var1);
            this.updateBlock();
        }
    }
}
