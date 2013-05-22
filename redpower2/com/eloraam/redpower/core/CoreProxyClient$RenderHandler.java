package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerCore;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class CoreProxyClient$RenderHandler implements ISimpleBlockRenderingHandler
{
    public void renderInventoryBlock(Block var1, int var2, int var3, RenderBlocks var4)
    {
        if (var3 == RedPowerCore.customBlockModel)
        {
            RenderCustomBlock var5 = RenderLib.getInvRenderer(var1.blockID, var2);

            if (var5 == null)
            {
                System.out.printf("Bad Render at %d:%d\n", new Object[] {Integer.valueOf(var1.blockID), Integer.valueOf(var2)});
            }
            else
            {
                var5.renderInvBlock(var4, var2);
            }
        }
    }

    public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
    {
        if (var7.overrideBlockTexture != null)
        {
            return false;
        }
        else if (var6 != RedPowerCore.customBlockModel)
        {
            return false;
        }
        else
        {
            int var8 = var1.getBlockMetadata(var2, var3, var4);
            RenderCustomBlock var9 = RenderLib.getRenderer(var5.blockID, var8);

            if (var9 == null)
            {
                System.out.printf("Bad Render at %d:%d\n", new Object[] {Integer.valueOf(var5.blockID), Integer.valueOf(var8)});
                return true;
            }
            else
            {
                //var9.renderWorldBlock(var7, var1, var2, var3, var4, var8);
                return false;
            }
        }
    }

    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    public int getRenderId()
    {
        return RedPowerCore.customBlockModel;
    }
}
