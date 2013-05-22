package com.eloraam.redpower.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CraftLib
{
    static List alloyRecipes = new ArrayList();
    public static HashSet damageOnCraft = new HashSet();
    public static HashMap damageContainer = new HashMap();

    public static void addAlloyResult(ItemStack var0, Object ... var1)
    {
        alloyRecipes.add(Arrays.asList(new Object[] {var1, var0}));
    }

    public static void addOreRecipe(ItemStack var0, Object ... var1)
    {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(var0, new Object[] {Boolean.valueOf(true), var1}));
    }

    public static void addShapelessOreRecipe(ItemStack var0, Object ... var1)
    {
        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(var0, var1));
    }

    public static boolean isOreClass(ItemStack var0, String var1)
    {
        ArrayList var2 = OreDictionary.getOres(var1);
        Iterator var3 = var2.iterator();
        ItemStack var4;

        do
        {
            if (!var3.hasNext())
            {
                return false;
            }

            var4 = (ItemStack)var3.next();
        }
        while (!var4.isItemEqual(var0));

        return true;
    }

    public static ItemStack getAlloyResult(ItemStack[] var0, int var1, int var2, boolean var3)
    {
        Iterator var4 = alloyRecipes.iterator();
        label136:

        while (var4.hasNext())
        {
            List var5 = (List)var4.next();
            Object[] var6 = var5.toArray();
            Object[] var7 = (Object[])((Object[])var6[0]);
            Object[] var8 = var7;
            int var9 = var7.length;
            int var10;
            Object var11;
            ItemStack var12;
            int var13;
            int var14;
            OreStack var15;

            for (var10 = 0; var10 < var9; ++var10)
            {
                var11 = var8[var10];

                if (var11 instanceof ItemStack)
                {
                    var12 = (ItemStack)var11;
                    var13 = var12.stackSize;

                    for (var14 = var1; var14 < var2; ++var14)
                    {
                        if (var0[var14] != null)
                        {
                            if (var0[var14].isItemEqual(var12))
                            {
                                var13 -= var0[var14].stackSize;
                            }

                            if (var13 <= 0)
                            {
                                break;
                            }
                        }
                    }

                    if (var13 > 0)
                    {
                        continue label136;
                    }
                }
                else if (var11 instanceof OreStack)
                {
                    var15 = (OreStack)var11;
                    var13 = var15.quantity;

                    for (var14 = var1; var14 < var2; ++var14)
                    {
                        if (var0[var14] != null)
                        {
                            if (isOreClass(var0[var14], var15.material))
                            {
                                var13 -= var0[var14].stackSize;
                            }

                            if (var13 <= 0)
                            {
                                break;
                            }
                        }
                    }

                    if (var13 > 0)
                    {
                        continue label136;
                    }
                }
            }

            if (var3)
            {
                var8 = var7;
                var9 = var7.length;

                for (var10 = 0; var10 < var9; ++var10)
                {
                    var11 = var8[var10];

                    if (var11 instanceof ItemStack)
                    {
                        var12 = (ItemStack)var11;
                        var13 = var12.stackSize;

                        for (var14 = var1; var14 < var2; ++var14)
                        {
                            if (var0[var14] != null && var0[var14].isItemEqual(var12))
                            {
                                var13 -= var0[var14].stackSize;

                                if (var13 < 0)
                                {
                                    var0[var14].stackSize = -var13;
                                }
                                else if (var0[var14].getItem().hasContainerItem())
                                {
                                    var0[var14] = new ItemStack(var0[var14].getItem().getContainerItem());
                                }
                                else
                                {
                                    var0[var14] = null;
                                }

                                if (var13 <= 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                    else if (var11 instanceof OreStack)
                    {
                        var15 = (OreStack)var11;
                        var13 = var15.quantity;

                        for (var14 = var1; var14 < var2; ++var14)
                        {
                            if (var0[var14] != null && isOreClass(var0[var14], var15.material))
                            {
                                var13 -= var0[var14].stackSize;

                                if (var13 < 0)
                                {
                                    var0[var14].stackSize = -var13;
                                }
                                else
                                {
                                    var0[var14] = null;
                                }

                                if (var13 <= 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            return (ItemStack)var6[1];
        }

        return null;
    }
}
