package k4unl.minecraft.pvpToggle.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

public class GuiRenderer extends Gui {

    private static ResourceLocation shield = new ResourceLocation(ModInfo.LID, "textures/gui/shield.png");
    private static ResourceLocation lock = new ResourceLocation(ModInfo.LID, "textures/gui/lock.png");
    private static ResourceLocation sword = new ResourceLocation(ModInfo.LID, "textures/gui/swords.png");

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

        Tessellator tessellator = Tessellator.instance;

        if(PvpToggle.instance.isPvPEnabled){
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(sword);
        }else{
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(shield);
        }

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)this.zLevel, 0.0, 1.0);
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)this.zLevel, 1.0, 1.0);
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)this.zLevel, 1.0, 0.0);
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, 0.0, 0.0);
        tessellator.draw();

        if(PvpToggle.instance.isPvPForced){
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(lock);

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)this.zLevel, 0.0, 1.0);
            tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)this.zLevel, 1.0, 1.0);
            tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)this.zLevel, 1.0, 0.0);
            tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, 0.0, 0.0);
            tessellator.draw();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }
}
