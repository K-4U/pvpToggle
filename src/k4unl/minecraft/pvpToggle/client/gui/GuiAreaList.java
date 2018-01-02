package k4unl.minecraft.pvpToggle.client.gui;

import k4unl.minecraft.k4lib.client.gui.GuiWithWidgets;
import k4unl.minecraft.k4lib.client.gui.elements.GuiButton;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.client.ClientHandler;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.network.packets.PacketRemoveArea;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiAreaList extends GuiListExtended {
    
    Map<Integer, ListEntryArea> areaList = new HashMap<>();
    private GuiWithWidgets parent;
    
    public GuiAreaList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, GuiWithWidgets parent) {
        
        super(mcIn, widthIn, heightIn, topIn, bottomIn, 22);
        this.parent = parent;
        int i = 0;
        if (ClientHandler.getAreas().size() > 0) {
            //PvPArea area = PvpToggle.areas.get(0);
            for (PvPArea area : ClientHandler.getAreas()) {
                areaList.put(i, new ListEntryArea(area, i, this));
                i++;
            }
            Log.debug("Build a list of " + areaList.size());
        }
    }
    
    @Override
    public IGuiListEntry getListEntry(int index) {
        
        return areaList.get(index);
    }
    
    @Override
    protected int getSize() {
        
        return areaList.size();
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
    
    class ListEntryArea implements IGuiListEntry {
        
        private GuiButton   btnOpen;
        private GuiButton   btnRemove;
        private GuiLabel    lblName;
        private PvPArea     area;
        private GuiAreaList parent;
        
        public ListEntryArea(PvPArea area, int index, GuiAreaList parent) {
            
            this.area = area;
            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            lblName = new GuiLabel(fr, index, 0, 0, fr.getStringWidth(area.getName()), fr.FONT_HEIGHT, 0xFFFFFFFF);
            lblName.addLine(area.getName());
            btnOpen = new GuiButton(index, parent.getListWidth() - 100, 0, 80, 20, "Edit", (button) -> {
                Minecraft.getMinecraft().displayGuiScreen(new GuiPvpAreaAddEdit(parent.parent, GuiPvpAreaAddEdit.mode.EDIT, this.area));
            });
            btnRemove = new GuiButton(index+1, parent.getListWidth() - 21, 0, 20, 20, "x", (button) -> {
                PvpToggle.networkHandler.getChannel().sendToServer(new PacketRemoveArea(area.getName()));
            });
            btnRemove.packedFGColour = 0xFFD90000;
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
            if (y >= 0 && y <= (parent.height - btnOpen.height)) {
                btnOpen.y = y;
                btnOpen.drawButton(mc, mouseX, mouseY, partialTicks);
                btnRemove.y = y;
                btnRemove.drawButton(mc, mouseX, mouseY, partialTicks);
            }
        }
        
        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            
            if (btnOpen.mousePressed(parent.mc, mouseX, mouseY)) {
                btnOpen.playPressSound(mc.getSoundHandler());
                btnOpen.getCallback().accept(btnOpen);
            }
            if(btnRemove.mousePressed(parent.mc, mouseX, mouseY)){
                btnRemove.playPressSound(mc.getSoundHandler());
                btnRemove.getCallback().accept(btnRemove);
            }
            
            return false;
        }
        
        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            
            btnOpen.mouseReleased(x, y);
            btnRemove.mouseReleased(x, y);
        }
    }
}
