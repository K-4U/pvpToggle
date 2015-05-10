package k4unl.minecraft.pvpToggle.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import k4unl.minecraft.k4lib.lib.SpecialChars;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.lib.Areas;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.PvPForced;
import k4unl.minecraft.pvpToggle.lib.User;
import k4unl.minecraft.pvpToggle.lib.Users;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

import java.util.HashMap;
import java.util.List;

public class EventHelper {

    private int ticksPassed = 0;

	public static void init(){
		MinecraftForge.EVENT_BUS.register(new EventHelper());
		FMLCommonHandler.instance().bus().register(new EventHelper());
	}

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event){
        if (event.entityLiving instanceof EntityPlayer && event.source != null && event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer && !(event.source.getEntity() instanceof FakePlayer)){
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            if (Users.hasPVPEnabled(((EntityPlayer) event.source.getEntity()).getDisplayName())){
                if (Users.hasPVPEnabled(((EntityPlayer) event.entityLiving).getDisplayName())){
                    event.setCanceled(false);
                }else{
                    event.setCanceled(true);
                    player.addChatMessage(new ChatComponentTranslation(SpecialChars.RED + "Both players must have PvP enabled!"));
                }
            }else{
                event.setCanceled(true);
                player.addChatMessage(new ChatComponentTranslation(SpecialChars.RED + "Both players must have PvP enabled!"));
            }
        }
    }


    @SubscribeEvent
    public void onPlayerDeath(PlayerDropsEvent event){
        if(PvPConfig.INSTANCE.getBool("keepInventoryOnPVPDeath") || PvPConfig.INSTANCE.getBool("keepExperienceOnPVPDeath")){
            if(event.source.getEntity() instanceof EntityPlayer && !(event.source.getEntity() instanceof FakePlayer)){
                if (Users.hasPVPEnabled(((EntityPlayer) event.source.getEntity()).getDisplayName())){
                    if (Users.hasPVPEnabled(((EntityPlayer) event.entityLiving).getDisplayName())){

                        NBTTagCompound entityData = event.entityPlayer.getEntityData();

                        // PvP Kill
                        entityData.setBoolean("killedByRealPlayer", true);

                        if(PvPConfig.INSTANCE.getBool("keepInventoryOnPVPDeath")){
                            NBTTagList inventory = new NBTTagList();

                            for(int currentIndex = 0; currentIndex < event.drops.size(); ++currentIndex) {
                                NBTTagCompound itemTag = new NBTTagCompound();
                                event.drops.get(currentIndex).getEntityItem().writeToNBT(itemTag);
                                inventory.appendTag(itemTag);
                            }

                            entityData.setTag("inventoryOnDeath", inventory);
                        }
                        if(PvPConfig.INSTANCE.getBool("keepExperienceOnPVPDeath")){
                            entityData.setFloat("experienceOnDeath", event.entityPlayer.experience);
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void OnPlayerRespawn(PlayerEvent.Clone event){
        if(event.wasDeath){
            NBTTagCompound entityData = event.original.getEntityData();
            // Only repopulate if killed by real player
            if (entityData.getBoolean("killedByRealPlayer")) {
                if(PvPConfig.INSTANCE.getBool("keepInventoryOnPVPDeath")){
                    NBTTagList inventory = entityData.getTagList("inventoryOnDeath", 10);
                    for(int i = 0; i < inventory.tagCount(); i++){
                        ItemStack created = ItemStack.loadItemStackFromNBT(inventory.getCompoundTagAt(i));
                        if(!event.entityPlayer.inventory.addItemStackToInventory(created)){
                            EntityItem ei = new EntityItem(event.entityPlayer.getEntityWorld());
                            ei.setEntityItemStack(created);
                            ei.setPosition(event.entity.chunkCoordX, event.entity.chunkCoordY, event.entity.chunkCoordZ);
                            event.entityPlayer.getEntityWorld().spawnEntityInWorld(ei);
                        }
                    }
                }
                if(PvPConfig.INSTANCE.getBool("keepExperienceOnPVPDeath")){
                    event.entityPlayer.experience = entityData.getFloat("experienceOnDeath");
                }
            }
        }
    }

    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {
        if(PvPConfig.INSTANCE.getBool("showMessageOnLogin")){
            if(!MinecraftServer.getServer().worldServerForDimension(event.player.dimension).isRemote) {
                if(Users.hasPVPEnabled(event.player.getDisplayName())){
                    event.player.addChatMessage(new ChatComponentText("PVPToggle is enabled on this server. You have PVP currently " + SpecialChars
                      .RED + "Enabled"));
                }else{
                    event.player.addChatMessage(new ChatComponentText("PVPToggle is enabled on this server. You have PVP currently " + SpecialChars
                      .GREEN + "Disabled"));
                }
            }
        }

        if(!MinecraftServer.getServer().worldServerForDimension(event.player.dimension).isRemote){
            NetworkHandler.sendToDimension(Users.createPacket(event.player.getGameProfile().getName()), event.player.dimension);
        }
    }

    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.END) {
            if(event.side.isServer()){
                Users.tickCoolDown();
            }
        }
    }

    @SubscribeEvent
    public void playerChangedDimension(PlayerChangedDimensionEvent event){
        if(PvpToggle.instance.dimensionSettings.containsKey(event.toDim)){
            if(!PvpToggle.instance.dimensionSettings.get(event.toDim).equals(PvPForced.NOTFORCED)){
                if(PvPConfig.INSTANCE.getBool("announceDimensionSetting")) {
                    event.player.addChatMessage(
                            new ChatComponentText(
                                    "This dimension has PvP forced to " + (PvpToggle.instance.dimensionSettings.get(event.toDim) == PvPForced.FORCEDON ? "On" : "Off")));
                }
            }
            Users.getUserByName(event.player.getGameProfile().getName()).setIsPvPForced(PvpToggle.instance.dimensionSettings.get(event.toDim));
            NetworkHandler.sendToDimension(Users.createPacket(event.player.getGameProfile().getName()), event.toDim);
        }else{
            Users.getUserByName(event.player.getGameProfile().getName()).setIsPvPForced(PvPForced.NOTFORCED);
            NetworkHandler.sendToDimension(Users.createPacket(event.player.getGameProfile().getName()), event.toDim);
        }
        //And, send him all the packets from all the users in the dimension.
        for (EntityPlayerMP player : (List<EntityPlayerMP>) MinecraftServer.getServer().worldServerForDimension(event.toDim).playerEntities) {
            NetworkHandler.sendTo(Users.createPacket(player.getGameProfile().getName()), (EntityPlayerMP) event.player);
        }
    }


    private List<PvPArea>                   areas          = Areas.getAreas();
    private HashMap<String, PvPArea>        players        = new HashMap<String, PvPArea>();
    private HashMap<String, EntityPlayerMP> playerEntities = new HashMap<String, EntityPlayerMP>();

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {

        if (event.phase == TickEvent.Phase.END) {
            //Only check every so often
            ticksPassed++;
            if (ticksPassed == 10) {
                ticksPassed = 0;
                //Check all areas

                players = new HashMap<String, PvPArea>();
                playerEntities = new HashMap<String, EntityPlayerMP>();

                for (World world : MinecraftServer.getServer().worldServers) {
                    for (EntityPlayerMP player : (List<EntityPlayerMP>) world.playerEntities) {
                        playerEntities.put(player.getGameProfile().getName(), player);
                        for (PvPArea area : Areas.getAreas()) {
                            if (area.contains((int) player.posX, (int) player.posY, (int) player.posZ)) {
                                players.put(player.getGameProfile().getName(), area);
                            }
                        }
                    }
                }

                for (User usr : Users.getUserList()) {
                    if (players.containsKey(usr.getUserName())) {
                        //Player is in an area
                        if (!usr.getIsInArea().equals(players.get(usr.getUserName()).getName())) {
                            //He just entered an area. Let's send packets etc.
                            Log.info("Player " + usr.getUserName() + " just entered area " + players.get(usr.getUserName()).getName());
                            usr.setIsInArea(players.get(usr.getUserName()).getName());
                            usr.setIsPvPForced((players.get(usr.getUserName()).getForced() ? PvPForced.FORCEDON : PvPForced.FORCEDOFF));

                            if(players.get(usr.getUserName()).getAnnounce()){
                                playerEntities.get(usr.getUserName()).addChatMessage( new ChatComponentText("You have entered area " + players.get(usr.getUserName()).getName() + ". Your PvP is forced to " + (players.get(
                                        usr.getUserName()).getForced() ? "On" : "Off")));
                            }

                            NetworkHandler.sendToDimension(Users.createPacket(usr.getUserName()), playerEntities.get(usr.getUserName()).dimension);
                        }
                    } else if(playerEntities.containsKey(usr.getUserName())) {
                        if (!usr.getIsInArea().equals("")) {
                            //He just left an area. Let's send packets etc.
                            Log.info("Player " + usr.getUserName() + " just left area " + usr.getIsInArea());

                            if(Areas.getAreaByName(usr.getIsInArea()).getAnnounce()){
                                playerEntities.get(usr.getUserName()).addChatMessage(new ChatComponentText("You have left area " + usr.getIsInArea() + ". Your PvP is set to " + (usr.getPVP() ? "On" : "Off")));
                            }

                            usr.setIsInArea("");
                            usr.setIsPvPForced(PvPForced.NOTFORCED);
                            NetworkHandler.sendToDimension(Users.createPacket(usr.getUserName()), playerEntities.get(usr.getUserName()).dimension);
                        }
                    } else{
                        //Player not logged in.
                    }
                }
            }
        }
    }
}

