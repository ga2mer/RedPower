package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IFrameLink;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileMultipart;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.machine.TileFrameMoving$FrameBlockAccess;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

public class TileFrameMoving extends TileMultipart implements IFrameLink, IHandlePackets
{
    TileFrameMoving$FrameBlockAccess frameblock = new TileFrameMoving$FrameBlockAccess(this);
    public int motorX;
    public int motorY;
    public int motorZ;
    public int movingBlockID = 0;
    public int movingBlockMeta = 0;
    public boolean movingCrate = false;
    public TileEntity movingTileEntity = null;
    public byte lastMovePos = 0;

    public boolean isFrameMoving()
    {
        return true;
    }

    public boolean canFrameConnectIn(int var1)
    {
        return true;
    }

    public boolean canFrameConnectOut(int var1)
    {
        return true;
    }

    public WorldCoord getFrameLinkset()
    {
        return new WorldCoord(this.motorX, this.motorY, this.motorZ);
    }

    public int getExtendedID()
    {
        return 1;
    }

    public void onBlockNeighborChange(int var1) {}

    public int getBlockID()
    {
        return RedPowerMachine.blockFrame.blockID;
    }

    public int getPartsMask()
    {
        return this.movingBlockID == 0 ? 0 : 536870912;
    }

    public int getSolidPartsMask()
    {
        return this.movingBlockID == 0 ? 0 : 536870912;
    }

    public boolean blockEmpty()
    {
        return false;
    }

    public void onHarvestPart(EntityPlayer var1, int var2) {}

    public void addHarvestContents(ArrayList var1)
    {
        super.addHarvestContents(var1);
    }

    public float getPartStrength(EntityPlayer var1, int var2)
    {
        BlockMachine var3 = RedPowerMachine.blockMachine;
        return 0.0F;
    }

    public void setPartBounds(BlockMultipart var1, int var2)
    {
        TileMotor var3 = (TileMotor)CoreLib.getTileEntity(this.worldObj, this.motorX, this.motorY, this.motorZ, TileMotor.class);

        if (var3 != null)
        {
            float var4 = var3.getMoveScaled();

            switch (var3.MoveDir)
            {
                case 0:
                    var1.setBlockBounds(0.0F, 0.0F - var4, 0.0F, 1.0F, 1.0F - var4, 1.0F);
                    break;

                case 1:
                    var1.setBlockBounds(0.0F, 0.0F + var4, 0.0F, 1.0F, 1.0F + var4, 1.0F);
                    break;

                case 2:
                    var1.setBlockBounds(0.0F, 0.0F, 0.0F - var4, 1.0F, 1.0F, 1.0F - var4);
                    break;

                case 3:
                    var1.setBlockBounds(0.0F, 0.0F, 0.0F + var4, 1.0F, 1.0F, 1.0F + var4);
                    break;

                case 4:
                    var1.setBlockBounds(0.0F - var4, 0.0F, 0.0F, 1.0F - var4, 1.0F, 1.0F);
                    break;

                case 5:
                    var1.setBlockBounds(0.0F + var4, 0.0F, 0.0F, 1.0F + var4, 1.0F, 1.0F);
            }
        }
    }

    public IBlockAccess getFrameBlockAccess()
    {
        return this.frameblock;
    }

    public void setContents(int var1, int var2, int var3, int var4, int var5, TileEntity var6)
    {
        this.movingBlockID = var1;
        this.movingBlockMeta = var2;
        this.motorX = var3;
        this.motorY = var4;
        this.motorZ = var5;
        this.movingTileEntity = var6;

        if (this.movingTileEntity != null)
        {
            if (RedPowerMachine.FrameAlwaysCrate)
            {
                this.movingCrate = true;
            }

            if (!(this.movingTileEntity instanceof IFrameSupport))
            {
                this.movingCrate = true;
            }
        }
    }

    public void doRefresh(IBlockAccess var1)
    {
        if (this.movingTileEntity instanceof IFrameSupport)
        {
            IFrameSupport var2 = (IFrameSupport)this.movingTileEntity;
            var2.onFrameRefresh(var1);
        }
    }

    public void dropBlock()
    {
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.movingBlockID, this.movingBlockMeta);

        if (this.movingTileEntity != null)
        {
            this.movingTileEntity.xCoord = this.xCoord;
            this.movingTileEntity.yCoord = this.yCoord;
            this.movingTileEntity.zCoord = this.zCoord;
            this.movingTileEntity.validate();
            this.worldObj.setBlockTileEntity(this.xCoord, this.yCoord, this.zCoord, this.movingTileEntity);
        }

        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        CoreLib.markBlockDirty(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        RedPowerLib.updateIndirectNeighbors(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.movingBlockID);
    }

    private AxisAlignedBB getAABB(int var1, float var2)
    {
        AxisAlignedBB var3 = AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1));

        switch (var1)
        {
            case 0:
                var3.minY -= (double)var2;
                var3.maxY -= (double)var2;
                break;

            case 1:
                var3.minY += (double)var2;
                var3.maxY += (double)var2;
                break;

            case 2:
                var3.minZ -= (double)var2;
                var3.maxZ -= (double)var2;
                break;

            case 3:
                var3.minZ += (double)var2;
                var3.maxZ += (double)var2;
                break;

            case 4:
                var3.minX -= (double)var2;
                var3.maxX -= (double)var2;
                break;

            case 5:
                var3.minX += (double)var2;
                var3.maxX += (double)var2;
        }

        return var3;
    }

    void pushEntities(TileMotor var1)
    {
        float var2 = (float)this.lastMovePos / 16.0F;
        float var3 = (float)var1.MovePos / 16.0F;
        this.lastMovePos = (byte)var1.MovePos;
        float var4 = 0.0F;
        float var5 = 0.0F;
        float var6 = 0.0F;

        switch (var1.MoveDir)
        {
            case 0:
                var5 -= var3 - var2;
                break;

            case 1:
                var5 += var3 - var2;
                break;

            case 2:
                var6 -= var3 - var2;
                break;

            case 3:
                var6 += var3 - var2;
                break;

            case 4:
                var4 -= var3 - var2;
                break;

            case 5:
                var4 += var3 - var2;
        }

        AxisAlignedBB var7 = this.getAABB(var1.MoveDir, var3);
        List var8 = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, var7);
        ArrayList var9 = new ArrayList();
        var9.addAll(var8);
        Iterator var10 = var9.iterator();

        while (var10.hasNext())
        {
            Object var11 = var10.next();
            Entity var12 = (Entity)var11;
            var12.moveEntity((double)var4, (double)var5, (double)var6);
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();
        TileMotor var1 = (TileMotor)CoreLib.getTileEntity(this.worldObj, this.motorX, this.motorY, this.motorZ, TileMotor.class);

        if (var1 != null && var1.MovePos >= 0)
        {
            this.pushEntities(var1);
        }
        else if (!CoreLib.isClient(this.worldObj))
        {
            this.dropBlock();
        }
    }

    /**
     * validates a tile entity
     */
    public void validate()
    {
        super.validate();

        if (this.movingTileEntity != null)
        {
            this.movingTileEntity.worldObj = this.worldObj;
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.motorX = var1.getInteger("mx");
        this.motorY = var1.getInteger("my");
        this.motorZ = var1.getInteger("mz");
        this.movingBlockID = var1.getInteger("mbid");
        this.movingBlockMeta = var1.getInteger("mbmd");
        this.lastMovePos = var1.getByte("lmp");

        if (var1.hasKey("mte"))
        {
            NBTTagCompound var2 = var1.getCompoundTag("mte");
            this.movingTileEntity = TileEntity.createAndLoadEntity(var2);
        }
        else
        {
            this.movingTileEntity = null;
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("mx", this.motorX);
        var1.setInteger("my", this.motorY);
        var1.setInteger("mz", this.motorZ);
        var1.setInteger("mbid", this.movingBlockID);
        var1.setInteger("mbmd", this.movingBlockMeta);
        var1.setByte("lmp", this.lastMovePos);

        if (this.movingTileEntity != null)
        {
            NBTTagCompound var2 = new NBTTagCompound();
            this.movingTileEntity.writeToNBT(var2);
            var1.setTag("mte", var2);
        }
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.motorX = (int)var1.getVLC();
        this.motorY = (int)var1.getVLC();
        this.motorZ = (int)var1.getVLC();
        this.movingBlockID = (int)var1.getUVLC();
        this.movingBlockMeta = var1.getByte();

        if (this.movingBlockID != 0)
        {
            this.movingTileEntity = Block.blocksList[this.movingBlockID].createTileEntity(this.worldObj, this.movingBlockMeta);

            if (this.movingTileEntity != null)
            {
                if (!(this.movingTileEntity instanceof IFrameSupport))
                {
                    this.movingCrate = true;
                    return;
                }

                this.movingTileEntity.worldObj = this.worldObj;
                this.movingTileEntity.xCoord = this.xCoord;
                this.movingTileEntity.yCoord = this.yCoord;
                this.movingTileEntity.zCoord = this.zCoord;
                IFrameSupport var2 = (IFrameSupport)this.movingTileEntity;
                var2.handleFramePacket(var1.getByteArray());
            }
        }
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addVLC((long)this.motorX);
        var1.addVLC((long)this.motorY);
        var1.addVLC((long)this.motorZ);
        var1.addUVLC((long)this.movingBlockID);
        var1.addByte(this.movingBlockMeta);

        if (this.movingTileEntity instanceof IFrameSupport)
        {
            IFrameSupport var2 = (IFrameSupport)this.movingTileEntity;
            var1.addByteArray(var2.getFramePacket());
        }
        else
        {
            var1.addByteArray(new byte[0]);
        }
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
