package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerCore;
import java.io.File;
import net.minecraft.world.World;

public class DiskLib
{
    public static File getSaveDir(World var0)
    {
        File var1 = new File(RedPowerCore.getSaveDir(var0), "redpower");
        var1.mkdirs();
        return var1;
    }

    public static String generateSerialNumber(World var0)
    {
        String var1 = "";

        for (int var2 = 0; var2 < 16; ++var2)
        {
            var1 = var1 + String.format("%01x", new Object[] {Integer.valueOf(var0.rand.nextInt(16))});
        }

        return var1;
    }

    public static File getDiskFile(File var0, String var1)
    {
        return new File(var0, String.format("disk_%s.img", new Object[] {var1}));
    }
}
