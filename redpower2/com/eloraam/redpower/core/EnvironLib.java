package com.eloraam.redpower.core;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

public class EnvironLib
{
    public static double getWindSpeed(World var0, WorldCoord var1)
    {
        if (var0.provider.isHellWorld)
        {
            return 0.5D;
        }
        else
        {
            double var2 = FractalLib.noise1D(2576710L, (double)var0.getWorldTime() * 1.0E-4D, 0.6F, 5);
            var2 = Math.max(0.0D, 1.6D * (var2 - 0.5D) + 0.5D);

            if (var0.getWorldInfo().getTerrainType() != WorldType.FLAT)
            {
                var2 *= Math.sqrt((double)var1.y) / 16.0D;
            }

            BiomeGenBase var4 = var0.getBiomeGenForCoords(var1.x, var1.z);

            if (var4.canSpawnLightningBolt())
            {
                if (var0.isThundering())
                {
                    return 4.0D * var2;
                }

                if (var0.isRaining())
                {
                    return 0.5D + 0.5D * var2;
                }
            }

            return var2;
        }
    }
}
