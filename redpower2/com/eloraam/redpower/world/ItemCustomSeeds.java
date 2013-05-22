package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class ItemCustomSeeds extends Item implements IPlantable
{
    public ItemCustomSeeds(int var1)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMaterials);
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
        else
        {
            int var11 = var3.getBlockId(var4, var5, var6);
            Block var12 = Block.blocksList[var11];

            if (var12 == null)
            {
                return false;
            }
            else if (var12.canSustainPlant(var3, var4, var5, var6, ForgeDirection.UP, this) && var3.isAirBlock(var4, var5 + 1, var6))
            {
                var3.setBlockMetadataWithNotify(var4, var5 + 1, var6, RedPowerWorld.blockCrops.blockID, 0);
                --var1.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public String getItemNameIS(ItemStack var1)
    {
        switch (var1.getItemDamage())
        {
            case 0:
                return "item.seedFlax";

            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public void addCreativeItems(ArrayList var1)
    {
        for (int var2 = 0; var2 <= 0; ++var2)
        {
            var1.add(new ItemStack(this, 1, var2));
        }
    }

    public String getTextureFile()
    {
        return "/eloraam/world/worlditems1.png";
    }

    public EnumPlantType getPlantType(World var1, int var2, int var3, int var4)
    {
        return EnumPlantType.Crop;
    }

    public int getPlantID(World var1, int var2, int var3, int var4)
    {
        return RedPowerWorld.blockCrops.blockID;
    }

    public int getPlantMetadata(World var1, int var2, int var3, int var4)
    {
        return 0;
    }
}
