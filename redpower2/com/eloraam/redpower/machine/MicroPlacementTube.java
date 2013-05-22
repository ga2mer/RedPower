package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import com.eloraam.redpower.core.IMicroPlacement;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileCovered;
import com.eloraam.redpower.core.WorldCoord;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MicroPlacementTube implements IMicroPlacement
{
    private void blockUsed(World var1, WorldCoord var2, ItemStack var3)
    {
        --var3.stackSize;
        CoreLib.placeNoise(var1, var2.x, var2.y, var2.z, var3.itemID);
        var1.markBlockForUpdate(var2.x, var2.y, var2.z);
        RedPowerLib.updateIndirectNeighbors(var1, var2.x, var2.y, var2.z, var3.itemID);
    }

    private boolean initialPlace(ItemStack var1, EntityPlayer var2, World var3, WorldCoord var4, int var5)
    {
        int var6 = var1.getItemDamage() >> 8;
        int var7 = var1.itemID;

        if (!var3.canPlaceEntityOnSide(var7, var4.x, var4.y, var4.z, false, var5, var2))
        {
            return false;
        }
        else if (!var3.setBlockMetadataWithNotify(var4.x, var4.y, var4.z, var7, var6))
        {
            return true;
        }
        else
        {
            this.blockUsed(var3, var4, var1);
            return true;
        }
    }

    public boolean onPlaceMicro(ItemStack var1, EntityPlayer var2, World var3, WorldCoord var4, int var5)
    {
        var4.step(var5);
        int var6 = var3.getBlockId(var4.x, var4.y, var4.z);

        if (var6 != var1.itemID)
        {
            return this.initialPlace(var1, var2, var3, var4, var5);
        }
        else
        {
            TileCovered var7 = (TileCovered)CoreLib.getTileEntity(var3, var4, TileCovered.class);

            if (var7 == null)
            {
                return false;
            }
            else
            {
                int var8 = var7.getExtendedID();

                if (var8 != 7 && var8 != 8 && var8 != 9 && var8 != 10 && var8 != 11)
                {
                    if (!CoverLib.tryMakeCompatible(var3, var4, var1.itemID, var1.getItemDamage()))
                    {
                        return false;
                    }
                    else
                    {
                        this.blockUsed(var3, var4, var1);
                        return true;
                    }
                }
                else
                {
                    return false;
                }
            }
        }
    }

    public String getMicroName(int var1, int var2)
    {
        return var1 == 7 ? "tile.rppipe" : (var1 == 8 ? "tile.rptube" : (var1 == 9 ? "tile.rprstube" : (var1 == 10 ? "tile.rprtube" : (var1 == 11 ? "tile.rpmtube" : null))));
    }

    public void addCreativeItems(int var1, CreativeTabs var2, List var3)
    {
        if (var2 == CreativeExtraTabs.tabMachine)
        {
            if (var1 == 7)
            {
                var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 1792));
            }
            else if (var1 == 8)
            {
                var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 2048));
            }
            else if (var1 == 9)
            {
                var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 2304));
            }
            else if (var1 == 10)
            {
                var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 2560));
            }
            else if (var1 == 11)
            {
                var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 2816));
            }
        }
    }
}
