package com.eloraam.redpower.machine;

import net.minecraftforge.client.IRenderContextHandler;
import org.lwjgl.opengl.GL11;

class MachineProxyClient$1 implements IRenderContextHandler
{
    final MachineProxyClient this$0;

    MachineProxyClient$1(MachineProxyClient var1)
    {
        this.this$0 = var1;
    }

    public void beforeRenderContext()
    {
        GL11.glPolygonOffset(-0.1F, -1.0F);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
    }

    public void afterRenderContext()
    {
        GL11.glPolygonOffset(0.0F, 0.0F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
    }
}
