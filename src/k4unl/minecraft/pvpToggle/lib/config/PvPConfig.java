package k4unl.minecraft.pvpToggle.lib.config;

import k4unl.minecraft.k4lib.lib.config.Config;
import k4unl.minecraft.k4lib.lib.config.ConfigOption;
import k4unl.minecraft.pvpToggle.lib.PvPForced;

import java.util.HashMap;
import java.util.Map;

public class PvPConfig  extends Config{

    public static final PvPConfig INSTANCE = new PvPConfig();

    private static final Map<Integer, PvPForced> dimensionList = new HashMap<Integer, PvPForced>();

    @Override
    public void init() {

        super.init();
        configOptions.add(new ConfigOption("keepInventoryOnPVPDeath", false).setComment("Whether or not a player drops their inventory on a PvP death"));
        configOptions.add(new ConfigOption("keepExperienceOnPVPDeath", false).setComment("Whether or not to keep the experience when a player dies"));
        configOptions.add(new ConfigOption("showMessageOnLogin", true).setComment("Whether or not to display a message when the player logs on."));
        configOptions.add(new ConfigOption("coolDownInSeconds", 30).setComment("The ammount of seconds a player needs to wait before toggling their PvP status"));
        configOptions.add(new ConfigOption("announceDimensionSetting", true).setComment("Whether or not to display a message when a player enters a dimension with forced PvP settings"));
    }
}
