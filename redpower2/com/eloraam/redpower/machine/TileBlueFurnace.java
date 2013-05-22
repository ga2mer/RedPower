package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.base.TileAppliance;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.machine.TileBlueFurnace$1;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileBlueFurnace extends TileAppliance implements IInventory, ISidedInventory, IBluePowerConnectable
{
    BluePowerEndpoint cond = new TileBlueFurnace$1(this);
    private ItemStack[] contents = new ItemStack[2];
    public int cooktime = 0;
    public int ConMask = -1;

    public int getConnectableMask()
    {
        return 1073741823;
    }

    public int getConnectClass(int var1)
    {
        return 64;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return this.cond;
    }

    void updateLight()
    {
        this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
    }

    public int getExtendedID()
    {
        return 1;
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

            if (this.cond.getVoltage() < 60.0D)
            {
                if (this.Active && this.cond.Flow == 0)
                {
                    this.Active = false;
                    this.updateBlock();
                    this.updateLight();
                }
            }
            else
            {
                boolean var1 = this.canSmelt();

                if (var1)
                {
                    if (!this.Active)
                    {
                        this.Active = true;
                        this.updateBlock();
                        this.updateLight();
                    }

                    ++this.cooktime;
                    this.cond.drawPower(1000.0D);

                    if (this.cooktime >= 100)
                    {
                        this.cooktime = 0;
                        this.smeltItem();
                        this.onInventoryChanged();
                    }
                }
                else
                {
                    if (this.Active)
                    {
                        this.Active = false;
                        this.updateBlock();
                        this.updateLight();
                    }

                    this.cooktime = 0;
                }
            }
        }
    }

    boolean canSmelt()
    {
        if (this.contents[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.contents[0]);

            if (var1 == null)
            {
                return false;
            }
            else if (this.contents[1] == null)
            {
                return true;
            }
            else if (!this.contents[1].isItemEqual(var1))
            {
                return false;
            }
            else
            {
                int var2 = this.contents[1].stackSize + var1.stackSize;
                return var2 <= this.getInventoryStackLimit() && var2 <= var1.getMaxStackSize();
            }
        }
    }

    void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.contents[0]);

            if (this.contents[1] == null)
            {
                this.contents[1] = var1.copy();
            }
            else if (this.contents[1].isItemEqual(var1))
            {
                this.contents[1].stackSize += var1.stackSize;
            }

            if (this.contents[0].getItem().hasContainerItem())
            {
                this.contents[0] = new ItemStack(this.contents[0].getItem().getContainerItem());
            }
            else
            {
                --this.contents[0].stackSize;
            }

            if (this.contents[0].stackSize <= 0)
            {
                this.contents[0] = null;
            }
        }
    }

    int getCookScaled(int var1)
    {
        return this.cooktime * var1 / 100;
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
            var1.openGui(RedPowerMachine.instance, 3, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = (int)Math.floor((double)(var3.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    }

    public void onBlockRemoval()
    {
        for (int var1 = 0; var1 < 2; ++var1)
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
        return "Blulectric Furnace";
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
        int var3 = CoreLib.rotToSide(this.Rotation);
        return var2 == var3 ? 0 : (var2 == (var3 ^ 1) ? 1 : 0);
    }

    public int getSizeInventorySide(ForgeDirection var1)
    {
        int var2 = var1.ordinal();
        int var3 = CoreLib.rotToSide(this.Rotation);
        return var2 == var3 ? 1 : (var2 == (var3 ^ 1) ? 1 : 0);
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

        this.cooktime = var1.getShort("CookTime");
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
        var1.setShort("CookTime", (short)this.cooktime);
        this.cond.writeToNBT(var1);
    }
}
