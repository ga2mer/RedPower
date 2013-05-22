package com.eloraam.redpower.machine;

import com.eloraam.redpower.RedPowerMachine;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.machine.ItemRenderMachine$1;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderMachine implements IItemRenderer
{
    protected RenderContext context = new RenderContext();

    public boolean handleRenderType(ItemStack var1, IItemRenderer.ItemRenderType var2)
    {
        return var1.itemID == RedPowerMachine.blockMachine.blockID && var1.getItemDamage() == 6 ? var1.stackTagCompound != null : false;
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType var1, ItemStack var2, IItemRenderer.ItemRendererHelper var3)
    {
        switch (ItemRenderMachine$1.$SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRendererHelper[var3.ordinal()])
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return true;

            default:
                return false;
        }
    }

    public void renderItem(IItemRenderer.ItemRenderType var1, ItemStack var2, Object ... var3)
    {
        if (var2.getItemDamage() == 6)
        {
            this.context.setDefaults();
            this.context.setPos(-0.5D, -0.5D, -0.5D);
            this.context.useNormal = true;
            RenderLib.bindTexture("/eloraam/machine/machine1.png");
            Tessellator var4 = Tessellator.instance;
            var4.startDrawingQuads();
            short var5 = 0;

            if (var2.stackTagCompound != null)
            {
                var5 = var2.stackTagCompound.getShort("batLevel");
            }

            int var6 = 129 + var5 * 8 / 6000;
            this.context.setTex(84, 128, var6, var6, var6, var6);
            this.context.renderBox(63, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            var4.draw();
            RenderLib.unbindTexture();
            this.context.useNormal = false;
        }
    }
}
