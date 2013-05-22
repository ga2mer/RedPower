package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerWorld;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

public class ItemCustomHoe extends ItemHoe
{
    public ItemCustomHoe(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack var1, ItemStack var2)
    {
        return this.theToolMaterial == RedPowerWorld.toolMaterialRuby && var2.isItemEqual(RedPowerBase.itemRuby) ? true : (this.theToolMaterial == RedPowerWorld.toolMaterialSapphire && var2.isItemEqual(RedPowerBase.itemSapphire) ? true : (this.theToolMaterial == RedPowerWorld.toolMaterialGreenSapphire && var2.isItemEqual(RedPowerBase.itemGreenSapphire) ? true : super.getIsRepairable(var1, var2)));
    }

    public String getTextureFile()
    {
        return "/eloraam/world/worlditems1.png";
    }
}
