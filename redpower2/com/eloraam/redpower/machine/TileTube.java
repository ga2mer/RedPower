package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.IPaintable;
import com.eloraam.redpower.core.ITubeFlow;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.TileCovered;
import com.eloraam.redpower.core.TubeFlow;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.machine.TileTube$1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileTube extends TileCovered implements ITubeFlow, IPaintable
{
    protected TubeFlow flow = new TileTube$1(this);
    public byte lastDir = 0;
    public byte paintColor = 0;
    private boolean hasChanged = false;

    public int getTubeConnectableSides()
    {
        int var2 = 63;

        for (int var1 = 0; var1 < 6; ++var1)
        {
            if ((this.CoverSides & 1 << var1) > 0 && this.Covers[var1] >> 8 < 3)
            {
                var2 &= ~(1 << var1);
            }
        }

        return var2;
    }

    public int getTubeConClass()
    {
        return this.paintColor;
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
        else if (var3.color != 0 && this.paintColor != 0 && var3.color != this.paintColor)
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
        return var3.color != 0 && this.paintColor != 0 && var3.color != this.paintColor ? false : var2 == 0;
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

    public boolean tryPaint(int var1, int var2, int var3)
    {
        if (var1 == 29)
        {
            if (this.paintColor == var3)
            {
                return false;
            }
            else
            {
                this.paintColor = (byte)var3;
                this.updateBlockChange();
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean canUpdate()
    {
        return true;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
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
    }

    public int getBlockID()
    {
        return RedPowerBase.blockMicro.blockID;
    }

    public int getExtendedID()
    {
        return 8;
    }

    public void onBlockNeighborChange(int var1) {}

    public int getPartsMask()
    {
        return this.CoverSides | 536870912;
    }

    public int getSolidPartsMask()
    {
        return this.CoverSides | 536870912;
    }

    public boolean blockEmpty()
    {
        return false;
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        if (var2 == 29)
        {
            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(RedPowerBase.blockMicro.blockID, 1, this.getExtendedID() << 8));
            this.flow.onRemove();

            if (this.CoverSides > 0)
            {
                this.replaceWithCovers();
            }
            else
            {
                this.deleteBlock();
            }
        }
        else
        {
            super.onHarvestPart(var1, var2);
        }
    }

    public void addHarvestContents(ArrayList var1)
    {
        super.addHarvestContents(var1);
        var1.add(new ItemStack(RedPowerBase.blockMicro.blockID, 1, this.getExtendedID() << 8));
    }

    public float getPartStrength(EntityPlayer var1, int var2)
    {
        BlockMachine var3 = RedPowerMachine.blockMachine;
        return var2 == 29 ? var1.getCurrentPlayerStrVsBlock(var3, false) / (var3.getHardness() * 30.0F) : super.getPartStrength(var1, var2);
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        if (var2 == 29)
        {
            var1.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
        }
        else
        {
            super.setPartBounds(var1, var2);
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.flow.readFromNBT(var1);
        this.lastDir = var1.getByte("lDir");
        this.paintColor = var1.getByte("pCol");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.flow.writeToNBT(var1);
        var1.setByte("lDir", this.lastDir);
        var1.setByte("pCol", this.paintColor);
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
            this.paintColor = (byte)var1.getByte();
            this.updateBlock();
        }
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        var1.addByte(this.paintColor);
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

    public void handlePacket(Packet211TileDesc var1)
    {
        try
        {
            this.readFromPacket(var1);
        }
        catch (IOException var3)
        {
            ;
        }
    }
}
