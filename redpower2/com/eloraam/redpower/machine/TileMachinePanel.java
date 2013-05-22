package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.IRotatable;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.TileMultipart;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;

public class TileMachinePanel extends TileMultipart implements IHandlePackets, IRotatable, IFrameSupport
{
    public int Rotation = 0;
    public boolean Active = false;
    public boolean Powered = false;
    public boolean Delay = false;
    public boolean Charged = false;

    public int getLightValue()
    {
        return 0;
    }

    void updateLight()
    {
        this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
    }

    public int getFacing(EntityLiving var1)
    {
        int var2 = (int)Math.floor((double)(var1.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (Math.abs(var1.posX - (double)this.xCoord) < 2.0D && Math.abs(var1.posZ - (double)this.zCoord) < 2.0D)
        {
            double var3 = var1.posY + 1.82D - (double)var1.yOffset - (double)this.yCoord;

            if (var3 > 2.0D)
            {
                return 0;
            }

            if (var3 < 0.0D)
            {
                return 1;
            }
        }

        switch (var2)
        {
            case 0:
                return 3;

            case 1:
                return 4;

            case 2:
                return 2;

            default:
                return 5;
        }
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = (int)Math.floor((double)(var3.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    }

    public int getBlockID()
    {
        return RedPowerMachine.blockMachinePanel.blockID;
    }

    public void addHarvestContents(ArrayList var1)
    {
        var1.add(new ItemStack(this.getBlockID(), 1, this.getExtendedID()));
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        this.breakBlock();
    }

    public float getPartStrength(EntityPlayer var1, int var2)
    {
        BlockMachinePanel var3 = RedPowerMachine.blockMachinePanel;
        return var1.getCurrentPlayerStrVsBlock(var3, false) / (var3.getHardness() * 30.0F);
    }

    public boolean blockEmpty()
    {
        return false;
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int getSolidPartsMask()
    {
        return 1;
    }

    public int getPartsMask()
    {
        return 1;
    }

    public int getPartMaxRotation(int var1, boolean var2)
    {
        return var2 ? 0 : 3;
    }

    public int getPartRotation(int var1, boolean var2)
    {
        return var2 ? 0 : this.Rotation;
    }

    public void setPartRotation(int var1, boolean var2, int var3)
    {
        if (!var2)
        {
            this.Rotation = var3;
            this.updateBlockChange();
        }
    }

    public byte[] getFramePacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 8;
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
        this.Active = (var2 & 1) > 0;
        this.Powered = (var2 & 2) > 0;
        this.Delay = (var2 & 4) > 0;
        this.Charged = (var2 & 8) > 0;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        int var2 = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        var1.setByte("ps", (byte)var2);
        var1.setByte("rot", (byte)this.Rotation);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Rotation = var1.getByte();
        int var2 = var1.getByte();
        this.Active = (var2 & 1) > 0;
        this.Powered = (var2 & 2) > 0;
        this.Delay = (var2 & 4) > 0;
        this.Charged = (var2 & 8) > 0;
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.Rotation);
        int var2 = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        var1.addByte(var2);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 8;
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
            if (var1.subId != 8)
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
