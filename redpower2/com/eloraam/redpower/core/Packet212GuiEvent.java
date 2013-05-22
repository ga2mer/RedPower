package com.eloraam.redpower.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.network.packet.NetHandler;

public class Packet212GuiEvent extends PacketVLC
{
    public int eventId;
    public int windowId;
    private int cnt1 = 0;
    private int size = 0;

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream var1) throws IOException
    {
        this.windowId = var1.read();

        if (this.windowId == -1)
        {
            throw new IOException("Not enough data");
        }
        else
        {
            this.eventId = (int)this.readUVLC(var1);
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
        this.headout.write(this.windowId);
        writeUVLC(this.headout, (long)this.eventId);
        writeUVLC(this.headout, (long)this.bodyout.size());
        this.size = this.headout.size() + this.bodyout.size();
        this.fixLocalPacket();
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler var1)
    {
        CoreProxy.instance.processPacket212(this, var1);
    }
}
