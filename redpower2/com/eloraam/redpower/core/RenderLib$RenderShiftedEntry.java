package com.eloraam.redpower.core;

import com.eloraam.redpower.core.RenderLib$RenderListEntry;

class RenderLib$RenderShiftedEntry extends RenderLib$RenderListEntry
{
    public int shift;

    public RenderLib$RenderShiftedEntry(int var1)
    {
        this.shift = var1;
    }

    public int mapDamageValue(int var1)
    {
        return var1 >> this.shift;
    }
}
