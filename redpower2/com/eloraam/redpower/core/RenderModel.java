package com.eloraam.redpower.core;

import com.eloraam.redpower.RedPowerCore;
import com.eloraam.redpower.core.RenderModel$ModelReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RenderModel
{
    public Vector3[] vertexList;
    public TexVertex[][] texList;
    int[][][] groups;

    public static RenderModel loadModel(String var0)
    {
        InputStream var1 = RedPowerCore.class.getResourceAsStream(var0);
        RenderModel$ModelReader var2 = new RenderModel$ModelReader();

        try
        {
            var2.readModel(var1);
        }
        catch (IOException var10)
        {
            return null;
        }

        ArrayList var3 = new ArrayList();
        int var4 = 0;
        int var6;
        int var7;
        int var8;

        while (var4 < var2.faceno.size())
        {
            TexVertex[] var5 = new TexVertex[4];

            for (var6 = 0; var6 < 4; ++var6)
            {
                var7 = ((Integer)var2.faceno.get(var4)).intValue();
                ++var4;

                if (var7 < 0)
                {
                    throw new IllegalArgumentException("Non-Quad Face");
                }

                var8 = ((Integer)var2.faceno.get(var4)).intValue();
                ++var4;
                TexVertex var9 = ((TexVertex)var2.texvert.get(var8 - 1)).copy();
                var9.vtx = var7 - 1;
                var9.v = 1.0D - var9.v;
                var5[var6] = var9;
            }

            var6 = ((Integer)var2.faceno.get(var4)).intValue();
            ++var4;

            if (var6 >= 0)
            {
                throw new IllegalArgumentException("Non-Quad Face");
            }

            var3.add(var5);
        }

        RenderModel var11 = new RenderModel();
        var11.vertexList = (Vector3[])var2.vertex.toArray(new Vector3[0]);
        var11.texList = (TexVertex[][])var3.toArray(new TexVertex[0][]);
        var11.groups = new int[var2.grcnt.size()][][];

        for (var4 = 0; var4 < var2.grcnt.size(); ++var4)
        {
            var6 = ((Integer)var2.grcnt.get(var4)).intValue();
            var11.groups[var4] = new int[var6][];

            for (var7 = 0; var7 < ((Integer)var2.grcnt.get(var4)).intValue(); ++var7)
            {
                var11.groups[var4][var7] = new int[2];
            }
        }

        var4 = 0;
        var6 = -1;
        var7 = -1;

        for (var8 = -1; var4 < var2.groups.size(); var4 += 3)
        {
            if (var6 >= 0)
            {
                var11.groups[var7][var8][0] = var6;
                var11.groups[var7][var8][1] = ((Integer)var2.groups.get(var4 + 2)).intValue();
            }

            var7 = ((Integer)var2.groups.get(var4)).intValue();
            var8 = ((Integer)var2.groups.get(var4 + 1)).intValue();
            var6 = ((Integer)var2.groups.get(var4 + 2)).intValue();
        }

        if (var6 >= 0)
        {
            var11.groups[var7][var8][0] = var6;
            var11.groups[var7][var8][1] = var2.fno;
        }

        return var11;
    }

    public void scale(double var1)
    {
        for (int var3 = 0; var3 < this.vertexList.length; ++var3)
        {
            this.vertexList[var3].multiply(var1);
        }
    }
}
