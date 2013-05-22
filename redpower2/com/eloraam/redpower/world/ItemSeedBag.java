package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.WorldCoord;
import com.eloraam.redpower.world.ItemSeedBag$InventorySeedBag;
import com.eloraam.redpower.world.ItemSeedBag$SpiralSearch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class ItemSeedBag extends Item
{
    public ItemSeedBag(int var1)
    {
        super(var1);
        this.setMaxDamage(576);
        this.setMaxStackSize(1);
        //this.setTextureFile("/eloraam/world/worlditems1.png");
        this.setUnlocalizedName("rpSeedBag");
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public static IInventory getBagInventory(ItemStack var0)
    {
        return !(var0.getItem() instanceof ItemSeedBag) ? null : new ItemSeedBag$InventorySeedBag(var0);
    }

    public static boolean canAdd(IInventory var0, ItemStack var1)
    {
        if (!(var1.getItem() instanceof IPlantable))
        {
            return false;
        }
        else
        {
            for (int var2 = 0; var2 < var0.getSizeInventory(); ++var2)
            {
                ItemStack var3 = var0.getStackInSlot(var2);

                if (var3 != null && var3.stackSize != 0 && CoreLib.compareItemStack(var3, var1) != 0)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public static ItemStack getPlant(IInventory var0)
    {
        for (int var1 = 0; var1 < var0.getSizeInventory(); ++var1)
        {
            ItemStack var2 = var0.getStackInSlot(var1);

            if (var2 != null && var2.stackSize != 0)
            {
                return var2;
            }
        }

        return null;
    }

    private static void decrPlant(IInventory var0)
    {
        for (int var1 = 0; var1 < var0.getSizeInventory(); ++var1)
        {
            ItemStack var2 = var0.getStackInSlot(var1);

            if (var2 != null && var2.stackSize != 0)
            {
                var0.decrStackSize(var1, 1);
                break;
            }
        }
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack var1)
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */


    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (CoreLib.isClient(var2))
        {
            return var1;
        }
        else if (!var3.isSneaking())
        {
            return var1;
        }
        else
        {
            var3.openGui(RedPowerWorld.instance, 1, var2, 0, 0, 0);
            return var1;
        }
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        if (var7 != 1)
        {
            return false;
        }
        else if (CoreLib.isClient(var3))
        {
            return false;
        }
        else if (var2.isSneaking())
        {
            return false;
        }
        else
        {
            IInventory var11 = getBagInventory(var1);
            ItemSeedBag$SpiralSearch var12 = new ItemSeedBag$SpiralSearch(new WorldCoord(var4, var5, var6), 5);

            for (boolean var13 = false; var12.again(); var12.step())
            {
                int var14 = var3.getBlockId(var12.point.x, var12.point.y, var12.point.z);
                Block var15 = Block.blocksList[var14];

                if (var15 == null)
                {
                    if (!var13)
                    {
                        break;
                    }
                }
                else
                {
                    ItemStack var16 = getPlant(var11);

                    if (var16 == null || !(var16.getItem() instanceof IPlantable))
                    {
                        break;
                    }

                    IPlantable var17 = (IPlantable)var16.getItem();

                    if (var15 != null && var15.canSustainPlant(var3, var12.point.x, var12.point.y, var12.point.z, ForgeDirection.UP, var17))
                    {
                        int var18 = var3.getBlockId(var12.point.x, var12.point.y + 1, var12.point.z);

                        if (var18 != 0 && !Block.blocksList[var14].isAirBlock(var3, var12.point.x, var12.point.y + 1, var12.point.z))
                        {
                            if (!var13)
                            {
                                break;
                            }
                        }
                        else
                        {
                            var13 = true;
                            var3.setBlockMetadataWithNotify(var12.point.x, var12.point.y + 1, var12.point.z, var17.getPlantID(var3, var12.point.x, var12.point.y + 1, var12.point.z), var17.getPlantMetadata(var3, var12.point.x, var12.point.y + 1, var12.point.z));

                            if (!var2.capabilities.isCreativeMode)
                            {
                                decrPlant(var11);
                            }
                        }
                    }
                    else if (!var13)
                    {
                        break;
                    }
                }
            }

            return true;
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack var1, EntityPlayer var2, List var3, boolean var4)
    {
        if (var1.stackTagCompound != null && var1.getItemDamage() != 0)
        {
            IInventory var5 = getBagInventory(var1);

            for (int var6 = 0; var6 < var5.getSizeInventory(); ++var6)
            {
                ItemStack var7 = var5.getStackInSlot(var6);

                if (var7 != null && var7.stackSize != 0)
                {
                    var3.add(var7.getItem().getItemDisplayName(var7));
                    return;
                }
            }
        }
    }
}
