package com.eloraam.redpower.core;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.ForgeSubscribe;
import org.lwjgl.opengl.GL11;

public class RenderHighlight
{
    RenderContext context = new RenderContext();
    CoverRenderer coverRenderer;

    public RenderHighlight()
    {
        this.coverRenderer = new CoverRenderer(this.context);
    }

    @ForgeSubscribe
    public void highlightEvent(DrawBlockHighlightEvent var1)
    {
        this.onBlockHighlight(var1.context, var1.player, var1.target, var1.subID, var1.currentItem, var1.partialTicks);
    }

    public boolean onBlockHighlight(RenderGlobal var1, EntityPlayer var2, MovingObjectPosition var3, int var4, ItemStack var5, float var6)
    {
        World var7 = var2.worldObj;
        int var8 = var7.getBlockId(var3.blockX, var3.blockY, var3.blockZ);

        if (!var1.damagedBlocks.isEmpty())
        {
            Iterator var9 = var1.damagedBlocks.values().iterator();

            while (var9.hasNext())
            {
                Object var10 = var9.next();
                DestroyBlockProgress var11 = (DestroyBlockProgress)var10;

                if (var11.getPartialBlockX() == var3.blockX && var11.getPartialBlockY() == var3.blockY && var11.getPartialBlockZ() == var3.blockZ)
                {
                    if (Block.blocksList[var8] instanceof BlockExtended)
                    {
                        this.drawBreaking(var2.worldObj, var1, (BlockExtended)Block.blocksList[var8], var2, var3, var6, var11.getPartialBlockDamage());
                        var1.drawSelectionBox(var2, var3, var4, var5, var6);
                        return true;
                    }

                    break;
                }
            }
        }

        if (var5 != null && CoverLib.blockCoverPlate != null && var5.itemID == CoverLib.blockCoverPlate.blockID)
        {
            if (var3.typeOfHit != EnumMovingObjectType.TILE)
            {
                return false;
            }
            else
            {
                MovingObjectPosition var12;

                switch (var5.getItemDamage() >> 8)
                {
                    case 0:
                    case 16:
                    case 17:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                        this.drawSideBox(var7, var2, var3, var6);
                        var12 = CoverLib.getPlacement(var7, var3, var5.getItemDamage());

                        if (var12 != null)
                        {
                            this.drawPreview(var2, var12, var6, var5.getItemDamage());
                        }

                        break;

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    default:
                        return false;

                    case 18:
                    case 19:
                    case 20:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                        this.drawCornerBox(var7, var2, var3, var6);
                        var12 = CoverLib.getPlacement(var7, var3, var5.getItemDamage());

                        if (var12 != null)
                        {
                            this.drawPreview(var2, var12, var6, var5.getItemDamage());
                        }
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }

    private void setRawPos(EntityPlayer var1, MovingObjectPosition var2, float var3)
    {
        double var4 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var3;
        double var6 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var3;
        double var8 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var3;
        this.context.setPos((double)var2.blockX - var4, (double)var2.blockY - var6, (double)var2.blockZ - var8);
    }

    private void setCollPos(EntityPlayer var1, MovingObjectPosition var2, float var3)
    {
        this.setRawPos(var1, var2, var3);

        switch (var2.sideHit)
        {
            case 0:
                this.context.setRelPos(0.0D, var2.hitVec.yCoord - (double)var2.blockY, 0.0D);
                break;

            case 1:
                this.context.setRelPos(0.0D, (double)var2.blockY - var2.hitVec.yCoord + 1.0D, 0.0D);
                break;

            case 2:
                this.context.setRelPos(0.0D, var2.hitVec.zCoord - (double)var2.blockZ, 0.0D);
                break;

            case 3:
                this.context.setRelPos(0.0D, (double)var2.blockZ - var2.hitVec.zCoord + 1.0D, 0.0D);
                break;

            case 4:
                this.context.setRelPos(0.0D, var2.hitVec.xCoord - (double)var2.blockX, 0.0D);
                break;

            default:
                this.context.setRelPos(0.0D, (double)var2.blockX - var2.hitVec.xCoord + 1.0D, 0.0D);
        }
    }

    public void drawCornerBox(World var1, EntityPlayer var2, MovingObjectPosition var3, float var4)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.9F);
        GL11.glLineWidth(3.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        float var5 = 0.002F;
        float var6 = 0.25F;
        int var7 = var1.getBlockId(var3.blockX, var3.blockY, var3.blockZ);

        if (var7 > 0)
        {
            this.context.setSize(0.0D, (double)(-var5), 0.0D, 1.0D, (double)(-var5), 1.0D);
            this.context.setupBox();
            this.context.vertexList[4].set(0.0D, (double)(-var5), 0.5D);
            this.context.vertexList[5].set(1.0D, (double)(-var5), 0.5D);
            this.context.vertexList[6].set(0.5D, (double)(-var5), 0.0D);
            this.context.vertexList[7].set(0.5D, (double)(-var5), 1.0D);
            this.context.setOrientation(var3.sideHit, 0);
            this.setCollPos(var2, var3, var4);
            this.context.transformRotate();
            Tessellator.instance.startDrawing(3);
            this.context.drawPoints(new int[] {0, 1, 2, 3, 0});
            Tessellator.instance.draw();
            Tessellator.instance.startDrawing(1);
            this.context.drawPoints(new int[] {4, 5, 6, 7});
            Tessellator.instance.draw();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        this.context.setRelPos(0.0D, 0.0D, 0.0D);
    }

    public void drawSideBox(World var1, EntityPlayer var2, MovingObjectPosition var3, float var4)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.9F);
        GL11.glLineWidth(3.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        float var5 = 0.002F;
        float var6 = 0.25F;
        int var7 = var1.getBlockId(var3.blockX, var3.blockY, var3.blockZ);

        if (var7 > 0)
        {
            this.context.setSize(0.0D, (double)(-var5), 0.0D, 1.0D, (double)(-var5), 1.0D);
            this.context.setupBox();
            this.context.vertexList[4].set((double)(1.0F - var6), (double)(-var5), (double)var6);
            this.context.vertexList[5].set((double)var6, (double)(-var5), (double)var6);
            this.context.vertexList[6].set((double)var6, (double)(-var5), (double)(1.0F - var6));
            this.context.vertexList[7].set((double)(1.0F - var6), (double)(-var5), (double)(1.0F - var6));
            this.context.setOrientation(var3.sideHit, 0);
            this.setCollPos(var2, var3, var4);
            this.context.transformRotate();
            Tessellator.instance.startDrawing(3);
            this.context.drawPoints(new int[] {0, 1, 2, 3, 0});
            Tessellator.instance.draw();
            Tessellator.instance.startDrawing(3);
            this.context.drawPoints(new int[] {4, 5, 6, 7, 4});
            Tessellator.instance.draw();
            Tessellator.instance.startDrawing(1);
            this.context.drawPoints(new int[] {0, 4, 1, 5, 2, 6, 3, 7});
            Tessellator.instance.draw();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        this.context.setRelPos(0.0D, 0.0D, 0.0D);
    }

    public void drawBreaking(World var1, RenderGlobal var2, BlockExtended var3, EntityPlayer var4, MovingObjectPosition var5, float var6, int var7)
    {
        if (var3 instanceof BlockMultipart)
        {
            BlockMultipart var8 = (BlockMultipart)var3;
            var8.setPartBounds(var1, var5.blockX, var5.blockY, var5.blockZ, var5.subHit);
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
        int var15 = var2.renderEngine.getTexture("/terrain.png");
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var15);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GL11.glPolygonOffset(-3.0F, -3.0F);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        double var9 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var6;
        double var11 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var6;
        double var13 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var6;
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.context.setPos((double)var5.blockX - var9, (double)var5.blockY - var11, (double)var5.blockZ - var13);
        this.context.setTex(240 + var7);
        Tessellator.instance.startDrawingQuads();
        this.context.setSize(var3.getBlockBoundsMinX(), var3.getBlockBoundsMinY(), var3.getBlockBoundsMinZ(), var3.getBlockBoundsMaxX(), var3.getBlockBoundsMaxY(), var3.getBlockBoundsMaxZ());
        this.context.setupBox();
        this.context.transform();
        this.context.renderFaces(63);
        Tessellator.instance.draw();
        GL11.glPolygonOffset(0.0F, 0.0F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
    }

    public void drawPreview(EntityPlayer var1, MovingObjectPosition var2, float var3, int var4)
    {
        this.setRawPos(var1, var2, var3);
        this.coverRenderer.start();
        this.coverRenderer.setupCorners();
        this.coverRenderer.setSize(var2.subHit, CoverLib.getThickness(var2.subHit, CoverLib.damageToCoverValue(var4)));
        this.context.setTexFile(CoverLib.coverTextureFiles[var4 & 255]);
        this.context.setTex(CoverLib.coverTextures[var4 & 255]);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDepthMask(false);
        GL11.glPolygonOffset(-3.0F, -3.0F);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        Tessellator.instance.startDrawingQuads();
        this.context.setupBox();
        this.context.transform();
        this.context.doMappingBox(63);
        this.context.doLightLocal(63);
        this.context.renderAlpha(63, 0.8F);
        Tessellator.instance.draw();
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
