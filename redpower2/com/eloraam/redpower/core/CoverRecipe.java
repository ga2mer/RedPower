package com.eloraam.redpower.core;

import com.eloraam.redpower.base.ItemHandsaw;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class CoverRecipe implements IRecipe
{
    private static ItemStack newCover(int var0, int var1, int var2)
    {
        return new ItemStack(CoverLib.blockCoverPlate, var0, var1 << 8 | var2);
    }

    private ItemStack getSawRecipe(InventoryCrafting var1, ItemStack var2, int var3, ItemStack var4, int var5)
    {
        int var6 = var3 & 15;
        int var7 = var3 >> 4;
        int var8 = var5 & 15;
        int var9 = var5 >> 4;
        boolean var10 = true;
        int var11 = -1;
        int var13;

        if (var4.itemID == CoverLib.blockCoverPlate.blockID)
        {
            var11 = var4.getItemDamage();
            var13 = var11 & 255;
            var11 >>= 8;
        }
        else
        {
            Integer var12 = CoverLib.getMaterial(var4);

            if (var12 == null)
            {
                return null;
            }

            var13 = var12.intValue();
        }

        ItemHandsaw var14 = (ItemHandsaw)var2.getItem();

        if (var14.getSharpness() < CoverLib.getHardness(var13))
        {
            return null;
        }
        else if (var6 == var8 && (var7 == var9 + 1 || var7 == var9 - 1))
        {
            switch (var11)
            {
                case -1:
                    return newCover(2, 17, var13);

                case 16:
                    return newCover(2, 0, var13);

                case 17:
                    return newCover(2, 16, var13);

                case 25:
                    return newCover(2, 24, var13);

                case 26:
                    return newCover(2, 25, var13);

                case 29:
                    return newCover(2, 27, var13);

                case 33:
                    return newCover(2, 31, var13);

                default:
                    return null;
            }
        }
        else if (var7 == var9 && (var6 == var8 + 1 || var6 == var8 - 1))
        {
            switch (var11)
            {
                case 0:
                    return newCover(2, 21, var13);

                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 18:
                case 19:
                case 20:
                case 24:
                case 25:
                case 26:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                default:
                    return null;

                case 16:
                    return newCover(2, 22, var13);

                case 17:
                    return newCover(2, 23, var13);

                case 21:
                    return newCover(2, 18, var13);

                case 22:
                    return newCover(2, 19, var13);

                case 23:
                    return newCover(2, 20, var13);

                case 27:
                    return newCover(2, 39, var13);

                case 28:
                    return newCover(2, 40, var13);

                case 29:
                    return newCover(2, 41, var13);

                case 30:
                    return newCover(2, 42, var13);

                case 39:
                    return newCover(2, 35, var13);

                case 40:
                    return newCover(2, 36, var13);

                case 41:
                    return newCover(2, 37, var13);

                case 42:
                    return newCover(2, 38, var13);
            }
        }
        else
        {
            return null;
        }
    }

    private ItemStack getColumnRecipe(ItemStack var1)
    {
        if (var1.itemID != CoverLib.blockCoverPlate.blockID)
        {
            return null;
        }
        else
        {
            int var2 = var1.getItemDamage();
            int var3 = var2 & 255;
            var2 >>= 8;

            switch (var2)
            {
                case 22:
                    return newCover(1, 43, var3);

                case 23:
                    return newCover(1, 44, var3);

                case 41:
                    return newCover(1, 45, var3);

                case 43:
                    return newCover(1, 22, var3);

                case 44:
                    return newCover(1, 23, var3);

                case 45:
                    return newCover(1, 41, var3);

                default:
                    return null;
            }
        }
    }

    private ItemStack getMergeRecipe(int var1, int var2, int var3)
    {
        int var4 = var1 >> 20;
        var1 &= 255;

        switch (var4)
        {
            case 0:
                switch (var2)
                {
                    case 2:
                        return newCover(1, 16, var1);

                    case 3:
                        return newCover(1, 27, var1);

                    case 4:
                        return newCover(1, 17, var1);

                    case 5:
                        return newCover(1, 28, var1);

                    case 6:
                        return newCover(1, 29, var1);

                    case 7:
                        return newCover(1, 30, var1);

                    case 8:
                        return CoverLib.getItemStack(var1);

                    default:
                        return null;
                }

            case 1:
                switch (var2)
                {
                    case 2:
                        return newCover(1, 25, var1);

                    case 3:
                        return newCover(1, 31, var1);

                    case 4:
                        return newCover(1, 26, var1);

                    case 5:
                        return newCover(1, 32, var1);

                    case 6:
                        return newCover(1, 33, var1);

                    case 7:
                        return newCover(1, 34, var1);

                    case 8:
                        return CoverLib.getItemStack(var1);

                    default:
                        return null;
                }

            case 16:
                switch (var2)
                {
                    case 2:
                        return newCover(1, 0, var1);

                    case 4:
                        return newCover(1, 16, var1);

                    case 8:
                        return newCover(1, 17, var1);

                    case 16:
                        return CoverLib.getItemStack(var1);

                    default:
                        return null;
                }

            case 32:
                if (var3 == 2)
                {
                    switch (var2)
                    {
                        case 2:
                            return newCover(1, 21, var1);

                        case 4:
                            return newCover(1, 22, var1);

                        case 8:
                            return newCover(1, 23, var1);
                    }
                }
                else
                {
                    switch (var2)
                    {
                        case 4:
                            return newCover(1, 0, var1);

                        case 8:
                            return newCover(1, 16, var1);

                        case 16:
                            return newCover(1, 17, var1);

                        case 32:
                            return CoverLib.getItemStack(var1);
                    }
                }
        }

        return null;
    }

    private ItemStack getHollowRecipe(int var1)
    {
        int var2 = var1 >> 8 & 255;
        var1 &= 255;

        switch (var2)
        {
            case 0:
                return newCover(8, 24, var1);

            case 16:
                return newCover(8, 25, var1);

            case 17:
                return newCover(8, 26, var1);

            case 27:
                return newCover(8, 31, var1);

            case 28:
                return newCover(8, 32, var1);

            case 29:
                return newCover(8, 33, var1);

            case 30:
                return newCover(8, 34, var1);

            default:
                return null;
        }
    }

    private int getMicroClass(ItemStack var1)
    {
        if (var1.itemID != CoverLib.blockCoverPlate.blockID)
        {
            return -1;
        }
        else
        {
            int var2 = var1.getItemDamage();
            return CoverLib.damageToCoverData(var2);
        }
    }

    private ItemStack findResult(InventoryCrafting var1)
    {
        ItemStack var2 = null;
        ItemStack var3 = null;
        boolean var4 = false;
        boolean var5 = true;
        boolean var6 = true;
        int var7 = 0;
        int var8 = 0;
        int var9 = -1;
        int var10 = 0;
        int var11 = 0;

        for (int var12 = 0; var12 < 3; ++var12)
        {
            for (int var13 = 0; var13 < 3; ++var13)
            {
                ItemStack var14 = var1.getStackInRowAndColumn(var12, var13);

                if (var14 != null)
                {
                    if (var14.getItem() instanceof ItemHandsaw)
                    {
                        if (var2 != null)
                        {
                            var4 = true;
                        }
                        else
                        {
                            var2 = var14;
                            var7 = var12 + var13 * 16;
                        }
                    }
                    else if (var3 == null)
                    {
                        var3 = var14;
                        var8 = var12 + var13 * 16;
                        var9 = this.getMicroClass(var14);

                        if (var9 >= 0)
                        {
                            var10 += var9 >> 16 & 15;
                        }
                        else
                        {
                            var5 = false;
                        }

                        var11 = 1;
                    }
                    else
                    {
                        var4 = true;

                        if (var5)
                        {
                            int var15 = this.getMicroClass(var14);

                            if (((var15 ^ var9) & -1048321) != 0)
                            {
                                var5 = false;
                            }
                            else
                            {
                                if (var15 != var9)
                                {
                                    var6 = false;
                                }

                                var10 += var15 >> 16 & 15;
                                ++var11;
                            }
                        }
                    }
                }
            }
        }

        if (var2 != null && var3 != null && !var4)
        {
            return this.getSawRecipe(var1, var2, var7, var3, var8);
        }
        else if (var2 == null && var3 != null && !var4)
        {
            return this.getColumnRecipe(var3);
        }
        else if (var5 && var4 && var2 == null)
        {
            if (var11 == 8 && var6 && var1.getStackInRowAndColumn(1, 1) == null && var9 >> 20 == 0)
            {
                return this.getHollowRecipe(var9);
            }
            else
            {
                return this.getMergeRecipe(var9, var10, var11);
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting var1, World var2)
    {
        return this.findResult(var1) != null;
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return 9;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting var1)
    {
        return this.findResult(var1).copy();
    }

    public ItemStack getRecipeOutput()
    {
        return new ItemStack(CoverLib.blockCoverPlate, 1, 0);
    }
}
