package com.eloraam.redpower.core;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TubeFlow$TubeScheduleContext
{
    public World world;
    public WorldCoord wc;
    public int cons;
    public ArrayList tir = new ArrayList();
    public Iterator tii;
    public WorldCoord dest = null;

    public TubeFlow$TubeScheduleContext(TileEntity var1)
    {
        this.world = var1.worldObj;
        this.wc = new WorldCoord(var1);
        this.cons = TubeLib.getConnections(this.world, this.wc.x, this.wc.y, this.wc.z);
    }
}
