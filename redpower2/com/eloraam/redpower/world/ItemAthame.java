package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerBase;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemAthame extends ItemSword
{
    public ItemAthame(int var1)
    {
        super(var1, EnumToolMaterial.EMERALD);
        this.setMaxDamage(100);
        //this.setIconCoord(0, 7);
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack var1, Block var2)
    {
        return 1.0F;
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity var1)
    {
        return !(var1 instanceof EntityEnderman) && !(var1 instanceof EntityDragon) ? 1 : 25;
    }

    public String getTextureFile()
    {
        return "/eloraam/world/worlditems1.png";
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack var1, ItemStack var2)
    {
        return var2.isItemEqual(RedPowerBase.itemIngotSilver);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 30;
    }
}
