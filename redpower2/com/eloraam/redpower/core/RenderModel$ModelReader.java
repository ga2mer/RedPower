package com.eloraam.redpower.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class RenderModel$ModelReader
{
    public ArrayList vertex = new ArrayList();
    public ArrayList faceno = new ArrayList();
    public ArrayList texvert = new ArrayList();
    public ArrayList groups = new ArrayList();
    public ArrayList grcnt = new ArrayList();
    int fno = 0;

    private void eatline(StreamTokenizer var1) throws IOException
    {
        while (true)
        {
            if (var1.nextToken() != -1)
            {
                if (var1.ttype != 10)
                {
                    continue;
                }

                return;
            }

            return;
        }
    }

    private void endline(StreamTokenizer var1) throws IOException
    {
        if (var1.nextToken() != 10)
        {
            throw new IllegalArgumentException("Parse error");
        }
    }

    private double getfloat(StreamTokenizer var1) throws IOException
    {
        if (var1.nextToken() != -2)
        {
            throw new IllegalArgumentException("Parse error");
        }
        else
        {
            return var1.nval;
        }
    }

    private int getint(StreamTokenizer var1) throws IOException
    {
        if (var1.nextToken() != -2)
        {
            throw new IllegalArgumentException("Parse error");
        }
        else
        {
            return (int)var1.nval;
        }
    }

    private void parseface(StreamTokenizer var1) throws IOException
    {
        while (true)
        {
            var1.nextToken();

            if (var1.ttype == -1 || var1.ttype == 10)
            {
                this.faceno.add(Integer.valueOf(-1));
                ++this.fno;
                return;
            }

            if (var1.ttype != -2)
            {
                throw new IllegalArgumentException("Parse error");
            }

            int var2 = (int)var1.nval;

            if (var1.nextToken() != 47)
            {
                throw new IllegalArgumentException("Parse error");
            }

            int var3 = this.getint(var1);
            this.faceno.add(Integer.valueOf(var2));
            this.faceno.add(Integer.valueOf(var3));
        }
    }

    private void setgroup(int var1, int var2)
    {
        this.groups.add(Integer.valueOf(var1));
        this.groups.add(Integer.valueOf(var2));
        this.groups.add(Integer.valueOf(this.fno));

        if (this.grcnt.size() < var1)
        {
            throw new IllegalArgumentException("Parse error");
        }
        else
        {
            if (this.grcnt.size() == var1)
            {
                this.grcnt.add(Integer.valueOf(0));
            }

            this.grcnt.set(var1, Integer.valueOf(Math.max(((Integer)this.grcnt.get(var1)).intValue(), var2 + 1)));
        }
    }

    private void parsegroup(StreamTokenizer var1) throws IOException
    {
        int var2 = this.getint(var1);
        int var3 = 0;
        var1.nextToken();

        if (var1.ttype == 95)
        {
            var3 = this.getint(var1);
            var1.nextToken();
        }

        this.setgroup(var2, var3);

        if (var1.ttype != 10)
        {
            throw new IllegalArgumentException("Parse error");
        }
    }

    public void readModel(InputStream var1) throws IOException
    {
        BufferedReader var2 = new BufferedReader(new InputStreamReader(var1));
        StreamTokenizer var3 = new StreamTokenizer(var2);
        var3.commentChar(35);
        var3.eolIsSignificant(true);
        var3.lowerCaseMode(false);
        var3.parseNumbers();
        var3.quoteChar(34);
        var3.ordinaryChar(47);

        while (var3.nextToken() != -1)
        {
            if (var3.ttype != 10)
            {
                if (var3.ttype != -3)
                {
                    throw new IllegalArgumentException("Parse error");
                }

                if (var3.sval.equals("v"))
                {
                    Vector3 var4 = new Vector3();
                    var4.x = this.getfloat(var3);
                    var4.y = this.getfloat(var3);
                    var4.z = this.getfloat(var3);
                    this.vertex.add(var4);
                    this.endline(var3);
                }
                else
                {
                    double var6;
                    double var9;

                    if (var3.sval.equals("vt"))
                    {
                        var9 = this.getfloat(var3);
                        var6 = this.getfloat(var3);
                        this.texvert.add(new TexVertex(0, var9, var6));
                        this.endline(var3);
                    }
                    else if (var3.sval.equals("vtc"))
                    {
                        var9 = this.getfloat(var3);
                        var6 = this.getfloat(var3);
                        TexVertex var8 = new TexVertex(0, var9, var6);
                        var8.r = (float)this.getfloat(var3);
                        var8.g = (float)this.getfloat(var3);
                        var8.b = (float)this.getfloat(var3);
                        this.texvert.add(var8);
                        this.endline(var3);
                    }
                    else if (var3.sval.equals("f"))
                    {
                        this.parseface(var3);
                    }
                    else if (var3.sval.equals("g"))
                    {
                        this.parsegroup(var3);
                    }
                    else
                    {
                        this.eatline(var3);
                    }
                }
            }
        }

        var1.close();
    }
}
