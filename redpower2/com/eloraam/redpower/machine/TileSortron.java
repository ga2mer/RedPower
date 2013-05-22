package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.base.ItemScrewdriver;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.IRedbusConnectable;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.machine.TileSortron$1;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileSortron extends TileTranspose implements IBluePowerConnectable, IRedbusConnectable
{
    BluePowerEndpoint cond = new TileSortron$1(this);
    public int ConMask = -1;
    int rbaddr = 4;
    private int cmdDelay = 0;
    private int command = 0;
    private int itemSlot = 0;
    private int itemType = 0;
    private int itemDamage = 0;
    private int itemDamageMax = 0;
    private int itemQty = 0;
    private int itemColor = 0;
    private int itemInColor = 0;

    public int getConnectableMask()
    {
        return 1073741823;
    }

    public int getConnectClass(int var1)
    {
        return 67;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return this.cond;
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
        switch (var1)
        {
            case 0:
                return this.command & 255;

            case 1:
                return this.itemQty & 255;

            case 2:
                return this.itemSlot & 255;

            case 3:
                return this.itemSlot >> 8 & 255;

            case 4:
                return this.itemType & 255;

            case 5:
                return this.itemType >> 8 & 255;

            case 6:
                return this.itemType >> 16 & 255;

            case 7:
                return this.itemType >> 24 & 255;

            case 8:
                return this.itemDamage & 255;

            case 9:
                return this.itemDamage >> 8 & 255;

            case 10:
                return this.itemDamageMax & 255;

            case 11:
                return this.itemDamageMax >> 8 & 255;

            case 12:
                return this.itemColor & 255;

            case 13:
                return this.itemInColor & 255;

            default:
                return 0;
        }
    }

    public void rbWrite(int var1, int var2)
    {
        this.dirtyBlock();

        switch (var1)
        {
            case 0:
                this.command = var2;
                this.cmdDelay = 2;
                break;

            case 1:
                this.itemQty = var2;
                break;

            case 2:
                this.itemSlot = this.itemSlot & 65280 | var2;
                break;

            case 3:
                this.itemSlot = this.itemSlot & 255 | var2 << 8;
                break;

            case 4:
                this.itemType = this.itemType & -256 | var2;
                break;

            case 5:
                this.itemType = this.itemType & -65281 | var2 << 8;
                break;

            case 6:
                this.itemType = this.itemType & -16711681 | var2 << 16;
                break;

            case 7:
                this.itemType = this.itemType & 16777215 | var2 << 24;
                break;

            case 8:
                this.itemDamage = this.itemDamage & 65280 | var2;
                break;

            case 9:
                this.itemDamage = this.itemDamage & 255 | var2 << 8;
                break;

            case 10:
                this.itemDamageMax = this.itemDamageMax & 65280 | var2;
                break;

            case 11:
                this.itemDamageMax = this.itemDamageMax & 255 | var2 << 8;
                break;

            case 12:
                this.itemColor = var2;
                break;

            case 13:
                this.itemInColor = var2;
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
            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }

            this.cond.iterate();
            this.dirtyBlock();

            if (this.cmdDelay > 0 && --this.cmdDelay == 0)
            {
                this.processCommand();
            }

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

    public int getBlockID()
    {
        return RedPowerMachine.blockMachine2.blockID;
    }

    public int getExtendedID()
    {
        return 0;
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
        else
        {
            return false;
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;
    }

    public void onTileTick()
    {
        if (!CoreLib.isClient(this.worldObj))
        {
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
        }
    }

    public static int hashItem(ItemStack var0)
    {
        String var1 = var0.getItem().getUnlocalizedName();
        int var2;

        if (var1 == null)
        {
            var2 = Integer.valueOf(var0.itemID).hashCode();
        }
        else
        {
            var2 = var1.hashCode();
        }

        if (var0.getHasSubtypes())
        {
            var2 = Integer.valueOf(Integer.valueOf(var2).hashCode() ^ var0.getItemDamage()).intValue();
        }

        return var2;
    }

    void processCommand()
    {
        if (this.cond.getVoltage() < 60.0D)
        {
            this.cmdDelay = 20;
        }
        else
        {
            IInventory var1;
            ItemStack var2;

            switch (this.command)
            {
                case 0:
                    break;

                case 1:
                    var1 = this.getConnectedInventory(false);

                    if (var1 == null)
                    {
                        this.command = 255;
                    }
                    else
                    {
                        this.itemSlot = var1.getSizeInventory();
                        this.command = 0;
                    }

                    break;

                case 2:
                    var1 = this.getConnectedInventory(false);

                    if (var1 == null)
                    {
                        this.command = 255;
                    }
                    else if (this.itemSlot >= var1.getSizeInventory())
                    {
                        this.command = 255;
                    }
                    else
                    {
                        var2 = var1.getStackInSlot(this.itemSlot);

                        if (var2 != null && var2.stackSize != 0)
                        {
                            this.itemQty = var2.stackSize;
                            this.itemType = hashItem(var2);

                            if (var2.isItemStackDamageable())
                            {
                                this.itemDamage = var2.getItemDamage();
                                this.itemDamageMax = var2.getMaxDamage();
                            }
                            else
                            {
                                this.itemDamage = 0;
                                this.itemDamageMax = 0;
                            }

                            this.command = 0;
                        }
                        else
                        {
                            this.itemQty = 0;
                            this.itemType = 0;
                            this.itemDamage = 0;
                            this.itemDamageMax = 0;
                            this.command = 0;
                        }
                    }

                    break;

                case 3:
                    if (this.Active)
                    {
                        this.cmdDelay = 2;
                        return;
                    }

                    var1 = this.getConnectedInventory(false);

                    if (var1 == null)
                    {
                        this.command = 255;
                    }
                    else if (this.itemSlot >= var1.getSizeInventory())
                    {
                        this.command = 255;
                    }
                    else
                    {
                        var2 = var1.getStackInSlot(this.itemSlot);

                        if (var2 != null && var2.stackSize != 0)
                        {
                            int var3 = Math.min(this.itemQty, var2.stackSize);
                            this.itemQty = var3;

                            if (this.itemColor > 16)
                            {
                                this.itemColor = 0;
                            }

                            this.buffer.addNewColor(var1.decrStackSize(this.itemSlot, var3), this.itemColor);
                            this.cond.drawPower((double)(50 * var2.stackSize));
                            this.drainBuffer();
                            this.Active = true;
                            this.command = 0;
                            this.updateBlock();
                            this.scheduleTick(5);
                        }
                        else
                        {
                            this.itemQty = 0;
                            this.command = 0;
                        }
                    }

                    break;

                case 4:
                    if (this.itemQty == 0)
                    {
                        this.command = 0;
                    }

                    break;

                default:
                    this.command = 255;
            }
        }
    }

    protected boolean handleExtract(IInventory var1, int var2, int var3)
    {
        return false;
    }

    protected void addToBuffer(ItemStack var1)
    {
        if (this.itemColor > 16)
        {
            this.itemColor = 0;
        }

        this.buffer.addNewColor(var1, this.itemColor);
    }

    protected int suckEntity(Object var1)
    {
        if (var1 instanceof EntityItem)
        {
            EntityItem var2 = (EntityItem)var1;
            ItemStack var3 = var2.getEntityItem();

            if (var3.stackSize != 0 && !var2.isDead)
            {
                int var4 = var3.stackSize;

                if (!this.suckFilter(var3))
                {
                    return var4 == var3.stackSize ? 0 : 2;
                }
                else
                {
                    this.addToBuffer(var3);
                    var2.setDead();
                    return 1;
                }
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }

    protected boolean suckFilter(ItemStack var1)
    {
        if (this.command != 4)
        {
            return false;
        }
        else if (this.cond.getVoltage() < 60.0D)
        {
            return false;
        }
        else if (this.itemType != 0 && this.itemType != hashItem(var1))
        {
            return false;
        }
        else
        {
            boolean var2 = true;

            if (this.itemQty < var1.stackSize)
            {
                var2 = false;
                var1 = var1.splitStack(this.itemQty);

                if (this.itemColor > 16)
                {
                    this.itemColor = 0;
                }

                this.buffer.addNewColor(var1, this.itemColor);
            }

            this.itemQty -= var1.stackSize;

            if (this.itemQty == 0)
            {
                this.command = 0;
            }

            this.cond.drawPower((double)(50 * var1.stackSize));
            return var2;
        }
    }

    public boolean tubeItemEnter(int var1, int var2, TubeItem var3)
    {
        if (var1 == this.Rotation && var2 == 2)
        {
            return super.tubeItemEnter(var1, var2, var3);
        }
        else if (var1 == (this.Rotation ^ 1) && var2 == 1)
        {
            if (this.command != 4)
            {
                return false;
            }
            else if (this.cond.getVoltage() < 60.0D)
            {
                return false;
            }
            else if (this.itemType != 0 && this.itemType != hashItem(var3.item))
            {
                return false;
            }
            else if (this.itemInColor != 0 && this.itemInColor != var3.color)
            {
                return false;
            }
            else
            {
                boolean var4 = true;
                ItemStack var5 = var3.item;

                if (this.itemQty < var5.stackSize)
                {
                    var4 = false;
                    var5 = var5.splitStack(this.itemQty);
                }

                this.itemQty -= var5.stackSize;

                if (this.itemQty == 0)
                {
                    this.command = 0;
                }

                if (this.itemColor > 16)
                {
                    this.itemColor = 0;
                }

                this.buffer.addNewColor(var5, this.itemColor);
                this.cond.drawPower((double)(50 * var5.stackSize));
                this.drainBuffer();
                this.Active = true;
                this.updateBlock();
                this.scheduleTick(5);
                return var4;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean tubeItemCanEnter(int var1, int var2, TubeItem var3)
    {
        return var1 == this.Rotation && var2 == 2 ? true : (var1 == (this.Rotation ^ 1) && var2 == 1 ? (this.command != 4 ? false : (this.cond.getVoltage() < 60.0D ? false : (this.itemType != 0 && this.itemType != hashItem(var3.item) ? false : this.itemInColor == 0 || this.itemInColor == var3.color))) : false);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.cond.readFromNBT(var1);
        this.rbaddr = var1.getByte("rbaddr") & 255;
        this.cmdDelay = var1.getByte("cmddelay") & 255;
        this.command = var1.getByte("cmd") & 255;
        this.itemSlot = var1.getShort("itemslot") & 65535;
        this.itemType = var1.getInteger("itemtype");
        this.itemDamage = var1.getShort("itemdmg") & 65535;
        this.itemDamageMax = var1.getShort("itemdmgmax") & 65535;
        this.itemQty = var1.getByte("itemqty") & 255;
        this.itemInColor = var1.getByte("itemincolor") & 255;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.cond.writeToNBT(var1);
        var1.setByte("rbaddr", (byte)this.rbaddr);
        var1.setByte("cmddelay", (byte)this.cmdDelay);
        var1.setByte("cmd", (byte)this.command);
        var1.setShort("itemslot", (short)this.itemSlot);
        var1.setInteger("itemtype", this.itemType);
        var1.setShort("itemdmg", (short)this.itemDamage);
        var1.setShort("itemdmgmax", (short)this.itemDamageMax);
        var1.setByte("itemqty", (byte)this.itemQty);
        var1.setByte("itemcolor", (byte)this.itemColor);
        var1.setByte("itemincolor", (byte)this.itemInColor);
    }
}
