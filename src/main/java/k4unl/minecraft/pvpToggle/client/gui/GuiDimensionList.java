package k4unl.minecraft.pvpToggle.client.gui;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiDimensionList {
}/*extends GuiListExtended {
    
    Map<Integer, ListEntryDimension> dimensionList = new HashMap<>();
    private GuiWithWidgets parent;
    
    public GuiDimensionList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, GuiWithWidgets parent) {
        
        super(mcIn, widthIn, heightIn, topIn, bottomIn, 22);
        this.parent = parent;
        int i = 0;
        
        if (ClientHandler.getDimensions().size() > 0) {
            for (DimensionDTO dimensionDTO : ClientHandler.getDimensions()) {
                dimensionList.put(i, new ListEntryDimension(dimensionDTO, i, this));
                i++;
            }
            Log.debug("Build a dimensional list of " + dimensionList.size());
        }
    }
    
    @Override
    public IGuiListEntry getListEntry(int index) {
        
        return dimensionList.get(index);
    }
    
    @Override
    protected int getSize() {
        
        return dimensionList.size();
    }
    
    @Override
    protected int getScrollBarX() {
        
        return this.width;
    }
    
    @Override
    protected void drawContainerBackground(Tessellator tessellator) {
    
    }
    
    @Override
    public int getListWidth() {
        
        return this.width;
    }
    
    class ListEntryDimension implements IGuiListEntry {
        
        private GuiButton        btnNotForced;
        private GuiImageButton   btnForcedOn;
        private GuiImageButton   btnForcedOff;
        private GuiLabel         lblName;
        private DimensionDTO     dimensionDTO;
        private GuiDimensionList parent;
        
        public ListEntryDimension(DimensionDTO dto, int index, GuiDimensionList parent) {
            
            this.dimensionDTO = dto;
            
            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            lblName = new GuiLabel(fr, index, 0, 0, fr.getStringWidth(dimensionDTO.dimensionName), fr.FONT_HEIGHT, 0xFFFFFFFF);
            lblName.addLine(dimensionDTO.dimensionName);
            
            btnNotForced = new GuiButton(index + 1, parent.getListWidth() - 21, 0, 20, 20, "x", (button) -> {
                dimensionDTO.status = PvPStatus.NOTFORCED;
                NetworkHandler.INSTANCE.sendToServer(new PacketSaveDimension(dimensionDTO));
                updateButtons();
            });
            btnForcedOn = new GuiImageButton(index + 2, parent.getListWidth() - 21 - 19 - 19, 0, 20, 20, ClientEventHandler.sword, guiButton -> {
                dimensionDTO.status = PvPStatus.FORCEDON;
                NetworkHandler.INSTANCE.sendToServer(new PacketSaveDimension(dimensionDTO));
                updateButtons();
            });
            btnForcedOff = new GuiImageButton(index + 2, parent.getListWidth() - 21 - 19, 0, 20, 20, ClientEventHandler.shield, guiButton -> {
                dimensionDTO.status = PvPStatus.FORCEDOFF;
                NetworkHandler.INSTANCE.sendToServer(new PacketSaveDimension(dimensionDTO));
                updateButtons();
            });
            
            //btnNotForced.packedFGColour = 0xFFD90000;
            this.parent = parent;
            updateButtons();
        }
        
        @Override
        public void updatePosition(int entryID, int insideLeft, int yPos, float partialTicks) {
        
        }
        
        private void updateButtons(){
            btnNotForced.setActive(false);
            btnForcedOff.setActive(false);
            btnForcedOn.setActive(false);
            Log.info(dimensionDTO.status.toString());
            switch (dimensionDTO.status) {
                case FORCEDON:
                    btnForcedOn.setActive(true);
                    break;
                case FORCEDOFF:
                    btnForcedOff.setActive(true);
                    break;
                case NOTFORCED:
                    btnNotForced.setActive(true);
                    //TODO: Remove the dimension?
                    break;
                case ON:
                case OFF:
                    //These won't work.
                    break;
            }
            //Send the DTO
            
            
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            
            //Draw text here. Maybe a button?
            if (y >= 0 && y <= (parent.height - parent.mc.fontRenderer.FONT_HEIGHT)) { //Doing this the lazy-mans-way
                lblName.x = x;
                lblName.y = y + ((slotHeight - parent.mc.fontRenderer.FONT_HEIGHT) / 2);
                lblName.drawLabel(Minecraft.getMinecraft(), mouseX, mouseY);
            }
            if (y >= 0 && y <= (parent.height - btnNotForced.height)) {
                btnForcedOn.y = y;
                btnForcedOn.drawButton(mc, mouseX, mouseY, partialTicks);
                btnForcedOff.y = y;
                btnForcedOff.drawButton(mc, mouseX, mouseY, partialTicks);
                btnNotForced.y = y;
                btnNotForced.drawButton(mc, mouseX, mouseY, partialTicks);
            }
        }
        
        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            
            if (btnNotForced.mousePressed(parent.mc, mouseX, mouseY)) {
                btnNotForced.playPressSound(mc.getSoundHandler());
                btnNotForced.getCallback().accept(btnNotForced);
            }
            if (btnForcedOn.mousePressed(parent.mc, mouseX, mouseY)) {
                btnForcedOn.playPressSound(mc.getSoundHandler());
                btnForcedOn.getCallback().accept(btnForcedOn);
            }
            if (btnForcedOff.mousePressed(parent.mc, mouseX, mouseY)) {
                btnForcedOff.playPressSound(mc.getSoundHandler());
                btnForcedOff.getCallback().accept(btnForcedOff);
            }
            return false;
        }
        
        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            
            btnNotForced.mouseReleased(x, y);
            btnForcedOff.mouseReleased(x, y);
            btnForcedOn.mouseReleased(x, y);
        }
    }
}*/
