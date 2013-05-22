package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.BluePowerEndpoint;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.FrameLib$FrameSolver;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.IFrameLink;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.IRotatable;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileExtended;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileMotor$1;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class TileMotor extends TileExtended implements IHandlePackets, IBluePowerConnectable, IRotatable, IFrameLink, IFrameSupport
{
    BluePowerEndpoint cond = new TileMotor$1(this);
    public int Rotation = 0;
    public int MoveDir = 4;
    public int MovePos = -1;
    public boolean Powered = false;
    public boolean Active = false;
    public boolean Charged = false;
    public int LinkSize = -1;
    public int ConMask = -1;

    public int getConnectableMask()
    {
        return 1073741823 ^ RedPowerLib.getConDirMask(this.Rotation >> 2 ^ 1);
    }

    public int getConnectClass(int var1)
    {
        return 65;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public WorldCoord getFrameLinkset()
    {
        return null;
    }

    public BluePowerConductor getBlueConductor(int var1)
    {
        return this.cond;
    }

    public int getPartMaxRotation(int var1, boolean var2)
    {
        return this.MovePos >= 0 ? 0 : (var2 ? 5 : 3);
    }

    public int getPartRotation(int var1, boolean var2)
    {
        return var2 ? this.Rotation >> 2 : this.Rotation & 3;
    }

    public void setPartRotation(int var1, boolean var2, int var3)
    {
        if (this.MovePos < 0)
        {
            if (var2)
            {
                this.Rotation = this.Rotation & 3 | var3 << 2;
            }
            else
            {
                this.Rotation = this.Rotation & -4 | var3 & 3;
            }

            this.updateBlockChange();
        }
    }

    public boolean isFrameMoving()
    {
        return false;
    }

    public boolean canFrameConnectIn(int var1)
    {
        return var1 != (this.Rotation >> 2 ^ 1);
    }

    public boolean canFrameConnectOut(int var1)
    {
        return var1 == (this.Rotation >> 2 ^ 1);
    }

    public int getExtendedID()
    {
        return 7;
    }

    public int getBlockID()
    {
        return RedPowerMachine.blockMachine.blockID;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (this.MovePos >= 0 && this.MovePos < 16)
        {
            ++this.MovePos;
            this.dirtyBlock();
        }

        if (!CoreLib.isClient(this.worldObj))
        {
            if (this.MovePos >= 0)
            {
                this.cond.drawPower((double)(100 + 10 * this.LinkSize));
            }

            if (this.MovePos >= 16)
            {
                this.dropFrame(true);
                this.MovePos = -1;
                this.Active = false;
                this.updateBlock();
            }

            if (this.ConMask < 0)
            {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }

            this.cond.iterate();
            this.dirtyBlock();

            if (this.MovePos < 0)
            {
                if (this.cond.getVoltage() < 60.0D)
                {
                    if (this.Charged && this.cond.Flow == 0)
                    {
                        this.Charged = false;
                        this.updateBlock();
                    }
                }
                else
                {
                    if (!this.Charged)
                    {
                        this.Charged = true;
                        this.updateBlock();
                    }
                }
            }
        }
    }

    private int getDriveSide()
    {
        short var1;

        switch (this.Rotation >> 2)
        {
            case 0:
                var1 = 13604;
                break;

            case 1:
                var1 = 13349;
                break;

            case 2:
                var1 = 20800;
                break;

            case 3:
                var1 = 16720;
                break;

            case 4:
                var1 = 8496;
                break;

            default:
                var1 = 12576;
        }

        int var2 = var1 >> ((this.Rotation & 3) << 2);
        var2 &= 7;
        return var2;
    }

    void pickFrame()
    {
        this.MoveDir = this.getDriveSide();
        WorldCoord var1 = new WorldCoord(this);
        FrameLib$FrameSolver var2 = new FrameLib$FrameSolver(this.worldObj, var1.coordStep(this.Rotation >> 2 ^ 1), var1, this.MoveDir);

        if (var2.solveLimit(RedPowerMachine.FrameLinkSize))
        {
            if (var2.addMoved())
            {
                this.LinkSize = var2.getFrameSet().size();
                this.MovePos = 0;
                this.Active = true;
                this.updateBlock();
                Iterator var3 = var2.getClearSet().iterator();
                WorldCoord var4;

                while (var3.hasNext())
                {
                    var4 = (WorldCoord)var3.next();
                    this.worldObj.setBlock(var4.x, var4.y, var4.z, 0);
                }

                var3 = var2.getFrameSet().iterator();

                while (var3.hasNext())
                {
                    var4 = (WorldCoord)var3.next();
                    int var5 = this.worldObj.getBlockId(var4.x, var4.y, var4.z);
                    int var6 = this.worldObj.getBlockMetadata(var4.x, var4.y, var4.z);
                    TileEntity var7 = this.worldObj.getBlockTileEntity(var4.x, var4.y, var4.z);

                    if (var7 != null)
                    {
                        this.worldObj.removeBlockTileEntity(var4.x, var4.y, var4.z);
                    }

                    boolean var8 = this.worldObj.isRemote;
                    this.worldObj.isRemote = true;
                    this.worldObj.setBlock(var4.x, var4.y, var4.z, RedPowerMachine.blockFrame.blockID);
                    this.worldObj.isRemote = var8;
                    TileFrameMoving var9 = (TileFrameMoving)CoreLib.getTileEntity(this.worldObj, var4, TileFrameMoving.class);

                    if (var9 != null)
                    {
                        var9.setContents(var5, var6, this.xCoord, this.yCoord, this.zCoord, var7);
                    }
                }

                var3 = var2.getFrameSet().iterator();

                while (var3.hasNext())
                {
                    var4 = (WorldCoord)var3.next();
                    this.worldObj.markBlockForUpdate(var4.x, var4.y, var4.z);
                    CoreLib.markBlockDirty(this.worldObj, var4.x, var4.y, var4.z);
                    TileFrameMoving var10 = (TileFrameMoving)CoreLib.getTileEntity(this.worldObj, var4, TileFrameMoving.class);

                    if (var10 != null && var10.movingTileEntity instanceof IFrameSupport)
                    {
                        IFrameSupport var11 = (IFrameSupport)var10.movingTileEntity;
                        var11.onFramePickup(var10.getFrameBlockAccess());
                    }
                }
            }
        }
    }

    void dropFrame(boolean var1)
    {
        WorldCoord var2 = new WorldCoord(this);
        FrameLib$FrameSolver var3 = new FrameLib$FrameSolver(this.worldObj, var2.coordStep(this.Rotation >> 2 ^ 1), var2, -1);

        if (var3.solve())
        {
            this.LinkSize = 0;
            var3.sort(this.MoveDir);
            Iterator var4 = var3.getFrameSet().iterator();
            WorldCoord var5;

            while (var4.hasNext())
            {
                var5 = (WorldCoord)var4.next();
                TileFrameMoving var6 = (TileFrameMoving)CoreLib.getTileEntity(this.worldObj, var5, TileFrameMoving.class);

                if (var6 != null)
                {
                    var6.pushEntities(this);
                    WorldCoord var7 = var5.copy();

                    if (var1)
                    {
                        var7.step(this.MoveDir);
                    }

                    if (var6.movingBlockID != 0)
                    {
                        boolean var8 = this.worldObj.isRemote;
                        this.worldObj.isRemote = true;
                        this.worldObj.setBlockMetadataWithNotify(var7.x, var7.y, var7.z, var6.movingBlockID, var6.movingBlockMeta);
                        this.worldObj.isRemote = var8;

                        if (var6.movingTileEntity != null)
                        {
                            var6.movingTileEntity.xCoord = var7.x;
                            var6.movingTileEntity.yCoord = var7.y;
                            var6.movingTileEntity.zCoord = var7.z;
                            var6.movingTileEntity.validate();
                            this.worldObj.setBlockTileEntity(var7.x, var7.y, var7.z, var6.movingTileEntity);
                        }
                    }

                    if (var1)
                    {
                        this.worldObj.setBlock(var5.x, var5.y, var5.z, 0);
                    }
                }
            }

            var4 = var3.getFrameSet().iterator();

            while (var4.hasNext())
            {
                var5 = (WorldCoord)var4.next();
                IFrameSupport var9 = (IFrameSupport)CoreLib.getTileEntity(this.worldObj, var5, IFrameSupport.class);

                if (var9 != null)
                {
                    var9.onFrameDrop();
                }

                this.worldObj.markBlockForUpdate(var5.x, var5.y, var5.z);
                CoreLib.markBlockDirty(this.worldObj, var5.x, var5.y, var5.z);
                RedPowerLib.updateIndirectNeighbors(this.worldObj, var5.x, var5.y, var5.z, this.worldObj.getBlockId(var5.x, var5.y, var5.z));
            }
        }
    }

    float getMoveScaled()
    {
        return (float)this.MovePos / 16.0F;
    }

    public void onBlockRemoval()
    {
        if (this.MovePos >= 0)
        {
            this.Active = false;
            this.dropFrame(false);
        }

        this.MovePos = -1;
    }

    public void onBlockNeighborChange(int var1)
    {
        this.ConMask = -1;

        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 16777215, 63))
        {
            if (this.Charged)
            {
                if (!this.Powered)
                {
                    if (this.MovePos < 0)
                    {
                        this.Powered = true;
                        this.updateBlockChange();

                        if (this.Powered)
                        {
                            this.pickFrame();
                        }
                    }
                }
            }
        }
        else if (this.Powered)
        {
            this.Powered = false;
            this.updateBlockChange();
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
        this.Rotation = this.getFacing(var3) << 2;
    }

    public byte[] getFramePacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 7;
        this.writeToPacket(var1);
        var1.headout.write(var1.subId);
        return var1.toByteArray();
    }

    public void handleFramePacket(byte[] var1) throws IOException
    {
        Packet211TileDesc var2 = new Packet211TileDesc(var1);
        var2.subId = var2.getByte();
        this.readFromPacket(var2);
    }

    public void onFrameRefresh(IBlockAccess var1) {}

    public void onFramePickup(IBlockAccess var1) {}

    public void onFrameDrop() {}

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.Rotation = var1.getByte("rot");
        this.MoveDir = var1.getByte("mdir");
        this.MovePos = var1.getByte("mpos");
        this.LinkSize = var1.getInteger("links");
        this.cond.readFromNBT(var1);
        byte var2 = var1.getByte("ps");
        this.Powered = (var2 & 1) > 0;
        this.Active = (var2 & 2) > 0;
        this.Charged = (var2 & 4) > 0;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("rot", (byte)this.Rotation);
        var1.setByte("mdir", (byte)this.MoveDir);
        var1.setByte("mpos", (byte)this.MovePos);
        var1.setInteger("links", this.LinkSize);
        this.cond.writeToNBT(var1);
        int var2 = (this.Powered ? 1 : 0) | (this.Active ? 2 : 0) | (this.Charged ? 4 : 0);
        var1.setByte("ps", (byte)var2);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Rotation = var1.getByte();
        this.MoveDir = var1.getByte();
        this.MovePos = var1.getByte() - 1;
        int var2 = var1.getByte();
        this.Powered = (var2 & 1) > 0;
        this.Active = (var2 & 2) > 0;
        this.Charged = (var2 & 4) > 0;
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.Rotation);
        var1.addByte(this.MoveDir);
        var1.addByte(this.MovePos + 1);
        int var2 = (this.Powered ? 1 : 0) | (this.Active ? 2 : 0) | (this.Charged ? 4 : 0);
        var1.addByte(var2);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 7;
        var1.xCoord = this.xCoord;
        var1.yCoord = this.yCoord;
        var1.zCoord = this.zCoord;
        this.writeToPacket(var1);
        var1.encode();
        return var1;
    }

    public void handlePacket(Packet211TileDesc var1)
    {
        try
        {
            if (var1.subId != 7)
            {
                return;
            }

            this.readFromPacket(var1);
        }
        catch (IOException var3)
        {
            ;
        }

        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
}
