package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.ITubeConnectable;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.MachineLib$FilterMap;
import com.eloraam.redpower.core.TubeBuffer;
import com.eloraam.redpower.core.TubeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileItemDetect extends TileMachine implements ITubeConnectable, IInventory, ISidedInventory
{
    TubeBuffer buffer = new TubeBuffer();
    int count = 0;
    public byte mode = 0;
    protected ItemStack[] contents = new ItemStack[9];
    protected MachineLib$FilterMap filterMap = null;

    void regenFilterMap()
    {
        this.filterMap = MachineLib.makeFilterMap(this.contents);
    }

    public int getTubeConnectableSides()
    {
        return 3 << (this.Rotation & -2);
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
            this.updateBlock();
            this.scheduleTick(5);
            return true;
        }
        else if (var1 == (this.Rotation ^ 1) && var2 == 1)
        {
            if (!this.buffer.isEmpty())
            {
                return false;
            }
            else
            {
                this.buffer.add(var3);

                if (this.filterMap == null)
                {
                    this.regenFilterMap();
                }

                if (this.filterMap.size() == 0 || this.filterMap.containsKey(var3.item))
                {
                    if (this.mode == 0)
                    {
                        this.count += var3.item.stackSize;
                    }
                    else if (this.mode == 1)
                    {
                        ++this.count;
                    }
                }

                this.Active = true;
                this.updateBlock();
                this.scheduleTick(5);
                this.drainBuffer();
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean tubeItemCanEnter(int var1, int var2, TubeItem var3)
    {
        return var1 == this.Rotation && var2 == 2 ? true : (var1 == (this.Rotation ^ 1) && var2 == 1 ? this.buffer.isEmpty() : false);
    }

    public int tubeWeight(int var1, int var2)
    {
        return var1 == this.Rotation && var2 == 2 ? this.buffer.size() : 0;
    }

    public int getStartInventorySide(ForgeDirection var1)
    {
        return 0;
    }

    public int getSizeInventorySide(ForgeDirection var1)
    {
        int var2 = var1.ordinal();
        return var2 != this.Rotation && var2 != (this.Rotation ^ 1) ? 9 : 0;
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

                    if (this.mode == 2 && !this.Powered)
                    {
                        this.Delay = false;
                        this.Powered = true;
                        this.count = 0;
                        this.updateBlockChange();
                    }

                    return;
                }

                this.buffer.pop();

                if (!this.buffer.plugged)
                {
                    continue;
                }

                if (this.mode == 2 && !this.Powered)
                {
                    this.Delay = false;
                    this.Powered = true;
                    this.count = 0;
                    this.updateBlockChange();
                }

                return;
            }

            if (this.mode == 2 && this.Powered)
            {
                this.Powered = false;
                this.updateBlockChange();
            }

            return;
        }
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
            if (this.mode != 2)
            {
                if (this.Powered)
                {
                    if (this.Delay)
                    {
                        this.Delay = false;
                        this.dirtyBlock();
                    }
                    else
                    {
                        this.Powered = false;

                        if (this.count > 0)
                        {
                            this.Delay = true;
                        }

                        this.updateBlockChange();
                    }
                }
                else if (this.count != 0)
                {
                    if (this.Delay)
                    {
                        this.Delay = false;
                        this.dirtyBlock();
                    }
                    else
                    {
                        --this.count;
                        this.Powered = true;
                        this.Delay = true;
                        this.updateBlockChange();
                    }
                }
            }
        }
    }

    public boolean isPoweringTo(int var1)
    {
        return var1 == (this.Rotation ^ 1) ? false : this.Powered;
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
            var1.openGui(RedPowerMachine.instance, 6, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public int getExtendedID()
    {
        return 4;
    }

    public void onBlockRemoval()
    {
        this.buffer.onRemove(this);

        for (int var1 = 0; var1 < 9; ++var1)
        {
            ItemStack var2 = this.contents[var1];

            if (var2 != null && var2.stackSize > 0)
            {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
            }
        }
    }

    public void onTileTick()
    {
        if (!CoreLib.isClient(this.worldObj))
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
                this.Active = false;
                this.updateBlock();
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 9;
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
        return "Item Detector";
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

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        this.filterMap = null;
        super.onInventoryChanged();
    }

    public void closeChest() {}

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void openChest() {}

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

        this.buffer.readFromNBT(var1);
        this.count = var1.getInteger("cnt");
        this.mode = var1.getByte("mode");
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
        this.buffer.writeToNBT(var1);
        var1.setInteger("cnt", this.count);
        var1.setByte("mode", this.mode);
    }
}
