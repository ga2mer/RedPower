package com.eloraam.redpower.base;

import com.eloraam.redpower.RedPowerBase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeBag implements IRecipe
{
    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return 2;
    }

    public ItemStack getRecipeOutput()
    {
        return new ItemStack(RedPowerBase.itemBag, 1, 0);
    }

    private ItemStack findResult(InventoryCrafting var1)
    {
        ItemStack var2 = null;
        int var3 = -1;

        for (int var4 = 0; var4 < 3; ++var4)
        {
            for (int var5 = 0; var5 < 3; ++var5)
            {
                ItemStack var6 = var1.getStackInRowAndColumn(var4, var5);

                if (var6 != null)
                {
                    if (var6.getItem() instanceof ItemBag)
                    {
                        if (var2 != null)
                        {
                            return null;
                        }

                        var2 = var6;
                    }
                    else
                    {
                        if (var6.itemID != Item.dyePowder.itemID)
                        {
                            return null;
                        }

                        if (var3 >= 0)
                        {
                            return null;
                        }

                        var3 = 15 - var6.getItemDamage();
                    }
                }
            }
        }

        if (var2 != null && var3 >= 0)
        {
            if (var2.getItemDamage() == var3)
            {
                return null;
            }
            else
            {
                var2 = var2.copy();
                var2.setItemDamage(var3);
                return var2;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting var1)
    {
        return this.findResult(var1).copy();
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting var1, World var2)
    {
        return this.findResult(var1) != null;
    }
}
