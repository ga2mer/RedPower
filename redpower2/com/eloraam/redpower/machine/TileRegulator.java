package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.ITubeConnectable;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.MachineLib$FilterMap;
import com.eloraam.redpower.core.TubeBuffer;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.WorldCoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileRegulator extends TileMachine implements ITubeConnectable, IInventory, ISidedInventory
{
    TubeBuffer buffer = new TubeBuffer();
    public byte mode = 0;
    protected ItemStack[] contents = new ItemStack[27];
    protected MachineLib$FilterMap inputMap = null;
    protected MachineLib$FilterMap outputMap = null;
    public int color = 0;

    void regenFilterMap()
    {
        this.inputMap = MachineLib.makeFilterMap(this.contents, 0, 9);
        this.outputMap = MachineLib.makeFilterMap(this.contents, 18, 9);
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
            int var4 = this.inCount(var3.item);

            if (var4 == 0)
            {
                return false;
            }
            else
            {
                boolean var5 = true;
                ItemStack var6 = var3.item;

                if (var4 < var6.stackSize)
                {
                    var5 = false;
                    var6 = var6.splitStack(var4);
                }

                if (MachineLib.addToInventoryCore(this, var6, 9, 9, true))
                {
                    this.onInventoryChanged();
                    this.scheduleTick(2);
                    this.dirtyBlock();
                    return var5;
                }
                else
                {
                    this.dirtyBlock();
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

    public boolean tubeItemCanEnter(int var1, int var2, TubeItem var3)
    {
        return var1 == this.Rotation && var2 == 2 ? true : (var1 == (this.Rotation ^ 1) && var2 == 1 ? (this.inCount(var3.item) == 0 ? false : MachineLib.addToInventoryCore(this, var3.item, 9, 9, false)) : false);
    }

    public int tubeWeight(int var1, int var2)
    {
        return var1 == this.Rotation && var2 == 2 ? this.buffer.size() : 0;
    }

    public int getStartInventorySide(ForgeDirection var1)
    {
        return 9;
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

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (!CoreLib.isClient(this.worldObj))
        {
            if (!this.isTickScheduled())
            {
                this.scheduleTick(10);
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
            var1.openGui(RedPowerMachine.instance, 9, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public int getExtendedID()
    {
        return 10;
    }

    public void onBlockRemoval()
    {
        this.buffer.onRemove(this);

        for (int var1 = 0; var1 < 27; ++var1)
        {
            ItemStack var2 = this.contents[var1];

            if (var2 != null && var2.stackSize > 0)
            {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
            }
        }
    }

    private int[] scanInput()
    {
        if (this.inputMap == null)
        {
            this.regenFilterMap();
        }

        if (this.inputMap.size() == 0)
        {
            return null;
        }
        else
        {
            int[] var1 = MachineLib.genMatchCounts(this.inputMap);
            MachineLib.decMatchCounts(this.inputMap, var1, this, 9, 9);
            return var1;
        }
    }

    private int inCount(ItemStack var1)
    {
        if (this.inputMap == null)
        {
            this.regenFilterMap();
        }

        if (this.inputMap.size() == 0)
        {
            return 0;
        }
        else if (!this.inputMap.containsKey(var1))
        {
            return 0;
        }
        else
        {
            int[] var2 = MachineLib.genMatchCounts(this.inputMap);
            MachineLib.decMatchCounts(this.inputMap, var2, this, 9, 9);
            return MachineLib.decMatchCount(this.inputMap, var2, var1);
        }
    }

    private int[] scanOutput()
    {
        WorldCoord var1 = new WorldCoord(this);
        var1.step(this.Rotation);
        IInventory var2 = MachineLib.getInventory(this.worldObj, var1);

        if (var2 == null)
        {
            return null;
        }
        else
        {
            int var3 = 0;
            int var4 = var2.getSizeInventory();

            if (var2 instanceof ISidedInventory)
            {
                ISidedInventory var5 = (ISidedInventory)var2;
                ForgeDirection var6 = ForgeDirection.getOrientation((this.Rotation ^ 1) & 255);
                var3 = var5.getStartInventorySide(var6);
                var4 = var5.getSizeInventorySide(var6);
            }

            if (this.outputMap == null)
            {
                this.regenFilterMap();
            }

            if (this.outputMap.size() == 0)
            {
                return null;
            }
            else
            {
                int[] var7 = MachineLib.genMatchCounts(this.outputMap);
                MachineLib.decMatchCounts(this.outputMap, var7, var2, var3, var4);
                return var7;
            }
        }
    }

    private void handleTransfer(int[] var1)
    {
        if (this.mode != 0 && var1 != null)
        {
            boolean var7 = false;

            for (int var8 = 0; var8 < 9; ++var8)
            {
                while (var1[var8] > 0)
                {
                    ItemStack var4 = this.contents[18 + var8].copy();
                    int var5 = Math.min(var4.stackSize, var1[var8]);
                    var1[var8] -= var5;
                    var4.stackSize = var5;
                    ItemStack var6 = MachineLib.collectOneStack(this, 9, 9, var4);

                    if (var6 != null)
                    {
                        this.buffer.addNewColor(var6, this.color);
                        var7 = true;
                    }
                }
            }

            if (!var7)
            {
                return;
            }
        }
        else
        {
            for (int var2 = 0; var2 < 9; ++var2)
            {
                ItemStack var3 = this.contents[9 + var2];

                if (var3 != null && var3.stackSize != 0)
                {
                    this.buffer.addNewColor(var3, this.color);
                    this.contents[9 + var2] = null;
                }
            }
        }

        this.onInventoryChanged();
        this.Powered = true;
        this.Active = true;
        this.updateBlockChange();
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

    public void onTileTick()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.Active)
            {
                if (!this.buffer.isEmpty())
                {
                    this.Powered = true;
                    this.drainBuffer();
                    this.updateBlockChange();

                    if (!this.buffer.isEmpty())
                    {
                        this.scheduleTick(10);
                    }

                    return;
                }

                this.Active = false;
                this.updateBlock();
            }

            int[] var1;
            int[] var2;

            if (this.Powered)
            {
                var1 = this.scanOutput();

                if (var1 == null)
                {
                    this.Powered = false;
                    this.updateBlockChange();
                }
                else if (!MachineLib.isMatchEmpty(var1))
                {
                    var2 = this.scanInput();

                    if (var2 != null && MachineLib.isMatchEmpty(var2))
                    {
                        this.handleTransfer(var1);
                    }
                    else
                    {
                        this.Powered = false;
                        this.updateBlockChange();
                    }
                }
            }
            else
            {
                var1 = this.scanOutput();

                if (var1 != null && MachineLib.isMatchEmpty(var1))
                {
                    this.Powered = true;
                    this.updateBlockChange();
                }
                else
                {
                    var2 = this.scanInput();

                    if (var2 != null && MachineLib.isMatchEmpty(var2))
                    {
                        this.handleTransfer(var1);
                    }
                    else if (var1 != null && this.mode == 1)
                    {
                        this.handleTransfer(var1);
                    }
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
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
        return "Regulator";
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
        this.inputMap = null;
        this.outputMap = null;
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
        this.mode = var1.getByte("mode");
        this.color = var1.getByte("col");
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
        var1.setByte("mode", this.mode);
        var1.setByte("col", (byte)this.color);
    }
}
