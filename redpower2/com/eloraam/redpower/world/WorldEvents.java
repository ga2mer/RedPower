package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.MachineLib;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class WorldEvents
{
    @ForgeSubscribe
    public void onBonemeal(BonemealEvent var1)
    {
        if (var1.ID == RedPowerWorld.blockCrops.blockID)
        {
            int var2 = var1.world.getBlockMetadata(var1.X, var1.Y, var1.Z);

            if (var2 == 4 || var2 == 5)
            {
                return;
            }

            if (CoreLib.isClient(var1.world))
            {
                var1.setResult(Event.Result.ALLOW);
                return;
            }

            if (RedPowerWorld.blockCrops.fertilize(var1.world, var1.X, var1.Y, var1.Z))
            {
                var1.setResult(Event.Result.ALLOW);
            }
        }
    }

    @ForgeSubscribe
    public void onDeath(LivingDeathEvent var1)
    {
        if (var1.source instanceof EntityDamageSource)
        {
            EntityDamageSource var2 = (EntityDamageSource)var1.source;
            Entity var3 = var2.getEntity();

            if (var3 instanceof EntityPlayer)
            {
                EntityPlayer var4 = (EntityPlayer)var3;
                ItemStack var5 = var4.getCurrentEquippedItem();

                if (EnchantmentHelper.getEnchantmentLevel(RedPowerWorld.enchantVorpal.effectId, var5) != 0)
                {
                    if (var1.entityLiving.getHealth() <= -20)
                    {
                        if (var1.entityLiving instanceof EntitySkeleton)
                        {
                            EntitySkeleton var6 = (EntitySkeleton)var1.entityLiving;

                            if (var6.getSkeletonType() == 1)
                            {
                                return;
                            }

                            var1.entityLiving.entityDropItem(new ItemStack(Item.skull.itemID, 1, 0), 0.0F);
                        }
                        else if (var1.entityLiving instanceof EntityZombie)
                        {
                            var1.entityLiving.entityDropItem(new ItemStack(Item.skull.itemID, 1, 2), 0.0F);
                        }
                        else if (var1.entityLiving instanceof EntityPlayer)
                        {
                            ItemStack var7 = new ItemStack(Item.skull.itemID, 1, 3);
                            var7.setTagCompound(new NBTTagCompound());
                            var7.getTagCompound().setString("SkullOwner", var1.entityLiving.getEntityName());
                            var1.entityLiving.entityDropItem(var7, 0.0F);
                        }
                        else if (var1.entityLiving instanceof EntityCreeper)
                        {
                            var1.entityLiving.entityDropItem(new ItemStack(Item.skull.itemID, 1, 4), 0.0F);
                        }
                    }
                }
            }
        }
    }

    @ForgeSubscribe
    public void onPickupItem(EntityItemPickupEvent var1)
    {
        for (int var2 = 0; var2 < 9; ++var2)
        {
            ItemStack var3 = var1.entityPlayer.inventory.getStackInSlot(var2);

            if (var3 != null && var3.getItem() instanceof ItemSeedBag)
            {
                IInventory var4 = ItemSeedBag.getBagInventory(var3);

                if (var4 != null && ItemSeedBag.getPlant(var4) != null)
                {
                    ItemStack var5 = var1.item.getEntityItem();

                    if (ItemSeedBag.canAdd(var4, var5) && MachineLib.addToInventoryCore(var4, var5, 0, var4.getSizeInventory(), true))
                    {
                        var1.item.setDead();
                        var1.setResult(Event.Result.ALLOW);
                        return;
                    }
                }
            }
        }
    }
}
