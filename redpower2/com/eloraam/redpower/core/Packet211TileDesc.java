package com.eloraam.redpower.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.network.packet.NetHandler;

public class Packet211TileDesc extends PacketVLC
{
    public int subId;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    private int cnt1 = 0;
    private int size = 0;

    public Packet211TileDesc()
    {
        this.isChunkDataPacket = true;
    }

    public Packet211TileDesc(byte[] var1)
    {
        this.bodyin = new ByteArrayInputStream(var1);
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream var1) throws IOException
    {
        this.subId = var1.read();

        if (this.subId == -1)
        {
            throw new IOException("Not enough data");
        }
        else
        {
            this.xCoord = (int)this.readVLC(var1);
            this.yCoord = (int)this.readVLC(var1);
            this.zCoord = (int)this.readVLC(var1);
            int var2 = (int)this.readUVLC(var1);

            if (var2 > 65535)
            {
                throw new IOException("Packet too big");
            }
            else
            {
                this.size = this.cnt1 + var2 + 1;
                byte[] var3 = new byte[var2];
                int var4;

                for (int var5 = 0; var2 - var5 > 0; var5 += var4)
                {
                    var4 = var1.read(var3, var5, var2 - var5);

                    if (var4 < 1)
                    {
                        throw new IOException("Not enough data");
                    }

                    if (var5 + var4 >= var2)
                    {
                        break;
                    }
                }

                this.bodyin = new ByteArrayInputStream(var3);
            }
        }
    }

    public void encode()
    {
        this.headout.write(this.subId);
        writeVLC(this.headout, (long)this.xCoord);
        writeVLC(this.headout, (long)this.yCoord);
        writeVLC(this.headout, (long)this.zCoord);
        writeUVLC(this.headout, (long)this.bodyout.size());
        this.size = this.headout.size() + this.bodyout.size();
        this.fixLocalPacket();
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler var1)
    {
        CoreProxy.instance.processPacket211(this, var1);
    }
}
