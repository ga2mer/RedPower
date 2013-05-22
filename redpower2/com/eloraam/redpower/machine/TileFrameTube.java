package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IFrameLink;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.WorldCoord;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileFrameTube extends TileTube implements IFrameLink
{
    public int StickySides = 63;

    public boolean isFrameMoving()
    {
        return false;
    }

    public boolean canFrameConnectIn(int var1)
    {
        return (this.StickySides & 1 << var1) > 0;
    }

    public boolean canFrameConnectOut(int var1)
    {
        return (this.StickySides & 1 << var1) > 0;
    }

    public WorldCoord getFrameLinkset()
    {
        return null;
    }

    public int getExtendedID()
    {
        return 2;
    }

    public int getBlockID()
    {
        return RedPowerMachine.blockFrame.blockID;
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        boolean var3 = false;

        if (var2 == 29)
        {
            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(this.getBlockID(), 1, 2));
            this.flow.onRemove();

            if (this.CoverSides > 0)
            {
                this.replaceWithCovers();
                this.updateBlockChange();
            }
            else
            {
                this.deleteBlock();
            }
        }
        else
        {
            super.onHarvestPart(var1, var2);
        }
    }

    public void addHarvestContents(ArrayList var1)
    {
        this.addCoverableHarvestContents(var1);
        var1.add(new ItemStack(this.getBlockID(), 1, 2));
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        if (var2 == 29)
        {
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            super.setPartBounds(var1, var2);
        }
    }

    public boolean canAddCover(int var1, int var2)
    {
        if (var1 > 5)
        {
            return false;
        }
        else
        {
            int var3 = var2 >> 8;
            return var3 != 0 && var3 != 1 && var3 != 3 && var3 != 4 ? false : (this.CoverSides & 1 << var1) <= 0;
        }
    }

    void rebuildSticky()
    {
        int var1 = 0;

        for (int var2 = 0; var2 < 6; ++var2)
        {
            int var3 = 1 << var2;

            if ((this.CoverSides & var3) == 0)
            {
                var1 |= var3;
            }
            else
            {
                int var4 = this.Covers[var2] >> 8;

                if (var4 == 1 || var4 == 4)
                {
                    var1 |= var3;
                }
            }
        }

        this.StickySides = var1;
    }

    public boolean tryAddCover(int var1, int var2)
    {
        if (!super.tryAddCover(var1, var2))
        {
            return false;
        }
        else
        {
            this.rebuildSticky();
            return true;
        }
    }

    public int tryRemoveCover(int var1)
    {
        int var2 = super.tryRemoveCover(var1);

        if (var2 < 0)
        {
            return var2;
        }
        else
        {
            this.rebuildSticky();
            return var2;
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.rebuildSticky();
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);
        this.rebuildSticky();
    }
}
