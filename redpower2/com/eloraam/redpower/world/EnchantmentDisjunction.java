package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.ItemStack;

public class EnchantmentDisjunction extends Enchantment
{
    public EnchantmentDisjunction(int var1, int var2)
    {
        super(var1, var2, EnumEnchantmentType.weapon);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int var1)
    {
        return 5 + 8 * var1;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int var1)
    {
        return this.getMinEnchantability(var1) + 20;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 5;
    }

    /**
     * Calculates de (magic) damage done by the enchantment on a living entity based on level and entity passed.
     */
    public int calcModifierLiving(int var1, EntityLiving var2)
    {
        return !(var2 instanceof EntityEnderman) && !(var2 instanceof EntityDragon) ? 0 : var1 * 6;
    }

    /**
     * Return the name of key in translation table of this enchantment.
     */
    public String getName()
    {
        return "enchantment.damage.disjunction";
    }

    public boolean canEnchantItem(ItemStack var1)
    {
        return var1.itemID == RedPowerWorld.itemAthame.itemID;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment var1)
    {
        return var1 == this ? false : !(var1 instanceof EnchantmentDamage);
    }
}
