package k4unl.minecraft.pvpToggle.events;

import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.*;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import k4unl.minecraft.pvpToggle.network.packets.PacketPvPList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class EventHelper {
    
    private int ticksPassed = 0;
    
    public static void init() {
        
        MinecraftForge.EVENT_BUS.register(new EventHelper());
    }
    
    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        
        if (event.getEntityLiving() instanceof EntityPlayer && event.getSource() != null && event.getSource() instanceof EntityDamageSource && ((EntityDamageSource) event.getSource()).getTrueSource() instanceof EntityPlayer && !(((EntityDamageSource) event.getSource()).getTrueSource() instanceof FakePlayer)) {
            EntityPlayer source = (EntityPlayer) ((EntityDamageSource) event.getSource()).getTrueSource();
            if (Users.hasPVPEnabled(source.getName())) {
                if (!Users.hasPVPEnabled(event.getEntityLiving().getName())) {
                    event.setCanceled(true);
                    source.sendMessage(new TextComponentTranslation(TextFormatting.RED + "Both players must have PvP enabled!"));
                }
            } else {
                event.setCanceled(true);
                source.sendMessage(new TextComponentTranslation(TextFormatting.RED + "Both players must have PvP enabled!"));
            }
        }
    }
    
    
    @SubscribeEvent
    public void onPlayerDeath(PlayerDropsEvent event) {
        
        if (PvPConfig.INSTANCE.getBool("keepInventoryOnPVPDeath") || PvPConfig.INSTANCE.getBool("keepExperienceOnPVPDeath")) {
            if (!(event.getSource() instanceof EntityDamageSource || !(((EntityDamageSource) event.getSource()).getTrueSource() instanceof EntityPlayer))) {
                return;
            }
            EntityPlayer source = (EntityPlayer) ((EntityDamageSource) event.getSource()).getTrueSource();
            
            if (!(source instanceof FakePlayer)) {
                if (Users.hasPVPEnabled(source.getName())) {
                    if (Users.hasPVPEnabled(((EntityPlayer) event.getEntityLiving()).getName())) {
                        
                        NBTTagCompound entityData = event.getEntityPlayer().getEntityData();
                        
                        // PvP Kill
                        entityData.setBoolean("killedByRealPlayer", true);
                        
                        if (PvPConfig.INSTANCE.getBool("keepInventoryOnPVPDeath")) {
                            NBTTagList inventory = new NBTTagList();
                            
                            for (int currentIndex = 0; currentIndex < event.getDrops().size(); ++currentIndex) {
                                NBTTagCompound itemTag = new NBTTagCompound();
                                event.getDrops().get(currentIndex).getItem().writeToNBT(itemTag);
                                inventory.appendTag(itemTag);
                            }
                            
                            entityData.setTag("inventoryOnDeath", inventory);
                        }
                        if (PvPConfig.INSTANCE.getBool("keepExperienceOnPVPDeath")) {
                            entityData.setFloat("experienceOnDeath", event.getEntityPlayer().experience);
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void OnPlayerRespawn(PlayerEvent.Clone event) {
        
        if (event.isWasDeath()) {
            NBTTagCompound entityData = event.getOriginal().getEntityData();
            // Only repopulate if killed by real player
            if (entityData.getBoolean("killedByRealPlayer")) {
                if (PvPConfig.INSTANCE.getBool("keepInventoryOnPVPDeath")) {
                    NBTTagList inventory = entityData.getTagList("inventoryOnDeath", 10);
                    for (int i = 0; i < inventory.tagCount(); i++) {
                        ItemStack created = new ItemStack(inventory.getCompoundTagAt(i));
                        if (!event.getEntityPlayer().inventory.addItemStackToInventory(created)) {
                            EntityItem ei = new EntityItem(event.getEntityPlayer().getEntityWorld());
                            ei.setItem(created);
                            ei.setPosition(event.getEntity().chunkCoordX, event.getEntity().chunkCoordY, event.getEntity().chunkCoordZ);
                            event.getEntityPlayer().getEntityWorld().spawnEntity(ei);
                        }
                    }
                }
                if (PvPConfig.INSTANCE.getBool("keepExperienceOnPVPDeath")) {
                    event.getEntityPlayer().experience = entityData.getFloat("experienceOnDeath");
                }
            }
        }
    }
    
    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {
        
        if (PvPConfig.INSTANCE.getBool("showMessageOnLogin")) {
            if (!Functions.getServer().getWorld(event.player.dimension).isRemote) {
                if (Users.hasPVPEnabled(event.player.getName())) {
                    event.player.sendMessage(new TextComponentString("PVPToggle is enabled on this server. You have PVP currently " + TextFormatting
                            .RED + "Enabled"));
                } else {
                    event.player.sendMessage(new TextComponentString("PVPToggle is enabled on this server. You have PVP currently " + TextFormatting
                            .GREEN + "Disabled"));
                }
            }
        }
        
        if (!Functions.getServer().getWorld(event.player.dimension).isRemote) {
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(event.player.getGameProfile().getName()), event.player.dimension);
            
            //Send packet for all the users on the server
            PacketPvPList toSend = new PacketPvPList();
            for (EntityPlayer player : Functions.getServer().getWorld(event.player.dimension).playerEntities) {
                Users.addToPvpList(toSend, player.getGameProfile().getName());
            }
            PvpToggle.networkHandler.sendTo(toSend, (EntityPlayerMP) event.player);
        }
    }
    
    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent event) {
        
        if (event.phase == TickEvent.Phase.END) {
            if (event.side.isServer()) {
                //TODO: This could move to a custom thread.
                Users.tickCoolDown();
            }
        }
    }
    
    @SubscribeEvent
    public void playerChangedDimension(PlayerChangedDimensionEvent event) {
        
        if (PvpToggle.instance.dimensionSettings.containsKey(event.toDim)) {
            if (!PvpToggle.instance.dimensionSettings.get(event.toDim).equals(PvPStatus.NOTFORCED)) {
                if (PvPConfig.INSTANCE.getBool("announceDimensionSetting")) {
                    event.player.sendMessage(
                            new TextComponentString(
                                    "This dimension has PvP forced to " + (PvpToggle.instance.dimensionSettings.get(event.toDim).equals(PvPStatus.FORCEDON) ? "On" : "Off")));
                }
            }
            Users.getUserByName(event.player.getGameProfile().getName()).setIsPvPForced(PvpToggle.instance.dimensionSettings.get(event.toDim));
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(event.player.getGameProfile().getName()), event.toDim);
        } else {
            //Remember his old setting.
            Users.getUserByName(event.player.getGameProfile().getName()).setIsPvPForced(PvPStatus.OFF);
            PvpToggle.networkHandler.sendToDimension(Users.createPacket(event.player.getGameProfile().getName()), event.toDim);
        }
        //And, send him all a packet from all the users in the dimension.
        PacketPvPList toSend = new PacketPvPList();
        for (EntityPlayer player : Functions.getServer().getWorld(event.toDim).playerEntities) {
            Users.addToPvpList(toSend, player.getGameProfile().getName());
        }
        PvpToggle.networkHandler.sendTo(toSend, (EntityPlayerMP) event.player);
        
    }
    
    
    private HashMap<String, PvPArea>      players        = new HashMap<>();
    private HashMap<String, EntityPlayer> playerEntities = new HashMap<>();
    
    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {
        
        if (event.phase == TickEvent.Phase.END) {
            //Only check every so often
            ticksPassed++;
            if (ticksPassed == 10) {
                ticksPassed = 0;
                //Check all areas
                
                players = new HashMap<>();
                playerEntities = new HashMap<>();
                
                for (World world : Functions.getServer().worlds) {
                    for (EntityPlayer player : world.playerEntities) {
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
                            //Log.info("Player " + usr.getUserName() + " just entered area " + players.get(usr.getUserName()).getName());
                            usr.setIsInArea(players.get(usr.getUserName()).getName());
                            usr.setIsPvPForced((players.get(usr.getUserName()).getForced() ? PvPStatus.FORCEDON : PvPStatus.FORCEDOFF));
                            
                            if (players.get(usr.getUserName()).getAnnounce()) {
                                Functions.displayTitleMessage(SPacketTitle.Type.TITLE, (EntityPlayerMP) playerEntities.get(usr.getUserName()), new TextComponentString(players.get(usr.getUserName()).getName()));
                                Functions.displayTitleMessage(SPacketTitle.Type.SUBTITLE, (EntityPlayerMP) playerEntities.get(usr.getUserName()), new TextComponentString("Your PvP is forced to " + (players.get(usr.getUserName()).getForced() ? "On" : "Off")));
                                /*playerEntities.get(usr.getUserName()).sendMessage(new TextComponentString("You have entered area " + players.get(usr.getUserName()).getName() + ". Your PvP is forced to " + (players.get(
                                        usr.getUserName()).getForced() ? "On" : "Off")));*/
                            }
                            
                            PvpToggle.networkHandler.sendToDimension(Users.createPacket(usr.getUserName()), playerEntities.get(usr.getUserName()).dimension);
                        }
                    } else if (playerEntities.containsKey(usr.getUserName())) {
                        if (usr == null) {
                            Log.error("NULL");
                        }
                        if (!usr.getIsInArea().equals("")) {
                            if (Areas.getAreaByName(usr.getIsInArea()) == null)
                                return;
                            //He just left an area. Let's send packets etc.
                            //Log.info("Player " + usr.getUserName() + " just left area " + usr.getIsInArea());
                            
                            //Now check if the dimension they're in requires a force:
                            int dimension = playerEntities.get(usr.getUserName()).dimension;
                            if (PvpToggle.instance.dimensionSettings.containsKey(dimension)) {
                                if (!PvpToggle.instance.dimensionSettings.get(dimension).equals(PvPStatus.NOTFORCED)) {
                                    usr.setIsPvPForced(PvpToggle.instance.dimensionSettings.get(dimension));
                                } else {
                                    usr.setIsPvPForced(PvPStatus.NOTFORCED);
                                }
                            } else {
                                usr.setIsPvPForced(PvPStatus.NOTFORCED);
                            }
                            
                            if (Areas.getAreaByName(usr.getIsInArea()).getAnnounce()) {
                                if (usr.getPvpStatus().equals(PvPStatus.NOTFORCED)) {
                                    playerEntities.get(usr.getUserName()).sendMessage(
                                            new TextComponentString("You have left area " + usr.getIsInArea() + ". Your PvP is set to " + (usr.getPVP() ? "On" : "Off")));
                                } else {
                                    playerEntities.get(usr.getUserName()).sendMessage(
                                            new TextComponentString("You have left area " + usr.getIsInArea() + ". Your PvP is forced to " + (usr.getPvpStatus().equals(PvPStatus.FORCEDON) ? "On" : "Off")));
                                }
                            }
                            
                            usr.setIsInArea("");
                            
                            PvpToggle.networkHandler.sendToDimension(Users.createPacket(usr.getUserName()), playerEntities.get(usr.getUserName()).dimension);
                        }
                    }
                }
            }
        }
    }
}

