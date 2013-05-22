package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.IConnectable;
import com.eloraam.redpower.core.IFrameLink;
import com.eloraam.redpower.core.ITubeConnectable;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TubeBuffer;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.WorldCoord;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class TileBreaker extends TileMachine implements ITubeConnectable, IFrameLink, IConnectable
{
    TubeBuffer buffer = new TubeBuffer();

    public boolean isFrameMoving()
    {
        return false;
    }

    public boolean canFrameConnectIn(int var1)
    {
        return var1 != (this.Rotation ^ 1);
    }

    public boolean canFrameConnectOut(int var1)
    {
        return false;
    }

    public WorldCoord getFrameLinkset()
    {
        return null;
    }

    public int getConnectableMask()
    {
        return 1073741823 ^ RedPowerLib.getConDirMask(this.Rotation ^ 1);
    }

    public int getConnectClass(int var1)
    {
        return 0;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public int getTubeConnectableSides()
    {
        return 1 << this.Rotation;
    }

    public int getTubeConClass()
    {
        return 0;
    }

    public boolean canRouteItems()
    {
        return false;
    }

    public boolean tubeItemEnter(int var1, int var2, TubeItem var3)
    {
        if (var1 == this.Rotation && var2 == 2)
        {
            this.buffer.addBounce(var3);
            this.Active = true;
            this.scheduleTick(5);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean tubeItemCanEnter(int var1, int var2, TubeItem var3)
    {
        return var1 == this.Rotation && var2 == 2;
    }

    public int tubeWeight(int var1, int var2)
    {
        return var1 == this.Rotation && var2 == 2 ? this.buffer.size() : 0;
    }

    public void onBlockNeighborChange(int var1)
    {
        int var2 = this.getConnectableMask();

        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2, var2 >> 24))
        {
            if (!this.Powered)
            {
                this.Powered = true;
                this.dirtyBlock();

                if (!this.Active)
                {
                    WorldCoord var3 = new WorldCoord(this.xCoord, this.yCoord, this.zCoord);
                    var3.step(this.Rotation ^ 1);
                    int var4 = this.worldObj.getBlockId(var3.x, var3.y, var3.z);

                    if (var4 != 0 && var4 != Block.bedrock.blockID)
                    {
                        Block var5 = Block.blocksList[var4];

                        if (var5.getBlockHardness(this.worldObj, var3.x, var3.y, var3.z) >= 0.0F)
                        {
                            this.Active = true;
                            this.updateBlock();
                            int var6 = this.worldObj.getBlockMetadata(var3.x, var3.y, var3.z);
                            this.buffer.addAll(var5.getBlockDropped(this.worldObj, var3.x, var3.y, var3.z, var6, 0));
                            this.worldObj.setBlock(var3.x, var3.y, var3.z, 0);
                            this.drainBuffer();

                            if (!this.buffer.isEmpty())
                            {
                                this.scheduleTick(5);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            if (this.Active && !this.isTickScheduled())
            {
                this.scheduleTick(5);
            }

            if (this.Powered)
            {
                this.Powered = false;
            }
        }
    }

    public void drainBuffer()
    {
        while (true)
        {
            if (!this.buffer.isEmpty())
            {
                TubeItem var1 = this.buffer.getLast();

                if (!this.handleItem(var1))
                {
                    this.buffer.plugged = true;
                    return;
                }

                this.buffer.pop();

                if (!this.buffer.plugged)
                {
                    continue;
                }

                return;
            }

            return;
        }
    }

    public void onBlockRemoval()
    {
        this.buffer.onRemove(this);
    }

    public void onTileTick()
    {
        if (!this.buffer.isEmpty())
        {
            this.drainBuffer();

            if (!this.buffer.isEmpty())
            {
                this.scheduleTick(10);
            }
            else
            {
                this.scheduleTick(5);
            }
        }
        else
        {
            if (!this.Powered)
            {
                this.Active = false;
                this.updateBlock();
            }
        }
    }

    public int getExtendedID()
    {
        return 1;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.buffer.readFromNBT(var1);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.buffer.writeToNBT(var1);
    }
}
