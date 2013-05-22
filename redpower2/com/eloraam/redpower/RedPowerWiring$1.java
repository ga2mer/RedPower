package com.eloraam.redpower;

import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.CoverLib$IMaterialHandler;
import com.eloraam.redpower.core.CraftLib;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

final class RedPowerWiring$1 implements CoverLib$IMaterialHandler
{
    public void addMaterial(int var1)
    {
        if (!CoverLib.isTransparent(var1))
        {
            String var2 = CoverLib.getName(var1);
            String var3 = CoverLib.getDesc(var1);
            Config.addName("tile.rparmwire." + var2 + ".name", var3 + " Jacketed Wire");
            Config.addName("tile.rparmcable." + var2 + ".name", var3 + " Jacketed Cable");
            Config.addName("tile.rparmbwire." + var2 + ".name", var3 + " Jacketed Bluewire");
            GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 4, 16384 + var1), new Object[] {"SSS", "SRS", "SSS", 'S', new ItemStack(RedPowerBase.blockMicro, 1, var1), 'R', RedPowerBase.itemIngotRed});
            GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 16640 + var1), new Object[] {"SSS", "SCS", "SSS", 'S', new ItemStack(RedPowerBase.blockMicro, 1, var1), 'C', new ItemStack(RedPowerBase.blockMicro, 1, 768)});
            GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 4, 16896 + var1), new Object[] {"SSS", "SBS", "SSS", 'S', new ItemStack(RedPowerBase.blockMicro, 1, var1), 'B', RedPowerBase.itemIngotBlue});
            CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotRed, 1), new Object[] {new ItemStack(RedPowerBase.blockMicro, 4, 16384 + var1)});
            CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotRed, 5), new Object[] {new ItemStack(RedPowerBase.blockMicro, 8, 16640 + var1)});
            CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotBlue, 1), new Object[] {new ItemStack(RedPowerBase.blockMicro, 4, 16896 + var1)});
        }
    }
}
