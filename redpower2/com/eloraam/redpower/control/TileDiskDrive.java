package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerControl;
import com.eloraam.redpower.base.ItemScrewdriver;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.DiskLib;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.IRedbusConnectable;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.TileExtended;
import com.eloraam.redpower.core.WorldCoord;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;

public class TileDiskDrive extends TileExtended implements IRedbusConnectable, IInventory, IHandlePackets, IFrameSupport
{
    public int Rotation = 0;
    public boolean hasDisk = false;
    public boolean Active = false;
    private ItemStack[] contents = new ItemStack[1];
    private int accessTime = 0;
    private byte[] databuf = new byte[128];
    private int sector = 0;
    private int cmdreg = 0;
    private int rbaddr = 2;

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
        if (var1 < 128)
        {
            return this.databuf[var1] & 255;
        }
        else
        {
            switch (var1)
            {
                case 128:
                    return this.sector & 255;

                case 129:
                    return this.sector >> 8;

                case 130:
                    return this.cmdreg & 255;

                default:
                    return 0;
            }
        }
    }

    public void rbWrite(int var1, int var2)
    {
        this.dirtyBlock();

        if (var1 < 128)
        {
            this.databuf[var1] = (byte)var2;
        }
        else
        {
            switch (var1)
            {
                case 128:
                    this.sector = this.sector & 65280 | var2;
                    break;

                case 129:
                    this.sector = this.sector & 255 | var2 << 8;
                    break;

                case 130:
                    this.cmdreg = var2;
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
        else if (!this.hasDisk)
        {
            return false;
        }
        else if (this.contents[0] == null)
        {
            return false;
        }
        else if (this.Active)
        {
            return false;
        }
        else
        {
            this.ejectDisk();
            return true;
        }
    }

    public int getBlockID()
    {
        return RedPowerControl.blockPeripheral.blockID;
    }

    public int getExtendedID()
    {
        return 2;
    }

    public void onBlockRemoval()
    {
        for (int var1 = 0; var1 < 1; ++var1)
        {
            ItemStack var2 = this.contents[var1];

            if (var2 != null && var2.stackSize > 0)
            {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
            }
        }
    }

    boolean setDisk(ItemStack var1)
    {
        if (this.contents[0] != null)
        {
            return false;
        }
        else
        {
            this.setInventorySlotContents(0, var1);
            return true;
        }
    }

    private NBTTagCompound getDiskTags()
    {
        NBTTagCompound var1 = this.contents[0].stackTagCompound;

        if (var1 == null)
        {
            this.contents[0].setTagCompound(new NBTTagCompound());
            var1 = this.contents[0].stackTagCompound;
        }

        return var1;
    }

    private File startDisk()
    {
        if (this.contents[0].getItemDamage() > 0)
        {
            return null;
        }
        else
        {
            NBTTagCompound var1 = this.getDiskTags();
            File var2 = DiskLib.getSaveDir(this.worldObj);

            if (var1.hasKey("serno"))
            {
                return DiskLib.getDiskFile(var2, var1.getString("serno"));
            }
            else
            {
                String var3 = null;

                while (true)
                {
                    var3 = DiskLib.generateSerialNumber(this.worldObj);
                    File var4 = DiskLib.getDiskFile(var2, var3);

                    try
                    {
                        if (var4.createNewFile())
                        {
                            var1.setString("serno", var3);
                            return var4;
                        }
                    }
                    catch (IOException var6)
                    {
                        var6.printStackTrace();
                        return null;
                    }
                }
            }
        }
    }

    private void runCmd1()
    {
        Arrays.fill(this.databuf, (byte)0);
        String var1 = "";

        if (this.contents[0].getItemDamage() > 0)
        {
            var1 = "System Disk";
        }
        else
        {
            NBTTagCompound var2 = this.contents[0].stackTagCompound;

            if (var2 == null)
            {
                return;
            }

            var1 = var2.getString("label");
        }

        try
        {
            byte[] var4 = var1.getBytes("US-ASCII");
            System.arraycopy(var4, 0, this.databuf, 0, Math.min(var4.length, 128));
        }
        catch (UnsupportedEncodingException var3)
        {
            ;
        }
    }

    private void runCmd2()
    {
        if (this.contents[0].getItemDamage() > 0)
        {
            this.cmdreg = -1;
        }
        else
        {
            NBTTagCompound var1 = this.getDiskTags();
            int var2;

            for (var2 = 0; this.databuf[var2] != 0 && var2 < 64; ++var2)
            {
                ;
            }

            this.cmdreg = 0;

            try
            {
                String var3 = new String(this.databuf, 0, var2, "US-ASCII");
                var1.setString("label", var3);
            }
            catch (UnsupportedEncodingException var4)
            {
                ;
            }
        }
    }

    private void runCmd3()
    {
        Arrays.fill(this.databuf, (byte)0);
        String var1 = "";

        if (this.contents[0].getItemDamage() > 0)
        {
            var1 = String.format("%016d", new Object[] {Integer.valueOf(this.contents[0].getItemDamage())});
        }
        else
        {
            NBTTagCompound var2 = this.getDiskTags();
            this.startDisk();

            if (var2 == null)
            {
                return;
            }

            var1 = var2.getString("serno");
        }

        try
        {
            byte[] var4 = var1.getBytes("US-ASCII");
            System.arraycopy(var4, 0, this.databuf, 0, Math.min(var4.length, 128));
        }
        catch (UnsupportedEncodingException var3)
        {
            ;
        }
    }

    private void runCmd4()
    {
        if (this.sector > 2048)
        {
            this.cmdreg = -1;
        }
        else
        {
            long var1 = (long)(this.sector * 128);
            Object var3 = null;

            if (this.contents[0].getItemDamage() > 0)
            {
                InputStream var4 = null;

                switch (this.contents[0].getItemDamage())
                {
                    case 1:
                        var4 = RedPowerControl.class.getResourceAsStream("/eloraam/control/redforth.img");
                        break;

                    case 2:
                        var4 = RedPowerControl.class.getResourceAsStream("/eloraam/control/redforthxp.img");
                }

                try
                {
                    if (var4.skip(var1) == var1)
                    {
                        if (var4.read(this.databuf) != 128)
                        {
                            this.cmdreg = -1;
                            return;
                        }

                        this.cmdreg = 0;
                        return;
                    }

                    this.cmdreg = -1;
                }
                catch (IOException var37)
                {
                    var37.printStackTrace();
                    this.cmdreg = -1;
                    return;
                }
                finally
                {
                    try
                    {
                        if (var4 != null)
                        {
                            var4.close();
                        }
                    }
                    catch (IOException var34)
                    {
                        ;
                    }
                }
            }
            else
            {
                File var39 = this.startDisk();

                if (var39 == null)
                {
                    this.cmdreg = -1;
                }
                else
                {
                    RandomAccessFile var5 = null;

                    try
                    {
                        var5 = new RandomAccessFile(var39, "r");
                        var5.seek(var1);

                        if (var5.read(this.databuf) == 128)
                        {
                            this.cmdreg = 0;
                            return;
                        }

                        this.cmdreg = -1;
                    }
                    catch (IOException var35)
                    {
                        var35.printStackTrace();
                        this.cmdreg = -1;
                        return;
                    }
                    finally
                    {
                        try
                        {
                            if (var5 != null)
                            {
                                var5.close();
                            }
                        }
                        catch (IOException var33)
                        {
                            ;
                        }
                    }
                }
            }
        }
    }

    private void runCmd5()
    {
        if (this.contents[0].getItemDamage() > 0)
        {
            this.cmdreg = -1;
        }
        else if (this.sector > 2048)
        {
            this.cmdreg = -1;
        }
        else
        {
            long var1 = (long)(this.sector * 128);
            File var3 = this.startDisk();

            if (var3 == null)
            {
                this.cmdreg = -1;
            }
            else
            {
                RandomAccessFile var4 = null;

                try
                {
                    var4 = new RandomAccessFile(var3, "rw");
                    var4.seek(var1);
                    var4.write(this.databuf);
                    var4.close();
                    var4 = null;
                    this.cmdreg = 0;
                }
                catch (IOException var14)
                {
                    this.cmdreg = -1;
                }
                finally
                {
                    try
                    {
                        if (var4 != null)
                        {
                            var4.close();
                        }
                    }
                    catch (IOException var13)
                    {
                        ;
                    }
                }
            }
        }
    }

    private void runDiskCmd()
    {
        this.dirtyBlock();

        if (this.contents[0] == null)
        {
            this.cmdreg = -1;
        }
        else if (!(this.contents[0].getItem() instanceof ItemDisk))
        {
            this.cmdreg = -1;
        }
        else
        {
            switch (this.cmdreg)
            {
                case 1:
                    this.runCmd1();
                    this.cmdreg = 0;
                    break;

                case 2:
                    this.runCmd2();
                    break;

                case 3:
                    this.runCmd3();
                    this.cmdreg = 0;
                    break;

                case 4:
                    this.runCmd4();
                    break;

                case 5:
                    this.runCmd5();
                    break;

                default:
                    this.cmdreg = -1;
            }

            this.accessTime = 5;

            if (!this.Active)
            {
                this.Active = true;
                this.updateBlock();
            }
        }
    }

    private void ejectDisk()
    {
        if (this.contents[0] != null)
        {
            MachineLib.ejectItem(this.worldObj, new WorldCoord(this), this.contents[0], CoreLib.rotToSide(this.Rotation) ^ 1);
            this.contents[0] = null;
            this.hasDisk = false;
            this.updateBlock();
        }
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        super.onInventoryChanged();

        if (this.contents[0] != null && !(this.contents[0].getItem() instanceof ItemDisk))
        {
            this.ejectDisk();
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (this.cmdreg != 0 && this.cmdreg != -1)
        {
            this.runDiskCmd();
        }

        if (this.accessTime > 0 && --this.accessTime == 0)
        {
            this.Active = false;
            this.updateBlock();
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 1;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.contents[var1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.contents[var1] == null)
        {
            return null;
        }
        else
        {
            ItemStack var3;

            if (this.contents[var1].stackSize <= var2)
            {
                var3 = this.contents[var1];
                this.contents[var1] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.contents[var1].splitStack(var2);

                if (this.contents[var1].stackSize == 0)
                {
                    this.contents[var1] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.contents[var1] == null)
        {
            return null;
        }
        else
        {
            ItemStack var2 = this.contents[var1];
            this.contents[var1] = null;
            return var2;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.contents[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
        this.hasDisk = this.contents[var1] != null;
        this.updateBlock();
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Disk Drive";
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void closeChest() {}

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void openChest() {}

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
        this.accessTime = var1.getByte("actime");
        this.sector = var1.getShort("sect") & 65535;
        this.cmdreg = var1.getByte("cmd") & 255;
        this.rbaddr = var1.getByte("rbaddr") & 255;
        byte var2 = var1.getByte("fl");
        this.hasDisk = (var2 & 1) > 0;
        this.Active = (var2 & 2) > 0;
        this.databuf = var1.getByteArray("dbuf");

        if (this.databuf.length != 128)
        {
            this.databuf = new byte[128];
        }

        NBTTagList var3 = var1.getTagList("Items");
        this.contents = new ItemStack[this.getSizeInventory()];

        for (int var4 = 0; var4 < var3.tagCount(); ++var4)
        {
            NBTTagCompound var5 = (NBTTagCompound)var3.tagAt(var4);
            int var6 = var5.getByte("Slot") & 255;

            if (var6 >= 0 && var6 < this.contents.length)
            {
                this.contents[var6] = ItemStack.loadItemStackFromNBT(var5);
            }
        }

        this.hasDisk = this.contents[0] != null;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("rot", (byte)this.Rotation);
        int var2 = (this.hasDisk ? 1 : 0) | (this.Active ? 2 : 0);
        var1.setByte("fl", (byte)var2);
        var1.setByte("actime", (byte)this.accessTime);
        var1.setByteArray("dbuf", this.databuf);
        var1.setShort("sect", (short)this.sector);
        var1.setByte("cmd", (byte)this.cmdreg);
        var1.setByte("rbaddr", (byte)this.rbaddr);
        NBTTagList var3 = new NBTTagList();

        for (int var4 = 0; var4 < this.contents.length; ++var4)
        {
            if (this.contents[var4] != null)
            {
                NBTTagCompound var5 = new NBTTagCompound();
                var5.setByte("Slot", (byte)var4);
                this.contents[var4].writeToNBT(var5);
                var3.appendTag(var5);
            }
        }

        var1.setTag("Items", var3);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Rotation = var1.getByte();
        int var2 = var1.getByte();
        this.hasDisk = (var2 & 1) > 0;
        this.Active = (var2 & 2) > 0;
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.Rotation);
        int var2 = (this.hasDisk ? 1 : 0) | (this.Active ? 2 : 0);
        var1.addByte(var2);
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
