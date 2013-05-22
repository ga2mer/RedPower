package com.eloraam.redpower.world;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCustomStone extends ItemBlock
{
    public ItemCustomStone(int var1)
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
                return "tile.marble";

            case 1:
                return "tile.basalt";

            case 2:
                return "tile.marbleBrick";

            case 3:
                return "tile.basaltCobble";

            case 4:
                return "tile.basaltBrick";

            case 5:
                return "tile.basaltCircle";

            case 6:
                return "tile.basaltPaver";

            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
