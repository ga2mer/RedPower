package com.eloraam.redpower.core;

public class FractalLib$BlockRay
{
    private Vector3 p1;
    private Vector3 p2;
    private Vector3 dv;
    public Vector3 enter;
    public Vector3 exit;
    public int xp;
    public int yp;
    public int zp;
    public int dir;
    public int face;

    public FractalLib$BlockRay(Vector3 var1, Vector3 var2)
    {
        this.p1 = new Vector3(var1);
        this.p2 = new Vector3(var2);
        this.dv = new Vector3(var2);
        this.dv.subtract(var1);
        this.exit = new Vector3(var1);
        this.enter = new Vector3();
        this.xp = (int)Math.floor(var1.x);
        this.yp = (int)Math.floor(var1.y);
        this.zp = (int)Math.floor(var1.z);
        this.dir = 0;
        this.dir |= var2.x > var1.x ? 4 : 0;
        this.dir |= var2.y > var1.y ? 1 : 0;
        this.dir |= var2.z > var1.z ? 2 : 0;
    }

    public void set(Vector3 var1, Vector3 var2)
    {
        this.p1.set(var1);
        this.p2.set(var2);
        this.dv.set(var2);
        this.dv.subtract(var1);
        this.exit.set(var1);
        this.xp = (int)Math.floor(var1.x);
        this.yp = (int)Math.floor(var1.y);
        this.zp = (int)Math.floor(var1.z);
        this.dir = 0;
        this.dir |= var2.x > var1.x ? 4 : 0;
        this.dir |= var2.y > var1.y ? 1 : 0;
        this.dir |= var2.z > var1.z ? 2 : 0;
    }

    boolean step()
    {
        double var6 = 1.0D;
        int var8 = -1;
        double var4;

        if (this.dv.x != 0.0D)
        {
            int var1 = this.xp;

            if ((this.dir & 4) > 0)
            {
                ++var1;
            }

            var4 = ((double)var1 - this.p1.x) / this.dv.x;

            if (var4 >= 0.0D && var4 <= var6)
            {
                var6 = var4;
                var8 = (this.dir & 4) > 0 ? 4 : 5;
            }
        }

        if (this.dv.y != 0.0D)
        {
            int var2 = this.yp;

            if ((this.dir & 1) > 0)
            {
                ++var2;
            }

            var4 = ((double)var2 - this.p1.y) / this.dv.y;

            if (var4 >= 0.0D && var4 <= var6)
            {
                var6 = var4;
                var8 = (this.dir & 1) > 0 ? 0 : 1;
            }
        }

        if (this.dv.z != 0.0D)
        {
            int var3 = this.zp;

            if ((this.dir & 2) > 0)
            {
                ++var3;
            }

            var4 = ((double)var3 - this.p1.z) / this.dv.z;

            if (var4 >= 0.0D && var4 <= var6)
            {
                var6 = var4;
                var8 = (this.dir & 2) > 0 ? 2 : 3;
            }
        }

        this.face = var8;

        switch (var8)
        {
            case 0:
                ++this.yp;
                break;

            case 1:
                --this.yp;
                break;

            case 2:
                ++this.zp;
                break;

            case 3:
                --this.zp;
                break;

            case 4:
                ++this.xp;
                break;

            case 5:
                --this.xp;
        }

        this.enter.set(this.exit);
        this.exit.set(this.dv);
        this.exit.multiply(var6);
        this.exit.add(this.p1);
        return var6 >= 1.0D;
    }
}
