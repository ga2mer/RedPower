package com.eloraam.redpower;

import com.eloraam.redpower.RedPowerWiring$1;
import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.CraftLib;
import com.eloraam.redpower.core.RedPowerLib;
import com.eloraam.redpower.core.TileCovered;
import com.eloraam.redpower.wiring.MicroPlacementJacket;
import com.eloraam.redpower.wiring.MicroPlacementWire;
import com.eloraam.redpower.wiring.TileBluewire;
import com.eloraam.redpower.wiring.TileCable;
import com.eloraam.redpower.wiring.TileInsulatedWire;
import com.eloraam.redpower.wiring.TileRedwire;
import com.eloraam.redpower.wiring.WiringProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(
        modid = "RedPowerWiring",
        name = "RedPower Wiring",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b",
        dependencies = "required-after:RedPowerBase"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerWiring
{
    @Mod.Instance("RedPowerWiring")
    public static RedPowerWiring instance;
    @SidedProxy(
            clientSide = "com.eloraam.redpower.wiring.WiringProxyClient",
            serverSide = "com.eloraam.redpower.wiring.WiringProxy"
    )
    public static WiringProxy proxy;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1) {}

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        initJacketRecipes();
        setupWires();
        proxy.registerRenderers();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    private static void initJacketRecipes()
    {
        CoverLib.addMaterialHandler(new RedPowerWiring$1());
    }

    public static void setupWires()
    {
        GameRegistry.registerTileEntity(TileRedwire.class, "Redwire");
        GameRegistry.registerTileEntity(TileInsulatedWire.class, "InsRedwire");
        GameRegistry.registerTileEntity(TileCable.class, "RedCable");
        GameRegistry.registerTileEntity(TileCovered.class, "Covers");
        GameRegistry.registerTileEntity(TileBluewire.class, "Bluewire");
        MicroPlacementWire var0 = new MicroPlacementWire();
        RedPowerBase.blockMicro.registerPlacement(1, var0);
        RedPowerBase.blockMicro.registerPlacement(2, var0);
        RedPowerBase.blockMicro.registerPlacement(3, var0);
        RedPowerBase.blockMicro.registerPlacement(5, var0);
        MicroPlacementJacket var3 = new MicroPlacementJacket();
        RedPowerBase.blockMicro.registerPlacement(64, var3);
        RedPowerBase.blockMicro.registerPlacement(65, var3);
        RedPowerBase.blockMicro.registerPlacement(66, var3);
        RedPowerBase.blockMicro.addTileEntityMapping(1, TileRedwire.class);
        RedPowerBase.blockMicro.addTileEntityMapping(2, TileInsulatedWire.class);
        RedPowerBase.blockMicro.addTileEntityMapping(3, TileCable.class);
        RedPowerBase.blockMicro.addTileEntityMapping(5, TileBluewire.class);
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 12, 256), new Object[] {"R", "R", "R", 'R', RedPowerBase.itemIngotRed});
        CraftLib.addAlloyResult(RedPowerBase.itemIngotRed, new Object[] {new ItemStack(RedPowerBase.blockMicro, 4, 256)});
        CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotRed, 5), new Object[] {new ItemStack(RedPowerBase.blockMicro, 8, 768)});
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 12, 1280), new Object[] {"WBW", "WBW", "WBW", 'B', RedPowerBase.itemIngotBlue, 'W', Block.cloth});
        CraftLib.addAlloyResult(RedPowerBase.itemIngotBlue, new Object[] {new ItemStack(RedPowerBase.blockMicro, 4, 1280)});
        GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 1281), new Object[] {new ItemStack(RedPowerBase.blockMicro, 1, 1280), Block.cloth});
        CraftLib.addAlloyResult(RedPowerBase.itemIngotBlue, new Object[] {new ItemStack(RedPowerBase.blockMicro, 4, 1281)});
        int var1;
        int var2;

        for (var1 = 0; var1 < 16; ++var1)
        {
            Config.addName("tile.rpinsulated." + CoreLib.rawColorNames[var1] + ".name", CoreLib.enColorNames[var1] + " Insulated Wire");
            Config.addName("tile.rpcable." + CoreLib.rawColorNames[var1] + ".name", CoreLib.enColorNames[var1] + " Bundled Cable");
            GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 12, 512 + var1), new Object[] {"WRW", "WRW", "WRW", 'R', RedPowerBase.itemIngotRed, 'W', new ItemStack(Block.cloth, 1, var1)});

            for (var2 = 0; var2 < 16; ++var2)
            {
                if (var1 != var2)
                {
                    GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 512 + var1), new Object[] {new ItemStack(RedPowerBase.blockMicro, 1, 512 + var2), new ItemStack(Item.dyePowder, 1, 15 - var1)});
                    GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 769 + var1), new Object[] {new ItemStack(RedPowerBase.blockMicro, 1, 769 + var2), new ItemStack(Item.dyePowder, 1, 15 - var1)});
                }
            }

            CraftLib.addAlloyResult(RedPowerBase.itemIngotRed, new Object[] {new ItemStack(RedPowerBase.blockMicro, 4, 512 + var1)});
            GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 2, 768), new Object[] {"SWS", "WWW", "SWS", 'W', new ItemStack(RedPowerBase.blockMicro, 1, 512 + var1), 'S', Item.silk});
            GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 769 + var1), new Object[] {new ItemStack(RedPowerBase.blockMicro, 1, 768), new ItemStack(Item.dyePowder, 1, 15 - var1), Item.paper});
            CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotRed, 5), new Object[] {new ItemStack(RedPowerBase.blockMicro, 8, 769 + var1)});
        }

        for (var1 = 0; var1 < 16; ++var1)
        {
            if (var1 != 11)
            {
                CraftLib.addShapelessOreRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 523), new Object[] {new ItemStack(RedPowerBase.blockMicro, 1, 512 + var1), "dyeBlue"});
                CraftLib.addShapelessOreRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 780), new Object[] {new ItemStack(RedPowerBase.blockMicro, 1, 769 + var1), "dyeBlue"});
            }
        }

        CraftLib.addShapelessOreRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 780), new Object[] {new ItemStack(RedPowerBase.blockMicro, 1, 768), "dyeBlue", Item.paper});
        RedPowerLib.addCompatibleMapping(0, 1);

        for (var1 = 0; var1 < 16; ++var1)
        {
            RedPowerLib.addCompatibleMapping(0, 2 + var1);
            RedPowerLib.addCompatibleMapping(1, 2 + var1);
            RedPowerLib.addCompatibleMapping(65, 2 + var1);

            for (var2 = 0; var2 < 16; ++var2)
            {
                RedPowerLib.addCompatibleMapping(19 + var2, 2 + var1);
            }

            RedPowerLib.addCompatibleMapping(18, 2 + var1);
            RedPowerLib.addCompatibleMapping(18, 19 + var1);
        }

        RedPowerLib.addCompatibleMapping(0, 65);
        RedPowerLib.addCompatibleMapping(1, 65);
        RedPowerLib.addCompatibleMapping(64, 65);
        RedPowerLib.addCompatibleMapping(64, 67);
        RedPowerLib.addCompatibleMapping(65, 67);
        RedPowerLib.addCompatibleMapping(66, 67);
    }
}
