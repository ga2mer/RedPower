package com.eloraam.redpower.control;

import com.eloraam.redpower.wiring.TileWiring;

public class TileRibbon extends TileWiring
{
    public int getExtendedID()
    {
        return 12;
    }

    public int getConnectClass(int var1)
    {
        return 66;
    }

    public void onBlockNeighborChange(int var1)
    {
        super.onBlockNeighborChange(var1);
        this.getConnectionMask();
        this.getExtConnectionMask();
    }
}
