package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.gui.GuiWithWidgets;
import k4unl.minecraft.k4lib.client.gui.elements.GuiButton;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * @author Koen Beckers (K-4U)
 */
@SideOnly(Side.CLIENT)
public class GuiPvpDimensionAdd extends GuiWithWidgets {
    
    private static ResourceLocation background = new ResourceLocation(ModInfo.ID, "textures/gui/maingui.png");
    private   GuiWithWidgets       previous;
    protected GuiButton            btnBack;
    private   GuiAllDimensionsList dimensionList;
    
    public GuiPvpDimensionAdd(GuiWithWidgets previous) {
        
        this.previous = previous;
        
        
    }
    
    
    @Override
    protected void drawBackground() {
        
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    protected void renderForeground(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) 4.0, (float) 20 + 20, 0.0F);
        if (dimensionList != null) { //WAIT WHAT!?
            try {
                dimensionList.drawScreen(mouseX - 4, mouseY - 20 - 20, 0);
            } catch (NullPointerException e) {
                //FUCK YOU
            }
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public void initGui() {
        
        super.initGui();
        
        String title = "Add dimension ";
        
        int titleWidth = fontRenderer.getStringWidth(title);
        GuiLabel lblTitle = new GuiLabel(fontRenderer, 0, (xSize - titleWidth) / 2, 6, titleWidth, fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblTitle.addLine(title);
        
        
        btnBack = new GuiButton(0, 4, 4, 20, 20, "<", (button) -> {
            Minecraft.getMinecraft().displayGuiScreen(previous);
        });
        //Display a list of all dimensions:
        dimensionList = new GuiAllDimensionsList(mc, xSize - 14, ySize - 20 - 24, 0, ySize - 20 - 24, this);
        dimensionList.setEnabled(true);
        
        labelList.add(lblTitle);
        
        buttonList.add(btnBack);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX = mouseX - guiLeft;
        mouseY = mouseY - guiTop - 20 - 20; // This will handle mouse input correctly.. somewhat
        dimensionList.mouseClicked(mouseX, mouseY, mouseButton);
        
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        
        super.handleMouseInput();
    }
    
    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 1) {
                this.btnBack.getCallback().accept(this.btnBack);
                //this.actionPerformed(this.cancelBtn);
            }
        } else {
            //this.actionPerformed(this.doneBtn);
        }
        
    }
}
