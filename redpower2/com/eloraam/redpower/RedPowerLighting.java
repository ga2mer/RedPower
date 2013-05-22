package com.eloraam.redpower;

import com.eloraam.redpower.RedPowerLighting$1;
import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.lighting.BlockLamp;
import com.eloraam.redpower.lighting.BlockShapedLamp;
import com.eloraam.redpower.lighting.ItemLamp;
import com.eloraam.redpower.lighting.ItemShapedLamp;
import com.eloraam.redpower.lighting.LightingProxy;
import com.eloraam.redpower.lighting.TileShapedLamp;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(
        modid = "RedPowerLighting",
        name = "RedPower Lighting",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b",
        dependencies = "required-after:RedPowerBase"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerLighting
{
    @Mod.Instance("RedPowerLighting")
    public static RedPowerLighting instance;
    @SidedProxy(
            clientSide = "com.eloraam.redpower.lighting.LightingProxyClient",
            serverSide = "com.eloraam.redpower.lighting.LightingProxy"
    )
    public static LightingProxy proxy;
    public static BlockLamp blockLampOff;
    public static BlockLamp blockLampOn;
    public static BlockLamp blockInvLampOff;
    public static BlockLamp blockInvLampOn;
    public static BlockShapedLamp blockShapedLamp;
    public static CreativeTabs tabLamp = new RedPowerLighting$1(CreativeTabs.getNextID(), "RPLights");
    public static final String textureFile = "/eloraam/lighting/lighting1.png";

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1) {}

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        setupLighting();
        proxy.registerRenderers();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    public static void setupLighting()
    {
        blockLampOff = new BlockLamp(Config.getBlockID("blocks.lighting.lampOff.id"), false, false);
        blockLampOn = new BlockLamp(Config.getBlockID("blocks.lighting.lampOn.id"), true, true);
        blockLampOn.setLightValue(1.0F);
        blockLampOff.setUnlocalizedName("rplampoff");
        blockLampOn.setUnlocalizedName("rplampon");
        GameRegistry.registerBlock(blockLampOn, "lampon");
        GameRegistry.registerBlock(blockLampOff, ItemLamp.class, "lampoff");
        blockLampOn.onID = blockLampOn.blockID;
        blockLampOn.offID = blockLampOff.blockID;
        blockLampOff.onID = blockLampOn.blockID;
        blockLampOff.offID = blockLampOff.blockID;
        blockInvLampOff = new BlockLamp(Config.getBlockID("blocks.lighting.lampInvOff.id"), false, true);
        blockInvLampOn = new BlockLamp(Config.getBlockID("blocks.lighting.lampInvOn.id"), true, false);
        blockInvLampOn.setLightValue(1.0F);
        blockInvLampOff.setUnlocalizedName("rplampoff");
        blockInvLampOn.setUnlocalizedName("rplampon");
        GameRegistry.registerBlock(blockInvLampOn, ItemLamp.class, "ilampon");
        GameRegistry.registerBlock(blockInvLampOff, "ilampoff");
        blockInvLampOn.onID = blockInvLampOff.blockID;
        blockInvLampOn.offID = blockInvLampOn.blockID;
        blockInvLampOff.onID = blockInvLampOff.blockID;
        blockInvLampOff.offID = blockInvLampOn.blockID;
        int var0;

        for (var0 = 0; var0 < 16; ++var0)
        {
            Config.addName("tile.rplamp." + CoreLib.rawColorNames[var0] + ".name", CoreLib.enColorNames[var0] + " Lamp");
            Config.addName("tile.rpilamp." + CoreLib.rawColorNames[var0] + ".name", "Inverted " + CoreLib.enColorNames[var0] + " Lamp");
            GameRegistry.addRecipe(new ItemStack(blockLampOff, 1, var0), new Object[] {"GLG", "GLG", "GRG", 'G', Block.thinGlass, 'L', new ItemStack(RedPowerBase.itemLumar, 1, var0), 'R', Item.redstone});
            GameRegistry.addRecipe(new ItemStack(blockInvLampOn, 1, var0), new Object[] {"GLG", "GLG", "GRG", 'G', Block.thinGlass, 'L', new ItemStack(RedPowerBase.itemLumar, 1, var0), 'R', Block.torchRedstoneActive});
        }

        blockShapedLamp = new BlockShapedLamp(Config.getBlockID("blocks.lighting.lampShaped.id"));
        GameRegistry.registerBlock(blockShapedLamp, ItemShapedLamp.class, "shlamp");
        GameRegistry.registerTileEntity(TileShapedLamp.class, "RPShLamp");
        blockShapedLamp.addTileEntityMapping(0, TileShapedLamp.class);
        String var1;

        for (var0 = 0; var0 < 16; ++var0)
        {
            var1 = "rpshlamp." + CoreLib.rawColorNames[var0];
            blockShapedLamp.setItemName(var0, var1);
            Config.addName("tile." + var1 + ".name", CoreLib.enColorNames[var0] + " Fixture");
            GameRegistry.addRecipe(new ItemStack(blockShapedLamp, 1, var0), new Object[] {"GLG", "GLG", "SRS", 'G', Block.thinGlass, 'L', new ItemStack(RedPowerBase.itemLumar, 1, var0), 'R', Item.redstone, 'S', Block.stoneSingleSlab});
        }

        for (var0 = 0; var0 < 16; ++var0)
        {
            var1 = "rpishlamp." + CoreLib.rawColorNames[var0];
            blockShapedLamp.setItemName(var0 + 16, var1);
            Config.addName("tile." + var1 + ".name", "Inverted " + CoreLib.enColorNames[var0] + " Fixture");
            GameRegistry.addRecipe(new ItemStack(blockShapedLamp, 1, 16 + var0), new Object[] {"GLG", "GLG", "SRS", 'G', Block.thinGlass, 'L', new ItemStack(RedPowerBase.itemLumar, 1, var0), 'R', Block.torchRedstoneActive, 'S', new ItemStack(Block.stoneSingleSlab, 1, 0)});
        }

        for (var0 = 0; var0 < 16; ++var0)
        {
            var1 = "rpshlamp2." + CoreLib.rawColorNames[var0];
            blockShapedLamp.setItemName(var0 + 32, var1);
            Config.addName("tile." + var1 + ".name", CoreLib.enColorNames[var0] + " Cage Lamp");
            GameRegistry.addRecipe(new ItemStack(blockShapedLamp, 1, 32 + var0), new Object[] {"ILI", "GLG", "SRS", 'G', Block.thinGlass, 'L', new ItemStack(RedPowerBase.itemLumar, 1, var0), 'R', Item.redstone, 'I', Block.fenceIron, 'S', new ItemStack(Block.stoneSingleSlab, 1, 0)});
        }

        for (var0 = 0; var0 < 16; ++var0)
        {
            var1 = "rpishlamp2." + CoreLib.rawColorNames[var0];
            blockShapedLamp.setItemName(var0 + 48, var1);
            Config.addName("tile." + var1 + ".name", "Inverted " + CoreLib.enColorNames[var0] + " Cage Lamp");
            GameRegistry.addRecipe(new ItemStack(blockShapedLamp, 1, 48 + var0), new Object[] {"ILI", "GLG", "SRS", 'G', Block.thinGlass, 'L', new ItemStack(RedPowerBase.itemLumar, 1, var0), 'R', Block.torchRedstoneActive, 'I', Block.fenceIron, 'S', Block.stoneSingleSlab});
        }
    }
}
