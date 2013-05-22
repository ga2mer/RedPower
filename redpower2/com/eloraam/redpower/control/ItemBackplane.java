package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerControl;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.ItemExtended;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBackplane extends ItemExtended
{
    public ItemBackplane(int var1)
    {
        super(var1);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return var2.isSneaking() ? false : this.itemUseShared(var1, var2, var3, var4, var5, var6, var7);
    }

    public boolean onItemUseFirst(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return CoreLib.isClient(var3) ? false : (!var2.isSneaking() ? false : this.itemUseShared(var1, var2, var3, var4, var5, var6, var7));
    }

    protected boolean itemUseShared(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7)
    {
        int var8 = var3.getBlockId(var4, var5, var6);
        int var9 = var3.getBlockMetadata(var4, var5, var6);
        int var10 = var1.getItemDamage();
        int var12;

        if (var8 == var1.itemID && var9 == 0 && var10 != 0)
        {
            TileBackplane var19 = (TileBackplane)CoreLib.getTileEntity(var3, var4, var5, var6, TileBackplane.class);

            if (var19 == null)
            {
                return false;
            }
            else
            {
                var12 = var19.Rotation;
                BlockMultipart.removeMultipart(var3, var4, var5, var6);

                if (!var3.setBlockMetadataWithNotify(var4, var5, var6, var8, var10))
                {
                    return false;
                }
                else
                {
                    var19 = (TileBackplane)CoreLib.getTileEntity(var3, var4, var5, var6, TileBackplane.class);

                    if (var19 != null)
                    {
                        var19.Rotation = var12;
                    }

                    var3.markBlockForUpdate(var4, var5, var6);
                    CoreLib.placeNoise(var3, var4, var5, var6, var1.itemID);
                    --var1.stackSize;
                    RedPowerLib.updateIndirectNeighbors(var3, var4, var5, var6, var1.itemID);
                    return true;
                }
            }
        }
        else if (var10 != 0)
        {
            return false;
        }
        else
        {
            WorldCoord var11 = new WorldCoord(var4, var5, var6);
            var11.step(var7);

            if (!var3.canPlaceEntityOnSide(var1.itemID, var11.x, var11.y, var11.z, false, 1, var2))
            {
                return false;
            }
            else if (!RedPowerLib.isSideNormal(var3, var11.x, var11.y, var11.z, 0))
            {
                return false;
            }
            else
            {
                var12 = -1;
                label77:

                for (int var13 = 0; var13 < 4; ++var13)
                {
                    WorldCoord var14 = var11.copy();
                    int var15 = CoreLib.rotToSide(var13) ^ 1;
                    var14.step(var15);
                    TileCPU var16 = (TileCPU)CoreLib.getTileEntity(var3, var14, TileCPU.class);

                    if (var16 != null && var16.Rotation == var13)
                    {
                        var12 = var13;
                        break;
                    }

                    TileBackplane var17 = (TileBackplane)CoreLib.getTileEntity(var3, var14, TileBackplane.class);

                    if (var17 != null && var17.Rotation == var13)
                    {
                        for (int var18 = 0; var18 < 6; ++var18)
                        {
                            var14.step(var15);

                            if (var3.getBlockId(var14.x, var14.y, var14.z) == RedPowerControl.blockPeripheral.blockID && var3.getBlockMetadata(var14.x, var14.y, var14.z) == 1)
                            {
                                var12 = var13;
                                break label77;
                            }
                        }
                    }
                }

                if (var12 < 0)
                {
                    return false;
                }
                else if (!var3.setBlockMetadataWithNotify(var11.x, var11.y, var11.z, var1.itemID, var10))
                {
                    return true;
                }
                else
                {
                    TileBackplane var20 = (TileBackplane)CoreLib.getTileEntity(var3, var11, TileBackplane.class);
                    var20.Rotation = var12;
                    CoreLib.placeNoise(var3, var11.x, var11.y, var11.z, var1.itemID);
                    --var1.stackSize;
                    var3.markBlockForUpdate(var11.x, var11.y, var11.z);
                    RedPowerLib.updateIndirectNeighbors(var3, var11.x, var11.y, var11.z, var1.itemID);
                    return true;
                }
            }
        }
    }
}
