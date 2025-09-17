package me.SavageUser.TutorialExitPortal;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Core extends JavaPlugin implements Listener {

    public ConfigUtil config;

    @Override
    public void onEnable() {
        this.config = new ConfigUtil(new File(this.getDataFolder(), "config.yml"));
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("settutorialexit").setExecutor(this);
        getCommand("setdestination").setExecutor(this);

    }

    @Override
    public void onDisable() {}

    public void setupTutorialExitPoint(Player player) {
        try {
            this.config.setProperty("TP-Points.TutorialExit.x", player.getLocation().getBlockX());
            this.config.setProperty("TP-Points.TutorialExit.y", player.getLocation().getBlockY());
            this.config.setProperty("TP-Points.TutorialExit.z", player.getLocation().getBlockZ());
            this.config.save();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void setDestinationTP(Player player) {
        try {
            this.config.setProperty("TP-Points.Destination.x", player.getLocation().getBlockX());
            this.config.setProperty("TP-Points.Destination.y", player.getLocation().getBlockY());
            this.config.setProperty("TP-Points.Destination.z", player.getLocation().getBlockZ());
            this.config.setProperty("TP-Points.Destination.yaw", player.getLocation().getYaw());
            this.config.setProperty("TP-Points.Destination.pitch", player.getLocation().getPitch());
            this.config.save();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (this.config.getConfigOption("TP-Points.TutorialExit.x") == null && this.config.getConfigOption("TP-Points.TutorialExit.y") == null && this.config.getConfigOption("TP-Points.TutorialExit.z") == null) {
            return;
        }

        if (this.config.getConfigOption("TP-Points.Destination.x") == null && this.config.getConfigOption("TP-Points.Destination.y") == null && this.config.getConfigOption("TP-Points.Destination.z") == null && this.config.getConfigOption("TP-Points.Destination.yaw") == null && this.config.getConfigOption("TP-Points.Destination.pitch") == null) {
            return;
        }

        int x = (int) this.config.getConfigOption("TP-Points.TutorialExit.x");
        int y = (int) this.config.getConfigOption("TP-Points.TutorialExit.y");
        int z = (int) this.config.getConfigOption("TP-Points.TutorialExit.z");

        if (event.getTo().getBlockX() == x && event.getTo().getBlockY() == y && event.getTo().getBlockZ() == z) {
            int destX = (int) this.config.getConfigOption("TP-Points.Destination.x");
            int destY = (int) this.config.getConfigOption("TP-Points.Destination.y");
            int destZ = (int) this.config.getConfigOption("TP-Points.Destination.z");
            float destYaw = (float) this.config.getConfigOption("TP-Points.Destination.yaw");
            float destPitch = (float) this.config.getConfigOption("TP-Points.Destination.pitch");

            player.teleport(new Location(player.getWorld(), destX, destY, destZ, destYaw, destPitch));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("settutorialexit")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (player.isOp() || player.hasPermission("TutorialExitPortal.SetTutorialExit")) {
                    if (args.length != 0) {
                        player.sendMessage("§cUsage: /settutorialexit");
                        return true;
                    }

                    setupTutorialExitPoint(player);
                    player.sendMessage("§aTutorial exit point set!");
                    player.sendMessage("§aFind an open spot to run §b/setdestination §aat!");
                    return true;
                }
                else {
                    player.sendMessage("§cI'm sorry, Dave. I'm afraid I can't do that.");
                    return true;
                }
            }
            else {
                sender.sendMessage("Command can only be executed by a player!");
                return true;
            }

        }

        if (cmd.getName().equalsIgnoreCase("setdestination")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (player.isOp() || player.hasPermission("TutorialExitPortal.SetDestination")) {
                    if (args.length != 0) {
                        player.sendMessage("§cUsage: /setdestination");
                        return true;
                    }

                    if (this.config.getConfigOption("TP-Points.TutorialExit.x") == null && this.config.getConfigOption("TP-Points.TutorialExit.y") == null && this.config.getConfigOption("TP-Points.TutorialExit.z") == null) {
                        player.sendMessage("§cCannot create destination point as the exit point is not set!");
                        return true;
                    }

                    setDestinationTP(player);
                    player.sendMessage("§aTutorial destination point set!");
                    return true;
                }
                else {
                    player.sendMessage("§cI'm sorry, Dave. I'm afraid I can't do that.");
                    return true;
                }
            }
            else {
                sender.sendMessage("Command can only be executed by a player!");
                return true;
            }
        }
        return true;
    }
}
