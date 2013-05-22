package com.eloraam.redpower.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerAdvBench$ContainerNull extends Container
{
    public boolean canInteractWith(EntityPlayer var1)
    {
        return false;
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory var1) {}
}
