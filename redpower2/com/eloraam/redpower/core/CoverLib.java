package com.eloraam.redpower.core;

import com.eloraam.redpower.core.CoverLib$IMaterialHandler;
import com.eloraam.redpower.core.CoverLib$PlacementValidator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CoverLib
{
    public static final float selectBoxWidth = 0.25F;
    public static Block blockCoverPlate = null;
    private static ItemStack[] materials = new ItemStack[256];
    private static String[] names = new String[256];
    private static String[] descs = new String[256];
    private static int[] hardness = new int[256];
    private static ArrayList materialHandlers = new ArrayList();
    private static boolean[] transparency = new boolean[256];
    public static int[][] coverTextures = new int[256][];
    public static String[] coverTextureFiles = new String[256];
    private static float[] miningHardness = new float[256];
    private static HashMap coverIndex = new HashMap();

    public static void addMaterialHandler(CoverLib$IMaterialHandler var0)
    {
        for (int var1 = 0; var1 < 256; ++var1)
        {
            if (materials[var1] != null)
            {
                var0.addMaterial(var1);
            }
        }

        materialHandlers.add(var0);
    }

    public static Integer getMaterial(ItemStack var0)
    {
        return (Integer)coverIndex.get(Arrays.asList(new Integer[] {Integer.valueOf(var0.itemID), Integer.valueOf(var0.getItemDamage())}));
    }

    public static void addMaterial(int var0, int var1, Block var2, String var3, String var4)
    {
        addMaterial(var0, var1, false, var2, 0, var3, var4);
    }

    public static void addMaterial(int var0, int var1, Block var2, int var3, String var4, String var5)
    {
        addMaterial(var0, var1, false, var2, var3, var4, var5);
    }

    public static void addMaterial(int var0, int var1, boolean var2, Block var3, String var4, String var5)
    {
        addMaterial(var0, var1, var2, var3, 0, var4, var5);
    }

    public static void addMaterial(int var0, int var1, boolean var2, Block var3, int var4, String var5, String var6)
    {
        ItemStack var8 = new ItemStack(var3, 1, var4);
        coverTextures[var0] = new int[6];

        /*for (int var7 = 0; var7 < 6; ++var7)
        {
            coverTextures[var0][var7] = var3.getBlockTextureFromSideAndMetadata(var7, var4);
        }

        if (!var3.isDefaultTexture)
        {
            coverTextureFiles[var0] = var3.getTextureFile();
        }  */

        if (var3 instanceof IBlockHardness)
        {
            miningHardness[var0] = ((IBlockHardness)var3).getPrototypicalHardness(var4);
        }
        else
        {
            miningHardness[var0] = var3.getBlockHardness((World)null, 0, 0, 0);
        }

        materials[var0] = var8;
        names[var0] = var5;
        descs[var0] = var6;
        hardness[var0] = var1;
        transparency[var0] = var2;
        coverIndex.put(Arrays.asList(new Integer[] {Integer.valueOf(var3.blockID), Integer.valueOf(var4)}), Integer.valueOf(var0));
        Iterator var9 = materialHandlers.iterator();

        while (var9.hasNext())
        {
            CoverLib$IMaterialHandler var10 = (CoverLib$IMaterialHandler)var9.next();
            var10.addMaterial(var0);
        }

        Config.addName("tile.rpcover." + var5 + ".name", var6 + " Cover");
        Config.addName("tile.rppanel." + var5 + ".name", var6 + " Panel");
        Config.addName("tile.rpslab." + var5 + ".name", var6 + " Slab");
        Config.addName("tile.rphcover." + var5 + ".name", "Hollow " + var6 + " Cover");
        Config.addName("tile.rphpanel." + var5 + ".name", "Hollow " + var6 + " Panel");
        Config.addName("tile.rphslab." + var5 + ".name", "Hollow " + var6 + " Slab");
        Config.addName("tile.rpcovc." + var5 + ".name", var6 + " Cover Corner");
        Config.addName("tile.rppanc." + var5 + ".name", var6 + " Panel Corner");
        Config.addName("tile.rpslabc." + var5 + ".name", var6 + " Slab Corner");
        Config.addName("tile.rpcovs." + var5 + ".name", var6 + " Cover Strip");
        Config.addName("tile.rppans." + var5 + ".name", var6 + " Panel Strip");
        Config.addName("tile.rpslabs." + var5 + ".name", var6 + " Slab Strip");
        Config.addName("tile.rpcov3." + var5 + ".name", var6 + " Triple Cover");
        Config.addName("tile.rpcov5." + var5 + ".name", var6 + " Cover Slab");
        Config.addName("tile.rpcov6." + var5 + ".name", var6 + " Triple Panel");
        Config.addName("tile.rpcov7." + var5 + ".name", var6 + " Anticover");
        Config.addName("tile.rphcov3." + var5 + ".name", var6 + " Hollow Triple Cover");
        Config.addName("tile.rphcov5." + var5 + ".name", var6 + " Hollow Cover Slab");
        Config.addName("tile.rphcov6." + var5 + ".name", var6 + " Hollow Triple Panel");
        Config.addName("tile.rphcov7." + var5 + ".name", var6 + " Hollow Anticover");
        Config.addName("tile.rpcov3c." + var5 + ".name", var6 + " Triple Cover Corner");
        Config.addName("tile.rpcov5c." + var5 + ".name", var6 + " Cover Slab Corner");
        Config.addName("tile.rpcov6c." + var5 + ".name", var6 + " Triple Panel Corner");
        Config.addName("tile.rpcov7c." + var5 + ".name", var6 + " Anticover Corner");
        Config.addName("tile.rpcov3s." + var5 + ".name", var6 + " Triple Cover Strip");
        Config.addName("tile.rpcov5s." + var5 + ".name", var6 + " Cover Slab Strip");
        Config.addName("tile.rpcov6s." + var5 + ".name", var6 + " Triple Panel Strip");
        Config.addName("tile.rpcov7s." + var5 + ".name", var6 + " Anticover Strip");
        Config.addName("tile.rppole1." + var5 + ".name", var6 + " Post");
        Config.addName("tile.rppole2." + var5 + ".name", var6 + " Pillar");
        Config.addName("tile.rppole3." + var5 + ".name", var6 + " Column");
    }

    public static int damageToCoverData(int var0)
    {
        int var1 = var0 >> 8;
        int var2 = var0 & 255;

        switch (var1)
        {
            case 0:
                var2 |= 65536;

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                break;

            case 16:
                var2 |= 131328;
                break;

            case 17:
                var2 |= 262656;
                break;

            case 18:
                var2 |= 33619968;
                break;

            case 19:
                var2 |= 33685760;
                break;

            case 20:
                var2 |= 33817088;
                break;

            case 21:
                var2 |= 16842752;
                break;

            case 22:
                var2 |= 16908544;
                break;

            case 23:
                var2 |= 17039872;
                break;

            case 24:
                var2 |= 1114880;
                break;

            case 25:
                var2 |= 1180672;
                break;

            case 26:
                var2 |= 1312000;
                break;

            case 27:
                var2 |= 198144;
                break;

            case 28:
                var2 |= 329472;
                break;

            case 29:
                var2 |= 395264;
                break;

            case 30:
                var2 |= 461056;
                break;

            case 31:
                var2 |= 1247744;
                break;

            case 32:
                var2 |= 1379072;
                break;

            case 33:
                var2 |= 1444864;
                break;

            case 34:
                var2 |= 1510656;
                break;

            case 35:
                var2 |= 33751808;
                break;

            case 36:
                var2 |= 33883136;
                break;

            case 37:
                var2 |= 33948928;
                break;

            case 38:
                var2 |= 34014720;
                break;

            case 39:
                var2 |= 16974592;
                break;

            case 40:
                var2 |= 17105920;
                break;

            case 41:
                var2 |= 17171712;
                break;

            case 42:
                var2 |= 17237504;
                break;

            case 43:
                var2 |= 50462720;
                break;

            case 44:
                var2 |= 50594048;
                break;

            case 45:
                var2 |= 50725376;
        }

        return var2;
    }

    public static int damageToCoverValue(int var0)
    {
        return damageToCoverData(var0) & 65535;
    }

    public static int coverValueToDamage(int var0, int var1)
    {
        int var2 = var1 >> 8;
        int var3 = var1 & 255;

        if (var0 < 6)
        {
            switch (var2)
            {
                case 1:
                    var3 |= 4096;
                    break;

                case 2:
                    var3 |= 4352;
                    break;

                case 3:
                    var3 |= 6144;
                    break;

                case 4:
                    var3 |= 6400;
                    break;

                case 5:
                    var3 |= 6656;
                    break;

                case 6:
                    var3 |= 6912;
                    break;

                case 7:
                    var3 |= 7168;
                    break;

                case 8:
                    var3 |= 7424;
                    break;

                case 9:
                    var3 |= 7680;
                    break;

                case 10:
                    var3 |= 7936;
                    break;

                case 11:
                    var3 |= 8192;
                    break;

                case 12:
                    var3 |= 8448;
                    break;

                case 13:
                    var3 |= 8704;
            }
        }
        else if (var0 < 14)
        {
            switch (var2)
            {
                case 0:
                    var3 |= 4608;
                    break;

                case 1:
                    var3 |= 4864;
                    break;

                case 2:
                    var3 |= 5120;
                    break;

                case 3:
                    var3 |= 8960;
                    break;

                case 4:
                    var3 |= 9216;
                    break;

                case 5:
                    var3 |= 9472;
                    break;

                case 6:
                    var3 |= 9728;
            }
        }
        else if (var0 < 26)
        {
            switch (var2)
            {
                case 0:
                    var3 |= 5376;
                    break;

                case 1:
                    var3 |= 5632;
                    break;

                case 2:
                    var3 |= 5888;
                    break;

                case 3:
                    var3 |= 9984;
                    break;

                case 4:
                    var3 |= 10240;
                    break;

                case 5:
                    var3 |= 10496;
                    break;

                case 6:
                    var3 |= 10752;
            }
        }
        else if (var0 < 29)
        {
            switch (var2)
            {
                case 0:
                    var3 |= 11008;
                    break;

                case 1:
                    var3 |= 11264;
                    break;

                case 2:
                    var3 |= 11520;
            }
        }

        return var3;
    }

    public static ItemStack convertCoverPlate(int var0, int var1)
    {
        return blockCoverPlate == null ? null : new ItemStack(blockCoverPlate, 1, coverValueToDamage(var0, var1));
    }

    public static int cornerToCoverMask(int var0)
    {
        switch (var0)
        {
            case 0:
                return 21;

            case 1:
                return 25;

            case 2:
                return 37;

            case 3:
                return 41;

            case 4:
                return 22;

            case 5:
                return 26;

            case 6:
                return 38;

            default:
                return 42;
        }
    }

    public static int coverToCornerMask(int var0)
    {
        switch (var0)
        {
            case 0:
                return 15;

            case 1:
                return 240;

            case 2:
                return 85;

            case 3:
                return 170;

            case 4:
                return 51;

            default:
                return 204;
        }
    }

    public static int coverToStripMask(int var0)
    {
        switch (var0)
        {
            case 0:
                return 15;

            case 1:
                return 3840;

            case 2:
                return 337;

            case 3:
                return 674;

            case 4:
                return 1076;

            default:
                return 2248;
        }
    }

    public static int stripToCornerMask(int var0)
    {
        switch (var0)
        {
            case 0:
                return 5;

            case 1:
                return 10;

            case 2:
                return 3;

            case 3:
                return 12;

            case 4:
                return 17;

            case 5:
                return 34;

            case 6:
                return 68;

            case 7:
                return 136;

            case 8:
                return 80;

            case 9:
                return 160;

            case 10:
                return 48;

            default:
                return 192;
        }
    }

    public static int stripToCoverMask(int var0)
    {
        switch (var0)
        {
            case 0:
                return 5;

            case 1:
                return 9;

            case 2:
                return 17;

            case 3:
                return 33;

            case 4:
                return 20;

            case 5:
                return 24;

            case 6:
                return 36;

            case 7:
                return 40;

            case 8:
                return 6;

            case 9:
                return 10;

            case 10:
                return 18;

            default:
                return 34;
        }
    }

    public static float getThickness(int var0, int var1)
    {
        if (var0 < 6)
        {
            switch (var1 >> 8)
            {
                case 0:
                    return 0.125F;

                case 1:
                    return 0.25F;

                case 2:
                    return 0.5F;

                case 3:
                    return 0.125F;

                case 4:
                    return 0.25F;

                case 5:
                    return 0.5F;

                case 6:
                    return 0.375F;

                case 7:
                    return 0.625F;

                case 8:
                    return 0.75F;

                case 9:
                    return 0.875F;

                case 10:
                    return 0.375F;

                case 11:
                    return 0.625F;

                case 12:
                    return 0.75F;

                case 13:
                    return 0.875F;

                default:
                    return 1.0F;
            }
        }
        else
        {
            if (var0 >= 26 && var0 < 29)
            {
                switch (var1 >> 8)
                {
                    case 0:
                        return 0.125F;

                    case 1:
                        return 0.25F;

                    case 2:
                        return 0.375F;
                }
            }

            switch (var1 >> 8)
            {
                case 0:
                    return 0.125F;

                case 1:
                    return 0.25F;

                case 2:
                    return 0.5F;

                case 3:
                    return 0.375F;

                case 4:
                    return 0.625F;

                case 5:
                    return 0.75F;

                case 6:
                    return 0.875F;

                default:
                    return 1.0F;
            }
        }
    }

    public static int getThicknessQuanta(int var0, int var1)
    {
        if (var0 < 6)
        {
            switch (var1 >> 8)
            {
                case 0:
                    return 1;

                case 1:
                    return 2;

                case 2:
                    return 4;

                case 3:
                    return 1;

                case 4:
                    return 2;

                case 5:
                    return 4;

                case 6:
                    return 3;

                case 7:
                    return 5;

                case 8:
                    return 6;

                case 9:
                    return 7;

                case 10:
                    return 3;

                case 11:
                    return 5;

                case 12:
                    return 6;

                case 13:
                    return 7;

                default:
                    return 0;
            }
        }
        else
        {
            if (var0 >= 26 && var0 < 29)
            {
                switch (var1 >> 8)
                {
                    case 0:
                        return 1;

                    case 1:
                        return 2;

                    case 2:
                        return 3;
                }
            }

            switch (var1 >> 8)
            {
                case 0:
                    return 1;

                case 1:
                    return 2;

                case 2:
                    return 4;

                case 3:
                    return 3;

                case 4:
                    return 5;

                case 5:
                    return 6;

                case 6:
                    return 7;

                default:
                    return 0;
            }
        }
    }

    public static boolean checkPlacement(int var0, short[] var1, int var2, boolean var3)
    {
        boolean var7 = false;
        boolean var8 = false;
        CoverLib$PlacementValidator var9 = new CoverLib$PlacementValidator(var0, var1);
        return var9.checkPlacement(var2, var3);
    }

    private static boolean canAddCover(World var0, MovingObjectPosition var1, int var2)
    {
        if (var0.canPlaceEntityOnSide(blockCoverPlate.blockID, var1.blockX, var1.blockY, var1.blockZ, false, var1.sideHit, (Entity)null))
        {
            return true;
        }
        else
        {
            ICoverable var3 = (ICoverable)CoreLib.getTileEntity(var0, var1.blockX, var1.blockY, var1.blockZ, ICoverable.class);
            return var3 == null ? false : var3.canAddCover(var1.subHit, var2);
        }
    }

    private static int extractCoverSide(MovingObjectPosition var0)
    {
        byte var1 = 0;
        double var2 = var0.hitVec.xCoord - (double)var0.blockX - 0.5D;
        double var4 = var0.hitVec.yCoord - (double)var0.blockY - 0.5D;
        double var6 = var0.hitVec.zCoord - (double)var0.blockZ - 0.5D;
        float var8 = 0.25F;

        switch (var0.sideHit)
        {
            case 0:
            case 1:
                if (var6 > (double)(-var8) && var6 < (double)var8 && var2 > (double)(-var8) && var2 < (double)var8)
                {
                    return var0.sideHit;
                }
                else if (var6 > var2)
                {
                    if (var6 > -var2)
                    {
                        return 3;
                    }

                    return 4;
                }
                else
                {
                    if (var6 > -var2)
                    {
                        return 5;
                    }

                    return 2;
                }

            case 2:
            case 3:
                if (var4 > (double)(-var8) && var4 < (double)var8 && var2 > (double)(-var8) && var2 < (double)var8)
                {
                    return var0.sideHit;
                }
                else if (var4 > var2)
                {
                    if (var4 > -var2)
                    {
                        return 1;
                    }

                    return 4;
                }
                else
                {
                    if (var4 > -var2)
                    {
                        return 5;
                    }

                    return 0;
                }

            case 4:
            case 5:
                if (var4 > (double)(-var8) && var4 < (double)var8 && var6 > (double)(-var8) && var6 < (double)var8)
                {
                    return var0.sideHit;
                }
                else if (var4 > var6)
                {
                    if (var4 > -var6)
                    {
                        return 1;
                    }

                    return 2;
                }
                else
                {
                    if (var4 > -var6)
                    {
                        return 3;
                    }

                    return 0;
                }

            default:
                return var1;
        }
    }

    private static int extractCoverAxis(MovingObjectPosition var0)
    {
        switch (var0.sideHit)
        {
            case 0:
            case 1:
                return var0.hitVec.yCoord - (double)var0.blockY > 0.5D ? 1 : 0;

            case 2:
            case 3:
                return var0.hitVec.zCoord - (double)var0.blockZ > 0.5D ? 1 : 0;

            default:
                return var0.hitVec.xCoord - (double)var0.blockX > 0.5D ? 1 : 0;
        }
    }

    private static void stepDir(MovingObjectPosition var0)
    {
        switch (var0.sideHit)
        {
            case 0:
                --var0.blockY;
                break;

            case 1:
                ++var0.blockY;
                break;

            case 2:
                --var0.blockZ;
                break;

            case 3:
                ++var0.blockZ;
                break;

            case 4:
                --var0.blockX;
                break;

            default:
                ++var0.blockX;
        }
    }

    private static boolean isClickOutside(MovingObjectPosition var0)
    {
        if (var0.subHit < 0)
        {
            return true;
        }
        else if (var0.subHit < 6)
        {
            return var0.sideHit != (var0.subHit ^ 1);
        }
        else
        {
            int var1;

            if (var0.subHit < 14)
            {
                var1 = var0.subHit - 6;
                var1 = var1 >> 2 | (var1 & 3) << 1;
                return ((var0.sideHit ^ var1 >> (var0.sideHit >> 1)) & 1) == 0;
            }
            else if (var0.subHit < 26)
            {
                var1 = var0.subHit - 14;
                var1 = stripToCoverMask(var1);
                return (var1 & 1 << (var0.sideHit ^ 1)) <= 0;
            }
            else
            {
                return var0.subHit < 29 ? true : var0.subHit == 29;
            }
        }
    }

    public static MovingObjectPosition getPlacement(World var0, MovingObjectPosition var1, int var2)
    {
        MovingObjectPosition var7 = new MovingObjectPosition(var1.blockX, var1.blockY, var1.blockZ, var1.sideHit, var1.hitVec);
        int var8 = damageToCoverValue(var2);
        int var3;

        switch (var2 >> 8)
        {
            case 0:
            case 16:
            case 17:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
                var3 = extractCoverSide(var1);

                if (var3 != var7.sideHit)
                {
                    var7.subHit = var3;

                    if (!isClickOutside(var1) && canAddCover(var0, var7, var8))
                    {
                        return var7;
                    }
                    else
                    {
                        stepDir(var7);

                        if (canAddCover(var0, var7, var8))
                        {
                            return var7;
                        }

                        return null;
                    }
                }
                else
                {
                    if (!isClickOutside(var1))
                    {
                        var7.subHit = var3 ^ 1;

                        if (canAddCover(var0, var7, var8))
                        {
                            return var7;
                        }
                    }

                    var7.subHit = var3;

                    if (canAddCover(var0, var7, var8))
                    {
                        return var7;
                    }
                    else if (!isClickOutside(var1))
                    {
                        return null;
                    }
                    else
                    {
                        stepDir(var7);
                        var7.subHit = var3 ^ 1;

                        if (canAddCover(var0, var7, var8))
                        {
                            return var7;
                        }

                        return null;
                    }
                }

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                return null;

            case 18:
            case 19:
            case 20:
            case 35:
            case 36:
            case 37:
            case 38:
                double var9 = var1.hitVec.xCoord - (double)var1.blockX;
                double var11 = var1.hitVec.yCoord - (double)var1.blockY;
                double var13 = var1.hitVec.zCoord - (double)var1.blockZ;
                var3 = 0;

                if (var13 > 0.5D)
                {
                    ++var3;
                }

                if (var9 > 0.5D)
                {
                    var3 += 2;
                }

                if (var11 > 0.5D)
                {
                    var3 += 4;
                }

                switch (var1.sideHit)
                {
                    case 0:
                        var3 &= 3;
                        break;

                    case 1:
                        var3 |= 4;
                        break;

                    case 2:
                        var3 &= 6;
                        break;

                    case 3:
                        var3 |= 1;
                        break;

                    case 4:
                        var3 &= 5;
                        break;

                    default:
                        var3 |= 2;
                }

                int var4;

                switch (var1.sideHit)
                {
                    case 0:
                    case 1:
                        var4 = var3 ^ 4;
                        break;

                    case 2:
                    case 3:
                        var4 = var3 ^ 1;
                        break;

                    default:
                        var4 = var3 ^ 2;
                }

                if (isClickOutside(var1))
                {
                    var7.subHit = var4 + 6;
                    stepDir(var7);

                    if (canAddCover(var0, var7, var8))
                    {
                        return var7;
                    }

                    return null;
                }
                else
                {
                    var7.subHit = var4 + 6;

                    if (canAddCover(var0, var7, var8))
                    {
                        return var7;
                    }
                    else
                    {
                        var7.subHit = var3 + 6;

                        if (canAddCover(var0, var7, var8))
                        {
                            return var7;
                        }

                        return null;
                    }
                }

            case 21:
            case 22:
            case 23:
            case 39:
            case 40:
            case 41:
            case 42:
                var3 = extractCoverSide(var1);

                if (var3 == var7.sideHit)
                {
                    return null;
                }
                else
                {
                    int var5 = coverToStripMask(var3);
                    int var6;

                    if (!isClickOutside(var1))
                    {
                        var6 = var5 & coverToStripMask(var7.sideHit ^ 1);
                        var7.subHit = 14 + Integer.numberOfTrailingZeros(var6);

                        if (canAddCover(var0, var7, var8))
                        {
                            return var7;
                        }
                        else
                        {
                            var6 = var5 & coverToStripMask(var7.sideHit);
                            var7.subHit = 14 + Integer.numberOfTrailingZeros(var6);

                            if (canAddCover(var0, var7, var8))
                            {
                                return var7;
                            }

                            return null;
                        }
                    }
                    else
                    {
                        stepDir(var7);
                        var6 = var5 & coverToStripMask(var7.sideHit ^ 1);
                        var7.subHit = 14 + Integer.numberOfTrailingZeros(var6);

                        if (canAddCover(var0, var7, var8))
                        {
                            return var7;
                        }

                        return null;
                    }
                }

            case 43:
            case 44:
            case 45:
                var3 = extractCoverSide(var1);

                if (var3 != var7.sideHit && var3 != (var7.sideHit ^ 1))
                {
                    return null;
                }
                else
                {
                    if (isClickOutside(var1))
                    {
                        stepDir(var7);
                    }

                    var7.subHit = (var3 >> 1) + 26;
                    return canAddCover(var0, var7, var8) ? var7 : null;
                }
        }
    }

    public static void replaceWithCovers(World var0, int var1, int var2, int var3, int var4, short[] var5)
    {
        BlockMultipart.removeMultipart(var0, var1, var2, var3);

        if (blockCoverPlate != null)
        {
            if (var4 != 0)
            {
                var0.setBlockMetadataWithNotify(var1, var2, var3, blockCoverPlate.blockID, 0);
                TileCovered var6 = (TileCovered)CoreLib.getTileEntity(var0, var1, var2, var3, TileCovered.class);

                if (var6 != null)
                {
                    var6.CoverSides = var4;
                    var6.Covers = var5;
                    RedPowerLib.updateIndirectNeighbors(var0, var1, var2, var3, blockCoverPlate.blockID);
                }
            }
        }
    }

    public static boolean tryMakeCompatible(World var0, WorldCoord var1, int var2, int var3)
    {
        TileCovered var4 = (TileCovered)CoreLib.getTileEntity(var0, var1, TileCovered.class);

        if (var4 == null)
        {
            return false;
        }
        else
        {
            int var5 = var3 >> 8;
            int var6 = var3 & 255;
            int var7 = var4.getExtendedID();

            if (var7 == var5)
            {
                return var4.getExtendedMetadata() == var6;
            }
            else if (var7 != 0)
            {
                return false;
            }
            else
            {
                short[] var8 = var4.Covers;
                int var9 = var4.CoverSides;
                BlockMultipart.removeMultipart(var0, var1.x, var1.y, var1.z);

                if (!var0.setBlockMetadataWithNotify(var1.x, var1.y, var1.z, var2, var5))
                {
                    return false;
                }
                else
                {
                    var4 = (TileCovered)CoreLib.getTileEntity(var0, var1, TileCovered.class);

                    if (var4 == null)
                    {
                        return true;
                    }
                    else
                    {
                        var4.Covers = var8;
                        var4.CoverSides = var9;
                        var4.setExtendedMetadata(var6);
                        return true;
                    }
                }
            }
        }
    }

    public static ItemStack getItemStack(int var0)
    {
        return materials[var0];
    }

    public static Block getBlock(int var0)
    {
        ItemStack var1 = materials[var0];
        return Block.blocksList[var1.itemID];
    }

    public static String getName(int var0)
    {
        return names[var0];
    }

    public static String getDesc(int var0)
    {
        return descs[var0];
    }

    public static int getHardness(int var0)
    {
        return hardness[var0];
    }

    public static float getMiningHardness(int var0)
    {
        return miningHardness[var0];
    }

    public static boolean isTransparent(int var0)
    {
        return transparency[var0];
    }
}
