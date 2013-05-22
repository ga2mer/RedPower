package com.eloraam.redpower.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBufferChest extends Container
{
    private TileBufferChest tileBuffer;

    public ContainerBufferChest(IInventory var1, TileBufferChest var2)
    {
        this.tileBuffer = var2;
        int var3;
        int var4;

        for (var3 = 0; var3 < 5; ++var3)
        {
            for (var4 = 0; var4 < 4; ++var4)
            {
                this.addSlotToContainer(new Slot(var2, var4 + var3 * 4, 44 + var3 * 18, 18 + var4 * 18));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 104 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(var1, var3, 8 + var3 * 18, 162));
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileBuffer.isUseableByPlayer(var1);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer var1, int var2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(var2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (var2 < 20)
            {
                if (!this.mergeItemStack(var5, 20, 56, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 20, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(var1, var5);
        }

        return var3;
    }
}
