package com.eloraam.redpower.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemPlan extends Item
{
    public ItemPlan(int var1)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("planFull");
        this.setMaxStackSize(1);
    }


    public String getTextureFile()
    {
        return "/eloraam/base/items1.png";
    }

    @SideOnly(Side.CLIENT)
    public String getItemDisplayName(ItemStack var1)
    {
        if (var1.stackTagCompound == null)
        {
            return super.getItemDisplayName(var1);
        }
        else if (!var1.stackTagCompound.hasKey("result"))
        {
            return super.getItemDisplayName(var1);
        }
        else
        {
            NBTTagCompound var2 = var1.stackTagCompound.getCompoundTag("result");
            ItemStack var3 = ItemStack.loadItemStackFromNBT(var2);
            return var3.getItem().getItemDisplayName(var3) + " Plan";
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack var1, EntityPlayer var2, List var3, boolean var4)
    {
        if (var1.stackTagCompound != null)
        {
            NBTTagList var5 = var1.stackTagCompound.getTagList("requires");

            if (var5 != null)
            {
                HashMap var6 = new HashMap();

                for (int var7 = 0; var7 < var5.tagCount(); ++var7)
                {
                    NBTTagCompound var8 = (NBTTagCompound)var5.tagAt(var7);
                    ItemStack var9 = ItemStack.loadItemStackFromNBT(var8);
                    List var10 = Arrays.asList(new Integer[] {Integer.valueOf(var9.itemID), Integer.valueOf(var9.getItemDamage())});
                    Integer var11 = (Integer)var6.get(var10);

                    if (var11 == null)
                    {
                        var11 = Integer.valueOf(0);
                    }

                    var6.put(var10, Integer.valueOf(var11.intValue() + 1));
                }

                Iterator var12 = var6.entrySet().iterator();

                while (var12.hasNext())
                {
                    Entry var13 = (Entry)var12.next();
                    List var14 = (List)var13.getKey();
                    ItemStack var15 = new ItemStack(((Integer)var14.get(0)).intValue(), 1, ((Integer)var14.get(1)).intValue());
                    var3.add(var13.getValue() + " x " + var15.getItem().getItemDisplayName(var15));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack var1)
    {
        return EnumRarity.rare;
    }

    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    public boolean getShareTag()
    {
        return true;
    }
}
