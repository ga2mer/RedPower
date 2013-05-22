package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.Config;
import cpw.mods.fml.common.IWorldGenerator;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenFlowers;

public class WorldGenHandler implements IWorldGenerator
{
    public void generate(Random var1, int var2, int var3, World var4, IChunkProvider var5, IChunkProvider var6)
    {
        if (!(var5 instanceof ChunkProviderHell) && !(var5 instanceof ChunkProviderEnd))
        {
            Random var8 = new Random((long)(Integer.valueOf(var2).hashCode() * 31 + Integer.valueOf(var3).hashCode()));
            int var7;
            int var9;
            int var10;
            int var11;

            for (var7 = 0; var7 < 2; ++var7)
            {
                var9 = var2 * 16 + var8.nextInt(16);
                var10 = var8.nextInt(48);
                var11 = var3 * 16 + var8.nextInt(16);
                (new WorldGenCustomOre(RedPowerWorld.blockOres.blockID, 0, 7)).generate(var4, var8, var9, var10, var11);
            }

            for (var7 = 0; var7 < 2; ++var7)
            {
                var9 = var2 * 16 + var8.nextInt(16);
                var10 = var8.nextInt(48);
                var11 = var3 * 16 + var8.nextInt(16);
                (new WorldGenCustomOre(RedPowerWorld.blockOres.blockID, 1, 7)).generate(var4, var8, var9, var10, var11);
            }

            for (var7 = 0; var7 < 2; ++var7)
            {
                var9 = var2 * 16 + var8.nextInt(16);
                var10 = var8.nextInt(48);
                var11 = var3 * 16 + var8.nextInt(16);
                (new WorldGenCustomOre(RedPowerWorld.blockOres.blockID, 2, 7)).generate(var4, var8, var9, var10, var11);
            }

            if (Config.getInt("settings.world.generate.silver") > 0)
            {
                for (var7 = 0; var7 < 4; ++var7)
                {
                    var9 = var2 * 16 + var8.nextInt(16);
                    var10 = var8.nextInt(32);
                    var11 = var3 * 16 + var8.nextInt(16);
                    (new WorldGenCustomOre(RedPowerWorld.blockOres.blockID, 3, 8)).generate(var4, var8, var9, var10, var11);
                }
            }

            if (Config.getInt("settings.world.generate.tin") > 0)
            {
                for (var7 = 0; var7 < 10; ++var7)
                {
                    var9 = var2 * 16 + var8.nextInt(16);
                    var10 = var8.nextInt(48);
                    var11 = var3 * 16 + var8.nextInt(16);
                    (new WorldGenCustomOre(RedPowerWorld.blockOres.blockID, 4, 8)).generate(var4, var8, var9, var10, var11);
                }
            }

            if (Config.getInt("settings.world.generate.copper") > 0)
            {
                for (var7 = 0; var7 < 20; ++var7)
                {
                    var9 = var2 * 16 + var8.nextInt(16);
                    var10 = var8.nextInt(64);
                    var11 = var3 * 16 + var8.nextInt(16);
                    (new WorldGenCustomOre(RedPowerWorld.blockOres.blockID, 5, 8)).generate(var4, var8, var9, var10, var11);
                }
            }

            for (var7 = 0; var7 < 1; ++var7)
            {
                var9 = var2 * 16 + var8.nextInt(16);
                var10 = var8.nextInt(16);
                var11 = var3 * 16 + var8.nextInt(16);
                (new WorldGenCustomOre(RedPowerWorld.blockOres.blockID, 6, 4)).generate(var4, var8, var9, var10, var11);
            }

            for (var7 = 0; var7 < 4; ++var7)
            {
                var9 = var2 * 16 + var8.nextInt(16);
                var10 = var8.nextInt(16);
                var11 = var3 * 16 + var8.nextInt(16);
                (new WorldGenCustomOre(RedPowerWorld.blockOres.blockID, 7, 10)).generate(var4, var8, var9, var10, var11);
            }

            for (var7 = 0; var7 < 4; ++var7)
            {
                var9 = var2 * 16 + var8.nextInt(16);
                var10 = 32 + var8.nextInt(32);
                var11 = var3 * 16 + var8.nextInt(16);
                (new WorldGenMarble(RedPowerWorld.blockStone.blockID, 0, var8.nextInt(4096))).generate(var4, var8, var9, var10, var11);
            }

            var9 = Math.max(1, var8.nextInt(10) - 6);
            var9 *= var9;
            int var12;

            for (var7 = 0; var7 < var9; ++var7)
            {
                var10 = var2 * 16 + var8.nextInt(16);
                var11 = var8.nextInt(32);
                var12 = var3 * 16 + var8.nextInt(16);
                (new WorldGenVolcano(RedPowerWorld.blockStone.blockID, 1, var8.nextInt(65536))).generate(var4, var8, var10, var11, var12);
            }

            BiomeGenBase var15 = var4.getWorldChunkManager().getBiomeGenAt(var2 * 16 + 16, var3 * 16 + 16);
            byte var16 = 0;

            if (var15 == BiomeGenBase.jungle)
            {
                var16 = 1;
            }
            else if (var15 == BiomeGenBase.jungleHills)
            {
                var16 = 1;
            }
            else if (var15 == BiomeGenBase.forest)
            {
                var16 = 1;
            }
            else if (var15 == BiomeGenBase.plains)
            {
                var16 = 4;
            }

            int var13;
            int var14;

            for (var7 = 0; var7 < var16; ++var7)
            {
                var12 = var2 * 16 + var8.nextInt(16) + 8;
                var13 = var8.nextInt(128);
                var14 = var3 * 16 + var8.nextInt(16) + 8;
                (new WorldGenFlowers(RedPowerWorld.blockPlants.blockID)).generate(var4, var8, var12, var13, var14);
            }

            if (var15 == BiomeGenBase.jungle || var15 == BiomeGenBase.jungleHills)
            {
                for (var7 = 0; var7 < 6; ++var7)
                {
                    var12 = var2 * 16 + var8.nextInt(16) + 8;
                    var13 = var3 * 16 + var8.nextInt(16) + 8;
                    var14 = var4.getHeightValue(var12, var13);
                    (new WorldGenRubberTree()).generate(var4, var4.rand, var12, var14, var13);
                }
            }
        }
    }
}
