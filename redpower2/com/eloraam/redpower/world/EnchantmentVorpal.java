package com.eloraam.redpower.world;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLiving;

public class EnchantmentVorpal extends Enchantment
{
    public EnchantmentVorpal(int var1, int var2)
    {
        super(var1, var2, EnumEnchantmentType.weapon);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int var1)
    {
        return 20 + 10 * (var1 - 1);
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int var1)
    {
        return this.getMinEnchantability(var1) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 4;
    }

    /**
     * Calculates de (magic) damage done by the enchantment on a living entity based on level and entity passed.
     */
    public int calcModifierLiving(int var1, EntityLiving var2)
    {
        return var2.worldObj.rand.nextInt(100) < 2 * var1 * var1 ? 100 : 0;
    }

    /**
     * Return the name of key in translation table of this enchantment.
     */
    public String getName()
    {
        return "enchantment.damage.vorpal";
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment var1)
    {
        return var1 != this;
    }
}
