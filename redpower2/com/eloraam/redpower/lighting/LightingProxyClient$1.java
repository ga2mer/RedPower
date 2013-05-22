package com.eloraam.redpower.lighting;

import net.minecraftforge.client.IRenderContextHandler;
import org.lwjgl.opengl.GL11;

class LightingProxyClient$1 implements IRenderContextHandler
{
    final LightingProxyClient this$0;

    LightingProxyClient$1(LightingProxyClient var1)
    {
        this.this$0 = var1;
    }

    public void beforeRenderContext()
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
    }

    public void afterRenderContext()
    {
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
}
