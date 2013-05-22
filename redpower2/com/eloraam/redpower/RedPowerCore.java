package com.eloraam.redpower;

import com.eloraam.redpower.core.Config;
import com.eloraam.redpower.core.CoreEvents;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoreProxy;
import com.eloraam.redpower.core.CoverRecipe;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.Packet212GuiEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import java.io.File;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

@Mod(
        modid = "RedPowerCore",
        name = "RedPower Core",
        version = "2.0pr6",
        certificateFingerprint = "28f7f8a775e597088f3a418ea29290b6a1d23c7b"
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false
)
public class RedPowerCore
{
    @Mod.Instance("RedPowerCore")
    public static RedPowerCore instance;
    public static int customBlockModel = -1;
    public static int nullBlockModel = -1;
    public static final int idLogic = 1;
    public static final int idTimer = 2;
    public static final int idSequencer = 3;
    public static final int idCounter = 4;
    public static final int idWiring = 5;
    public static final int idArray = 6;
    public static final int idMachine = 7;
    public static final int idMachinePanel = 8;
    public static final int idFrame = 9;
    public static final int idItemUpdate = 10;
    public static final int idPipeUpdate = 11;
    public static final int idLighting = 11;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent var1)
    {
        Config.loadConfig();
        CoreLib.readOres();
        MinecraftForge.EVENT_BUS.register(new CoreEvents());
    }

    @Mod.Init
    public void load(FMLInitializationEvent var1)
    {
        Packet.addIdClassMapping(211, true, true, Packet211TileDesc.class);
        Packet.addIdClassMapping(212, true, true, Packet212GuiEvent.class);
        CoreProxy.instance.setupRenderers();
        CraftingManager.getInstance().getRecipeList().add(new CoverRecipe());
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent var1)
    {
        Config.saveConfig();
    }

    public static File getSaveDir(World var0)
    {
        return DimensionManager.getCurrentSaveRootDirectory();
    }
}
