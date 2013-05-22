package com.eloraam.redpower.core;

public class OreStack
{
    public String material;
    public int quantity;

    public OreStack(String var1, int var2)
    {
        this.material = var1;
        this.quantity = var2;
    }

    public OreStack(String var1)
    {
        this.material = var1;
        this.quantity = 1;
    }
}
