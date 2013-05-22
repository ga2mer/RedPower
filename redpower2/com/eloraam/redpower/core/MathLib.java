package com.eloraam.redpower.core;

public class MathLib
{
    private static Matrix3[] orientMatrixList = new Matrix3[24];
    private static Quat[] orientQuatList = new Quat[24];

    public static void orientMatrix(Matrix3 var0, int var1, int var2)
    {
        var0.set(orientMatrixList[var1 * 4 + var2]);
    }

    public static Quat orientQuat(int var0, int var1)
    {
        return new Quat(orientQuatList[var0 * 4 + var1]);
    }

    static
    {
        Quat var4 = Quat.aroundAxis(1.0D, 0.0D, 0.0D, Math.PI);
        int var1;
        Quat var3;

        for (var1 = 0; var1 < 4; ++var1)
        {
            var3 = Quat.aroundAxis(0.0D, 1.0D, 0.0D, -(Math.PI / 2D) * (double)var1);
            orientQuatList[var1] = var3;
            var3 = new Quat(var3);
            var3.multiply(var4);
            orientQuatList[var1 + 4] = var3;
        }

        int var0;

        for (var0 = 0; var0 < 4; ++var0)
        {
            int var2 = (var0 >> 1 | var0 << 1) & 3;
            var4 = Quat.aroundAxis(0.0D, 0.0D, 1.0D, (Math.PI / 2D));
            var4.multiply(Quat.aroundAxis(0.0D, 1.0D, 0.0D, (Math.PI / 2D) * (double)(var2 + 1)));

            for (var1 = 0; var1 < 4; ++var1)
            {
                var3 = new Quat(orientQuatList[var1]);
                var3.multiply(var4);
                orientQuatList[8 + 4 * var0 + var1] = var3;
            }
        }

        for (var0 = 0; var0 < 24; ++var0)
        {
            orientMatrixList[var0] = new Matrix3(orientQuatList[var0]);
        }
    }
}
