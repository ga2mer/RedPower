package com.eloraam.redpower.core;

import java.util.Formatter;
import java.util.Locale;

public class Vector3
{
    public double x;
    public double y;
    public double z;

    public Vector3() {}

    public Vector3(double var1, double var3, double var5)
    {
        this.x = var1;
        this.y = var3;
        this.z = var5;
    }

    public Vector3(Vector3 var1)
    {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
    }

    public Object clone()
    {
        return new Vector3(this);
    }

    public void set(double var1, double var3, double var5)
    {
        this.x = var1;
        this.y = var3;
        this.z = var5;
    }

    public void set(Vector3 var1)
    {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
    }

    public double dotProduct(Vector3 var1)
    {
        return var1.x * this.x + var1.y * this.y + var1.z * this.z;
    }

    public double dotProduct(double var1, double var3, double var5)
    {
        return var1 * this.x + var3 * this.y + var5 * this.z;
    }

    public void crossProduct(Vector3 var1)
    {
        double var2 = this.y * var1.z - this.z * var1.y;
        double var4 = this.z * var1.x - this.x * var1.z;
        double var6 = this.x * var1.y - this.y * var1.x;
        this.x = var2;
        this.y = var4;
        this.z = var6;
    }

    public void add(double var1, double var3, double var5)
    {
        this.x += var1;
        this.y += var3;
        this.z += var5;
    }

    public void add(Vector3 var1)
    {
        this.x += var1.x;
        this.y += var1.y;
        this.z += var1.z;
    }

    public void subtract(Vector3 var1)
    {
        this.x -= var1.x;
        this.y -= var1.y;
        this.z -= var1.z;
    }

    public void multiply(double var1)
    {
        this.x *= var1;
        this.y *= var1;
        this.z *= var1;
    }

    public double mag()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double magSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public void normalize()
    {
        double var1 = this.mag();

        if (var1 != 0.0D)
        {
            this.multiply(1.0D / var1);
        }
    }

    public String toString()
    {
        StringBuilder var1 = new StringBuilder();
        Formatter var2 = new Formatter(var1, Locale.US);
        var2.format("Vector:\n", new Object[0]);
        var2.format("  < %f %f %f >\n", new Object[] {Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z)});
        return var1.toString();
    }
}
