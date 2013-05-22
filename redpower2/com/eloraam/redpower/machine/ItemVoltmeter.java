package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BluePowerConductor;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IBluePowerConnectable;
import com.eloraam.redpower.core.IPipeConnectable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemVoltmeter extends Item
{
    public ItemVoltmeter(int var1)
    {
        super(var1);
        //this.setIconIndex(24);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    private boolean measureBlue(EntityPlayer var1, World var2, int var3, int var4, int var5, int var6)
    {
        IBluePowerConnectable var7 = (IBluePowerConnectable)CoreLib.getTileEntity(var2, var3, var4, var5, IBluePowerConnectable.class);

        if (var7 == null)
        {
            return false;
        }
        else
        {
            BluePowerConductor var8 = var7.getBlueConductor(var6);
            double var9 = var8.getVoltage();
            CoreLib.writeChat(var1, String.format("Reading %.2fV %.2fA (%.2fW)", new Object[] {Double.valueOf(var9), Double.valueOf(var8.Itot), Double.valueOf(var9 * var8.Itot)}));
            return true;
        }
    }

    private boolean measurePressure(EntityPlayer var1, World var2, int var3, int var4, int var5, int var6)
    {
        IPipeConnectable var7 = (IPipeConnectable)CoreLib.getTileEntity(var2, var3, var4, var5, IPipeConnectable.class);

        if (var7 == null)
        {
            return false;
        }
        else
        {
            int var8 = var7.getPipePressure(var6);
            CoreLib.writeChat(var1, String.format("Reading %d psi", new Object[] {Integer.valueOf(var8)}));
            return true;
        }
    }

    private boolean itemUseShared(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7)
    {
        return this.measureBlue(var2, var3, var4, var5, var6, var7) ? true : this.measurePressure(var2, var3, var4, var5, var6, var7);
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

    public String getTextureFile()
    {
        return "/eloraam/base/items1.png";
    }
}
