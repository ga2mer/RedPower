package com.eloraam.redpower.core;

import java.util.Formatter;
import java.util.Locale;

public class Quat
{
    public double x;
    public double y;
    public double z;
    public double s;
    public static final double SQRT2 = Math.sqrt(2.0D);

    public Quat()
    {
        this.s = 1.0D;
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }

    public Quat(Quat var1)
    {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
        this.s = var1.s;
    }

    public Quat(double var1, double var3, double var5, double var7)
    {
        this.x = var3;
        this.y = var5;
        this.z = var7;
        this.s = var1;
    }

    public void set(Quat var1)
    {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
        this.s = var1.s;
    }

    public static Quat aroundAxis(double var0, double var2, double var4, double var6)
    {
        var6 *= 0.5D;
        double var8 = Math.sin(var6);
        return new Quat(Math.cos(var6), var0 * var8, var2 * var8, var4 * var8);
    }

    public void multiply(Quat var1)
    {
        double var2 = this.s * var1.s - this.x * var1.x - this.y * var1.y - this.z * var1.z;
        double var4 = this.s * var1.x + this.x * var1.s - this.y * var1.z + this.z * var1.y;
        double var6 = this.s * var1.y + this.x * var1.z + this.y * var1.s - this.z * var1.x;
        double var8 = this.s * var1.z - this.x * var1.y + this.y * var1.x + this.z * var1.s;
        this.s = var2;
        this.x = var4;
        this.y = var6;
        this.z = var8;
    }

    public void rightMultiply(Quat var1)
    {
        double var2 = this.s * var1.s - this.x * var1.x - this.y * var1.y - this.z * var1.z;
        double var4 = this.s * var1.x + this.x * var1.s + this.y * var1.z - this.z * var1.y;
        double var6 = this.s * var1.y - this.x * var1.z + this.y * var1.s + this.z * var1.x;
        double var8 = this.s * var1.z + this.x * var1.y - this.y * var1.x + this.z * var1.s;
        this.s = var2;
        this.x = var4;
        this.y = var6;
        this.z = var8;
    }

    public double mag()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.s * this.s);
    }

    public void normalize()
    {
        double var1 = this.mag();

        if (var1 != 0.0D)
        {
            var1 = 1.0D / var1;
            this.x *= var1;
            this.y *= var1;
            this.z *= var1;
            this.s *= var1;
        }
    }

    public void rotate(Vector3 var1)
    {
        double var2 = -this.x * var1.x - this.y * var1.y - this.z * var1.z;
        double var4 = this.s * var1.x + this.y * var1.z - this.z * var1.y;
        double var6 = this.s * var1.y - this.x * var1.z + this.z * var1.x;
        double var8 = this.s * var1.z + this.x * var1.y - this.y * var1.x;
        var1.x = var4 * this.s - var2 * this.x - var6 * this.z + var8 * this.y;
        var1.y = var6 * this.s - var2 * this.y + var4 * this.z - var8 * this.x;
        var1.z = var8 * this.s - var2 * this.z - var4 * this.y + var6 * this.x;
    }

    public String toString()
    {
        StringBuilder var1 = new StringBuilder();
        Formatter var2 = new Formatter(var1, Locale.US);
        var2.format("Quaternion:\n", new Object[0]);
        var2.format("  < %f %f %f %f >\n", new Object[] {Double.valueOf(this.s), Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z)});
        return var1.toString();
    }
}
