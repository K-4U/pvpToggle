package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.WidgetLabel;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.client.ClientHandler;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.network.packets.PacketPlayerPicksLocation;
import k4unl.minecraft.pvpToggle.network.packets.PacketRequestData;
import k4unl.minecraft.pvpToggle.network.packets.PacketSaveArea;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiPvpAreaAddEdit extends PvpToggleScreen {

    private static ResourceLocation background = new ResourceLocation(ModInfo.ID, "textures/gui/maingui.png");
    private mode ourMode;
    private PvPArea area;
    private Screen previous;
    private TextFieldWidget[] point1Fields;
    private TextFieldWidget[] point2Fields;
    private TextFieldWidget nameField;
    private Button btnBack;
    private String name = "";
    private int[] coords1;
    private int[] coords2;

    public GuiPvpAreaAddEdit(Screen previous, GuiPvpAreaAddEdit.mode ourMode, PvPArea toEdit) {
        super(new StringTextComponent("Area options"));

        this.ourMode = ourMode;
        this.area = toEdit;
        this.previous = previous;
        point1Fields = new TextFieldWidget[3];
        point2Fields = new TextFieldWidget[3];
        coords1 = new int[3];
        coords2 = new int[3];
        if (ourMode == mode.EDIT) {
            coords1[0] = area.getLoc1().getX();
            coords1[1] = area.getLoc1().getY();
            coords1[2] = area.getLoc1().getZ();

            coords2[0] = area.getLoc2().getX();
            coords2[1] = area.getLoc2().getY();
            coords2[2] = area.getLoc2().getZ();
        }
    }

    public void handlePicked() {

        if (ClientHandler.getLocationPicking() == 1) {
            coords1[0] = ClientHandler.getPickedLocation().getX();
            coords1[1] = ClientHandler.getPickedLocation().getY();
            coords1[2] = ClientHandler.getPickedLocation().getZ();
        } else if (ClientHandler.getLocationPicking() == 2) {
            coords2[0] = ClientHandler.getPickedLocation().getX();
            coords2[1] = ClientHandler.getPickedLocation().getY();
            coords2[2] = ClientHandler.getPickedLocation().getZ();
        }
    }
/*
    @Override
    protected void renderForeground(int mouseX, int mouseY) {

        for (int i = 0; i < 3; i++) {
            point1Fields[i].drawTextBox();
            point2Fields[i].drawTextBox();
        }
        if (ourMode == mode.ADD) {
            nameField.drawTextBox();
        }
    }*/

    @Override
    public void init() {
        String title = (this.ourMode == GuiPvpAreaAddEdit.mode.ADD ? "Add" : "Edit") + " area " + (this.ourMode == mode.EDIT ? area.getName() : "");

        int titleWidth = font.getStringWidth(title);
        WidgetLabel lblTitle = new WidgetLabel(getX() + (WIDTH - titleWidth) / 2, getY() + 6, titleWidth, font.FONT_HEIGHT, title);

        btnBack = new Button(getX() + 4, getY() + 4, 20, 20, "<", (button) -> Minecraft.getInstance().displayGuiScreen(previous));

        if (ourMode == mode.ADD) {
            String name = "Name:";
            WidgetLabel lblName = new WidgetLabel(getX() + 5, getY() + 35, font.getStringWidth(name), font.FONT_HEIGHT, name);

            nameField = new TextFieldWidget(font, getX() + 10 + font.getStringWidth(name), getY() + 28, 60, 20, this.name);
            nameField.setText(this.name);
            addButton(lblName);
            addButton(nameField);
        }

        int y = (ourMode == mode.ADD ? 53 : 35);

        String p1 = "Point 1";
        WidgetLabel lblPoint1 = new WidgetLabel(getX() + 5, getY() + y, font.getStringWidth(p1), font.FONT_HEIGHT, p1);

        String p2 = "Point 2";
        WidgetLabel lblPoint2 = new WidgetLabel(getX() + (int) (WIDTH - font.getStringWidth(p2) - font.getCharWidth(' ')), getY() + y, font.getStringWidth(p2), font.FONT_HEIGHT, p2);


        String[] coordText = new String[]{"X:", "Y:", "Z:"};
        //Render all textboxes
        for (int i = 0; i < 3; i++) {
            WidgetLabel x1 = new WidgetLabel(getX() + 5, getY() + y + 20 + (24 * i), font.getStringWidth(coordText[i]), font.FONT_HEIGHT, coordText[i]);
            WidgetLabel x2 = new WidgetLabel(getX() + WIDTH - 40 - 5 - font.getStringWidth(coordText[i]) - 5, getY() + y + 20 + (24 * i), font.getStringWidth(coordText[i]), font.FONT_HEIGHT, coordText[i]);

            String first = coords1[i] + "";
            String second = coords2[i] + "";

            point1Fields[i] = new TextFieldWidget(font, getX() + 15, getY() + y + 14 + (24 * i), 40, 20, first);
            point1Fields[i].setText(first);
            point2Fields[i] = new TextFieldWidget(font, getX() + WIDTH - 40 - 5, getY() + y + 14 + (24 * i), 40, 20, second);
            point2Fields[i].setText(second);
            addButton(point1Fields[i]);
            addButton(point2Fields[i]);
            addButton(x1);
            addButton(x2);
        }
        Button btnPick1 = new Button(getX() + 4, getY() + y + 14 + (24 * 3), font.getStringWidth("Pick") + 10, 20, "Pick", (guiButton -> {
            //TODO: Send packet to server, minimize gui to the left hand side
            //Save the name
            if (mode.ADD == ourMode) {
                this.name = nameField.getText();
            }
            ClientHandler.setLocationPicking(1);
            ClientHandler.setIsPicking(true);
            PvpToggle.networkHandler.getChannel().sendToServer(new PacketPlayerPicksLocation());
            ClientHandler.setHolder(this);
            Minecraft.getInstance().displayGuiScreen(null);
        }));
        Button btnPick2 = new Button(getX() + WIDTH - 5 - font.getStringWidth("Pick") - 10, getY() + y + 14 + (24 * 3), font.getStringWidth("Pick") + 10, 20, "Pick", (guiButton -> {
            //TODO: Send packet to server, minimize gui to the left hand side
            if (mode.ADD == ourMode) {
                this.name = nameField.getText();
            }
            ClientHandler.setLocationPicking(2);
            ClientHandler.setIsPicking(true);
            PvpToggle.networkHandler.getChannel().sendToServer(new PacketPlayerPicksLocation());
            ClientHandler.setHolder(this);
            Minecraft.getInstance().displayGuiScreen(null);
        }));

        Button btnSave = new Button(getX() + WIDTH - 5 - font.getStringWidth("Save") - 10, getY() + HEIGHT - 20 - 5, font.getStringWidth("Save") + 10, 20, "Save", (guiButton -> {
            boolean errors = false;
            if (ourMode == mode.ADD) {
                if (!validateTextField(nameField)) {
                    errors = true;
                }
                for (int i = 0; i < 3; i++) {
                    if (!validateCoordField(point1Fields[i])) {
                        errors = true;
                    }
                    if (!validateCoordField(point2Fields[i])) {
                        errors = true;
                    }
                }
            }
            if (!errors) {
                //Save it:
                if (ourMode == mode.ADD) {
                    this.area.setName(nameField.getText());
                }
                this.area.setLoc1(new Location(point1Fields[0].getText(), point1Fields[1].getText(), point1Fields[2].getText()));
                this.area.setLoc2(new Location(point2Fields[0].getText(), point2Fields[1].getText(), point2Fields[2].getText()));
                this.area.setDimension(Minecraft.getInstance().player.dimension);

                PvpToggle.networkHandler.sendToServer(new PacketSaveArea(this.area));
                //Let's just assume the save went well?
                //TODO: Get another packet to actually feedback
                //And open the parent again
                //Get areas:
                PvpToggle.networkHandler.sendToServer(new PacketRequestData());
                ClientHandler.setOpenGui(true);
            }
        }));
        Button btnOptions = new Button(getX() + 4, getY() + HEIGHT - 20 - 5, font.getStringWidth("Options") + 10, 20, "Options", (guiButton -> {
            //TODO: Open new GUI with the options.
            Minecraft.getInstance().displayGuiScreen(new GuiPvpAreaOptions(this, ourMode, area));
        }));


        addButton(lblTitle);
        addButton(lblPoint1);
        addButton(lblPoint2);
        addButton(btnBack);
        addButton(btnPick1);
        addButton(btnPick2);
        addButton(btnSave);
        addButton(btnOptions);
    }

    private boolean validateTextField(TextFieldWidget field) {

        if (field.getText().isEmpty()) {
            field.setTextColor(0XFF8c0023);
            return false;
        } else {
            field.setTextColor(14737632);
            return true;
        }
    }

    private boolean validateCoordField(TextFieldWidget field) {

        if (field.getText().isEmpty()) {
            field.setTextColor(0XFF8c0023);
            return false;
        } else {
            try {
                int x = Integer.parseInt(field.getText());
            } catch (NumberFormatException e) {
                field.setTextColor(0XFF8c0023);
                return false;
            }
        }
        field.setTextColor(14737632);
        return true;
    }

    @Override
    public ResourceLocation getBackground() {
        return background;
    }

    enum mode {
        ADD, EDIT
    }
}
