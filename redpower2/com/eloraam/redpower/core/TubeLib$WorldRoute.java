package com.eloraam.redpower.core;

class TubeLib$WorldRoute implements Comparable
{
    public WorldCoord wc;
    public int start;
    public int side;
    public int weight;
    public boolean solved = false;

    public TubeLib$WorldRoute(WorldCoord var1, int var2, int var3, int var4)
    {
        this.wc = var1;
        this.start = var2;
        this.side = var3;
        this.weight = var4;
    }

    public int compareTo(Object var1)
    {
        TubeLib$WorldRoute var2 = (TubeLib$WorldRoute)var1;
        return this.weight - var2.weight;
    }
}
