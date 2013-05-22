package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.ITubeConnectable;
import com.eloraam.redpower.core.MachineLib;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.TubeLib$InRouteFinder;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileRetriever$1;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileRetriever extends TileFilter implements IBluePowerConnectable
{
    BluePowerEndpoint cond = new TileRetriever$1(this);
    public int ConMask = -1;
    public byte select = 0;
    public byte mode = 0;

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

    public boolean tubeItemEnter(int var1, int var2, TubeItem var3)
    {
        if (var1 == (this.Rotation ^ 1) && var2 == 3)
        {
            if (!this.buffer.isEmpty())
            {
                return false;
            }
            else
            {
                if (this.filterMap == null)
                {
                    this.regenFilterMap();
                }

                if (this.filterMap.size() > 0 && !this.filterMap.containsKey(var3.item))
                {
                    return false;
                }
                else
                {
                    this.buffer.addNewColor(var3.item, this.color);
                    this.Delay = true;
                    this.updateBlock();
                    this.scheduleTick(5);
                    this.drainBuffer();
                    return true;
                }
            }
        }
        else
        {
            return var1 == this.Rotation && var2 == 2 ? super.tubeItemEnter(var1, var2, var3) : false;
        }
    }

    public boolean tubeItemCanEnter(int var1, int var2, TubeItem var3)
    {
        if (var1 == (this.Rotation ^ 1) && var2 == 3)
        {
            if (!this.buffer.isEmpty())
            {
                return false;
            }
            else
            {
                if (this.filterMap == null)
                {
                    this.regenFilterMap();
                }

                return this.filterMap.size() == 0 ? true : this.filterMap.containsKey(var3.item);
            }
        }
        else
        {
            return var1 == this.Rotation && var2 == 2 ? super.tubeItemCanEnter(var1, var2, var3) : false;
        }
    }

    private void stepSelect()
    {
        for (int var1 = 0; var1 < 9; ++var1)
        {
            ++this.select;

            if (this.select > 8)
            {
                this.select = 0;
            }

            ItemStack var2 = this.contents[this.select];

            if (var2 != null && var2.stackSize > 0)
            {
                return;
            }
        }

        this.select = 0;
    }

    protected boolean handleExtract(WorldCoord var1)
    {
        ITubeConnectable var2 = (ITubeConnectable)CoreLib.getTileEntity(this.worldObj, var1, ITubeConnectable.class);

        if (var2 != null && var2.canRouteItems())
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

                TubeLib$InRouteFinder var3 = new TubeLib$InRouteFinder(this.worldObj, this.filterMap);

                if (this.mode == 0)
                {
                    var3.setSubFilt(this.select);
                }

                int var4 = var3.find(new WorldCoord(this), 1 << (this.Rotation ^ 1));

                if (var4 < 0)
                {
                    return false;
                }
                else
                {
                    WorldCoord var5 = var3.getResultPoint();
                    IInventory var6 = MachineLib.getInventory(this.worldObj, var5);

                    if (var6 == null)
                    {
                        return false;
                    }
                    else
                    {
                        int var7 = var3.getResultSide();
                        int var8 = 0;
                        int var9 = var6.getSizeInventory();

                        if (var6 instanceof ISidedInventory)
                        {
                            ISidedInventory var10 = (ISidedInventory)var6;
                            var8 = var10.getStartInventorySide(ForgeDirection.getOrientation(var7));
                            var9 = var10.getSizeInventorySide(ForgeDirection.getOrientation(var7));
                        }

                        var5.step(var7);
                        TileTube var13 = (TileTube)CoreLib.getTileEntity(this.worldObj, var5, TileTube.class);

                        if (var13 == null)
                        {
                            return false;
                        }
                        else
                        {
                            ItemStack var11 = MachineLib.collectOneStack(var6, var8, var9, this.contents[var4]);

                            if (var11 == null)
                            {
                                return false;
                            }
                            else
                            {
                                TubeItem var12 = new TubeItem(var7, var11);
                                this.cond.drawPower((double)(25 * var11.stackSize));
                                var12.mode = 3;
                                var13.addTubeItem(var12);

                                if (this.mode == 0)
                                {
                                    this.stepSelect();
                                }

                                return true;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            return super.handleExtract(var1);
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

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;
        super.onBlockNeighborChange(var1);
    }

    public void onTileTick()
    {
        super.onTileTick();

        if (this.Delay)
        {
            this.Delay = false;
            this.updateBlock();
        }
    }

    protected void doSuck()
    {
        this.suckEntities(this.getSizeBox(2.55D, 5.05D, -0.95D));
    }

    protected boolean suckFilter(ItemStack var1)
    {
        if (this.cond.getVoltage() < 60.0D)
        {
            return false;
        }
        else if (!super.suckFilter(var1))
        {
            return false;
        }
        else
        {
            this.cond.drawPower((double)(25 * var1.stackSize));
            return true;
        }
    }

    protected int suckEntity(Object var1)
    {
        if (!(var1 instanceof EntityMinecart))
        {
            return super.suckEntity(var1);
        }
        else if (this.cond.getVoltage() < 60.0D)
        {
            return 0;
        }
        else
        {
            if (this.filterMap == null)
            {
                this.regenFilterMap();
            }

            EntityMinecartChest var2 = (EntityMinecartChest)var1;

            if (!MachineLib.emptyInventory(var2, 0, var2.getSizeInventory()))
            {
                return super.suckEntity(var1);
            }
            else
            {
                //List var3 = var2.getItemsDropped();
                //Iterator var4 = var3.iterator();

                /*while (var4.hasNext())
                {
                    ItemStack var5 = (ItemStack)var4.next();
                    this.buffer.addNewColor(var5, this.color);
                }*/

                var2.setDead();
                this.cond.drawPower(200.0D);
                return 2;
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
            var1.openGui(RedPowerMachine.instance, 7, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public int getExtendedID()
    {
        return 8;
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Retriever";
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.cond.readFromNBT(var1);
        this.mode = var1.getByte("mode");
        this.select = var1.getByte("sel");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        this.cond.writeToNBT(var1);
        var1.setByte("mode", this.mode);
        var1.setByte("sel", this.select);
    }
}
