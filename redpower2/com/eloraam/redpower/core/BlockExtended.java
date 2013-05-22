package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockExtended extends BlockContainer
{
    private Class[] tileEntityMap = new Class[16];

    public BlockExtended(int var1, Material var2)
    {
        super(var1, var2);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isACube()
    {
        return false;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
    }

    public float getHardness()
    {
        return this.blockHardness;
    }

    public ArrayList getBlockDropped(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        ArrayList var7 = new ArrayList();
        TileExtended var8 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);

        if (var8 == null)
        {
            return var7;
        }
        else
        {
            var8.addHarvestContents(var7);
            return var7;
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return 0;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6) {}

    public boolean removeBlockByPlayer(World var1, EntityPlayer var2, int var3, int var4, int var5)
    {
        if (CoreLib.isClient(var1))
        {
            return true;
        }
        else
        {
            int var6 = var1.getBlockId(var3, var4, var5);
            int var7 = var1.getBlockMetadata(var3, var4, var5);
            Block var8 = Block.blocksList[var6];

            if (var8 == null)
            {
                return false;
            }
            else
            {
                if (var8.canHarvestBlock(var2, var7) && !var2.capabilities.isCreativeMode)
                {
                    ArrayList var9 = this.getBlockDropped(var1, var3, var4, var5, var7, EnchantmentHelper.getFortuneModifier(var2));
                    Iterator var10 = var9.iterator();

                    while (var10.hasNext())
                    {
                        ItemStack var11 = (ItemStack)var10.next();
                        CoreLib.dropItem(var1, var3, var4, var5, var11);
                    }
                }

                var1.setBlock(var3, var4, var5, 0);
                return true;
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        TileExtended var6 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);

        if (var6 == null)
        {
            var1.setBlock(var2, var3, var4, 0);
        }
        else
        {
            var6.onBlockNeighborChange(var5);
        }
    }

    public void onBlockPlacedUseful(World var1, int var2, int var3, int var4, int var5, EntityLiving var6, ItemStack var7)
    {
        TileExtended var8 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);

        if (var8 != null)
        {
            var8.onBlockPlaced(var7, var5, var6);
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        TileExtended var7 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);

        if (var7 != null)
        {
            var7.onBlockRemoval();
            super.breakBlock(var1, var2, var3, var4, var5, var6);
        }
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        TileExtended var6 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);
        return var6 == null ? 0 : var6.isBlockStrongPoweringTo(var5);
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        TileExtended var6 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);
        return var6 == null ? 0 : var6.isBlockWeakPoweringTo(var5);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        TileExtended var10 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);
        return var10 == null ? false : var10.onBlockActivated(var5);
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5)
    {
        TileExtended var6 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);

        if (var6 != null)
        {
            var6.onEntityCollidedWithBlock(var5);
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        TileExtended var5 = (TileExtended)CoreLib.getTileEntity(var1, var2, var3, var4, TileExtended.class);

        if (var5 != null)
        {
            AxisAlignedBB var6 = var5.getCollisionBoundingBox();

            if (var6 != null)
            {
                return var6;
            }
        }

        return super.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return RedPowerCore.customBlockModel;
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        RenderCustomBlock var7 = RenderLib.getRenderer(this.blockID, var6);

        if (var7 != null)
        {
            var7.randomDisplayTick(var1, var2, var3, var4, var5);
        }
    }

    public TileEntity getBlockEntity()
    {
        return null;
    }

    public void addTileEntityMapping(int var1, Class var2)
    {
        this.tileEntityMap[var1] = var2;
    }

    public void setItemName(int var1, String var2)
    {
        Item var3 = Item.itemsList[this.blockID];
        ((ItemExtended)var3).setMetaName(var1, "tile." + var2);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World var1)
    {
        return null;
    }

    public TileEntity createTileEntity(World var1, int var2)
    {
        try
        {
            return (TileEntity)this.tileEntityMap[var2].getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (Exception var5)
        {
            return null;
        }
    }

    public void addCollidingBlockToList(World var1, int var2, int var3, int var4, AxisAlignedBB var5, List var6, Entity var7) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
