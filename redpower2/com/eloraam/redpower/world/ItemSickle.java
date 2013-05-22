package com.eloraam.redpower.world;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

public class ItemSickle extends ItemTool
{
    public int cropRadius = 2;
    public int leafRadius = 1;

    public ItemSickle(int var1, EnumToolMaterial var2)
    {
        super(var1, 3, var2, new Block[0]);
        this.maxStackSize = 1;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack var1, Block var2)
    {
        return var2 instanceof BlockLeaves ? this.efficiencyOnProperMaterial : super.getStrVsBlock(var1, var2);
    }

    public boolean onBlockDestroyed(ItemStack var1, World var2, int var3, int var4, int var5, int var6, EntityLiving var7)
    {
        boolean var11 = false;

        if (!(var7 instanceof EntityPlayer))
        {
            return false;
        }
        else
        {
            EntityPlayer var13 = (EntityPlayer)var7;
            Block var12 = Block.blocksList[var3];
            int var8;
            int var9;
            int var14;
            int var15;

            if (var12 != null && var12.isLeaves(var2, var4, var5, var6))
            {
                for (var14 = -this.leafRadius; var14 <= this.leafRadius; ++var14)
                {
                    for (var15 = -this.leafRadius; var15 <= this.leafRadius; ++var15)
                    {
                        for (int var16 = -this.leafRadius; var16 <= this.leafRadius; ++var16)
                        {
                            var8 = var2.getBlockId(var4 + var14, var5 + var15, var6 + var16);
                            var9 = var2.getBlockMetadata(var4 + var14, var5 + var15, var6 + var16);
                            var12 = Block.blocksList[var8];

                            if (var12 != null && var12.isLeaves(var2, var4 + var14, var5 + var15, var6 + var16))
                            {
                                if (var12.canHarvestBlock(var13, var9))
                                {
                                    var12.harvestBlock(var2, var13, var4 + var14, var5 + var15, var6 + var16, var9);
                                }

                                var2.setBlock(var4 + var14, var5 + var15, var6 + var16, 0);
                                var11 = true;
                            }
                        }
                    }
                }

                if (var11)
                {
                    var1.damageItem(1, var7);
                }

                return var11;
            }
            else
            {
                for (var14 = -this.cropRadius; var14 <= this.cropRadius; ++var14)
                {
                    for (var15 = -this.cropRadius; var15 <= this.cropRadius; ++var15)
                    {
                        var8 = var2.getBlockId(var4 + var14, var5, var6 + var15);
                        var9 = var2.getBlockMetadata(var4 + var14, var5, var6 + var15);

                        if (var8 != 0)
                        {
                            var12 = Block.blocksList[var8];

                            if (var12.blockID != Block.waterlily.blockID && var12 instanceof BlockFlower)
                            {
                                if (var12.canHarvestBlock(var13, var9))
                                {
                                    var12.harvestBlock(var2, var13, var4 + var14, var5, var6 + var15, var9);
                                }

                                var2.setBlock(var4 + var14, var5, var6 + var15, 0);
                                var11 = true;
                            }
                        }
                    }
                }

                if (var11)
                {
                    var1.damageItem(1, var7);
                }

                return var11;
            }
        }
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

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 20;
    }
}
