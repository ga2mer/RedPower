package com.eloraam.redpower;

import com.eloraam.redpower.compat.BlockMachineCompat;
import com.eloraam.redpower.compat.CompatProxy;
import com.eloraam.redpower.compat.ItemMachineCompat;
import com.eloraam.redpower.compat.TileBlueEngine;
import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CraftLib;
import com.eloraam.redpower.core.ItemParts;
import com.eloraam.redpower.core.PipeLib;
import com.eloraam.redpower.core.ReflectLib;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(
        modid = "RedPowerCompat",
        name = "RedPower Compat",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b",
        dependencies = "required-after:RedPowerBase;required-after:RedPowerMachine;after:BuildCraftBase"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerCompat
{
    @Mod.Instance("RedPowerCompat")
    public static RedPowerCompat instance;
    @SidedProxy(
            clientSide = "com.eloraam.redpower.compat.CompatProxyClient",
            serverSide = "com.eloraam.redpower.compat.CompatProxy"
    )
    public static CompatProxy proxy;
    public static BlockMachineCompat blockMachineCompat;
    public static ItemParts itemCompatParts;
    public static ItemStack itemGearBrass;
    public static final String textureFile = "/eloraam/compat/compat1.png";

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1) {}

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        this.setupBlocks();
        this.initFluids();
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    private void initFluids()
    {
        Block var1 = (Block)ReflectLib.getStaticField("buildcraft.BuildCraftEnergy", "oilStill", Block.class);
        Block var2 = (Block)ReflectLib.getStaticField("buildcraft.BuildCraftEnergy", "oilMoving", Block.class);

        if (var1 != null && var2 != null)
        {
            PipeLib.registerVanillaFluid(var1.blockID, var2.blockID);
        }
    }

    private void setupBlocks()
    {
        GameRegistry.registerTileEntity(TileBlueEngine.class, "RPBTEngine");
        blockMachineCompat = new BlockMachineCompat(Config.getBlockID("blocks.compat.machine.id"));
        GameRegistry.registerBlock(blockMachineCompat, ItemMachineCompat.class, "compat");
        blockMachineCompat.setItemName(0, "rpbtengine");
        blockMachineCompat.addTileEntityMapping(0, TileBlueEngine.class);
        itemCompatParts = new ItemParts(Config.getItemID("items.compat.parts.id"), "/eloraam/compat/compat1.png");
        itemCompatParts.addItem(0, 1, "item.rpbgear");
        itemCompatParts.setCreativeTab(CreativeTabs.tabMaterials);
        itemGearBrass = new ItemStack(itemCompatParts, 1, 0);
        Item var1 = (Item)ReflectLib.getStaticField("buildcraft.BuildCraftCore", "stoneGearItem", Item.class);

        if (var1 != null)
        {
            CraftLib.addOreRecipe(new ItemStack(itemCompatParts, 1, 0), new Object[] {" B ", "BGB", " B ", 'B', "ingotBrass", 'G', var1});
        }

        if (Config.getInt("settings.compat.gear.altRecipe") > 0)
        {
            CraftLib.addOreRecipe(new ItemStack(itemCompatParts, 1, 0), new Object[] {" B ", "BIB", " B ", 'B', "ingotBrass", 'I', new ItemStack(RedPowerBase.blockMicro, 1, 5649)});
        }

        CraftLib.addOreRecipe(new ItemStack(blockMachineCompat, 1, 0), new Object[] {"BBB", " G ", "ZMZ", 'B', "ingotBrass", 'G', Block.glass, 'Z', itemGearBrass, 'M', RedPowerBase.itemMotor});
    }
}
