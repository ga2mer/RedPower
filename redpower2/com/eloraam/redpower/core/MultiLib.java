package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.World;

public class MultiLib
{
    public static boolean isClear(World var0, WorldCoord var1, List var2)
    {
        Iterator var3 = var2.iterator();
        TileMultiblock var5;

        do
        {
            WorldCoord var4;

            do
            {
                if (!var3.hasNext())
                {
                    return true;
                }

                var4 = (WorldCoord)var3.next();
            }
            while (RedPowerBase.blockMultiblock.canPlaceBlockAt(var0, var4.x, var4.y, var4.z));

            var5 = (TileMultiblock)CoreLib.getTileEntity(var0, var4, TileMultiblock.class);

            if (var5 == null)
            {
                return false;
            }
        }
        while (var5.relayX == var1.x && var5.relayY == var1.y && var5.relayZ == var1.z);

        return false;
    }

    public static void addRelays(World var0, WorldCoord var1, int var2, List var3)
    {
        int var4 = 0;
        Iterator var5 = var3.iterator();

        while (var5.hasNext())
        {
            WorldCoord var6 = (WorldCoord)var5.next();
            var0.setBlockMetadataWithNotify(var6.x, var6.y, var6.z, RedPowerBase.blockMultiblock.blockID, var2);
            TileMultiblock var7 = (TileMultiblock)CoreLib.getTileEntity(var0, var6, TileMultiblock.class);

            if (var7 != null)
            {
                var7.relayX = var1.x;
                var7.relayY = var1.y;
                var7.relayZ = var1.z;
                var7.relayNum = var4++;
            }
        }
    }

    public static void removeRelays(World var0, WorldCoord var1, List var2)
    {
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            WorldCoord var4 = (WorldCoord)var3.next();
            TileMultiblock var5 = (TileMultiblock)CoreLib.getTileEntity(var0, var4, TileMultiblock.class);

            if (var5 != null && var5.relayX == var1.x && var5.relayY == var1.y && var5.relayZ == var1.z)
            {
                var0.setBlock(var4.x, var4.y, var4.z, 0);
            }
        }
    }
}
