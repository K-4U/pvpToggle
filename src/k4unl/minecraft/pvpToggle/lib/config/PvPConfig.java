package k4unl.minecraft.pvpToggle.lib.config;

import k4unl.minecraft.k4lib.lib.config.Config;
import k4unl.minecraft.k4lib.lib.config.ConfigOption;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class PvPConfig  extends Config{

    public static final PvPConfig INSTANCE = new PvPConfig();

    @Override
	public void init(){
        super.init();
        configOptions.add(new ConfigOption("keepInventoryOnPVPDeath", false));
        configOptions.add(new ConfigOption("keepExperienceOnPVPDeath", false));
        configOptions.add(new ConfigOption("showMessageOnLogin", true));
        configOptions.add(new ConfigOption("coolDownInSeconds", 30));
	}

}
