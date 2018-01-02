package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.gui.GuiWithWidgets;
import k4unl.minecraft.k4lib.client.gui.elements.GuiButton;
import k4unl.minecraft.k4lib.client.gui.elements.GuiTextField;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.client.ClientHandler;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.network.packets.PacketPlayerPicksLocation;
import k4unl.minecraft.pvpToggle.network.packets.PacketRequestData;
import k4unl.minecraft.pvpToggle.network.packets.PacketSaveArea;
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
public class GuiPvpAreaAddEdit extends GuiWithWidgets {
    
    private static ResourceLocation background = new ResourceLocation(ModInfo.ID, "textures/gui/maingui.png");
    private mode           ourMode;
    private PvPArea        area;
    private GuiWithWidgets previous;
    private GuiTextField[] point1Fields;
    private GuiTextField[] point2Fields;
    private GuiTextField   nameField;
    private GuiButton      btnBack;
    private String name = "";
    private int[] coords1;
    private int[] coords2;
    
    public GuiPvpAreaAddEdit(GuiWithWidgets previous, GuiPvpAreaAddEdit.mode ourMode, PvPArea toEdit) {
        
        this.ourMode = ourMode;
        this.area = toEdit;
        this.previous = previous;
        point1Fields = new GuiTextField[3];
        point2Fields = new GuiTextField[3];
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
    
    @Override
    protected void drawBackground() {
        
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    protected void renderForeground(int mouseX, int mouseY) {
        
        for (int i = 0; i < 3; i++) {
            point1Fields[i].drawTextBox();
            point2Fields[i].drawTextBox();
        }
        if (ourMode == mode.ADD) {
            nameField.drawTextBox();
        }
    }
    
    @Override
    public void initGui() {
        
        super.initGui();
        
        String title = (this.ourMode == GuiPvpAreaAddEdit.mode.ADD ? "Add" : "Edit") + " area " + (this.ourMode == mode.EDIT ? area.getName() : "");
        
        
        int titleWidth = fontRenderer.getStringWidth(title);
        GuiLabel lblTitle = new GuiLabel(fontRenderer, 0, (xSize - titleWidth) / 2, 6, titleWidth, fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblTitle.addLine(title);
        
        
        btnBack = new GuiButton(0, 4, 4, 20, 20, "<", (button) -> {
            Minecraft.getMinecraft().displayGuiScreen(previous);
        });
        
        if (ourMode == mode.ADD) {
            String name = "Name:";
            GuiLabel lblName = new GuiLabel(fontRenderer, 1, 5, 35, fontRenderer.getStringWidth(name), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
            lblName.addLine(name);
            
            nameField = new GuiTextField(0, fontRenderer, 10 + fontRenderer.getStringWidth(name), 28, 60, 20);
            nameField.setText(this.name);
            labelList.add(lblName);
        }
        
        int y = (ourMode == mode.ADD ? 53 : 35);
        
        String p1 = "Point 1";
        GuiLabel lblPoint1 = new GuiLabel(fontRenderer, 1, 5, y, fontRenderer.getStringWidth(p1), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblPoint1.addLine(p1);
        
        String p2 = "Point 2";
        GuiLabel lblPoint2 = new GuiLabel(fontRenderer, 2, xSize - fontRenderer.getStringWidth(p2) - fontRenderer.getCharWidth(' '), y, fontRenderer.getStringWidth(p2), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
        lblPoint2.addLine(p2);
        
        
        String[] coordText = new String[]{"X:", "Y:", "Z:"};
        //Render all textboxes
        for (int i = 0; i < 3; i++) {
            GuiLabel x1 = new GuiLabel(fontRenderer, i + 2, 5, y + 20 + (24 * i), fontRenderer.getStringWidth(coordText[i]), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
            x1.addLine(coordText[i]);
            GuiLabel x2 = new GuiLabel(fontRenderer, i + 5, xSize - 40 - 5 - fontRenderer.getStringWidth(coordText[i]) - 5, y + 20 + (24 * i), fontRenderer.getStringWidth(coordText[i]), fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
            x2.addLine(coordText[i]);
            point1Fields[i] = new GuiTextField(i + 1, fontRenderer, 15, y + 14 + (24 * i), 40, 20);
            point1Fields[i].setText(coords1[i] + "");
            point2Fields[i] = new GuiTextField(i + 4, fontRenderer, xSize - 40 - 5, y + 14 + (24 * i), 40, 20);
            point2Fields[i].setText(coords2[i] + "");
            labelList.add(x1);
            labelList.add(x2);
        }
        GuiButton btnPick1 = new GuiButton(1, 4, y + 14 + (24 * 3), fontRenderer.getStringWidth("Pick") + 10, 20, "Pick", (guiButton -> {
            //TODO: Send packet to server, minimize gui to the left hand side
            ClientHandler.setLocationPicking(1);
            ClientHandler.setIsPicking(true);
            PvpToggle.networkHandler.getChannel().sendToServer(new PacketPlayerPicksLocation());
            ClientHandler.setHolder(this);
            Minecraft.getMinecraft().displayGuiScreen(null);
        }));
        GuiButton btnPick2 = new GuiButton(2, xSize - 5 - fontRenderer.getStringWidth("Pick") - 10, y + 14 + (24 * 3), fontRenderer.getStringWidth("Pick") + 10, 20, "Pick", (guiButton -> {
            //TODO: Send packet to server, minimize gui to the left hand side
            ClientHandler.setLocationPicking(2);
            ClientHandler.setIsPicking(true);
            PvpToggle.networkHandler.getChannel().sendToServer(new PacketPlayerPicksLocation());
            ClientHandler.setHolder(this);
            Minecraft.getMinecraft().displayGuiScreen(null);
        }));
        
        GuiButton btnSave = new GuiButton(3, xSize - 5 - fontRenderer.getStringWidth("Save") - 10, ySize - 20 - 5, fontRenderer.getStringWidth("Save") + 10, 20, "Save", (guiButton -> {
            boolean errors = false;
            if (ourMode == mode.ADD) {
                if (!validateTextField(nameField)) {
                    errors = true;
                }
                for (int i = 0; i < 3; i++) {
                    if (!validateTextField(point1Fields[i])) {
                        errors = true;
                    }
                    if (!validateTextField(point2Fields[i])) {
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
                this.area.setDimensionId(Minecraft.getMinecraft().player.dimension);
                
                PvpToggle.networkHandler.sendToServer(new PacketSaveArea(this.area));
                //Let's just assume the save went well?
                //TODO: Get another packet to actually feedback
                //And open the parent again
                //Get areas:
                PvpToggle.networkHandler.sendToServer(new PacketRequestData());
                ClientHandler.setOpenGui(true);
            }
        }));
        GuiButton btnOptions = new GuiButton(3, 4, ySize - 20 - 5, fontRenderer.getStringWidth("Options") + 10, 20, "Options", (guiButton -> {
            //TODO: Open new GUI with the options.
            Minecraft.getMinecraft().displayGuiScreen(new GuiPvpAreaOptions(this, ourMode, area));
        }));
        
        labelList.add(lblTitle);
        
        labelList.add(lblPoint1);
        labelList.add(lblPoint2);
        buttonList.add(btnBack);
        buttonList.add(btnPick1);
        buttonList.add(btnPick2);
        buttonList.add(btnSave);
        buttonList.add(btnOptions);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX = mouseX - guiLeft;
        mouseY = mouseY - guiTop;
        for (int i = 0; i < 3; i++) {
            point1Fields[i].mouseClicked(mouseX, mouseY, mouseButton);
            point2Fields[i].mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (ourMode == mode.ADD) {
            nameField.mouseClicked(mouseX, mouseY, mouseButton);
        }
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
        
        if (!Character.isAlphabetic(typedChar) && Character.getType(typedChar) != Character.OTHER_PUNCTUATION && typedChar != '=' && typedChar != '+') {
            for (int i = 0; i < 3; i++) {
                point1Fields[i].textboxKeyTyped(typedChar, keyCode);
                if (validateCoordField(point1Fields[i])) {
                    coords1[i] = Integer.parseInt(point1Fields[i].getText());
                }
                
                point2Fields[i].textboxKeyTyped(typedChar, keyCode);
                if (validateCoordField(point2Fields[i])) {
                    coords2[i] = Integer.parseInt(point2Fields[i].getText());
                }
            }
        }
        nameField.textboxKeyTyped(typedChar, keyCode);
        validateTextField(nameField);
        name = nameField.getText();
        
        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 1) {
                this.btnBack.getCallback().accept(this.btnBack);
                //this.actionPerformed(this.cancelBtn);
            }
        } else {
            //this.actionPerformed(this.doneBtn);
        }
    }
    
    private boolean validateTextField(GuiTextField field) {
        
        if (field.getText().isEmpty()) {
            field.setNeedsAttention(true);
            return false;
        } else {
            field.setNeedsAttention(false);
            return true;
        }
    }
    
    private boolean validateCoordField(GuiTextField field) {
        
        if (field.getText().isEmpty()) {
            field.setNeedsAttention(true);
            return false;
        } else {
            try {
                int x = Integer.parseInt(field.getText());
            } catch (NumberFormatException e) {
                field.setNeedsAttention(true);
                return false;
            }
        }
        field.setNeedsAttention(false);
        return true;
    }
    
    enum mode {
        ADD, EDIT
    }
}
