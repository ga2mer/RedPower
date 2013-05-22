package com.eloraam.redpower.wiring;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import com.eloraam.redpower.core.IMicroPlacement;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MicroPlacementJacket implements IMicroPlacement
{
    private void blockUsed(World var1, WorldCoord var2, ItemStack var3)
    {
        --var3.stackSize;
        CoreLib.placeNoise(var1, var2.x, var2.y, var2.z, var3.itemID);
        var1.markBlockForUpdate(var2.x, var2.y, var2.z);
        RedPowerLib.updateIndirectNeighbors(var1, var2.x, var2.y, var2.z, var3.itemID);
    }

    private int getWireMeta(int var1)
    {
        switch (var1)
        {
            case 64:
                return 1;

            case 65:
                return 3;

            case 66:
                return 5;

            default:
                return 0;
        }
    }

    private boolean initialPlace(ItemStack var1, EntityPlayer var2, World var3, WorldCoord var4, int var5)
    {
        int var6 = var1.getItemDamage() >> 8;
        int var7 = var1.itemID;
        var6 = this.getWireMeta(var6);

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
            TileWiring var8 = (TileWiring)CoreLib.getTileEntity(var3, var4, TileWiring.class);

            if (var8 == null)
            {
                return false;
            }
            else
            {
                var8.CenterPost = (short)(var1.getItemDamage() & 255);
                var8.ConSides |= 64;
                this.blockUsed(var3, var4, var1);
                return true;
            }
        }
    }

    private boolean tryAddingJacket(World var1, WorldCoord var2, ItemStack var3)
    {
        TileWiring var4 = (TileWiring)CoreLib.getTileEntity(var1, var2, TileWiring.class);

        if (var4 == null)
        {
            return false;
        }
        else if ((var4.ConSides & 64) > 0)
        {
            return false;
        }
        else if (!CoverLib.checkPlacement(var4.CoverSides, var4.Covers, var4.ConSides, true))
        {
            return false;
        }
        else
        {
            var4.CenterPost = (short)(var3.getItemDamage() & 255);
            var4.ConSides |= 64;
            var4.uncache();
            this.blockUsed(var1, var2, var3);
            return true;
        }
    }

    public boolean onPlaceMicro(ItemStack var1, EntityPlayer var2, World var3, WorldCoord var4, int var5)
    {
        int var6 = var1.getItemDamage();
        int var7 = var6 & 255;
        var6 >>= 8;
        var6 = this.getWireMeta(var6);
        int var8 = var6 << 8;

        if (CoverLib.tryMakeCompatible(var3, var4, var1.itemID, var8) && this.tryAddingJacket(var3, var4, var1))
        {
            return true;
        }
        else
        {
            var4.step(var5);
            int var9 = var3.getBlockId(var4.x, var4.y, var4.z);
            return var9 != var1.itemID ? this.initialPlace(var1, var2, var3, var4, var5) : (!CoverLib.tryMakeCompatible(var3, var4, var1.itemID, var8) ? false : this.tryAddingJacket(var3, var4, var1));
        }
    }

    public String getMicroName(int var1, int var2)
    {
        String var3;

        switch (var1)
        {
            case 64:
                var3 = CoverLib.getName(var2);

                if (var3 == null)
                {
                    return null;
                }
                else
                {
                    if (CoverLib.isTransparent(var2))
                    {
                        return null;
                    }

                    return "tile.rparmwire." + var3;
                }

            case 65:
                var3 = CoverLib.getName(var2);

                if (var3 == null)
                {
                    return null;
                }
                else
                {
                    if (CoverLib.isTransparent(var2))
                    {
                        return null;
                    }

                    return "tile.rparmcable." + var3;
                }

            case 66:
                var3 = CoverLib.getName(var2);

                if (var3 == null)
                {
                    return null;
                }
                else
                {
                    if (CoverLib.isTransparent(var2))
                    {
                        return null;
                    }

                    return "tile.rparmbwire." + var3;
                }

            default:
                return null;
        }
    }

    public void addCreativeItems(int var1, CreativeTabs var2, List var3)
    {
        if (var2 == CreativeExtraTabs.tabWires)
        {
            switch (var1)
            {
                case 64:
                    var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 16386));
                    break;

                case 65:
                    var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 16666));
                    break;

                case 66:
                    var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 16902));
            }
        }
    }
}
