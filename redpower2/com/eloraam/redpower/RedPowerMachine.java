package com.eloraam.redpower;

import com.eloraam.redpower.core.AchieveLib;
import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CraftLib;
import com.eloraam.redpower.core.ItemExtended;
import com.eloraam.redpower.core.ItemParts;
import com.eloraam.redpower.core.ItemTextured;
import com.eloraam.redpower.machine.BlockFrame;
import com.eloraam.redpower.machine.BlockMachine;
import com.eloraam.redpower.machine.BlockMachinePanel;
import com.eloraam.redpower.machine.ItemBattery;
import com.eloraam.redpower.machine.ItemMachinePanel;
import com.eloraam.redpower.machine.ItemSonicDriver;
import com.eloraam.redpower.machine.ItemVoltmeter;
import com.eloraam.redpower.machine.ItemWindmill;
import com.eloraam.redpower.machine.MachineProxy;
import com.eloraam.redpower.machine.MicroPlacementTube;
import com.eloraam.redpower.machine.TileAccel;
import com.eloraam.redpower.machine.TileAssemble;
import com.eloraam.redpower.machine.TileBatteryBox;
import com.eloraam.redpower.machine.TileBlueAlloyFurnace;
import com.eloraam.redpower.machine.TileBlueFurnace;
import com.eloraam.redpower.machine.TileBreaker;
import com.eloraam.redpower.machine.TileBufferChest;
import com.eloraam.redpower.machine.TileChargingBench;
import com.eloraam.redpower.machine.TileDeploy;
import com.eloraam.redpower.machine.TileEject;
import com.eloraam.redpower.machine.TileFilter;
import com.eloraam.redpower.machine.TileFrame;
import com.eloraam.redpower.machine.TileFrameMoving;
import com.eloraam.redpower.machine.TileFrameRedstoneTube;
import com.eloraam.redpower.machine.TileFrameTube;
import com.eloraam.redpower.machine.TileGrate;
import com.eloraam.redpower.machine.TileIgniter;
import com.eloraam.redpower.machine.TileItemDetect;
import com.eloraam.redpower.machine.TileMagTube;
import com.eloraam.redpower.machine.TileManager;
import com.eloraam.redpower.machine.TileMotor;
import com.eloraam.redpower.machine.TilePipe;
import com.eloraam.redpower.machine.TilePump;
import com.eloraam.redpower.machine.TileRedstoneTube;
import com.eloraam.redpower.machine.TileRegulator;
import com.eloraam.redpower.machine.TileRelay;
import com.eloraam.redpower.machine.TileRestrictTube;
import com.eloraam.redpower.machine.TileRetriever;
import com.eloraam.redpower.machine.TileSolarPanel;
import com.eloraam.redpower.machine.TileSorter;
import com.eloraam.redpower.machine.TileSortron;
import com.eloraam.redpower.machine.TileThermopile;
import com.eloraam.redpower.machine.TileTransformer;
import com.eloraam.redpower.machine.TileTranspose;
import com.eloraam.redpower.machine.TileTube;
import com.eloraam.redpower.machine.TileWindTurbine;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

@Mod(
        modid = "RedPowerMachine",
        name = "RedPower Machine",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b",
        dependencies = "required-after:RedPowerBase"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerMachine
{
    @Mod.Instance("RedPowerMachine")
    public static RedPowerMachine instance;
    @SidedProxy(
            clientSide = "com.eloraam.redpower.machine.MachineProxyClient",
            serverSide = "com.eloraam.redpower.machine.MachineProxy"
    )
    public static MachineProxy proxy;
    public static BlockMachine blockMachine;
    public static BlockMachine blockMachine2;
    public static BlockMachinePanel blockMachinePanel;
    public static BlockFrame blockFrame;
    public static ItemVoltmeter itemVoltmeter;
    public static ItemSonicDriver itemSonicDriver;
    public static Item itemBatteryEmpty;
    public static Item itemBatteryPowered;
    public static ItemParts itemMachineParts;
    public static ItemStack itemWoodSail;
    public static Item itemWoodTurbine;
    public static Item itemWoodWindmill;
    public static final String textureFile = "/eloraam/machine/machine1.png";
    public static final String textureFile2 = "/eloraam/machine/machine2.png";
    public static boolean FrameAlwaysCrate;
    public static int FrameLinkSize;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1) {}

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        FrameAlwaysCrate = Config.getInt("settings.machine.frame.alwayscrate") > 0;
        FrameLinkSize = Config.getInt("settings.machine.frame.linksize");
        setupItems();
        setupBlocks();
        initAchievements();
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    private static void setupItems()
    {
        itemVoltmeter = new ItemVoltmeter(Config.getItemID("items.machine.voltmeter.id"));
        itemVoltmeter.setUnlocalizedName("voltmeter");
        itemBatteryEmpty = new ItemTextured(Config.getItemID("items.machine.battery.empty.id"), 25, "/eloraam/base/items1.png");
        itemBatteryEmpty.setUnlocalizedName("btbattery");
        itemBatteryPowered = new ItemBattery(Config.getItemID("items.machine.battery.powered.id"));
        itemBatteryPowered.setUnlocalizedName("btbattery");
        CraftLib.addOreRecipe(new ItemStack(itemVoltmeter), new Object[] {"WWW", "WNW", "CCC", 'W', "plankWood", 'N', RedPowerBase.itemNikolite, 'C', "ingotCopper"});
        CraftLib.addOreRecipe(new ItemStack(itemBatteryEmpty, 1), new Object[] {"NCN", "NTN", "NCN", 'N', RedPowerBase.itemNikolite, 'C', "ingotCopper", 'T', "ingotTin"});
        itemSonicDriver = new ItemSonicDriver(Config.getItemID("items.machine.sonicDriver.id"));
        itemSonicDriver.setUnlocalizedName("sonicDriver");
        GameRegistry.addRecipe(new ItemStack(itemSonicDriver, 1, itemSonicDriver.getMaxDamage()), new Object[] {"E  ", " R ", "  B", 'R', RedPowerBase.itemIngotBrass, 'E', RedPowerBase.itemGreenSapphire, 'B', itemBatteryEmpty});
        itemWoodTurbine = new ItemWindmill(Config.getItemID("items.machine.turbineWood.id"), 1);
        itemWoodWindmill = new ItemWindmill(Config.getItemID("items.machine.windmillWood.id"), 2);
        itemWoodWindmill.setUnlocalizedName("windmillWood");
        itemMachineParts = new ItemParts(Config.getItemID("items.machine.parts.id"), "/eloraam/base/items1.png");
        itemMachineParts.addItem(0, 176, "item.windSailWood");
        itemWoodSail = new ItemStack(itemMachineParts, 1, 0);
        CraftLib.addOreRecipe(itemWoodSail, new Object[] {"CCS", "CCW", "CCS", 'C', RedPowerBase.itemCanvas, 'W', "plankWood", 'S', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemWoodTurbine), new Object[] {"SAS", "SAS", "SAS", 'S', itemWoodSail, 'A', new ItemStack(RedPowerBase.blockMicro, 1, 5905)});
        GameRegistry.addRecipe(new ItemStack(itemWoodWindmill), new Object[] {" S ", "SAS", " S ", 'S', itemWoodSail, 'A', new ItemStack(RedPowerBase.blockMicro, 1, 5905)});
    }

    private static void setupBlocks()
    {
        blockMachine = new BlockMachine(Config.getBlockID("blocks.machine.machine.id"));
        blockMachine.setUnlocalizedName("rpmachine");
        GameRegistry.registerBlock(blockMachine, ItemExtended.class, "machine");
        blockMachine.setItemName(0, "rpdeploy");
        blockMachine.setItemName(1, "rpbreaker");
        blockMachine.setItemName(2, "rptranspose");
        blockMachine.setItemName(3, "rpfilter");
        blockMachine.setItemName(4, "rpitemdet");
        blockMachine.setItemName(5, "rpsorter");
        blockMachine.setItemName(6, "rpbatbox");
        blockMachine.setItemName(7, "rpmotor");
        blockMachine.setItemName(8, "rpretriever");
        blockMachine.setItemName(9, "rpkgen");
        blockMachine.setItemName(10, "rpregulate");
        blockMachine.setItemName(11, "rpthermo");
        blockMachine.setItemName(12, "rpignite");
        blockMachine.setItemName(13, "rpassemble");
        blockMachine.setItemName(14, "rpeject");
        blockMachine.setItemName(15, "rprelay");
        GameRegistry.registerTileEntity(TileWindTurbine.class, "RPWind");
        GameRegistry.registerTileEntity(TilePipe.class, "RPPipe");
        GameRegistry.registerTileEntity(TilePump.class, "RPPump");
        GameRegistry.registerTileEntity(TileTube.class, "RPTube");
        GameRegistry.registerTileEntity(TileRedstoneTube.class, "RPRSTube");
        GameRegistry.registerTileEntity(TileRestrictTube.class, "RPRTube");
        GameRegistry.registerTileEntity(TileMagTube.class, "RPMTube");
        GameRegistry.registerTileEntity(TileAccel.class, "RPAccel");
        GameRegistry.registerTileEntity(TileDeploy.class, "RPDeploy");
        GameRegistry.registerTileEntity(TileBreaker.class, "RPBreaker");
        GameRegistry.registerTileEntity(TileTranspose.class, "RPTranspose");
        GameRegistry.registerTileEntity(TileFilter.class, "RPFilter");
        GameRegistry.registerTileEntity(TileItemDetect.class, "RPItemDet");
        GameRegistry.registerTileEntity(TileSorter.class, "RPSorter");
        GameRegistry.registerTileEntity(TileBatteryBox.class, "RPBatBox");
        GameRegistry.registerTileEntity(TileMotor.class, "RPMotor");
        GameRegistry.registerTileEntity(TileRetriever.class, "RPRetrieve");
        GameRegistry.registerTileEntity(TileRegulator.class, "RPRegulate");
        GameRegistry.registerTileEntity(TileThermopile.class, "RPThermo");
        GameRegistry.registerTileEntity(TileIgniter.class, "RPIgnite");
        GameRegistry.registerTileEntity(TileAssemble.class, "RPAssemble");
        GameRegistry.registerTileEntity(TileEject.class, "RPEject");
        GameRegistry.registerTileEntity(TileRelay.class, "RPRelay");
        blockMachine.addTileEntityMapping(0, TileDeploy.class);
        blockMachine.addTileEntityMapping(1, TileBreaker.class);
        blockMachine.addTileEntityMapping(2, TileTranspose.class);
        blockMachine.addTileEntityMapping(3, TileFilter.class);
        blockMachine.addTileEntityMapping(4, TileItemDetect.class);
        blockMachine.addTileEntityMapping(5, TileSorter.class);
        blockMachine.addTileEntityMapping(6, TileBatteryBox.class);
        blockMachine.addTileEntityMapping(7, TileMotor.class);
        blockMachine.addTileEntityMapping(8, TileRetriever.class);
        blockMachine.addTileEntityMapping(9, TileWindTurbine.class);
        blockMachine.addTileEntityMapping(10, TileRegulator.class);
        blockMachine.addTileEntityMapping(11, TileThermopile.class);
        blockMachine.addTileEntityMapping(12, TileIgniter.class);
        blockMachine.addTileEntityMapping(13, TileAssemble.class);
        blockMachine.addTileEntityMapping(14, TileEject.class);
        blockMachine.addTileEntityMapping(15, TileRelay.class);
        blockMachine2 = new BlockMachine(Config.getBlockID("blocks.machine.machine2.id"));
        blockMachine.setUnlocalizedName("rpmachine2");
        GameRegistry.registerBlock(blockMachine2, ItemExtended.class, "machine2");
        blockMachine2.setItemName(0, "rpsortron");
        blockMachine2.setItemName(1, "rpmanager");
        GameRegistry.registerTileEntity(TileSortron.class, "RPSortron");
        GameRegistry.registerTileEntity(TileManager.class, "RPManager");
        blockMachine2.addTileEntityMapping(0, TileSortron.class);
        blockMachine2.addTileEntityMapping(1, TileManager.class);
        blockMachinePanel = new BlockMachinePanel(Config.getBlockID("blocks.machine.machinePanel.id"));
        GameRegistry.registerBlock(blockMachinePanel, ItemMachinePanel.class, "machinePanel");
        GameRegistry.registerTileEntity(TileSolarPanel.class, "RPSolar");
        GameRegistry.registerTileEntity(TileGrate.class, "RPGrate");
        GameRegistry.registerTileEntity(TileTransformer.class, "RPXfmr");
        blockMachinePanel.addTileEntityMapping(0, TileSolarPanel.class);
        blockMachinePanel.addTileEntityMapping(1, TilePump.class);
        blockMachinePanel.addTileEntityMapping(2, TileAccel.class);
        blockMachinePanel.addTileEntityMapping(3, TileGrate.class);
        blockMachinePanel.addTileEntityMapping(4, TileTransformer.class);
        blockMachinePanel.setItemName(0, "rpsolar");
        blockMachinePanel.setItemName(1, "rppump");
        blockMachinePanel.setItemName(2, "rpaccel");
        blockMachinePanel.setItemName(3, "rpgrate");
        blockMachinePanel.setItemName(4, "rptransformer");
        GameRegistry.registerTileEntity(TileBlueFurnace.class, "RPBFurnace");
        GameRegistry.registerTileEntity(TileBufferChest.class, "RPBuffer");
        GameRegistry.registerTileEntity(TileBlueAlloyFurnace.class, "RPBAFurnace");
        GameRegistry.registerTileEntity(TileChargingBench.class, "RPCharge");
        RedPowerBase.blockAppliance.setItemName(1, "rpbfurnace");
        RedPowerBase.blockAppliance.addTileEntityMapping(1, TileBlueFurnace.class);
        RedPowerBase.blockAppliance.setItemName(2, "rpbuffer");
        RedPowerBase.blockAppliance.addTileEntityMapping(2, TileBufferChest.class);
        RedPowerBase.blockAppliance.setItemName(4, "rpbafurnace");
        RedPowerBase.blockAppliance.addTileEntityMapping(4, TileBlueAlloyFurnace.class);
        RedPowerBase.blockAppliance.setItemName(5, "rpcharge");
        RedPowerBase.blockAppliance.addTileEntityMapping(5, TileChargingBench.class);
        blockFrame = new BlockFrame(Config.getBlockID("blocks.machine.frame.id"));
        GameRegistry.registerBlock(blockFrame, ItemExtended.class, "frame");
        blockFrame.setUnlocalizedName("rpframe");
        blockFrame.setItemName(0, "rpframe");
        blockFrame.setItemName(2, "rptframe");
        blockFrame.setItemName(3, "rprtframe");
        GameRegistry.registerTileEntity(TileFrame.class, "RPFrame");
        GameRegistry.registerTileEntity(TileFrameMoving.class, "RPMFrame");
        GameRegistry.registerTileEntity(TileFrameTube.class, "RPTFrame");
        GameRegistry.registerTileEntity(TileFrameRedstoneTube.class, "RPRTFrame");
        blockFrame.addTileEntityMapping(0, TileFrame.class);
        blockFrame.addTileEntityMapping(1, TileFrameMoving.class);
        blockFrame.addTileEntityMapping(2, TileFrameTube.class);
        blockFrame.addTileEntityMapping(3, TileFrameRedstoneTube.class);
        MicroPlacementTube var0 = new MicroPlacementTube();
        RedPowerBase.blockMicro.registerPlacement(7, var0);
        RedPowerBase.blockMicro.registerPlacement(8, var0);
        RedPowerBase.blockMicro.registerPlacement(9, var0);
        RedPowerBase.blockMicro.registerPlacement(10, var0);
        RedPowerBase.blockMicro.registerPlacement(11, var0);
        RedPowerBase.blockMicro.addTileEntityMapping(7, TilePipe.class);
        RedPowerBase.blockMicro.addTileEntityMapping(8, TileTube.class);
        RedPowerBase.blockMicro.addTileEntityMapping(9, TileRedstoneTube.class);
        RedPowerBase.blockMicro.addTileEntityMapping(10, TileRestrictTube.class);
        RedPowerBase.blockMicro.addTileEntityMapping(11, TileMagTube.class);
        GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 0), new Object[] {"SCS", "SPS", "SRS", 'S', Block.cobblestone, 'C', Block.chest, 'R', Item.redstone, 'P', Block.pistonBase});
        GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 1), new Object[] {"SAS", "SPS", "SRS", 'S', Block.cobblestone, 'A', Item.pickaxeIron, 'R', Item.redstone, 'P', Block.pistonBase});
        CraftLib.addOreRecipe(new ItemStack(blockMachine, 1, 2), new Object[] {"SSS", "WPW", "SRS", 'S', Block.cobblestone, 'R', Item.redstone, 'P', Block.pistonBase, 'W', "plankWood"});
        GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 3), new Object[] {"SSS", "GPG", "SRS", 'S', Block.cobblestone, 'R', RedPowerBase.itemWaferRed, 'P', Block.pistonBase, 'G', Item.ingotGold});
        CraftLib.addOreRecipe(new ItemStack(blockMachine, 1, 4), new Object[] {"BTB", "RPR", "WTW", 'B', "ingotBrass", 'T', new ItemStack(RedPowerBase.blockMicro, 1, 2048), 'R', RedPowerBase.itemWaferRed, 'W', "plankWood", 'P', Block.pressurePlatePlanks});
        GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 5), new Object[] {"III", "RFR", "IBI", 'B', RedPowerBase.itemIngotBlue, 'R', RedPowerBase.itemWaferRed, 'F', new ItemStack(blockMachine, 1, 3), 'I', Item.ingotIron});
        GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 8), new Object[] {"BLB", "EFE", "INI", 'N', RedPowerBase.itemIngotBlue, 'B', RedPowerBase.itemIngotBrass, 'E', Item.enderPearl, 'L', Item.leather, 'F', new ItemStack(blockMachine, 1, 3), 'I', Item.ingotIron});
        GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 9), new Object[] {"IBI", "IMI", "IUI", 'I', Item.ingotIron, 'B', RedPowerBase.itemIngotBrass, 'M', RedPowerBase.itemMotor, 'U', RedPowerBase.itemIngotBlue});
        CraftLib.addOreRecipe(new ItemStack(RedPowerBase.blockAppliance, 1, 2), new Object[] {"BWB", "W W", "BWB", 'B', Block.fenceIron, 'W', "plankWood"});
        CraftLib.addOreRecipe(new ItemStack(blockMachine, 1, 10), new Object[] {"BCB", "RDR", "WCW", 'R', RedPowerBase.itemWaferRed, 'B', "ingotBrass", 'D', new ItemStack(blockMachine, 1, 4), 'W', "plankWood", 'C', new ItemStack(RedPowerBase.blockAppliance, 1, 2)});
        CraftLib.addOreRecipe(new ItemStack(blockMachine, 1, 11), new Object[] {"CIC", "WBW", "CIC", 'I', Item.ingotIron, 'B', RedPowerBase.itemIngotBlue, 'W', RedPowerBase.itemWaferBlue, 'C', "ingotCopper"});
        CraftLib.addOreRecipe(new ItemStack(RedPowerBase.blockMicro, 8, 2048), new Object[] {"BGB", 'G', Block.glass, 'B', "ingotBrass"});
        GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 2304), new Object[] {Item.redstone, new ItemStack(RedPowerBase.blockMicro, 1, 2048)});
        GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.blockMicro, 1, 2560), new Object[] {Item.ingotIron, new ItemStack(RedPowerBase.blockMicro, 1, 2048)});
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 8, 2816), new Object[] {"CCC", "OGO", "CCC", 'G', Block.glass, 'O', Block.obsidian, 'C', RedPowerBase.itemFineCopper});
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockAppliance, 1, 1), new Object[] {"CCC", "C C", "IBI", 'C', Block.blockClay, 'B', RedPowerBase.itemIngotBlue, 'I', Item.ingotIron});
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockAppliance, 1, 4), new Object[] {"CCC", "C C", "IBI", 'C', Block.brick, 'B', RedPowerBase.itemIngotBlue, 'I', Item.ingotIron});
        GameRegistry.addRecipe(new ItemStack(blockMachinePanel, 1, 0), new Object[] {"WWW", "WBW", "WWW", 'W', RedPowerBase.itemWaferBlue, 'B', RedPowerBase.itemIngotBlue});
        GameRegistry.addRecipe(new ItemStack(blockMachinePanel, 1, 2), new Object[] {"BOB", "O O", "BOB", 'O', Block.obsidian, 'B', RedPowerBase.itemIngotBlue});
        CraftLib.addOreRecipe(new ItemStack(blockMachine, 1, 6), new Object[] {"BWB", "BIB", "IAI", 'I', Item.ingotIron, 'W', "plankWood", 'A', RedPowerBase.itemIngotBlue, 'B', itemBatteryEmpty});
        GameRegistry.addRecipe(new ItemStack(blockMachinePanel, 1, 4), new Object[] {"III", "CIC", "BIB", 'I', Item.ingotIron, 'C', RedPowerBase.itemCopperCoil, 'B', RedPowerBase.itemIngotBlue});
        GameRegistry.addRecipe(new ItemStack(blockMachine2, 1, 0), new Object[] {"IDI", "RSR", "IWI", 'D', Item.diamond, 'I', Item.ingotIron, 'R', RedPowerBase.itemWaferRed, 'W', new ItemStack(RedPowerBase.blockMicro, 1, 3072), 'S', new ItemStack(blockMachine, 1, 5)});
        CraftLib.addOreRecipe(new ItemStack(blockMachine2, 1, 1), new Object[] {"IMI", "RSR", "WBW", 'I', Item.ingotIron, 'R', RedPowerBase.itemWaferRed, 'S', new ItemStack(blockMachine, 1, 5), 'M', new ItemStack(blockMachine, 1, 10), 'W', "plankWood", 'B', RedPowerBase.itemIngotBlue});
        CraftLib.addOreRecipe(new ItemStack(RedPowerBase.blockAppliance, 1, 5), new Object[] {"OQO", "BCB", "WUW", 'O', Block.obsidian, 'W', "plankWood", 'U', RedPowerBase.itemIngotBlue, 'C', Block.chest, 'Q', RedPowerBase.itemCopperCoil, 'B', itemBatteryEmpty});
        GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 12), new Object[] {"NFN", "SDS", "SRS", 'N', Block.netherrack, 'F', Item.flintAndSteel, 'D', new ItemStack(blockMachine, 1, 0), 'S', Block.cobblestone, 'R', Item.redstone});
        GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 13), new Object[] {"BIB", "CDC", "IRI", 'I', Item.ingotIron, 'D', new ItemStack(blockMachine, 1, 0), 'C', new ItemStack(RedPowerBase.blockMicro, 1, 768), 'R', RedPowerBase.itemWaferRed, 'B', RedPowerBase.itemIngotBrass});
        CraftLib.addOreRecipe(new ItemStack(blockMachine, 1, 14), new Object[] {"WBW", "WTW", "SRS", 'R', Item.redstone, 'T', new ItemStack(blockMachine, 1, 2), 'W', "plankWood", 'B', new ItemStack(RedPowerBase.blockAppliance, 1, 2), 'S', Block.cobblestone});
        CraftLib.addOreRecipe(new ItemStack(blockMachine, 1, 15), new Object[] {"WBW", "WTW", "SRS", 'R', RedPowerBase.itemWaferRed, 'T', new ItemStack(blockMachine, 1, 2), 'W', "plankWood", 'B', new ItemStack(RedPowerBase.blockAppliance, 1, 2), 'S', Block.cobblestone});
        GameRegistry.addRecipe(RedPowerBase.itemCopperCoil, new Object[] {"FBF", "BIB", "FBF", 'F', RedPowerBase.itemFineCopper, 'B', Block.fenceIron, 'I', Item.ingotIron});
        GameRegistry.addRecipe(RedPowerBase.itemMotor, new Object[] {"ICI", "ICI", "IBI", 'C', RedPowerBase.itemCopperCoil, 'B', RedPowerBase.itemIngotBlue, 'I', Item.ingotIron});
        CraftLib.addOreRecipe(new ItemStack(blockFrame, 1), new Object[] {"SSS", "SBS", "SSS", 'S', Item.stick, 'B', "ingotBrass"});
        GameRegistry.addShapelessRecipe(new ItemStack(blockFrame, 1, 2), new Object[] {new ItemStack(blockFrame, 1), new ItemStack(RedPowerBase.blockMicro, 1, 2048)});
        GameRegistry.addShapelessRecipe(new ItemStack(blockFrame, 1, 3), new Object[] {new ItemStack(blockFrame, 1), new ItemStack(RedPowerBase.blockMicro, 1, 2304)});
        GameRegistry.addShapelessRecipe(new ItemStack(blockFrame, 1, 3), new Object[] {new ItemStack(blockFrame, 1, 2), Item.redstone});
        CraftLib.addOreRecipe(new ItemStack(blockMachine, 1, 7), new Object[] {"III", "BMB", "IAI", 'I', Item.ingotIron, 'A', RedPowerBase.itemIngotBlue, 'B', "ingotBrass", 'M', RedPowerBase.itemMotor});
        CraftLib.addOreRecipe(new ItemStack(RedPowerBase.blockMicro, 16, 1792), new Object[] {"B B", "BGB", "B B", 'G', Block.glass, 'B', "ingotBrass"});
        GameRegistry.addRecipe(new ItemStack(blockMachinePanel, 1, 3), new Object[] {"III", "I I", "IPI", 'P', new ItemStack(RedPowerBase.blockMicro, 1, 1792), 'I', Block.fenceIron});
        GameRegistry.addRecipe(new ItemStack(blockMachinePanel, 1, 1), new Object[] {"III", "PMP", "IAI", 'I', Item.ingotIron, 'A', RedPowerBase.itemIngotBlue, 'P', new ItemStack(RedPowerBase.blockMicro, 1, 1792), 'M', RedPowerBase.itemMotor});
    }

    public static void initAchievements()
    {
        AchieveLib.registerAchievement(117283, "rpTranspose", -2, 2, new ItemStack(blockMachine, 1, 2), AchievementList.acquireIron);
        AchieveLib.registerAchievement(117284, "rpBreaker", -2, 4, new ItemStack(blockMachine, 1, 1), AchievementList.acquireIron);
        AchieveLib.registerAchievement(117285, "rpDeploy", -2, 6, new ItemStack(blockMachine, 1, 0), AchievementList.acquireIron);
        AchieveLib.addCraftingAchievement(new ItemStack(blockMachine, 1, 2), "rpTranspose");
        AchieveLib.addCraftingAchievement(new ItemStack(blockMachine, 1, 1), "rpBreaker");
        AchieveLib.addCraftingAchievement(new ItemStack(blockMachine, 1, 0), "rpDeploy");
        AchieveLib.registerAchievement(117286, "rpFrames", 4, 4, new ItemStack(blockMachine, 1, 7), "rpIngotBlue");
        AchieveLib.registerAchievement(117287, "rpPump", 4, 5, new ItemStack(blockMachinePanel, 1, 1), "rpIngotBlue");
        AchieveLib.addCraftingAchievement(new ItemStack(blockMachine, 1, 7), "rpFrames");
        AchieveLib.addCraftingAchievement(new ItemStack(blockMachinePanel, 1, 1), "rpPump");
    }

    public static int blockDamageDropped(Block var0, int var1)
    {
        return var0.damageDropped(var1);
    }
}
