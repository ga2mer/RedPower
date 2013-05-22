package com.eloraam.redpower.base;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import com.eloraam.redpower.core.ICoverable;
import com.eloraam.redpower.core.IMicroPlacement;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.WorldCoord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemMicro extends ItemBlock
{
    IMicroPlacement[] placers = new IMicroPlacement[256];

    public ItemMicro(int var1)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    private boolean useCover(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7)
    {
        MovingObjectPosition var8 = CoreLib.retraceBlock(var3, var2, var4, var5, var6);

        if (var8 == null)
        {
            return false;
        }
        else if (var8.typeOfHit != EnumMovingObjectType.TILE)
        {
            return false;
        }
        else
        {
            var8 = CoverLib.getPlacement(var3, var8, var1.getItemDamage());

            if (var8 == null)
            {
                return false;
            }
            else
            {
                if (var3.canPlaceEntityOnSide(var1.itemID, var8.blockX, var8.blockY, var8.blockZ, false, var7, var2))
                {
                    var3.setBlockMetadataWithNotify(var8.blockX, var8.blockY, var8.blockZ, RedPowerBase.blockMicro.blockID, 0);
                }

                TileEntity var9 = var3.getBlockTileEntity(var8.blockX, var8.blockY, var8.blockZ);

                if (!(var9 instanceof ICoverable))
                {
                    return false;
                }
                else
                {
                    ICoverable var10 = (ICoverable)var9;

                    if (var10.tryAddCover(var8.subHit, CoverLib.damageToCoverValue(var1.getItemDamage())))
                    {
                        --var1.stackSize;
                        CoreLib.placeNoise(var3, var8.blockX, var8.blockY, var8.blockZ, CoverLib.getBlock(var1.getItemDamage() & 255).blockID);
                        RedPowerLib.updateIndirectNeighbors(var3, var8.blockX, var8.blockY, var8.blockZ, RedPowerBase.blockMicro.blockID);
                        var3.markBlockForUpdate(var8.blockX, var8.blockY, var8.blockZ);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
    }

    /**
     * Returns true if the given ItemBlock can be placed on the given side of the given block position.
     */
    public boolean canPlaceItemBlockOnSide(World var1, int var2, int var3, int var4, int var5, EntityPlayer var6, ItemStack var7)
    {
        return true;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return var2 == null ? false : (var2.isSneaking() ? false : this.itemUseShared(var1, var2, var3, var4, var5, var6, var7));
    }

    public boolean onItemUseFirst(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        return CoreLib.isClient(var3) ? false : (!var2.isSneaking() ? false : this.itemUseShared(var1, var2, var3, var4, var5, var6, var7));
    }

    private boolean itemUseShared(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7)
    {
        int var8 = var1.getItemDamage();
        int var9 = var8 & 255;
        var8 >>= 8;
        return var8 != 0 && (var8 < 16 || var8 > 45) ? (this.placers[var8] == null ? false : this.placers[var8].onPlaceMicro(var1, var2, var3, new WorldCoord(var4, var5, var6), var7)) : this.useCover(var1, var2, var3, var4, var5, var6, var7);
    }

    private String getMicroName(int var1)
    {
        switch (var1)
        {
            case 0:
                return "rpcover";

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

            case 16:
                return "rppanel";

            case 17:
                return "rpslab";

            case 18:
                return "rpcovc";

            case 19:
                return "rppanc";

            case 20:
                return "rpslabc";

            case 21:
                return "rpcovs";

            case 22:
                return "rppans";

            case 23:
                return "rpslabs";

            case 24:
                return "rphcover";

            case 25:
                return "rphpanel";

            case 26:
                return "rphslab";

            case 27:
                return "rpcov3";

            case 28:
                return "rpcov5";

            case 29:
                return "rpcov6";

            case 30:
                return "rpcov7";

            case 31:
                return "rphcov3";

            case 32:
                return "rphcov5";

            case 33:
                return "rphcov6";

            case 34:
                return "rphcov7";

            case 35:
                return "rpcov3c";

            case 36:
                return "rpcov5c";

            case 37:
                return "rpcov6c";

            case 38:
                return "rpcov7c";

            case 39:
                return "rpcov3s";

            case 40:
                return "rpcov5s";

            case 41:
                return "rpcov6s";

            case 42:
                return "rpcov7s";

            case 43:
                return "rppole1";

            case 44:
                return "rppole2";

            case 45:
                return "rppole3";
        }
    }

    public String getItemNameIS(ItemStack var1)
    {
        int var2 = var1.getItemDamage();
        int var3 = var2 & 255;
        var2 >>= 8;
        String var4 = this.getMicroName(var2);
        String var5;

        if (var4 != null)
        {
            var5 = CoverLib.getName(var3);

            if (var5 == null)
            {
                throw new IndexOutOfBoundsException();
            }
            else
            {
                return "tile." + var4 + "." + var5;
            }
        }
        else if (this.placers[var2] == null)
        {
            throw new IndexOutOfBoundsException();
        }
        else
        {
            var5 = this.placers[var2].getMicroName(var2, var3);

            if (var5 == null)
            {
                throw new IndexOutOfBoundsException();
            }
            else
            {
                return var5;
            }
        }
    }

    public void registerPlacement(int var1, IMicroPlacement var2)
    {
        this.placers[var1] = var2;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        int var4;

        if (var2 != CreativeExtraTabs.tabWires && var2 != CreativeExtraTabs.tabMachine)
        {
            if (var2 == CreativeExtraTabs.tabMicros)
            {
                String var5;

                for (var4 = 0; var4 < 255; ++var4)
                {
                    var5 = CoverLib.getName(var4);

                    if (var5 != null)
                    {
                        var3.add(new ItemStack(RedPowerBase.blockMicro, 1, var4));
                    }
                }

                for (var4 = 1; var4 < 255; ++var4)
                {
                    var5 = this.getMicroName(var4);

                    if (var5 != null)
                    {
                        var3.add(new ItemStack(RedPowerBase.blockMicro, 1, var4 << 8));
                    }
                }

                for (var4 = 1; var4 < 255; ++var4)
                {
                    var5 = this.getMicroName(var4);

                    if (var5 != null)
                    {
                        var3.add(new ItemStack(RedPowerBase.blockMicro, 1, var4 << 8 | 2));
                    }
                }

                for (var4 = 1; var4 < 255; ++var4)
                {
                    var5 = this.getMicroName(var4);

                    if (var5 != null)
                    {
                        var3.add(new ItemStack(RedPowerBase.blockMicro, 1, var4 << 8 | 23));
                    }
                }

                for (var4 = 1; var4 < 255; ++var4)
                {
                    var5 = this.getMicroName(var4);

                    if (var5 != null)
                    {
                        var3.add(new ItemStack(RedPowerBase.blockMicro, 1, var4 << 8 | 26));
                    }
                }
            }
        }
        else
        {
            for (var4 = 0; var4 < 255; ++var4)
            {
                if (this.placers[var4] != null)
                {
                    this.placers[var4].addCreativeItems(var4, var2, var3);
                }
            }
        }
    }

    public CreativeTabs[] getCreativeTabs()
    {
        return new CreativeTabs[] {CreativeExtraTabs.tabWires, CreativeExtraTabs.tabMicros, CreativeExtraTabs.tabMachine};
    }
}
