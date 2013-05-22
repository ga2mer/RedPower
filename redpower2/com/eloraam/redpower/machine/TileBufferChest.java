package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.base.TileAppliance;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IRotatable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileBufferChest extends TileAppliance implements IInventory, ISidedInventory, IRotatable
{
    private ItemStack[] contents = new ItemStack[20];

    public int getExtendedID()
    {
        return 2;
    }

    public boolean canUpdate()
    {
        return false;
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
            var1.openGui(RedPowerMachine.instance, 4, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public int getFacing(EntityLiving var1)
    {
        int var2 = (int)Math.floor((double)(var1.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (Math.abs(var1.posX - (double)this.xCoord) < 2.0D && Math.abs(var1.posZ - (double)this.zCoord) < 2.0D)
        {
            double var3 = var1.posY + 1.82D - (double)var1.yOffset - (double)this.yCoord;

            if (var3 > 2.0D)
            {
                return 0;
            }

            if (var3 < 0.0D)
            {
                return 1;
            }
        }

        switch (var2)
        {
            case 0:
                return 3;

            case 1:
                return 4;

            case 2:
                return 2;

            default:
                return 5;
        }
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = this.getFacing(var3);
    }

    public void onBlockRemoval()
    {
        for (int var1 = 0; var1 < 20; ++var1)
        {
            ItemStack var2 = this.contents[var1];

            if (var2 != null && var2.stackSize > 0)
            {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
            }
        }
    }

    public int getPartMaxRotation(int var1, boolean var2)
    {
        return var2 ? 0 : 5;
    }

    public int getPartRotation(int var1, boolean var2)
    {
        return var2 ? 0 : this.Rotation;
    }

    public void setPartRotation(int var1, boolean var2, int var3)
    {
        if (!var2)
        {
            this.Rotation = var3;
            this.updateBlockChange();
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 20;
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
        return "Buffer";
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
        return (var2 ^ 1) == this.Rotation ? 0 : 4 * ((5 + (var2 ^ 1) - this.Rotation) % 6);
    }

    public int getSizeInventorySide(ForgeDirection var1)
    {
        int var2 = var1.ordinal();
        return (var2 ^ 1) == this.Rotation ? 20 : 4;
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
    }
}
