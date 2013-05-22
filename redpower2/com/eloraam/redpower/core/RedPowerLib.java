package com.eloraam.redpower.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class RedPowerLib
{
    private static HashSet powerClassMapping = new HashSet();
    private static HashSet blockUpdates = new HashSet();
    private static LinkedList powerSearch = new LinkedList();
    private static HashSet powerSearchTest = new HashSet();
    private static boolean searching = false;

    public static void notifyBlock(World var0, int var1, int var2, int var3, int var4)
    {
        Block var5 = Block.blocksList[var0.getBlockId(var1, var2, var3)];

        if (var5 != null)
        {
            var5.onNeighborBlockChange(var0, var1, var2, var3, var4);
        }
    }

    public static void updateIndirectNeighbors(World var0, int var1, int var2, int var3, int var4)
    {

            for (int var5 = -3; var5 <= 3; ++var5)
            {
                for (int var6 = -3; var6 <= 3; ++var6)
                {
                    for (int var7 = -3; var7 <= 3; ++var7)
                    {
                        int var8 = var5 < 0 ? -var5 : var5;
                        var8 += var6 < 0 ? -var6 : var6;
                        var8 += var7 < 0 ? -var7 : var7;

                        if (var8 <= 3)
                        {
                            notifyBlock(var0, var1 + var5, var2 + var6, var3 + var7, var4);
                        }
                    }
                }
            }

    }

    public static boolean isBlockRedstone(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        switch (var4)
        {
            case 0:
                --var2;
                break;

            case 1:
                ++var2;
                break;

            case 2:
                --var3;
                break;

            case 3:
                ++var3;
                break;

            case 4:
                --var1;
                break;

            case 5:
                ++var1;
        }

        int var5 = var0.getBlockId(var1, var2, var3);
        return var5 == Block.redstoneWire.blockID;
    }

    public static boolean isSideNormal(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        switch (var4)
        {
            case 0:
                --var2;
                break;

            case 1:
                ++var2;
                break;

            case 2:
                --var3;
                break;

            case 3:
                ++var3;
                break;

            case 4:
                --var1;
                break;

            case 5:
                ++var1;
        }

        var4 ^= 1;

        if (var0.isBlockNormalCube(var1, var2, var3))
        {
            return true;
        }
        else
        {
            var0.getBlockId(var1, var2, var3);
            IMultipart var6 = (IMultipart)CoreLib.getTileEntity(var0, var1, var2, var3, IMultipart.class);
            return var6 == null ? false : var6.isSideNormal(var4);
        }
    }

    public static boolean canSupportWire(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        switch (var4)
        {
            case 0:
                --var2;
                break;

            case 1:
                ++var2;
                break;

            case 2:
                --var3;
                break;

            case 3:
                ++var3;
                break;

            case 4:
                --var1;
                break;

            case 5:
                ++var1;
        }

        var4 ^= 1;

        if (var0 instanceof World)
        {
            World var5 = (World)var0;

            if (!var5.blockExists(var1, var2, var3))
            {
                return true;
            }

            if (var5.isBlockSolidOnSide(var1, var2, var3, ForgeDirection.getOrientation(var4)))
            {
                return true;
            }
        }

        if (var0.isBlockNormalCube(var1, var2, var3))
        {
            return true;
        }
        else
        {
            int var7 = var0.getBlockId(var1, var2, var3);

            if (var7 == Block.pistonMoving.blockID)
            {
                return true;
            }
            else if (var7 != Block.pistonStickyBase.blockID && var7 != Block.pistonBase.blockID)
            {
                IMultipart var8 = (IMultipart)CoreLib.getTileEntity(var0, var1, var2, var3, IMultipart.class);
                return var8 == null ? false : var8.isSideNormal(var4);
            }
            else
            {
                int var6 = var0.getBlockMetadata(var1, var2, var3) & 7;
                return var1 != var6 && var6 != 7;
            }
        }
    }

    public static int isStrongPoweringTo(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        int var5 = var0.getBlockId(var1, var2, var3);

        if (var5 == 0)
        {
            return 0;
        }
        else if (searching && var5 == Block.redstoneWire.blockID)
        {
            return 0;
        }
        else if (!(var0 instanceof World))
        {
            return 0;
        }
        else
        {
            World var6 = (World)var0;
            return Block.blocksList[var5].isProvidingStrongPower(var6, var1, var2, var3, var4);
        }
    }

    public static int isStrongPowered(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        return 0;
    }

    public static int isWeakPoweringTo(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        return 0;
    }

    public static boolean isPoweringTo(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        int var5 = var0.getBlockId(var1, var2, var3);

        if (var5 == 0)
        {
            return false;
        }

        else
        {
            if (var4 > 1 && var5 == Block.redstoneWire.blockID)
            {
                if (searching)
                {
                    return false;
                }


            }

            return false;
        }
    }

    public static boolean isPowered(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5)
    {
        return false;
    }

    private static int getSidePowerMask(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5)
    {
        IRedPowerConnectable var6 = (IRedPowerConnectable)CoreLib.getTileEntity(var0, var1, var2, var3, IRedPowerConnectable.class);
        int var7 = getConDirMask(var5);

        if (var6 != null)
        {
            int var8 = var6.getPoweringMask(var4);
            var8 = (var8 & 1431655765) << 1 | (var8 & 715827882) >> 1;
            return var8 & var7;
        }
        else
        {
            return 0;
        }
    }

    public static int getPowerState(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5)
    {
        int var6 = 0;

        if ((var4 & 17895680) > 0)
        {
            var6 |= getSidePowerMask(var0, var1, var2 - 1, var3, var5, 0);
        }

        if ((var4 & 35791360) > 0)
        {
            var6 |= getSidePowerMask(var0, var1, var2 + 1, var3, var5, 1);
        }

        if ((var4 & 71565329) > 0)
        {
            var6 |= getSidePowerMask(var0, var1, var2, var3 - 1, var5, 2);
        }

        if ((var4 & 143130658) > 0)
        {
            var6 |= getSidePowerMask(var0, var1, var2, var3 + 1, var5, 3);
        }

        if ((var4 & 268452932) > 0)
        {
            var6 |= getSidePowerMask(var0, var1 - 1, var2, var3, var5, 4);
        }

        if ((var4 & 536905864) > 0)
        {
            var6 |= getSidePowerMask(var0, var1 + 1, var2, var3, var5, 5);
        }

        return var6 & var4;
    }

    public static int getRotPowerState(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5, int var6)
    {
        int var7 = mapRotToCon(var4, var5);
        int var8 = getPowerState(var0, var1, var2, var3, var7, var6);
        return mapConToRot(var8, var5);
    }

    public static int getConDirMask(int var0)
    {
        switch (var0)
        {
            case 0:
                return 17895680;

            case 1:
                return 35791360;

            case 2:
                return 71565329;

            case 3:
                return 143130658;

            case 4:
                return 268452932;

            default:
                return 536905864;
        }
    }

    public static int mapConToLocal(int var0, int var1)
    {
        var0 >>= var1 * 4;
        var0 &= 15;

        switch (var1)
        {
            case 0:
                return var0;

            case 1:
                var0 ^= ((var0 ^ var0 >> 1) & 1) * 3;
                return var0;

            case 2:
            default:
                var0 ^= ((var0 ^ var0 >> 2) & 3) * 5;
                return var0;

            case 3:
            case 4:
                var0 ^= ((var0 ^ var0 >> 2) & 3) * 5;
                var0 ^= ((var0 ^ var0 >> 1) & 1) * 3;
                return var0;
        }
    }

    public static int mapLocalToCon(int var0, int var1)
    {
        switch (var1)
        {
            case 0:
                break;

            case 1:
                var0 ^= ((var0 ^ var0 >> 1) & 1) * 3;
                break;

            case 2:
            default:
                var0 ^= ((var0 ^ var0 >> 2) & 3) * 5;
                break;

            case 3:
            case 4:
                var0 ^= ((var0 ^ var0 >> 1) & 1) * 3;
                var0 ^= ((var0 ^ var0 >> 2) & 3) * 5;
        }

        return var0 << var1 * 4;
    }

    public static int mapRotToLocal(int var0, int var1)
    {
        var0 = var0 << var1 | var0 >> 4 - var1;
        var0 &= 15;
        return var0 & 8 | (var0 & 3) << 1 | var0 >> 2 & 1;
    }

    public static int mapLocalToRot(int var0, int var1)
    {
        var0 = var0 & 8 | (var0 & 6) >> 1 | var0 << 2 & 4;
        var0 = var0 << 4 - var1 | var0 >> var1;
        return var0 & 15;
    }

    public static int mapConToRot(int var0, int var1)
    {
        return mapLocalToRot(mapConToLocal(var0, var1 >> 2), var1 & 3);
    }

    public static int mapRotToCon(int var0, int var1)
    {
        return mapLocalToCon(mapRotToLocal(var0, var1 & 3), var1 >> 2);
    }

    public static int getDirToRedstone(int var0)
    {
        switch (var0)
        {
            case 2:
                return 0;

            case 3:
                return 2;

            case 4:
                return 3;

            case 5:
                return 1;

            default:
                return 0;
        }
    }

    public static int getConSides(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5)
    {
        int var6 = var0.getBlockId(var1, var2, var3);

        if (var6 == 0)
        {
            return 0;
        }
        else
        {
            IConnectable var7 = (IConnectable)CoreLib.getTileEntity(var0, var1, var2, var3, IConnectable.class);
            int var8;

            if (var7 != null)
            {
                var8 = var7.getConnectClass(var4);
                return isCompatible(var8, var5) ? var7.getConnectableMask() : 0;
            }
            else if (!isCompatible(0, var5))
            {
                return 0;
            }
            else if (var6 != Block.pistonBase.blockID && var6 != Block.pistonStickyBase.blockID)
            {
                if (var6 == Block.pistonMoving.blockID)
                {
                    TileEntity var12 = var0.getBlockTileEntity(var1, var2, var3);

                    if (!(var12 instanceof TileEntityPiston))
                    {
                        return 0;
                    }
                    else
                    {
                        TileEntityPiston var9 = (TileEntityPiston)var12;
                        int var10 = var9.getStoredBlockID();

                        if (var10 != Block.pistonBase.blockID && var10 != Block.pistonStickyBase.blockID)
                        {
                            return 0;
                        }
                        else
                        {
                            int var11 = var9.getBlockMetadata() & 7;
                            return var11 == 7 ? 0 : 1073741823 ^ getConDirMask(var11);
                        }
                    }
                }
                else if (var6 != Block.dispenser.blockID && var6 != Block.stoneButton.blockID && var6 != Block.woodenButton.blockID && var6 != Block.lever.blockID)
                {
                    if (var6 != Block.torchRedstoneActive.blockID && var6 != Block.torchRedstoneIdle.blockID)
                    {
                        if (var6 != Block.redstoneRepeaterIdle.blockID && var6 != Block.redstoneRepeaterActive.blockID)
                        {
                            return Block.blocksList[var6].canConnectRedstone(var0, var1, var2, var3, getDirToRedstone(var4)) ? getConDirMask(var4) : 0;
                        }
                        else
                        {
                            var8 = var0.getBlockMetadata(var1, var2, var3) & 1;
                            return var8 > 0 ? 12 : 3;
                        }
                    }
                    else
                    {
                        return 1073741823;
                    }
                }
                else
                {
                    return 1073741823;
                }
            }
            else
            {
                var8 = var0.getBlockMetadata(var1, var2, var3) & 7;
                return var8 == 7 ? 0 : 1073741823 ^ getConDirMask(var8);
            }
        }
    }

    private static int getES1(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5, int var6)
    {
        int var7 = var0.getBlockId(var1, var2, var3);

        if (var7 == 0)
        {
            return 0;
        }
        else
        {
            IConnectable var8 = (IConnectable)CoreLib.getTileEntity(var0, var1, var2, var3, IConnectable.class);

            if (var8 != null)
            {
                int var9 = var8.getCornerPowerMode();

                if (var6 != 0 && var9 != 0)
                {
                    if (var6 == 2 && var9 == 2)
                    {
                        return 0;
                    }
                    else if (var6 == 3 && var9 == 1)
                    {
                        return 0;
                    }
                    else
                    {
                        int var10 = var8.getConnectClass(var4);
                        return isCompatible(var10, var5) ? var8.getConnectableMask() : 0;
                    }
                }
                else
                {
                    return 0;
                }
            }
            else
            {
                return 0;
            }
        }
    }

    public static int getExtConSides(IBlockAccess var0, IConnectable var1, int var2, int var3, int var4, int var5, int var6)
    {
        int var7 = var1.getConnectableMask();
        var7 &= getConDirMask(var5) & 16777215;

        if (var7 == 0)
        {
            return 0;
        }
        else
        {
            int var8 = var0.getBlockId(var2, var3, var4);
            int var10;

            if (CoverLib.blockCoverPlate != null && var8 == CoverLib.blockCoverPlate.blockID)
            {
                if (var0.getBlockMetadata(var2, var3, var4) != 0)
                {
                    return 0;
                }

                ICoverable var9 = (ICoverable)CoreLib.getTileEntity(var0, var2, var3, var4, ICoverable.class);

                if (var9 == null)
                {
                    return 0;
                }

                var10 = var9.getCoverMask();

                if ((var10 & 1 << (var5 ^ 1)) > 0)
                {
                    return 0;
                }

                var10 |= var10 << 12;
                var10 |= var10 << 6;
                var10 &= 197379;
                var10 |= var10 << 3;
                var10 &= 1118481;
                var10 |= var10 << 2;
                var10 |= var10 << 1;
                var7 &= ~var10;
            }
            else if (var8 != 0 && var8 != Block.waterMoving.blockID && var8 != Block.waterStill.blockID)
            {
                return 0;
            }

            int var11 = var1.getConnectClass(var5);
            var10 = 0;

            if ((var7 & 15) > 0)
            {
                var10 |= getES1(var0, var2, var3 - 1, var4, 1, var11, var6) & 2236928;
            }

            if ((var7 & 240) > 0)
            {
                var10 |= getES1(var0, var2, var3 + 1, var4, 0, var11, var6) & 1118464;
            }

            if ((var7 & 3840) > 0)
            {
                var10 |= getES1(var0, var2, var3, var4 - 1, 3, var11, var6) & 8912930;
            }

            if ((var7 & 61440) > 0)
            {
                var10 |= getES1(var0, var2, var3, var4 + 1, 2, var11, var6) & 4456465;
            }

            if ((var7 & 983040) > 0)
            {
                var10 |= getES1(var0, var2 - 1, var3, var4, 5, var11, var6) & 34952;
            }

            if ((var7 & 15728640) > 0)
            {
                var10 |= getES1(var0, var2 + 1, var3, var4, 4, var11, var6) & 17476;
            }

            var10 >>= (var5 ^ 1) << 2;
            var10 = (var10 & 10) >> 1 | (var10 & 5) << 1;
            var10 |= var10 << 6;
            var10 |= var10 << 3;
            var10 &= 4369;
            var10 <<= var5 & 1;

            switch (var5)
            {
                case 0:
                case 1:
                    return var10 << 8;

                case 2:
                case 3:
                    return var10 << 10 & 16711680 | var10 & 255;

                default:
                    return var10 << 2;
            }
        }
    }

    public static int getConnections(IBlockAccess var0, IConnectable var1, int var2, int var3, int var4)
    {
        int var5 = var1.getConnectableMask();
        int var7 = 0;
        int var6;

        if ((var5 & 17895680) > 0)
        {
            var6 = var1.getConnectClass(0);
            var7 |= getConSides(var0, var2, var3 - 1, var4, 1, var6) & 35791360;
        }

        if ((var5 & 35791360) > 0)
        {
            var6 = var1.getConnectClass(1);
            var7 |= getConSides(var0, var2, var3 + 1, var4, 0, var6) & 17895680;
        }

        if ((var5 & 71565329) > 0)
        {
            var6 = var1.getConnectClass(2);
            var7 |= getConSides(var0, var2, var3, var4 - 1, 3, var6) & 143130658;
        }

        if ((var5 & 143130658) > 0)
        {
            var6 = var1.getConnectClass(3);
            var7 |= getConSides(var0, var2, var3, var4 + 1, 2, var6) & 71565329;
        }

        if ((var5 & 268452932) > 0)
        {
            var6 = var1.getConnectClass(4);
            var7 |= getConSides(var0, var2 - 1, var3, var4, 5, var6) & 536905864;
        }

        if ((var5 & 536905864) > 0)
        {
            var6 = var1.getConnectClass(5);
            var7 |= getConSides(var0, var2 + 1, var3, var4, 4, var6) & 268452932;
        }

        var7 = var7 << 1 & 715827882 | var7 >> 1 & 357913941;
        var7 &= var5;
        return var7;
    }

    public static int getExtConnections(IBlockAccess var0, IConnectable var1, int var2, int var3, int var4)
    {
        byte var5 = 0;
        int var6 = var1.getCornerPowerMode();
        int var7 = var5 | getExtConSides(var0, var1, var2, var3 - 1, var4, 0, var6);
        var7 |= getExtConSides(var0, var1, var2, var3 + 1, var4, 1, var6);
        var7 |= getExtConSides(var0, var1, var2, var3, var4 - 1, 2, var6);
        var7 |= getExtConSides(var0, var1, var2, var3, var4 + 1, 3, var6);
        var7 |= getExtConSides(var0, var1, var2 - 1, var3, var4, 4, var6);
        var7 |= getExtConSides(var0, var1, var2 + 1, var3, var4, 5, var6);
        return var7;
    }

    public static int getExtConnectionExtras(IBlockAccess var0, IConnectable var1, int var2, int var3, int var4)
    {
        byte var5 = 0;
        int var6 = var5 | getExtConSides(var0, var1, var2, var3 - 1, var4, 0, 3);
        var6 |= getExtConSides(var0, var1, var2, var3 + 1, var4, 1, 3);
        var6 |= getExtConSides(var0, var1, var2, var3, var4 - 1, 2, 3);
        var6 |= getExtConSides(var0, var1, var2, var3, var4 + 1, 3, 3);
        var6 |= getExtConSides(var0, var1, var2 - 1, var3, var4, 4, 3);
        var6 |= getExtConSides(var0, var1, var2 + 1, var3, var4, 5, 3);
        return var6;
    }

    public static int getTileCurrentStrength(World var0, int var1, int var2, int var3, int var4, int var5)
    {
        IRedPowerConnectable var6 = (IRedPowerConnectable)CoreLib.getTileEntity(var0, var1, var2, var3, IRedPowerConnectable.class);

        if (var6 == null)
        {
            return -1;
        }
        else if (var6 instanceof IRedPowerWiring)
        {
            IRedPowerWiring var7 = (IRedPowerWiring)var6;
            return var7.getCurrentStrength(var4, var5);
        }
        else
        {
            return (var6.getPoweringMask(var5) & var4) > 0 ? 255 : -1;
        }
    }

    public static int getTileOrRedstoneCurrentStrength(World var0, int var1, int var2, int var3, int var4, int var5)
    {
        int var6 = var0.getBlockId(var1, var2, var3);

        if (var6 == 0)
        {
            return -1;
        }
        else if (var6 == Block.redstoneWire.blockID)
        {
            int var9 = var0.getBlockMetadata(var1, var2, var3);
            return var9 > 0 ? var9 : -1;
        }
        else
        {
            IRedPowerConnectable var7 = (IRedPowerConnectable)CoreLib.getTileEntity(var0, var1, var2, var3, IRedPowerConnectable.class);

            if (var7 == null)
            {
                return -1;
            }
            else if (var7 instanceof IRedPowerWiring)
            {
                IRedPowerWiring var8 = (IRedPowerWiring)var7;
                return var8.getCurrentStrength(var4, var5);
            }
            else
            {
                return (var7.getPoweringMask(var5) & var4) > 0 ? 255 : -1;
            }
        }
    }

    private static int getIndCur(World var0, int var1, int var2, int var3, int var4, int var5, int var6)
    {
        int var7;

        switch (var4)
        {
            case 0:
                --var2;
                var7 = var5 + 2;
                break;

            case 1:
                ++var2;
                var7 = var5 + 2;
                break;

            case 2:
                --var3;
                var7 = var5 + (var5 & 2);
                break;

            case 3:
                ++var3;
                var7 = var5 + (var5 & 2);
                break;

            case 4:
                --var1;
                var7 = var5;
                break;

            default:
                ++var1;
                var7 = var5;
        }

        int var8;

        switch (var7)
        {
            case 0:
                --var2;
                var8 = var4 - 2;
                break;

            case 1:
                ++var2;
                var8 = var4 - 2;
                break;

            case 2:
                --var3;
                var8 = var4 & 1 | (var4 & 4) >> 1;
                break;

            case 3:
                ++var3;
                var8 = var4 & 1 | (var4 & 4) >> 1;
                break;

            case 4:
                --var1;
                var8 = var4;
                break;

            default:
                ++var1;
                var8 = var4;
        }

        return getTileCurrentStrength(var0, var1, var2, var3, 1 << (var8 ^ 1) << ((var7 ^ 1) << 2), var6);
    }

    public static int getMaxCurrentStrength(World var0, int var1, int var2, int var3, int var4, int var5, int var6)
    {
        int var7 = -1;
        int var8 = var4 << 1 & 715827882 | var4 >> 1 & 357913941;

        if ((var4 & 17895680) > 0)
        {
            var7 = Math.max(var7, getTileOrRedstoneCurrentStrength(var0, var1, var2 - 1, var3, var8 & 35791360, var6));
        }

        if ((var4 & 35791360) > 0)
        {
            var7 = Math.max(var7, getTileOrRedstoneCurrentStrength(var0, var1, var2 + 1, var3, var8 & 17895680, var6));
        }

        if ((var4 & 71565329) > 0)
        {
            var7 = Math.max(var7, getTileOrRedstoneCurrentStrength(var0, var1, var2, var3 - 1, var8 & 143130658, var6));
        }

        if ((var4 & 143130658) > 0)
        {
            var7 = Math.max(var7, getTileOrRedstoneCurrentStrength(var0, var1, var2, var3 + 1, var8 & 71565329, var6));
        }

        if ((var4 & 268452932) > 0)
        {
            var7 = Math.max(var7, getTileOrRedstoneCurrentStrength(var0, var1 - 1, var2, var3, var8 & 536905864, var6));
        }

        if ((var4 & 536905864) > 0)
        {
            var7 = Math.max(var7, getTileOrRedstoneCurrentStrength(var0, var1 + 1, var2, var3, var8 & 268452932, var6));
        }

        for (int var9 = 0; var9 < 6; ++var9)
        {
            for (int var10 = 0; var10 < 4; ++var10)
            {
                if ((var5 & 1 << var9 * 4 + var10) > 0)
                {
                    var7 = Math.max(var7, getIndCur(var0, var1, var2, var3, var9, var10, var6));
                }
            }
        }

        return var7;
    }

    public static void addUpdateBlock(int var0, int var1, int var2)
    {
        for (int var3 = -3; var3 <= 3; ++var3)
        {
            for (int var4 = -3; var4 <= 3; ++var4)
            {
                for (int var5 = -3; var5 <= 3; ++var5)
                {
                    int var6 = var3 < 0 ? -var3 : var3;
                    var6 += var4 < 0 ? -var4 : var4;
                    var6 += var5 < 0 ? -var5 : var5;

                    if (var6 <= 3)
                    {
                        blockUpdates.add(Arrays.asList(new Integer[] {Integer.valueOf(var0 + var3), Integer.valueOf(var1 + var4), Integer.valueOf(var2 + var5)}));
                    }
                }
            }
        }
    }

    public static void addStartSearchBlock(int var0, int var1, int var2)
    {
        List var3 = Arrays.asList(new Integer[] {Integer.valueOf(var0), Integer.valueOf(var1), Integer.valueOf(var2)});

        if (!powerSearchTest.contains(var3))
        {
            powerSearch.addLast(var3);
            powerSearchTest.add(var3);
        }
    }

    public static void addSearchBlock(int var0, int var1, int var2)
    {
        addStartSearchBlock(var0, var1, var2);
        blockUpdates.add(Arrays.asList(new Integer[] {Integer.valueOf(var0), Integer.valueOf(var1), Integer.valueOf(var2)}));
    }

    private static void addIndBl(int var0, int var1, int var2, int var3, int var4)
    {
        int var5;

        switch (var3)
        {
            case 0:
                --var1;
                var5 = var4 + 2;
                break;

            case 1:
                ++var1;
                var5 = var4 + 2;
                break;

            case 2:
                --var2;
                var5 = var4 + (var4 & 2);
                break;

            case 3:
                ++var2;
                var5 = var4 + (var4 & 2);
                break;

            case 4:
                --var0;
                var5 = var4;
                break;

            default:
                ++var0;
                var5 = var4;
        }

        switch (var5)
        {
            case 0:
                --var1;
                break;

            case 1:
                ++var1;
                break;

            case 2:
                --var2;
                break;

            case 3:
                ++var2;
                break;

            case 4:
                --var0;
                break;

            case 5:
                ++var0;
        }

        addSearchBlock(var0, var1, var2);
    }

    public static void addSearchBlocks(int var0, int var1, int var2, int var3, int var4)
    {
        int var5 = var3 << 1 & 11184810 | var3 >> 1 & 5592405;

        if ((var3 & 17895680) > 0)
        {
            addSearchBlock(var0, var1 - 1, var2);
        }

        if ((var3 & 35791360) > 0)
        {
            addSearchBlock(var0, var1 + 1, var2);
        }

        if ((var3 & 71565329) > 0)
        {
            addSearchBlock(var0, var1, var2 - 1);
        }

        if ((var3 & 143130658) > 0)
        {
            addSearchBlock(var0, var1, var2 + 1);
        }

        if ((var3 & 268452932) > 0)
        {
            addSearchBlock(var0 - 1, var1, var2);
        }

        if ((var3 & 536905864) > 0)
        {
            addSearchBlock(var0 + 1, var1, var2);
        }

        for (int var6 = 0; var6 < 6; ++var6)
        {
            for (int var7 = 0; var7 < 4; ++var7)
            {
                if ((var4 & 1 << var6 * 4 + var7) > 0)
                {
                    addIndBl(var0, var1, var2, var6, var7);
                }
            }
        }
    }

    public static void updateCurrent(World var0, int var1, int var2, int var3)
    {
        addStartSearchBlock(var1, var2, var3);

        if (!searching)
        {
            searching = true;

            while (powerSearch.size() > 0)
            {
                List var4 = (List)powerSearch.removeFirst();
                powerSearchTest.remove(var4);
                Integer[] var5 = (Integer[])((Integer[])var4.toArray());
                IRedPowerWiring var6 = (IRedPowerWiring)CoreLib.getTileEntity(var0, var5[0].intValue(), var5[1].intValue(), var5[2].intValue(), IRedPowerWiring.class);

                if (var6 != null)
                {
                    var6.updateCurrentStrength();
                }
            }

            searching = false;
            ArrayList var7 = new ArrayList(blockUpdates);
            blockUpdates.clear();

            for (int var8 = 0; var8 < var7.size(); ++var8)
            {
                Integer[] var9 = (Integer[])((Integer[])((List)var7.get(var8)).toArray());
                notifyBlock(var0, var9[0].intValue(), var9[1].intValue(), var9[2].intValue(), Block.redstoneWire.blockID);
                var0.markBlockForUpdate(var9[0].intValue(), var9[1].intValue(), var9[2].intValue());
            }
        }
    }

    public static int updateBlockCurrentStrength(World var0, IRedPowerWiring var1, int var2, int var3, int var4, int var5, int var6)
    {
        int var7 = var1.getConnectionMask() & var5;
        int var8 = var1.getExtConnectionMask() & var5;
        int var9 = -1;
        int var10 = 0;
        int var11 = 0;
        int var13;

        for (int var12 = var6; var12 > 0; var10 = Math.max(var10, var1.scanPoweringStrength(var7 | var8, var13)))
        {
            var13 = Integer.numberOfTrailingZeros(var12);
            var12 &= ~(1 << var13);
            var11 = Math.max(var11, var1.getCurrentStrength(var5, var13));
            var9 = Math.max(var9, getMaxCurrentStrength(var0, var2, var3, var4, var7, var8, var13));
        }

        if (var10 <= var11 && (var9 == var11 + 1 || var11 == 0 && var9 == 0))
        {
            return var11;
        }
        else if (var10 == var11 && var9 <= var11)
        {
            return var11;
        }
        else
        {
            var11 = Math.max(var10, var11);

            if (var11 >= var9)
            {
                if (var11 > var10)
                {
                    var11 = 0;
                }
            }
            else
            {
                var11 = Math.max(0, var9 - 1);
            }

            if ((var6 & 1) > 0)
            {
                addUpdateBlock(var2, var3, var4);
            }

            addSearchBlocks(var2, var3, var4, var7, var8);
            return var11;
        }
    }

    public static boolean isSearching()
    {
        return searching;
    }

    public static void addCompatibleMapping(int var0, int var1)
    {
        powerClassMapping.add(Arrays.asList(new Integer[] {Integer.valueOf(var0), Integer.valueOf(var1)}));
        powerClassMapping.add(Arrays.asList(new Integer[] {Integer.valueOf(var1), Integer.valueOf(var0)}));
    }

    public static boolean isCompatible(int var0, int var1)
    {
        return var0 == var1 || powerClassMapping.contains(Arrays.asList(new Integer[] {Integer.valueOf(var0), Integer.valueOf(var1)}));
    }
}
