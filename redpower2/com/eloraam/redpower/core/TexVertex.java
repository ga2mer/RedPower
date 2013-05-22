package com.eloraam.redpower.core;

public class TexVertex
{
    public int vtx;
    public double u;
    public double v;
    public float r;
    public float g;
    public float b;
    public int brtex;

    public TexVertex() {}

    public TexVertex(int var1, int var2, double var3, double var5)
    {
        this.vtx = var1;
        this.u = (double)(var2 & 15) * 0.0625D + var3 * 0.0625D;
        this.v = (double)(var2 >> 4) * 0.0625D + var5 * 0.0625D;
        this.r = 1.0F;
        this.g = 1.0F;
        this.b = 1.0F;
    }

    public TexVertex(int var1, double var2, double var4)
    {
        this.vtx = var1;
        this.u = var2;
        this.v = var4;
        this.r = 1.0F;
        this.g = 1.0F;
        this.b = 1.0F;
    }

    public void setUV(double var1, double var3)
    {
        this.u = var1;
        this.v = var3;
    }

    public TexVertex copy()
    {
        TexVertex var1 = new TexVertex(this.vtx, this.u, this.v);
        var1.r = this.r;
        var1.g = this.g;
        var1.b = this.b;
        var1.brtex = this.brtex;
        return var1;
    }
}
