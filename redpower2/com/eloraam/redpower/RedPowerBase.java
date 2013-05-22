package com.eloraam.redpower;

import com.eloraam.redpower.base.BaseProxy;
import com.eloraam.redpower.base.BlockAppliance;
import com.eloraam.redpower.base.BlockMicro;
import com.eloraam.redpower.base.ItemBag;
import com.eloraam.redpower.base.ItemDrawplate;
import com.eloraam.redpower.base.ItemDyeIndigo;
import com.eloraam.redpower.base.ItemHandsaw;
import com.eloraam.redpower.base.ItemMicro;
import com.eloraam.redpower.base.ItemPlan;
import com.eloraam.redpower.base.ItemScrewdriver;
import com.eloraam.redpower.base.RecipeBag;
import com.eloraam.redpower.base.TileAdvBench;
import com.eloraam.redpower.base.TileAlloyFurnace;
import com.eloraam.redpower.core.AchieveLib;
import com.eloraam.redpower.core.BlockMultiblock;
import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.CraftLib;
import com.eloraam.redpower.core.ItemExtended;
import com.eloraam.redpower.core.ItemParts;
import com.eloraam.redpower.core.ItemTextured;
import com.eloraam.redpower.core.OreStack;
import com.eloraam.redpower.core.PipeLib;
import com.eloraam.redpower.core.TileCovered;
import com.eloraam.redpower.core.TileMultiblock;
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
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.oredict.OreDictionary;

@Mod(
        modid = "RedPowerBase",
        name = "RedPower Base",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b",
        dependencies = "required-after:RedPowerCore"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerBase
{
    @Mod.Instance("RedPowerBase")
    public static RedPowerBase instance;
    @SidedProxy(
            clientSide = "com.eloraam.redpower.base.BaseProxyClient",
            serverSide = "com.eloraam.redpower.base.BaseProxy"
    )
    public static BaseProxy proxy;
    public static BlockAppliance blockAppliance;
    public static Item itemHandsawIron;
    public static Item itemHandsawDiamond;
    public static ItemParts itemLumar;
    public static ItemParts itemResource;
    public static ItemStack itemRuby;
    public static ItemStack itemGreenSapphire;
    public static ItemStack itemSapphire;
    public static ItemStack itemIngotSilver;
    public static ItemStack itemIngotTin;
    public static ItemStack itemIngotCopper;
    public static ItemStack itemNikolite;
    public static ItemParts itemAlloy;
    public static ItemStack itemIngotRed;
    public static ItemStack itemIngotBlue;
    public static ItemStack itemIngotBrass;
    public static ItemStack itemBouleSilicon;
    public static ItemStack itemWaferSilicon;
    public static ItemStack itemWaferBlue;
    public static ItemStack itemWaferRed;
    public static ItemStack itemTinplate;
    public static ItemStack itemFineCopper;
    public static ItemStack itemFineIron;
    public static ItemStack itemCopperCoil;
    public static ItemStack itemMotor;
    public static ItemStack itemCanvas;
    public static ItemParts itemNugget;
    public static ItemStack itemNuggetIron;
    public static ItemStack itemNuggetSilver;
    public static ItemStack itemNuggetTin;
    public static ItemStack itemNuggetCopper;
    public static Item itemDyeIndigo;
    public static BlockMicro blockMicro;
    public static BlockMultiblock blockMultiblock;
    public static ItemScrewdriver itemScrewdriver;
    public static Item itemDrawplateDiamond;
    public static Item itemPlanBlank;
    public static Item itemPlanFull;
    public static Item itemBag;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1) {}

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        initBaseItems();
        initAlloys();
        initIndigo();
        initMicroblocks();
        initCoverMaterials();
        initFluids();
        initBlocks();
        initAchievements();
        CraftingManager.getInstance().getRecipeList().add(new RecipeBag());
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1) {}

    public static void initBaseItems()
    {
        itemLumar = new ItemParts(Config.getItemID("items.base.lumar.id"), "/eloraam/base/items1.png");
        itemLumar.setCreativeTab(CreativeTabs.tabMaterials);
        int var0;

        for (var0 = 0; var0 < 16; ++var0)
        {
            itemLumar.addItem(var0, 32 + var0, "item.rplumar." + CoreLib.rawColorNames[var0]);
            Config.addName("item.rplumar." + CoreLib.rawColorNames[var0] + ".name", CoreLib.enColorNames[var0] + " Lumar");
            ItemStack var1 = new ItemStack(Item.dyePowder, 1, 15 - var0);
            GameRegistry.addShapelessRecipe(new ItemStack(itemLumar, 2, var0), new Object[] {Item.redstone, var1, var1, Item.lightStoneDust});
        }

        itemResource = new ItemParts(Config.getItemID("items.base.resource.id"), "/eloraam/base/items1.png");
        itemAlloy = new ItemParts(Config.getItemID("items.base.alloy.id"), "/eloraam/base/items1.png");
        itemResource.setCreativeTab(CreativeTabs.tabMaterials);
        itemAlloy.setCreativeTab(CreativeTabs.tabMaterials);
        itemResource.addItem(0, 48, "item.ruby");
        itemResource.addItem(1, 49, "item.greenSapphire");
        itemResource.addItem(2, 50, "item.sapphire");
        itemResource.addItem(3, 51, "item.ingotSilver");
        itemResource.addItem(4, 52, "item.ingotTin");
        itemResource.addItem(5, 53, "item.ingotCopper");
        itemResource.addItem(6, 54, "item.nikolite");
        itemAlloy.addItem(0, 64, "item.ingotRed");
        itemAlloy.addItem(1, 65, "item.ingotBlue");
        itemAlloy.addItem(2, 66, "item.ingotBrass");
        itemAlloy.addItem(3, 67, "item.bouleSilicon");
        itemAlloy.addItem(4, 68, "item.waferSilicon");
        itemAlloy.addItem(5, 69, "item.waferBlue");
        itemAlloy.addItem(6, 70, "item.waferRed");
        itemAlloy.addItem(7, 71, "item.tinplate");
        itemAlloy.addItem(8, 72, "item.finecopper");
        itemAlloy.addItem(9, 73, "item.fineiron");
        itemAlloy.addItem(10, 74, "item.coppercoil");
        itemAlloy.addItem(11, 75, "item.btmotor");
        itemAlloy.addItem(12, 76, "item.rpcanvas");
        itemRuby = new ItemStack(itemResource, 1, 0);
        itemGreenSapphire = new ItemStack(itemResource, 1, 1);
        itemSapphire = new ItemStack(itemResource, 1, 2);
        itemIngotSilver = new ItemStack(itemResource, 1, 3);
        itemIngotTin = new ItemStack(itemResource, 1, 4);
        itemIngotCopper = new ItemStack(itemResource, 1, 5);
        itemNikolite = new ItemStack(itemResource, 1, 6);
        itemIngotRed = new ItemStack(itemAlloy, 1, 0);
        itemIngotBlue = new ItemStack(itemAlloy, 1, 1);
        itemIngotBrass = new ItemStack(itemAlloy, 1, 2);
        itemBouleSilicon = new ItemStack(itemAlloy, 1, 3);
        itemWaferSilicon = new ItemStack(itemAlloy, 1, 4);
        itemWaferBlue = new ItemStack(itemAlloy, 1, 5);
        itemWaferRed = new ItemStack(itemAlloy, 1, 6);
        itemTinplate = new ItemStack(itemAlloy, 1, 7);
        itemFineCopper = new ItemStack(itemAlloy, 1, 8);
        itemFineIron = new ItemStack(itemAlloy, 1, 9);
        itemCopperCoil = new ItemStack(itemAlloy, 1, 10);
        itemMotor = new ItemStack(itemAlloy, 1, 11);
        itemCanvas = new ItemStack(itemAlloy, 1, 12);
        OreDictionary.registerOre("gemRuby", itemRuby);
        OreDictionary.registerOre("gemGreenSapphire", itemGreenSapphire);
        OreDictionary.registerOre("gemSapphire", itemSapphire);
        OreDictionary.registerOre("ingotTin", itemIngotTin);
        OreDictionary.registerOre("ingotCopper", itemIngotCopper);
        OreDictionary.registerOre("ingotSilver", itemIngotSilver);
        OreDictionary.registerOre("ingotBrass", itemIngotBrass);
        OreDictionary.registerOre("dustNikolite", itemNikolite);
        itemNugget = new ItemParts(Config.getItemID("items.base.nuggets.id"), "/eloraam/base/items1.png");
        itemNugget.setCreativeTab(CreativeTabs.tabMaterials);
        itemNugget.addItem(0, 160, "item.nuggetIron");
        itemNugget.addItem(1, 161, "item.nuggetSilver");
        itemNugget.addItem(2, 162, "item.nuggetTin");
        itemNugget.addItem(3, 163, "item.nuggetCopper");
        itemNuggetIron = new ItemStack(itemNugget, 1, 0);
        itemNuggetSilver = new ItemStack(itemNugget, 1, 1);
        itemNuggetTin = new ItemStack(itemNugget, 1, 2);
        itemNuggetCopper = new ItemStack(itemNugget, 1, 3);
        OreDictionary.registerOre("nuggetIron", itemNuggetIron);
        OreDictionary.registerOre("nuggetSilver", itemNuggetSilver);
        OreDictionary.registerOre("nuggetTin", itemNuggetTin);
        OreDictionary.registerOre("nuggetCopper", itemNuggetCopper);
        itemDrawplateDiamond = new ItemDrawplate(Config.getItemID("items.base.drawplateDiamond.id"));
        itemDrawplateDiamond.setUnlocalizedName("drawplateDiamond").setMaxDamage(255);
        itemBag = new ItemBag(Config.getItemID("items.base.bag.id"));
        GameRegistry.addRecipe(new ItemStack(itemBag, 1, 0), new Object[] {"CCC", "C C", "CCC", 'C', itemCanvas});

        for (var0 = 1; var0 < 16; ++var0)
        {
            GameRegistry.addRecipe(new ItemStack(itemBag, 1, var0), new Object[] {"CCC", "CDC", "CCC", 'C', itemCanvas, 'D', new ItemStack(Item.dyePowder, 1, 15 - var0)});
        }
    }

    public static void initIndigo()
    {
        itemDyeIndigo = new ItemDyeIndigo(Config.getItemID("items.base.dyeIndigo.id"));
        OreDictionary.registerOre("dyeBlue", new ItemStack(itemDyeIndigo));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.cloth.blockID, 1, 11), new Object[] {itemDyeIndigo, Block.cloth});
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 12), new Object[] {itemDyeIndigo, new ItemStack(Item.dyePowder, 1, 15)});
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 6), new Object[] {itemDyeIndigo, new ItemStack(Item.dyePowder, 1, 2)});
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 5), new Object[] {itemDyeIndigo, new ItemStack(Item.dyePowder, 1, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 3, 13), new Object[] {itemDyeIndigo, new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 9)});
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 4, 13), new Object[] {itemDyeIndigo, new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 15)});
        CraftLib.addShapelessOreRecipe(new ItemStack(itemLumar, 2, 11), new Object[] {Item.redstone, "dyeBlue", "dyeBlue", Item.lightStoneDust});
        CraftLib.addOreRecipe(new ItemStack(itemBag, 1, 11), new Object[] {"CCC", "CDC", "CCC", 'C', itemCanvas, 'D', "dyeBlue"});
        itemPlanBlank = new ItemTextured(Config.getItemID("items.base.planBlank.id"), 81, "/eloraam/base/items1.png");
        itemPlanBlank.setUnlocalizedName("planBlank");
        itemPlanBlank.setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.addShapelessRecipe(new ItemStack(itemPlanBlank), new Object[] {Item.paper, itemDyeIndigo});
        itemPlanFull = new ItemPlan(Config.getItemID("items.base.planFull.id"));
    }

    public static void initAlloys()
    {
        CraftLib.addAlloyResult(itemIngotRed, new Object[] {new ItemStack(Item.redstone, 4), new ItemStack(Item.ingotIron, 1)});
        CraftLib.addAlloyResult(itemIngotRed, new Object[] {new ItemStack(Item.redstone, 4), new OreStack("ingotCopper")});
        CraftLib.addAlloyResult(CoreLib.copyStack(itemIngotBrass, 4), new Object[] {new OreStack("ingotTin"), new OreStack("ingotCopper", 3)});
        CraftLib.addAlloyResult(CoreLib.copyStack(itemTinplate, 4), new Object[] {new OreStack("ingotTin"), new ItemStack(Item.ingotIron, 2)});
        CraftLib.addAlloyResult(itemIngotBlue, new Object[] {new OreStack("ingotSilver"), new OreStack("dustNikolite", 4)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 3), new Object[] {new ItemStack(Block.rail, 8)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 3), new Object[] {new ItemStack(Item.bucketEmpty, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 5), new Object[] {new ItemStack(Item.minecartEmpty, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 6), new Object[] {new ItemStack(Item.doorIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 3), new Object[] {new ItemStack(Block.fenceIron, 8)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 31), new Object[] {new ItemStack(Block.anvil, 1, 0)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 31), new Object[] {new ItemStack(Block.anvil, 1, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 31), new Object[] {new ItemStack(Block.anvil, 1, 2)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 2), new Object[] {new ItemStack(Item.swordIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 3), new Object[] {new ItemStack(Item.pickaxeIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 3), new Object[] {new ItemStack(Item.axeIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 1), new Object[] {new ItemStack(Item.shovelIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 2), new Object[] {new ItemStack(Item.hoeIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 2), new Object[] {new ItemStack(Item.swordGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 3), new Object[] {new ItemStack(Item.pickaxeGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 3), new Object[] {new ItemStack(Item.axeGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 1), new Object[] {new ItemStack(Item.shovelGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 2), new Object[] {new ItemStack(Item.hoeGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 5), new Object[] {new ItemStack(Item.helmetIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 8), new Object[] {new ItemStack(Item.plateIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 7), new Object[] {new ItemStack(Item.legsIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 4), new Object[] {new ItemStack(Item.bootsIron, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 5), new Object[] {new ItemStack(Item.helmetGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 8), new Object[] {new ItemStack(Item.plateGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 7), new Object[] {new ItemStack(Item.legsGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 4), new Object[] {new ItemStack(Item.bootsGold, 1)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotGold, 1), new Object[] {new ItemStack(Item.goldNugget, 9)});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 1), new Object[] {CoreLib.copyStack(itemNuggetIron, 9)});
        CraftLib.addAlloyResult(itemIngotSilver, new Object[] {CoreLib.copyStack(itemNuggetSilver, 9)});
        CraftLib.addAlloyResult(itemIngotCopper, new Object[] {CoreLib.copyStack(itemNuggetCopper, 9)});
        CraftLib.addAlloyResult(itemIngotTin, new Object[] {CoreLib.copyStack(itemNuggetTin, 9)});
        CraftLib.addAlloyResult(itemIngotCopper, new Object[] {itemFineCopper});
        CraftLib.addAlloyResult(new ItemStack(Item.ingotIron, 1), new Object[] {itemFineIron});
        CraftLib.addAlloyResult(itemBouleSilicon, new Object[] {new ItemStack(Item.coal, 8, 0), new ItemStack(Block.sand, 8)});
        CraftLib.addAlloyResult(itemBouleSilicon, new Object[] {new ItemStack(Item.coal, 8, 1), new ItemStack(Block.sand, 8)});
        CraftLib.addAlloyResult(itemWaferBlue, new Object[] {CoreLib.copyStack(itemWaferSilicon, 1), new OreStack("dustNikolite", 4)});
        CraftLib.addAlloyResult(itemWaferRed, new Object[] {CoreLib.copyStack(itemWaferSilicon, 1), new ItemStack(Item.redstone, 4)});
    }

    public static void initMicroblocks()
    {
        blockMicro = new BlockMicro(Config.getBlockID("blocks.base.microblock.id"));
        blockMicro.setUnlocalizedName("rpwire");
        GameRegistry.registerBlock(blockMicro, ItemMicro.class, "micro");
        blockMicro.addTileEntityMapping(0, TileCovered.class);
        CoverLib.blockCoverPlate = blockMicro;
    }

    public static void initFluids()
    {
        PipeLib.registerFluids();
        PipeLib.registerVanillaFluid(Block.waterStill.blockID, Block.waterMoving.blockID);
        PipeLib.registerVanillaFluid(Block.lavaStill.blockID, Block.lavaMoving.blockID);
    }

    public static void initCoverMaterials()
    {
        CoverLib.addMaterial(0, 1, Block.cobblestone, "cobble", "Cobblestone");
        CoverLib.addMaterial(1, 1, Block.stone, "stone", "Stone");
        CoverLib.addMaterial(2, 0, Block.planks, "planks", "Wooden Plank");
        CoverLib.addMaterial(3, 1, Block.sandStone, "sandstone", "Sandstone");
        CoverLib.addMaterial(4, 1, Block.cobblestoneMossy, "moss", "Moss Stone");
        CoverLib.addMaterial(5, 1, Block.brick, "brick", "Brick");
        CoverLib.addMaterial(6, 2, Block.obsidian, "obsidian", "Obsidian");
        CoverLib.addMaterial(7, 1, true, Block.glass, "glass", "Glass");
        CoverLib.addMaterial(8, 0, Block.dirt, "dirt", "Dirt");
        CoverLib.addMaterial(9, 0, Block.blockClay, "clay", "Clay");
        CoverLib.addMaterial(10, 0, Block.bookShelf, "books", "Bookshelf");
        CoverLib.addMaterial(11, 0, Block.blocksList[87], "netherrack", "Netherrack");
        CoverLib.addMaterial(12, 0, Block.wood, 0, "wood", "Oak Wood");
        CoverLib.addMaterial(13, 0, Block.wood, 1, "wood1", "Spruce Wood");
        CoverLib.addMaterial(14, 0, Block.wood, 2, "wood2", "Birch Wood");
        CoverLib.addMaterial(15, 0, Block.slowSand, "soul", "Soul Sand");
        CoverLib.addMaterial(16, 1, Block.stoneSingleSlab, "slab", "Polished Stone");
        CoverLib.addMaterial(17, 1, Block.blockIron, "iron", "Iron");
        CoverLib.addMaterial(18, 1, Block.blockGold, "gold", "Gold");
        CoverLib.addMaterial(19, 2, Block.blockDiamond, "diamond", "Diamond");
        CoverLib.addMaterial(20, 1, Block.blockLapis, "lapis", "Lapis Lazuli");
        CoverLib.addMaterial(21, 0, Block.blockSnow, "snow", "Snow");
        CoverLib.addMaterial(22, 0, Block.pumpkin, "pumpkin", "Pumpkin");
        CoverLib.addMaterial(23, 1, Block.stoneBrick, 0, "stonebrick", "Stone Brick");
        CoverLib.addMaterial(24, 1, Block.stoneBrick, 1, "stonebrick1", "Stone Brick");
        CoverLib.addMaterial(25, 1, Block.stoneBrick, 2, "stonebrick2", "Stone Brick");
        CoverLib.addMaterial(26, 1, Block.netherBrick, "netherbrick", "Nether Brick");
        CoverLib.addMaterial(27, 1, Block.stoneBrick, 3, "stonebrick3", "Stone Brick");
        CoverLib.addMaterial(28, 0, Block.planks, 1, "planks1", "Wooden Plank");
        CoverLib.addMaterial(29, 0, Block.planks, 2, "planks2", "Wooden Plank");
        CoverLib.addMaterial(30, 0, Block.planks, 3, "planks3", "Wooden Plank");
        CoverLib.addMaterial(31, 1, Block.sandStone, 1, "sandstone1", "Sandstone");
        CoverLib.addMaterial(64, 1, Block.sandStone, 2, "sandstone2", "Sandstone");
        CoverLib.addMaterial(65, 0, Block.wood, 3, "wood3", "Jungle Wood");

        for (int var0 = 0; var0 < 16; ++var0)
        {
            CoverLib.addMaterial(32 + var0, 0, Block.cloth, var0, "wool." + CoreLib.rawColorNames[var0], CoreLib.enColorNames[var0] + " Wool");
        }
    }

    public static void initAchievements()
    {
        AchieveLib.registerAchievement(117027, "rpMakeAlloy", 0, 0, new ItemStack(blockAppliance, 1, 0), AchievementList.buildFurnace);
        AchieveLib.registerAchievement(117028, "rpMakeSaw", 4, 0, new ItemStack(itemHandsawDiamond), AchievementList.diamonds);
        AchieveLib.registerAchievement(117029, "rpIngotRed", 2, 2, itemIngotRed, "rpMakeAlloy");
        AchieveLib.registerAchievement(117030, "rpIngotBlue", 2, 4, itemIngotBlue, "rpMakeAlloy");
        AchieveLib.registerAchievement(117031, "rpIngotBrass", 2, 6, itemIngotBrass, "rpMakeAlloy");
        AchieveLib.registerAchievement(117032, "rpAdvBench", -2, 0, new ItemStack(blockAppliance, 1, 3), AchievementList.buildWorkBench);
        AchieveLib.addCraftingAchievement(new ItemStack(blockAppliance, 1, 0), "rpMakeAlloy");
        AchieveLib.addCraftingAchievement(new ItemStack(blockAppliance, 1, 3), "rpAdvBench");
        AchieveLib.addCraftingAchievement(new ItemStack(itemHandsawDiamond), "rpMakeSaw");
        AchieveLib.addAlloyAchievement(itemIngotRed, "rpIngotRed");
        AchieveLib.addAlloyAchievement(itemIngotBlue, "rpIngotBlue");
        AchieveLib.addAlloyAchievement(itemIngotBrass, "rpIngotBrass");
        AchievementPage.registerAchievementPage(AchieveLib.achievepage);
    }

    public static void initBlocks()
    {
        blockMultiblock = new BlockMultiblock(Config.getBlockID("blocks.base.multiblock.id"));
        GameRegistry.registerBlock(blockMultiblock, "multi");
        GameRegistry.registerTileEntity(TileMultiblock.class, "RPMulti");
        blockAppliance = new BlockAppliance(Config.getBlockID("blocks.base.appliance.id"));
        GameRegistry.registerBlock(blockAppliance, ItemExtended.class, "appliance");
        GameRegistry.registerTileEntity(TileAlloyFurnace.class, "RPAFurnace");
        blockAppliance.addTileEntityMapping(0, TileAlloyFurnace.class);
        blockAppliance.setItemName(0, "rpafurnace");
        GameRegistry.addRecipe(new ItemStack(blockAppliance, 1, 0), new Object[] {"BBB", "B B", "BBB", 'B', Block.brick});
        GameRegistry.registerTileEntity(TileAdvBench.class, "RPAdvBench");
        blockAppliance.addTileEntityMapping(3, TileAdvBench.class);
        blockAppliance.setItemName(3, "rpabench");
        CraftLib.addOreRecipe(new ItemStack(blockAppliance, 1, 3), new Object[] {"SSS", "WTW", "WCW", 'S', Block.stone, 'W', "plankWood", 'T', Block.workbench, 'C', Block.chest});
        itemHandsawIron = new ItemHandsaw(Config.getItemID("items.base.handsawIron.id"), 0);
        itemHandsawDiamond = new ItemHandsaw(Config.getItemID("items.base.handsawDiamond.id"), 2);
        itemHandsawIron.setUnlocalizedName("handsawIron");
        itemHandsawIron.setMaxDamage(320);
        itemHandsawDiamond.setUnlocalizedName("handsawDiamond");
        itemHandsawDiamond.setMaxDamage(1280);
        GameRegistry.addRecipe(new ItemStack(itemHandsawIron, 1), new Object[] {"WWW", " II", " II", 'I', Item.ingotIron, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemHandsawDiamond, 1), new Object[] {"WWW", " II", " DD", 'I', Item.ingotIron, 'D', Item.diamond, 'W', Item.stick});
        GameRegistry.addShapelessRecipe(CoreLib.copyStack(itemWaferSilicon, 16), new Object[] {itemBouleSilicon, new ItemStack(itemHandsawDiamond, 1, -1)});
        itemScrewdriver = new ItemScrewdriver(Config.getItemID("items.base.screwdriver.id"));
        itemScrewdriver.setUnlocalizedName("screwdriver");
        GameRegistry.addRecipe(new ItemStack(itemScrewdriver, 1), new Object[] {"I ", " W", 'I', Item.ingotIron, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(itemDrawplateDiamond, 1), new Object[] {" I ", "IDI", " I ", 'I', new ItemStack(blockMicro, 1, 5649), 'D', new ItemStack(blockMicro, 1, 4115)});
        GameRegistry.addShapelessRecipe(itemFineIron, new Object[] {Item.ingotIron, new ItemStack(itemDrawplateDiamond, 1, -1)});
        CraftLib.addShapelessOreRecipe(itemFineCopper, new Object[] {"ingotCopper", new ItemStack(itemDrawplateDiamond, 1, -1)});
        GameRegistry.addRecipe(CoreLib.copyStack(itemNuggetIron, 9), new Object[] {"I", 'I', Item.ingotIron});
        CraftLib.addOreRecipe(CoreLib.copyStack(itemNuggetCopper, 9), new Object[] {"I", 'I', "ingotCopper"});
        CraftLib.addOreRecipe(CoreLib.copyStack(itemNuggetTin, 9), new Object[] {"I", 'I', "ingotTin"});
        CraftLib.addOreRecipe(CoreLib.copyStack(itemNuggetSilver, 9), new Object[] {"I", 'I', "ingotSilver"});
        GameRegistry.addRecipe(new ItemStack(Item.ingotIron, 1, 0), new Object[] {"III", "III", "III", 'I', itemNuggetIron});
        GameRegistry.addRecipe(itemIngotSilver, new Object[] {"III", "III", "III", 'I', itemNuggetSilver});
        GameRegistry.addRecipe(itemIngotTin, new Object[] {"III", "III", "III", 'I', itemNuggetTin});
        GameRegistry.addRecipe(itemIngotCopper, new Object[] {"III", "III", "III", 'I', itemNuggetCopper});
        GameRegistry.addRecipe(itemCanvas, new Object[] {"SSS", "SWS", "SSS", 'S', Item.silk, 'W', Item.stick});
        GameRegistry.addRecipe(new ItemStack(Item.diamond, 2), new Object[] {"D", 'D', new ItemStack(blockMicro, 1, 4115)});
        GameRegistry.addRecipe(new ItemStack(Item.diamond, 1), new Object[] {"D", 'D', new ItemStack(blockMicro, 1, 19)});
        GameRegistry.addRecipe(new ItemStack(Item.ingotIron, 2), new Object[] {"I", 'I', new ItemStack(blockMicro, 1, 4113)});
        GameRegistry.addRecipe(new ItemStack(Item.ingotIron, 1), new Object[] {"I", 'I', new ItemStack(blockMicro, 1, 17)});
    }
}
