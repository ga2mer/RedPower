package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerControl;
import com.eloraam.redpower.base.ItemScrewdriver;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.IRedPowerConnectable;
import com.eloraam.redpower.core.IRedbusConnectable;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileMultipart;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;

public class TileIOExpander extends TileMultipart implements IRedbusConnectable, IRedPowerConnectable, IHandlePackets, IFrameSupport
{
    public int Rotation = 0;
    public int WBuf = 0;
    public int WBufNew = 0;
    public int RBuf = 0;
    private int rbaddr = 3;

    public int rbGetAddr()
    {
        return this.rbaddr;
    }

    public void rbSetAddr(int var1)
    {
        this.rbaddr = var1;
    }

    public int rbRead(int var1)
    {
        switch (var1)
        {
            case 0:
                return this.RBuf & 255;

            case 1:
                return this.RBuf >> 8;

            case 2:
                return this.WBufNew & 255;

            case 3:
                return this.WBufNew >> 8;

            default:
                return 0;
        }
    }

    public void rbWrite(int var1, int var2)
    {
        this.dirtyBlock();

        switch (var1)
        {
            case 0:
            case 2:
                this.WBufNew = this.WBufNew & 65280 | var2;
                this.scheduleTick(2);
                break;

            case 1:
            case 3:
                this.WBufNew = this.WBufNew & 255 | var2 << 8;
                this.scheduleTick(2);
        }
    }

    public int getConnectableMask()
    {
        return 15;
    }

    public int getConnectClass(int var1)
    {
        return var1 == CoreLib.rotToSide(this.Rotation) ? 18 : 66;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public int getPoweringMask(int var1)
    {
        return var1 == 0 ? 0 : ((this.WBuf & 1 << var1 - 1) > 0 ? RedPowerLib.mapRotToCon(8, this.Rotation) : 0);
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = (int)Math.floor((double)(var3.rotationYaw * 4.0F / 360.0F) + 0.5D) + 1 & 3;
    }

    public boolean onPartActivateSide(EntityPlayer var1, int var2, int var3)
    {
        if (var1.isSneaking())
        {
            if (CoreLib.isClient(this.worldObj))
            {
                return false;
            }
            else
            {
                ItemStack var4 = var1.inventory.getCurrentItem();

                if (var4 == null)
                {
                    return false;
                }
                else if (!(var4.getItem() instanceof ItemScrewdriver))
                {
                    return false;
                }
                else
                {
                    var1.openGui(RedPowerBase.instance, 3, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

    public void onTileTick()
    {
        if (this.WBuf != this.WBufNew)
        {
            this.WBuf = this.WBufNew;
            this.onBlockNeighborChange(0);
            this.updateBlockChange();
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        boolean var2 = false;

        for (int var3 = 0; var3 < 16; ++var3)
        {
            int var4 = RedPowerLib.getRotPowerState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 8, this.Rotation, var3 + 1);

            if (var4 == 0)
            {
                if ((this.RBuf & 1 << var3) > 0)
                {
                    this.RBuf &= ~(1 << var3);
                    var2 = true;
                }
            }
            else if ((this.RBuf & 1 << var3) == 0)
            {
                this.RBuf |= 1 << var3;
                var2 = true;
            }
        }

        if (var2)
        {
            this.updateBlock();
        }
    }

    public int getBlockID()
    {
        return RedPowerControl.blockFlatPeripheral.blockID;
    }

    public int getExtendedID()
    {
        return 0;
    }

    public void addHarvestContents(ArrayList var1)
    {
        super.addHarvestContents(var1);
        var1.add(new ItemStack(this.getBlockID(), 1, 0));
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
        var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
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
        this.WBuf = var1.getShort("wb") & 65535;
        this.WBufNew = var1.getShort("wbn") & 65535;
        this.RBuf = var1.getShort("rb") & 65535;
        this.rbaddr = var1.getByte("rbaddr") & 255;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("rot", (byte)this.Rotation);
        var1.setShort("wb", (short)this.WBuf);
        var1.setShort("wbn", (short)this.WBufNew);
        var1.setShort("rb", (short)this.RBuf);
        var1.setByte("rbaddr", (byte)this.rbaddr);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Rotation = var1.getByte();
        this.WBuf = (int)var1.getUVLC();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.Rotation);
        var1.addUVLC((long)this.WBuf);
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
