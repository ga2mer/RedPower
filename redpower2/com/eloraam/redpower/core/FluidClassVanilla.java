package com.eloraam.redpower.core;

import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidStack;

public class FluidClassVanilla extends FluidClass
{
    int fluidID;
    int idStill;
    int idMoving;
    String texFile;
    int texture;

    public FluidClassVanilla(int var1, int var2, int var3, String var4, int var5)
    {
        this.idStill = var2;
        this.idMoving = var3;
        this.texture = var5;
        this.texFile = var4;
        this.fluidID = var1;
    }

    public int getFluidId()
    {
        return this.fluidID;
    }

    public int getFluidId(World var1, WorldCoord var2)
    {
        int var3 = var1.getBlockId(var2.x, var2.y, var2.z);
        return var3 != this.idStill && var3 != this.idMoving ? 0 : this.fluidID;
    }

    public int getFluidLevel(World var1, WorldCoord var2)
    {
        int var3 = var1.getBlockMetadata(var2.x, var2.y, var2.z);
        return var3 == 0 ? 1000 : 0;
    }

    public boolean setFluidLevel(World var1, WorldCoord var2, int var3)
    {
        if (var3 == 1000)
        {
            var1.setBlock(var2.x, var2.y, var2.z, this.idMoving);
            return true;
        }
        else
        {
            return false;
        }
    }

    public int getFluidQuanta()
    {
        return 1000;
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
        return new LiquidStack(this.idStill, var1, 0);
    }
}
