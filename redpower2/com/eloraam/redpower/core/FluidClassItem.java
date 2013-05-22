package com.eloraam.redpower.core;

import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidStack;

public class FluidClassItem extends FluidClass
{
    int fluidID;
    int itemID;
    int itemMeta;
    String texFile;
    int texture;

    public FluidClassItem(int var1, int var2, int var3, String var4, int var5)
    {
        this.fluidID = var1;
        this.itemID = var2;
        this.itemMeta = var3;
        this.texture = var5;
        this.texFile = var4;
    }

    public int getFluidId()
    {
        return this.fluidID;
    }

    public int getFluidId(World var1, WorldCoord var2)
    {
        return 0;
    }

    public int getFluidLevel(World var1, WorldCoord var2)
    {
        return 0;
    }

    public boolean setFluidLevel(World var1, WorldCoord var2, int var3)
    {
        return false;
    }

    public int getFluidQuanta()
    {
        return 0;
    }

    public String getTextureFile()
    {
        return this.texFile;
    }

    public int getTexture()
    {
        return this.texture;
    }

    public LiquidStack getLiquidStack(int var1)
    {
        return new LiquidStack(this.itemID, var1, this.itemMeta);
    }
}
