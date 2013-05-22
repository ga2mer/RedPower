package com.eloraam.redpower.machine;

import com.eloraam.redpower.base.ItemScrewdriver;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IChargeable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSonicDriver extends ItemScrewdriver implements IChargeable
{
    public ItemSonicDriver(int var1)
    {
        super(var1);
        this.setMaxDamage(400);
        this.setNoRepair();
    }

    public boolean onItemUseFirst(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return CoreLib.isClient(var3) ? false : (var1.getItemDamage() == var1.getMaxDamage() ? false : super.onItemUseFirst(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10));
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity var1)
    {
        return 1;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3)
    {
        return false;
    }
}
