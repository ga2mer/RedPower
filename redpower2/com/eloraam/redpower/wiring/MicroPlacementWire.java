package com.eloraam.redpower.wiring;

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

public class MicroPlacementWire implements IMicroPlacement
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
        else if (!RedPowerLib.canSupportWire(var3, var4.x, var4.y, var4.z, var5 ^ 1))
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
                var8.ConSides = 1 << (var5 ^ 1);
                var8.Metadata = var1.getItemDamage() & 255;
                this.blockUsed(var3, var4, var1);
                return true;
            }
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
                int var8 = 1 << (var5 ^ 1);

                if ((var7.CoverSides & var8) > 0)
                {
                    return false;
                }
                else
                {
                    int var9 = var1.getItemDamage();
                    int var10 = var9 & 255;
                    var9 >>= 8;

                    if (!CoverLib.tryMakeCompatible(var3, var4, var1.itemID, var1.getItemDamage()))
                    {
                        return false;
                    }
                    else
                    {
                        TileWiring var11 = (TileWiring)CoreLib.getTileEntity(var3, var4, TileWiring.class);

                        if (var11 == null)
                        {
                            return false;
                        }
                        else if (!RedPowerLib.canSupportWire(var3, var4.x, var4.y, var4.z, var5 ^ 1))
                        {
                            return false;
                        }
                        else if (((var11.ConSides | var11.CoverSides) & var8) > 0)
                        {
                            return false;
                        }
                        else
                        {
                            var8 |= var11.ConSides;
                            int var12 = var8 & 63;

                            if (var12 != 3 && var12 != 12 && var12 != 48)
                            {
                                if (!CoverLib.checkPlacement(var11.CoverSides, var11.Covers, var12, (var11.ConSides & 64) > 0))
                                {
                                    return false;
                                }
                                else
                                {
                                    var11.ConSides = var8;
                                    var11.uncache();
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
            }
        }
    }

    public String getMicroName(int var1, int var2)
    {
        switch (var1)
        {
            case 1:
                if (var2 == 0)
                {
                    return "tile.rpwire";
                }

                break;

            case 2:
                return "tile.rpinsulated." + CoreLib.rawColorNames[var2];

            case 3:
                if (var2 == 0)
                {
                    return "tile.rpcable";
                }

                return "tile.rpcable." + CoreLib.rawColorNames[var2 - 1];

            case 4:
            default:
                break;

            case 5:
                if (var2 == 0)
                {
                    return "tile.bluewire";
                }

                if (var2 == 1)
                {
                    return "tile.bluewire10";
                }

                if (var2 == 2)
                {
                    return "tile.bluewire1M";
                }
        }

        return null;
    }

    public void addCreativeItems(int var1, CreativeTabs var2, List var3)
    {
        if (var2 == CreativeExtraTabs.tabWires)
        {
            int var4;

            switch (var1)
            {
                case 1:
                    var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 256));
                    break;

                case 2:
                    for (var4 = 0; var4 < 16; ++var4)
                    {
                        var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 512 + var4));
                    }

                    return;

                case 3:
                    for (var4 = 0; var4 < 17; ++var4)
                    {
                        var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 768 + var4));
                    }

                case 4:
                default:
                    break;

                case 5:
                    var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 1280));
                    var3.add(new ItemStack(CoverLib.blockCoverPlate, 1, 1281));
            }
        }
    }
}
