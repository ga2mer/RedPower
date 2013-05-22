package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MachineProxy implements IGuiHandler
{
    public void registerRenderers() {}

    public Object getClientGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new GuiDeploy(var2.inventory, (TileDeploy)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileDeploy.class));

            case 2:
                return new GuiFilter(var2.inventory, (TileFilter)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileFilter.class));

            case 3:
                return new GuiBlueFurnace(var2.inventory, (TileBlueFurnace)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileBlueFurnace.class));

            case 4:
                return new GuiBufferChest(var2.inventory, (TileBufferChest)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileBufferChest.class));

            case 5:
                return new GuiSorter(var2.inventory, (TileSorter)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileSorter.class));

            case 6:
                return new GuiItemDetect(var2.inventory, (TileItemDetect)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileItemDetect.class));

            case 7:
                return new GuiRetriever(var2.inventory, (TileRetriever)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileRetriever.class));

            case 8:
                return new GuiBatteryBox(var2.inventory, (TileBatteryBox)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileBatteryBox.class));

            case 9:
                return new GuiRegulator(var2.inventory, (TileRegulator)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileRegulator.class));

            case 10:
                return new GuiBlueAlloyFurnace(var2.inventory, (TileBlueAlloyFurnace)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileBlueAlloyFurnace.class));

            case 11:
                return new GuiAssemble(var2.inventory, (TileAssemble)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileAssemble.class));

            case 12:
                return new GuiEject(var2.inventory, (TileEjectBase)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileEjectBase.class));

            case 13:
                return new GuiEject(var2.inventory, (TileEjectBase)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileRelay.class));

            case 14:
                return new GuiChargingBench(var2.inventory, (TileChargingBench)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileChargingBench.class));

            case 15:
                return new GuiWindTurbine(var2.inventory, (TileWindTurbine)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileWindTurbine.class));

            case 16:
                return new GuiManager(var2.inventory, (TileManager)CoreLib.getGuiTileEntity(var3, var4, var5, var6, TileManager.class));

            default:
                return null;
        }
    }

    public Object getServerGuiElement(int var1, EntityPlayer var2, World var3, int var4, int var5, int var6)
    {
        switch (var1)
        {
            case 1:
                return new ContainerDeploy(var2.inventory, (TileDeploy)CoreLib.getTileEntity(var3, var4, var5, var6, TileDeploy.class));

            case 2:
                return new ContainerFilter(var2.inventory, (TileFilter)CoreLib.getTileEntity(var3, var4, var5, var6, TileFilter.class));

            case 3:
                return new ContainerBlueFurnace(var2.inventory, (TileBlueFurnace)CoreLib.getTileEntity(var3, var4, var5, var6, TileBlueFurnace.class));

            case 4:
                return new ContainerBufferChest(var2.inventory, (TileBufferChest)CoreLib.getTileEntity(var3, var4, var5, var6, TileBufferChest.class));

            case 5:
                return new ContainerSorter(var2.inventory, (TileSorter)CoreLib.getTileEntity(var3, var4, var5, var6, TileSorter.class));

            case 6:
                return new ContainerItemDetect(var2.inventory, (TileItemDetect)CoreLib.getTileEntity(var3, var4, var5, var6, TileItemDetect.class));

            case 7:
                return new ContainerRetriever(var2.inventory, (TileRetriever)CoreLib.getTileEntity(var3, var4, var5, var6, TileRetriever.class));

            case 8:
                return new ContainerBatteryBox(var2.inventory, (TileBatteryBox)CoreLib.getTileEntity(var3, var4, var5, var6, TileBatteryBox.class));

            case 9:
                return new ContainerRegulator(var2.inventory, (TileRegulator)CoreLib.getTileEntity(var3, var4, var5, var6, TileRegulator.class));

            case 10:
                return new ContainerBlueAlloyFurnace(var2.inventory, (TileBlueAlloyFurnace)CoreLib.getTileEntity(var3, var4, var5, var6, TileBlueAlloyFurnace.class));

            case 11:
                return new ContainerAssemble(var2.inventory, (TileAssemble)CoreLib.getTileEntity(var3, var4, var5, var6, TileAssemble.class));

            case 12:
                return new ContainerEject(var2.inventory, (TileEjectBase)CoreLib.getTileEntity(var3, var4, var5, var6, TileEjectBase.class));

            case 13:
                return new ContainerEject(var2.inventory, (TileEjectBase)CoreLib.getTileEntity(var3, var4, var5, var6, TileRelay.class));

            case 14:
                return new ContainerChargingBench(var2.inventory, (TileChargingBench)CoreLib.getTileEntity(var3, var4, var5, var6, TileChargingBench.class));

            case 15:
                return new ContainerWindTurbine(var2.inventory, (TileWindTurbine)CoreLib.getTileEntity(var3, var4, var5, var6, TileWindTurbine.class));

            case 16:
                return new ContainerManager(var2.inventory, (TileManager)CoreLib.getTileEntity(var3, var4, var5, var6, TileManager.class));

            default:
                return null;
        }
    }
}
