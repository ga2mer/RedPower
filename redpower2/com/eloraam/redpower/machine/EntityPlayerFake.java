package com.eloraam.redpower.machine;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityPlayerFake extends EntityPlayer
{
    public EntityPlayerFake(World var1)
    {
        super(var1);
        this.username = "";

        for (int var2 = 9; var2 < 36; ++var2)
        {
            this.inventory.setInventorySlotContents(var2, new ItemStack(Item.stick));
        }
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity var1) {}

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int var1, String var2)
    {
        return false;
    }

    public void sendChatToPlayer(String var1) {}

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ));
    }
}
