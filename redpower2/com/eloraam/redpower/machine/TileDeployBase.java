package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.IConnectable;
import com.eloraam.redpower.core.IFrameLink;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class TileDeployBase extends TileMachine implements IFrameLink, IConnectable
{
    protected static EntityPlayer fakePlayer = null;

    public boolean isFrameMoving()
    {
        return false;
    }

    public boolean canFrameConnectIn(int var1)
    {
        return var1 != (this.Rotation ^ 1);
    }

    public boolean canFrameConnectOut(int var1)
    {
        return false;
    }

    public WorldCoord getFrameLinkset()
    {
        return null;
    }

    public int getConnectableMask()
    {
        return 1073741823 ^ RedPowerLib.getConDirMask(this.Rotation ^ 1);
    }

    public int getConnectClass(int var1)
    {
        return 0;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public int getBlockID()
    {
        return RedPowerMachine.blockMachine.blockID;
    }

    protected void initPlayer()
    {
        if (fakePlayer == null)
        {
            fakePlayer = new EntityPlayerFake(this.worldObj);
        }

        double var3 = (double)this.xCoord + 0.5D;
        double var5 = (double)this.yCoord - 1.1D;
        double var7 = (double)this.zCoord + 0.5D;
        float var1;
        float var2;

        switch (this.Rotation)
        {
            case 0:
                var1 = -90.0F;
                var2 = 0.0F;
                var5 += 0.51D;
                break;

            case 1:
                var1 = 90.0F;
                var2 = 0.0F;
                var5 -= 0.51D;
                break;

            case 2:
                var1 = 0.0F;
                var2 = 0.0F;
                var7 += 0.51D;
                break;

            case 3:
                var1 = 0.0F;
                var2 = 180.0F;
                var7 -= 0.51D;
                break;

            case 4:
                var1 = 0.0F;
                var2 = 270.0F;
                var3 += 0.51D;
                break;

            default:
                var1 = 0.0F;
                var2 = 90.0F;
                var3 -= 0.51D;
        }

        fakePlayer.worldObj = this.worldObj;
        fakePlayer.setLocationAndAngles(var3, var5, var7, var2, var1);
    }

    protected static Entity traceEntities(World var0, Entity var1, Vec3 var2, Vec3 var3)
    {
        AxisAlignedBB var4 = AxisAlignedBB.getBoundingBox(var2.xCoord, var2.yCoord, var2.zCoord, var2.xCoord, var2.yCoord, var2.zCoord);
        List var5 = var0.getEntitiesWithinAABBExcludingEntity(var1, var4.addCoord(var3.xCoord, var3.yCoord, var3.zCoord).expand(1.0D, 1.0D, 1.0D));
        Vec3 var6 = var2.addVector(var3.xCoord, var3.yCoord, var3.zCoord);
        Entity var7 = null;
        double var8 = 0.0D;

        for (int var10 = 0; var10 < var5.size(); ++var10)
        {
            Entity var11 = (Entity)var5.get(var10);

            if (var11.canBeCollidedWith())
            {
                float var12 = var11.getCollisionBorderSize();
                AxisAlignedBB var13 = var11.boundingBox.expand((double)var12, (double)var12, (double)var12);

                if (var13.isVecInside(var2))
                {
                    var7 = var11;
                    var8 = 0.0D;
                    break;
                }

                MovingObjectPosition var14 = var13.calculateIntercept(var2, var6);

                if (var14 != null)
                {
                    double var15 = var2.distanceTo(var14.hitVec);

                    if (var15 < var8 || var8 == 0.0D)
                    {
                        var7 = var11;
                        var8 = var15;
                    }
                }
            }
        }

        return var7;
    }

    protected boolean useOnEntity(Entity var1)
    {
        if (var1.interact(fakePlayer))
        {
            return true;
        }
        else
        {
            ItemStack var2 = fakePlayer.getCurrentEquippedItem();

            if (var2 != null && var1 instanceof EntityLiving)
            {
                int var3 = var2.stackSize;
                var2.interactWith((EntityLiving)var1);

                if (var2.stackSize != var3)
                {
                    return true;
                }
            }

            return false;
        }
    }

    protected boolean tryUseItemStack(ItemStack var1, int var2, int var3, int var4, int var5)
    {
        fakePlayer.inventory.currentItem = var5;

        if (var1.itemID != Item.dyePowder.itemID && var1.itemID != Item.minecartEmpty.itemID && var1.itemID != Item.minecartPowered.itemID && var1.itemID != Item.minecartCrate.itemID)
        {
            if (var1.getItem().onItemUseFirst(var1, fakePlayer, this.worldObj, var2, var3, var4, 1, 0.5F, 0.5F, 0.5F))
            {
                return true;
            }

            if (var1.getItem().onItemUse(var1, fakePlayer, this.worldObj, var2, var3 - 1, var4, 1, 0.5F, 0.5F, 0.5F))
            {
                return true;
            }
        }
        else if (var1.getItem().onItemUse(var1, fakePlayer, this.worldObj, var2, var3, var4, 1, 0.5F, 0.5F, 0.5F))
        {
            return true;
        }

        int var6 = var1.stackSize;
        ItemStack var7 = var1.useItemRightClick(this.worldObj, fakePlayer);

        if (var7 == var1 && (var7 == null || var7.stackSize == var6))
        {
            Vec3 var8 = fakePlayer.getLook(1.0F);
            var8.xCoord *= 2.5D;
            var8.yCoord *= 2.5D;
            var8.zCoord *= 2.5D;
            Vec3 var9 = Vec3.createVectorHelper((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D);
            Entity var10 = traceEntities(this.worldObj, fakePlayer, var9, var8);
            return var10 != null && this.useOnEntity(var10);
        }
        else
        {
            fakePlayer.inventory.setInventorySlotContents(var5, var7);
            return true;
        }
    }

    public abstract void enableTowards(WorldCoord var1);

    public void onBlockNeighborChange(int var1)
    {
        int var2 = this.getConnectableMask();

        if (!RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2, var2 >> 24))
        {
            if (this.Active)
            {
                this.scheduleTick(5);
            }
        }
        else if (!this.Active)
        {
            this.Active = true;
            this.updateBlock();
            WorldCoord var3 = new WorldCoord(this);
            var3.step(this.Rotation ^ 1);
            this.enableTowards(var3);
        }
    }

    public void onTileTick()
    {
        this.Active = false;
        this.updateBlock();
    }
}
