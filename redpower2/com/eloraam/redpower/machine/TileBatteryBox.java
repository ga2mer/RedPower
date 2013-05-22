package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileExtended;
import com.eloraam.redpower.machine.TileBatteryBox$1;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileBatteryBox extends TileExtended implements IHandlePackets, IInventory, IBluePowerConnectable, ISidedInventory, IFrameSupport
{
    BluePowerConductor cond = new TileBatteryBox$1(this);
    protected ItemStack[] contents = new ItemStack[2];
    public int Charge = 0;
    public int Storage = 0;
    public int ConMask = -1;
    public boolean Powered = false;

    public int getConnectableMask()
    {
        return 1073741823;
    }

    public int getConnectClass(int var1)
    {
        return 65;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return this.cond;
    }

    public int getStartInventorySide(ForgeDirection var1)
    {
        int var2 = var1.ordinal();
        return var2 == 0 ? 1 : 0;
    }

    public int getSizeInventorySide(ForgeDirection var1)
    {
        int var2 = var1.ordinal();
        return var2 >= 2 ? 0 : 1;
    }

    public void addHarvestContents(ArrayList var1)
    {
        ItemStack var2 = new ItemStack(this.getBlockID(), 1, this.getExtendedID());

        if (this.Storage > 0)
        {
            var2.setTagCompound(new NBTTagCompound());
            var2.stackTagCompound.setShort("batLevel", (short)this.Storage);
        }

        var1.add(var2);
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        if (var1.stackTagCompound != null)
        {
            this.Storage = var1.stackTagCompound.getShort("batLevel");
        }
    }

    public int getExtendedID()
    {
        return 6;
    }

    public int getBlockID()
    {
        return RedPowerMachine.blockMachine.blockID;
    }

    public int getMaxStorage()
    {
        return 6000;
    }

    public int getStorageForRender()
    {
        return this.Storage * 8 / this.getMaxStorage();
    }

    public int getChargeScaled(int var1)
    {
        return Math.min(var1, var1 * this.Charge / 1000);
    }

    public int getStorageScaled(int var1)
    {
        return Math.min(var1, var1 * this.Storage / this.getMaxStorage());
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }

            this.cond.iterate();
            this.dirtyBlock();
            this.Charge = (int)(this.cond.getVoltage() * 10.0D);
            int var1 = this.getStorageForRender();
            int var2;

            if (this.contents[0] != null && this.Storage > 0)
            {
                if (this.contents[0].getItem() == RedPowerMachine.itemBatteryEmpty)
                {
                    this.contents[0] = new ItemStack(RedPowerMachine.itemBatteryPowered, 1, RedPowerMachine.itemBatteryPowered.getMaxDamage());
                    this.onInventoryChanged();
                }

                if (this.contents[0].getItem() == RedPowerMachine.itemBatteryPowered)
                {
                    var2 = Math.min(this.contents[0].getItemDamage() - 1, this.Storage);
                    var2 = Math.min(var2, 25);
                    this.Storage -= var2;
                    this.contents[0].setItemDamage(this.contents[0].getItemDamage() - var2);
                    this.onInventoryChanged();
                }
            }

            if (this.contents[1] != null && this.contents[1].getItem() == RedPowerMachine.itemBatteryPowered)
            {
                var2 = Math.min(this.contents[1].getMaxDamage() - this.contents[1].getItemDamage(), this.getMaxStorage() - this.Storage);
                var2 = Math.min(var2, 25);
                this.Storage += var2;
                this.contents[1].setItemDamage(this.contents[1].getItemDamage() + var2);

                if (this.contents[1].getItemDamage() == this.contents[1].getMaxDamage())
                {
                    this.contents[1] = new ItemStack(RedPowerMachine.itemBatteryEmpty, 1);
                }

                this.onInventoryChanged();
            }

            if (this.Charge > 900 && this.Storage < this.getMaxStorage())
            {
                var2 = Math.min((this.Charge - 900) / 10, 10);
                var2 = Math.min(var2, this.getMaxStorage() - this.Storage);
                this.cond.drawPower((double)(var2 * 1000));
                this.Storage += var2;
            }
            else if (this.Charge < 800 && this.Storage > 0 && !this.Powered)
            {
                var2 = Math.min((800 - this.Charge) / 10, 10);
                var2 = Math.min(var2, this.Storage);
                this.cond.applyPower((double)(var2 * 1000));
                this.Storage -= var2;
            }

            if (var1 != this.getStorageForRender())
            {
                this.updateBlock();
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 2;
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
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Battery Box";
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
        return 1;
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

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;

        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 16777215, 63))
        {
            if (!this.Powered)
            {
                this.Powered = true;
                this.dirtyBlock();
            }
        }
        else if (this.Powered)
        {
            this.Powered = false;
            this.dirtyBlock();
        }
    }

    public boolean onBlockActivated(EntityPlayer var1)
    {
        if (var1.isSneaking())
        {
            return false;
        }
        else if (CoreLib.isClient(this.worldObj))
        {
            return true;
        }
        else
        {
            var1.openGui(RedPowerMachine.instance, 8, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public void onBlockRemoval()
    {
        super.onBlockRemoval();

        for (int var1 = 0; var1 < 2; ++var1)
        {
            ItemStack var2 = this.contents[var1];

            if (var2 != null && var2.stackSize > 0)
            {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
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
        NBTTagList var2 = var1.getTagList("Items");
        this.contents = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.contents.length)
            {
                this.contents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.cond.readFromNBT(var1);
        this.Charge = var1.getShort("chg");
        this.Storage = var1.getShort("stor");
        byte var6 = var1.getByte("ps");
        this.Powered = (var6 & 1) > 0;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        NBTTagList var2 = new NBTTagList();
        int var3;

        for (var3 = 0; var3 < this.contents.length; ++var3)
        {
            if (this.contents[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.contents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        var1.setTag("Items", var2);
        this.cond.writeToNBT(var1);
        var1.setShort("chg", (short)this.Charge);
        var1.setShort("stor", (short)this.Storage);
        var3 = this.Powered ? 1 : 0;
        var1.setByte("ps", (byte)var3);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Storage = (int)var1.getUVLC();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addUVLC((long)this.Storage);
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

        this.updateBlock();
    }
}
