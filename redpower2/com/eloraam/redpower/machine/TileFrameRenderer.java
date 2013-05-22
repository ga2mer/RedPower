package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.WorldCoord;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class TileFrameRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks rblocks;
    RenderContext context = new RenderContext();

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        if (!var1.isInvalid())
        {
            TileFrameMoving var9 = (TileFrameMoving)var1;
            Block var10 = Block.blocksList[var9.movingBlockID];

            if (var10 != null)
            {
                Tessellator var11 = Tessellator.instance;
                this.bindTextureByName("/terrain.png");
                int var12 = var1.worldObj.getLightBrightnessForSkyBlocks(var1.xCoord, var1.yCoord, var1.zCoord, 0);
                var11.setBrightness(var12);
                RenderHelper.disableStandardItemLighting();
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_CULL_FACE);

                if (Minecraft.isAmbientOcclusionEnabled())
                {
                    GL11.glShadeModel(GL11.GL_SMOOTH);
                }
                else
                {
                    GL11.glShadeModel(GL11.GL_FLAT);
                }

                IBlockAccess var13 = this.rblocks.blockAccess;
                this.rblocks.blockAccess = var9.getFrameBlockAccess();
                //ForgeHooksClient.beforeBlockRender(var10, this.rblocks);
                TileMotor var14 = (TileMotor)CoreLib.getTileEntity(var9.worldObj, var9.motorX, var9.motorY, var9.motorZ, TileMotor.class);
                GL11.glPushMatrix();

                if (var14 != null)
                {
                    WorldCoord var15 = new WorldCoord(0, 0, 0);
                    var15.step(var14.MoveDir);
                    float var16 = var14.getMoveScaled();
                    GL11.glTranslatef((float)var15.x * var16, (float)var15.y * var16, (float)var15.z * var16);
                }

                var11.startDrawingQuads();
                var11.setTranslation(var2 - (double)var9.xCoord, var4 - (double)var9.yCoord, var6 - (double)var9.zCoord);
                var11.setColorOpaque(1, 1, 1);

                if (var9.movingCrate)
                {
                    this.context.setDefaults();
                    this.context.setBrightness(var12);
                    this.context.setPos((double)var9.xCoord, (double)var9.yCoord, (double)var9.zCoord);
                    this.context.setTexFile("/eloraam/machine/machine1.png");
                    this.context.setTex(5);
                    this.context.renderBox(63, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                }
                else
                {
                    var9.doRefresh(var9.getFrameBlockAccess());
                    this.rblocks.renderAllFaces = true;
                    this.rblocks.renderBlockByRenderType(var10, var9.xCoord, var9.yCoord, var9.zCoord);
                    this.rblocks.renderAllFaces = false;
                }

                var11.setTranslation(0.0D, 0.0D, 0.0D);
                var11.draw();
                GL11.glPopMatrix();
                //ForgeHooksClient.afterBlockRender(var10, this.rblocks);
                this.rblocks.blockAccess = var13;
                RenderHelper.enableStandardItemLighting();
            }
        }
    }

    /**
     * Called when the ingame world being rendered changes (e.g. on world -> nether travel) due to using one renderer
     * per tile entity type, rather than instance
     */
    public void onWorldChange(World var1)
    {
        this.rblocks = new RenderBlocks(var1);
    }
}
