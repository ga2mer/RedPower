package com.eloraam.redpower.wiring;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.base.BlockMicro;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.IWiring;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileCovered;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

public abstract class TileWiring extends TileCovered implements IWiring
{
    public int ConSides = 0;
    public int Metadata = 0;
    public short CenterPost = 0;
    public int ConMask = -1;
    public int EConMask = -1;
    public int EConEMask = -1;
    public int ConaMask = -1;

    public float getWireHeight()
    {
        return 0.125F;
    }

    public void uncache0()
    {
        this.EConMask = -1;
        this.EConEMask = -1;
        this.ConMask = -1;
    }

    public void uncache()
    {
        if (this.ConaMask >= 0 || this.EConMask >= 0 || this.ConMask >= 0)
        {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }

        this.ConaMask = -1;
        this.EConMask = -1;
        this.EConEMask = -1;
        this.ConMask = -1;
    }

    private static int stripBlockConMask(int var0)
    {
        switch (var0)
        {
            case 0:
                return 257;

            case 1:
                return 4098;

            case 2:
                return 65540;

            case 3:
                return 1048584;

            case 4:
                return 263168;

            case 5:
                return 540672;

            case 6:
                return 4196352;

            case 7:
                return 8421376;

            case 8:
                return 528;

            case 9:
                return 8224;

            case 10:
                return 131136;

            default:
                return 2097280;
        }
    }

    public int getConnectableMask()
    {
        if (this.ConaMask >= 0)
        {
            return this.ConaMask;
        }
        else
        {
            int var3 = 0;

            if ((this.ConSides & 1) > 0)
            {
                var3 |= 15;
            }

            if ((this.ConSides & 2) > 0)
            {
                var3 |= 240;
            }

            if ((this.ConSides & 4) > 0)
            {
                var3 |= 3840;
            }

            if ((this.ConSides & 8) > 0)
            {
                var3 |= 61440;
            }

            if ((this.ConSides & 16) > 0)
            {
                var3 |= 983040;
            }

            if ((this.ConSides & 32) > 0)
            {
                var3 |= 15728640;
            }

            if ((this.CoverSides & 1) > 0)
            {
                var3 &= -1118465;
            }

            if ((this.CoverSides & 2) > 0)
            {
                var3 &= -2236929;
            }

            if ((this.CoverSides & 4) > 0)
            {
                var3 &= -4456466;
            }

            if ((this.CoverSides & 8) > 0)
            {
                var3 &= -8912931;
            }

            if ((this.CoverSides & 16) > 0)
            {
                var3 &= -17477;
            }

            if ((this.CoverSides & 32) > 0)
            {
                var3 &= -34953;
            }

            int var1;

            for (var1 = 0; var1 < 12; ++var1)
            {
                if ((this.CoverSides & 16384 << var1) > 0)
                {
                    var3 &= ~stripBlockConMask(var1);
                }
            }

            if ((this.ConSides & 64) > 0)
            {
                var3 |= 1056964608;

                for (var1 = 0; var1 < 6; ++var1)
                {
                    if ((this.CoverSides & 1 << var1) > 0)
                    {
                        int var2 = this.Covers[var1] >> 8;

                        if (var2 < 3)
                        {
                            var3 &= ~(1 << var1 + 24);
                        }

                        if (var2 == 5)
                        {
                            var3 &= 3 << (var1 & -2) + 24;
                        }
                    }
                }
            }

            this.ConaMask = var3;
            return var3;
        }
    }

    public int getConnectionMask()
    {
        if (this.ConMask >= 0)
        {
            return this.ConMask;
        }
        else
        {
            this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
            return this.ConMask;
        }
    }

    public int getExtConnectionMask()
    {
        if (this.EConMask >= 0)
        {
            return this.EConMask;
        }
        else
        {
            this.EConMask = RedPowerLib.getExtConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
            this.EConEMask = RedPowerLib.getExtConnectionExtras(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
            return this.EConMask;
        }
    }

    public int getCornerPowerMode()
    {
        return 1;
    }

    public void onFrameRefresh(IBlockAccess var1)
    {
        if (this.ConMask < 0)
        {
            this.ConMask = RedPowerLib.getConnections(var1, this, this.xCoord, this.yCoord, this.zCoord);
        }

        if (this.EConMask < 0)
        {
            this.EConMask = RedPowerLib.getExtConnections(var1, this, this.xCoord, this.yCoord, this.zCoord);
            this.EConEMask = RedPowerLib.getExtConnectionExtras(var1, this, this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        if (this.EConMask >= 0 || this.ConMask >= 0)
        {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }

        this.ConMask = -1;
        this.EConMask = -1;
        this.EConEMask = -1;
        this.refreshBlockSupport();
        RedPowerLib.updateCurrent(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public int getExtendedMetadata()
    {
        return this.Metadata;
    }

    public void setExtendedMetadata(int var1)
    {
        this.Metadata = var1;
    }

    public boolean canAddCover(int var1, int var2)
    {
        if (var1 < 6 && (this.ConSides & 1 << var1) > 0)
        {
            return false;
        }
        else if ((this.CoverSides & 1 << var1) > 0)
        {
            return false;
        }
        else
        {
            short[] var3 = Arrays.copyOf(this.Covers, 29);
            var3[var1] = (short)var2;
            return CoverLib.checkPlacement(this.CoverSides | 1 << var1, var3, this.ConSides, (this.ConSides & 64) > 0);
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

    public boolean blockEmpty()
    {
        return this.CoverSides == 0 && this.ConSides == 0;
    }

    public void addHarvestContents(ArrayList var1)
    {
        super.addHarvestContents(var1);

        for (int var2 = 0; var2 < 6; ++var2)
        {
            if ((this.ConSides & 1 << var2) != 0)
            {
                var1.add(new ItemStack(RedPowerBase.blockMicro.blockID, 1, this.getExtendedID() * 256 + this.Metadata));
            }
        }

        if ((this.ConSides & 64) > 0)
        {
            int var3 = 16384 + this.CenterPost;

            if (this.getExtendedID() == 3)
            {
                var3 += 256;
            }

            if (this.getExtendedID() == 5)
            {
                var3 += 512;
            }

            var1.add(new ItemStack(RedPowerBase.blockMicro.blockID, 1, var3));
        }
    }

    public int getPartsMask()
    {
        return this.CoverSides | this.ConSides & 63 | (this.ConSides & 64) << 23;
    }

    public int getSolidPartsMask()
    {
        return this.CoverSides | (this.ConSides & 64) << 23;
    }

    public boolean refreshBlockSupport()
    {
        boolean var2 = false;
        int var1 = this.ConSides & 63;

        if (var1 == 3 || var1 == 12 || var1 == 48)
        {
            var2 = true;
        }

        for (var1 = 0; var1 < 6; ++var1)
        {
            if ((this.ConSides & 1 << var1) != 0 && (var2 || !RedPowerLib.canSupportWire(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var1)))
            {
                this.uncache();
                CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(RedPowerBase.blockMicro.blockID, 1, this.getExtendedID() * 256 + this.Metadata));
                this.ConSides &= ~(1 << var1);
            }
        }

        if (this.ConSides == 0)
        {
            if (this.CoverSides > 0)
            {
                this.replaceWithCovers();
            }
            else
            {
                this.deleteBlock();
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        boolean var3 = false;

        if (var2 == 29 && (this.ConSides & 64) > 0)
        {
            int var4 = 16384 + this.CenterPost;

            if (this.getExtendedID() == 3)
            {
                var4 += 256;
            }

            if (this.getExtendedID() == 5)
            {
                var4 += 512;
            }

            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(RedPowerBase.blockMicro.blockID, 1, var4));
            this.ConSides &= 63;
        }
        else
        {
            if ((this.ConSides & 1 << var2) <= 0)
            {
                super.onHarvestPart(var1, var2);
                return;
            }

            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(RedPowerBase.blockMicro.blockID, 1, this.getExtendedID() * 256 + this.Metadata));
            this.ConSides &= ~(1 << var2);
        }

        this.uncache();

        if (this.ConSides == 0)
        {
            if (this.CoverSides > 0)
            {
                this.replaceWithCovers();
            }
            else
            {
                this.deleteBlock();
            }
        }

        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        RedPowerLib.updateIndirectNeighbors(this.worldObj, this.xCoord, this.yCoord, this.zCoord, RedPowerBase.blockMicro.blockID);
    }

    public float getPartStrength(EntityPlayer var1, int var2)
    {
        BlockMicro var3 = RedPowerBase.blockMicro;
        return var2 == 29 && (this.ConSides & 64) > 0 ? var1.getCurrentPlayerStrVsBlock(var3, false) / (var3.getHardness() * 30.0F) : ((this.ConSides & 1 << var2) > 0 ? var1.getCurrentPlayerStrVsBlock(var3, false) / (var3.getHardness() * 30.0F) : super.getPartStrength(var1, var2));
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        if (var2 == 29)
        {
            if ((this.ConSides & 64) == 0)
            {
                super.setPartBounds(var1, var2);
                return;
            }
        }
        else if ((this.ConSides & 1 << var2) == 0)
        {
            super.setPartBounds(var1, var2);
            return;
        }

        float var3 = this.getWireHeight();

        switch (var2)
        {
            case 0:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
                break;

            case 1:
                var1.setBlockBounds(0.0F, 1.0F - var3, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 2:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var3);
                break;

            case 3:
                var1.setBlockBounds(0.0F, 0.0F, 1.0F - var3, 1.0F, 1.0F, 1.0F);
                break;

            case 4:
                var1.setBlockBounds(0.0F, 0.0F, 0.0F, var3, 1.0F, 1.0F);
                break;

            case 5:
                var1.setBlockBounds(1.0F - var3, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 29:
                var1.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.ConSides = var1.getByte("cons") & 255;
        this.Metadata = var1.getByte("md") & 255;
        this.CenterPost = (short)(var1.getShort("post") & 255);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("cons", (byte)this.ConSides);
        var1.setByte("md", (byte)this.Metadata);
        var1.setShort("post", this.CenterPost);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);
        this.Metadata = var1.getByte();
        this.ConSides = var1.getByte();

        if ((this.ConSides & 64) > 0)
        {
            this.CenterPost = (short)((int)var1.getUVLC());
        }

        this.ConaMask = -1;
        this.EConMask = -1;
        this.EConEMask = -1;
        this.ConMask = -1;
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        var1.addByte(this.Metadata);
        var1.addByte(this.ConSides);

        if ((this.ConSides & 64) > 0)
        {
            var1.addUVLC((long)this.CenterPost);
        }
    }
}
