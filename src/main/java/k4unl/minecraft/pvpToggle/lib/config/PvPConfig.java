package k4unl.minecraft.pvpToggle.lib.config;

import k4unl.minecraft.k4lib.lib.config.Config;
import net.minecraftforge.common.ForgeConfigSpec;

public class PvPConfig extends Config {

    public static ForgeConfigSpec.BooleanValue keepInventoryOnPVPDeath;
    public static ForgeConfigSpec.BooleanValue keepExperienceOnPVPDeath;
    public static ForgeConfigSpec.BooleanValue showMessageOnLogin;
    public static ForgeConfigSpec.BooleanValue announceDimensionSettings;
    public static ForgeConfigSpec.IntValue cooldownInSeconds;

    public static ForgeConfigSpec.BooleanValue renderOwnIcon;
    public static ForgeConfigSpec.BooleanValue renderOtherIcon;
    public static ForgeConfigSpec.ConfigValue<Integer> xCoord;
    public static ForgeConfigSpec.ConfigValue<Integer> yCoord;


    @Override
    protected void buildCommon(ForgeConfigSpec.Builder builder) {

    }

    @Override
    protected void buildServer(ForgeConfigSpec.Builder builder) {
        keepInventoryOnPVPDeath = builder.comment("Whether or not a player drops their inventory on a PvP death").define("keepInventoryOnPVPDeath", false);
        keepExperienceOnPVPDeath = builder.comment("Whether or not to keep the experience when a player dies").define("keepExperienceOnPVPDeath", false);
        showMessageOnLogin = builder.comment("Whether or not to display a message when the player logs on.").define("showMessageOnLogin", true);
        announceDimensionSettings = builder.comment("Whether or not to display a message when a player enters a dimension with forced PvP settings").define("announceDimensionSetting", true);
        cooldownInSeconds = builder.comment("The ammount of seconds a player needs to wait before toggling their PvP status").defineInRange("coolDownInSeconds", 30, 1, 600);
    }

    @Override
    protected void buildClient(ForgeConfigSpec.Builder builder) {
        renderOwnIcon = builder.comment("Whether or not to display the icon for you").define("renderOwnIcon", true);
        renderOtherIcon = builder.comment("Whether or not to display the icon of other players").define("renderOtherIcon", true);
        xCoord = builder.comment("The X coordinate on your screen to render the icon").define("x", 0);
        yCoord = builder.comment("The Y coordinate on your screen to render the icon").define("y", 0);
    }
}
