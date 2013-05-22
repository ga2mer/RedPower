package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerBase;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public class TileMultiblock extends TileEntity implements IHandlePackets
{
    public int relayX;
    public int relayY;
    public int relayZ;
    public int relayNum;

    public boolean canUpdate()
    {
        return false;
    }

    public int getBlockID()
    {
        return RedPowerBase.blockMultiblock.blockID;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.relayX = var1.getInteger("rlx");
        this.relayY = var1.getInteger("rly");
        this.relayZ = var1.getInteger("rlz");
        this.relayNum = var1.getInteger("rln");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("rlx", this.relayX);
        var1.setInteger("rly", this.relayY);
        var1.setInteger("rlz", this.relayZ);
        var1.setInteger("rln", this.relayNum);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.relayX = (int)var1.getVLC();
        this.relayY = (int)var1.getVLC();
        this.relayZ = (int)var1.getVLC();
        this.relayNum = (int)var1.getUVLC();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addVLC((long)this.relayX);
        var1.addVLC((long)this.relayY);
        var1.addVLC((long)this.relayZ);
        var1.addUVLC((long)this.relayNum);
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
    }
}
