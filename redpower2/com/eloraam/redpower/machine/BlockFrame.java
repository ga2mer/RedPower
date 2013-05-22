package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.BlockCoverable;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CreativeExtraTabs;
import com.eloraam.redpower.core.WorldCoord;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockFrame extends BlockCoverable
{
    public BlockFrame(int var1)
    {
        super(var1, CoreLib.materialRedpower);
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }

    /**
     * if the specified block is in the given AABB, add its collision bounding box to the given list
     */
    public void addCollidingBlockToList(World var1, int var2, int var3, int var4, AxisAlignedBB var5, List var6, Entity var7)
    {
        TileFrameMoving var8 = (TileFrameMoving)CoreLib.getTileEntity(var1, var2, var3, var4, TileFrameMoving.class);

        if (var8 == null)
        {
            super.addCollidingBlockToList(var1, var2, var3, var4, var5, var6, var7);
        }
        else
        {
            this.computeCollidingBoxes(var1, var2, var3, var4, var5, var6, var8);
            TileMotor var9 = (TileMotor)CoreLib.getTileEntity(var1, var8.motorX, var8.motorY, var8.motorZ, TileMotor.class);

            if (var9 != null)
            {
                WorldCoord var10 = new WorldCoord(var2, var3, var4);
                var10.step(var9.MoveDir ^ 1);
                var8 = (TileFrameMoving)CoreLib.getTileEntity(var1, var10, TileFrameMoving.class);

                if (var8 != null)
                {
                    this.computeCollidingBoxes(var1, var10.x, var10.y, var10.z, var5, var6, var8);
                }
            }
        }
    }

    public String getTextureFile()
    {
        return "/eloraam/machine/machine1.png";
    }
}
