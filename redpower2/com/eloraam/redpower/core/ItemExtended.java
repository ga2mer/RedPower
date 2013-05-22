package com.eloraam.redpower.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemExtended extends ItemBlock
{
    HashMap names = new HashMap();
    ArrayList valid = new ArrayList();

    public ItemExtended(int var1)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int var1)
    {
        return var1;
    }

    public void setMetaName(int var1, String var2)
    {
        this.names.put(Integer.valueOf(var1), var2);
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

    public boolean placeBlockAt(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10, int var11)
    {
        if (!var3.setBlock(var4, var5, var6, this.itemID))
        {
            return false;
        }
        else
        {
            if (var3.getBlockId(var4, var5, var6) == this.itemID)
            {
                BlockExtended var12 = (BlockExtended)Block.blocksList[this.itemID];
                var12.onBlockPlacedUseful(var3, var4, var5, var6, var7, var2, var1);
            }

            return true;
        }
    }
}
