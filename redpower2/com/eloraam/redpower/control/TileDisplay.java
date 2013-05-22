package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerControl;
import com.eloraam.redpower.base.ItemScrewdriver;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.IRedbusConnectable;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.TileExtended;
import java.io.IOException;
import java.util.Arrays;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;

public class TileDisplay extends TileExtended implements IRedbusConnectable, IHandlePackets, IFrameSupport
{
    public byte[] screen = new byte[4000];
    public int Rotation = 0;
    public int memRow = 0;
    public int cursX = 0;
    public int cursY = 0;
    public int cursMode = 2;
    public int kbstart = 0;
    public int kbpos = 0;
    public int blitXS = 0;
    public int blitYS = 0;
    public int blitXD = 0;
    public int blitYD = 0;
    public int blitW = 0;
    public int blitH = 0;
    public int blitMode = 0;
    public byte[] kbbuf = new byte[16];
    int rbaddr = 1;

    public TileDisplay()
    {
        Arrays.fill(this.screen, (byte)32);
    }

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
        if (var1 >= 16 && var1 < 96)
        {
            return this.screen[this.memRow * 80 + var1 - 16];
        }
        else
        {
            switch (var1)
            {
                case 0:
                    return this.memRow;

                case 1:
                    return this.cursX;

                case 2:
                    return this.cursY;

                case 3:
                    return this.cursMode;

                case 4:
                    return this.kbstart;

                case 5:
                    return this.kbpos;

                case 6:
                    return this.kbbuf[this.kbstart] & 255;

                case 7:
                    return this.blitMode;

                case 8:
                    return this.blitXS;

                case 9:
                    return this.blitYS;

                case 10:
                    return this.blitXD;

                case 11:
                    return this.blitYD;

                case 12:
                    return this.blitW;

                case 13:
                    return this.blitH;

                default:
                    return 0;
            }
        }
    }

    public void rbWrite(int var1, int var2)
    {
        this.dirtyBlock();

        if (var1 >= 16 && var1 < 96)
        {
            this.screen[this.memRow * 80 + var1 - 16] = (byte)var2;
        }
        else
        {
            switch (var1)
            {
                case 0:
                    this.memRow = var2;

                    if (this.memRow > 49)
                    {
                        this.memRow = 49;
                    }

                    return;

                case 1:
                    this.cursX = var2;
                    return;

                case 2:
                    this.cursY = var2;
                    return;

                case 3:
                    this.cursMode = var2;
                    return;

                case 4:
                    this.kbstart = var2 & 15;
                    return;

                case 5:
                    this.kbpos = var2 & 15;
                    return;

                case 6:
                    this.kbbuf[this.kbstart] = (byte)var2;
                    return;

                case 7:
                    this.blitMode = var2;
                    return;

                case 8:
                    this.blitXS = var2;
                    return;

                case 9:
                    this.blitYS = var2;
                    return;

                case 10:
                    this.blitXD = var2;
                    return;

                case 11:
                    this.blitYD = var2;
                    return;

                case 12:
                    this.blitW = var2;
                    return;

                case 13:
                    this.blitH = var2;
                    return;

                default:
            }
        }
    }

    public int getConnectableMask()
    {
        return 16777215;
    }

    public int getConnectClass(int var1)
    {
        return 66;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = (int)Math.floor((double)(var3.rotationYaw * 4.0F / 360.0F) + 0.5D) + 1 & 3;
    }

    public boolean onBlockActivated(EntityPlayer var1)
    {
        if (var1.isSneaking())
        {
            if (CoreLib.isClient(this.worldObj))
            {
                return false;
            }
            else
            {
                ItemStack var2 = var1.inventory.getCurrentItem();

                if (var2 == null)
                {
                    return false;
                }
                else if (!(var2.getItem() instanceof ItemScrewdriver))
                {
                    return false;
                }
                else
                {
                    var1.openGui(RedPowerBase.instance, 3, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                    return true;
                }
            }
        }
        else if (CoreLib.isClient(this.worldObj))
        {
            return true;
        }
        else
        {
            var1.openGui(RedPowerControl.instance, 1, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public int getBlockID()
    {
        return RedPowerControl.blockPeripheral.blockID;
    }

    public int getExtendedID()
    {
        return 0;
    }

    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void pushKey(byte var1)
    {
        int var2 = this.kbpos + 1 & 15;

        if (var2 != this.kbstart)
        {
            this.kbbuf[this.kbpos] = var1;
            this.kbpos = var2;
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        this.runblitter();
    }

    private void runblitter()
    {
        if (this.blitMode != 0)
        {
            this.dirtyBlock();
            int var1 = this.blitW;
            int var2 = this.blitH;
            var1 = Math.min(var1, 80 - this.blitXD);
            var2 = Math.min(var2, 50 - this.blitYD);

            if (var1 >= 0 && var2 >= 0)
            {
                int var3 = this.blitYD * 80 + this.blitXD;
                int var4;
                int var5;

                switch (this.blitMode)
                {
                    case 1:
                        for (var4 = 0; var4 < var2; ++var4)
                        {
                            for (var5 = 0; var5 < var1; ++var5)
                            {
                                this.screen[var3 + 80 * var4 + var5] = (byte)this.blitXS;
                            }
                        }

                        this.blitMode = 0;
                        return;

                    case 2:
                        for (var4 = 0; var4 < var2; ++var4)
                        {
                            for (var5 = 0; var5 < var1; ++var5)
                            {
                                this.screen[var3 + 80 * var4 + var5] = (byte)(this.screen[var3 + 80 * var4 + var5] ^ 128);
                            }
                        }

                        this.blitMode = 0;
                        return;

                    default:
                        var1 = Math.min(var1, 80 - this.blitXS);
                        var2 = Math.min(var2, 50 - this.blitYS);

                        if (var1 >= 0 && var2 >= 0)
                        {
                            var4 = this.blitYS * 80 + this.blitXS;

                            switch (this.blitMode)
                            {
                                case 3:
                                    for (var5 = 0; var5 < var2; ++var5)
                                    {
                                        for (int var6 = 0; var6 < var1; ++var6)
                                        {
                                            this.screen[var3 + 80 * var5 + var6] = this.screen[var4 + 80 * var5 + var6];
                                        }
                                    }

                                    this.blitMode = 0;
                                    return;

                                default:
                            }
                        }
                        else
                        {
                            this.blitMode = 0;
                        }
                }
            }
            else
            {
                this.blitMode = 0;
            }
        }
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
        this.screen = var1.getByteArray("fb");

        if (this.screen.length != 4000)
        {
            this.screen = new byte[4000];
        }

        this.memRow = var1.getByte("row") & 255;
        this.cursX = var1.getByte("cx") & 255;
        this.cursY = var1.getByte("cy") & 255;
        this.cursMode = var1.getByte("cm") & 255;
        this.kbstart = var1.getByte("kbs");
        this.kbpos = var1.getByte("kbp");
        this.kbbuf = var1.getByteArray("kbb");

        if (this.kbbuf.length != 16)
        {
            this.kbbuf = new byte[16];
        }

        this.blitXS = var1.getByte("blxs") & 255;
        this.blitYS = var1.getByte("blys") & 255;
        this.blitXD = var1.getByte("blxd") & 255;
        this.blitYD = var1.getByte("blyd") & 255;
        this.blitW = var1.getByte("blw") & 255;
        this.blitH = var1.getByte("blh") & 255;
        this.blitMode = var1.getByte("blmd");
        this.rbaddr = var1.getByte("rbaddr") & 255;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("rot", (byte)this.Rotation);
        var1.setByteArray("fb", this.screen);
        var1.setByte("row", (byte)this.memRow);
        var1.setByte("cx", (byte)this.cursX);
        var1.setByte("cy", (byte)this.cursY);
        var1.setByte("cm", (byte)this.cursMode);
        var1.setByte("kbs", (byte)this.kbstart);
        var1.setByte("kbp", (byte)this.kbpos);
        var1.setByteArray("kbb", this.kbbuf);
        var1.setByte("blxs", (byte)this.blitXS);
        var1.setByte("blys", (byte)this.blitYS);
        var1.setByte("blxd", (byte)this.blitXD);
        var1.setByte("blyd", (byte)this.blitYD);
        var1.setByte("blw", (byte)this.blitW);
        var1.setByte("blh", (byte)this.blitH);
        var1.setByte("blmd", (byte)this.blitMode);
        var1.setByte("rbaddr", (byte)this.rbaddr);
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
