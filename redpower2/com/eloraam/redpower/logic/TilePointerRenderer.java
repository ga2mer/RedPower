package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.MathLib;
import com.eloraam.redpower.core.Quat;
import com.eloraam.redpower.core.RenderLib;
import com.eloraam.redpower.core.Vector3;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TilePointerRenderer extends TileEntitySpecialRenderer
{
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        Tessellator var9 = Tessellator.instance;

        if (var1 instanceof TileLogicPointer)
        {
            TileLogicPointer var10 = (TileLogicPointer)var1;
            this.bindTextureByName("/terrain.png");
            int var11 = var1.worldObj.getLightBrightnessForSkyBlocks(var1.xCoord, var1.yCoord, var1.zCoord, 0);
            var9.setBrightness(var11);
            GL11.glDisable(GL11.GL_LIGHTING);
            var9.startDrawingQuads();
            float var12 = var10.getPointerDirection(var8) + 0.25F;
            Quat var13 = MathLib.orientQuat(var10.Rotation >> 2, var10.Rotation & 3);
            Vector3 var14 = var10.getPointerOrigin();
            var13.rotate(var14);
            var14.add(var2 + 0.5D, var4 + 0.5D, var6 + 0.5D);
            var13.rightMultiply(Quat.aroundAxis(0.0D, 1.0D, 0.0D, -(Math.PI * 2D) * (double)var12));
            RenderLib.renderPointer(var14, var13);
            var9.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }
}
