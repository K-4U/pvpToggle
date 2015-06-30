package k4unl.minecraft.pvpToggle.client;

import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

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
        if(!PvPConfig.INSTANCE.getBool("renderOwnIcon","ui")){
            return;
        }
        
        Minecraft mc = Minecraft.getMinecraft();

        GL11.glPushMatrix();
        GL11.glPushMatrix();

        //GL11.glColor3f(1.0F, 1.0F, 1.0F);
        //GL11.glDisable(GL11.GL_LIGHTING);
        //GL11.glEnable(GL11.GL_BLEND);

        //GL11.glEnable(GL11.GL_ALPHA_TEST);

        int x = PvPConfig.INSTANCE.getInt("x", "ui") -3;
        int y = PvPConfig.INSTANCE.getInt("y", "ui")-3;
        int w = 32;
        int h = 32;
        
        float zLevel = 0;

        Tessellator tessellator = Tessellator.getInstance();

        Boolean b = PvpToggle.clientPvPEnabled.get(mc.thePlayer.getGameProfile().getName());
        
        if(b == null || b){
            mc.getTextureManager().bindTexture(sword);
        }else {
            mc.getTextureManager().bindTexture(shield);
        }

        tessellator.getWorldRenderer().startDrawingQuads();
        tessellator.getWorldRenderer().addVertexWithUV((double) (x + 0), (double) (y + h), (double) zLevel, 0.0, 1.0);
        tessellator.getWorldRenderer().addVertexWithUV((double) (x + w), (double) (y + h), (double) zLevel, 1.0, 1.0);
        tessellator.getWorldRenderer().addVertexWithUV((double) (x + w), (double) (y + 0), (double) zLevel, 1.0, 0.0);
        tessellator.getWorldRenderer().addVertexWithUV((double) (x + 0), (double) (y + 0), (double) zLevel, 0.0, 0.0);
        tessellator.draw();

        if(PvpToggle.clientPvPForced.containsKey(mc.thePlayer.getGameProfile().getName())){
            if(PvpToggle.clientPvPForced.get(mc.thePlayer.getGameProfile().getName())) {
                FMLClientHandler.instance().getClient().getTextureManager().bindTexture(lock);

                tessellator.getWorldRenderer().startDrawingQuads();
                tessellator.getWorldRenderer().addVertexWithUV((double) (x + 0), (double) (y + h), (double) zLevel, 0.0, 1.0);
                tessellator.getWorldRenderer().addVertexWithUV((double) (x + w), (double) (y + h), (double) zLevel, 1.0, 1.0);
                tessellator.getWorldRenderer().addVertexWithUV((double) (x + w), (double) (y + 0), (double) zLevel, 1.0, 0.0);
                tessellator.getWorldRenderer().addVertexWithUV((double) (x + 0), (double) (y + 0), (double) zLevel, 0.0, 0.0);
                tessellator.draw();
            }
        }

        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        //GL11.glDisable(GL11.GL_BLEND);
        //GL11.glDisable(GL11.GL_ALPHA_TEST);

        //GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glPopMatrix();

    }
    
    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Post e) {
        if(!PvPConfig.INSTANCE.getBool("renderOtherIcon", "ui")){
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        
        float f = (20F - e.entity.getDistanceToEntity(mc.thePlayer)) / 1F;
        
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

        double x = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * e.partialRenderTick;
        double y = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * e.partialRenderTick;
        double z = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * e.partialRenderTick;
        Vec3 playerPos = new Vec3(x, y, z);

        x = e.entityPlayer.prevPosX + (e.entityPlayer.posX - e.entityPlayer.prevPosX) * e.partialRenderTick;
        y = e.entityPlayer.prevPosY + (e.entityPlayer.posY - e.entityPlayer.prevPosY) * e.partialRenderTick;
        z = e.entityPlayer.prevPosZ + (e.entityPlayer.posZ - e.entityPlayer.prevPosZ) * e.partialRenderTick;
        Vec3 renderPos = new Vec3(x, y, z);

        GL11.glTranslated(renderPos.xCoord - playerPos.xCoord, renderPos.yCoord - playerPos.yCoord, renderPos.zCoord - playerPos.zCoord);
        GL11.glTranslated(0D, 2.5D, 0D);
        GL11.glRotatef(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0F, 1F, 0F);
        GL11.glRotatef(Minecraft.getMinecraft().getRenderManager().playerViewX, 1F, 0F, 0F);
        GL11.glScalef(.5F, -.5F, -.5F);

        Boolean b = PvpToggle.clientPvPEnabled.get(e.entityPlayer.getGameProfile().getName());

        if(b == null || b){
            mc.getTextureManager().bindTexture(sword);
        }else {
            mc.getTextureManager().bindTexture(shield);
        }

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0F, 0F); GL11.glVertex2f(-0.5F, -0.5F);
        GL11.glTexCoord2f(1F, 0F); GL11.glVertex2f(0.5F, -0.5F);
        GL11.glTexCoord2f(1F, 1F); GL11.glVertex2f(0.5F, 0.5F);
        GL11.glTexCoord2f(0F, 1F); GL11.glVertex2f(-0.5F, 0.5F);
        GL11.glEnd();

        if(PvpToggle.clientPvPForced.containsKey(e.entityPlayer.getGameProfile().getName())){
            if(PvpToggle.clientPvPForced.get(e.entityPlayer.getGameProfile().getName())) {

                GL11.glTranslated(0D, 0D, 0.001D);

                mc.getTextureManager().bindTexture(lock);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0F, 0F);
                GL11.glVertex2f(-0.5F, -0.5F);
                GL11.glTexCoord2f(1F, 0F);
                GL11.glVertex2f(0.5F, -0.5F);
                GL11.glTexCoord2f(1F, 1F);
                GL11.glVertex2f(0.5F, 0.5F);
                GL11.glTexCoord2f(0F, 1F);
                GL11.glVertex2f(-0.5F, 0.5F);
                GL11.glEnd();
            }
        }

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
