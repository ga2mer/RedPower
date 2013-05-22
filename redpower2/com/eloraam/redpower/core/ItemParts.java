package com.eloraam.redpower.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemParts extends Item
{
    String textureFile;
    HashMap names = new HashMap();
    HashMap icons = new HashMap();
    ArrayList valid = new ArrayList();

    public ItemParts(int var1, String var2)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.textureFile = var2;
    }

    public void addItem(int var1, int var2, String var3)
    {
        this.icons.put(Integer.valueOf(var1), Integer.valueOf(var2));
        this.names.put(Integer.valueOf(var1), var3);
        this.valid.add(Integer.valueOf(var1));
    }

    public String getItemNameIS(ItemStack var1)
    {
        String var2 = (String)this.names.get(Integer.valueOf(var1.getItemDamage()));

        if (var2 == null)
        {
            throw new IndexOutOfBoundsException();
        }
        else
        {
            return var2;
        }
    }


    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        Iterator var4 = this.valid.iterator();

        while (var4.hasNext())
        {
            Integer var5 = (Integer)var4.next();
            var3.add(new ItemStack(this, 1, var5.intValue()));
        }
    }

    public String getTextureFile()
    {
        return this.textureFile;
    }
}
