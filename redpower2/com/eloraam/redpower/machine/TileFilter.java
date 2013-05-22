package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.MachineLib$FilterMap;
import com.eloraam.redpower.core.TubeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileFilter extends TileTranspose implements IInventory, ISidedInventory
{
    protected ItemStack[] contents = new ItemStack[9];
    protected MachineLib$FilterMap filterMap = null;
    public byte color = 0;

    void regenFilterMap()
    {
        this.filterMap = MachineLib.makeFilterMap(this.contents);
    }

    public boolean tubeItemEnter(int var1, int var2, TubeItem var3)
    {
        if (var1 == (this.Rotation ^ 1) && var2 == 1)
        {
            if (this.filterMap == null)
            {
                this.regenFilterMap();
            }

            return this.filterMap.size() == 0 ? super.tubeItemEnter(var1, var2, var3) : (!this.filterMap.containsKey(var3.item) ? false : super.tubeItemEnter(var1, var2, var3));
        }
        else
        {
            return super.tubeItemEnter(var1, var2, var3);
        }
    }

    public boolean tubeItemCanEnter(int var1, int var2, TubeItem var3)
    {
        if (var1 == (this.Rotation ^ 1) && var2 == 1)
        {
            if (this.filterMap == null)
            {
                this.regenFilterMap();
            }

            return this.filterMap.size() == 0 ? super.tubeItemCanEnter(var1, var2, var3) : (!this.filterMap.containsKey(var3.item) ? false : super.tubeItemCanEnter(var1, var2, var3));
        }
        else
        {
            return super.tubeItemCanEnter(var1, var2, var3);
        }
    }

    protected void addToBuffer(ItemStack var1)
    {
        this.buffer.addNewColor(var1, this.color);
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
            var1.openGui(RedPowerMachine.instance, 2, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public int getExtendedID()
    {
        return 3;
    }

    public void onBlockRemoval()
    {
        super.onBlockRemoval();

        for (int var1 = 0; var1 < 9; ++var1)
        {
            ItemStack var2 = this.contents[var1];

            if (var2 != null && var2.stackSize > 0)
            {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
            }
        }
    }

    protected boolean handleExtract(IInventory var1, int var2, int var3)
    {
        if (this.filterMap == null)
        {
            this.regenFilterMap();
        }

        if (this.filterMap.size() == 0)
        {
            ItemStack var6 = MachineLib.collectOneStack(var1, var2, var3, (ItemStack)null);

            if (var6 == null)
            {
                return false;
            }
            else
            {
                this.buffer.addNewColor(var6, this.color);
                this.drainBuffer();
                return true;
            }
        }
        else
        {
            int var4 = MachineLib.matchAnyStack(this.filterMap, var1, var2, var3);

            if (var4 < 0)
            {
                return false;
            }
            else
            {
                ItemStack var5 = MachineLib.collectOneStack(var1, var2, var3, this.contents[var4]);
                this.buffer.addNewColor(var5, this.color);
                this.drainBuffer();
                return true;
            }
        }
    }

    protected boolean suckFilter(ItemStack var1)
    {
        if (this.filterMap == null)
        {
            this.regenFilterMap();
        }

        return this.filterMap.size() == 0 ? true : this.filterMap.containsKey(var1);
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
        return "Filter";
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

        this.color = var1.getByte("color");
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
        var1.setByte("color", this.color);
    }
}
