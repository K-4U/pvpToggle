package k4unl.minecraft.pvpToggle.events;

import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.pvpToggle.PvpToggle;
import k4unl.minecraft.pvpToggle.api.PvPStatus;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.PvPArea;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import k4unl.minecraft.pvpToggle.lib.config.PvPConfig;
import k4unl.minecraft.pvpToggle.network.NetworkHandler;
import k4unl.minecraft.pvpToggle.network.packets.PacketPickedLocation;
import k4unl.minecraft.pvpToggle.network.packets.PacketPvPList;
import k4unl.minecraft.pvpToggle.server.Areas;
import k4unl.minecraft.pvpToggle.server.ServerHandler;
import k4unl.minecraft.pvpToggle.server.User;
import k4unl.minecraft.pvpToggle.server.Users;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ModInfo.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHelper {

    private static int ticksPassed = 0;
    private static HashMap<UUID, PvPArea> players = new HashMap<>();
    private static HashMap<UUID, PlayerEntity> playerEntities = new HashMap<>();

    public static void init(final FMLCommonSetupEvent event) {
//        Log.info("Initializing event helper");
        MinecraftForge.EVENT_BUS.register(EventHelper.class);
        /*MinecraftForge.EVENT_BUS.addListener(EventHelper::onPlayerInteractEvent);
        MinecraftForge.EVENT_BUS.addListener(EventHelper::onLivingAttack);
        MinecraftForge.EVENT_BUS.addListener(EventHelper::onPlayerDeath);
        MinecraftForge.EVENT_BUS.addListener(EventHelper::onPlayerRespawn);
        MinecraftForge.EVENT_BUS.addListener(EventHelper::loggedInEvent);
//        MinecraftForge.EVENT_BUS.addListener(EventHelper::tickPlayer);
        MinecraftForge.EVENT_BUS.addListener(EventHelper::playerChangedDimension);
        MinecraftForge.EVENT_BUS.addListener(EventHelper::serverTickEvent);*/
    }

    @SubscribeEvent
    public static void onPlayerInteractEvent(PlayerInteractEvent.LeftClickBlock event) {

        if (event.getItemStack().getItem() == Items.DIAMOND_HOE && event.getEntityLiving() instanceof PlayerEntity) {
            if (ServerHandler.getPlayersPicking().contains(((PlayerEntity) event.getEntityLiving()).getGameProfile().getId())) {
                event.setCanceled(true); //First things first, stop whatever it was gonna do
                //Now, send this position to the client:
                Location loc = new Location(event.getPos());
                NetworkHandler.getInstance().sendTo(new PacketPickedLocation(loc), (ServerPlayerEntity) event.getPlayer());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {

        if (event.getEntityLiving() instanceof PlayerEntity && event.getSource() != null && event.getSource() instanceof EntityDamageSource && event.getSource().getTrueSource() instanceof PlayerEntity && !(event.getSource().getTrueSource() instanceof FakePlayer)) {
            PlayerEntity source = (PlayerEntity) event.getSource().getTrueSource();
            PlayerEntity target = (PlayerEntity) event.getEntityLiving();
            if (Users.hasPVPEnabled(source.getGameProfile().getId())) {
                if (!Users.hasPVPEnabled(target.getGameProfile().getId())) {
                    event.setCanceled(true);
                    source.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Both players must have PvP enabled!"), true);
                }
            } else {
                event.setCanceled(true);
                source.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Both players must have PvP enabled!"), true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDropsEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }

        if (PvPConfig.keepInventoryOnPVPDeath.get() || PvPConfig.keepExperienceOnPVPDeath.get()) {
            if (!(event.getSource() instanceof EntityDamageSource || !(event.getSource().getTrueSource() instanceof PlayerEntity))) {
                return;
            }
            PlayerEntity source = (PlayerEntity) event.getSource().getTrueSource();
            PlayerEntity target = (PlayerEntity) event.getEntityLiving();

            if (!(source instanceof FakePlayer)) {
                if (Users.hasPVPEnabled(source.getGameProfile().getId())) {
                    if (Users.hasPVPEnabled(target.getGameProfile().getId())) {

                        CompoundNBT entityData = target.getPersistentData();

                        // PvP Kill
                        entityData.putBoolean("killedByRealPlayer", true);

                        if (PvPConfig.keepInventoryOnPVPDeath.get()) {
                            ListNBT inventory = event.getDrops().stream().map(IForgeEntity::serializeNBT).collect(Collectors.toCollection(ListNBT::new));

                            entityData.put("inventoryOnDeath", inventory);
                        }
                        if (PvPConfig.keepExperienceOnPVPDeath.get()) {
                            entityData.putFloat("experienceOnDeath", target.experience);
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {

        if (event.isWasDeath()) {
            PlayerEntity target = event.getPlayer();
            CompoundNBT entityData = target.getPersistentData();
            // Only repopulate if killed by real player
            if (entityData.getBoolean("killedByRealPlayer")) {
                if (PvPConfig.keepInventoryOnPVPDeath.get()) {
                    ListNBT inventory = entityData.getList("inventoryOnDeath", 10);
                    for (int i = 0; i < inventory.size(); i++) {
                        CompoundNBT compound = inventory.getCompound(i);

                        ItemStack created = ItemStack.read(compound.getCompound("Item"));
                        if (!target.inventory.addItemStackToInventory(created)) { //Spawn in world what we couldn't fit
                            ItemEntity ei = new ItemEntity(target.getEntityWorld(), event.getEntity().chunkCoordX, event.getEntity().chunkCoordY, event.getEntity().chunkCoordZ, created);
                            target.getEntityWorld().addEntity(ei);
                        }
                    }
                }
                if (PvPConfig.keepExperienceOnPVPDeath.get()) {
                    target.experience = entityData.getFloat("experienceOnDeath");
                }
            }
        }
    }

    @SubscribeEvent
    public static void loggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().world.isRemote) {
            if (PvPConfig.showMessageOnLogin.get()) {
                if (Users.hasPVPEnabled(event.getPlayer().getGameProfile().getId())) {
                    event.getPlayer().sendMessage(new StringTextComponent("PVPToggle is enabled on this server. You have PVP currently " + TextFormatting
                            .RED + "Enabled"));
                } else {
                    event.getPlayer().sendMessage(new StringTextComponent("PVPToggle is enabled on this server. You have PVP currently " + TextFormatting
                            .GREEN + "Disabled"));
                }
            }

            PvpToggle.networkHandler.sendToDimension(Users.createPacket(event.getPlayer().getGameProfile().getId()), event.getPlayer().dimension);

            //Send packet for all the users on the server
            PacketPvPList toSend = new PacketPvPList();
            for (PlayerEntity player : event.getPlayer().world.getPlayers()) {
                Users.addToPvpList(toSend, player.getGameProfile().getId());
            }
            PvpToggle.networkHandler.sendTo(toSend, (ServerPlayerEntity) event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void tickPlayer(TickEvent.PlayerTickEvent event) {


    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {

        ServerHandler.updatePlayerToDimension(event.getTo(), event.getPlayer());
        //And, send him all a packet from all the users in the dimension.
        PacketPvPList toSend = new PacketPvPList();
        for (PlayerEntity player : Functions.getWorldServerForDimensionType(event.getTo()).getPlayers()) {
            Users.addToPvpList(toSend, player.getGameProfile().getId());
        }
        PvpToggle.networkHandler.sendTo(toSend, (ServerPlayerEntity) event.getPlayer());

    }

    @SubscribeEvent
    public static void serverTickEvent(TickEvent.ServerTickEvent event) {

        if (event.phase == TickEvent.Phase.END) {
            if (event.side.isServer()) {
                //TODO: This could move to a custom thread.
                Log.info("Ticking user");
                Users.tickCoolDown();
            }

            //Only check every so often
            ticksPassed++;
            if (ticksPassed == 10) {
                ticksPassed = 0;
                //Check all areas

                players = new HashMap<>();
                playerEntities = new HashMap<>();

                for (World world : Functions.getServer().getWorlds()) {
                    for (PlayerEntity player : world.getPlayers()) {
                        playerEntities.put(player.getGameProfile().getId(), player);
                        for (PvPArea area : Areas.getAreas()) {
                            if (area.contains((int) player.posX, (int) player.posY, (int) player.posZ)) {
                                players.put(player.getGameProfile().getId(), area);
                            }
                        }
                    }
                }

                for (User usr : Users.getUserList()) {
                    if (players.containsKey(usr.getUuid())) {
                        //Player is in an area
                        if (!usr.getIsInArea().equals(players.get(usr.getUuid()).getName())) {
                            //He just entered an area. Let's send packets etc.
                            //Log.info("Player " + usr.getUserName() + " just entered area " + players.get(usr.getUserName()).getName());
                            usr.setIsInArea(players.get(usr.getUuid()).getName());
                            usr.setIsPvPForced((players.get(usr.getUuid()).getForced() ? PvPStatus.FORCEDON : PvPStatus.FORCEDOFF));

                            if (players.get(usr.getUuid()).getAnnounce()) {
                                Functions.displayTitleMessage(STitlePacket.Type.TITLE, (ServerPlayerEntity) playerEntities.get(usr.getUuid()), new StringTextComponent(players.get(usr.getUuid()).getName()));
                                Functions.displayTitleMessage(STitlePacket.Type.SUBTITLE, (ServerPlayerEntity) playerEntities.get(usr.getUuid()), new StringTextComponent("Your PvP is forced to " + (players.get(usr.getUuid()).getForced() ? "On" : "Off")));
                                /*playerEntities.get(usr.getUserName()).sendMessage(new TextComponentString("You have entered area " + players.get(usr.getUserName()).getName() + ". Your PvP is forced to " + (players.get(
                                        usr.getUserName()).getForced() ? "On" : "Off")));*/
                            }

                            PvpToggle.networkHandler.sendToDimension(Users.createPacket(usr.getUuid()), playerEntities.get(usr.getUuid()).dimension);
                        }
                    } else if (playerEntities.containsKey(usr.getUuid())) {
                        if (usr == null) {
                            Log.error("NULL");
                        }
                        if (!usr.getIsInArea().equals("")) {
                            if (Areas.getAreaByName(usr.getIsInArea()) == null)
                                return;
                            //He just left an area. Let's send packets etc.
                            //Log.info("Player " + usr.getUserName() + " just left area " + usr.getIsInArea());

                            //Now check if the dimension they're in requires a force:
                            DimensionType dimension = playerEntities.get(usr.getUuid()).dimension;
                            if (ServerHandler.getDimensionSettings().containsKey(dimension)) {
                                if (!ServerHandler.getDimensionSettings().get(dimension).equals(PvPStatus.NOTFORCED)) {
                                    usr.setIsPvPForced(ServerHandler.getDimensionSettings().get(dimension));
                                } else {
                                    usr.setIsPvPForced(PvPStatus.NOTFORCED);
                                }
                            } else {
                                usr.setIsPvPForced(PvPStatus.NOTFORCED);
                            }

                            if (Areas.getAreaByName(usr.getIsInArea()).getAnnounce()) {
                                if (usr.getPvpStatus().equals(PvPStatus.NOTFORCED)) {
                                    playerEntities.get(usr.getUuid()).sendStatusMessage(
                                            new StringTextComponent("You have left area " + usr.getIsInArea() + ". Your PvP is set to " + (usr.getPVP() ? "On" : "Off")), true);
                                } else {
                                    playerEntities.get(usr.getUuid()).sendStatusMessage(
                                            new StringTextComponent("You have left area " + usr.getIsInArea() + ". Your PvP is forced to " + (usr.getPvpStatus().equals(PvPStatus.FORCEDON) ? "On" : "Off")), true);
                                }
                            }

                            usr.setIsInArea("");

                            PvpToggle.networkHandler.sendToDimension(Users.createPacket(usr.getUuid()), playerEntities.get(usr.getUuid()).dimension);
                        }
                    }
                }
            }
        }
    }
}

