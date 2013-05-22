package com.eloraam.redpower.core;

import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidStack;

public abstract class FluidClass
{
    public abstract int getFluidId();

    public abstract int getFluidId(World var1, WorldCoord var2);

    public abstract int getFluidLevel(World var1, WorldCoord var2);

    public abstract boolean setFluidLevel(World var1, WorldCoord var2, int var3);

    public abstract int getFluidQuanta();

    public abstract String getTextureFile();

    public abstract int getTexture();

    public abstract LiquidStack getLiquidStack(int var1);
}
