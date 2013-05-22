package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.EnvironLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.IMultiblock;
import com.eloraam.redpower.core.MultiLib;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileWindTurbine$1;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;

public class TileWindTurbine extends TileMachine implements IInventory, IBluePowerConnectable, IMultiblock
{
    BluePowerConductor cond = new TileWindTurbine$1(this);
    private byte[] rayTrace = null;
    private int efficiency = 0;
    private int tracer = 0;
    public int windSpeed = 0;
    public int speed = 0;
    public int phase = 0;
    private int power = 0;
    private int propTicks = 0;
    public boolean hasBlades = false;
    public boolean hasBrakes = false;
    public byte windmillType = 0;
    protected ItemStack[] contents = new ItemStack[1];
    public int ConMask = -1;
    public int EConMask = -1;

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
        return 2;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return this.cond;
    }

    public void setPartRotation(int var1, boolean var2, int var3)
    {
        this.teardownBlades();
        super.setPartRotation(var1, var2, var3);
    }

    public void onMultiRemoval(int var1)
    {
        ItemStack var2 = this.contents[0];

        if (var2 != null && var2.stackSize > 0)
        {
            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord + 1, this.zCoord, var2);
        }

        this.contents[0] = null;
        this.onInventoryChanged();
        this.teardownBlades();
    }

    public AxisAlignedBB getMultiBounds(int var1)
    {
        switch (this.windmillType)
        {
            case 1:
                return AxisAlignedBB.getBoundingBox(-2.5D, 1.3D, -2.5D, 3.5D, 9.0D, 3.5D);

            case 2:
                WorldCoord var2 = new WorldCoord(0, 0, 0);
                int var3 = WorldCoord.getRightDir(this.Rotation);
                var2.step(this.Rotation ^ 1);
                WorldCoord var4 = var2.coordStep(this.Rotation ^ 1);
                var2.step(var3, 8);
                var4.step(var3, -8);
                return AxisAlignedBB.getBoundingBox((double)Math.min(var2.x, var4.x) + 0.5D, -7.5D, Math.min((double)var2.z, (double)var4.z + 0.5D), (double)Math.max(var2.x, var4.x) + 0.5D, 8.5D, (double)Math.max(var2.z, var4.z) + 0.5D);

            default:
                return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        }
    }

    public float getMultiBlockStrength(int var1, EntityPlayer var2)
    {
        return 0.08F;
    }

    public int getExtendedID()
    {
        return 9;
    }

    public int getBlockID()
    {
        return RedPowerMachine.blockMachine.blockID;
    }

    public List getRelayBlockList(int var1)
    {
        ArrayList var2 = new ArrayList();
        int var3 = WorldCoord.getRightDir(this.Rotation);
        int var4;
        int var5;

        switch (var1)
        {
            case 1:
                for (var4 = -3; var4 <= 3; ++var4)
                {
                    for (var5 = -3; var5 <= 3; ++var5)
                    {
                        for (int var7 = 1; var7 < 8; ++var7)
                        {
                            var2.add(new WorldCoord(var4 + this.xCoord, var7 + this.yCoord, var5 + this.zCoord));
                        }
                    }
                }

                return var2;

            case 2:
                for (var4 = -8; var4 <= 8; ++var4)
                {
                    for (var5 = -8; var5 <= 8; ++var5)
                    {
                        WorldCoord var6 = new WorldCoord(this);
                        var6.step(this.Rotation ^ 1);
                        var6.step(var3, var4);
                        var6.y += var5;
                        var2.add(var6);
                    }
                }
        }

        return var2;
    }

    private void teardownBlades()
    {
        this.hasBlades = false;
        this.efficiency = 0;
        this.speed = 0;
        this.rayTrace = null;
        this.updateBlock();
        List var1 = this.getRelayBlockList(this.windmillType);
        MultiLib.removeRelays(this.worldObj, new WorldCoord(this), var1);
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (CoreLib.isClient(this.worldObj))
        {
            if (this.hasBrakes)
            {
                this.phase = (int)((double)this.phase + (double)this.speed * 0.1D);
            }
            else
            {
                this.phase += this.speed;
            }
        }
        else
        {
            if (!this.isTickScheduled())
            {
                this.scheduleTick(5);
            }

            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.EConMask = RedPowerLib.getExtConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, this.EConMask);
            }

            this.cond.iterate();
            this.dirtyBlock();

            if (this.hasBlades)
            {
                if (this.contents[0] == null || !(this.contents[0].getItem() instanceof ItemWindmill))
                {
                    this.teardownBlades();
                    return;
                }

                ItemWindmill var1 = (ItemWindmill)this.contents[0].getItem();

                if (var1.windmillType != this.windmillType)
                {
                    this.teardownBlades();
                    return;
                }

                if (this.propTicks <= 0)
                {
                    this.contents[0].setItemDamage(this.contents[0].getItemDamage() + 1);

                    if (this.contents[0].getItemDamage() > this.contents[0].getMaxDamage())
                    {
                        this.contents[0] = null;
                        this.onInventoryChanged();
                        this.teardownBlades();
                        this.contents[0] = var1.getBrokenItem();
                        this.onInventoryChanged();
                        return;
                    }

                    this.onInventoryChanged();
                    this.propTicks += 6600;
                }

                if (this.hasBrakes)
                {
                    return;
                }

                --this.propTicks;

                if (this.cond.getVoltage() > 130.0D)
                {
                    return;
                }

                this.cond.applyPower((double)(this.power / 5));
            }
        }
    }

    private void traceAir0()
    {
        int var1 = this.yCoord + 1 + this.tracer / 28;
        int var2 = this.tracer % 7;
        boolean var4 = false;
        WorldCoord var3;
        byte var6;

        switch (this.tracer / 7 % 4)
        {
            case 0:
                var6 = 2;
                var3 = new WorldCoord(this.xCoord - 3 + var2, var1, this.zCoord - 4);
                break;

            case 1:
                var6 = 4;
                var3 = new WorldCoord(this.xCoord - 4, var1, this.zCoord - 3 + var2);
                break;

            case 2:
                var6 = 3;
                var3 = new WorldCoord(this.xCoord - 3 + var2, var1, this.zCoord + 4);
                break;

            default:
                var6 = 5;
                var3 = new WorldCoord(this.xCoord + 4, var1, this.zCoord - 3 + var2);
        }

        int var5;

        for (var5 = 0; var5 < 10 && this.worldObj.getBlockId(var3.x, var3.y, var3.z) == 0; ++var5)
        {
            var3.step(var6);
        }

        if (this.rayTrace == null)
        {
            this.rayTrace = new byte[224];
        }

        this.efficiency = this.efficiency - this.rayTrace[this.tracer] + var5;
        this.rayTrace[this.tracer] = (byte)var5;
        ++this.tracer;

        if (this.tracer >= 224)
        {
            this.tracer = 0;
        }
    }

    private void traceAir1()
    {
        int var1 = this.tracer / 17;
        int var2 = this.tracer % 17;
        int var3 = WorldCoord.getRightDir(this.Rotation);
        WorldCoord var4 = new WorldCoord(this);
        var4.step(this.Rotation ^ 1, 2);
        var4.step(var3, var2 - 8);
        var4.y += var1;
        int var5;

        for (var5 = 0; var5 < 20 && this.worldObj.getBlockId(var4.x, var4.y, var4.z) == 0; ++var5)
        {
            var4.step(this.Rotation ^ 1);
        }

        if (this.rayTrace == null)
        {
            this.rayTrace = new byte[289];
        }

        this.efficiency = this.efficiency - this.rayTrace[this.tracer] + var5;
        this.rayTrace[this.tracer] = (byte)var5;
        ++this.tracer;

        if (this.tracer >= 289)
        {
            this.tracer = 0;
        }
    }

    public int getWindScaled(int var1)
    {
        return Math.min(var1, var1 * this.windSpeed / 13333);
    }

    private void tryDeployBlades()
    {
        ItemWindmill var1 = (ItemWindmill)this.contents[0].getItem();

        if (var1.canFaceDirection(this.Rotation))
        {
            List var2 = this.getRelayBlockList(var1.windmillType);

            if (MultiLib.isClear(this.worldObj, new WorldCoord(this), var2))
            {
                this.windmillType = (byte)var1.windmillType;
                this.hasBlades = true;
                MultiLib.addRelays(this.worldObj, new WorldCoord(this), 0, var2);
                this.updateBlock();
            }
        }
    }

    public void onTileTick()
    {
        if (!this.hasBlades && this.contents[0] != null && this.contents[0].getItem() instanceof ItemWindmill)
        {
            this.tryDeployBlades();
        }

        if (!this.hasBrakes && this.cond.getVoltage() > 110.0D)
        {
            this.hasBrakes = true;
        }
        else if (this.hasBrakes && this.cond.getVoltage() < 100.0D)
        {
            this.hasBrakes = false;
        }

        this.windSpeed = (int)(10000.0D * EnvironLib.getWindSpeed(this.worldObj, new WorldCoord(this)));

        if (this.hasBlades)
        {
            switch (this.windmillType)
            {
                case 1:
                    this.power = 2 * this.windSpeed * this.efficiency / 2240;
                    this.speed = this.power * this.power / 20000;
                    this.traceAir0();
                    break;

                case 2:
                    this.power = this.windSpeed * this.efficiency / 5780;
                    this.speed = this.power * this.power / 5000;
                    this.traceAir1();
            }

            this.updateBlock();
        }

        this.scheduleTick(20);
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
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Wind Turbine";
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

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;
        this.EConMask = -1;
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
            var1.openGui(RedPowerMachine.instance, 15, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public void onBlockRemoval()
    {
        super.onBlockRemoval();

        if (this.hasBlades)
        {
            this.teardownBlades();
        }

        ItemStack var1 = this.contents[0];

        if (var1 != null && var1.stackSize > 0)
        {
            CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var1);
        }
    }

    @SideOnly(Side.CLIENT)
    public double func_82115_m()
    {
        return 1048576.0D;
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

        this.windmillType = var1.getByte("wmt");
        this.hasBlades = this.windmillType > 0;
        this.efficiency = 0;
        byte[] var6 = var1.getByteArray("rays");

        if (var6 != null)
        {
            switch (this.windmillType)
            {
                case 1:
                    if (var6.length != 224)
                    {
                        var6 = null;
                    }

                    break;

                case 2:
                    if (var6.length != 289)
                    {
                        var6 = null;
                    }

                    break;

                default:
                    var6 = null;
            }
        }

        this.rayTrace = var6;

        if (var6 != null)
        {
            for (int var7 = 0; var7 < var6.length; ++var7)
            {
                this.efficiency += var6[var7];
            }
        }

        this.tracer = var1.getInteger("tracer");
        this.speed = var1.getInteger("speed");
        this.power = var1.getInteger("spdpwr");
        this.propTicks = var1.getInteger("proptick");
        this.cond.readFromNBT(var1);
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

        if (!this.hasBlades)
        {
            this.windmillType = 0;
        }

        var1.setByte("wmt", this.windmillType);

        if (this.rayTrace != null)
        {
            var1.setByteArray("rays", this.rayTrace);
        }

        var1.setInteger("tracer", this.tracer);
        var1.setInteger("speed", this.speed);
        var1.setInteger("spdpwr", this.power);
        var1.setInteger("proptick", this.propTicks);
        this.cond.writeToNBT(var1);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        super.readFromPacket(var1);
        int var2 = var1.getByte();
        this.hasBlades = (var2 & 1) > 0;
        this.hasBrakes = (var2 & 2) > 0;
        this.windmillType = (byte)var1.getByte();
        this.speed = (int)var1.getUVLC();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        super.writeToPacket(var1);
        int var2 = (this.hasBlades ? 1 : 0) | (this.hasBrakes ? 2 : 0);
        var1.addByte(var2);
        var1.addByte(this.windmillType);
        var1.addUVLC((long)this.speed);
    }
}
