package com.eloraam.redpower.core;

import com.eloraam.redpower.core.CoreLib$1;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class CoreLib
{
    public static Comparator itemStackComparator = new CoreLib$1();
    private static TreeMap oreMap = new TreeMap(itemStackComparator);
    public static String[] rawColorNames = new String[] {"white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};
    public static String[] enColorNames = new String[] {"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};
    public static int[] paintColors = new int[] {16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583};
    public static final Material materialRedpower = new Material(MapColor.woodColor);

    public static boolean isClient(World var0)
    {
        return var0.isRemote;
    }

    @Deprecated
    void initModule(String var1)
    {
        Class var2;

        try
        {
            var2 = Class.forName(var1);
        }
        catch (ClassNotFoundException var8)
        {
            return;
        }

        Method var3;

        try
        {
            var3 = var2.getDeclaredMethod("initialize", new Class[0]);
        }
        catch (NoSuchMethodException var7)
        {
            return;
        }

        try
        {
            var3.invoke((Object)null, new Object[0]);
        }
        catch (IllegalAccessException var5)
        {
            ;
        }
        catch (InvocationTargetException var6)
        {
            ;
        }
    }

    public static Object getTileEntity(IBlockAccess var0, int var1, int var2, int var3, Class var4)
    {
        TileEntity var5 = var0.getBlockTileEntity(var1, var2, var3);
        return !var4.isInstance(var5) ? null : var5;
    }

    public static Object getTileEntity(IBlockAccess var0, WorldCoord var1, Class var2)
    {
        TileEntity var3 = var0.getBlockTileEntity(var1.x, var1.y, var1.z);
        return !var2.isInstance(var3) ? null : var3;
    }

    public static Object getGuiTileEntity(World var0, int var1, int var2, int var3, Class var4)
    {
        if (var0.isRemote)
        {
            try
            {
                return var4.newInstance();
            }
            catch (InstantiationException var6)
            {
                return null;
            }
            catch (IllegalAccessException var7)
            {
                return null;
            }
        }
        else
        {
            TileEntity var5 = var0.getBlockTileEntity(var1, var2, var3);
            return !var4.isInstance(var5) ? null : var5;
        }
    }

    public static void markBlockDirty(World var0, int var1, int var2, int var3)
    {
        if (var0.blockExists(var1, var2, var3))
        {
            var0.getChunkFromBlockCoords(var1, var3).setChunkModified();
        }
    }

    public static int compareItemStack(ItemStack var0, ItemStack var1)
    {
        return var0.itemID != var1.itemID ? var0.itemID - var1.itemID : (var0.getItemDamage() == var1.getItemDamage() ? 0 : (var0.getItem().getHasSubtypes() ? var0.getItemDamage() - var1.getItemDamage() : 0));
    }

    static void registerOre(String var0, ItemStack var1)
    {
        oreMap.put(var1, var0);
    }

    public static void readOres()
    {
        String[] var0 = OreDictionary.getOreNames();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            String var3 = var0[var2];
            Iterator var4 = OreDictionary.getOres(var3).iterator();

            while (var4.hasNext())
            {
                ItemStack var5 = (ItemStack)var4.next();
                registerOre(var3, var5);
            }
        }
    }

    public static String getOreClass(ItemStack var0)
    {
        String var1 = (String)oreMap.get(var0);

        if (var1 != null)
        {
            return var1;
        }
        else
        {
            var0 = new ItemStack(var0.itemID, 1, -1);
            return (String)oreMap.get(var0);
        }
    }

    public static boolean matchItemStackOre(ItemStack var0, ItemStack var1)
    {
        String var2 = getOreClass(var0);
        String var3 = getOreClass(var1);
        return var2 != null && var3 != null && var2.equals(var3) ? true : compareItemStack(var0, var1) == 0;
    }

    public static void dropItem(World var0, int var1, int var2, int var3, ItemStack var4)
    {
        if (!isClient(var0))
        {
            double var5 = 0.7D;
            double var7 = (double)var0.rand.nextFloat() * var5 + (1.0D - var5) * 0.5D;
            double var9 = (double)var0.rand.nextFloat() * var5 + (1.0D - var5) * 0.5D;
            double var11 = (double)var0.rand.nextFloat() * var5 + (1.0D - var5) * 0.5D;
            EntityItem var13 = new EntityItem(var0, (double)var1 + var7, (double)var2 + var9, (double)var3 + var11, var4);
            var13.delayBeforeCanPickup = 10;
            var0.spawnEntityInWorld(var13);
        }
    }

    public static ItemStack copyStack(ItemStack var0, int var1)
    {
        return new ItemStack(var0.itemID, var1, var0.getItemDamage());
    }

    public static int rotToSide(int var0)
    {
        switch (var0)
        {
            case 0:
                return 5;

            case 1:
                return 3;

            case 2:
                return 4;

            default:
                return 2;
        }
    }

    public static MovingObjectPosition retraceBlock(World var0, EntityLiving var1, int var2, int var3, int var4)
    {
        Vec3 var5 = Vec3.createVectorHelper(var1.posX, var1.posY + 1.62D - (double)var1.yOffset, var1.posZ);
        Vec3 var6 = var1.getLook(1.0F);
        Vec3 var7 = var5.addVector(var6.xCoord * 5.0D, var6.yCoord * 5.0D, var6.zCoord * 5.0D);
        Block var8 = Block.blocksList[var0.getBlockId(var2, var3, var4)];
        return var8 == null ? null : var8.collisionRayTrace(var0, var2, var3, var4, var5, var7);
    }

    public static MovingObjectPosition traceBlock(EntityPlayer var0)
    {
        Vec3 var1 = Vec3.createVectorHelper(var0.posX, var0.posY + 1.62D - (double)var0.yOffset, var0.posZ);
        Vec3 var2 = var0.getLook(1.0F);
        Vec3 var3 = var1.addVector(var2.xCoord * 5.0D, var2.yCoord * 5.0D, var2.zCoord * 5.0D);
        return var0.worldObj.rayTraceBlocks(var1, var3);
    }

    public static void placeNoise(World var0, int var1, int var2, int var3, int var4)
    {
        Block var5 = Block.blocksList[var4];
        var0.playSoundEffect((double)((float)var1 + 0.5F), (double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), "step.stone", (var5.stepSound.getVolume() + 1.0F) / 2.0F, var5.stepSound.getPitch() * 0.8F);
    }

    public static int getBurnTime(ItemStack var0)
    {
        return TileEntityFurnace.getItemBurnTime(var0);
    }

    public static double getAverageEdgeLength(AxisAlignedBB var0)
    {
        double var1 = var0.maxX - var0.minX;
        double var3 = var0.maxY - var0.minY;
        double var5 = var0.maxZ - var0.minZ;
        return (var1 + var3 + var5) / 3.0D;
    }

    public static void writeChat(EntityPlayer var0, String var1)
    {
        if (var0 instanceof EntityPlayerMP)
        {
            EntityPlayerMP var2 = (EntityPlayerMP)var0;
            var2.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(var1));
        }
    }
}
