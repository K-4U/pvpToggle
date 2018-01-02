package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.gui.GuiWithWidgets;
import k4unl.minecraft.k4lib.client.gui.elements.GuiButton;
import k4unl.minecraft.k4lib.client.gui.elements.ToggleButton;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * @author Koen Beckers (K-4U)
 */
@SideOnly(Side.CLIENT)
public class GuiPvpAreaOptions extends GuiWithWidgets {
    
    private static ResourceLocation background = new ResourceLocation(ModInfo.ID, "textures/gui/maingui.png");
    
    private GuiPvpAreaAddEdit.mode ourMode;
    private PvPArea                area;
    private GuiWithWidgets         previous;
    private GuiButton              btnBack;
    
    
    public GuiPvpAreaOptions(GuiWithWidgets previous, GuiPvpAreaAddEdit.mode ourMode, PvPArea toEdit) {
        
        this.ourMode = ourMode;
        this.area = toEdit;
        this.previous = previous;
    }
    
    @Override
    protected void drawBackground() {
        
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    protected void renderForeground(int mouseX, int mouseY) {
    
    }
    
    @Override
    public void initGui() {
        
        super.initGui();
        
        String title = (this.ourMode == GuiPvpAreaAddEdit.mode.ADD ? "Add" : "Edit") + " area " + (this.ourMode == GuiPvpAreaAddEdit.mode.EDIT ? area.getName() : "") + " options";
        
        
        int titleWidth = fontRenderer.getStringWidth(title);
        GuiLabel lblTitle = new GuiLabel(fontRenderer, 0, (xSize - titleWidth) / 2, 6, titleWidth, fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblTitle.addLine(title);
        
        btnBack = new GuiButton(0, 4, 4, 20, 20, "<", (button) -> {
            Minecraft.getMinecraft().displayGuiScreen(previous);
        });
        
        int y = 40;
        String announce = "Announce: ";
        GuiLabel lblAnnounce = new GuiLabel(fontRenderer, 1, 5, y + 5, fontRenderer.getStringWidth(announce), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblAnnounce.addLine(announce);
    
        ToggleButton btnAnnounce = new ToggleButton("btnAnnounce", fontRenderer.getStringWidth(announce) + 15, y, Minecraft.getMinecraft(), this, guiButton -> {
            area.setAnnounce(guiButton.getValue());
        });
        btnAnnounce.setValue(area.getAnnounce());
        
        
        String forced = "Forced: ";
        GuiLabel lblForced = new GuiLabel(fontRenderer, 1, 5, y + 24 + 5, fontRenderer.getStringWidth(forced), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblForced.addLine(forced);
        ToggleButton btnForce = new ToggleButton("btnForce", fontRenderer.getStringWidth(announce) + 15, y + 24, mc, this, guiButton -> {
            area.setForced(guiButton.getValue());
        });
        btnForce.setValue(area.getForced());
        
        
        labelList.add(lblTitle);
        labelList.add(lblAnnounce);
        labelList.add(lblForced);
        
        buttonList.add(btnBack);
        addWidget(btnAnnounce);
        addWidget(btnForce);
    }
    
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX = mouseX - guiLeft;
        mouseY = mouseY - guiTop;
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        
        super.handleMouseInput();
    }
}
