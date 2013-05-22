package com.eloraam.redpower.world;

import com.eloraam.redpower.world.ContainerSeedBag$SlotSeeds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSeedBag extends Container
{
    ItemStack itemBag;
    IInventory baginv;

    public ContainerSeedBag(InventoryPlayer var1, IInventory var2, ItemStack var3)
    {
        this.baginv = var2;
        int var4;
        int var5;

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 3; ++var5)
            {
                this.addSlotToContainer(new ContainerSeedBag$SlotSeeds(var2, var5 + var4 * 3, 62 + var5 * 18, 17 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(var1, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(var1, var4, 8 + var4 * 18, 142));
        }

        this.itemBag = var3;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return var1.inventory.getCurrentItem() == this.itemBag;
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

            if (!ItemSeedBag.canAdd(this.baginv, var5))
            {
                return null;
            }

            var3 = var5.copy();

            if (var2 < 9)
            {
                if (!this.mergeItemStack(var5, 9, 45, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 9, false))
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
