package k4unl.minecraft.pvpToggle.client;

import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.client.gui.GuiPvpToggle;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;


public class ClientEventHandler {

    public static final ClientEventHandler instance = new ClientEventHandler();

    public static final ResourceLocation shield = new ResourceLocation(ModInfo.LID, "textures/gui/shield.png");
    public static final ResourceLocation lock = new ResourceLocation(ModInfo.LID, "textures/gui/lock.png");
    public static final ResourceLocation sword = new ResourceLocation(ModInfo.LID, "textures/gui/swords.png");
    public static final ResourceLocation pickingGui = new ResourceLocation(ModInfo.LID, "textures/gui/pickinggui.png");

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event) {

        if ((event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE && event.getType() != RenderGameOverlayEvent.ElementType.JUMPBAR) ||
                event.isCancelable()) {
            return;
        }
        if (!PvPConfig.renderOwnIcon.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        GL11.glPushMatrix();
        GL11.glPushMatrix();

        //GL11.glColor3f(1.0F, 1.0F, 1.0F);
        //GL11.glDisable(GL11.GL_LIGHTING);
        //GL11.glEnable(GL11.GL_BLEND);

        //GL11.glEnable(GL11.GL_ALPHA_TEST);

        int x = PvPConfig.yCoord.get() - 3;
        int y = PvPConfig.yCoord.get() - 3;
        int w = 32;
        int h = 32;

        float zLevel = 0;

        Tessellator tessellator = Tessellator.getInstance();

        PvPStatus status = ClientHandler.getClientPvPStatus().get(mc.player.getGameProfile().getName());

        if (status == null || status == PvPStatus.ON || status == PvPStatus.FORCEDON) {
            mc.getTextureManager().bindTexture(sword);
        } else {
            mc.getTextureManager().bindTexture(shield);
        }

        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        tessellator.getBuffer().pos((double) (x + 0), (double) (y + h), (double) zLevel).tex(0.0, 1.0).endVertex();
        tessellator.getBuffer().pos((double) (x + w), (double) (y + h), (double) zLevel).tex(1.0, 1.0).endVertex();
        tessellator.getBuffer().pos((double) (x + w), (double) (y + 0), (double) zLevel).tex(1.0, 0.0).endVertex();
        tessellator.getBuffer().pos((double) (x + 0), (double) (y + 0), (double) zLevel).tex(0.0, 0.0).endVertex();
        tessellator.draw();

        if (status == PvPStatus.FORCEDON || status == PvPStatus.FORCEDOFF) {
            mc.getTextureManager().bindTexture(lock);

            tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            tessellator.getBuffer().pos((double) (x + 0), (double) (y + h), (double) zLevel).tex(0.0, 1.0).endVertex();
            tessellator.getBuffer().pos((double) (x + w), (double) (y + h), (double) zLevel).tex(1.0, 1.0).endVertex();
            tessellator.getBuffer().pos((double) (x + w), (double) (y + 0), (double) zLevel).tex(1.0, 0.0).endVertex();
            tessellator.getBuffer().pos((double) (x + 0), (double) (y + 0), (double) zLevel).tex(0.0, 0.0).endVertex();
            tessellator.draw();
        }

        if (ClientHandler.isPicking()) {
            //TODO: Render gui
            tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            h = 112;
            w = 88;
            y = (event.getWindow().getScaledHeight() - h) / 2;
            x = 5;
            mc.getTextureManager().bindTexture(pickingGui);
            tessellator.getBuffer().pos((double) (x + 0), (double) (y + h), (double) zLevel).tex(0.0, 1.0).endVertex();
            tessellator.getBuffer().pos((double) (x + w), (double) (y + h), (double) zLevel).tex(1.0, 1.0).endVertex();
            tessellator.getBuffer().pos((double) (x + w), (double) (y + 0), (double) zLevel).tex(1.0, 0.0).endVertex();
            tessellator.getBuffer().pos((double) (x + 0), (double) (y + 0), (double) zLevel).tex(0.0, 0.0).endVertex();
            tessellator.draw();

            mc.fontRenderer.drawString("Picking block", x + 5, y + 5, 0xFFFFFF);
            mc.fontRenderer.drawString("Hit a block with", x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 1, 0xFFFFFF);
            mc.fontRenderer.drawString("a diamond hoe", x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 2, 0xFFFFFF);
            mc.fontRenderer.drawString("to select a", x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 3, 0xFFFFFF);
            mc.fontRenderer.drawString("location.", x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 4, 0xFFFFFF);
            //Have cached the location the player is looking at.
            if (ClientHandler.getLookingAt() != null) {
                mc.fontRenderer.drawString("X: " + ClientHandler.getLookingAt().getX(), x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 5, 0xFFFFFF);
                mc.fontRenderer.drawString("Y: " + ClientHandler.getLookingAt().getY(), x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 6, 0xFFFFFF);
                mc.fontRenderer.drawString("Z: " + ClientHandler.getLookingAt().getZ(), x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 7, 0xFFFFFF);
            } else {
                mc.fontRenderer.drawString("X: ", x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 5, 0xFFFFFF);
                mc.fontRenderer.drawString("Y: ", x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 6, 0xFFFFFF);
                mc.fontRenderer.drawString("Z: ", x + 5, y + 7 + (mc.fontRenderer.FONT_HEIGHT + 2) * 7, 0xFFFFFF);
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
    public static void onPlayerRender(RenderPlayerEvent.Post e) {

        if (!PvPConfig.renderOtherIcon.get()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();

        float f = (20F - e.getEntity().getDistance(mc.player)) / 1F;

        float alpha = MathHelper.clamp(f, 0F, 1F);
        if (alpha == 0F) return;

        if (e.getPlayer() == mc.player) return;

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, alpha);
        //RenderPlayer

        double x = mc.player.prevPosX + (mc.player.posX - mc.player.prevPosX) * e.getPartialRenderTick();
        double y = mc.player.prevPosY + (mc.player.posY - mc.player.prevPosY) * e.getPartialRenderTick();
        double z = mc.player.prevPosZ + (mc.player.posZ - mc.player.prevPosZ) * e.getPartialRenderTick();
        Vec3d playerPos = new Vec3d(x, y, z);

        x = e.getPlayer().prevPosX + (e.getPlayer().posX - e.getPlayer().prevPosX) * e.getPartialRenderTick();
        y = e.getPlayer().prevPosY + (e.getPlayer().posY - e.getPlayer().prevPosY) * e.getPartialRenderTick();
        z = e.getPlayer().prevPosZ + (e.getPlayer().posZ - e.getPlayer().prevPosZ) * e.getPartialRenderTick();
        Vec3d renderPos = new Vec3d(x, y, z);

        GL11.glTranslated(renderPos.x - playerPos.x, renderPos.y - playerPos.y, renderPos.z - playerPos.z);
        GL11.glTranslated(0D, 2.5D, 0D);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
        GL11.glRotatef(mc.getRenderManager().playerViewX, 1F, 0F, 0F);
        GL11.glScalef(.5F, -.5F, -.5F);

        PvPStatus status = ClientHandler.getClientPvPStatus().get(e.getPlayer().getGameProfile().getId());

        if (status == null || status == PvPStatus.ON || status == PvPStatus.FORCEDON) {
            mc.getTextureManager().bindTexture(sword);
        } else {
            mc.getTextureManager().bindTexture(shield);
        }

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

        if (status == PvPStatus.FORCEDON || status == PvPStatus.FORCEDOFF) {
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

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public static void tickPlayer(TickEvent event) {

        if (event.phase == TickEvent.Phase.END) {
            if (event.side.isClient()) {
                if (ClientHandler.isOpenGui()) {
                    Minecraft.getInstance().displayGuiScreen(new GuiPvpToggle());
                    ClientHandler.setOpenGui(false);
                }
                if (ClientHandler.isOpenHolder()) {
                    Minecraft.getInstance().displayGuiScreen(ClientHandler.getHolder());
                    ClientHandler.setOpenHolder(false);
                }
                if (ClientHandler.isPicking()) {
                    try {
                        Location lookedBlock = Functions.getEntityLookedBlock(Minecraft.getInstance().player, 5.75F);
                        ClientHandler.setLookingAt(lookedBlock);
                    } catch (NullPointerException e) {
                        //I hate you
                    }
                }
            }
        }
    }

    public void renderTimeOverlay() {

    }

}
