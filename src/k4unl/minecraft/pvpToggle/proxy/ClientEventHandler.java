package k4unl.minecraft.pvpToggle.proxy;

import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	public static final ClientEventHandler instance = new ClientEventHandler();
	
    public static final ResourceLocation shield = new ResourceLocation(ModInfo.LID, "textures/gui/shield.png");
    public static final ResourceLocation lock = new ResourceLocation(ModInfo.LID, "textures/gui/lock.png");
    public static final ResourceLocation sword = new ResourceLocation(ModInfo.LID, "textures/gui/swords.png");

    public void renderTimeOverlay() {

    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {

        if ((event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE && event.type != RenderGameOverlayEvent.ElementType.JUMPBAR) ||
                event.isCancelable()) {
            return;
        }
        
        Minecraft mc = Minecraft.getMinecraft();

        GL11.glPushMatrix();

        //GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_ALPHA_TEST);

        int x = -3;
        int y = -3;
        int w = 32;
        int h = 32;
        
        float zLevel = 0;

        Tessellator tessellator = Tessellator.instance;

        Boolean b = PvpToggle.clientPvPEnabled.get(mc.thePlayer.getUniqueID());
        
        if(b == null || b.booleanValue()) mc.getTextureManager().bindTexture(sword);
        else mc.getTextureManager().bindTexture(shield);

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)zLevel, 0.0, 1.0);
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)zLevel, 1.0, 1.0);
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)zLevel, 1.0, 0.0);
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)zLevel, 0.0, 0.0);
        tessellator.draw();

        if(PvpToggle.clientPvPForced){
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(lock);

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)zLevel, 0.0, 1.0);
            tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)zLevel, 1.0, 1.0);
            tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)zLevel, 1.0, 0.0);
            tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)zLevel, 0.0, 0.0);
            tessellator.draw();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }
    
    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Post e)
    {
        Minecraft mc = Minecraft.getMinecraft();
        
        float f = (6F - e.entity.getDistanceToEntity(mc.thePlayer)) / 1F;
        
        float alpha = MathHelper.clamp_float(f, 0F, 1F);
        if(alpha == 0F) return;
        
        if(e.entityPlayer == mc.thePlayer) return;
        
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1F, 1F, 1F, alpha);
            //RenderPlayer
            GL11.glTranslated(e.entity.posX - mc.thePlayer.posX, e.entity.posY - mc.thePlayer.posY, e.entity.posZ - mc.thePlayer.posZ);
            GL11.glTranslated(0D, 2.8D, 0D);
            GL11.glRotatef(-RenderManager.instance.playerViewY, 0F, 1F, 0F);
            GL11.glRotatef(RenderManager.instance.playerViewX, 1F, 0F, 0F);
            GL11.glScalef(1F, -1F, -1F);
            
            Boolean b = PvpToggle.clientPvPEnabled.get(mc.thePlayer.getUniqueID());
           
            if(b == null || b.booleanValue()) mc.getTextureManager().bindTexture(sword);
            else mc.getTextureManager().bindTexture(shield);
            
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0F, 0F); GL11.glVertex2f(-0.5F, -0.5F);
            GL11.glTexCoord2f(1F, 0F); GL11.glVertex2f(0.5F, -0.5F);
            GL11.glTexCoord2f(1F, 1F); GL11.glVertex2f(0.5F, 0.5F);
            GL11.glTexCoord2f(0F, 1F); GL11.glVertex2f(-0.5F, 0.5F);
            GL11.glEnd();
            
            if(PvpToggle.clientPvPForced)
            {
            	GL11.glTranslated(0D, 0D, -0.001D);
            	mc.getTextureManager().bindTexture(lock);
            	GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0F, 0F); GL11.glVertex2f(-0.5F, -0.5F);
                GL11.glTexCoord2f(1F, 0F); GL11.glVertex2f(0.5F, -0.5F);
                GL11.glTexCoord2f(1F, 1F); GL11.glVertex2f(0.5F, 0.5F);
                GL11.glTexCoord2f(0F, 1F); GL11.glVertex2f(-0.5F, 0.5F);
                GL11.glEnd();
            }
           
            GL11.glPopAttrib();
            GL11.glPopMatrix();
    }
}
