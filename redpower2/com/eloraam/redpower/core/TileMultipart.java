package com.eloraam.redpower.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;

public abstract class TileMultipart extends TileExtended implements IMultipart
{
    public boolean isSideSolid(int var1)
    {
        return false;
    }

    public boolean isSideNormal(int var1)
    {
        return false;
    }

    public List harvestMultipart()
    {
        ArrayList var1 = new ArrayList();
        this.addHarvestContents(var1);
        this.deleteBlock();
        return var1;
    }

    public void onHarvestPart(EntityPlayer var1, int var2) {}

    public boolean onPartActivateSide(EntityPlayer var1, int var2, int var3)
    {
        return false;
    }

    public float getPartStrength(EntityPlayer var1, int var2)
    {
        return 0.0F;
    }

    public abstract boolean blockEmpty();

    public abstract void setPartBounds(BlockMultipart var1, int var2);

    public abstract int getSolidPartsMask();

    public abstract int getPartsMask();

    public void deleteBlock()
    {
        BlockMultipart.removeMultipartWithNotify(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }
}
