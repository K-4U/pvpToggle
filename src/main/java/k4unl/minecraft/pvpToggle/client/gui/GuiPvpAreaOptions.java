package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.WidgetLabel;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiPvpAreaOptions extends PvpToggleScreen {

    private static ResourceLocation background = new ResourceLocation(ModInfo.ID, "textures/gui/maingui.png");

    private GuiPvpAreaAddEdit.mode ourMode;
    private PvPArea area;
    private Screen previous;
    private Button btnBack;


    public GuiPvpAreaOptions(Screen previous, GuiPvpAreaAddEdit.mode ourMode, PvPArea toEdit) {
        super(new StringTextComponent("Area options"));
        this.ourMode = ourMode;
        this.area = toEdit;
        this.previous = previous;
    }

    @Override
    public void init() {

        String title = (this.ourMode == GuiPvpAreaAddEdit.mode.ADD ? "Add" : "Edit") + " area " + (this.ourMode == GuiPvpAreaAddEdit.mode.EDIT ? area.getName() : "") + " options";


        int titleWidth = font.getStringWidth(title);
        WidgetLabel lblTitle = new WidgetLabel(getX() + (WIDTH - titleWidth) / 2, getY() + 6, titleWidth, font.FONT_HEIGHT, title);

        btnBack = new Button(getX() + 4, 4, 20, 20, "<", (button) -> Minecraft.getInstance().displayGuiScreen(previous));

        int y = 40;
        String announce = "Announce: ";
//        WidgetLabel lblAnnounce = new WidgetLabel(5, y + 5, font.getStringWidth(announce), font.FONT_HEIGHT, announce);

        //super(xIn, yIn, widthIn, heightIn, msg);
        CheckboxButton btnAnnounce = new CheckboxButton(getX() + 5, getY() + y, font.getStringWidth(announce), 20, announce, area.getAnnounce()) {
            @Override
            public void onPress() {
                super.onPress();
                area.setAnnounce(this.isChecked());
            }
        };

        String forced = "Forced: ";
//        WidgetLabel lblForced = new WidgetLabel(font, 1, 5, y + 24 + 5, font.getStringWidth(forced), font.FONT_HEIGHT, 0xFFFFFFFF);
//        lblForced.addLine(forced);
//        ToggleButton btnForce = new ToggleButton("btnForce", font.getStringWidth(announce) + 15, y + 24, mc, this, guiButton -> {
//            area.setForced(guiButton.getValue());
//        });
//        btnForce.setValue(area.getForced());

        CheckboxButton btnForce = new CheckboxButton(getX() + 5, getY() + y + 24 + 20, font.getStringWidth(forced), 20, forced, area.getForced()) {
            @Override
            public void onPress() {
                super.onPress();
                area.setForced(this.isChecked());
            }
        };
        addButton(btnBack);
        addButton(btnAnnounce);
        addButton(btnForce);
    }

    /*
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
    */
    @Override
    public ResourceLocation getBackground() {
        return background;
    }
}
