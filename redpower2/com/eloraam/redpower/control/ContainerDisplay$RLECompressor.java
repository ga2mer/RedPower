package com.eloraam.redpower.control;

import java.io.ByteArrayOutputStream;

public class ContainerDisplay$RLECompressor
{
    ByteArrayOutputStream bas;
    byte[] datbuf;
    byte srledat;
    int rleoffs;
    int srleoffs;
    int datpos;
    boolean changed;

    final ContainerDisplay this$0;

    public ContainerDisplay$RLECompressor(ContainerDisplay var1)
    {
        this.this$0 = var1;
        this.bas = new ByteArrayOutputStream();
        this.datbuf = new byte[256];
        this.srledat = 0;
        this.rleoffs = 0;
        this.srleoffs = 0;
        this.datpos = 0;
        this.changed = false;
    }

    public void writeRLE()
    {
        this.bas.write((byte)this.rleoffs);
        this.datpos = 0;
        this.rleoffs = 0;
        this.srleoffs = 0;
    }

    public void writeSRLE()
    {
        this.bas.write(-1);
        this.bas.write((byte)this.srleoffs);
        this.bas.write(this.srledat);
        this.datpos = 0;
        this.rleoffs = 0;
        this.srleoffs = 0;
    }

    public void writeDat(int var1)
    {
        if (var1 != 0)
        {
            this.bas.write((byte)(128 | var1));
            this.bas.write(this.datbuf, 0, var1);
            this.datpos -= var1;
        }
    }

    public void addByte(byte var1, boolean var2)
    {
        if (var2)
        {
            this.changed = true;

            if (this.rleoffs > 5 && this.rleoffs >= this.srleoffs)
            {
                this.writeDat(this.datpos - this.rleoffs);
                this.writeRLE();
            }

            this.rleoffs = 0;
        }
        else
        {
            ++this.rleoffs;

            if (this.rleoffs >= 127)
            {
                ++this.datpos;
                this.writeDat(this.datpos - this.rleoffs);
                this.writeRLE();
                return;
            }
        }

        if (this.srleoffs == 0)
        {
            this.srledat = var1;
            this.srleoffs = 1;
        }
        else if (var1 == this.srledat)
        {
            ++this.srleoffs;

            if (this.srleoffs >= 127)
            {
                ++this.datpos;
                this.writeDat(this.datpos - this.srleoffs);
                this.writeSRLE();
                return;
            }
        }
        else
        {
            if (this.srleoffs > 5 && this.srleoffs >= this.rleoffs)
            {
                this.writeDat(this.datpos - this.srleoffs);
                this.writeSRLE();
            }

            this.srledat = var1;
            this.srleoffs = 1;
        }

        this.datbuf[this.datpos] = var1;
        ++this.datpos;
        int var3 = Math.max(this.srleoffs, this.rleoffs);

        if (var3 <= 5 && this.datpos >= 126)
        {
            this.writeDat(this.datpos);
            this.srleoffs = 0;
            this.rleoffs = 0;
        }
        else if (this.datpos - var3 >= 126)
        {
            this.writeDat(this.datpos - var3);
        }
    }

    public void flush()
    {
        this.datpos -= this.rleoffs;
        this.srleoffs = Math.max(0, this.srleoffs - this.rleoffs);

        if (this.datpos != 0)
        {
            if (this.srleoffs > 5)
            {
                this.writeDat(this.datpos - this.srleoffs);
                this.writeSRLE();
            }
            else
            {
                this.writeDat(this.datpos);
            }
        }
    }

    byte[] getByteArray()
    {
        return !this.changed ? null : this.bas.toByteArray();
    }
}
