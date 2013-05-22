package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.ITubeConnectable;
import com.eloraam.redpower.core.ITubeRequest;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.MachineLib$FilterMap;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TubeBuffer;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.TubeLib$RequestRouteFinder;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileManager$1;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileManager extends TileMachine implements IBluePowerConnectable, IInventory, ISidedInventory, ITubeConnectable, ITubeRequest
{
    BluePowerEndpoint cond = new TileManager$1(this);
    TubeBuffer buffer = new TubeBuffer();
    protected ItemStack[] contents = new ItemStack[24];
    public int ConMask = -1;
    public byte color = 0;
    public byte mode = 0;
    public int priority = 0;
    public byte rqnum = 0;
    protected MachineLib$FilterMap filterMap = null;

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
        return 0;
    }

    public int getSizeInventorySide(ForgeDirection var1)
    {
        int var2 = var1.ordinal();
        return var2 != this.Rotation && var2 != (this.Rotation ^ 1) ? 24 : 0;
    }

    protected IInventory getConnectedInventory(boolean var1)
    {
        WorldCoord var2 = new WorldCoord(this);
        var2.step(this.Rotation ^ 1);
        return MachineLib.getSideInventory(this.worldObj, var2, this.Rotation, var1);
    }

    protected void regenFilterMap()
    {
        this.filterMap = MachineLib.makeFilterMap(this.contents, 0, 24);
    }

    protected int[] getAcceptCounts()
    {
        if (this.filterMap == null)
        {
            this.regenFilterMap();
        }

        if (this.filterMap.size() == 0)
        {
            return null;
        }
        else
        {
            IInventory var1 = this.getConnectedInventory(true);

            if (var1 == null)
            {
                return null;
            }
            else
            {
                int[] var2 = MachineLib.genMatchCounts(this.filterMap);
                MachineLib.decMatchCounts(this.filterMap, var2, var1, 0, var1.getSizeInventory());
                return var2;
            }
        }
    }

    protected int acceptCount(ItemStack var1)
    {
        if (this.filterMap == null)
        {
            this.regenFilterMap();
        }

        if (this.filterMap.size() == 0)
        {
            return 0;
        }
        else if (!this.filterMap.containsKey(var1))
        {
            return 0;
        }
        else
        {
            int[] var2 = this.getAcceptCounts();
            return var2 == null ? 0 : MachineLib.getMatchCount(this.filterMap, var2, var1);
        }
    }

    protected void doRequest(int var1, int var2)
    {
        ItemStack var3 = CoreLib.copyStack(this.contents[var1], Math.min(64, var2));
        TubeItem var4 = new TubeItem(0, var3);
        var4.priority = (short)this.priority;
        var4.color = this.color;
        TubeLib$RequestRouteFinder var5 = new TubeLib$RequestRouteFinder(this.worldObj, var4);

        if (var5.find(new WorldCoord(this), 63) >= 0)
        {
            WorldCoord var6 = var5.getResultPoint();
            ITubeRequest var7 = (ITubeRequest)CoreLib.getTileEntity(this.worldObj, var6, ITubeRequest.class);
            var7.requestTubeItem(var4, true);
            this.cond.drawPower(100.0D);
            this.scheduleTick(20);
        }
    }

    protected void scanInventory()
    {
        IInventory var1 = this.getConnectedInventory(false);

        if (var1 != null)
        {
            if (this.filterMap == null)
            {
                this.regenFilterMap();
            }

            int[] var2 = MachineLib.genMatchCounts(this.filterMap);

            if (var2 != null)
            {
                for (int var3 = 0; var3 < var1.getSizeInventory(); ++var3)
                {
                    ItemStack var4 = var1.getStackInSlot(var3);

                    if (var4 != null && var4.stackSize != 0)
                    {
                        if (this.mode == 0)
                        {
                            int var5 = MachineLib.decMatchCount(this.filterMap, var2, var4);

                            if (var5 < var4.stackSize)
                            {
                                ItemStack var6 = var1.decrStackSize(var3, var4.stackSize - var5);
                                this.cond.drawPower((double)(25 * var4.stackSize));
                                this.buffer.addNewColor(var6, this.color);
                                this.Active = true;
                                this.scheduleTick(5);
                                this.updateBlock();
                                return;
                            }
                        }
                        else if (this.mode == 1 && !this.filterMap.containsKey(var4))
                        {
                            var1.setInventorySlotContents(var3, (ItemStack)null);
                            this.cond.drawPower((double)(25 * var4.stackSize));
                            this.buffer.addNewColor(var4, this.color);
                            this.Active = true;
                            this.scheduleTick(5);
                            this.updateBlock();
                            return;
                        }
                    }
                }

                boolean var7 = false;

                if (this.mode == 0)
                {
                    var2 = this.getAcceptCounts();

                    if (var2 != null)
                    {
                        var7 = true;
                        ++this.rqnum;

                        if (this.rqnum >= 24)
                        {
                            this.rqnum = 0;
                        }

                        int var8;

                        for (var8 = this.rqnum; var8 < var2.length; ++var8)
                        {
                            if (var2[var8] != 0)
                            {
                                var7 = false;
                                this.doRequest(var8, var2[var8]);
                                break;
                            }
                        }

                        for (var8 = 0; var8 < this.rqnum; ++var8)
                        {
                            if (var2[var8] != 0)
                            {
                                var7 = false;
                                this.doRequest(var8, var2[var8]);
                                break;
                            }
                        }
                    }
                }

                if (this.Powered != var7)
                {
                    this.Powered = var7;
                    this.updateBlockChange();
                }
            }
        }
    }

    public int getTubeConnectableSides()
    {
        return 1 << this.Rotation;
    }

    public int getTubeConClass()
    {
        return 0;
    }

    public boolean canRouteItems()
    {
        return false;
    }

    private boolean handleTubeItem(TubeItem var1)
    {
        if (this.cond.getVoltage() < 60.0D)
        {
            return false;
        }
        else if (var1.priority > this.priority)
        {
            return false;
        }
        else if (var1.color != this.color && this.color != 0 && var1.color != 0)
        {
            return false;
        }
        else if (this.mode == 1)
        {
            if (this.filterMap == null)
            {
                this.regenFilterMap();
            }

            if (this.filterMap.size() == 0)
            {
                return false;
            }
            else if (!this.filterMap.containsKey(var1.item))
            {
                return false;
            }
            else
            {
                IInventory var6 = this.getConnectedInventory(true);

                if (MachineLib.addToInventoryCore(var6, var1.item, 0, var6.getSizeInventory(), true))
                {
                    this.Delay = true;
                    this.scheduleTick(5);
                    this.updateBlock();
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            int var2 = this.acceptCount(var1.item);

            if (var2 == 0)
            {
                return false;
            }
            else
            {
                boolean var3 = true;
                ItemStack var4 = var1.item;

                if (var2 < var4.stackSize)
                {
                    var3 = false;
                    var4 = var4.splitStack(var2);
                }

                IInventory var5 = this.getConnectedInventory(true);

                if (MachineLib.addToInventoryCore(var5, var4, 0, var5.getSizeInventory(), true))
                {
                    this.Delay = true;
                    this.scheduleTick(5);
                    this.updateBlock();
                    return var3;
                }
                else
                {
                    return false;
                }
            }
        }
    }

    public boolean tubeItemEnter(int var1, int var2, TubeItem var3)
    {
        if (var1 != this.Rotation)
        {
            return false;
        }
        else if (var2 == 2)
        {
            if (this.handleTubeItem(var3))
            {
                return true;
            }
            else
            {
                this.buffer.addBounce(var3);
                this.Active = true;
                this.updateBlock();
                this.scheduleTick(5);
                return true;
            }
        }
        else
        {
            return this.handleTubeItem(var3);
        }
    }

    public boolean tubeItemCanEnter(int var1, int var2, TubeItem var3)
    {
        if (var1 != this.Rotation)
        {
            return false;
        }
        else if (var2 == 2)
        {
            return true;
        }
        else if (this.cond.getVoltage() < 60.0D)
        {
            return false;
        }
        else if (var3.priority > this.priority)
        {
            return false;
        }
        else if (var3.color != this.color && this.color != 0 && var3.color != 0)
        {
            return false;
        }
        else
        {
            switch (this.mode)
            {
                case 0:
                    return this.acceptCount(var3.item) > 0;

                case 1:
                    if (this.filterMap == null)
                    {
                        this.regenFilterMap();
                    }

                    if (this.filterMap.size() == 0)
                    {
                        return false;
                    }

                    return this.filterMap.containsKey(var3.item);

                default:
                    return false;
            }
        }
    }

    public int tubeWeight(int var1, int var2)
    {
        return var1 == this.Rotation && var2 == 2 ? this.buffer.size() : 0;
    }

    public boolean requestTubeItem(TubeItem var1, boolean var2)
    {
        if (this.Active)
        {
            return false;
        }
        else if (this.cond.getVoltage() < 60.0D)
        {
            return false;
        }
        else
        {
            if (this.filterMap == null)
            {
                this.regenFilterMap();
            }

            if (this.filterMap.size() == 0)
            {
                return false;
            }
            else if (!this.filterMap.containsKey(var1.item))
            {
                return false;
            }
            else if (var1.priority <= this.priority)
            {
                return false;
            }
            else if (var1.color != this.color && this.color > 0)
            {
                return false;
            }
            else
            {
                IInventory var3 = this.getConnectedInventory(false);

                if (var3 == null)
                {
                    return false;
                }
                else
                {
                    for (int var4 = 0; var4 < var3.getSizeInventory(); ++var4)
                    {
                        ItemStack var5 = var3.getStackInSlot(var4);

                        if (var5 != null && var5.stackSize != 0 && CoreLib.compareItemStack(var1.item, var5) == 0)
                        {
                            if (var2)
                            {
                                ItemStack var6 = var3.decrStackSize(var4, Math.min(var1.item.stackSize, var5.stackSize));
                                TubeItem var7 = new TubeItem(0, var6);
                                this.cond.drawPower((double)(25 * var7.item.stackSize));
                                var7.priority = var1.priority;
                                var7.color = this.color;
                                this.buffer.add(var7);
                                this.Active = true;
                                this.scheduleTick(5);
                                this.updateBlock();
                            }

                            return true;
                        }
                    }

                    return false;
                }
            }
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

            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }

            this.cond.iterate();
            this.dirtyBlock();

            if (this.cond.Flow == 0)
            {
                if (this.Charged)
                {
                    this.Charged = false;
                    this.updateBlock();
                }
            }
            else if (!this.Charged)
            {
                this.Charged = true;
                this.updateBlock();
            }
        }
    }

    public boolean isPoweringTo(int var1)
    {
        return var1 == (this.Rotation ^ 1) ? false : this.Powered;
    }

    public int getBlockID()
    {
        return RedPowerMachine.blockMachine2.blockID;
    }

    public int getExtendedID()
    {
        return 1;
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
            var1.openGui(RedPowerMachine.instance, 16, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;
    }

    public void drainBuffer()
    {
        while (true)
        {
            if (!this.buffer.isEmpty())
            {
                TubeItem var1 = this.buffer.getLast();

                if (this.handleTubeItem(var1))
                {
                    this.buffer.pop();

                    if (!this.buffer.plugged)
                    {
                        continue;
                    }

                    return;
                }

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

    public void onTileTick()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            boolean var1 = false;

            if (this.Delay)
            {
                this.Delay = false;
                var1 = true;
            }

            if (this.Active)
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
            else if (var1)
            {
                this.updateBlock();
            }
            else if (this.cond.getVoltage() >= 60.0D)
            {
                this.scanInventory();
            }
        }
    }

    public void onBlockRemoval()
    {
        super.onBlockRemoval();

        for (int var1 = 0; var1 < this.contents.length; ++var1)
        {
            ItemStack var2 = this.contents[var1];

            if (var2 != null && var2.stackSize > 0)
            {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 24;
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
        return "Manager";
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
        this.cond.readFromNBT(var1);
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
        this.mode = var1.getByte("mode");
        this.priority = var1.getInteger("prio");
        this.rqnum = var1.getByte("rqnum");
        this.buffer.readFromNBT(var1);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.cond.writeToNBT(var1);
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
        var1.setByte("mode", this.mode);
        var1.setInteger("prio", this.priority);
        var1.setByte("rqnum", this.rqnum);
        this.buffer.writeToNBT(var1);
    }
}
