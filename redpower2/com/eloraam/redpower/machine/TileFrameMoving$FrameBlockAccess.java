package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;

class TileFrameMoving$FrameBlockAccess implements IBlockAccess
{
    private final Vec3Pool vecpool;

    final TileFrameMoving this$0;

    public TileFrameMoving$FrameBlockAccess(TileFrameMoving var1)
    {
        this.this$0 = var1;
        this.vecpool = new Vec3Pool(300, 2000);
    }

    private TileFrameMoving getFrame(int var1, int var2, int var3)
    {
        TileFrameMoving var4 = (TileFrameMoving)CoreLib.getTileEntity(this.this$0.worldObj, var1, var2, var3, TileFrameMoving.class);
        return var4 == null ? null : (var4.motorX == this.this$0.motorX && var4.motorY == this.this$0.motorY && var4.motorZ == var4.motorZ ? var4 : null);
    }

    /**
     * Returns the block ID at coords x,y,z
     */
    public int getBlockId(int var1, int var2, int var3)
    {
        TileFrameMoving var4 = this.getFrame(var1, var2, var3);
        return var4 == null ? 0 : var4.movingBlockID;
    }

    /**
     * Returns the TileEntity associated with a given block in X,Y,Z coordinates, or null if no TileEntity exists
     */
    public TileEntity getBlockTileEntity(int var1, int var2, int var3)
    {
        TileFrameMoving var4 = this.getFrame(var1, var2, var3);
        return var4 == null ? null : var4.movingTileEntity;
    }

    /**
     * Any Light rendered on a 1.8 Block goes through here
     */
    public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4)
    {
        return this.this$0.worldObj.getLightBrightnessForSkyBlocks(var1, var2, var3, var4);
    }

    public float getBrightness(int var1, int var2, int var3, int var4)
    {
        return this.this$0.worldObj.getBrightness(var1, var2, var3, var4);
    }

    /**
     * Returns how bright the block is shown as which is the block's light value looked up in a lookup table (light
     * values aren't linear for brightness). Args: x, y, z
     */
    public float getLightBrightness(int var1, int var2, int var3)
    {
        return this.this$0.worldObj.getLightBrightness(var1, var2, var3);
    }

    /**
     * Returns the block metadata at coords x,y,z
     */
    public int getBlockMetadata(int var1, int var2, int var3)
    {
        TileFrameMoving var4 = this.getFrame(var1, var2, var3);
        return var4 == null ? 0 : var4.movingBlockMeta;
    }

    /**
     * Returns the block's material.
     */
    public Material getBlockMaterial(int var1, int var2, int var3)
    {
        int var4 = this.getBlockId(var1, var2, var3);
        return var4 == 0 ? Material.air : Block.blocksList[var4].blockMaterial;
    }

    /**
     * Returns true if the block at the specified coordinates is an opaque cube. Args: x, y, z
     */
    public boolean isBlockOpaqueCube(int var1, int var2, int var3)
    {
        Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
        return var4 == null ? false : var4.isOpaqueCube();
    }

    /**
     * Indicate if a material is a normal solid opaque cube.
     */
    public boolean isBlockNormalCube(int var1, int var2, int var3)
    {
        Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
        return var4 == null ? false : var4.isBlockNormalCube(this.this$0.worldObj, var1, var2, var3);
    }

    /**
     * Returns true if the block at the specified coordinates is empty
     */
    public boolean isAirBlock(int var1, int var2, int var3)
    {
        int var4 = this.getBlockId(var1, var2, var3);
        return var4 == 0 ? true : Block.blocksList[var4].isAirBlock(this.this$0.worldObj, var1, var2, var3);
    }

    /**
     * Returns current world height.
     */
    public int getHeight()
    {
        return this.this$0.worldObj.getHeight();
    }

    /**
     * Returns true if the block at the given coordinate has a solid (buildable) top surface.
     */
    public boolean doesBlockHaveSolidTopSurface(int var1, int var2, int var3)
    {
        return this.this$0.worldObj.isBlockSolidOnSide(var1, var2, var3, ForgeDirection.UP);
    }

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    public boolean extendedLevelsInChunkCache()
    {
        return false;
    }

    /**
     * Gets the biome for a given set of x/z coordinates
     */
    public BiomeGenBase getBiomeGenForCoords(int var1, int var2)
    {
        return this.this$0.worldObj.getBiomeGenForCoords(var1, var2);
    }

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    public int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4)
    {
        return 0;
    }

    /**
     * Return the Vec3Pool object for this world.
     */
    public Vec3Pool getWorldVec3Pool()
    {
        return this.vecpool;
    }
}
