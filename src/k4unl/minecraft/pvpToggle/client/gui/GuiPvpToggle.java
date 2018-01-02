package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.gui.GuiWithWidgets;
import k4unl.minecraft.k4lib.client.gui.elements.GuiButton;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
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
public class GuiPvpToggle extends GuiWithWidgets {
    
    private static ResourceLocation background = new ResourceLocation(ModInfo.ID, "textures/gui/maingui.png");
    private GuiAreaList      areaList;
    private GuiDimensionList dimensionList;
    
    @Override
    protected void drawBackground() {
        
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    protected void renderForeground(int mouseX, int mouseY) {
        
        int listHeight = (ySize - 20 - 20 - 20 - 20) / 2;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) 4.0, (float) 20 + 20, 0.0F);
        if (areaList != null) { //WAIT WHAT!?
            try {
                areaList.drawScreen(mouseX - 4, mouseY - 20 - 20, 0);
            } catch (NullPointerException e) {
                //FUCK YOU
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) 4.0, (float) 20 + 20 + 95, 0.0F);
        if (dimensionList != null) { //WAIT WHAT!?
            try {
                dimensionList.drawScreen(mouseX - 4, mouseY - 20 - 20 - 95, 0);
            } catch (NullPointerException e) {
                //FUCK YOU
            }
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public void initGui() {
        
        super.initGui();
        Log.debug("Initializing UI!");
        String pvptoggleTitle = "PVPToggle Options";
        String areas = "Areas:";
        int titleWidth = fontRenderer.getStringWidth(pvptoggleTitle);
        GuiLabel lblPvpToggle = new GuiLabel(fontRenderer, 0, (xSize - titleWidth) / 2, 6, titleWidth, fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblPvpToggle.addLine(pvptoggleTitle);
        GuiLabel lblAreas = new GuiLabel(fontRenderer, 1, 5, 21, fontRenderer.getStringWidth(areas), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblAreas.addLine(areas);
        GuiButton addArea = new GuiButton(0, xSize - 20 - 11, 16, 20, 20, "+", (button) -> {
            Minecraft.getMinecraft().displayGuiScreen(new GuiPvpAreaAddEdit(this, GuiPvpAreaAddEdit.mode.ADD, new PvPArea()));
        });
        addArea.packedFGColour = 0xFF238C00;
        int listHeight = (ySize - 20 - 20 - 20 - 20) / 2;
        
        areaList = new GuiAreaList(mc, xSize - 14, listHeight, 0, listHeight, this);
        
        String dimensions = "Dimensions:";
        GuiLabel lblDimensions = new GuiLabel(fontRenderer, 1, 5, 20 + 20 + listHeight + 9, fontRenderer.getStringWidth(dimensions), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblDimensions.addLine(dimensions);
        GuiButton btnAddDimension = new GuiButton(1, xSize - 20 - 11, 20 + 20 + listHeight + 2, 20, 20, "+", (guiButton -> {
            //We need to know the number first.
            Minecraft.getMinecraft().displayGuiScreen(new GuiPvpDimensionAdd(this));
        }));
        btnAddDimension.packedFGColour = 0xFF238C00;
        
        dimensionList = new GuiDimensionList(mc, xSize - 14, ySize - 20 - 24 - 107, 0, ySize - 20 - 24 - 107, this);
        areaList.setEnabled(true);
        dimensionList.setEnabled(true);
        
        labelList.add(lblPvpToggle);
        labelList.add(lblAreas);
        labelList.add(lblDimensions);
        buttonList.add(addArea);
        buttonList.add(btnAddDimension);
    }
    
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX = mouseX - guiLeft;
        mouseY = mouseY - guiTop - 20 - 20; // This will handle mouse input correctly.. somewhat
        areaList.mouseClicked(mouseX, mouseY, mouseButton);
        mouseY = mouseY - 95;
        dimensionList.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
        areaList.mouseReleased(mouseX, mouseY, state);
        dimensionList.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        
        super.handleMouseInput();
        areaList.handleMouseInput();
        dimensionList.handleMouseInput();
    }
}
