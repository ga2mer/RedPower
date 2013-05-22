package com.eloraam.redpower.world;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCustomOre extends ItemBlock
{
    public ItemCustomOre(int var1)
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
                return "tile.oreRuby";

            case 1:
                return "tile.oreGreenSapphire";

            case 2:
                return "tile.oreSapphire";

            case 3:
                return "tile.oreSilver";

            case 4:
                return "tile.oreTin";

            case 5:
                return "tile.oreCopper";

            case 6:
                return "tile.oreTungsten";

            case 7:
                return "tile.oreNikolite";

            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
