package com.eloraam.redpower.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.network.packet.Packet;

public abstract class PacketVLC extends Packet
{
    protected int cnt1 = 0;
    protected int size = 0;
    public ByteArrayOutputStream headout = null;
    public ByteArrayOutputStream bodyout = null;
    public ByteArrayInputStream bodyin = null;

    public PacketVLC()
    {
        this.headout = new ByteArrayOutputStream();
        this.bodyout = new ByteArrayOutputStream();
    }

    public PacketVLC(byte[] var1)
    {
        this.bodyin = new ByteArrayInputStream(var1);
    }

    public byte[] toByteArray()
    {
        try
        {
            this.bodyout.writeTo(this.headout);
        }
        catch (IOException var2)
        {
            ;
        }

        return this.headout.toByteArray();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream var1) throws IOException
    {
        this.headout.writeTo(var1);
        this.bodyout.writeTo(var1);
    }

    public void addByte(int var1)
    {
        this.bodyout.write(var1);
    }

    public void addUVLC(long var1)
    {
        writeUVLC(this.bodyout, var1);
    }

    public void addVLC(long var1)
    {
        writeVLC(this.bodyout, var1);
    }

    public void addByteArray(byte[] var1)
    {
        this.addUVLC((long)var1.length);
        this.bodyout.write(var1, 0, var1.length);
    }

    public int getByte() throws IOException
    {
        int var1 = this.bodyin.read();

        if (var1 < 0)
        {
            throw new IOException("Not enough data");
        }
        else
        {
            return var1;
        }
    }

    public long getUVLC() throws IOException
    {
        return this.readUVLC(this.bodyin);
    }

    public long getVLC() throws IOException
    {
        return this.readVLC(this.bodyin);
    }

    public byte[] getByteArray() throws IOException
    {
        int var1 = (int)this.getUVLC();
        byte[] var2 = new byte[var1];
        this.bodyin.read(var2, 0, var1);
        return var2;
    }

    protected static void writeVLC(ByteArrayOutputStream var0, long var1)
    {
        if (var1 >= 0L)
        {
            var1 <<= 1;
        }
        else
        {
            var1 = -var1 << 1 | 1L;
        }

        writeUVLC(var0, var1);
    }

    protected static void writeUVLC(ByteArrayOutputStream var0, long var1)
    {
        do
        {
            int var3 = (int)(var1 & 127L);
            var1 >>>= 7;

            if (var1 != 0L)
            {
                var3 |= 128;
            }

            var0.write(var3);
        }
        while (var1 != 0L);
    }

    protected long readUVLC(InputStream var1) throws IOException
    {
        long var2 = 0L;
        int var5 = 0;

        do
        {
            int var4 = var1.read();

            if (var4 < 0)
            {
                throw new IOException("Not enough data");
            }

            ++this.cnt1;
            var2 |= (long)((var4 & 127) << var5);

            if ((var4 & 128) == 0)
            {
                return var2;
            }

            var5 += 7;
        }
        while (var5 <= 64);

        throw new IOException("Bad VLC");
    }

    protected long readVLC(InputStream var1) throws IOException
    {
        long var2 = this.readUVLC(var1);

        if ((var2 & 1L) == 0L)
        {
            var2 >>>= 1;
        }
        else
        {
            var2 = -(var2 >>> 1);
        }

        return var2;
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return this.size;
    }

    protected void fixLocalPacket()
    {
        this.bodyin = new ByteArrayInputStream(this.bodyout.toByteArray());
    }
}
