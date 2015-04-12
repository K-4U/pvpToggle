package k4unl.minecraft.pvpToggle.client.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import k4unl.minecraft.k4lib.client.RenderHelper;
import k4unl.minecraft.k4lib.lib.Vector2fMax;
import k4unl.minecraft.pvpToggle.PvpToggle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
        //GL11.glDisable(GL11.GL_LIGHTING);

        if(PvpToggle.instance.isPvPEnabled) {
            drawRect(0, 0, 20, 20, 0xFFFF0000);
        }else{
            drawRect(0, 0, 20, 20, 0xFF00FF00);
        }

        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }
}
