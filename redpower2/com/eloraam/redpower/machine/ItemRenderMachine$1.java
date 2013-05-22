package com.eloraam.redpower.machine;

import net.minecraftforge.client.IItemRenderer;

class ItemRenderMachine$1
{
    static final int[] $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRendererHelper = new int[IItemRenderer.ItemRendererHelper.values().length];

    static
    {
        try
        {
            $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRendererHelper[IItemRenderer.ItemRendererHelper.INVENTORY_BLOCK.ordinal()] = 1;
        }
        catch (NoSuchFieldError var5)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRendererHelper[IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK.ordinal()] = 2;
        }
        catch (NoSuchFieldError var4)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRendererHelper[IItemRenderer.ItemRendererHelper.ENTITY_ROTATION.ordinal()] = 3;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRendererHelper[IItemRenderer.ItemRendererHelper.ENTITY_BOBBING.ordinal()] = 4;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRendererHelper[IItemRenderer.ItemRendererHelper.BLOCK_3D.ordinal()] = 5;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
