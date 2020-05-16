package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.WidgetLabel;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.io.IOException;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiPvpDimensionAdd extends PvpToggleScreen {

    private static ResourceLocation background = new ResourceLocation(ModInfo.ID, "textures/gui/maingui.png");
    protected Button btnBack;
    private Screen previous;
    private GuiAllDimensionsList dimensionList;

    public GuiPvpDimensionAdd(Screen previous) {
        super(new StringTextComponent("Add dimension"));
        this.previous = previous;
    }
/*
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
    }*/

    @Override
    public void init() {
        String title = "Add dimension ";

        int titleWidth = font.getStringWidth(title);
        WidgetLabel lblTitle = new WidgetLabel((WIDTH - titleWidth) / 2, 6, titleWidth, font.FONT_HEIGHT, title);

        btnBack = new Button(4, 4, 20, 20, "<", (button) -> Minecraft.getInstance().displayGuiScreen(previous));
        //Display a list of all dimensions:
//        dimensionList = new GuiAllDimensionsList(mc, WIDTH - 14, HEIGHT - 20 - 24, 0, HEIGHT - 20 - 24, this);
//        dimensionList.setEnabled(true);

        addButton(lblTitle);
        addButton(btnBack);
    }
/*
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
    }*/

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 1) {
                this.btnBack.onPress();
                //this.actionPerformed(this.cancelBtn);
            }
        } else {
            //this.actionPerformed(this.doneBtn);
        }

    }

    @Override
    public ResourceLocation getBackground() {
        return background;
    }
}
