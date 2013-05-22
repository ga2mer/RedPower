package com.eloraam.redpower.world;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCustomFlower extends ItemBlock
{
    public ItemCustomFlower(int var1)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getPlacedBlockMetadata(int var1)
    {
        return var1;
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int var1)
    {
        return var1;
    }

    public String getItemNameIS(ItemStack var1)
    {
        switch (var1.getItemDamage())
        {
            case 0:
                return "tile.indigo";

            case 1:
                return "tile.rubbersapling";

            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public String getTextureFile()
    {
        return "/eloraam/world/worlditems1.png";
    }
}
