package com.eloraam.redpower.core;

import com.eloraam.redpower.core.RenderLib$RenderListEntry;
import com.eloraam.redpower.core.RenderLib$RenderShiftedEntry;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class RenderLib
{
    private static RenderLib$RenderListEntry[] renderers = new RenderLib$RenderListEntry[4096];

    public static void bindTexture(String var0, int var1)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(var0);
    }

    public static void bindTexture(String var0)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(var0);
    }

    public static void unbindTexture()
    {
        ForgeHooksClient.onTextureLoad(null, null);
    }

    public static void setIntredTexture()
    {
        bindTexture("/eloraam/logic/logic1.png");
    }

    public static void setRedPowerTexture()
    {
        bindTexture("/eloraam/wiring/redpower1.png");
    }

    public static void setDefaultTexture()
    {
        unbindTexture();
    }

    public static void renderSpecialLever(Vector3 var0, Quat var1, int var2)
    {
        int var3 = (var2 & 15) << 4;
        int var4 = var2 & 240;
        Vector3[] var5 = new Vector3[8];
        float var6 = 0.0625F;
        float var7 = 0.0625F;
        float var8 = 0.375F;
        var5[0] = new Vector3((double)(-var6), 0.0D, (double)(-var7));
        var5[1] = new Vector3((double)var6, 0.0D, (double)(-var7));
        var5[2] = new Vector3((double)var6, 0.0D, (double)var7);
        var5[3] = new Vector3((double)(-var6), 0.0D, (double)var7);
        var5[4] = new Vector3((double)(-var6), (double)var8, (double)(-var7));
        var5[5] = new Vector3((double)var6, (double)var8, (double)(-var7));
        var5[6] = new Vector3((double)var6, (double)var8, (double)var7);
        var5[7] = new Vector3((double)(-var6), (double)var8, (double)var7);

        for (int var9 = 0; var9 < 8; ++var9)
        {
            var1.rotate(var5[var9]);
            var5[var9].add(var0.x + 0.5D, var0.y + 0.5D, var0.z + 0.5D);
        }

        float var10 = (float)(var3 + 7) / 256.0F;
        float var12 = ((float)(var3 + 9) - 0.01F) / 256.0F;
        float var11 = (float)(var4 + 6) / 256.0F;
        float var13 = ((float)(var4 + 8) - 0.01F) / 256.0F;
        addVectWithUV(var5[0], (double)var10, (double)var13);
        addVectWithUV(var5[1], (double)var12, (double)var13);
        addVectWithUV(var5[2], (double)var12, (double)var11);
        addVectWithUV(var5[3], (double)var10, (double)var11);
        addVectWithUV(var5[7], (double)var10, (double)var13);
        addVectWithUV(var5[6], (double)var12, (double)var13);
        addVectWithUV(var5[5], (double)var12, (double)var11);
        addVectWithUV(var5[4], (double)var10, (double)var11);
        var10 = (float)(var3 + 7) / 256.0F;
        var12 = ((float)(var3 + 9) - 0.01F) / 256.0F;
        var11 = (float)(var4 + 6) / 256.0F;
        var13 = ((float)(var4 + 12) - 0.01F) / 256.0F;
        addVectWithUV(var5[1], (double)var10, (double)var13);
        addVectWithUV(var5[0], (double)var12, (double)var13);
        addVectWithUV(var5[4], (double)var12, (double)var11);
        addVectWithUV(var5[5], (double)var10, (double)var11);
        addVectWithUV(var5[2], (double)var10, (double)var13);
        addVectWithUV(var5[1], (double)var12, (double)var13);
        addVectWithUV(var5[5], (double)var12, (double)var11);
        addVectWithUV(var5[6], (double)var10, (double)var11);
        addVectWithUV(var5[3], (double)var10, (double)var13);
        addVectWithUV(var5[2], (double)var12, (double)var13);
        addVectWithUV(var5[6], (double)var12, (double)var11);
        addVectWithUV(var5[7], (double)var10, (double)var11);
        addVectWithUV(var5[0], (double)var10, (double)var13);
        addVectWithUV(var5[3], (double)var12, (double)var13);
        addVectWithUV(var5[7], (double)var12, (double)var11);
        addVectWithUV(var5[4], (double)var10, (double)var11);
    }

    public static void addVectWithUV(Vector3 var0, double var1, double var3)
    {
        Tessellator var5 = Tessellator.instance;
        var5.addVertexWithUV(var0.x, var0.y, var0.z, var1, var3);
    }

    public static void renderPointer(Vector3 var0, Quat var1)
    {
        Tessellator var10 = Tessellator.instance;
        double var2 = 0.390625D;
        double var4 = 0.015625D;
        double var6 = 0.0312109375D;
        double var8 = 0.0077734375D;
        var10.setColorOpaque_F(0.9F, 0.9F, 0.9F);
        Vector3[] var11 = new Vector3[] {new Vector3(0.4D, 0.0D, 0.0D), new Vector3(0.0D, 0.0D, 0.2D), new Vector3(-0.2D, 0.0D, 0.0D), new Vector3(0.0D, 0.0D, -0.2D), new Vector3(0.4D, 0.1D, 0.0D), new Vector3(0.0D, 0.1D, 0.2D), new Vector3(-0.2D, 0.1D, 0.0D), new Vector3(0.0D, 0.1D, -0.2D)};

        for (int var12 = 0; var12 < 8; ++var12)
        {
            var1.rotate(var11[var12]);
            var11[var12].add(var0);
        }

        addVectWithUV(var11[0], var2, var4);
        addVectWithUV(var11[1], var2 + var6, var4);
        addVectWithUV(var11[2], var2 + var6, var4 + var6);
        addVectWithUV(var11[3], var2, var4 + var6);
        addVectWithUV(var11[4], var2, var4);
        addVectWithUV(var11[7], var2, var4 + var6);
        addVectWithUV(var11[6], var2 + var6, var4 + var6);
        addVectWithUV(var11[5], var2 + var6, var4);
        var10.setColorOpaque_F(0.6F, 0.6F, 0.6F);
        addVectWithUV(var11[0], var2 + var8, var4);
        addVectWithUV(var11[4], var2, var4);
        addVectWithUV(var11[5], var2, var4 + var6);
        addVectWithUV(var11[1], var2 + var8, var4 + var6);
        addVectWithUV(var11[0], var2, var4 + var8);
        addVectWithUV(var11[3], var2 + var6, var4 + var8);
        addVectWithUV(var11[7], var2 + var6, var4);
        addVectWithUV(var11[4], var2, var4);
        addVectWithUV(var11[2], var2 + var6, var4 + var6 - var8);
        addVectWithUV(var11[6], var2 + var6, var4 + var6);
        addVectWithUV(var11[7], var2, var4 + var6);
        addVectWithUV(var11[3], var2, var4 + var6 - var8);
        addVectWithUV(var11[2], var2 + var6, var4 + var6 - var8);
        addVectWithUV(var11[1], var2, var4 + var6 - var8);
        addVectWithUV(var11[5], var2, var4 + var6);
        addVectWithUV(var11[6], var2 + var6, var4 + var6);
    }

    public static RenderCustomBlock getRenderer(int var0, int var1)
    {
        RenderLib$RenderListEntry var2 = renderers[var0];
        return var2 == null ? null : var2.metaRenders[var1];
    }

    public static RenderCustomBlock getInvRenderer(int var0, int var1)
    {
        RenderLib$RenderListEntry var2 = renderers[var0];

        if (var2 == null)
        {
            return null;
        }
        else
        {
            int var3 = var2.mapDamageValue(var1);
            return var3 > 15 ? var2.defaultRender : var2.metaRenders[var2.mapDamageValue(var1)];
        }
    }

    private static RenderCustomBlock makeRenderer(Block var0, Class var1)
    {
        try
        {
            RenderCustomBlock var2 = (RenderCustomBlock)var1.getDeclaredConstructor(new Class[] {Block.class}).newInstance(new Object[] {var0});
            return var2;
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
            return null;
        }
    }

    public static void setRenderer(Block var0, Class var1)
    {
        RenderCustomBlock var2 = makeRenderer(var0, var1);

        if (renderers[var0.blockID] == null)
        {
            renderers[var0.blockID] = new RenderLib$RenderListEntry();
        }

        for (int var3 = 0; var3 < 16; ++var3)
        {
            renderers[var0.blockID].metaRenders[var3] = var2;
        }
    }

    public static void setRenderer(Block var0, int var1, Class var2)
    {
        RenderCustomBlock var3 = makeRenderer(var0, var2);

        if (renderers[var0.blockID] == null)
        {
            renderers[var0.blockID] = new RenderLib$RenderListEntry();
        }

        renderers[var0.blockID].metaRenders[var1] = var3;
    }

    public static void setHighRenderer(Block var0, int var1, Class var2)
    {
        RenderCustomBlock var3 = makeRenderer(var0, var2);

        if (renderers[var0.blockID] == null)
        {
            renderers[var0.blockID] = new RenderLib$RenderShiftedEntry(8);
        }

        renderers[var0.blockID].metaRenders[var1] = var3;
    }

    public static void setDefaultRenderer(Block var0, int var1, Class var2)
    {
        RenderCustomBlock var3 = makeRenderer(var0, var2);

        if (renderers[var0.blockID] == null)
        {
            renderers[var0.blockID] = new RenderLib$RenderShiftedEntry(var1);
        }

        for (int var4 = 0; var4 < 16; ++var4)
        {
            if (renderers[var0.blockID].metaRenders[var4] == null)
            {
                renderers[var0.blockID].metaRenders[var4] = var3;
            }
        }

        renderers[var0.blockID].defaultRender = var3;
    }

    public static void setShiftedRenderer(Block var0, int var1, int var2, Class var3)
    {
        RenderCustomBlock var4 = makeRenderer(var0, var3);

        if (renderers[var0.blockID] == null)
        {
            renderers[var0.blockID] = new RenderLib$RenderShiftedEntry(var2);
        }

        renderers[var0.blockID].metaRenders[var1] = var4;
    }
}
