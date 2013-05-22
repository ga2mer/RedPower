package com.eloraam.redpower.logic;

import com.eloraam.redpower.RedPowerLogic;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.IRedPowerConnectable;
import com.eloraam.redpower.core.IRotatable;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileCoverable;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;

public class TileLogic extends TileCoverable implements IHandlePackets, IRedPowerConnectable, IRotatable, IFrameSupport
{
    public int SubId = 0;
    public int Rotation = 0;
    public boolean Powered = false;
    public boolean Disabled = false;
    public boolean Active = false;
    public int PowerState = 0;
    public int Deadmap = 0;
    public int Cover = 255;

    public int getPartMaxRotation(int var1, boolean var2)
    {
        return var2 ? 0 : (var1 != this.Rotation >> 2 ? 0 : 3);
    }

    public int getPartRotation(int var1, boolean var2)
    {
        return var2 ? 0 : (var1 != this.Rotation >> 2 ? 0 : this.Rotation & 3);
    }

    public void setPartRotation(int var1, boolean var2, int var3)
    {
        if (!var2)
        {
            if (var1 == this.Rotation >> 2)
            {
                this.Rotation = var3 & 3 | this.Rotation & -4;
                this.updateBlockChange();
            }
        }
    }

    public int getConnectableMask()
    {
        return 15 << (this.Rotation & -4);
    }

    public int getConnectClass(int var1)
    {
        return 0;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public int getPoweringMask(int var1)
    {
        return var1 != 0 ? 0 : (this.Powered ? RedPowerLib.mapRotToCon(8, this.Rotation) : 0);
    }

    public boolean canAddCover(int var1, int var2)
    {
        return this.Cover != 255 ? false : ((var1 ^ 1) != this.Rotation >> 2 ? false : var2 <= 254);
    }

    public boolean tryAddCover(int var1, int var2)
    {
        if (!this.canAddCover(var1, var2))
        {
            return false;
        }
        else
        {
            this.Cover = var2;
            this.updateBlock();
            return true;
        }
    }

    public int tryRemoveCover(int var1)
    {
        if (this.Cover == 255)
        {
            return -1;
        }
        else if ((var1 ^ 1) != this.Rotation >> 2)
        {
            return -1;
        }
        else
        {
            int var2 = this.Cover;
            this.Cover = 255;
            this.updateBlock();
            return var2;
        }
    }

    public int getCover(int var1)
    {
        return this.Cover == 255 ? -1 : ((var1 ^ 1) != this.Rotation >> 2 ? -1 : this.Cover);
    }

    public int getCoverMask()
    {
        return this.Cover == 255 ? 0 : 1 << (this.Rotation >> 2 ^ 1);
    }

    public boolean blockEmpty()
    {
        return false;
    }

    public void addHarvestContents(ArrayList var1)
    {
        super.addHarvestContents(var1);
        var1.add(new ItemStack(this.getBlockID(), 1, this.getExtendedID() * 256 + this.SubId));
    }

    private void replaceWithCovers()
    {
        if (this.Cover != 255)
        {
            short[] var1 = new short[26];
            var1[this.Rotation >> 2 ^ 1] = (short)this.Cover;
            CoverLib.replaceWithCovers(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 1 << (this.Rotation >> 2 ^ 1), var1);
            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(this.getBlockID(), 1, this.getExtendedID() * 256 + this.SubId));
        }
        else
        {
            this.breakBlock();
            RedPowerLib.updateIndirectNeighbors(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.getBlockID());
        }
    }

    public boolean tryDropBlock()
    {
        if (RedPowerLib.canSupportWire(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.Rotation >> 2))
        {
            return false;
        }
        else
        {
            this.replaceWithCovers();
            return true;
        }
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        if (var2 == this.Rotation >> 2)
        {
            this.replaceWithCovers();
        }
        else
        {
            super.onHarvestPart(var1, var2);
        }
    }

    public float getPartStrength(EntityPlayer var1, int var2)
    {
        BlockLogic var3 = RedPowerLogic.blockLogic;
        return var2 == this.Rotation >> 2 ? var1.getCurrentPlayerStrVsBlock(var3, false) / (var3.getHardness() * 30.0F) : super.getPartStrength(var1, var2);
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        if (var2 != this.Rotation >> 2)
        {
            super.setPartBounds(var1, var2);
        }
        else
        {
            switch (var2)
            {
                case 0:
                    var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
                    break;

                case 1:
                    var1.setBlockBounds(0.0F, 0.875F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 2:
                    var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
                    break;

                case 3:
                    var1.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
                    break;

                case 4:
                    var1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
                    break;

                case 5:
                    var1.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public int getPartsMask()
    {
        int var1 = 1 << (this.Rotation >> 2);

        if (this.Cover != 255)
        {
            var1 |= 1 << (this.Rotation >> 2 ^ 1);
        }

        return var1;
    }

    public int getSolidPartsMask()
    {
        return this.getPartsMask();
    }

    public boolean isBlockStrongPoweringTo(int var1, int var2)
    {
        return (this.getPoweringMask(var1) & RedPowerLib.getConDirMask(var1 ^ 1)) > 0;
    }

    public boolean isBlockWeakPoweringTo(int var1, int var2)
    {
        return (this.getPoweringMask(var1) & RedPowerLib.getConDirMask(var1 ^ 1)) > 0;
    }

    public int getBlockID()
    {
        return RedPowerLogic.blockLogic.blockID;
    }

    public int getExtendedMetadata()
    {
        return this.SubId;
    }

    public void setExtendedMetadata(int var1)
    {
        this.SubId = var1;
    }

    public void playSound(String var1, float var2, float var3, boolean var4)
    {
        if (var4 || RedPowerLogic.EnableSounds)
        {
            this.worldObj.playSoundEffect((double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 0.5F), (double)((float)this.zCoord + 0.5F), var1, var2, var3);
        }
    }

    public void initSubType(int var1)
    {
        this.SubId = var1;

        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.getLightValue() != 9)
            {
                this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    public int getLightValue()
    {
        return 9;
    }

    public byte[] getFramePacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 7;
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
        this.SubId = var1.getByte("sid") & 255;
        this.Rotation = var1.getByte("rot") & 255;
        int var2 = var1.getByte("ps") & 255;
        this.Deadmap = var1.getByte("dm") & 255;
        this.Cover = var1.getByte("cov") & 255;
        this.PowerState = var2 & 15;
        this.Powered = (var2 & 16) > 0;
        this.Disabled = (var2 & 32) > 0;
        this.Active = (var2 & 64) > 0;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("sid", (byte)this.SubId);
        var1.setByte("rot", (byte)this.Rotation);
        int var2 = this.PowerState | (this.Powered ? 16 : 0) | (this.Disabled ? 32 : 0) | (this.Active ? 64 : 0);
        var1.setByte("ps", (byte)var2);
        var1.setByte("dm", (byte)this.Deadmap);
        var1.setByte("cov", (byte)this.Cover);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.SubId = var1.getByte();
        this.Rotation = var1.getByte();
        int var2 = var1.getByte();

        if (CoreLib.isClient(this.worldObj))
        {
            this.PowerState = var2 & 15;
            this.Powered = (var2 & 16) > 0;
            this.Disabled = (var2 & 32) > 0;
            this.Active = (var2 & 64) > 0;
        }

        if ((var2 & 128) > 0)
        {
            this.Deadmap = var1.getByte();
        }
        else
        {
            this.Deadmap = 0;
        }

        this.Cover = var1.getByte();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.SubId);
        var1.addByte(this.Rotation);
        int var2 = this.PowerState | (this.Powered ? 16 : 0) | (this.Disabled ? 32 : 0) | (this.Active ? 64 : 0) | (this.Deadmap > 0 ? 128 : 0);
        var1.addByte(var2);

        if (this.Deadmap > 0)
        {
            var1.addByte(this.Deadmap);
        }

        var1.addByte(this.Cover);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 1;
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
