package com.eloraam.redpower;

import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.ItemParts;
import com.eloraam.redpower.logic.BlockLogic;
import com.eloraam.redpower.logic.ItemLogic;
import com.eloraam.redpower.logic.LogicProxy;
import com.eloraam.redpower.logic.TileLogicAdv;
import com.eloraam.redpower.logic.TileLogicArray;
import com.eloraam.redpower.logic.TileLogicPointer;
import com.eloraam.redpower.logic.TileLogicSimple;
import com.eloraam.redpower.logic.TileLogicStorage;
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
import net.minecraft.item.crafting.FurnaceRecipes;

@Mod(
        modid = "RedPowerLogic",
        name = "RedPower Logic",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b",
        dependencies = "required-after:RedPowerBase"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerLogic
{
    @Mod.Instance("RedPowerLogic")
    public static RedPowerLogic instance;
    @SidedProxy(
            clientSide = "com.eloraam.redpower.logic.LogicProxyClient",
            serverSide = "com.eloraam.redpower.logic.LogicProxy"
    )
    public static LogicProxy proxy;
    public static BlockLogic blockLogic;
    public static ItemParts itemParts;
    public static ItemStack itemAnode;
    public static ItemStack itemCathode;
    public static ItemStack itemWire;
    public static ItemStack itemWafer;
    public static ItemStack itemPointer;
    public static ItemStack itemPlate;
    public static ItemStack itemWaferRedwire;
    public static ItemStack itemChip;
    public static ItemStack itemTaintedChip;
    public static ItemStack itemWaferBundle;
    public static boolean EnableSounds;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1) {}

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        EnableSounds = Config.getInt("settings.logic.enableSounds") > 0;
        setupLogic();
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    private static void setupLogic()
    {
        GameRegistry.registerTileEntity(TileLogicSimple.class, "RPLgSmp");
        GameRegistry.registerTileEntity(TileLogicArray.class, "RPLgAr");
        GameRegistry.registerTileEntity(TileLogicStorage.class, "RPLgStor");
        GameRegistry.registerTileEntity(TileLogicAdv.class, "RPLgAdv");
        GameRegistry.registerTileEntity(TileLogicPointer.class, "RPLgPtr");
        itemParts = new ItemParts(Config.getItemID("items.logic.parts.id"), "/eloraam/base/items1.png");
        itemParts.addItem(0, 0, "item.irwafer");
        itemParts.addItem(1, 1, "item.irwire");
        itemParts.addItem(2, 2, "item.iranode");
        itemParts.addItem(3, 3, "item.ircathode");
        itemParts.addItem(4, 4, "item.irpointer");
        itemParts.addItem(5, 5, "item.irredwire");
        itemParts.addItem(6, 6, "item.irplate");
        itemParts.addItem(7, 7, "item.irchip");
        itemParts.addItem(8, 8, "item.irtchip");
        itemParts.addItem(9, 9, "item.irbundle");
        itemWafer = new ItemStack(itemParts, 1, 0);
        itemWire = new ItemStack(itemParts, 1, 1);
        itemAnode = new ItemStack(itemParts, 1, 2);
        itemCathode = new ItemStack(itemParts, 1, 3);
        itemPointer = new ItemStack(itemParts, 1, 4);
        itemWaferRedwire = new ItemStack(itemParts, 1, 5);
        itemPlate = new ItemStack(itemParts, 1, 6);
        itemChip = new ItemStack(itemParts, 1, 7);
        itemTaintedChip = new ItemStack(itemParts, 1, 8);
        itemWaferBundle = new ItemStack(itemParts, 1, 9);
        FurnaceRecipes.smelting().addSmelting(Block.stone.blockID, 0, new ItemStack(itemParts, 2, 0), 0.1F);
        GameRegistry.addRecipe(itemWire, new Object[] {"R", "B", 'B', itemWafer, 'R', Item.redstone});
        GameRegistry.addRecipe(new ItemStack(itemParts, 3, 2), new Object[] {" R ", "RRR", "BBB", 'B', itemWafer, 'R', Item.redstone});
        GameRegistry.addRecipe(itemCathode, new Object[] {"T", "B", 'B', itemWafer, 'T', Block.torchRedstoneActive});
        GameRegistry.addRecipe(itemPointer, new Object[] {"S", "T", "B", 'B', itemWafer, 'S', Block.stone, 'T', Block.torchRedstoneActive});
        GameRegistry.addRecipe(itemWaferRedwire, new Object[] {"W", "B", 'B', itemWafer, 'W', new ItemStack(RedPowerBase.blockMicro, 1, 256)});
        GameRegistry.addRecipe(itemPlate, new Object[] {" B ", "SRS", "BCB", 'B', itemWafer, 'C', itemCathode, 'R', RedPowerBase.itemIngotRed, 'S', Item.stick});
        GameRegistry.addRecipe(CoreLib.copyStack(itemChip, 3), new Object[] {" R ", "BBB", 'B', itemWafer, 'R', RedPowerBase.itemWaferRed});
        GameRegistry.addShapelessRecipe(CoreLib.copyStack(itemTaintedChip, 1), new Object[] {itemChip, Item.lightStoneDust});
        GameRegistry.addRecipe(itemWaferBundle, new Object[] {"W", "B", 'B', itemWafer, 'W', new ItemStack(RedPowerBase.blockMicro, 1, 768)});
        blockLogic = new BlockLogic(Config.getBlockID("blocks.logic.logic.id"));
        GameRegistry.registerBlock(blockLogic, ItemLogic.class, "logic");
        blockLogic.addTileEntityMapping(0, TileLogicPointer.class);
        blockLogic.addTileEntityMapping(1, TileLogicSimple.class);
        blockLogic.addTileEntityMapping(2, TileLogicArray.class);
        blockLogic.addTileEntityMapping(3, TileLogicStorage.class);
        blockLogic.addTileEntityMapping(4, TileLogicAdv.class);
        blockLogic.setItemName(0, "irtimer");
        blockLogic.setItemName(1, "irseq");
        blockLogic.setItemName(2, "irstate");
        blockLogic.setItemName(256, "irlatch");
        blockLogic.setItemName(257, "irnor");
        blockLogic.setItemName(258, "iror");
        blockLogic.setItemName(259, "irnand");
        blockLogic.setItemName(260, "irand");
        blockLogic.setItemName(261, "irxnor");
        blockLogic.setItemName(262, "irxor");
        blockLogic.setItemName(263, "irpulse");
        blockLogic.setItemName(264, "irtoggle");
        blockLogic.setItemName(265, "irnot");
        blockLogic.setItemName(266, "irbuf");
        blockLogic.setItemName(267, "irmux");
        blockLogic.setItemName(268, "irrepeater");
        blockLogic.setItemName(269, "irsync");
        blockLogic.setItemName(270, "irrand");
        blockLogic.setItemName(271, "irdlatch");
        blockLogic.setItemName(272, "rplightsensor");
        blockLogic.setItemName(512, "rpanc");
        blockLogic.setItemName(513, "rpainv");
        blockLogic.setItemName(514, "rpaninv");
        blockLogic.setItemName(768, "ircounter");
        blockLogic.setItemName(1024, "irbusxcvr");
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 0), new Object[] {"BWB", "WPW", "ACA", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode, 'P', itemPointer});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 1), new Object[] {"BCB", "CPC", "BCB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode, 'P', itemPointer});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 2), new Object[] {"BAC", "WSP", "BWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode, 'P', itemPointer, 'S', itemChip});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 256), new Object[] {"WWA", "CBC", "AWW", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 257), new Object[] {"BAB", "WCW", "BWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 258), new Object[] {"BCB", "WCW", "BWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 259), new Object[] {"AAA", "CCC", "BWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 260), new Object[] {"ACA", "CCC", "BWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 261), new Object[] {"ACA", "CAC", "WCW", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 262), new Object[] {"AWA", "CAC", "WCW", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 263), new Object[] {"ACA", "CAC", "WWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 264), new Object[] {"BCB", "WLW", "BCB", 'L', Block.lever, 'W', itemWire, 'B', itemWafer, 'C', itemCathode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 265), new Object[] {"BAB", "ACA", "BWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 266), new Object[] {"ACA", "WCW", "BWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 267), new Object[] {"ACA", "CBC", "ACW", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 268), new Object[] {"BCW", "BAW", "BWC", 'W', itemWire, 'B', itemWafer, 'A', itemAnode, 'C', itemCathode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 269), new Object[] {"WCW", "SAS", "WWW", 'W', itemWire, 'B', itemWafer, 'A', itemAnode, 'C', itemCathode, 'S', itemChip});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 270), new Object[] {"BSB", "WWW", "SWS", 'W', itemWire, 'B', itemWafer, 'S', itemTaintedChip});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 271), new Object[] {"ACW", "CCC", "CWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'A', itemAnode});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 272), new Object[] {"BWB", "BSB", "BBB", 'W', itemWire, 'B', itemWafer, 'S', RedPowerBase.itemWaferBlue});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 768), new Object[] {"BWB", "CPC", "BWB", 'W', itemWire, 'B', itemWafer, 'C', itemCathode, 'P', itemPointer});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 512), new Object[] {"BRB", "RRR", "BRB", 'B', itemWafer, 'R', itemWaferRedwire});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 513), new Object[] {"BRB", "RPR", "BRB", 'B', itemWafer, 'R', itemWaferRedwire, 'P', itemPlate});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 514), new Object[] {"BRB", "RPR", "BRC", 'B', itemWafer, 'C', itemCathode, 'R', itemWaferRedwire, 'P', itemPlate});
        GameRegistry.addRecipe(new ItemStack(blockLogic, 1, 1024), new Object[] {"CCC", "WBW", "CCC", 'B', itemWafer, 'W', RedPowerBase.itemWaferRed, 'C', itemWaferBundle});
    }
}
