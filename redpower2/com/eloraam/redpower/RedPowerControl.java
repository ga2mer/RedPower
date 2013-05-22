package com.eloraam.redpower;

import com.eloraam.redpower.control.BlockPeripheral;
import com.eloraam.redpower.control.ControlProxy;
import com.eloraam.redpower.control.ItemBackplane;
import com.eloraam.redpower.control.ItemDisk;
import com.eloraam.redpower.control.MicroPlacementRibbon;
import com.eloraam.redpower.control.TileBackplane;
import com.eloraam.redpower.control.TileCPU;
import com.eloraam.redpower.control.TileDiskDrive;
import com.eloraam.redpower.control.TileDisplay;
import com.eloraam.redpower.control.TileIOExpander;
import com.eloraam.redpower.control.TileRAM;
import com.eloraam.redpower.control.TileRibbon;
import com.eloraam.redpower.core.BlockExtended;
import com.eloraam.redpower.core.BlockMultipart;
import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CraftLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import com.eloraam.redpower.core.ItemExtended;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DungeonHooks;

@Mod(
        modid = "RedPowerControl",
        name = "RedPower Control",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b",
        dependencies = "required-after:RedPowerBase"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerControl
{
    @Mod.Instance("RedPowerControl")
    public static RedPowerControl instance;
    @SidedProxy(
            clientSide = "com.eloraam.redpower.control.ControlProxyClient",
            serverSide = "com.eloraam.redpower.control.ControlProxy"
    )
    public static ControlProxy proxy;
    public static BlockExtended blockBackplane;
    public static BlockExtended blockPeripheral;
    public static BlockExtended blockFlatPeripheral;
    public static ItemDisk itemDisk;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1) {}

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        setupBlocks();
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    private static void setupBlocks()
    {
        blockBackplane = new BlockMultipart(Config.getBlockID("blocks.control.backplane.id"), CoreLib.materialRedpower);
        GameRegistry.registerBlock(blockBackplane, ItemBackplane.class, "backplane");
        blockBackplane.setHardness(1.0F);
        blockBackplane.setItemName(0, "rpbackplane");
        blockBackplane.setItemName(1, "rpram");
        blockPeripheral = new BlockPeripheral(Config.getBlockID("blocks.control.peripheral.id"));
        GameRegistry.registerBlock(blockPeripheral, ItemExtended.class, "peripheral");
        blockPeripheral.setHardness(1.0F);
        blockPeripheral.setItemName(0, "rpdisplay");
        blockPeripheral.setItemName(1, "rpcpu");
        blockPeripheral.setItemName(2, "rpdiskdrive");
        blockFlatPeripheral = new BlockMultipart(Config.getBlockID("blocks.control.peripheralFlat.id"), Material.rock);
        blockFlatPeripheral.setCreativeTab(CreativeExtraTabs.tabMachine);
        GameRegistry.registerBlock(blockFlatPeripheral, ItemExtended.class, "peripheralFlat");
        blockFlatPeripheral.setHardness(1.0F);
        blockFlatPeripheral.setItemName(0, "rpioexp");
        GameRegistry.registerTileEntity(TileBackplane.class, "RPConBP");
        blockBackplane.addTileEntityMapping(0, TileBackplane.class);
        GameRegistry.registerTileEntity(TileRAM.class, "RPConRAM");
        blockBackplane.addTileEntityMapping(1, TileRAM.class);
        GameRegistry.registerTileEntity(TileDisplay.class, "RPConDisp");
        blockPeripheral.addTileEntityMapping(0, TileDisplay.class);
        GameRegistry.registerTileEntity(TileDiskDrive.class, "RPConDDrv");
        blockPeripheral.addTileEntityMapping(2, TileDiskDrive.class);
        GameRegistry.registerTileEntity(TileCPU.class, "RPConCPU");
        blockPeripheral.addTileEntityMapping(1, TileCPU.class);
        GameRegistry.registerTileEntity(TileIOExpander.class, "RPConIOX");
        blockFlatPeripheral.addTileEntityMapping(0, TileIOExpander.class);
        GameRegistry.registerTileEntity(TileRibbon.class, "RPConRibbon");
        RedPowerBase.blockMicro.addTileEntityMapping(12, TileRibbon.class);
        MicroPlacementRibbon var0 = new MicroPlacementRibbon();
        RedPowerBase.blockMicro.registerPlacement(12, var0);
        itemDisk = new ItemDisk(Config.getItemID("items.control.disk.id"));
        CraftLib.addOreRecipe(new ItemStack(itemDisk, 1), new Object[] {"WWW", "W W", "WIW", 'I', Item.ingotIron, 'W', "plankWood"});
        GameRegistry.addShapelessRecipe(new ItemStack(itemDisk, 1, 1), new Object[] {new ItemStack(itemDisk, 1, 0), Item.redstone});
        GameRegistry.addShapelessRecipe(new ItemStack(itemDisk, 1, 2), new Object[] {new ItemStack(itemDisk, 1, 1), Item.redstone});
        GameRegistry.addRecipe(new ItemStack(blockBackplane, 1, 0), new Object[] {"ICI", "IGI", "ICI", 'C', RedPowerBase.itemFineCopper, 'I', Block.fenceIron, 'G', Item.ingotGold});
        GameRegistry.addRecipe(new ItemStack(blockBackplane, 1, 1), new Object[] {"IRI", "RDR", "IRI", 'I', Block.fenceIron, 'R', RedPowerBase.itemWaferRed, 'D', Item.diamond});
        CraftLib.addOreRecipe(new ItemStack(blockPeripheral, 1, 0), new Object[] {"GWW", "GPR", "GBW", 'P', new ItemStack(RedPowerBase.itemLumar, 1, 5), 'G', Block.glass, 'W', "plankWood", 'R', RedPowerBase.itemWaferRed, 'B', new ItemStack(RedPowerBase.blockMicro, 1, 3072)});
        CraftLib.addOreRecipe(new ItemStack(blockPeripheral, 1, 1), new Object[] {"WWW", "RDR", "WBW", 'W', "plankWood", 'D', Block.blockDiamond, 'R', RedPowerBase.itemWaferRed, 'B', new ItemStack(RedPowerBase.blockMicro, 1, 3072)});
        CraftLib.addOreRecipe(new ItemStack(blockPeripheral, 1, 2), new Object[] {"WWW", "WMR", "WBW", 'G', Block.glass, 'W', "plankWood", 'M', RedPowerBase.itemMotor, 'R', RedPowerBase.itemWaferRed, 'B', new ItemStack(RedPowerBase.blockMicro, 1, 3072)});
        CraftLib.addOreRecipe(new ItemStack(blockFlatPeripheral, 1, 0), new Object[] {"WCW", "WRW", "WBW", 'W', "plankWood", 'R', RedPowerBase.itemWaferRed, 'C', new ItemStack(RedPowerBase.blockMicro, 1, 768), 'B', new ItemStack(RedPowerBase.blockMicro, 1, 3072)});
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 8, 3072), new Object[] {"C", "C", "C", 'C', RedPowerBase.itemFineCopper});
        //DungeonHooks.addDungeonLoot(new ItemStack(itemDisk, 1, 1), 1);
        //DungeonHooks.addDungeonLoot(new ItemStack(itemDisk, 1, 2), 1);
    }
}
