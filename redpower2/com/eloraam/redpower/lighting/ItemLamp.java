package com.eloraam.redpower.lighting;

import com.eloraam.redpower.RedPowerLighting;
import com.eloraam.redpower.core.CoreLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemLamp extends ItemBlock
{
    public ItemLamp(int var1)
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

    public int getPlacedBlockMetadata(int var1)
    {
        return var1;
    }

    public String getItemNameIS(ItemStack var1)
    {
        return var1.itemID == RedPowerLighting.blockInvLampOn.blockID ? "tile.rpilamp." + CoreLib.rawColorNames[var1.getItemDamage()] : "tile.rplamp." + CoreLib.rawColorNames[var1.getItemDamage()];
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 <= 15; ++var4)
        {
            var3.add(new ItemStack(this, 1, var4));
        }
    }
}
