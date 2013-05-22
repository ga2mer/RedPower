package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerControl;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.IFrameSupport;
import com.eloraam.redpower.core.IHandlePackets;
import com.eloraam.redpower.core.IRedbusConnectable;
import com.eloraam.redpower.core.Packet211TileDesc;
import com.eloraam.redpower.core.RedbusLib;
import com.eloraam.redpower.core.TileExtended;
import com.eloraam.redpower.core.WorldCoord;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.IBlockAccess;

public class TileCPU extends TileExtended implements IRedbusConnectable, IHandlePackets, IFrameSupport
{
    public int Rotation = 0;
    public byte[] memory = new byte[8192];
    int addrPOR;
    int addrBRK;
    int regSP;
    int regPC;
    int regA;
    int regB;
    int regX;
    int regY;
    int regR;
    int regI;
    int regD;
    boolean flagC;
    boolean flagZ;
    boolean flagID;
    boolean flagD;
    boolean flagBRK;
    boolean flagO;
    boolean flagN;
    boolean flagE;
    boolean flagM;
    boolean flagX;
    int mmuRBB = 0;
    int mmuRBA = 0;
    int mmuRBW = 0;
    boolean mmuEnRB = false;
    boolean mmuEnRBW = false;
    private boolean rbTimeout = false;
    private boolean waiTimeout = false;
    public int sliceCycles = -1;
    IRedbusConnectable rbCache = null;
    public int rtcTicks = 0;
    public int byte0 = 2;
    public int byte1 = 1;
    public int rbaddr = 0;
    TileBackplane[] backplane = new TileBackplane[7];

    public TileCPU()
    {
        this.coldBootCPU();
    }

    public void coldBootCPU()
    {
        this.addrPOR = 8192;
        this.addrBRK = 8192;
        this.regSP = 512;
        this.regPC = 1024;
        this.regR = 768;
        this.regA = 0;
        this.regX = 0;
        this.regY = 0;
        this.regD = 0;
        this.flagC = false;
        this.flagZ = false;
        this.flagID = false;
        this.flagD = false;
        this.flagBRK = false;
        this.flagO = false;
        this.flagN = false;
        this.flagE = true;
        this.flagM = true;
        this.flagX = true;
        this.memory[0] = (byte)this.byte0;
        this.memory[1] = (byte)this.byte1;
        InputStream var1 = RedPowerControl.class.getResourceAsStream("/eloraam/control/rpcboot.bin");

        try
        {
            try
            {
                var1.read(this.memory, 1024, 256);
            }
            finally
            {
                if (var1 != null)
                {
                    var1.close();
                }
            }
        }
        catch (IOException var6)
        {
            var6.printStackTrace();
        }

        this.sliceCycles = -1;
    }

    public void warmBootCPU()
    {
        if (this.sliceCycles >= 0)
        {
            this.regSP = 512;
            this.regR = 768;
            this.regPC = this.addrPOR;
        }

        this.sliceCycles = 0;
    }

    public void haltCPU()
    {
        this.sliceCycles = -1;
    }

    public boolean isRunning()
    {
        return this.sliceCycles >= 0;
    }

    public int rbGetAddr()
    {
        return this.rbaddr;
    }

    public void rbSetAddr(int var1) {}

    public int rbRead(int var1)
    {
        return !this.mmuEnRBW ? 0 : this.readOnlyMem(this.mmuRBW + var1);
    }

    public void rbWrite(int var1, int var2)
    {
        if (this.mmuEnRBW)
        {
            this.writeOnlyMem(this.mmuRBW + var1, var2);
        }
    }

    public int getConnectableMask()
    {
        return 16777215;
    }

    public int getConnectClass(int var1)
    {
        return 66;
    }

    public int getCornerPowerMode()
    {
        return 0;
    }

    public void onBlockPlaced(ItemStack var1, int var2, EntityLiving var3)
    {
        this.Rotation = (int)Math.floor((double)(var3.rotationYaw * 4.0F / 360.0F) + 0.5D) + 1 & 3;
    }

    public boolean onBlockActivated(EntityPlayer var1)
    {
        if (var1.isSneaking())
        {
            return false;
        }
        else if (CoreLib.isClient(this.worldObj))
        {
            return true;
        }
        else
        {
            var1.openGui(RedPowerControl.instance, 2, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
    }

    public int getBlockID()
    {
        return RedPowerControl.blockPeripheral.blockID;
    }

    public int getExtendedID()
    {
        return 1;
    }

    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    protected void refreshBackplane()
    {
        boolean var1 = true;
        WorldCoord var2 = new WorldCoord(this);

        for (int var3 = 0; var3 < 7; ++var3)
        {
            if (!var1)
            {
                this.backplane[var3] = null;
            }
            else
            {
                var2.step(CoreLib.rotToSide(this.Rotation));
                TileBackplane var4 = (TileBackplane)CoreLib.getTileEntity(this.worldObj, var2, TileBackplane.class);
                this.backplane[var3] = var4;

                if (var4 == null)
                {
                    var1 = false;
                }
            }
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        ++this.rtcTicks;

        if (this.sliceCycles >= 0)
        {
            this.rbTimeout = false;
            this.rbCache = null;
            this.waiTimeout = false;
            this.sliceCycles += 1000;

            if (this.sliceCycles > 100000)
            {
                this.sliceCycles = 100000;
            }

            this.refreshBackplane();

            while (this.sliceCycles > 0 && !this.waiTimeout && !this.rbTimeout)
            {
                --this.sliceCycles;
                this.executeInsn();
            }
        }
    }

    protected int readOnlyMem(int var1)
    {
        var1 &= 65535;

        if (var1 < 8192)
        {
            return this.memory[var1] & 255;
        }
        else
        {
            int var2 = (var1 >> 13) - 1;
            return this.backplane[var2] == null ? 255 : this.backplane[var2].readBackplane(var1 & 8191);
        }
    }

    public int readMem(int var1)
    {
        if (this.mmuEnRB && var1 >= this.mmuRBB && var1 < this.mmuRBB + 256)
        {
            if (this.rbCache == null)
            {
                this.rbCache = RedbusLib.getAddr(this.worldObj, new WorldCoord(this), this.mmuRBA);
            }

            if (this.rbCache == null)
            {
                this.rbTimeout = true;
                return 0;
            }
            else
            {
                int var2 = this.rbCache.rbRead(var1 - this.mmuRBB);
                return var2;
            }
        }
        else
        {
            return this.readOnlyMem(var1);
        }
    }

    protected void writeOnlyMem(int var1, int var2)
    {
        var1 &= 65535;

        if (var1 < 8192)
        {
            this.memory[var1] = (byte)var2;
        }
        else
        {
            int var3 = (var1 >> 13) - 1;

            if (this.backplane[var3] != null)
            {
                this.backplane[var3].writeBackplane(var1 & 8191, var2);
            }
        }
    }

    public void writeMem(int var1, int var2)
    {
        if (this.mmuEnRB && var1 >= this.mmuRBB && var1 < this.mmuRBB + 256)
        {
            if (this.rbCache == null)
            {
                this.rbCache = RedbusLib.getAddr(this.worldObj, new WorldCoord(this), this.mmuRBA);
            }

            if (this.rbCache == null)
            {
                this.rbTimeout = true;
            }
            else
            {
                this.rbCache.rbWrite(var1 - this.mmuRBB, var2 & 255);
            }
        }
        else
        {
            this.writeOnlyMem(var1, var2);
        }
    }

    private void incPC()
    {
        this.regPC = this.regPC + 1 & 65535;
    }

    private int maskM()
    {
        return this.flagM ? 255 : 65535;
    }

    private int maskX()
    {
        return this.flagX ? 255 : 65535;
    }

    private int negM()
    {
        return this.flagM ? 128 : 32768;
    }

    private int negX()
    {
        return this.flagX ? 128 : 32768;
    }

    private int readB()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        return var1;
    }

    private int readM()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();

        if (!this.flagM)
        {
            var1 |= this.readMem(this.regPC) << 8;
            this.incPC();
        }

        return var1;
    }

    private int readX()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();

        if (!this.flagX)
        {
            var1 |= this.readMem(this.regPC) << 8;
            this.incPC();
        }

        return var1;
    }

    private int readM(int var1)
    {
        int var2 = this.readMem(var1);

        if (!this.flagM)
        {
            var2 |= this.readMem(var1 + 1) << 8;
        }

        return var2;
    }

    private int readX(int var1)
    {
        int var2 = this.readMem(var1);

        if (!this.flagX)
        {
            var2 |= this.readMem(var1 + 1) << 8;
        }

        return var2;
    }

    private void writeM(int var1, int var2)
    {
        this.writeMem(var1, var2);

        if (!this.flagM)
        {
            this.writeMem(var1 + 1, var2 >> 8);
        }
    }

    private void writeX(int var1, int var2)
    {
        this.writeMem(var1, var2);

        if (!this.flagX)
        {
            this.writeMem(var1 + 1, var2 >> 8);
        }
    }

    private int readBX()
    {
        int var1 = this.readMem(this.regPC) + this.regX;

        if (this.flagX)
        {
            var1 &= 255;
        }

        this.incPC();
        return var1;
    }

    private int readBY()
    {
        int var1 = this.readMem(this.regPC) + this.regY;

        if (this.flagX)
        {
            var1 &= 255;
        }

        this.incPC();
        return var1;
    }

    private int readBS()
    {
        int var1 = this.readMem(this.regPC) + this.regSP & 65535;
        this.incPC();
        return var1;
    }

    private int readBR()
    {
        int var1 = this.readMem(this.regPC) + this.regR & 65535;
        this.incPC();
        return var1;
    }

    private int readBSWY()
    {
        int var1 = this.readMem(this.regPC) + this.regSP & 65535;
        this.incPC();
        return this.readW(var1) + this.regY & 65535;
    }

    private int readBRWY()
    {
        int var1 = this.readMem(this.regPC) + this.regR & 65535;
        this.incPC();
        return this.readW(var1) + this.regY & 65535;
    }

    private int readW()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        var1 |= this.readMem(this.regPC) << 8;
        this.incPC();
        return var1;
    }

    private int readW(int var1)
    {
        int var2 = this.readMem(var1);
        var2 |= this.readMem(var1 + 1) << 8;
        return var2;
    }

    private int readWX()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        var1 |= this.readMem(this.regPC) << 8;
        this.incPC();
        return var1 + this.regX & 65535;
    }

    private int readWY()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        var1 |= this.readMem(this.regPC) << 8;
        this.incPC();
        return var1 + this.regY & 65535;
    }

    private int readWXW()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        var1 |= this.readMem(this.regPC) << 8;
        this.incPC();
        var1 = var1 + this.regX & 65535;
        int var2 = this.readMem(var1);
        var2 |= this.readMem(var1 + 1) << 8;
        return var2;
    }

    private int readBW()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        int var2 = this.readMem(var1);
        var2 |= this.readMem(var1 + 1) << 8;
        return var2;
    }

    private int readWW()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        var1 |= this.readMem(this.regPC) << 8;
        this.incPC();
        int var2 = this.readMem(var1);
        var2 |= this.readMem(var1 + 1) << 8;
        return var2;
    }

    private int readBXW()
    {
        int var1 = this.readMem(this.regPC) + this.regX & 255;
        this.incPC();
        int var2 = this.readMem(var1);
        var2 |= this.readMem(var1 + 1) << 8;
        return var2;
    }

    private int readBWY()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        int var2 = this.readMem(var1);
        var2 |= this.readMem(var1 + 1) << 8;
        return var2 + this.regY & 65535;
    }

    private void upNZ()
    {
        this.flagN = (this.regA & this.negM()) > 0;
        this.flagZ = this.regA == 0;
    }

    private void upNZ(int var1)
    {
        this.flagN = (var1 & this.negM()) > 0;
        this.flagZ = var1 == 0;
    }

    private void upNZX(int var1)
    {
        this.flagN = (var1 & this.negX()) > 0;
        this.flagZ = var1 == 0;
    }

    private void push1(int var1)
    {
        if (this.flagE)
        {
            this.regSP = this.regSP - 1 & 255 | this.regSP & 65280;
        }
        else
        {
            this.regSP = this.regSP - 1 & 65535;
        }

        this.writeMem(this.regSP, var1);
    }

    private void push1r(int var1)
    {
        this.regR = this.regR - 1 & 65535;
        this.writeMem(this.regR, var1);
    }

    private void push2(int var1)
    {
        this.push1(var1 >> 8);
        this.push1(var1 & 255);
    }

    private void push2r(int var1)
    {
        this.push1r(var1 >> 8);
        this.push1r(var1 & 255);
    }

    private void pushM(int var1)
    {
        if (this.flagM)
        {
            this.push1(var1);
        }
        else
        {
            this.push2(var1);
        }
    }

    private void pushX(int var1)
    {
        if (this.flagX)
        {
            this.push1(var1);
        }
        else
        {
            this.push2(var1);
        }
    }

    private void pushMr(int var1)
    {
        if (this.flagM)
        {
            this.push1r(var1);
        }
        else
        {
            this.push2r(var1);
        }
    }

    private void pushXr(int var1)
    {
        if (this.flagX)
        {
            this.push1r(var1);
        }
        else
        {
            this.push2r(var1);
        }
    }

    private int pop1()
    {
        int var1 = this.readMem(this.regSP);

        if (this.flagE)
        {
            this.regSP = this.regSP + 1 & 255 | this.regSP & 65280;
        }
        else
        {
            this.regSP = this.regSP + 1 & 65535;
        }

        return var1;
    }

    private int pop1r()
    {
        int var1 = this.readMem(this.regR);
        this.regR = this.regR + 1 & 65535;
        return var1;
    }

    private int pop2()
    {
        int var1 = this.pop1();
        var1 |= this.pop1() << 8;
        return var1;
    }

    private int pop2r()
    {
        int var1 = this.pop1r();
        var1 |= this.pop1r() << 8;
        return var1;
    }

    private int popM()
    {
        return this.flagM ? this.pop1() : this.pop2();
    }

    private int popMr()
    {
        return this.flagM ? this.pop1r() : this.pop2r();
    }

    private int popX()
    {
        return this.flagX ? this.pop1() : this.pop2();
    }

    private int popXr()
    {
        return this.flagX ? this.pop1r() : this.pop2r();
    }

    private int getFlags()
    {
        return (this.flagC ? 1 : 0) | (this.flagZ ? 2 : 0) | (this.flagID ? 4 : 0) | (this.flagD ? 8 : 0) | (this.flagX ? 16 : 0) | (this.flagM ? 32 : 0) | (this.flagO ? 64 : 0) | (this.flagN ? 128 : 0);
    }

    private void setFlags(int var1)
    {
        this.flagC = (var1 & 1) > 0;
        this.flagZ = (var1 & 2) > 0;
        this.flagID = (var1 & 4) > 0;
        this.flagD = (var1 & 8) > 0;
        boolean var2 = (var1 & 32) > 0;
        this.flagO = (var1 & 64) > 0;
        this.flagN = (var1 & 128) > 0;

        if (this.flagE)
        {
            this.flagX = false;
            this.flagM = false;
        }
        else
        {
            this.flagX = (var1 & 16) > 0;

            if (this.flagX)
            {
                this.regX &= 255;
                this.regY &= 255;
            }

            if (var2 != this.flagM)
            {
                if (var2)
                {
                    this.regB = this.regA >> 8;
                    this.regA &= 255;
                }
                else
                {
                    this.regA |= this.regB << 8;
                }

                this.flagM = var2;
            }
        }
    }

    private void i_adc(int var1)
    {
        int var2;

        if (this.flagM)
        {
            if (this.flagD)
            {
                var2 = (this.regA & 15) + (var1 & 15) + (this.flagC ? 1 : 0);

                if (var2 > 9)
                {
                    var2 = (var2 + 6 & 15) + 16;
                }

                int var3 = (this.regA & 240) + (var1 & 240) + var2;

                if (var3 > 160)
                {
                    var3 += 96;
                }

                this.flagC = var3 > 100;
                this.regA = var3 & 255;
                this.flagO = false;
            }
            else
            {
                var2 = this.regA + var1 + (this.flagC ? 1 : 0);
                this.flagC = var2 > 255;
                this.flagO = ((var2 ^ this.regA) & (var2 ^ var1) & 128) > 0;
                this.regA = var2 & 255;
            }
        }
        else
        {
            var2 = this.regA + var1 + (this.flagC ? 1 : 0);
            this.flagC = var2 > 65535;
            this.flagO = ((var2 ^ this.regA) & (var2 ^ var1) & 32768) > 0;
            this.regA = var2 & 65535;
        }

        this.upNZ();
    }

    private void i_sbc(int var1)
    {
        int var2;

        if (this.flagM)
        {
            if (this.flagD)
            {
                var2 = (this.regA & 15) - (var1 & 15) + (this.flagC ? 1 : 0) - 1;

                if (var2 < 0)
                {
                    var2 = (var2 - 6 & 15) - 16;
                }

                int var3 = (this.regA & 240) - (var1 & 240) + var2;

                if (var3 < 0)
                {
                    var3 -= 96;
                }

                this.flagC = var3 < 100;
                this.regA = var3 & 255;
                this.flagO = false;
            }
            else
            {
                var2 = this.regA - var1 + (this.flagC ? 1 : 0) - 1;
                this.flagC = (var2 & 256) == 0;
                this.flagO = ((var2 ^ this.regA) & (var2 ^ -var1) & 128) > 0;
                this.regA = var2 & 255;
            }
        }
        else
        {
            var2 = this.regA - var1 + (this.flagC ? 1 : 0) - 1;
            this.flagC = (var2 & 65536) == 0;
            this.flagO = ((var2 ^ this.regA) & (var2 ^ -var1) & 32768) > 0;
            this.regA = var2 & 65535;
        }

        this.upNZ();
    }

    private void i_mul(int var1)
    {
        if (this.flagM)
        {
            int var2;

            if (this.flagC)
            {
                var2 = (byte)var1 * (byte)this.regA;
            }
            else
            {
                var2 = var1 * this.regA;
            }

            this.regA = var2 & 255;
            this.regD = var2 >> 8 & 255;
            this.flagN = var2 < 0;
            this.flagZ = var2 == 0;
            this.flagO = this.regD != 0 && this.regD != 255;
        }
        else
        {
            long var4;

            if (this.flagC)
            {
                var4 = (long)((short)var1 * (short)this.regA);
            }
            else
            {
                var4 = (long)(var1 * this.regA);
            }

            this.regA = (int)(var4 & 65535L);
            this.regD = (int)(var4 >> 16 & 65535L);
            this.flagN = var4 < 0L;
            this.flagZ = var4 == 0L;
            this.flagO = this.regD != 0 && this.regD != 65535;
        }
    }

    private void i_div(int var1)
    {
        if (var1 == 0)
        {
            this.regA = 0;
            this.regD = 0;
            this.flagO = true;
            this.flagZ = false;
            this.flagN = false;
        }
        else
        {
            int var2;

            if (this.flagM)
            {
                if (this.flagC)
                {
                    var2 = (byte)this.regD << 8 | this.regA;
                    var1 = (byte)var1;
                }
                else
                {
                    var2 = this.regD << 8 | this.regA;
                }

                this.regD = var2 % var1 & 255;
                var2 /= var1;
                this.regA = var2 & 255;

                if (this.flagC)
                {
                    this.flagO = var2 > 127 || var2 < -128;
                }
                else
                {
                    this.flagO = var2 > 255;
                }

                this.flagZ = this.regA == 0;
                this.flagN = var2 < 0;
            }
            else if (this.flagC)
            {
                var2 = (short)this.regD << 16 | this.regA;
                short var4 = (short)var1;
                this.regD = var2 % var4 & 65535;
                var2 /= var4;
                this.regA = var2 & 65535;
                this.flagO = var2 > 32767 || var2 < -32768;
                this.flagZ = this.regA == 0;
                this.flagN = var2 < 0;
            }
            else
            {
                long var5 = (long)(this.regD << 16 | this.regA);
                this.regD = (int)(var5 % (long)var1 & 65535L);
                var5 /= (long)var1;
                this.regA = (int)(var5 & 65535L);
                this.flagO = var5 > 65535L;
                this.flagZ = this.regA == 0;
                this.flagN = var5 < 0L;
            }
        }
    }

    private void i_and(int var1)
    {
        this.regA &= var1;
        this.upNZ();
    }

    private void i_asl(int var1)
    {
        int var2 = this.readM(var1);
        this.flagC = (var2 & this.negM()) > 0;
        var2 = var2 << 1 & this.maskM();
        this.upNZ(var2);
        this.writeM(var1, var2);
    }

    private void i_lsr(int var1)
    {
        int var2 = this.readM(var1);
        this.flagC = (var2 & 1) > 0;
        var2 >>>= 1;
        this.upNZ(var2);
        this.writeM(var1, var2);
    }

    private void i_rol(int var1)
    {
        int var2 = this.readM(var1);
        int var3 = (var2 << 1 | (this.flagC ? 1 : 0)) & this.maskM();
        this.flagC = (var2 & this.negM()) > 0;
        this.upNZ(var3);
        this.writeM(var1, var3);
    }

    private void i_ror(int var1)
    {
        int var2 = this.readM(var1);
        int var3 = var2 >>> 1 | (this.flagC ? this.negM() : 0);
        this.flagC = (var2 & 1) > 0;
        this.upNZ(var3);
        this.writeM(var1, var3);
    }

    private void i_brc(boolean var1)
    {
        int var2 = this.readB();

        if (var1)
        {
            this.regPC = this.regPC + (byte)var2 & 65535;
        }
    }

    private void i_bit(int var1)
    {
        if (this.flagM)
        {
            this.flagO = (var1 & 64) > 0;
            this.flagN = (var1 & 128) > 0;
        }
        else
        {
            this.flagO = (var1 & 16384) > 0;
            this.flagN = (var1 & 32768) > 0;
        }

        this.flagZ = (var1 & this.regA) > 0;
    }

    private void i_trb(int var1)
    {
        this.flagZ = (var1 & this.regA) > 0;
        this.regA &= ~var1;
    }

    private void i_tsb(int var1)
    {
        this.flagZ = (var1 & this.regA) > 0;
        this.regA |= var1;
    }

    private void i_cmp(int var1, int var2)
    {
        var1 -= var2;
        this.flagC = var1 >= 0;
        this.flagZ = var1 == 0;
        this.flagN = (var1 & this.negM()) > 0;
    }

    private void i_cmpx(int var1, int var2)
    {
        var1 -= var2;
        this.flagC = var1 >= 0;
        this.flagZ = var1 == 0;
        this.flagN = (var1 & this.negX()) > 0;
    }

    private void i_dec(int var1)
    {
        int var2 = this.readM(var1);
        var2 = var2 - 1 & this.maskM();
        this.writeM(var1, var2);
        this.upNZ(var2);
    }

    private void i_inc(int var1)
    {
        int var2 = this.readM(var1);
        var2 = var2 + 1 & this.maskM();
        this.writeM(var1, var2);
        this.upNZ(var2);
    }

    private void i_eor(int var1)
    {
        this.regA ^= var1;
        this.upNZ();
    }

    private void i_or(int var1)
    {
        this.regA |= var1;
        this.upNZ();
    }

    private void i_mmu(int var1)
    {
        switch (var1)
        {
            case 0:
                int var2 = this.regA & 255;

                if (this.mmuRBA != var2)
                {
                    if (this.rbCache != null)
                    {
                        this.rbTimeout = true;
                    }

                    this.mmuRBA = var2;
                }

                break;

            case 1:
                this.mmuRBB = this.regA;
                break;

            case 2:
                this.mmuEnRB = true;
                break;

            case 3:
                this.mmuRBW = this.regA;
                break;

            case 4:
                this.mmuEnRBW = true;
                break;

            case 5:
                this.addrBRK = this.regA;
                break;

            case 6:
                this.addrPOR = this.regA;
                break;

            case 128:
                this.regA = this.mmuRBA;
                break;

            case 129:
                this.regA = this.mmuRBB;

                if (this.flagM)
                {
                    this.regB = this.regA >> 8;
                    this.regA &= 255;
                }

                break;

            case 130:
                this.mmuEnRB = false;
                break;

            case 131:
                this.regA = this.mmuRBW;

                if (this.flagM)
                {
                    this.regB = this.regA >> 8;
                    this.regA &= 255;
                }

                break;

            case 132:
                this.mmuEnRBW = false;
                break;

            case 133:
                this.regA = this.addrBRK;

                if (this.flagM)
                {
                    this.regB = this.regA >> 8;
                    this.regA &= 255;
                }

                break;

            case 134:
                this.regA = this.addrPOR;

                if (this.flagM)
                {
                    this.regB = this.regA >> 8;
                    this.regA &= 255;
                }

                break;

            case 135:
                this.regA = this.rtcTicks & 65535;
                this.regD = this.rtcTicks >> 16 & 65535;
        }
    }

    public void executeInsn()
    {
        int var1 = this.readMem(this.regPC);
        this.incPC();
        int var2;

        switch (var1)
        {
            case 0:
                this.push2(this.regPC);
                this.push1(this.getFlags());
                this.flagBRK = true;
                this.regPC = this.addrBRK;
                break;

            case 1:
                this.i_or(this.readM(this.readBXW()));
                break;

            case 2:
                this.regPC = this.readW(this.regI);
                this.regI += 2;
                break;

            case 3:
                this.i_or(this.readM(this.readBS()));
                break;

            case 4:
                this.i_tsb(this.readM(this.readB()));
                break;

            case 5:
                this.i_or(this.readM(this.readB()));
                break;

            case 6:
                this.i_asl(this.readB());
                break;

            case 7:
                this.i_or(this.readM(this.readBR()));
                break;

            case 8:
                this.push1(this.getFlags());
                break;

            case 9:
                this.i_or(this.readM());
                break;

            case 10:
                this.flagC = (this.regA & this.negM()) > 0;
                this.regA = this.regA << 1 & this.maskM();
                this.upNZ();
                break;

            case 11:
                this.push2r(this.regI);
                break;

            case 12:
                this.i_tsb(this.readM(this.readW()));
                break;

            case 13:
                this.i_or(this.readM(this.readW()));
                break;

            case 14:
                this.i_asl(this.readW());
                break;

            case 15:
                this.i_mul(this.readM(this.readB()));
                break;

            case 16:
                this.i_brc(!this.flagN);
                break;

            case 17:
                this.i_or(this.readM(this.readBWY()));
                break;

            case 18:
                this.i_or(this.readM(this.readBW()));
                break;

            case 19:
                this.i_or(this.readM(this.readBSWY()));
                break;

            case 20:
                this.i_trb(this.readM(this.readB()));
                break;

            case 21:
                this.i_or(this.readM(this.readBX()));
                break;

            case 22:
                this.i_asl(this.readBX());
                break;

            case 23:
                this.i_or(this.readM(this.readBRWY()));
                break;

            case 24:
                this.flagC = false;
                break;

            case 25:
                this.i_or(this.readM(this.readWY()));
                break;

            case 26:
                this.regA = this.regA + 1 & this.maskM();
                this.upNZ(this.regA);
                break;

            case 27:
                this.pushXr(this.regX);
                break;

            case 28:
                this.i_trb(this.readM(this.readW()));
                break;

            case 29:
                this.i_or(this.readM(this.readWX()));
                break;

            case 30:
                this.i_asl(this.readWX());
                break;

            case 31:
                this.i_mul(this.readM(this.readBX()));
                break;

            case 32:
                this.push2(this.regPC + 1);
                this.regPC = this.readW();
                break;

            case 33:
                this.i_and(this.readM(this.readBXW()));
                break;

            case 34:
                this.push2r(this.regI);
                this.regI = this.regPC + 2;
                this.regPC = this.readW(this.regPC);
                break;

            case 35:
                this.i_and(this.readM(this.readBS()));
                break;

            case 36:
                this.i_bit(this.readM(this.readB()));
                break;

            case 37:
                this.i_and(this.readM(this.readB()));
                break;

            case 38:
                this.i_rol(this.readB());
                break;

            case 39:
                this.i_and(this.readM(this.readBR()));
                break;

            case 40:
                this.setFlags(this.pop1());
                break;

            case 41:
                this.i_and(this.readM());
                break;

            case 42:
                var2 = (this.regA << 1 | (this.flagC ? 1 : 0)) & this.maskM();
                this.flagC = (this.regA & this.negM()) > 0;
                this.regA = var2;
                this.upNZ();
                break;

            case 43:
                this.regI = this.pop2r();
                this.upNZX(this.regI);
                break;

            case 44:
                this.i_bit(this.readM(this.readW()));
                break;

            case 45:
                this.i_and(this.readM(this.readW()));
                break;

            case 46:
                this.i_rol(this.readW());
                break;

            case 47:
                this.i_mul(this.readM(this.readW()));
                break;

            case 48:
                this.i_brc(this.flagN);
                break;

            case 49:
                this.i_and(this.readM(this.readBWY()));
                break;

            case 50:
                this.i_and(this.readM(this.readBW()));
                break;

            case 51:
                this.i_and(this.readM(this.readBSWY()));
                break;

            case 52:
                this.i_bit(this.readM(this.readBX()));
                break;

            case 53:
                this.i_and(this.readM(this.readBX()));
                break;

            case 54:
                this.i_rol(this.readBX());
                break;

            case 55:
                this.i_and(this.readM(this.readBRWY()));
                break;

            case 56:
                this.flagC = true;
                break;

            case 57:
                this.i_and(this.readM(this.readWY()));
                break;

            case 58:
                this.regA = this.regA - 1 & this.maskM();
                this.upNZ(this.regA);
                break;

            case 59:
                this.regX = this.popXr();
                this.upNZX(this.regX);
                break;

            case 60:
                this.i_bit(this.readM(this.readWX()));
                break;

            case 61:
                this.i_and(this.readM(this.readWX()));
                break;

            case 62:
                this.i_rol(this.readWX());
                break;

            case 63:
                this.i_mul(this.readM(this.readWX()));
                break;

            case 64:
                this.setFlags(this.pop1());
                this.regPC = this.pop2();
                break;

            case 65:
                this.i_eor(this.readM(this.readBXW()));
                break;

            case 66:
                if (this.flagM)
                {
                    this.regA = this.readMem(this.regI);
                    ++this.regI;
                }
                else
                {
                    this.regA = this.readW(this.regI);
                    this.regI += 2;
                }

                break;

            case 67:
                this.i_eor(this.readM(this.readBS()));
                break;

            case 68:
                this.push2r(this.readW());
                break;

            case 69:
                this.i_eor(this.readM(this.readB()));
                break;

            case 70:
                this.i_lsr(this.readB());
                break;

            case 71:
                this.i_eor(this.readM(this.readBR()));
                break;

            case 72:
                this.pushM(this.regA);
                break;

            case 73:
                this.i_eor(this.readM());
                break;

            case 74:
                this.flagC = (this.regA & 1) > 0;
                this.regA >>>= 1;
                this.upNZ();
                break;

            case 75:
                this.pushMr(this.regA);
                break;

            case 76:
                this.regPC = this.readW();
                break;

            case 77:
                this.i_eor(this.readM(this.readW()));
                break;

            case 78:
                this.i_lsr(this.readW());
                break;

            case 79:
                this.i_div(this.readM(this.readB()));
                break;

            case 80:
                this.i_brc(!this.flagO);
                break;

            case 81:
                this.i_eor(this.readM(this.readBWY()));
                break;

            case 82:
                this.i_eor(this.readM(this.readBW()));
                break;

            case 83:
                this.i_eor(this.readM(this.readBSWY()));
                break;

            case 84:
                this.push2r(this.readBW());
                break;

            case 85:
                this.i_eor(this.readM(this.readBX()));
                break;

            case 86:
                this.i_lsr(this.readBX());
                break;

            case 87:
                this.i_eor(this.readM(this.readBRWY()));
                break;

            case 88:
                this.flagID = false;
                break;

            case 89:
                this.i_eor(this.readM(this.readWY()));
                break;

            case 90:
                this.pushX(this.regY);
                break;

            case 91:
                this.pushXr(this.regY);
                break;

            case 92:
                this.regI = this.regX;
                this.upNZX(this.regX);
                break;

            case 93:
                this.i_eor(this.readM(this.readWX()));
                break;

            case 94:
                this.i_lsr(this.readWX());
                break;

            case 95:
                this.i_div(this.readM(this.readBX()));
                break;

            case 96:
                this.regPC = this.pop2() + 1;
                break;

            case 97:
                this.i_adc(this.readM(this.readBXW()));
                break;

            case 98:
                var2 = this.readB();
                this.push2(this.regPC + var2);
                break;

            case 99:
                this.i_adc(this.readM(this.readBS()));
                break;

            case 100:
                this.writeM(this.readB(), 0);
                break;

            case 101:
                this.i_adc(this.readM(this.readB()));
                break;

            case 102:
                this.i_ror(this.readB());
                break;

            case 103:
                this.i_adc(this.readM(this.readBR()));
                break;

            case 104:
                this.regA = this.popM();
                this.upNZ();
                break;

            case 105:
                this.i_adc(this.readM());
                break;

            case 106:
                var2 = this.regA >>> 1 | (this.flagC ? this.negM() : 0);
                this.flagC = (this.regA & 1) > 0;
                this.regA = var2;
                this.upNZ();
                break;

            case 107:
                this.regA = this.popMr();
                this.upNZ(this.regA);
                break;

            case 108:
                this.regPC = this.readWW();
                break;

            case 109:
                this.i_adc(this.readM(this.readW()));
                break;

            case 110:
                this.i_ror(this.readW());
                break;

            case 111:
                this.i_div(this.readM(this.readW()));
                break;

            case 112:
                this.i_brc(this.flagO);
                break;

            case 113:
                this.i_adc(this.readM(this.readBWY()));
                break;

            case 114:
                this.i_adc(this.readM(this.readBW()));
                break;

            case 115:
                this.i_adc(this.readM(this.readBSWY()));
                break;

            case 116:
                this.writeM(this.readBX(), 0);
                break;

            case 117:
                this.i_adc(this.readM(this.readBX()));
                break;

            case 118:
                this.i_ror(this.readBX());
                break;

            case 119:
                this.i_adc(this.readM(this.readBRWY()));
                break;

            case 120:
                this.flagID = true;
                break;

            case 121:
                this.i_adc(this.readM(this.readWY()));
                break;

            case 122:
                this.regY = this.popX();
                this.upNZX(this.regY);
                break;

            case 123:
                this.regY = this.popXr();
                this.upNZX(this.regY);
                break;

            case 124:
                this.regPC = this.readWXW();
                break;

            case 125:
                this.i_adc(this.readM(this.readWX()));
                break;

            case 126:
                this.i_ror(this.readWX());
                break;

            case 127:
                this.i_div(this.readM(this.readWX()));
                break;

            case 128:
                this.i_brc(true);
                break;

            case 129:
                this.writeM(this.readBXW(), this.regA);
                break;

            case 130:
                var2 = this.readB();
                this.push2r(this.regPC + var2);
                break;

            case 131:
                this.writeM(this.readBS(), this.regA);
                break;

            case 132:
                this.writeX(this.readB(), this.regY);
                break;

            case 133:
                this.writeM(this.readB(), this.regA);
                break;

            case 134:
                this.writeX(this.readB(), this.regX);
                break;

            case 135:
                this.writeM(this.readBR(), this.regA);
                break;

            case 136:
                this.regY = this.regY - 1 & this.maskX();
                this.upNZ(this.regY);
                break;

            case 137:
                this.flagZ = (this.readM() & this.regA) == 0;
                break;

            case 138:
                this.regA = this.regX;

                if (this.flagM)
                {
                    this.regA &= 255;
                }

                this.upNZ();
                break;

            case 139:
                if (this.flagX)
                {
                    this.regSP = this.regR & 65280 | this.regX & 255;
                }
                else
                {
                    this.regR = this.regX;
                }

                this.upNZX(this.regR);
                break;

            case 140:
                this.writeX(this.readW(), this.regY);
                break;

            case 141:
                this.writeM(this.readW(), this.regA);
                break;

            case 142:
                this.writeX(this.readW(), this.regX);
                break;

            case 143:
                this.regD = 0;
                this.regB = 0;
                break;

            case 144:
                this.i_brc(!this.flagC);
                break;

            case 145:
                this.writeM(this.readBWY(), this.regA);
                break;

            case 146:
                this.writeM(this.readBW(), this.regA);
                break;

            case 147:
                this.writeM(this.readBSWY(), this.regA);
                break;

            case 148:
                this.writeX(this.readBX(), this.regY);
                break;

            case 149:
                this.writeM(this.readBX(), this.regA);
                break;

            case 150:
                this.writeX(this.readBY(), this.regX);
                break;

            case 151:
                this.writeM(this.readBRWY(), this.regA);
                break;

            case 152:
                this.regA = this.regY;

                if (this.flagM)
                {
                    this.regA &= 255;
                }

                this.upNZX(this.regY);
                break;

            case 153:
                this.writeM(this.readWY(), this.regA);
                break;

            case 154:
                if (this.flagX)
                {
                    this.regSP = this.regSP & 65280 | this.regX & 255;
                }
                else
                {
                    this.regSP = this.regX;
                }

                this.upNZX(this.regX);
                break;

            case 155:
                this.regY = this.regX;
                this.upNZX(this.regY);
                break;

            case 156:
                this.writeM(this.readW(), 0);
                break;

            case 157:
                this.writeM(this.readWX(), this.regA);
                break;

            case 158:
                this.writeM(this.readWX(), 0);
                break;

            case 159:
                this.regD = (this.regA & this.negM()) > 0 ? 65535 : 0;
                this.regB = this.regD & 255;
                break;

            case 160:
                this.regY = this.readX();
                this.upNZ(this.regY);
                break;

            case 161:
                this.regA = this.readM(this.readBXW());
                this.upNZ();
                break;

            case 162:
                this.regX = this.readX();
                this.upNZ(this.regX);
                break;

            case 163:
                this.regA = this.readM(this.readBS());
                this.upNZ();
                break;

            case 164:
                this.regY = this.readX(this.readB());
                this.upNZ(this.regY);
                break;

            case 165:
                this.regA = this.readM(this.readB());
                this.upNZ();
                break;

            case 166:
                this.regX = this.readX(this.readB());
                this.upNZ(this.regX);
                break;

            case 167:
                this.regA = this.readM(this.readBR());
                this.upNZ();
                break;

            case 168:
                this.regY = this.regA;

                if (this.flagX)
                {
                    this.regY &= 255;
                }

                this.upNZX(this.regY);
                break;

            case 169:
                this.regA = this.readM();
                this.upNZ();
                break;

            case 170:
                this.regX = this.regA;

                if (this.flagX)
                {
                    this.regX &= 255;
                }

                this.upNZX(this.regX);
                break;

            case 171:
                this.regX = this.regR;

                if (this.flagX)
                {
                    this.regX &= 255;
                }

                this.upNZX(this.regX);
                break;

            case 172:
                this.regY = this.readX(this.readW());
                this.upNZ(this.regY);
                break;

            case 173:
                this.regA = this.readM(this.readW());
                this.upNZ();
                break;

            case 174:
                this.regX = this.readX(this.readW());
                this.upNZ(this.regX);
                break;

            case 175:
                this.regA = this.regD;

                if (this.flagM)
                {
                    this.regA &= 255;
                }

                this.upNZ(this.regA);
                break;

            case 176:
                this.i_brc(this.flagC);
                break;

            case 177:
                this.regA = this.readM(this.readBWY());
                this.upNZ();
                break;

            case 178:
                this.regA = this.readM(this.readBW());
                this.upNZ();
                break;

            case 179:
                this.regA = this.readM(this.readBSWY());
                this.upNZ();
                break;

            case 180:
                this.regY = this.readX(this.readBX());
                this.upNZ(this.regY);
                break;

            case 181:
                this.regA = this.readM(this.readBX());
                this.upNZ();
                break;

            case 182:
                this.regX = this.readX(this.readBY());
                this.upNZ(this.regX);
                break;

            case 183:
                this.regA = this.readM(this.readBRWY());
                this.upNZ();
                break;

            case 184:
                this.flagO = false;
                break;

            case 185:
                this.regA = this.readM(this.readWY());
                this.upNZ();
                break;

            case 186:
                this.regX = this.regSP;

                if (this.flagX)
                {
                    this.regX &= 255;
                }

                this.upNZX(this.regX);
                break;

            case 187:
                this.regX = this.regY;
                this.upNZX(this.regX);
                break;

            case 188:
                this.regY = this.readX(this.readWX());
                this.upNZ(this.regY);
                break;

            case 189:
                this.regA = this.readM(this.readWX());
                this.upNZ();
                break;

            case 190:
                this.regX = this.readX(this.readWY());
                this.upNZ(this.regX);
                break;

            case 191:
                if (this.flagM)
                {
                    this.regD = this.regA | this.regB << 8;
                }
                else
                {
                    this.regD = this.regA;
                }

                this.upNZ(this.regA);
                break;

            case 192:
                this.i_cmpx(this.regY, this.readX());
                break;

            case 193:
                this.i_cmp(this.regA, this.readM(this.readBXW()));
                break;

            case 194:
                this.setFlags(this.getFlags() & ~this.readB());
                break;

            case 195:
                this.i_cmp(this.regA, this.readM(this.readBS()));
                break;

            case 196:
                this.i_cmpx(this.regY, this.readX(this.readB()));
                break;

            case 197:
                this.i_cmp(this.regA, this.readM(this.readB()));
                break;

            case 198:
                this.i_dec(this.readB());
                break;

            case 199:
                this.i_cmp(this.regA, this.readM(this.readBR()));
                break;

            case 200:
                this.regY = this.regY + 1 & this.maskX();
                this.upNZ(this.regY);
                break;

            case 201:
                this.i_cmp(this.regA, this.readM());
                break;

            case 202:
                this.regX = this.regX - 1 & this.maskX();
                this.upNZ(this.regX);
                break;

            case 203:
                this.waiTimeout = true;
                break;

            case 204:
                this.i_cmpx(this.regY, this.readX(this.readW()));
                break;

            case 205:
                this.i_cmp(this.regA, this.readM(this.readW()));
                break;

            case 206:
                this.i_dec(this.readW());
                break;

            case 207:
                this.regD = this.popM();
                break;

            case 208:
                this.i_brc(!this.flagZ);
                break;

            case 209:
                this.i_cmp(this.regA, this.readM(this.readBWY()));
                break;

            case 210:
                this.i_cmp(this.regA, this.readM(this.readBW()));
                break;

            case 211:
                this.i_cmp(this.regA, this.readM(this.readBSWY()));
                break;

            case 212:
                this.push2(this.readBW());
                break;

            case 213:
                this.i_cmp(this.regA, this.readM(this.readBX()));
                break;

            case 214:
                this.i_dec(this.readBX());
                break;

            case 215:
                this.i_cmp(this.regA, this.readM(this.readBRWY()));
                break;

            case 216:
                this.flagD = false;
                break;

            case 217:
                this.i_cmp(this.regA, this.readM(this.readWY()));
                break;

            case 218:
                this.pushX(this.regX);
                break;

            case 219:
                this.sliceCycles = -1;

                if (this.worldObj.isAirBlock(this.xCoord, this.yCoord + 1, this.zCoord))
                {
                    this.worldObj.playSoundEffect((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D, "fire.ignite", 1.0F, this.worldObj.rand.nextFloat() * 0.4F + 0.8F);
                    this.worldObj.setBlock(this.xCoord, this.yCoord + 1, this.zCoord, Block.fire.blockID);
                }

                break;

            case 220:
                this.regX = this.regI;

                if (this.flagX)
                {
                    this.regX &= 255;
                }

                this.upNZX(this.regX);
                break;

            case 221:
                this.i_cmp(this.regA, this.readM(this.readWX()));
                break;

            case 222:
                this.i_dec(this.readWX());
                break;

            case 223:
                this.pushM(this.regD);
                break;

            case 224:
                this.i_cmpx(this.regX, this.readX());
                break;

            case 225:
                this.i_sbc(this.readM(this.readBXW()));
                break;

            case 226:
                this.setFlags(this.getFlags() | this.readB());
                break;

            case 227:
                this.i_sbc(this.readM(this.readBS()));
                break;

            case 228:
                this.i_cmpx(this.regX, this.readX(this.readB()));
                break;

            case 229:
                this.i_sbc(this.readM(this.readB()));
                break;

            case 230:
                this.i_inc(this.readB());
                break;

            case 231:
                this.i_sbc(this.readM(this.readBR()));
                break;

            case 232:
                this.regX = this.regX + 1 & this.maskX();
                this.upNZ(this.regX);
                break;

            case 233:
                this.i_sbc(this.readM());

            case 234:
            default:
                break;

            case 235:
                if (this.flagM)
                {
                    var2 = this.regA;
                    this.regA = this.regB;
                    this.regB = var2;
                }
                else
                {
                    this.regA = this.regA >> 8 & 255 | this.regA << 8 & 65280;
                }

                break;

            case 236:
                this.i_cmpx(this.regX, this.readX(this.readW()));
                break;

            case 237:
                this.i_sbc(this.readM(this.readW()));
                break;

            case 238:
                this.i_inc(this.readW());
                break;

            case 239:
                this.i_mmu(this.readB());
                break;

            case 240:
                this.i_brc(this.flagZ);
                break;

            case 241:
                this.i_sbc(this.readM(this.readBWY()));
                break;

            case 242:
                this.i_sbc(this.readM(this.readBW()));
                break;

            case 243:
                this.i_sbc(this.readM(this.readBSWY()));
                break;

            case 244:
                this.push2(this.readW());
                break;

            case 245:
                this.i_sbc(this.readM(this.readBX()));
                break;

            case 246:
                this.i_inc(this.readBX());
                break;

            case 247:
                this.i_sbc(this.readM(this.readBRWY()));
                break;

            case 248:
                this.flagD = true;
                break;

            case 249:
                this.i_sbc(this.readM(this.readWY()));
                break;

            case 250:
                this.regX = this.popX();
                this.upNZX(this.regX);
                break;

            case 251:
                if (this.flagE != this.flagC)
                {
                    if (this.flagE)
                    {
                        this.flagE = false;
                        this.flagC = true;
                    }
                    else
                    {
                        this.flagE = true;
                        this.flagC = false;

                        if (!this.flagM)
                        {
                            this.regB = this.regA >> 8;
                        }

                        this.flagM = true;
                        this.flagX = true;
                        this.regA &= 255;
                        this.regX &= 255;
                        this.regY &= 255;
                    }
                }

                break;

            case 252:
                this.push2(this.regPC + 1);
                this.regPC = this.readWXW();
                break;

            case 253:
                this.i_sbc(this.readM(this.readWX()));
                break;

            case 254:
                this.i_inc(this.readWX());
        }
    }

    public byte[] getFramePacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 7;
        this.writeToPacket(var1);
        var1.headout.write(var1.subId);
        return var1.toByteArray();
    }

    public void handleFramePacket(byte[] var1) throws IOException
    {
        Packet211TileDesc var2 = new Packet211TileDesc(var1);
        var2.subId = var2.getByte();
        this.readFromPacket(var2);
    }

    public void onFrameRefresh(IBlockAccess var1) {}

    public void onFramePickup(IBlockAccess var1) {}

    public void onFrameDrop() {}

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.memory = var1.getByteArray("ram");

        if (this.memory.length != 8192)
        {
            this.memory = new byte[8192];
        }

        this.Rotation = var1.getByte("rot");
        this.addrPOR = var1.getShort("por") & 65535;
        this.addrBRK = var1.getShort("brk") & 65535;
        byte var2 = var1.getByte("efl");
        this.flagE = (var2 & 1) > 0;
        this.mmuEnRB = (var2 & 2) > 0;
        this.mmuEnRBW = (var2 & 4) > 0;
        this.setFlags(var1.getByte("fl"));
        this.regSP = var1.getShort("rsp") & 65535;
        this.regPC = var1.getShort("rpc") & 65535;
        this.regA = var1.getShort("ra") & 65535;

        if (this.flagM)
        {
            this.regB = this.regA >> 8;
            this.regA &= 255;
        }

        this.regX = var1.getShort("rx") & 65535;
        this.regY = var1.getShort("ry") & 65535;
        this.regD = var1.getShort("rd") & 65535;
        this.regR = var1.getShort("rr") & 65535;
        this.regI = var1.getShort("ri") & 65535;
        this.mmuRBB = var1.getShort("mmrb") & 65535;
        this.mmuRBW = var1.getShort("mmrbw") & 65535;
        this.mmuRBA = var1.getByte("mmra") & 255;
        this.sliceCycles = var1.getInteger("cyc");
        this.rtcTicks = var1.getInteger("rtct");
        this.byte0 = var1.getByte("b0") & 255;
        this.byte1 = var1.getByte("b1") & 255;
        this.rbaddr = var1.getByte("rbaddr") & 255;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setByte("rot", (byte)this.Rotation);
        var1.setByteArray("ram", this.memory);
        var1.setShort("por", (short)this.addrPOR);
        var1.setShort("brk", (short)this.addrBRK);
        int var2 = (this.flagE ? 1 : 0) | (this.mmuEnRB ? 2 : 0) | (this.mmuEnRBW ? 4 : 0);
        var1.setByte("efl", (byte)var2);
        var1.setByte("fl", (byte)this.getFlags());
        var1.setShort("rsp", (short)this.regSP);
        var1.setShort("rpc", (short)this.regPC);

        if (this.flagM)
        {
            this.regA = this.regA & 255 | this.regB << 8;
        }

        var1.setShort("ra", (short)this.regA);

        if (this.flagM)
        {
            this.regA &= 255;
        }

        var1.setShort("rx", (short)this.regX);
        var1.setShort("ry", (short)this.regY);
        var1.setShort("rd", (short)this.regD);
        var1.setShort("rr", (short)this.regR);
        var1.setShort("ri", (short)this.regI);
        var1.setShort("mmrb", (short)this.mmuRBB);
        var1.setShort("mmrbw", (short)this.mmuRBW);
        var1.setByte("mmra", (byte)this.mmuRBA);
        var1.setInteger("cyc", this.sliceCycles);
        var1.setInteger("rtct", this.rtcTicks);
        var1.setByte("b0", (byte)this.byte0);
        var1.setByte("b1", (byte)this.byte1);
        var1.setByte("rbaddr", (byte)this.rbaddr);
    }

    protected void readFromPacket(Packet211TileDesc var1) throws IOException
    {
        this.Rotation = var1.getByte();
    }

    protected void writeToPacket(Packet211TileDesc var1)
    {
        var1.addByte(this.Rotation);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        Packet211TileDesc var1 = new Packet211TileDesc();
        var1.subId = 7;
        var1.xCoord = this.xCoord;
        var1.yCoord = this.yCoord;
        var1.zCoord = this.zCoord;
        this.writeToPacket(var1);
        var1.encode();
        return var1;
    }

    public void handlePacket(Packet211TileDesc var1)
    {
        try
        {
            if (var1.subId != 7)
            {
                return;
            }

            this.readFromPacket(var1);
        }
        catch (IOException var3)
        {
            ;
        }

        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
}
