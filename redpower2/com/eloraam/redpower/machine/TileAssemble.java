package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRedPowerWiring;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileAssemble extends TileDeployBase implements IInventory, ISidedInventory, IRedPowerWiring
{
    private ItemStack[] contents = new ItemStack[34];
    public byte select = 0;
    public byte mode = 0;
    public int skipSlots = 65534;
    public int ConMask = -1;
    public int PowerState = 0;

    public int getExtendedID()
    {
        return 13;
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
            var1.openGui(RedPowerMachine.instance, 11, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public void onBlockRemoval()
    {
        for (int var1 = 0; var1 < 34; ++var1)
        {
            ItemStack var2 = this.contents[var1];

            if (var2 != null && var2.stackSize > 0)
            {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
            }
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;

        if (this.mode == 0)
        {
            super.onBlockNeighborChange(var1);
        }

        RedPowerLib.updateCurrent(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    public int getConnectionMask()
    {
        if (this.ConMask >= 0)
        {
            return this.ConMask;
        }
        else
        {
            this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
            return this.ConMask;
        }
    }

    public int getExtConnectionMask()
    {
        return 0;
    }

    public int getPoweringMask(int var1)
    {
        return 0;
    }

    public int scanPoweringStrength(int var1, int var2)
    {
        return 0;
    }

    public int getCurrentStrength(int var1, int var2)
    {
        return -1;
    }

    public void updateCurrentStrength()
    {
        if (this.mode == 1)
        {
            int var1;

            for (var1 = 0; var1 < 16; ++var1)
            {
                short var2 = (short)RedPowerLib.getMaxCurrentStrength(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 1073741823, 0, var1 + 1);

                if (var2 > 0)
                {
                    this.PowerState |= 1 << var1;
                }
                else
                {
                    this.PowerState &= ~(1 << var1);
                }
            }

            CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

            if (this.PowerState == 0)
            {
                if (this.Active)
                {
                    this.scheduleTick(5);
                }
            }
            else if (!this.Active)
            {
                this.Active = true;
                this.updateBlock();
                var1 = Integer.numberOfTrailingZeros(this.PowerState);

                if (this.contents[var1] != null)
                {
                    WorldCoord var4 = new WorldCoord(this);
                    var4.step(this.Rotation ^ 1);
                    int var3 = this.getMatchingStack(var1);

                    if (var3 >= 0)
                    {
                        this.enableTowardsActive(var4, var3);
                    }
                }
            }
        }
    }

    public int getConnectClass(int var1)
    {
        return this.mode == 0 ? 0 : 18;
    }

    protected void packInv(ItemStack[] var1, int var2)
    {
        int var3;

        for (var3 = 0; var3 < 36; ++var3)
        {
            var1[var3] = fakePlayer.inventory.getStackInSlot(var3);
            fakePlayer.inventory.setInventorySlotContents(var3, (ItemStack)null);
        }

        for (var3 = 0; var3 < 18; ++var3)
        {
            if (var2 == var3)
            {
                fakePlayer.inventory.setInventorySlotContents(0, this.contents[16 + var3]);
            }
            else
            {
                fakePlayer.inventory.setInventorySlotContents(var3 + 9, this.contents[16 + var3]);
            }
        }
    }

    protected void unpackInv(ItemStack[] var1, int var2)
    {
        int var3;

        for (var3 = 0; var3 < 18; ++var3)
        {
            if (var2 == var3)
            {
                this.contents[16 + var3] = fakePlayer.inventory.getStackInSlot(0);
            }
            else
            {
                this.contents[16 + var3] = fakePlayer.inventory.getStackInSlot(var3 + 9);
            }
        }

        for (var3 = 0; var3 < 36; ++var3)
        {
            fakePlayer.inventory.setInventorySlotContents(var3, var1[var3]);
        }
    }

    protected int getMatchingStack(int var1)
    {
        for (int var2 = 0; var2 < 18; ++var2)
        {
            ItemStack var10000 = this.contents[16 + var2];

            if (this.contents[16 + var2] != null && CoreLib.compareItemStack(this.contents[16 + var2], this.contents[var1]) == 0)
            {
                return var2;
            }
        }

        return -1;
    }

    public void enableTowards(WorldCoord var1)
    {
        int var2;

        if (this.contents[this.select] != null)
        {
            var2 = this.getMatchingStack(this.select);

            if (var2 >= 0)
            {
                this.enableTowardsActive(var1, var2);
            }
        }

        for (var2 = 0; var2 < 16; ++var2)
        {
            this.select = (byte)(this.select + 1 & 15);

            if ((this.skipSlots & 1 << this.select) == 0 || this.select == 0)
            {
                break;
            }
        }
    }

    protected void enableTowardsActive(WorldCoord var1, int var2)
    {
        ItemStack[] var3 = new ItemStack[36];
        this.initPlayer();
        this.packInv(var3, var2);
        ItemStack var4 = this.contents[16 + var2];

        if (var4 != null && var4.stackSize > 0 && this.tryUseItemStack(var4, var1.x, var1.y, var1.z, 0))
        {
            if (fakePlayer.isUsingItem())
            {
                fakePlayer.stopUsingItem();
            }

            this.unpackInv(var3, var2);

            if (this.contents[16 + var2].stackSize == 0)
            {
                this.contents[16 + var2] = null;
            }

            this.onInventoryChanged();
        }
        else
        {
            this.unpackInv(var3, var2);
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 34;
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

        if (var2 != null && var1 < 16)
        {
            this.skipSlots &= ~(1 << var1);
        }

        this.onInventoryChanged();
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Assembler";
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

    public int getStartInventorySide(ForgeDirection var1)
    {
        int var2 = var1.ordinal();
        return (var2 ^ 1) == this.Rotation ? 0 : 16;
    }

    public int getSizeInventorySide(ForgeDirection var1)
    {
        int var2 = var1.ordinal();
        return (var2 ^ 1) == this.Rotation ? 0 : 18;
    }

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

        this.mode = var1.getByte("mode");
        this.select = var1.getByte("sel");
        this.skipSlots = var1.getShort("ssl") & 65535;
        this.PowerState = var1.getInteger("psex");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.contents.length; ++var3)
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
        var1.setByte("mode", this.mode);
        var1.setByte("sel", this.select);
        var1.setShort("ssl", (short)this.skipSlots);
        var1.setInteger("psex", this.PowerState);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);
        this.mode = (byte)var1.getByte();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        var1.addByte(this.mode);
    }
}
