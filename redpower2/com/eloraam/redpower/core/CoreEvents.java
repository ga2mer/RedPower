package com.eloraam.redpower.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.oredict.OreDictionary;

public class CoreEvents
{
    @ForgeSubscribe
    public void toolDestroyed(PlayerDestroyItemEvent var1)
    {
        EntityPlayer var2 = var1.entityPlayer;
        ItemStack var3 = var1.original;
        int var4 = var2.inventory.currentItem;
        int var8 = var3.itemID;
        int var9 = var3.getItemDamage();
        ItemStack var6 = var2.inventory.getStackInSlot(var4 + 27);
        ItemStack var5 = var2.inventory.getStackInSlot(var4);

        if (var5 != null && var5.stackSize <= 0)
        {
            if (var6 != null)
            {
                if (var6.itemID == var8)
                {
                    if (!var6.getHasSubtypes() || var6.getItemDamage() == var9)
                    {
                        var2.inventory.setInventorySlotContents(var4, var6);
                        var2.inventory.setInventorySlotContents(var4 + 27, (ItemStack)null);

                        for (int var7 = 2; var7 > 0; --var7)
                        {
                            var5 = var2.inventory.getStackInSlot(var4 + 9 * var7);

                            if (var5 == null)
                            {
                                return;
                            }

                            if (var5.itemID != var8)
                            {
                                return;
                            }

                            if (var5.getHasSubtypes() && var5.getItemDamage() != var9)
                            {
                                return;
                            }

                            var2.inventory.setInventorySlotContents(var4 + 9 * var7 + 9, var5);
                            var2.inventory.setInventorySlotContents(var4 + 9 * var7, (ItemStack)null);
                        }
                    }
                }
            }
        }
    }

    @ForgeSubscribe
    public void oreRegister(OreDictionary.OreRegisterEvent var1)
    {
        CoreLib.registerOre(var1.Name, var1.Ore);
    }

    @ForgeSubscribe
    public void liquidRegister(LiquidDictionary.LiquidRegisterEvent var1)
    {
        PipeLib.registerForgeFluid(var1.Name, var1.Liquid);
    }
}
