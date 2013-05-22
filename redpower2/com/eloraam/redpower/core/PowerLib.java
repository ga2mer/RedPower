package com.eloraam.redpower.core;

public class PowerLib
{
    public static int cutBits(int var0, int var1)
    {
        int var2 = 1;

        while (var2 <= var1)
        {
            if ((var1 & var2) == 0)
            {
                var2 <<= 1;
            }
            else
            {
                var0 = var0 & var2 - 1 | var0 >> 1 & ~(var2 - 1);
                var1 >>= 1;
            }
        }

        return var0;
    }
}
