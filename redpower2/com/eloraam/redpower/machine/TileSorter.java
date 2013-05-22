package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.MachineLib$FilterMap;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TubeBuffer;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileSorter$1;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileSorter extends TileTranspose implements IInventory, ISidedInventory, IBluePowerConnectable
{
    BluePowerEndpoint cond = new TileSorter$1(this);
    public int ConMask = -1;
    private ItemStack[] contents = new ItemStack[40];
    public byte[] colors = new byte[8];
    public byte mode = 0;
    public byte automode = 0;
    public byte defcolor = 0;
    public byte draining = -1;
    public byte column = 0;
    public int pulses = 0;
    protected MachineLib$FilterMap filterMap = null;
    TubeBuffer[] channelBuffers = new TubeBuffer[8];

    public TileSorter()
    {
        for (int var1 = 0; var1 < 8; ++var1)
        {
            this.channelBuffers[var1] = new TubeBuffer();
        }
    }

    void regenFilterMap()
    {
        this.filterMap = MachineLib.makeFilterMap(this.contents);
    }

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
        return 0;
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
            if (!this.Powered)
            {
                this.Delay = false;
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

            if ((this.automode == 1 || this.automode == 2 && this.pulses > 0) && !this.isTickScheduled())
            {
                this.scheduleTick(10);
            }
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
            var1.openGui(RedPowerMachine.instance, 5, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public int getExtendedID()
    {
        return 5;
    }

    public void onBlockRemoval()
    {
        super.onBlockRemoval();
        int var1;

        for (var1 = 0; var1 < 8; ++var1)
        {
            this.channelBuffers[var1].onRemove(this);
        }

        for (var1 = 0; var1 < 40; ++var1)
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

        if (this.automode == 0)
        {
            super.onBlockNeighborChange(var1);
        }

        if (this.automode == 2)
        {
            if (!RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 16777215, 63))
            {
                this.Powered = false;
                this.dirtyBlock();
                return;
            }

            if (this.Powered)
            {
                return;
            }

            this.Powered = true;
            this.dirtyBlock();

            if (this.Delay)
            {
                return;
            }

            this.Delay = true;
            ++this.pulses;
        }
    }

    protected int getColumnMatch(ItemStack var1)
    {
        if (this.filterMap == null)
        {
            this.regenFilterMap();
        }

        if (this.filterMap.size() == 0)
        {
            return -2;
        }
        else
        {
            int var2 = this.filterMap.firstMatch(var1);
            return var2 < 0 ? var2 : var2 & 7;
        }
    }

    protected void fireMatch()
    {
        this.Active = true;
        this.updateBlock();
        this.scheduleTick(5);
    }

    protected boolean tryDrainBuffer(TubeBuffer var1)
    {
        if (var1.isEmpty())
        {
            return false;
        }
        else
        {
            while (!var1.isEmpty())
            {
                TubeItem var2 = var1.getLast();

                if (this.stuffCart(var2.item))
                {
                    var1.pop();
                }
                else
                {
                    if (!this.handleItem(var2))
                    {
                        var1.plugged = true;
                        return true;
                    }

                    var1.pop();

                    if (var1.plugged)
                    {
                        return true;
                    }
                }
            }

            return true;
        }
    }

    protected boolean tryDrainBuffer()
    {
        for (int var1 = 0; var1 < 9; ++var1)
        {
            ++this.draining;
            TubeBuffer var2;

            if (this.draining > 7)
            {
                this.draining = -1;
                var2 = this.buffer;
            }
            else
            {
                var2 = this.channelBuffers[this.draining];
            }

            if (this.tryDrainBuffer(var2))
            {
                return false;
            }
        }

        return true;
    }

    protected boolean isBufferEmpty()
    {
        if (!this.buffer.isEmpty())
        {
            return false;
        }
        else
        {
            for (int var1 = 0; var1 < 8; ++var1)
            {
                if (!this.channelBuffers[var1].isEmpty())
                {
                    return false;
                }
            }

            return true;
        }
    }

    public void drainBuffer()
    {
        this.tryDrainBuffer();
    }

    private boolean autoTick()
    {
        if (this.Active)
        {
            return false;
        }
        else if (this.automode == 2 && this.pulses == 0)
        {
            return false;
        }
        else
        {
            WorldCoord var1 = new WorldCoord(this);
            var1.step(this.Rotation ^ 1);

            if (this.handleExtract(var1))
            {
                this.Active = true;
                this.updateBlock();
                this.scheduleTick(5);
            }
            else
            {
                this.scheduleTick(10);
            }

            return true;
        }
    }

    public void onTileTick()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.automode == 1 && this.Powered)
            {
                this.Powered = false;
                this.updateBlock();
            }

            if (this.automode <= 0 || !this.autoTick())
            {
                if (this.Active)
                {
                    if (!this.tryDrainBuffer())
                    {
                        if (this.isBufferEmpty())
                        {
                            this.scheduleTick(5);
                        }
                        else
                        {
                            this.scheduleTick(10);
                        }
                    }
                    else
                    {
                        if (!this.Powered || this.automode == 2)
                        {
                            this.Active = false;
                            this.updateBlock();
                        }

                        if (this.automode == 1 || this.automode == 2 && this.pulses > 0)
                        {
                            this.scheduleTick(5);
                        }
                    }
                }
            }
        }
    }

    public boolean tubeItemEnter(int var1, int var2, TubeItem var3)
    {
        int var4;
        TubeBuffer var5;

        if (var1 == this.Rotation && var2 == 2)
        {
            var4 = this.getColumnMatch(var3.item);
            var5 = this.buffer;

            if (var4 >= 0 && this.mode > 1)
            {
                var5 = this.channelBuffers[var4];
            }

            var5.addBounce(var3);
            this.fireMatch();
            return true;
        }
        else if (var1 == (this.Rotation ^ 1) && var2 == 1)
        {
            if (var3.priority > 0)
            {
                return false;
            }
            else if (this.automode == 0 && this.Powered)
            {
                return false;
            }
            else if (this.cond.getVoltage() < 60.0D)
            {
                return false;
            }
            else
            {
                var4 = this.getColumnMatch(var3.item);
                var5 = this.buffer;

                if (var4 >= 0 && this.mode > 1)
                {
                    var5 = this.channelBuffers[var4];
                }

                if (!var5.isEmpty())
                {
                    return false;
                }
                else if (var4 < 0)
                {
                    if (this.mode != 4 && this.mode != 6)
                    {
                        if (var4 == -2)
                        {
                            this.cond.drawPower((double)(25 * var3.item.stackSize));
                            var5.addNewColor(var3.item, 0);
                            this.fireMatch();
                            this.tryDrainBuffer(var5);
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        this.cond.drawPower((double)(25 * var3.item.stackSize));
                        var5.addNewColor(var3.item, this.defcolor);
                        this.fireMatch();
                        this.tryDrainBuffer(var5);
                        return true;
                    }
                }
                else
                {
                    this.cond.drawPower((double)(25 * var3.item.stackSize));
                    var5.addNewColor(var3.item, this.colors[var4]);
                    this.fireMatch();
                    this.tryDrainBuffer(var5);
                    return true;
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
        if (var1 == this.Rotation && var2 == 2)
        {
            return true;
        }
        else if (var1 == (this.Rotation ^ 1) && var2 == 1)
        {
            if (var3.priority > 0)
            {
                return false;
            }
            else if (this.automode == 0 && this.Powered)
            {
                return false;
            }
            else if (this.cond.getVoltage() < 60.0D)
            {
                return false;
            }
            else
            {
                int var4 = this.getColumnMatch(var3.item);
                TubeBuffer var5 = this.buffer;

                if (var4 >= 0 && this.mode > 1)
                {
                    var5 = this.channelBuffers[var4];
                }

                return !var5.isEmpty() ? false : (var4 < 0 ? (this.mode != 4 && this.mode != 6 ? var4 == -2 : true) : true);
            }
        }
        else
        {
            return false;
        }
    }

    protected void addToBuffer(ItemStack var1)
    {
        int var2 = this.getColumnMatch(var1);
        TubeBuffer var3 = this.buffer;

        if (var2 >= 0 && this.mode > 1)
        {
            var3 = this.channelBuffers[var2];
        }

        if (var2 < 0)
        {
            if (this.mode != 4 && this.mode != 6)
            {
                var3.addNewColor(var1, 0);
            }
            else
            {
                var3.addNewColor(var1, this.defcolor);
            }
        }
        else
        {
            var3.addNewColor(var1, this.colors[var2]);
        }
    }

    private void stepColumn()
    {
        for (int var1 = 0; var1 < 8; ++var1)
        {
            ++this.column;

            if (this.column > 7)
            {
                if (this.pulses > 0)
                {
                    --this.pulses;
                }

                this.column = 0;
            }

            for (int var2 = 0; var2 < 5; ++var2)
            {
                ItemStack var3 = this.contents[var2 * 8 + this.column];

                if (var3 != null && var3.stackSize != 0)
                {
                    return;
                }
            }
        }

        this.column = 0;
    }

    private void checkColumn()
    {
        for (int var1 = 0; var1 < 5; ++var1)
        {
            ItemStack var2 = this.contents[var1 * 8 + this.column];

            if (var2 != null && var2.stackSize != 0)
            {
                return;
            }
        }

        this.stepColumn();
        this.dirtyBlock();
    }

    protected boolean handleExtract(IInventory var1, int var2, int var3)
    {
        if (this.cond.getVoltage() < 60.0D)
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
                ItemStack var8 = MachineLib.collectOneStack(var1, var2, var3, (ItemStack)null);

                if (var8 == null)
                {
                    return false;
                }
                else
                {
                    if (this.mode != 4 && this.mode != 6)
                    {
                        this.buffer.addNew(var8);
                    }
                    else
                    {
                        this.buffer.addNewColor(var8, this.defcolor);
                    }

                    this.cond.drawPower((double)(25 * var8.stackSize));
                    this.drainBuffer();
                    return true;
                }
            }
            else
            {
                int var4;
                ItemStack var5;
                int var6;
                ItemStack var7;

                switch (this.mode)
                {
                    case 0:
                        this.checkColumn();
                        var4 = MachineLib.matchAnyStackCol(this.filterMap, var1, var2, var3, this.column);

                        if (var4 < 0)
                        {
                            return false;
                        }

                        var5 = MachineLib.collectOneStack(var1, var2, var3, this.contents[var4]);
                        this.buffer.addNewColor(var5, this.colors[var4 & 7]);
                        this.cond.drawPower((double)(25 * var5.stackSize));
                        this.stepColumn();
                        this.drainBuffer();
                        return true;

                    case 1:
                        this.checkColumn();

                        if (!MachineLib.matchAllCol(this.filterMap, var1, var2, var3, this.column))
                        {
                            return false;
                        }

                        for (var6 = 0; var6 < 5; ++var6)
                        {
                            var7 = this.contents[var6 * 8 + this.column];

                            if (var7 != null && var7.stackSize != 0)
                            {
                                var5 = MachineLib.collectOneStack(var1, var2, var3, var7);
                                this.buffer.addNewColor(var5, this.colors[this.column]);
                                this.cond.drawPower((double)(25 * var5.stackSize));
                            }
                        }

                        this.stepColumn();
                        this.drainBuffer();
                        return true;

                    case 2:
                        for (var4 = 0; var4 < 8 && !MachineLib.matchAllCol(this.filterMap, var1, var2, var3, var4); ++var4)
                        {
                            ;
                        }

                        if (var4 == 8)
                        {
                            return false;
                        }
                        else
                        {
                            for (var6 = 0; var6 < 5; ++var6)
                            {
                                var7 = this.contents[var6 * 8 + var4];

                                if (var7 != null && var7.stackSize != 0)
                                {
                                    var5 = MachineLib.collectOneStack(var1, var2, var3, var7);
                                    this.channelBuffers[var4].addNewColor(var5, this.colors[var4]);
                                    this.cond.drawPower((double)(25 * var5.stackSize));
                                }
                            }

                            if (this.pulses > 0)
                            {
                                --this.pulses;
                            }

                            this.drainBuffer();
                            return true;
                        }

                    case 3:
                        var4 = MachineLib.matchAnyStack(this.filterMap, var1, var2, var3);

                        if (var4 < 0)
                        {
                            return false;
                        }

                        var5 = MachineLib.collectOneStack(var1, var2, var3, this.contents[var4]);
                        this.channelBuffers[var4 & 7].addNewColor(var5, this.colors[var4 & 7]);
                        this.cond.drawPower((double)(25 * var5.stackSize));

                        if (this.pulses > 0)
                        {
                            --this.pulses;
                        }

                        this.drainBuffer();
                        return true;

                    case 4:
                        var4 = MachineLib.matchAnyStack(this.filterMap, var1, var2, var3);

                        if (var4 < 0)
                        {
                            var5 = MachineLib.collectOneStack(var1, var2, var3, (ItemStack)null);

                            if (var5 == null)
                            {
                                return false;
                            }

                            this.buffer.addNewColor(var5, this.defcolor);
                        }
                        else
                        {
                            var5 = MachineLib.collectOneStack(var1, var2, var3, this.contents[var4]);
                            this.channelBuffers[var4 & 7].addNewColor(var5, this.colors[var4 & 7]);
                        }

                        this.cond.drawPower((double)(25 * var5.stackSize));

                        if (this.pulses > 0)
                        {
                            --this.pulses;
                        }

                        this.drainBuffer();
                        return true;

                    case 5:
                        var4 = MachineLib.matchAnyStack(this.filterMap, var1, var2, var3);

                        if (var4 < 0)
                        {
                            return false;
                        }

                        var5 = MachineLib.collectOneStackFuzzy(var1, var2, var3, this.contents[var4]);
                        this.channelBuffers[var4 & 7].addNewColor(var5, this.colors[var4 & 7]);
                        this.cond.drawPower((double)(25 * var5.stackSize));

                        if (this.pulses > 0)
                        {
                            --this.pulses;
                        }

                        this.drainBuffer();
                        return true;

                    case 6:
                        var4 = MachineLib.matchAnyStack(this.filterMap, var1, var2, var3);

                        if (var4 < 0)
                        {
                            var5 = MachineLib.collectOneStack(var1, var2, var3, (ItemStack)null);

                            if (var5 == null)
                            {
                                return false;
                            }

                            this.buffer.addNewColor(var5, this.defcolor);
                        }
                        else
                        {
                            var5 = MachineLib.collectOneStackFuzzy(var1, var2, var3, this.contents[var4]);
                            this.channelBuffers[var4 & 7].addNewColor(var5, this.colors[var4 & 7]);
                        }

                        this.cond.drawPower((double)(25 * var5.stackSize));

                        if (this.pulses > 0)
                        {
                            --this.pulses;
                        }

                        this.drainBuffer();
                        return true;

                    default:
                        return false;
                }
            }
        }
    }

    protected boolean suckFilter(ItemStack var1)
    {
        if (this.cond.getVoltage() < 60.0D)
        {
            return false;
        }
        else
        {
            if (this.filterMap == null)
            {
                this.regenFilterMap();
            }

            int var2 = this.getColumnMatch(var1);
            TubeBuffer var3 = this.buffer;

            if (var2 >= 0 && this.mode > 1)
            {
                var3 = this.channelBuffers[var2];
            }

            if (var3.plugged)
            {
                return false;
            }
            else if (var2 < 0)
            {
                if (this.mode != 4 && this.mode != 6 && var2 != -2)
                {
                    return false;
                }
                else
                {
                    this.cond.drawPower((double)(25 * var1.stackSize));
                    return true;
                }
            }
            else
            {
                this.cond.drawPower((double)(25 * var1.stackSize));
                return true;
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 40;
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
        return "Sorter";
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
        int var5;

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.contents.length)
            {
                this.contents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.column = var1.getByte("coln");
        byte[] var7 = var1.getByteArray("cols");

        if (var7.length >= 8)
        {
            for (int var8 = 0; var8 < 8; ++var8)
            {
                this.colors[var8] = var7[var8];
            }
        }

        this.mode = var1.getByte("mode");
        this.automode = var1.getByte("amode");
        this.draining = var1.getByte("drain");

        if (this.mode == 4 || this.mode == 6)
        {
            this.defcolor = var1.getByte("defc");
        }

        this.pulses = var1.getInteger("pulses");
        this.cond.readFromNBT(var1);
        NBTTagList var9 = var1.getTagList("buffers");

        for (var5 = 0; var5 < var9.tagCount(); ++var5)
        {
            NBTTagCompound var6 = (NBTTagCompound)var9.tagAt(var5);
            this.channelBuffers[var5].readFromNBT(var6);
        }
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

        var1.setByte("coln", this.column);
        var1.setTag("Items", var2);
        var1.setByteArray("cols", this.colors);
        var1.setByte("mode", this.mode);
        var1.setByte("amode", this.automode);
        var1.setByte("drain", this.draining);
        var1.setInteger("pulses", this.pulses);

        if (this.mode == 4 || this.mode == 6)
        {
            var1.setByte("defc", this.defcolor);
        }

        this.cond.writeToNBT(var1);
        NBTTagList var6 = new NBTTagList();

        for (int var7 = 0; var7 < 8; ++var7)
        {
            NBTTagCompound var5 = new NBTTagCompound();
            this.channelBuffers[var7].writeToNBT(var5);
            var6.appendTag(var5);
        }

        var1.setTag("buffers", var6);
    }
}
