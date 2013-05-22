package com.eloraam.redpower;

import com.eloraam.redpower.base.ItemHandsaw;
import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.CraftLib;
import com.eloraam.redpower.core.ItemPartialCraft;
import com.eloraam.redpower.core.ItemTextured;
import com.eloraam.redpower.world.BlockBrickMossifier;
import com.eloraam.redpower.world.BlockCobbleMossifier;
import com.eloraam.redpower.world.BlockCustomCrops;
import com.eloraam.redpower.world.BlockCustomFlower;
import com.eloraam.redpower.world.BlockCustomLeaves;
import com.eloraam.redpower.world.BlockCustomLog;
import com.eloraam.redpower.world.BlockCustomOre;
import com.eloraam.redpower.world.BlockCustomStone;
import com.eloraam.redpower.world.BlockStorage;
import com.eloraam.redpower.world.EnchantmentDisjunction;
import com.eloraam.redpower.world.EnchantmentVorpal;
import com.eloraam.redpower.world.ItemAthame;
import com.eloraam.redpower.world.ItemCustomAxe;
import com.eloraam.redpower.world.ItemCustomFlower;
import com.eloraam.redpower.world.ItemCustomHoe;
import com.eloraam.redpower.world.ItemCustomOre;
import com.eloraam.redpower.world.ItemCustomPickaxe;
import com.eloraam.redpower.world.ItemCustomSeeds;
import com.eloraam.redpower.world.ItemCustomShovel;
import com.eloraam.redpower.world.ItemCustomStone;
import com.eloraam.redpower.world.ItemCustomSword;
import com.eloraam.redpower.world.ItemPaintBrush;
import com.eloraam.redpower.world.ItemPaintCan;
import com.eloraam.redpower.world.ItemSeedBag;
import com.eloraam.redpower.world.ItemSickle;
import com.eloraam.redpower.world.ItemStorage;
import com.eloraam.redpower.world.ItemWoolCard;
import com.eloraam.redpower.world.WorldEvents;
import com.eloraam.redpower.world.WorldGenHandler;
import com.eloraam.redpower.world.WorldProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@Mod(
        modid = "RedPowerWorld",
        name = "RedPower World",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b",
        dependencies = "required-after:RedPowerBase"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerWorld
{
    @Mod.Instance("RedPowerWorld")
    public static RedPowerWorld instance;
    @SidedProxy(
            clientSide = "com.eloraam.redpower.world.WorldProxyClient",
            serverSide = "com.eloraam.redpower.world.WorldProxy"
    )
    public static WorldProxy proxy;
    public static BlockCustomFlower blockPlants;
    public static BlockCustomOre blockOres;
    public static BlockCustomLeaves blockLeaves;
    public static BlockCustomLog blockLogs;
    public static BlockCustomStone blockStone;
    public static BlockCustomCrops blockCrops;
    public static BlockStorage blockStorage;
    public static ItemStack itemOreRuby;
    public static ItemStack itemOreGreenSapphire;
    public static ItemStack itemOreSapphire;
    public static ItemStack itemMarble;
    public static EnumToolMaterial toolMaterialRuby;
    public static EnumToolMaterial toolMaterialGreenSapphire;
    public static EnumToolMaterial toolMaterialSapphire;
    public static ItemSickle itemSickleWood;
    public static ItemSickle itemSickleStone;
    public static ItemSickle itemSickleIron;
    public static ItemSickle itemSickleDiamond;
    public static ItemSickle itemSickleGold;
    public static ItemSickle itemSickleRuby;
    public static ItemSickle itemSickleGreenSapphire;
    public static ItemSickle itemSickleSapphire;
    public static ItemCustomPickaxe itemPickaxeRuby;
    public static ItemCustomPickaxe itemPickaxeGreenSapphire;
    public static ItemCustomPickaxe itemPickaxeSapphire;
    public static ItemCustomShovel itemShovelRuby;
    public static ItemCustomShovel itemShovelGreenSapphire;
    public static ItemCustomShovel itemShovelSapphire;
    public static ItemCustomAxe itemAxeRuby;
    public static ItemCustomAxe itemAxeGreenSapphire;
    public static ItemCustomAxe itemAxeSapphire;
    public static ItemCustomSword itemSwordRuby;
    public static ItemCustomSword itemSwordGreenSapphire;
    public static ItemCustomSword itemSwordSapphire;
    public static ItemAthame itemAthame;
    public static ItemCustomHoe itemHoeRuby;
    public static ItemCustomHoe itemHoeGreenSapphire;
    public static ItemCustomHoe itemHoeSapphire;
    public static ItemCustomSeeds itemSeeds;
    public static Item itemHandsawRuby;
    public static Item itemHandsawGreenSapphire;
    public static Item itemHandsawSapphire;
    public static Item itemBrushDry;
    public static Item itemPaintCanEmpty;
    public static Item[] itemBrushPaint = new Item[16];
    public static ItemPartialCraft[] itemPaintCanPaint = new ItemPartialCraft[16];
    public static Item itemWoolCard;
    public static Item itemSeedBag;
    public static Enchantment enchantDisjunction;
    public static Enchantment enchantVorpal;
    public static final String blockTextureFile = "/eloraam/world/world1.png";
    public static final String itemTextureFile = "/eloraam/world/worlditems1.png";

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1)
    {
        MinecraftForge.EVENT_BUS.register(new WorldEvents());
    }

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        GameRegistry.registerWorldGenerator(new WorldGenHandler());
        this.setupOres();
        this.setupPlants();
        this.setupTools();
        this.setupMisc();
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    public void setupPlants()
    {
        blockPlants = new BlockCustomFlower(Config.getBlockID("blocks.world.plants.id"), 1);
        blockPlants.setUnlocalizedName("indigo");
        GameRegistry.registerBlock(blockPlants, ItemCustomFlower.class, "plants");
        MinecraftForge.addGrassPlant(blockPlants, 0, 10);
        GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.itemDyeIndigo, 2, 0), new Object[] {blockPlants});
        itemSeeds = new ItemCustomSeeds(Config.getItemID("items.world.seeds.id"));
        MinecraftForge.addGrassSeed(new ItemStack(itemSeeds.itemID, 1, 0), 5);
        blockCrops = new BlockCustomCrops(Config.getBlockID("blocks.world.crops.id"));
        blockLeaves = new BlockCustomLeaves(Config.getBlockID("blocks.world.leaves.id"));
        blockLeaves.setUnlocalizedName("rpleaves");
        GameRegistry.registerBlock(blockLeaves, "leaves");
        blockLogs = new BlockCustomLog(Config.getBlockID("blocks.world.log.id"));
        blockLogs.setUnlocalizedName("rplog");
        GameRegistry.registerBlock(blockLogs, "logs");
        MinecraftForge.setBlockHarvestLevel(blockLogs, "axe", 0);
        OreDictionary.registerOre("woodRubber", new ItemStack(blockLogs));
        GameRegistry.addRecipe(new ItemStack(Item.stick, 8), new Object[] {"W", 'W', blockLogs});
        FurnaceRecipes.smelting().addSmelting(blockLogs.blockID, 0, new ItemStack(Item.coal, 1, 1), 0.15F);
        CoverLib.addMaterial(53, 0, blockLogs, 0, "rplog", "Rubberwood");
    }

    public void setupOres()
    {
        blockStone = new BlockCustomStone(Config.getBlockID("blocks.world.stone.id"));
        blockStone.setUnlocalizedName("rpstone");
        GameRegistry.registerBlock(blockStone, ItemCustomStone.class, "stone");
        itemMarble = new ItemStack(blockStone, 0);
        MinecraftForge.setBlockHarvestLevel(blockStone, "pickaxe", 0);
        CoverLib.addMaterial(48, 1, blockStone, 0, "marble", "Marble");
        CoverLib.addMaterial(49, 1, blockStone, 1, "basalt", "Basalt");
        CoverLib.addMaterial(50, 1, blockStone, 2, "marbleBrick", "Marble Brick");
        CoverLib.addMaterial(51, 1, blockStone, 3, "basaltCobble", "Basalt Cobblestone");
        CoverLib.addMaterial(52, 1, blockStone, 4, "basaltBrick", "Basalt Brick");
        CoverLib.addMaterial(57, 1, blockStone, 5, "basaltCircle", "Chiseled Basalt Brick");
        CoverLib.addMaterial(58, 1, blockStone, 6, "basaltPaver", "Basalt Paver");
        blockOres = new BlockCustomOre(Config.getBlockID("blocks.world.ores.id"));
        blockOres.setUnlocalizedName("rpores");
        GameRegistry.registerBlock(blockOres, ItemCustomOre.class, "ores");
        itemOreRuby = new ItemStack(blockOres, 1, 0);
        itemOreGreenSapphire = new ItemStack(blockOres, 1, 1);
        itemOreSapphire = new ItemStack(blockOres, 1, 2);
        MinecraftForge.setBlockHarvestLevel(blockOres, 0, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockOres, 1, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockOres, 2, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockOres, 3, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(blockOres, 4, "pickaxe", 0);
        MinecraftForge.setBlockHarvestLevel(blockOres, 5, "pickaxe", 0);
        MinecraftForge.setBlockHarvestLevel(blockOres, 6, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockOres, 7, "pickaxe", 2);
        FurnaceRecipes.smelting().addSmelting(blockOres.blockID, 3, RedPowerBase.itemIngotSilver, 1.0F);
        FurnaceRecipes.smelting().addSmelting(blockOres.blockID, 4, RedPowerBase.itemIngotTin, 0.7F);
        FurnaceRecipes.smelting().addSmelting(blockOres.blockID, 5, RedPowerBase.itemIngotCopper, 0.7F);
        OreDictionary.registerOre("oreRuby", new ItemStack(blockOres, 1, 0));
        OreDictionary.registerOre("oreGreenSapphire", new ItemStack(blockOres, 1, 1));
        OreDictionary.registerOre("oreSapphire", new ItemStack(blockOres, 1, 2));
        OreDictionary.registerOre("oreSilver", new ItemStack(blockOres, 1, 3));
        OreDictionary.registerOre("oreTin", new ItemStack(blockOres, 1, 4));
        OreDictionary.registerOre("oreCopper", new ItemStack(blockOres, 1, 5));
        OreDictionary.registerOre("oreTungsten", new ItemStack(blockOres, 1, 6));
        OreDictionary.registerOre("oreNikolite", new ItemStack(blockOres, 1, 7));
        GameRegistry.addRecipe(new ItemStack(blockStone, 4, 2), new Object[] {"SS", "SS", 'S', new ItemStack(blockStone, 1, 0)});
        FurnaceRecipes.smelting().addSmelting(blockStone.blockID, 3, new ItemStack(blockStone, 1, 1), 0.2F);
        GameRegistry.addRecipe(new ItemStack(blockStone, 4, 4), new Object[] {"SS", "SS", 'S', new ItemStack(blockStone, 1, 1)});
        GameRegistry.addRecipe(new ItemStack(blockStone, 4, 5), new Object[] {"SS", "SS", 'S', new ItemStack(blockStone, 1, 4)});
        GameRegistry.addRecipe(new ItemStack(blockStone, 1, 6), new Object[] {"S", 'S', new ItemStack(blockStone, 1, 1)});
        blockStorage = new BlockStorage(Config.getBlockID("blocks.world.storage.id"));
        GameRegistry.registerBlock(blockStorage, ItemStorage.class, "orestorage");
        GameRegistry.addRecipe(new ItemStack(blockStorage, 1, 0), new Object[] {"GGG", "GGG", "GGG", 'G', RedPowerBase.itemRuby});
        GameRegistry.addRecipe(new ItemStack(blockStorage, 1, 1), new Object[] {"GGG", "GGG", "GGG", 'G', RedPowerBase.itemGreenSapphire});
        GameRegistry.addRecipe(new ItemStack(blockStorage, 1, 2), new Object[] {"GGG", "GGG", "GGG", 'G', RedPowerBase.itemSapphire});
        GameRegistry.addRecipe(new ItemStack(blockStorage, 1, 3), new Object[] {"GGG", "GGG", "GGG", 'G', RedPowerBase.itemIngotSilver});
        GameRegistry.addRecipe(new ItemStack(blockStorage, 1, 4), new Object[] {"GGG", "GGG", "GGG", 'G', RedPowerBase.itemIngotTin});
        GameRegistry.addRecipe(new ItemStack(blockStorage, 1, 5), new Object[] {"GGG", "GGG", "GGG", 'G', RedPowerBase.itemIngotCopper});
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemRuby, 9), new Object[] {"G", 'G', new ItemStack(blockStorage, 1, 0)});
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemGreenSapphire, 9), new Object[] {"G", 'G', new ItemStack(blockStorage, 1, 1)});
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemSapphire, 9), new Object[] {"G", 'G', new ItemStack(blockStorage, 1, 2)});
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemIngotSilver, 9), new Object[] {"G", 'G', new ItemStack(blockStorage, 1, 3)});
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemIngotTin, 9), new Object[] {"G", 'G', new ItemStack(blockStorage, 1, 4)});
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemIngotCopper, 9), new Object[] {"G", 'G', new ItemStack(blockStorage, 1, 5)});
        MinecraftForge.setBlockHarvestLevel(blockStorage, 0, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockStorage, 1, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockStorage, 2, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockStorage, 3, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockStorage, 4, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(blockStorage, 5, "pickaxe", 2);
        CoverLib.addMaterial(54, 2, blockStorage, 0, "rubyBlock", "Ruby Block");
        CoverLib.addMaterial(55, 2, blockStorage, 1, "greenSapphireBlock", "Green Sapphire Block");
        CoverLib.addMaterial(56, 2, blockStorage, 2, "sapphireBlock", "Sapphire Block");
        CoverLib.addMaterial(66, 2, blockStorage, 3, "silverBlock", "Silver Block");
        CoverLib.addMaterial(67, 2, blockStorage, 4, "tinBlock", "Tin Block");
        CoverLib.addMaterial(68, 2, blockStorage, 5, "copperBlock", "Copper Block");
    }

    public void setupTools()
    {
        toolMaterialRuby = EnumHelper.addToolMaterial("RUBY", 2, 500, 8.0F, 3, 12);
        toolMaterialGreenSapphire = EnumHelper.addToolMaterial("GREENSAPPHIRE", 2, 500, 8.0F, 3, 12);
        toolMaterialSapphire = EnumHelper.addToolMaterial("SAPPHIRE", 2, 500, 8.0F, 3, 12);
        itemPickaxeRuby = new ItemCustomPickaxe(Config.getItemID("items.world.pickaxeRuby.id"), toolMaterialRuby);
        itemPickaxeRuby.setUnlocalizedName("pickaxeRuby");
        itemPickaxeGreenSapphire = new ItemCustomPickaxe(Config.getItemID("items.world.pickaxeGreenSapphire.id"), toolMaterialGreenSapphire);
        itemPickaxeGreenSapphire.setUnlocalizedName("pickaxeGreenSapphire");
        itemPickaxeSapphire = new ItemCustomPickaxe(Config.getItemID("items.world.pickaxeSapphire.id"), toolMaterialSapphire);
        itemPickaxeSapphire.setUnlocalizedName("pickaxeSapphire");
        MinecraftForge.setToolClass(itemPickaxeRuby, "pickaxe", 2);
        MinecraftForge.setToolClass(itemPickaxeGreenSapphire, "pickaxe", 2);
        MinecraftForge.setToolClass(itemPickaxeSapphire, "pickaxe", 2);
        GameRegistry.addRecipe(new ItemStack(itemPickaxeRuby, 1), new Object[] {"GGG", " W ", " W ", 'G', RedPowerBase.itemRuby, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemPickaxeGreenSapphire, 1), new Object[] {"GGG", " W ", " W ", 'G', RedPowerBase.itemGreenSapphire, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemPickaxeSapphire, 1), new Object[] {"GGG", " W ", " W ", 'G', RedPowerBase.itemSapphire, 'W', Item.stick});
        itemShovelRuby = new ItemCustomShovel(Config.getItemID("items.world.shovelRuby.id"), toolMaterialRuby);
        itemShovelRuby.setUnlocalizedName("shovelRuby");
        itemShovelGreenSapphire = new ItemCustomShovel(Config.getItemID("items.world.shovelGreenSapphire.id"), toolMaterialGreenSapphire);
        itemShovelGreenSapphire.setUnlocalizedName("shovelGreenSapphire");
        itemShovelSapphire = new ItemCustomShovel(Config.getItemID("items.world.shovelSapphire.id"), toolMaterialSapphire);
        itemShovelSapphire.setUnlocalizedName("shovelSapphire");
        MinecraftForge.setToolClass(itemShovelRuby, "shovel", 2);
        MinecraftForge.setToolClass(itemShovelGreenSapphire, "shovel", 2);
        MinecraftForge.setToolClass(itemShovelSapphire, "shovel", 2);
        GameRegistry.addRecipe(new ItemStack(itemShovelRuby, 1), new Object[] {"G", "W", "W", 'G', RedPowerBase.itemRuby, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemShovelGreenSapphire, 1), new Object[] {"G", "W", "W", 'G', RedPowerBase.itemGreenSapphire, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemShovelSapphire, 1), new Object[] {"G", "W", "W", 'G', RedPowerBase.itemSapphire, 'W', Item.stick});
        itemAxeRuby = new ItemCustomAxe(Config.getItemID("items.world.axeRuby.id"), toolMaterialRuby);
        itemAxeRuby.setUnlocalizedName("axeRuby");
        itemAxeGreenSapphire = new ItemCustomAxe(Config.getItemID("items.world.axeGreenSapphire.id"), toolMaterialGreenSapphire);
        itemAxeGreenSapphire.setUnlocalizedName("axeGreenSapphire");
        itemAxeSapphire = new ItemCustomAxe(Config.getItemID("items.world.axeSapphire.id"), toolMaterialSapphire);
        itemAxeSapphire.setUnlocalizedName("axeSapphire");
        MinecraftForge.setToolClass(itemAxeRuby, "axe", 2);
        MinecraftForge.setToolClass(itemAxeGreenSapphire, "axe", 2);
        MinecraftForge.setToolClass(itemAxeSapphire, "axe", 2);
        GameRegistry.addRecipe(new ItemStack(itemAxeRuby, 1), new Object[] {"GG", "GW", " W", 'G', RedPowerBase.itemRuby, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemAxeGreenSapphire, 1), new Object[] {"GG", "GW", " W", 'G', RedPowerBase.itemGreenSapphire, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemAxeSapphire, 1), new Object[] {"GG", "GW", " W", 'G', RedPowerBase.itemSapphire, 'W', Item.stick});
        itemSwordRuby = new ItemCustomSword(Config.getItemID("items.world.swordRuby.id"), toolMaterialRuby);
        itemSwordRuby.setUnlocalizedName("swordRuby");
        itemSwordGreenSapphire = new ItemCustomSword(Config.getItemID("items.world.swordGreenSapphire.id"), toolMaterialGreenSapphire);
        itemSwordGreenSapphire.setUnlocalizedName("swordGreenSapphire");
        itemSwordSapphire = new ItemCustomSword(Config.getItemID("items.world.swordSapphire.id"), toolMaterialSapphire);
        itemSwordSapphire.setUnlocalizedName("swordSapphire");
        itemAthame = new ItemAthame(Config.getItemID("items.world.athame.id"));
        itemAthame.setUnlocalizedName("athame");
        MinecraftForge.setToolClass(itemSwordRuby, "sword", 2);
        MinecraftForge.setToolClass(itemSwordGreenSapphire, "sword", 2);
        MinecraftForge.setToolClass(itemSwordSapphire, "sword", 2);
        MinecraftForge.setToolClass(itemAthame, "sword", 0);
        CraftLib.addOreRecipe(new ItemStack(itemAthame, 1), new Object[] {"S", "W", 'S', "ingotSilver", 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSwordRuby, 1), new Object[] {"G", "G", "W", 'G', RedPowerBase.itemRuby, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSwordGreenSapphire, 1), new Object[] {"G", "G", "W", 'G', RedPowerBase.itemGreenSapphire, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSwordSapphire, 1), new Object[] {"G", "G", "W", 'G', RedPowerBase.itemSapphire, 'W', Item.stick});
        itemHoeRuby = new ItemCustomHoe(Config.getItemID("items.world.hoeRuby.id"), toolMaterialRuby);
        itemHoeRuby.setUnlocalizedName("hoeRuby");
        itemHoeRuby.setMaxDamage(500);
        itemHoeGreenSapphire = new ItemCustomHoe(Config.getItemID("items.world.hoeGreenSapphire.id"), toolMaterialGreenSapphire);
        itemHoeGreenSapphire.setUnlocalizedName("hoeGreenSapphire");
        itemHoeGreenSapphire.setMaxDamage(500);
        itemHoeSapphire = new ItemCustomHoe(Config.getItemID("items.world.hoeSapphire.id"), toolMaterialSapphire);
        itemHoeSapphire.setUnlocalizedName("hoeSapphire");
        itemHoeSapphire.setMaxDamage(500);
        MinecraftForge.setToolClass(itemHoeRuby, "hoe", 2);
        MinecraftForge.setToolClass(itemHoeGreenSapphire, "hoe", 2);
        MinecraftForge.setToolClass(itemHoeSapphire, "hoe", 2);
        GameRegistry.addRecipe(new ItemStack(itemHoeRuby, 1), new Object[] {"GG", " W", " W", 'G', RedPowerBase.itemRuby, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemHoeGreenSapphire, 1), new Object[] {"GG", " W", " W", 'G', RedPowerBase.itemGreenSapphire, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemHoeSapphire, 1), new Object[] {"GG", " W", " W", 'G', RedPowerBase.itemSapphire, 'W', Item.stick});
        itemSickleWood = new ItemSickle(Config.getItemID("items.world.sickleWood.id"), EnumToolMaterial.WOOD);
        itemSickleWood.setUnlocalizedName("sickleWood");
        itemSickleStone = new ItemSickle(Config.getItemID("items.world.sickleStone.id"), EnumToolMaterial.STONE);
        itemSickleStone.setUnlocalizedName("sickleStone");
        itemSickleIron = new ItemSickle(Config.getItemID("items.world.sickleIron.id"), EnumToolMaterial.IRON);
        itemSickleIron.setUnlocalizedName("sickleIron");
        itemSickleDiamond = new ItemSickle(Config.getItemID("items.world.sickleDiamond.id"), EnumToolMaterial.EMERALD);
        itemSickleDiamond.setUnlocalizedName("sickleDiamond");
        itemSickleGold = new ItemSickle(Config.getItemID("items.world.sickleGold.id"), EnumToolMaterial.GOLD);
        itemSickleGold.setUnlocalizedName("sickleGold");
        itemSickleRuby = new ItemSickle(Config.getItemID("items.world.sickleRuby.id"), toolMaterialRuby);
        itemSickleRuby.setUnlocalizedName("sickleRuby");
        itemSickleGreenSapphire = new ItemSickle(Config.getItemID("items.world.sickleGreenSapphire.id"), toolMaterialGreenSapphire);
        itemSickleGreenSapphire.setUnlocalizedName("sickleGreenSapphire");
        itemSickleSapphire = new ItemSickle(Config.getItemID("items.world.sickleSapphire.id"), toolMaterialSapphire);
        itemSickleSapphire.setUnlocalizedName("sickleSapphire");
        CraftLib.addOreRecipe(new ItemStack(itemSickleWood, 1), new Object[] {" I ", "  I", "WI ", 'I', "plankWood", 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSickleStone, 1), new Object[] {" I ", "  I", "WI ", 'I', Block.cobblestone, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSickleIron, 1), new Object[] {" I ", "  I", "WI ", 'I', Item.ingotIron, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSickleDiamond, 1), new Object[] {" I ", "  I", "WI ", 'I', Item.diamond, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSickleGold, 1), new Object[] {" I ", "  I", "WI ", 'I', Item.ingotGold, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSickleRuby, 1), new Object[] {" I ", "  I", "WI ", 'I', RedPowerBase.itemRuby, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSickleGreenSapphire, 1), new Object[] {" I ", "  I", "WI ", 'I', RedPowerBase.itemGreenSapphire, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemSickleSapphire, 1), new Object[] {" I ", "  I", "WI ", 'I', RedPowerBase.itemSapphire, 'W', Item.stick});
        itemHandsawRuby = new ItemHandsaw(Config.getItemID("items.world.handsawRuby.id"), 1);
        itemHandsawGreenSapphire = new ItemHandsaw(Config.getItemID("items.world.handsawGreenSapphire.id"), 1);
        itemHandsawSapphire = new ItemHandsaw(Config.getItemID("items.world.handsawSapphire.id"), 1);
        itemHandsawRuby.setUnlocalizedName("handsawRuby");
        itemHandsawGreenSapphire.setUnlocalizedName("handsawGreenSapphire");
        itemHandsawSapphire.setUnlocalizedName("handsawSapphire");
        itemHandsawRuby.setMaxDamage(640);
        itemHandsawGreenSapphire.setMaxDamage(640);
        itemHandsawSapphire.setMaxDamage(640);
        GameRegistry.addRecipe(new ItemStack(itemHandsawRuby, 1), new Object[] {"WWW", " II", " GG", 'I', Item.ingotIron, 'G', RedPowerBase.itemRuby, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemHandsawGreenSapphire, 1), new Object[] {"WWW", " II", " GG", 'I', Item.ingotIron, 'G', RedPowerBase.itemGreenSapphire, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemHandsawSapphire, 1), new Object[] {"WWW", " II", " GG", 'I', Item.ingotIron, 'G', RedPowerBase.itemSapphire, 'W', Item.stick});
        itemWoolCard = new ItemWoolCard(Config.getItemID("items.world.woolcard.id"));
        itemWoolCard.setUnlocalizedName("woolcard");
        itemWoolCard.setMaxDamage(63);
        CraftLib.addOreRecipe(new ItemStack(itemWoolCard, 1), new Object[] {"W", "P", "S", 'W', RedPowerBase.itemFineIron, 'P', "plankWood", 'S', Item.stick});
        GameRegistry.addShapelessRecipe(new ItemStack(Item.silk, 4), new Object[] {new ItemStack(itemWoolCard, 1, -1), new ItemStack(Block.cloth, 1, -1)});
        itemBrushDry = new ItemTextured(Config.getItemID("items.world.paintbrush.dry.id"), 22, "/eloraam/base/items1.png");
        itemBrushDry.setUnlocalizedName("paintbrush.dry");
        GameRegistry.addRecipe(new ItemStack(itemBrushDry), new Object[] {"W ", " S", 'S', Item.stick, 'W', Block.cloth});
        itemPaintCanEmpty = new ItemTextured(Config.getItemID("items.world.paintcan.empty.id"), 23, "/eloraam/base/items1.png");
        itemPaintCanEmpty.setUnlocalizedName("paintcan.empty");
        GameRegistry.addRecipe(new ItemStack(itemPaintCanEmpty, 3), new Object[] {"T T", "T T", "TTT", 'T', RedPowerBase.itemTinplate});

        for (int var1 = 0; var1 < 16; ++var1)
        {
            itemPaintCanPaint[var1] = new ItemPaintCan(Config.getItemID("items.world.paintcan." + CoreLib.rawColorNames[var1] + ".id"), var1);
            itemPaintCanPaint[var1].setUnlocalizedName("paintcan." + CoreLib.rawColorNames[var1]);
            itemPaintCanPaint[var1].setEmptyItem(new ItemStack(itemPaintCanEmpty));
            Config.addName("item.paintcan." + CoreLib.rawColorNames[var1] + ".name", CoreLib.enColorNames[var1] + " Paint");
            GameRegistry.addShapelessRecipe(new ItemStack(itemPaintCanPaint[var1]), new Object[] {itemPaintCanEmpty, new ItemStack(Item.dyePowder, 1, 15 - var1), new ItemStack(itemSeeds, 1, 0), new ItemStack(itemSeeds, 1, 0)});
            itemBrushPaint[var1] = new ItemPaintBrush(Config.getItemID("items.world.paintbrush." + CoreLib.rawColorNames[var1] + ".id"), var1);
            itemBrushPaint[var1].setUnlocalizedName("paintbrush." + CoreLib.rawColorNames[var1]);
            Config.addName("item.paintbrush." + CoreLib.rawColorNames[var1] + ".name", CoreLib.enColorNames[var1] + " Paint Brush");
            GameRegistry.addShapelessRecipe(new ItemStack(itemBrushPaint[var1]), new Object[] {new ItemStack(itemPaintCanPaint[var1], 1, -1), itemBrushDry});
        }

        CraftLib.addShapelessOreRecipe(new ItemStack(itemPaintCanPaint[11]), new Object[] {itemPaintCanEmpty, "dyeBlue", new ItemStack(itemSeeds, 1, 0), new ItemStack(itemSeeds, 1, 0)});
        itemSeedBag = new ItemSeedBag(Config.getItemID("items.world.seedbag.id"));
        GameRegistry.addRecipe(new ItemStack(itemSeedBag, 1, 0), new Object[] {" S ", "C C", "CCC", 'S', Item.silk, 'C', RedPowerBase.itemCanvas});
    }

    void setupMisc()
    {
        if (Config.getInt("settings.world.tweaks.spreadmoss") > 0)
        {
            int var1 = Block.cobblestoneMossy.blockID;
            Block.blocksList[var1] = null;
            new BlockCobbleMossifier(var1);
            var1 = Block.stoneBrick.blockID;
            Block.blocksList[var1] = null;
            new BlockBrickMossifier(var1);
        }

        if (Config.getInt("settings.world.tweaks.craftcircle") > 0)
        {
            GameRegistry.addRecipe(new ItemStack(Block.stoneBrick, 4, 3), new Object[] {"BB", "BB", 'B', new ItemStack(Block.stoneBrick, 1, 0)});
        }

        if (Config.getInt("settings.world.tweaks.unbricks") > 0)
        {
            GameRegistry.addShapelessRecipe(new ItemStack(Item.brick, 4, 0), new Object[] {new ItemStack(Block.brick, 1, 0)});
        }

        enchantDisjunction = new EnchantmentDisjunction(Config.getInt("enchant.disjunction.id"), 10);
        enchantVorpal = new EnchantmentVorpal(Config.getInt("enchant.vorpal.id"), 10);
    }
}
