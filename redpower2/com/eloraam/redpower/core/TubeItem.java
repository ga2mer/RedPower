package com.eloraam.redpower.core;

import java.io.IOException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TubeItem
{
    public short progress = 0;
    public byte mode = 1;
    public byte side;
    public byte color = 0;
    public short power = 0;
    public boolean scheduled = false;
    public short priority = 0;
    public ItemStack item;

    public TubeItem() {}

    public TubeItem(int var1, ItemStack var2)
    {
        this.item = var2;
        this.side = (byte)var1;
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        this.item = ItemStack.loadItemStackFromNBT(var1);
        this.side = var1.getByte("side");
        this.progress = var1.getShort("pos");
        this.mode = var1.getByte("mode");
        this.color = var1.getByte("col");
        this.priority = var1.getShort("prio");

        if (this.progress < 0)
        {
            this.scheduled = true;
            this.progress = (short)(-this.progress - 1);
        }

        this.power = (short)(var1.getByte("pow") & 255);
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        this.item.writeToNBT(var1);
        var1.setByte("side", this.side);
        var1.setShort("pos", (short)(this.scheduled ? -this.progress - 1 : this.progress));
        var1.setByte("mode", this.mode);
        var1.setByte("col", this.color);
        var1.setByte("pow", (byte)this.power);
        var1.setShort("prio", this.priority);
    }

    public static TubeItem newFromNBT(NBTTagCompound var0)
    {
        TubeItem var1 = new TubeItem();
        var1.readFromNBT(var0);
        return var1;
    }

    public void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.side = (byte)var1.getByte();
        this.progress = (short)((int)var1.getVLC());

        if (this.progress < 0)
        {
            this.scheduled = true;
            this.progress = (short)(-this.progress - 1);
        }

        this.color = (byte)var1.getByte();
        this.power = (short)((byte)var1.getByte());
        int var2 = var1.getByte();
        int var3 = (int)var1.getUVLC();
        int var4 = (int)var1.getUVLC();
        this.item = new ItemStack(Item.itemsList[var4], var2, var3);
    }

    public void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.side);

        if (this.scheduled)
        {
            int var10000 = -this.progress - 1;
        }
        else
        {
            short var3 = this.progress;
        }

        var1.addVLC(this.scheduled ? (long)(-this.progress - 1) : (long)this.progress);
        var1.addByte(this.color);
        var1.addByte(this.power);
        var1.addByte(this.item.stackSize);
        var1.addUVLC((long)this.item.getItemDamage());
        var1.addUVLC((long)this.item.itemID);
    }

    public static TubeItem newFromPacket(Packet211TileDesc var0) throws IOException
    {
        TubeItem var1 = new TubeItem();
        var1.readFromPacket(var0);
        return var1;
    }
}
