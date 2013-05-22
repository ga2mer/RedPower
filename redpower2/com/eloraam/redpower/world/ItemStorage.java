package com.eloraam.redpower.world;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemStorage extends ItemBlock
{
    public ItemStorage(int var1)
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
                return "tile.blockRuby";

            case 1:
                return "tile.blockGreenSapphire";

            case 2:
                return "tile.blockSapphire";

            case 3:
                return "tile.blockSilver";

            case 4:
                return "tile.blockTin";

            case 5:
                return "tile.blockCopper";

            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
