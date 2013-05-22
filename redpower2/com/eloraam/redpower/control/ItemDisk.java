package com.eloraam.redpower.control;

import com.eloraam.redpower.core.CoreLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDisk extends Item
{
    public ItemDisk(int var1)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    public String getItemNameIS(ItemStack var1)
    {
        switch (var1.getItemDamage())
        {
            case 0:
                return "item.disk";

            case 1:
                return "item.disk.forth";

            case 2:
                return "item.disk.forthxp";

            default:
                return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public String getItemDisplayName(ItemStack var1)
    {
        return var1.stackTagCompound == null ? super.getItemDisplayName(var1) : (!var1.stackTagCompound.hasKey("label") ? super.getItemDisplayName(var1) : var1.stackTagCompound.getString("label"));
    }

    @SideOnly(Side.CLIENT)

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack var1)
    {
        return var1.getItemDamage() >= 1 ? EnumRarity.uncommon : EnumRarity.common;
    }

    public boolean onItemUseFirst(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        if (CoreLib.isClient(var3))
        {
            return false;
        }
        else
        {
            TileDiskDrive var11 = (TileDiskDrive)CoreLib.getTileEntity(var3, var4, var5, var6, TileDiskDrive.class);

            if (var11 == null)
            {
                return false;
            }
            else if (var11.setDisk(var1.copy()))
            {
                var1.stackSize = 0;
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    public boolean getShareTag()
    {
        return true;
    }

    public String getTextureFile()
    {
        return "/eloraam/control/control1.png";
    }

    public void addCreativeItems(ArrayList var1)
    {
        for (int var2 = 0; var2 <= 1; ++var2)
        {
            var1.add(new ItemStack(this, 1, var2));
        }
    }
}
