package com.eloraam.redpower.core;

import java.io.IOException;
import java.util.Arrays;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;

public class TileCovered extends TileCoverable implements IHandlePackets, IFrameSupport
{
    public int CoverSides = 0;
    public short[] Covers = new short[29];

    public void replaceWithCovers()
    {
        CoverLib.replaceWithCovers(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.CoverSides, this.Covers);
    }

    public boolean canUpdate()
    {
        return false;
    }

    public int getExtendedID()
    {
        return 0;
    }

    public void onBlockNeighborChange(int var1)
    {
        if (this.CoverSides == 0)
        {
            this.deleteBlock();
        }
    }

    public int getBlockID()
    {
        return CoverLib.blockCoverPlate.blockID;
    }

    public boolean canAddCover(int var1, int var2)
    {
        if ((this.CoverSides & 1 << var1) > 0)
        {
            return false;
        }
        else
        {
            short[] var3 = Arrays.copyOf(this.Covers, 29);
            var3[var1] = (short)var2;
            return CoverLib.checkPlacement(this.CoverSides | 1 << var1, var3, 0, false);
        }
    }

    public boolean tryAddCover(int var1, int var2)
    {
        if (!this.canAddCover(var1, var2))
        {
            return false;
        }
        else
        {
            this.CoverSides |= 1 << var1;
            this.Covers[var1] = (short)var2;
            this.updateBlockChange();
            return true;
        }
    }

    public int tryRemoveCover(int var1)
    {
        if ((this.CoverSides & 1 << var1) == 0)
        {
            return -1;
        }
        else
        {
            this.CoverSides &= ~(1 << var1);
            short var2 = this.Covers[var1];
            this.Covers[var1] = 0;
            this.updateBlockChange();
            return var2;
        }
    }

    public int getCover(int var1)
    {
        return (this.CoverSides & 1 << var1) == 0 ? -1 : this.Covers[var1];
    }

    public int getCoverMask()
    {
        return this.CoverSides;
    }

    public boolean blockEmpty()
    {
        return this.CoverSides == 0;
    }

    public byte[] getFramePacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 5;
        this.writeToPacket(var1);
        var1.headout.write(var1.subId);
        return var1.toByteArray();
    }

    public void handleFramePacket(byte[] var1) throws IOException
    {
        Packet211TileDesc var2 = new Packet211TileDesc(var1);
        var2.subId = var2.getByte();
        this.readFromPacket(var2);
    }

    public void onFrameRefresh(IBlockAccess var1) {}

    public void onFramePickup(IBlockAccess var1) {}

    public void onFrameDrop() {}

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        int var4 = var1.getInteger("cvm") & 536870911;
        this.CoverSides |= var4;
        byte[] var3 = var1.getByteArray("cvs");

        if (var3 != null && var4 > 0)
        {
            int var5 = 0;

            for (int var2 = 0; var2 < 29; ++var2)
            {
                if ((var4 & 1 << var2) != 0)
                {
                    this.Covers[var2] = (short)((var3[var5] & 255) + ((var3[var5 + 1] & 255) << 8));
                    var5 += 2;
                }
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("cvm", this.CoverSides);
        byte[] var2 = new byte[Integer.bitCount(this.CoverSides) * 2];
        int var3 = 0;

        for (int var4 = 0; var4 < 29; ++var4)
        {
            if ((this.CoverSides & 1 << var4) != 0)
            {
                var2[var3] = (byte)(this.Covers[var4] & 255);
                var2[var3 + 1] = (byte)(this.Covers[var4] >> 8);
                var3 += 2;
            }
        }

        var1.setByteArray("cvs", var2);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        if (var1.subId == 5)
        {
            this.CoverSides = (int)var1.getUVLC();

            for (int var2 = 0; var2 < 29; ++var2)
            {
                if ((this.CoverSides & 1 << var2) > 0)
                {
                    this.Covers[var2] = (short)((int)var1.getUVLC());
                }
            }
        }
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addUVLC((long)this.CoverSides);

        for (int var2 = 0; var2 < 29; ++var2)
        {
            if ((this.CoverSides & 1 << var2) > 0)
            {
                var1.addUVLC((long)this.Covers[var2]);
            }
        }
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 5;
        var1.xCoord = this.xCoord;
        var1.yCoord = this.yCoord;
        var1.zCoord = this.zCoord;
        this.writeToPacket(var1);
        var1.encode();
        return var1;
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

        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
}
