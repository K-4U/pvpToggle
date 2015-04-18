package k4unl.minecraft.pvpToggle.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import k4unl.minecraft.pvpToggle.PvpToggle;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

public class GuiRenderer extends Gui {

    public void renderTimeOverlay() {

    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {

        if ((event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE && event.type != RenderGameOverlayEvent.ElementType.JUMPBAR) ||
          event.isCancelable()) {
            return;
        }

        GL11.glPushMatrix();

        //GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_ALPHA_TEST);

        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TextureMap.locationItemsTexture);

        IIcon sword = Items.diamond_sword.getIconFromDamage(0);

        int x = 1;
        int y = 1;
        int w = 16;
        int h = 16;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)this.zLevel, (double)sword.getMinU(), (double)sword.getMaxV());
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)this.zLevel, (double)sword.getMaxU(), (double)sword.getMaxV());
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)this.zLevel, (double)sword.getMaxU(), (double)sword.getMinV());
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)sword.getMinU(), (double)sword.getMinV());


        tessellator.addVertexWithUV((double)(x + 0 + 2), (double)(y + h), (double)this.zLevel, (double)sword.getMaxU(), (double)sword.getMaxV());
        tessellator.addVertexWithUV((double)(x + w + 2), (double)(y + h), (double)this.zLevel, (double)sword.getMinU(), (double)sword.getMaxV());
        tessellator.addVertexWithUV((double)(x + w + 2), (double)(y + 0), (double)this.zLevel, (double)sword.getMinU(), (double)sword.getMinV());
        tessellator.addVertexWithUV((double)(x + 0 + 2), (double)(y + 0), (double)this.zLevel, (double)sword.getMaxU(), (double)sword.getMinV());

        tessellator.draw();

        if(!PvpToggle.instance.isPvPEnabled) {
            int color = 0x7FFFFFFF;
            float f3 = (float)(color >> 24 & 255) / 255.0F;
            float f = (float)(color >> 16 & 255) / 255.0F;
            float f1 = (float)(color >> 8 & 255) / 255.0F;
            float f2 = (float)(color & 255) / 255.0F;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            //OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(f, f1, f2, f3);
            tessellator.startDrawingQuads();
            tessellator.addVertex((double)x, (double)y+h, 0.0D);
            tessellator.addVertex((double)w+2+x, (double)y+h, 0.0D);
            tessellator.addVertex((double)w+2+x, (double)y, 0.0D);
            tessellator.addVertex((double)x, (double)y, 0.0D);
            tessellator.draw();

            //drawTexturedModelRectFromIcon(1, 1, sword, 16, 16);
            //drawRect(0, 0, 20, 20, 0xFF00FF00);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }
}
