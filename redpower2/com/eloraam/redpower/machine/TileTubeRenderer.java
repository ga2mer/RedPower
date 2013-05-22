package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.ITubeFlow;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.TubeFlow;
import com.eloraam.redpower.core.TubeItem;
import com.eloraam.redpower.core.WorldCoord;
import java.util.Iterator;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileTubeRenderer extends TileEntitySpecialRenderer
{
    int[] paintColors = new int[] {16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583};
    RenderContext context = new RenderContext();
    EntityItem entityitem = new EntityItem((World)null);

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        Tessellator var9 = Tessellator.instance;
        int var10 = var1.worldObj.getLightBrightnessForSkyBlocks(var1.xCoord, var1.yCoord, var1.zCoord, 0);
        var9.setBrightness(var10);
        this.entityitem.worldObj = var1.worldObj;
        this.entityitem.setPosition((double)var1.xCoord + 0.5D, (double)var1.yCoord + 0.5D, (double)var1.zCoord + 0.5D);
        RenderItem var11 = (RenderItem)RenderManager.instance.getEntityClassRenderObject(EntityItem.class);
        this.entityitem.age = 0;
        this.entityitem.hoverStart = 0.0F;
        WorldCoord var12 = new WorldCoord(0, 0, 0);
        ITubeFlow var13 = (ITubeFlow)var1;
        TubeFlow var14 = var13.getTubeFlow();
        Iterator var15 = var14.contents.iterator();

        while (var15.hasNext())
        {
            TubeItem var16 = (TubeItem)var15.next();
            this.entityitem.setEntityItemStack(var16.item);
            var12.x = 0;
            var12.y = 0;
            var12.z = 0;
            var12.step(var16.side);
            double var17 = (double)var16.progress / 128.0D * 0.5D;

            if (!var16.scheduled)
            {
                var17 = 0.5D - var17;
            }

            double var19 = 0.0D;

            if (var16.item.itemID >= 256)
            {
                var19 += 0.1D;
            }

            var11.doRenderItem(this.entityitem, var2 + 0.5D + (double)var12.x * var17, var4 + 0.5D - (double)this.entityitem.yOffset - var19 + (double)var12.y * var17, var6 + 0.5D + (double)var12.z * var17, 0.0F, 0.0F);

            if (var16.color > 0)
            {
                this.bindTextureByName("/eloraam/machine/machine1.png");
                var9.startDrawingQuads();
                this.context.useNormal = true;
                this.context.setDefaults();
                this.context.setBrightness(var10);
                this.context.setPos(var2 + (double)var12.x * var17, var4 + (double)var12.y * var17, var6 + (double)var12.z * var17);
                this.context.setTintHex(this.paintColors[var16.color - 1]);
                this.context.setTex(3);
                this.context.renderBox(63, 0.25999999046325684D, 0.25999999046325684D, 0.25999999046325684D, 0.7400000095367432D, 0.7400000095367432D, 0.7400000095367432D);
                var9.draw();
            }
        }
    }
}
