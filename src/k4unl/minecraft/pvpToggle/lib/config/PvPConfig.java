package k4unl.minecraft.pvpToggle.lib.config;

import k4unl.minecraft.k4lib.lib.config.Config;
import k4unl.minecraft.k4lib.lib.config.ConfigOption;
import net.minecraftforge.fml.relauncher.Side;

public class PvPConfig  extends Config{

    public static final PvPConfig INSTANCE = new PvPConfig();

    public void init(Side side) {

        super.init();
        if(side.isServer()) {
            configOptions.add(new ConfigOption("keepInventoryOnPVPDeath", false).setComment("Whether or not a player drops their inventory on a PvP death"));
            configOptions.add(new ConfigOption("keepExperienceOnPVPDeath", false).setComment("Whether or not to keep the experience when a player dies"));
            configOptions.add(new ConfigOption("showMessageOnLogin", true).setComment("Whether or not to display a message when the player logs on."));
            configOptions.add(new ConfigOption("coolDownInSeconds", 30).setComment("The ammount of seconds a player needs to wait before toggling their PvP status"));
            configOptions.add(new ConfigOption("announceDimensionSetting", true)
                    .setComment("Whether or not to display a message when a player enters a dimension with forced PvP settings"));
        }else{
            configOptions.add(new ConfigOption("renderOwnIcon", true).setCategory("ui").setComment("Whether or not to display the icon for you"));
            configOptions.add(new ConfigOption("x", 0).setCategory("ui").setComment("The X coordinate on your screen to render the icon"));
            configOptions.add(new ConfigOption("y", 0).setCategory("ui").setComment("The Y coordinate on your screen to render the icon"));

            configOptions.add(new ConfigOption("renderOtherIcon", true).setCategory("ui").setComment("Whether or not to display the icon of other players"));
        }
    }
}
