package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerWorld;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemCustomAxe extends ItemAxe
{
    public ItemCustomAxe(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack var1, ItemStack var2)
    {
        return this.toolMaterial == RedPowerWorld.toolMaterialRuby && var2.isItemEqual(RedPowerBase.itemRuby) ? true : (this.toolMaterial == RedPowerWorld.toolMaterialSapphire && var2.isItemEqual(RedPowerBase.itemSapphire) ? true : (this.toolMaterial == RedPowerWorld.toolMaterialGreenSapphire && var2.isItemEqual(RedPowerBase.itemGreenSapphire) ? true : super.getIsRepairable(var1, var2)));
    }

    public String getTextureFile()
    {
        return "/eloraam/world/worlditems1.png";
    }
}
