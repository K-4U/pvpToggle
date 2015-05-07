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

        int x = 1;
        int y = 1;
        int w = 16;
        int h = 16;


        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TextureMap.locationItemsTexture);

        IIcon sword = Items.diamond_sword.getIconFromDamage(0);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)this.zLevel, (double)sword.getMinU(), (double)sword.getMaxV());
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)this.zLevel, (double)sword.getMaxU(), (double)sword.getMaxV());
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)this.zLevel, (double)sword.getMaxU(), (double)sword.getMinV());
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)sword.getMinU(), (double)sword.getMinV());


        if(!PvpToggle.instance.isPvPEnabled) {
            x = x + w;
        }
        tessellator.addVertexWithUV((double)(x + 0 + 2), (double)(y + h), (double)this.zLevel, (double)sword.getMaxU(), (double)sword.getMaxV());
        tessellator.addVertexWithUV((double)(x + w + 2), (double)(y + h), (double)this.zLevel, (double)sword.getMinU(), (double)sword.getMaxV());
        tessellator.addVertexWithUV((double)(x + w + 2), (double)(y + 0), (double)this.zLevel, (double)sword.getMinU(), (double)sword.getMinV());
        tessellator.addVertexWithUV((double)(x + 0 + 2), (double)(y + 0), (double)this.zLevel, (double)sword.getMaxU(), (double)sword.getMinV());

        tessellator.draw();


        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }
}
