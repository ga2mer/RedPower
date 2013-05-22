package com.eloraam.redpower.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TubeBuffer
{
    LinkedList buffer = null;
    public boolean plugged = false;

    public boolean isEmpty()
    {
        return this.buffer == null ? true : this.buffer.size() == 0;
    }

    public TubeItem getLast()
    {
        return this.buffer == null ? null : (TubeItem)this.buffer.getLast();
    }

    public void add(TubeItem var1)
    {
        if (this.buffer == null)
        {
            this.buffer = new LinkedList();
        }

        this.buffer.addFirst(var1);
    }

    public void addNew(ItemStack var1)
    {
        if (this.buffer == null)
        {
            this.buffer = new LinkedList();
        }

        this.buffer.addFirst(new TubeItem(0, var1));
    }

    public void addNewColor(ItemStack var1, int var2)
    {
        if (this.buffer == null)
        {
            this.buffer = new LinkedList();
        }

        TubeItem var3 = new TubeItem(0, var1);
        var3.color = (byte)var2;
        this.buffer.addFirst(var3);
    }

    public void addAll(Collection var1)
    {
        if (this.buffer == null)
        {
            this.buffer = new LinkedList();
        }

        Iterator var2 = var1.iterator();

        while (var2.hasNext())
        {
            ItemStack var3 = (ItemStack)var2.next();
            this.buffer.add(new TubeItem(0, var3));
        }
    }

    public void addBounce(TubeItem var1)
    {
        if (this.buffer == null)
        {
            this.buffer = new LinkedList();
        }

        this.buffer.addLast(var1);
        this.plugged = true;
    }

    public void pop()
    {
        this.buffer.removeLast();

        if (this.buffer.size() == 0)
        {
            this.plugged = false;
        }
    }

    public int size()
    {
        return this.buffer == null ? 0 : this.buffer.size();
    }

    public void onRemove(TileEntity var1)
    {
        if (this.buffer != null)
        {
            Iterator var2 = this.buffer.iterator();

            while (var2.hasNext())
            {
                TubeItem var3 = (TubeItem)var2.next();

                if (var3 != null && var3.item.stackSize > 0)
                {
                    CoreLib.dropItem(var1.worldObj, var1.xCoord, var1.yCoord, var1.zCoord, var3.item);
                }
            }
        }
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        NBTTagList var2 = var1.getTagList("Buffer");

        if (var2.tagCount() > 0)
        {
            this.buffer = new LinkedList();

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
                this.buffer.add(TubeItem.newFromNBT(var4));
            }
        }

        byte var5 = var1.getByte("Plug");
        this.plugged = var5 > 0;
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        NBTTagList var2 = new NBTTagList();

        if (this.buffer != null)
        {
            Iterator var3 = this.buffer.iterator();

            while (var3.hasNext())
            {
                TubeItem var4 = (TubeItem)var3.next();
                NBTTagCompound var5 = new NBTTagCompound();
                var4.writeToNBT(var5);
                var2.appendTag(var5);
            }
        }

        var1.setTag("Buffer", var2);
        var1.setByte("Plug", (byte)(this.plugged ? 1 : 0));
    }
}
