package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.WidgetLabel;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiPvpToggle extends PvpToggleScreen {

    private static final ResourceLocation background = new ResourceLocation(ModInfo.ID, "textures/gui/maingui.png");
    private GuiAreaList areaList;
    private GuiDimensionList dimensionList;

    public GuiPvpToggle() {
        super(new StringTextComponent("PVPToggle Options"));
    }

    @Override
    public ResourceLocation getBackground() {
        return background;
    }

    /*
    @Override
    protected void renderForeground(int mouseX, int mouseY) {
        
        int listHeight = (HEIGHT - 20 - 20 - 20 - 20) / 2;
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
    }*/

    @Override
    public void init() {
        Log.debug("Initializing UI!");
        String pvptoggleTitle = "PVPToggle Options";
        String areas = "Areas:";

        int titleWidth = font.getStringWidth(pvptoggleTitle);
        WidgetLabel lblPvpToggle = new WidgetLabel(getX() + (WIDTH - titleWidth) / 2, getY() + 6, titleWidth, font.FONT_HEIGHT, pvptoggleTitle);

        WidgetLabel lblAreas = new WidgetLabel(getX() + 5, getY() + 21, font.getStringWidth(areas), font.FONT_HEIGHT, areas);

        Button addArea = new Button(getX() + WIDTH - 20 - 11, getY() + 16, 20, 20, "+", (button) -> {
            Minecraft.getInstance().displayGuiScreen(new GuiPvpAreaAddEdit(this, GuiPvpAreaAddEdit.mode.ADD, new PvPArea()));
        });
        addArea.setFGColor(0xFF238C00);
        int listHeight = (HEIGHT - 20 - 20 - 20 - 20) / 2;

//        areaList = new GuiAreaList(mc, WIDTH - 14, listHeight, 0, listHeight, this);

        String dimensions = "Dimensions:";
        WidgetLabel lblDimensions = new WidgetLabel(getX() + 5, getY() + 20 + 20 + listHeight + 9, font.getStringWidth(dimensions), font.FONT_HEIGHT, dimensions);
        Button btnAddDimension = new Button(getX() + WIDTH - 20 - 11, getY() + 20 + 20 + listHeight + 2, 20, 20, "+", (guiButton -> {
            //We need to know the number first.
            Minecraft.getInstance().displayGuiScreen(new GuiPvpDimensionAdd(this));

        }));
        btnAddDimension.setFGColor(0xFF238C00);

//        dimensionList = new GuiDimensionList(Minecraft.getInstance(), WIDTH - 14, HEIGHT - 20 - 24 - 107, 0, HEIGHT - 20 - 24 - 107, this);
//        areaList.setEnabled(true);
//        dimensionList.setEnabled(true);


        addButton(addArea);
        addButton(btnAddDimension);
        addButton(lblPvpToggle);
        addButton(lblAreas);
        addButton(lblDimensions);
    }
    /*
    
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
    }*/
}
