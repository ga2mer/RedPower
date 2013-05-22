package com.eloraam.redpower.core;

import net.minecraft.world.IBlockAccess;

class RedbusLib$RedbusPathfinder extends WirePathfinder
{
    public IRedbusConnectable result = null;
    IBlockAccess iba;
    int addr;

    public RedbusLib$RedbusPathfinder(IBlockAccess var1, int var2)
    {
        this.iba = var1;
        this.addr = var2;
        this.init();
    }

    public boolean step(WorldCoord var1)
    {
        IRedbusConnectable var2 = (IRedbusConnectable)CoreLib.getTileEntity(this.iba, var1, IRedbusConnectable.class);

        if (var2 != null && var2.rbGetAddr() == this.addr)
        {
            this.result = var2;
            return false;
        }
        else
        {
            IWiring var3 = (IWiring)CoreLib.getTileEntity(this.iba, var1, IWiring.class);

            if (var3 == null)
            {
                return true;
            }
            else
            {
                this.addSearchBlocks(var1, var3.getConnectionMask(), var3.getExtConnectionMask());
                return true;
            }
        }
    }
}
