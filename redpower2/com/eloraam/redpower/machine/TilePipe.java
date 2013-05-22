package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.FluidBuffer;
import com.eloraam.redpower.core.IPipeConnectable;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.PipeLib;
import com.eloraam.redpower.core.TileCovered;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TilePipe$1;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

public class TilePipe extends TileCovered implements IPipeConnectable
{
    public FluidBuffer pipebuf = new TilePipe$1(this);
    public int Pressure = 0;
    public int ConCache = -1;
    public int Flanges = -1;
    private boolean hasChanged = false;

    public int getPipeConnectableSides()
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

    public int getPipeFlangeSides()
    {
        this.cacheCon();
        return this.ConCache != 3 && this.ConCache != 12 && this.ConCache != 48 ? (Integer.bitCount(this.ConCache) == 1 ? 0 : this.ConCache) : 0;
    }

    public int getPipePressure(int var1)
    {
        return this.Pressure;
    }

    public FluidBuffer getPipeBuffer(int var1)
    {
        return this.pipebuf;
    }

    public boolean tryAddCover(int var1, int var2)
    {
        if (!super.tryAddCover(var1, var2))
        {
            return false;
        }
        else
        {
            this.uncache();
            this.updateBlockChange();
            return true;
        }
    }

    public int tryRemoveCover(int var1)
    {
        int var2 = super.tryRemoveCover(var1);

        if (var2 < 0)
        {
            return -1;
        }
        else
        {
            this.uncache();
            this.updateBlockChange();
            return var2;
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
        super.updateEntity();

        if (!CoreLib.isClient(this.worldObj))
        {
            int var1 = 0;
            int var2 = 0;
            int var3 = 0;
            int var4 = 0;
            this.cacheCon();

            for (int var5 = 0; var5 < 6; ++var5)
            {
                if ((this.ConCache & 1 << var5) != 0)
                {
                    WorldCoord var6 = new WorldCoord(this);
                    var6.step(var5);
                    Integer var7 = PipeLib.getPressure(this.worldObj, var6, var5 ^ 1);

                    if (var7 != null)
                    {
                        var3 = Math.min(var7.intValue(), var3);
                        var4 = Math.max(var7.intValue(), var4);
                        var1 += var7.intValue();
                        ++var2;
                    }
                }
            }

            if (var2 == 0)
            {
                this.Pressure = 0;
            }
            else
            {
                if (var3 < 0)
                {
                    ++var3;
                }

                if (var4 > 0)
                {
                    --var4;
                }

                this.Pressure = Math.max(var3, Math.min(var4, var1 / var2 + Integer.signum(var1)));
            }

            PipeLib.movePipeLiquid(this.worldObj, this, new WorldCoord(this), this.ConCache);
            this.dirtyBlock();

            if (!CoreLib.isClient(this.worldObj) && (this.worldObj.getWorldTime() & 16L) == 0L)
            {
                this.sendItemUpdate();
            }
        }
    }

    public void uncache()
    {
        this.ConCache = -1;
        this.Flanges = -1;
    }

    public void cacheCon()
    {
        if (this.ConCache < 0)
        {
            this.ConCache = PipeLib.getConnections(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void cacheFlange()
    {
        if (this.Flanges < 0)
        {
            this.cacheCon();
            this.Flanges = this.getPipeFlangeSides();
            this.Flanges |= PipeLib.getFlanges(this.worldObj, new WorldCoord(this), this.ConCache);
        }
    }

    public void onFrameRefresh(IBlockAccess var1)
    {
        if (this.ConCache < 0)
        {
            this.ConCache = PipeLib.getConnections(var1, this.xCoord, this.yCoord, this.zCoord);
        }

        this.Flanges = 0;
    }

    public int getBlockID()
    {
        return RedPowerBase.blockMicro.blockID;
    }

    public int getExtendedID()
    {
        return 7;
    }

    public void onBlockNeighborChange(int var1)
    {
        int var2 = this.Flanges;
        int var3 = this.ConCache;
        this.uncache();
        this.cacheFlange();

        if (this.Flanges != var2 || var3 != this.ConCache)
        {
            this.updateBlock();
        }
    }

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

            if (this.CoverSides > 0)
            {
                this.replaceWithCovers();
            }
            else
            {
                this.deleteBlock();
            }

            this.uncache();
            this.updateBlockChange();
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
        this.Pressure = var1.getInteger("psi");
        this.pipebuf.readFromNBT(var1, "buf");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("psi", this.Pressure);
        this.pipebuf.writeToNBT(var1, "buf");
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        if (var1.subId == 11)
        {
            this.pipebuf.readFromPacket(var1);
            this.updateBlock();
        }
        else
        {
            super.readFromPacket(var1);
            this.pipebuf.readFromPacket(var1);
            this.ConCache = -1;
            this.Flanges = -1;
            this.updateBlock();
        }
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        this.pipebuf.writeToPacket(var1);
    }

    protected void sendItemUpdate()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 11;
        var1.xCoord = this.xCoord;
        var1.yCoord = this.yCoord;
        var1.zCoord = this.zCoord;
        this.pipebuf.writeToPacket(var1);
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
