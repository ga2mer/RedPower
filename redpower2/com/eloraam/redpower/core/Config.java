package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerCore;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Config
{
    static boolean[] reservedIds = new boolean[32768];
    static File configDir = null;
    static File configFile = null;
    static TagFile config = null;
    static Properties rpTranslateTable = null;
    static boolean autoAssign = true;

    public static void loadConfig()
    {
        config = new TagFile();
        InputStream var0 = RedPowerCore.class.getResourceAsStream("/eloraam/core/default.cfg");
        config.readStream(var0);
        File var1;

        if (configDir == null)
        {
            var1 = Loader.instance().getConfigDir();
            var1 = new File(var1, "/redpower/");
            var1.mkdir();
            configDir = var1;
            configFile = new File(var1, "redpower.cfg");
        }

        if (configFile.exists())
        {
            config.readFile(configFile);
        }

        config.commentFile("RedPower 2 Configuration");
        String var2;
        Iterator var4;

        for (var4 = config.query("blocks.%.%.id").iterator(); var4.hasNext(); reservedIds[config.getInt(var2)] = true)
        {
            var2 = (String)var4.next();
        }

        for (var4 = config.query("items.%.%.id").iterator(); var4.hasNext(); reservedIds[config.getInt(var2) + 256] = true)
        {
            var2 = (String)var4.next();
        }

        if (rpTranslateTable == null)
        {
            rpTranslateTable = new Properties();
        }

        try
        {
            rpTranslateTable.load(RedPowerCore.class.getResourceAsStream("/eloraam/core/redpower.lang"));
            var1 = new File(configDir, "redpower.lang");

            if (var1.exists())
            {
                FileInputStream var5 = new FileInputStream(var1);
                rpTranslateTable.load(var5);
            }
        }
        catch (IOException var3)
        {
            var3.printStackTrace();
        }

        var4 = rpTranslateTable.entrySet().iterator();

        while (var4.hasNext())
        {
            Entry var6 = (Entry)var4.next();
            LanguageRegistry.instance().addStringLocalization((String)var6.getKey(), (String)var6.getValue());
        }

        autoAssign = config.getInt("settings.core.autoAssign") > 0;
        config.addInt("settings.core.autoAssign", 0);
        config.commentTag("settings.core.autoAssign", "Automatically remap conflicting IDs.\nWARNING: May corrupt existing worlds");
    }

    public static void saveConfig()
    {
        config.saveFile(configFile);

        try
        {
            File var0 = new File(configDir, "redpower.lang");
            FileOutputStream var1 = new FileOutputStream(var0);
            rpTranslateTable.store(var1, "RedPower Language File");
        }
        catch (IOException var2)
        {
            var2.printStackTrace();
        }
    }

    public static void addName(String var0, String var1)
    {
        if (rpTranslateTable.get(var0) == null)
        {
            rpTranslateTable.put(var0, var1);
            LanguageRegistry.instance().addStringLocalization(var0, var1);
        }
    }

    public static void addName(Block var0, String var1)
    {
        addName(var0.getUnlocalizedName() + ".name", var1);
    }

    private static void die(String var0)
    {
        throw new RuntimeException("RedPowerCore: " + var0);
    }

    public static int getItemID(String var0)
    {
        int var1 = config.getInt(var0);

        if (Item.itemsList[256 + var1] == null)
        {
            return var1;
        }
        else if (!autoAssign)
        {
            die(String.format("ItemID %d exists, autoAssign is disabled.", new Object[] {Integer.valueOf(var1)}));
            return -1;
        }
        else
        {
            for (int var2 = 1024; var2 < 32000; ++var2)
            {
                if (!reservedIds[var2] && Item.itemsList[var2] == null)
                {
                    config.addInt(var0, var2 - 256);
                    return var2;
                }
            }

            die("Out of available ItemIDs, could not autoassign!");
            return -1;
        }
    }

    public static int getBlockID(String var0)
    {
        int var1 = config.getInt(var0);

        if (Block.blocksList[var1] == null)
        {
            return var1;
        }
        else if (!autoAssign)
        {
            die(String.format("BlockID %d occupied by %s, autoAssign is disabled.", new Object[] {Integer.valueOf(var1), Block.blocksList[var1].getClass().getName()}));
            return -1;
        }
        else
        {
            for (int var2 = 255; var2 >= 20; --var2)
            {
                if (!reservedIds[var2] && Block.blocksList[var2] == null)
                {
                    config.addInt(var0, var2);
                    return var2;
                }
            }

            die("Out of available BlockIDs, could not autoassign!");
            return -1;
        }
    }

    public static int getInt(String var0)
    {
        return config.getInt(var0);
    }

    public static String getString(String var0)
    {
        return config.getString(var0);
    }
}
