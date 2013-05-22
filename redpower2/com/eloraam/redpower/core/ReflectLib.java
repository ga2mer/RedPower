package com.eloraam.redpower.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectLib
{
    public static void callClassMethod(String var0, String var1, Class[] var2, Object ... var3)
    {
        Class var4;

        try
        {
            var4 = Class.forName(var0);
        }
        catch (ClassNotFoundException var10)
        {
            return;
        }

        Method var5;

        try
        {
            var5 = var4.getDeclaredMethod(var1, var2);
        }
        catch (NoSuchMethodException var9)
        {
            return;
        }

        try
        {
            var5.invoke((Object)null, var3);
        }
        catch (IllegalAccessException var7)
        {
            ;
        }
        catch (InvocationTargetException var8)
        {
            ;
        }
    }

    public static Object getStaticField(String var0, String var1, Class var2)
    {
        Class var3;

        try
        {
            var3 = Class.forName(var0);
        }
        catch (ClassNotFoundException var10)
        {
            return null;
        }

        Field var4;

        try
        {
            var4 = var3.getDeclaredField(var1);
        }
        catch (NoSuchFieldException var9)
        {
            return null;
        }

        Object var5;

        try
        {
            var5 = var4.get((Object)null);
        }
        catch (IllegalAccessException var7)
        {
            return null;
        }
        catch (NullPointerException var8)
        {
            return null;
        }

        return !var2.isInstance(var5) ? null : var5;
    }

    public static Object getField(Object var0, String var1, Class var2)
    {
        Class var3 = var0.getClass();
        Field var4;

        try
        {
            var4 = var3.getDeclaredField(var1);
        }
        catch (NoSuchFieldException var9)
        {
            return null;
        }

        Object var5;

        try
        {
            var5 = var4.get(var0);
        }
        catch (IllegalAccessException var7)
        {
            return null;
        }
        catch (NullPointerException var8)
        {
            return null;
        }

        return !var2.isInstance(var5) ? null : var5;
    }
}
