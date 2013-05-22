package com.eloraam.redpower.lighting;

import com.eloraam.redpower.RedPowerLighting;
import com.eloraam.redpower.core.IConnectable;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileExtended;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

public class TileShapedLamp extends TileExtended implements IHandlePackets, IFrameSupport, IConnectable
{
    public int Rotation = 0;
    public boolean Powered = false;
    public boolean Inverted = false;
    public int Style = 0;
    public int Color = 0;

    public int getConnectableMask()
    {
        return 16777216 << this.Rotation | 15 << (this.Rotation << 2);
    }

    public int getConnectClass(int var1)
    {
        return 1;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = var2 ^ 1;
        this.onBlockNeighborChange(0);
        this.Inverted = (var1.getItemDamage() & 16) > 0;
        this.Color = var1.getItemDamage() & 15;
        this.Style = (var1.getItemDamage() & 1023) >> 5;
    }

    public int getBlockID()
    {
        return RedPowerLighting.blockShapedLamp.blockID;
    }

    public int getExtendedID()
    {
        return 0;
    }

    public void onBlockNeighborChange(int var1)
    {
        int var2 = this.getConnectableMask();

        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2 & 16777215, var2 >> 24))
        {
            if (this.Powered)
            {
                return;
            }

            this.Powered = true;
            this.updateBlock();
            this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
        }
        else
        {
            if (!this.Powered)
            {
                return;
            }

            this.Powered = false;
            this.updateBlock();
            this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public int getLightValue()
    {
        return this.Powered != this.Inverted ? 15 : 0;
    }

    public void addHarvestContents(ArrayList var1)
    {
        ItemStack var2 = new ItemStack(this.getBlockID(), 1, (this.getExtendedID() << 10) + (this.Style << 5) + (this.Inverted ? 16 : 0) + this.Color);
        var1.add(var2);
    }

    public static AxisAlignedBB getRotatedBB(float var0, float var1, float var2, float var3, float var4, float var5, int var6)
    {
        switch (var6)
        {
            case 0:
                return AxisAlignedBB.getBoundingBox((double)var0, (double)var1, (double)var2, (double)var3, (double)var4, (double)var5);

            case 1:
                return AxisAlignedBB.getBoundingBox((double)var0, (double)(1.0F - var4), (double)var2, (double)var3, (double)(1.0F - var1), (double)var5);

            case 2:
                return AxisAlignedBB.getBoundingBox((double)var0, (double)var2, (double)var1, (double)var3, (double)var5, (double)var4);

            case 3:
                return AxisAlignedBB.getBoundingBox((double)var0, (double)(1.0F - var5), (double)(1.0F - var4), (double)var3, (double)(1.0F - var2), (double)(1.0F - var1));

            case 4:
                return AxisAlignedBB.getBoundingBox((double)var1, (double)var0, (double)var2, (double)var4, (double)var3, (double)var5);

            default:
                return AxisAlignedBB.getBoundingBox((double)(1.0F - var4), (double)(1.0F - var3), (double)var2, (double)(1.0F - var1), (double)(1.0F - var0), (double)var5);
        }
    }

    public AxisAlignedBB getBounds()
    {
        switch (this.Style)
        {
            case 0:
                return getRotatedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.5F, 0.875F, this.Rotation);

            case 1:
                return getRotatedBB(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.75F, 0.8125F, this.Rotation);

            default:
                return getRotatedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.5F, 0.875F, this.Rotation);
        }
    }

    public byte[] getFramePacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 11;
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
        byte var2 = var1.getByte("ps");
        this.Rotation = var1.getByte("rot");
        this.Powered = (var2 & 1) > 0;
        this.Inverted = (var2 & 2) > 0;
        this.Color = var1.getByte("color");
        this.Style = var1.getByte("style");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        int var2 = (this.Powered ? 1 : 0) | (this.Inverted ? 2 : 0);
        var1.setByte("ps", (byte)var2);
        var1.setByte("rot", (byte)this.Rotation);
        var1.setByte("color", (byte)this.Color);
        var1.setByte("style", (byte)this.Style);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Rotation = var1.getByte();
        this.Color = var1.getByte();
        this.Style = var1.getByte();
        int var2 = var1.getByte();
        this.Powered = (var2 & 1) > 0;
        this.Inverted = (var2 & 2) > 0;
        this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.Rotation);
        var1.addByte(this.Color);
        var1.addByte(this.Style);
        int var2 = (this.Powered ? 1 : 0) | (this.Inverted ? 2 : 0);
        var1.addByte(var2);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 7;
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
            if (var1.subId != 7)
            {
                return;
            }

            this.readFromPacket(var1);
        }
        catch (IOException var3)
        {
            ;
        }

        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
}
