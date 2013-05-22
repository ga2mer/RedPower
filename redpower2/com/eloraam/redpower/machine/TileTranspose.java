package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.ITubeConnectable;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TubeBuffer;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.WorldCoord;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileTranspose extends TileMachine implements ITubeConnectable
{
    TubeBuffer buffer = new TubeBuffer();

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
            if (this.Powered)
            {
                return false;
            }
            else if (!this.buffer.isEmpty())
            {
                return false;
            }
            else
            {
                this.addToBuffer(var3.item);
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
        return var1 == this.Rotation && var2 == 2 ? true : (var1 == (this.Rotation ^ 1) && var2 == 1 ? this.buffer.isEmpty() && !this.Powered : false);
    }

    public int tubeWeight(int var1, int var2)
    {
        return var1 == this.Rotation && var2 == 2 ? this.buffer.size() : 0;
    }

    protected void addToBuffer(ItemStack var1)
    {
        this.buffer.addNew(var1);
    }

    public boolean canSuck(int var1, int var2, int var3)
    {
        if (this.worldObj.isBlockSolidOnSide(var1, var2, var3, ForgeDirection.getOrientation(this.Rotation)))
        {
            return false;
        }
        else
        {
            TileEntity var4 = this.worldObj.getBlockTileEntity(var1, var2, var3);
            return var4 == null ? true : !(var4 instanceof IInventory) && !(var4 instanceof ITubeConnectable);
        }
    }

    public void onBlockNeighborChange(int var1)
    {
        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 16777215, 63))
        {
            if (!this.Powered)
            {
                this.Powered = true;
                this.dirtyBlock();

                if (!this.Active)
                {
                    this.Active = true;
                    WorldCoord var2 = new WorldCoord(this.xCoord, this.yCoord, this.zCoord);
                    var2.step(this.Rotation ^ 1);

                    if (this.canSuck(var2.x, var2.y, var2.z))
                    {
                        this.doSuck();
                        this.updateBlock();
                    }
                    else
                    {
                        if (this.handleExtract(var2))
                        {
                            this.updateBlock();
                        }
                    }
                }
            }
        }
        else
        {
            if (this.Active && !this.isTickScheduled())
            {
                this.scheduleTick(5);
            }

            this.Powered = false;
            this.dirtyBlock();
        }
    }

    protected IInventory getConnectedInventory(boolean var1)
    {
        WorldCoord var2 = new WorldCoord(this);
        var2.step(this.Rotation ^ 1);
        return MachineLib.getSideInventory(this.worldObj, var2, this.Rotation, var1);
    }

    protected boolean handleExtract(WorldCoord var1)
    {
        IInventory var2 = MachineLib.getInventory(this.worldObj, var1);

        if (var2 == null)
        {
            return false;
        }
        else
        {
            int var3 = 0;
            int var4 = var2.getSizeInventory();

            if (var2 instanceof ISidedInventory)
            {
                ISidedInventory var5 = (ISidedInventory)var2;
                var3 = var5.getStartInventorySide(ForgeDirection.getOrientation(this.Rotation));
                var4 = var5.getSizeInventorySide(ForgeDirection.getOrientation(this.Rotation));
            }

            return this.handleExtract(var2, var3, var4);
        }
    }

    protected boolean handleExtract(IInventory var1, int var2, int var3)
    {
        for (int var4 = var2; var4 < var2 + var3; ++var4)
        {
            ItemStack var5 = var1.getStackInSlot(var4);

            if (var5 != null && var5.stackSize != 0)
            {
                this.addToBuffer(var1.decrStackSize(var4, 1));
                this.drainBuffer();
                return true;
            }
        }

        return false;
    }

    protected AxisAlignedBB getSizeBox(double var1, double var3, double var5)
    {
        double var7 = (double)this.xCoord + 0.5D;
        double var9 = (double)this.yCoord + 0.5D;
        double var11 = (double)this.zCoord + 0.5D;

        switch (this.Rotation)
        {
            case 0:
                return AxisAlignedBB.getBoundingBox(var7 - var1, (double)this.yCoord - var5, var11 - var1, var7 + var1, (double)this.yCoord + var3, var11 + var1);

            case 1:
                return AxisAlignedBB.getBoundingBox(var7 - var1, (double)(this.yCoord + 1) - var3, var11 - var1, var7 + var1, (double)(this.yCoord + 1) + var5, var11 + var1);

            case 2:
                return AxisAlignedBB.getBoundingBox(var7 - var1, var9 - var1, (double)this.zCoord - var5, var7 + var1, var9 + var1, (double)this.zCoord + var3);

            case 3:
                return AxisAlignedBB.getBoundingBox(var7 - var1, var9 - var1, (double)(this.zCoord + 1) - var3, var7 + var1, var9 + var1, (double)(this.zCoord + 1) + var5);

            case 4:
                return AxisAlignedBB.getBoundingBox((double)this.xCoord - var5, var9 - var1, var11 - var1, (double)this.xCoord + var3, var9 + var1, var11 + var1);

            default:
                return AxisAlignedBB.getBoundingBox((double)(this.xCoord + 1) - var3, var9 - var1, var11 - var1, (double)(this.xCoord + 1) + var5, var9 + var1, var11 + var1);
        }
    }

    protected void doSuck()
    {
        this.suckEntities(this.getSizeBox(1.55D, 3.05D, -0.95D));
    }

    protected boolean suckFilter(ItemStack var1)
    {
        return true;
    }

    protected int suckEntity(Object var1)
    {
        if (var1 instanceof EntityItem)
        {
            EntityItem var4 = (EntityItem)var1;
            ItemStack var3 = var4.getEntityItem();

            if (var3.stackSize != 0 && !var4.isDead)
            {
                if (!this.suckFilter(var3))
                {
                    return 0;
                }
                else
                {
                    this.addToBuffer(var3);
                    var4.setDead();
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
            if (var1 instanceof EntityMinecartContainer)
            {
                if (this.Active)
                {
                    return 0;
                }

                EntityMinecartContainer var2 = (EntityMinecartContainer)var1;

                if (this.handleExtract(var2, 0, var2.getSizeInventory()))
                {
                    return 2;
                }
            }

            return 0;
        }
    }

    protected void suckEntities(AxisAlignedBB var1)
    {
        boolean var2 = false;
        List var3 = this.worldObj.getEntitiesWithinAABB(Entity.class, var1);
        Iterator var4 = var3.iterator();

        while (var4.hasNext())
        {
            Object var5 = var4.next();
            int var6 = this.suckEntity(var5);

            if (var6 != 0)
            {
                var2 = true;

                if (var6 == 2)
                {
                    break;
                }
            }
        }

        if (var2)
        {
            if (!this.Active)
            {
                this.Active = true;
                this.updateBlock();
            }

            this.drainBuffer();
            this.scheduleTick(5);
        }
    }

    public boolean stuffCart(ItemStack var1)
    {
        WorldCoord var2 = new WorldCoord(this);
        var2.step(this.Rotation);
        Block var3 = Block.blocksList[this.worldObj.getBlockId(var2.x, var2.y, var2.z)];

        if (!(var3 instanceof BlockRail))
        {
            return false;
        }
        else
        {
            List var4 = this.worldObj.getEntitiesWithinAABB(EntityMinecartContainer.class, this.getSizeBox(0.8D, 0.05D, 1.05D));
            Iterator var5 = var4.iterator();

            while (var5.hasNext())
            {
                Object var6 = var5.next();

                if (var6 instanceof EntityMinecartContainer)
                {
                    EntityMinecartContainer var7 = (EntityMinecartContainer)var6;

                    if (MachineLib.addToInventoryCore(var7, var1, 0, var7.getSizeInventory(), true))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public void drainBuffer()
    {
        while (true)
        {
            if (!this.buffer.isEmpty())
            {
                TubeItem var1 = this.buffer.getLast();

                if (this.stuffCart(var1.item))
                {
                    this.buffer.pop();
                    continue;
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

    public AxisAlignedBB getCollisionBoundingBox()
    {
        return this.getSizeBox(0.5D, 0.95D, 0.0D);
    }

    public void onEntityCollidedWithBlock(Entity var1)
    {
        if (!CoreLib.isClient(this.worldObj))
        {
            if (!this.Powered)
            {
                if (this.buffer.isEmpty())
                {
                    this.suckEntities(this.getSizeBox(0.55D, 1.05D, -0.95D));
                }
            }
        }
    }

    public void onBlockRemoval()
    {
        this.buffer.onRemove(this);
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
                if (!this.Powered)
                {
                    this.Active = false;
                    this.updateBlock();
                }
            }
        }
    }

    public int getExtendedID()
    {
        return 2;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.buffer.readFromNBT(var1);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.buffer.writeToNBT(var1);
    }
}
