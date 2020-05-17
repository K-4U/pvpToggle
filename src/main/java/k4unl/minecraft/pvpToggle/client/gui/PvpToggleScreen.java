package k4unl.minecraft.pvpToggle.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class PvpToggleScreen extends Screen {

    protected static final int WIDTH = 176;
    protected static final int HEIGHT = 224;

    protected PvpToggleScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    public abstract ResourceLocation getBackground();

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);//RenderSystem
        this.minecraft.getTextureManager().bindTexture(getBackground());
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        this.blit(relX, relY, 0, 0, WIDTH, HEIGHT);
        super.render(mouseX, mouseY, partialTicks);
    }

    int getX() {
        return (this.width - WIDTH) / 2;
    }

    int getY() {
        return (this.height - HEIGHT) / 2;
    }
}
