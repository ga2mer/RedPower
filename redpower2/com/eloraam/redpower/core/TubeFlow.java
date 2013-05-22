package com.eloraam.redpower.core;

import com.eloraam.redpower.core.TubeFlow$TubeScheduleContext;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public abstract class TubeFlow
{
    public LinkedList contents = new LinkedList();

    public abstract boolean schedule(TubeItem var1, TubeFlow$TubeScheduleContext var2);

    public boolean handleItem(TubeItem var1, TubeFlow$TubeScheduleContext var2)
    {
        return false;
    }

    public abstract TileEntity getParent();

    public boolean update()
    {
        boolean var1 = false;

        if (this.contents.size() == 0)
        {
            return false;
        }
        else
        {
            TubeFlow$TubeScheduleContext var2 = new TubeFlow$TubeScheduleContext(this.getParent());
            var2.tii = this.contents.iterator();

            while (var2.tii.hasNext())
            {
                TubeItem var3 = (TubeItem)var2.tii.next();
                var3.progress = (short)(var3.progress + var3.power + 16);

                if (var3.progress >= 128)
                {
                    if (var3.power > 0)
                    {
                        --var3.power;
                    }

                    var1 = true;

                    if (!var3.scheduled)
                    {
                        if (!this.schedule(var3, var2))
                        {
                            var2.tii.remove();
                        }
                    }
                    else
                    {
                        var2.tii.remove();

                        if (!CoreLib.isClient(var2.world))
                        {
                            var2.tir.add(var3);
                        }
                    }
                }
            }

            if (CoreLib.isClient(var2.world))
            {
                return var1;
            }
            else
            {
                Iterator var7 = var2.tir.iterator();

                while (var7.hasNext())
                {
                    TubeItem var4 = (TubeItem)var7.next();

                    if (var4.side >= 0 && (var2.cons & 1 << var4.side) != 0)
                    {
                        var2.dest = var2.wc.copy();
                        var2.dest.step(var4.side);
                        ITubeConnectable var5 = (ITubeConnectable)CoreLib.getTileEntity(var2.world, var2.dest, ITubeConnectable.class);

                        if (var5 instanceof ITubeFlow)
                        {
                            ITubeFlow var6 = (ITubeFlow)var5;
                            var6.addTubeItem(var4);
                        }
                        else if ((var5 == null || !var5.tubeItemEnter((var4.side ^ 1) & 63, var4.mode, var4)) && !this.handleItem(var4, var2))
                        {
                            var4.progress = 0;
                            var4.scheduled = false;
                            var4.mode = 2;
                            this.contents.add(var4);
                        }
                    }
                    else if (var2.cons == 0)
                    {
                        MachineLib.ejectItem(var2.world, var2.wc, var4.item, 1);
                    }
                    else
                    {
                        var4.side = (byte)Integer.numberOfTrailingZeros(var2.cons);
                        var4.progress = 128;
                        var4.scheduled = false;
                        this.contents.add(var4);
                        var1 = true;
                    }
                }

                return var1;
            }
        }
    }

    public void add(TubeItem var1)
    {
        var1.progress = 0;
        var1.scheduled = false;
        this.contents.add(var1);
    }

    public void onRemove()
    {
        TileEntity var1 = this.getParent();
        Iterator var2 = this.contents.iterator();

        while (var2.hasNext())
        {
            TubeItem var3 = (TubeItem)var2.next();

            if (var3 != null && var3.item.stackSize > 0)
            {
                CoreLib.dropItem(var1.worldObj, var1.xCoord, var1.yCoord, var1.zCoord, var3.item);
            }
        }
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        NBTTagList var2 = var1.getTagList("Items");

        if (var2.tagCount() > 0)
        {
            this.contents = new LinkedList();

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
                this.contents.add(TubeItem.newFromNBT(var4));
            }
        }
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        NBTTagList var2 = new NBTTagList();

        if (this.contents != null)
        {
            Iterator var3 = this.contents.iterator();

            while (var3.hasNext())
            {
                TubeItem var4 = (TubeItem)var3.next();
                NBTTagCompound var5 = new NBTTagCompound();
                var4.writeToNBT(var5);
                var2.appendTag(var5);
            }
        }

        var1.setTag("Items", var2);
    }
}
