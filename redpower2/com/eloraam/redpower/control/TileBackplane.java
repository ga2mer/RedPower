package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerControl;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.TileMultipart;
import com.eloraam.redpower.core.WorldCoord;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class TileBackplane extends TileMultipart implements IHandlePackets, IFrameSupport
{
    public int Rotation = 0;

    public int readBackplane(int var1)
    {
        return 255;
    }

    public void writeBackplane(int var1, int var2) {}

    public int getBlockID()
    {
        return RedPowerControl.blockBackplane.blockID;
    }

    public int getExtendedID()
    {
        return 0;
    }

    public void onBlockNeighborChange(int var1)
    {
        if (!this.worldObj.isBlockSolidOnSide(this.xCoord, this.yCoord - 1, this.zCoord, ForgeDirection.UP))
        {
            this.breakBlock();
        }
        else
        {
            WorldCoord var2 = new WorldCoord(this);
            var2.step(CoreLib.rotToSide(this.Rotation) ^ 1);
            int var3 = this.worldObj.getBlockId(var2.x, var2.y, var2.z);
            int var4 = this.worldObj.getBlockMetadata(var2.x, var2.y, var2.z);

            if (var3 != RedPowerControl.blockBackplane.blockID)
            {
                if (var3 != RedPowerControl.blockPeripheral.blockID || var4 != 1)
                {
                    this.breakBlock();
                }
            }
        }
    }

    public void addHarvestContents(ArrayList var1)
    {
        super.addHarvestContents(var1);
        var1.add(new ItemStack(RedPowerControl.blockBackplane, 1, 0));
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        this.breakBlock();
    }

    public float getPartStrength(EntityPlayer var1, int var2)
    {
        return 0.1F;
    }

    public boolean blockEmpty()
    {
        return false;
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public int getSolidPartsMask()
    {
        return 1;
    }

    public int getPartsMask()
    {
        return 1;
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
        this.Rotation = var1.getByte("rot");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("rot", (byte)this.Rotation);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Rotation = var1.getByte();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.Rotation);
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
