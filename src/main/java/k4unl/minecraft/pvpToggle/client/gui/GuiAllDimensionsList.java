package k4unl.minecraft.pvpToggle.client.gui;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiAllDimensionsList {
} /* extends GuiListExtended {
    
    Map<Integer, ListEntryDimension> dimensionList = new HashMap<>();
    private GuiWithWidgets parent;
    
    public GuiAllDimensionsList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, GuiWithWidgets parent) {
        
        super(mcIn, widthIn, heightIn, topIn, bottomIn, 22);
        this.parent = parent;
        int i = 0;
        
        if (ClientHandler.getAllDimensions().size() > 0) {
            for (DimensionDTO dimensionDTO : ClientHandler.getAllDimensions()) {
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
        
        private GuiLabel             lblName;
        private DimensionDTO         dimensionDTO;
        private GuiAllDimensionsList parent;
        private GuiButton btnAddDimension;
        
        public ListEntryDimension(DimensionDTO dto, int index, GuiAllDimensionsList parent) {
            
            this.dimensionDTO = dto;
            
            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            lblName = new GuiLabel(fr, index, 0, 0, fr.getStringWidth(dimensionDTO.dimensionName), fr.FONT_HEIGHT, 0xFFFFFFFF);
            lblName.addLine(dimensionDTO.dimensionName);
    
            btnAddDimension = new GuiButton(index + 1, parent.getListWidth() - 21, 0, 20, 20, "+", (button) -> {
                dimensionDTO.status = PvPStatus.NOTFORCED;
                NetworkHandler.INSTANCE.sendToServer(new PacketSaveDimension(dimensionDTO));
                //Now, add it to the list:
                ClientHandler.getDimensions().add(dimensionDTO);
                ((GuiPvpDimensionAdd)parent.parent).btnBack.getCallback().accept(((GuiPvpDimensionAdd)parent.parent).btnBack);
            });
            btnAddDimension.packedFGColour = 0xFF238C00;
            
            this.parent = parent;
        }
        
        @Override
        public void updatePosition(int entryID, int insideLeft, int yPos, float partialTicks) {
        
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            
            //Draw text here. Maybe a button?
            if (y >= 0 && y <= (parent.height - parent.mc.fontRenderer.FONT_HEIGHT)) { //Doing this the lazy-mans-way
                lblName.x = x;
                lblName.y = y + ((slotHeight - parent.mc.fontRenderer.FONT_HEIGHT) / 2);
                lblName.drawLabel(Minecraft.getMinecraft(), mouseX, mouseY);
            }
            if (y >= 0 && y <= (parent.height - btnAddDimension.height)) {
                btnAddDimension.y = y;
                btnAddDimension.drawButton(mc, mouseX, mouseY, partialTicks);
            }
        }
        
        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if (btnAddDimension.mousePressed(parent.mc, mouseX, mouseY)) {
                btnAddDimension.playPressSound(mc.getSoundHandler());
                btnAddDimension.getCallback().accept(btnAddDimension);
            }
            return false;
        }
        
        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            btnAddDimension.mouseReleased(x, y);
        }
    }
}*/
