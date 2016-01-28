PvPToggle
=========

PvPToggle is a mod that allows users to switch whether or not they want to have PvP enabled or not.

## Links ##
Curse: [http://minecraft.curseforge.com/mc-mods/223394-pvptoggle](http://minecraft.curseforge.com/mc-mods/223394-pvptoggle)

## License ##
This mod is released under the MMPLv2
 
## Using my API ##

Mod devs: Using my mod is easier than ever thanks to maven:

	repositories {
	    maven {
	        name = "K-4U repo"
	        url = "http://maven.k-4u.nl/"
	    }
	}

	dependencies {
		#Either just use the API
		compile "k4unl:pvpToggle:1.8.9-1.0.8:api"

		#Or use the deobf version
		compile "k4unl:pvpToggle:1.8.9-1.0.8:deobf"
	}


Get an instance of the API trough `PvPAPI.getInstance()`

