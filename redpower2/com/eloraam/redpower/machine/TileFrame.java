package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.IFrameLink;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.TileCoverable;
import com.eloraam.redpower.core.WorldCoord;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;

public class TileFrame extends TileCoverable implements IHandlePackets, IFrameLink, IFrameSupport
{
    public int CoverSides = 0;
    public int StickySides = 63;
    public short[] Covers = new short[6];

    public boolean isFrameMoving()
    {
        return false;
    }

    public boolean canFrameConnectIn(int var1)
    {
        return (this.StickySides & 1 << var1) > 0;
    }

    public boolean canFrameConnectOut(int var1)
    {
        return (this.StickySides & 1 << var1) > 0;
    }

    public WorldCoord getFrameLinkset()
    {
        return null;
    }

    public int getExtendedID()
    {
        return 0;
    }

    public void onBlockNeighborChange(int var1) {}

    public int getBlockID()
    {
        return RedPowerMachine.blockFrame.blockID;
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
        boolean var3 = false;

        if (var2 == 29)
        {
            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(RedPowerMachine.blockFrame, 1));

            if (this.CoverSides > 0)
            {
                this.replaceWithCovers();
                this.updateBlockChange();
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
        var1.add(new ItemStack(RedPowerMachine.blockFrame, 1));
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
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            super.setPartBounds(var1, var2);
        }
    }

    public boolean canAddCover(int var1, int var2)
    {
        if (var1 > 5)
        {
            return false;
        }
        else
        {
            int var3 = var2 >> 8;
            return var3 != 0 && var3 != 1 && var3 != 3 && var3 != 4 ? false : (this.CoverSides & 1 << var1) <= 0;
        }
    }

    void rebuildSticky()
    {
        int var1 = 0;

        for (int var2 = 0; var2 < 6; ++var2)
        {
            int var3 = 1 << var2;

            if ((this.CoverSides & var3) == 0)
            {
                var1 |= var3;
            }
            else
            {
                int var4 = this.Covers[var2] >> 8;

                if (var4 == 1 || var4 == 4)
                {
                    var1 |= var3;
                }
            }
        }

        this.StickySides = var1;
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
            this.rebuildSticky();
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
            this.rebuildSticky();
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

    public void replaceWithCovers()
    {
        short[] var1 = Arrays.copyOf(this.Covers, 29);
        CoverLib.replaceWithCovers(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.CoverSides, var1);
    }

    public byte[] getFramePacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 9;
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

    public void onFramePickup(IBlockAccess var1) {}

    public void onFrameRefresh(IBlockAccess var1) {}

    public void onFrameDrop() {}

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        int var4 = var1.getInteger("cvm") & 63;
        this.CoverSides |= var4;
        byte[] var3 = var1.getByteArray("cvs");

        if (var3 != null && var4 > 0)
        {
            int var5 = 0;

            for (int var2 = 0; var2 < 6; ++var2)
            {
                if ((var4 & 1 << var2) != 0)
                {
                    this.Covers[var2] = (short)((var3[var5] & 255) + ((var3[var5 + 1] & 255) << 8));
                    var5 += 2;
                }
            }
        }

        this.rebuildSticky();
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

        for (int var4 = 0; var4 < 6; ++var4)
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
        if (var1.subId == 9)
        {
            this.CoverSides = (int)var1.getUVLC();

            for (int var2 = 0; var2 < 6; ++var2)
            {
                if ((this.CoverSides & 1 << var2) > 0)
                {
                    this.Covers[var2] = (short)((int)var1.getUVLC());
                }
            }

            this.rebuildSticky();
        }
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addUVLC((long)this.CoverSides);

        for (int var2 = 0; var2 < 6; ++var2)
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
        var1.subId = 9;
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
