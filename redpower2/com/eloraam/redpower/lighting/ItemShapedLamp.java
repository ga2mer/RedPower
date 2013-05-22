package com.eloraam.redpower.lighting;

import com.eloraam.redpower.core.ItemExtended;

public class ItemShapedLamp extends ItemExtended
{
    public ItemShapedLamp(int var1)
    {
        super(var1);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int var1)
    {
        return var1 >> 10;
    }
}
