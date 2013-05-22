package com.eloraam.redpower.core;

import java.util.Formatter;
import java.util.Locale;

public class Matrix3
{
    public double xx;
    public double xy;
    public double xz;
    public double yx;
    public double yy;
    public double yz;
    public double zx;
    public double zy;
    public double zz;

    public Matrix3() {}

    public Matrix3(Quat var1)
    {
        this.set(var1);
    }

    public void set(Quat var1)
    {
        this.xx = var1.s * var1.s + var1.x * var1.x - var1.z * var1.z - var1.y * var1.y;
        this.xy = 2.0D * (var1.s * var1.z + var1.x * var1.y);
        this.xz = 2.0D * (var1.x * var1.z - var1.s * var1.y);
        this.yx = 2.0D * (var1.x * var1.y - var1.s * var1.z);
        this.yy = var1.s * var1.s + var1.y * var1.y - var1.z * var1.z - var1.x * var1.x;
        this.yz = 2.0D * (var1.s * var1.x + var1.y * var1.z);
        this.zx = 2.0D * (var1.s * var1.y + var1.x * var1.z);
        this.zy = 2.0D * (var1.y * var1.z - var1.s * var1.x);
        this.zz = var1.s * var1.s + var1.z * var1.z - var1.y * var1.y - var1.x * var1.x;
    }

    public void set(Matrix3 var1)
    {
        this.xx = var1.xx;
        this.xy = var1.xy;
        this.xz = var1.xz;
        this.yx = var1.yx;
        this.yy = var1.yy;
        this.yz = var1.yz;
        this.zx = var1.zx;
        this.zy = var1.zy;
        this.zz = var1.zz;
    }

    public Matrix3 multiply(Matrix3 var1)
    {
        Matrix3 var2 = new Matrix3();
        var2.xx = this.xx * var1.xx + this.xy * var1.yx + this.xz * var1.zx;
        var2.xy = this.xx * var1.xy + this.xy * var1.yy + this.xz * var1.zy;
        var2.xz = this.xx * var1.xz + this.xy * var1.yz + this.xz * var1.zz;
        var2.yx = this.yx * var1.xx + this.yy * var1.yx + this.yz * var1.zx;
        var2.yy = this.yx * var1.xy + this.yy * var1.yy + this.yz * var1.zy;
        var2.yz = this.yx * var1.xz + this.yy * var1.yz + this.yz * var1.zz;
        var2.zx = this.zx * var1.xx + this.zy * var1.yx + this.zz * var1.zx;
        var2.zy = this.zx * var1.xy + this.zy * var1.yy + this.zz * var1.zy;
        var2.zz = this.zx * var1.xz + this.zy * var1.yz + this.zz * var1.zz;
        return var2;
    }

    public static Matrix3 getRotY(double var0)
    {
        double var2 = Math.cos(var0);
        double var4 = Math.sin(var0);
        Matrix3 var6 = new Matrix3();
        var6.xx = var2;
        var6.xy = 0.0D;
        var6.xz = var4;
        var6.yx = 0.0D;
        var6.yy = 1.0D;
        var6.yz = 0.0D;
        var6.zx = -var4;
        var6.zy = 0.0D;
        var6.zz = var2;
        return var6;
    }

    public Vector3 getBasisVector(int var1)
    {
        return var1 == 0 ? new Vector3(this.xx, this.xy, this.xz) : (var1 == 1 ? new Vector3(this.yx, this.yy, this.yz) : new Vector3(this.zx, this.zy, this.zz));
    }

    public double det()
    {
        return this.xx * (this.yy * this.zz - this.yz * this.zy) - this.xy * (this.yx * this.zz - this.yz * this.zx) + this.xz * (this.yx * this.zy - this.yy * this.zx);
    }

    public void rotate(Vector3 var1)
    {
        double var2 = this.xx * var1.x + this.yx * var1.y + this.zx * var1.z;
        double var4 = this.xy * var1.x + this.yy * var1.y + this.zy * var1.z;
        double var6 = this.xz * var1.x + this.yz * var1.y + this.zz * var1.z;
        var1.x = var2;
        var1.y = var4;
        var1.z = var6;
    }

    public String toString()
    {
        StringBuilder var1 = new StringBuilder();
        Formatter var2 = new Formatter(var1, Locale.US);
        var2.format("Matrix:\n", new Object[0]);
        var2.format("  < %f %f %f >\n", new Object[] {Double.valueOf(this.xx), Double.valueOf(this.xy), Double.valueOf(this.xz)});
        var2.format("  < %f %f %f >\n", new Object[] {Double.valueOf(this.yx), Double.valueOf(this.yy), Double.valueOf(this.yz)});
        var2.format("  < %f %f %f >\n", new Object[] {Double.valueOf(this.zx), Double.valueOf(this.zy), Double.valueOf(this.zz)});
        return var1.toString();
    }
}
