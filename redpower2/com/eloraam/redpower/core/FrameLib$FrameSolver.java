package com.eloraam.redpower.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class FrameLib$FrameSolver
{
    HashSet scanmap;
    LinkedList scanpos;
    HashSet framemap;
    LinkedList frameset;
    LinkedList clearset;
    int movedir;
    WorldCoord motorpos;
    public boolean valid = true;
    World worldObj;

    public FrameLib$FrameSolver(World var1, WorldCoord var2, WorldCoord var3, int var4)
    {
        this.movedir = var4;
        this.motorpos = var3;
        this.worldObj = var1;
        this.scanmap = new HashSet();
        this.scanpos = new LinkedList();
        this.framemap = new HashSet();
        this.frameset = new LinkedList();
        this.clearset = new LinkedList();
        this.scanmap.add(var3);
        this.scanmap.add(var2);
        this.scanpos.addLast(var2);
    }

    boolean step()
    {
        WorldCoord var1 = (WorldCoord)this.scanpos.removeFirst();

        if (var1.y >= 0 && var1.y < this.worldObj.getHeight() - 1)
        {
            int var2 = this.worldObj.getBlockId(var1.x, var1.y, var1.z);

            if (this.movedir >= 0 && !this.worldObj.blockExists(var1.x, var1.y, var1.z))
            {
                this.valid = false;
                return false;
            }
            else if (var2 == 0)
            {
                return false;
            }
            else if (this.movedir >= 0 && Block.blocksList[var2].getBlockHardness(this.worldObj, var1.x, var1.y, var1.z) < 0.0F)
            {
                this.valid = false;
                return false;
            }
            else
            {
                this.framemap.add(var1);
                this.frameset.addLast(var1);
                IFrameLink var3 = (IFrameLink)CoreLib.getTileEntity(this.worldObj, var1, IFrameLink.class);

                if (var3 == null)
                {
                    return true;
                }
                else if (var3.isFrameMoving() && this.movedir >= 0)
                {
                    this.valid = false;
                    return true;
                }
                else
                {
                    for (int var4 = 0; var4 < 6; ++var4)
                    {
                        if (var3.canFrameConnectOut(var4))
                        {
                            WorldCoord var5 = var1.coordStep(var4);

                            if (!this.scanmap.contains(var5))
                            {
                                IFrameLink var6 = (IFrameLink)CoreLib.getTileEntity(this.worldObj, var5, IFrameLink.class);

                                if (var6 != null)
                                {
                                    if (!var6.canFrameConnectIn((var4 ^ 1) & 255))
                                    {
                                        continue;
                                    }

                                    if (this.movedir < 0)
                                    {
                                        WorldCoord var7 = var6.getFrameLinkset();

                                        if (var7 == null || !var7.equals(this.motorpos))
                                        {
                                            continue;
                                        }
                                    }
                                }

                                this.scanmap.add(var5);
                                this.scanpos.addLast(var5);
                            }
                        }
                    }

                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    }

    public boolean solve()
    {
        while (this.valid && this.scanpos.size() > 0)
        {
            this.step();
        }

        return this.valid;
    }

    public boolean solveLimit(int var1)
    {
        while (true)
        {
            if (this.valid && this.scanpos.size() > 0)
            {
                if (this.step())
                {
                    --var1;
                }

                if (var1 != 0)
                {
                    continue;
                }

                return false;
            }

            return this.valid;
        }
    }

    public boolean addMoved()
    {
        LinkedList var1 = (LinkedList)this.frameset.clone();
        Iterator var2 = var1.iterator();

        while (var2.hasNext())
        {
            WorldCoord var3 = (WorldCoord)var2.next();
            WorldCoord var4 = var3.coordStep(this.movedir);
            int var5 = this.worldObj.getBlockId(var4.x, var4.y, var4.z);

            if (!this.worldObj.blockExists(var4.x, var4.y, var4.z))
            {
                this.valid = false;
                return false;
            }

            if (!this.framemap.contains(var4))
            {
                if (var5 != 0)
                {
                    if (!this.worldObj.canPlaceEntityOnSide(Block.stone.blockID, var4.x, var4.y, var4.z, true, 0, (Entity)null))
                    {
                        this.valid = false;
                        return false;
                    }

                    this.clearset.add(var4);
                }

                this.framemap.add(var4);
                this.frameset.addLast(var4);
            }
        }

        return this.valid;
    }

    public void sort(int var1)
    {
        Collections.sort(this.frameset, WorldCoord.getCompareDir(var1));
    }

    public LinkedList getFrameSet()
    {
        return this.frameset;
    }

    public LinkedList getClearSet()
    {
        return this.clearset;
    }
}
