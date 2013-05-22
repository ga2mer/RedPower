package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerControl;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileRAM extends TileBackplane
{
    public byte[] memory = new byte[8192];

    public int readBackplane(int var1)
    {
        return this.memory[var1] & 255;
    }

    public void writeBackplane(int var1, int var2)
    {
        this.memory[var1] = (byte)var2;
    }

    public int getBlockID()
    {
        return RedPowerControl.blockBackplane.blockID;
    }

    public int getExtendedID()
    {
        return 1;
    }

    public void addHarvestContents(ArrayList var1)
    {
        super.addHarvestContents(var1);
        var1.add(new ItemStack(RedPowerControl.blockBackplane, 1, 1));
    }

    public void onHarvestPart(EntityPlayer var1, int var2)
    {
        CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(RedPowerControl.blockBackplane, 1, 1));
        BlockMultipart.removeMultipart(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, RedPowerControl.blockBackplane.blockID);
        TileBackplane var3 = (TileBackplane)CoreLib.getTileEntity(this.worldObj, this.xCoord, this.yCoord, this.zCoord, TileBackplane.class);

        if (var3 != null)
        {
            var3.Rotation = this.Rotation;
        }

        this.updateBlockChange();
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        if (var2 == 0)
        {
            super.setPartBounds(var1, var2);
        }
        else
        {
            var1.setBlockBounds(0.0F, 0.125F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public int getSolidPartsMask()
    {
        return 3;
    }

    public int getPartsMask()
    {
        return 3;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.memory = var1.getByteArray("ram");

        if (this.memory.length != 8192)
        {
            this.memory = new byte[8192];
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByteArray("ram", this.memory);
    }
}
