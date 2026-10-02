package net.noname18_.gayjumptest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class Gayjumptest extends JavaPlugin implements Listener {
    public final Set<UUID> nukeConf = new HashSet<>();

    private Ranks ranks;
    private Punishments punishments;
    private OneJump onejump;
    private SuggestionCompleter suggestionCompleter;
    private UseItem useitem;

    @Override
    public void onEnable() {
        punishments = new Punishments(this);

        onejump = new OneJump(this);
        ranks = new Ranks(this, onejump);
        suggestionCompleter = new SuggestionCompleter(onejump);
        useitem = new UseItem(onejump);

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new GIveItemsJoin(), this);
        getServer().getPluginManager().registerEvents(useitem, this);
        getServer().getPluginManager().registerEvents(ranks, this);
        getServer().getPluginManager().registerEvents(new ChatPrefixes(ranks, punishments, onejump, this, useitem), this);
        getServer().getPluginManager().registerEvents(punishments, this);
        getServer().getPluginManager().registerEvents(onejump, this);

        onejump.setRanks(ranks);

        getCommand("oj").setTabCompleter(suggestionCompleter);
        getCommand("kick").setTabCompleter(suggestionCompleter);
        getCommand("mute").setTabCompleter(suggestionCompleter);
        getCommand("unmute").setTabCompleter(suggestionCompleter);
        getCommand("ban").setTabCompleter(suggestionCompleter);
        getCommand("unban").setTabCompleter(suggestionCompleter);
        getCommand("rank").setTabCompleter(suggestionCompleter);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {


        if (command.getName().equalsIgnoreCase("smite")) {
            sender.sendMessage("§cfuck you get trolled :3");
            return true;
        } else if (command.getName().equalsIgnoreCase("rank")) {

            if (!sender.hasPermission("rank.set") || !sender.hasPermission("op")) {
                sender.sendMessage("§cYou have no permission to do that!");
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("§cInvalid arguments! Usage: /rank <player> <rank>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("§cInvalid player! Usage: /rank <player> <rank>");
                return true;
            }

            Ranks.Rank rank;

            try {
                rank = Ranks.Rank.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cInvalid rank! Usage: /rank <player> <rank>");
                return true;
            }

            ranks.setRank(target, rank);

            ranks.tablogic(target);

            sender.sendMessage(String.format(
                    "§aSuccess! Set %s's rank to %s!",
                    target.getName(),
                    rank
            ));

            return true;
        } else if (command.getName().equalsIgnoreCase("mute")) {
            if (!sender.hasPermission("mute.use")) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cYou have no permission to use this!");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid arguments! Usage: /mute <player> <duration(s/m/h/d/w/mo)> <reason>");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid player! Usage: /mute <player> <duration(s/m/h/d/w/mo)> <reason>");
                return true;
            }
            punishments.mute(target, args[1], args[2]);
        } else if (command.getName().equalsIgnoreCase("unmute")) {
            if (!sender.hasPermission("mute.use")) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cYou have no permission to use this!");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid arguments! Usage: /unmute <player>");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid player! /unmute <player>");
                return true;
            }
            punishments.unmute(target);
        } else if (command.getName().equalsIgnoreCase("kick")) {
            if (!sender.hasPermission("kick.use")) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cYou have no permission to use this!");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid arguments! /kick <player> <reason>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid player! /kick <player> <reason>");
                return true;
            }
            punishments.kick(target, args[1]);
        } else if (command.getName().equalsIgnoreCase("ban")) {
            if (!sender.hasPermission("ban.use")) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cYou have no permission to use this!");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid arguments! /ban <player> <duration(s/m/h/d/w/mo)> <reason>");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid player! /ban <player> <duration(s/m/h/d/w/mo)> <reason>");
                return true;
            }
            punishments.ban(target, args[1], args[2]);

        } else if (command.getName().equalsIgnoreCase("unban")) {
            if (!sender.hasPermission("unban.use")) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cYou have no permission to use this! /unban <player>");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid arguments! /unban <player>");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§8§l[§c§lPUNISHMENTS§8§l] §cInvalid player! /unban <player>");
                return true;
            }
            punishments.unban(target);
        } else if (command.getName().equalsIgnoreCase("oj")) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!sender.hasPermission("rank.set")) {
                    sender.sendMessage("§cYou have no permission to do that!");
                    return true;
                }

                if (args.length != 3) {
                    sender.sendMessage("§cInvalid arguments!");
                    return true;
                }

                if (!(sender instanceof Player player)) {
                    return false;
                }
                if (args[1].endsWith("+") || args[1].endsWith("-")) {
                    String difficulti = args[1].substring(0, args[1].length() - 1);
                    if (0 > Integer.valueOf(difficulti) || Integer.valueOf(difficulti) > 12) {
                        sender.sendMessage("§cInvalid difficulty! Choose one from 0-12. You may add a + or - at the end to further specify it!");
                        return false;
                    }
                } else {
                    int difficulti = Integer.valueOf(args[1]);
                    if (0 > difficulti || difficulti > 12) {
                        sender.sendMessage("§cInvalid difficulty! Choose one from 0-12. You may add a + or - at the end to further specify it!");
                        return false;
                    }
                }
                sender.sendMessage(String.format("§aSuccessfully created Jump %s!", String.valueOf(onejump.jump)));
                onejump.create(player, args[1], args[2]);

            } else if (args[0].equalsIgnoreCase("delete")) {
                if (!sender.hasPermission("rank.set")) {
                    sender.sendMessage("§cYou have no permission to do that!");
                    return true;
                }

                if (args.length != 2) {
                    sender.sendMessage("§cInvalid arguments!");
                    sender.sendMessage(args.length + " arg");
                    return true;
                }

                if (!(sender instanceof Player player)) {
                    return false;
                }

                if (!sender.hasPermission("op")) {
                    sender.sendMessage("§cYou have no permission to do that!");
                    return true;
                } else {
                    if (args[1].equalsIgnoreCase("all")) {
                        sender.sendMessage("§c§lWARNING! This command will delete EVERY jump, resulting in the death and inevitable doom of the server. If you wish to proceed, type this phrase in chat: \n \n§cYes, I agree to NUKE the entire server and make it a barren wasteland nobody will visit anymore.");
                        nukeConf.add(player.getUniqueId());
                        return true;
                    }
                }

                if (Integer.parseInt(args[1]) < 1 || Integer.parseInt(args[1]) > onejump.jump) {
                    sender.sendMessage("§cInvalid jump number! Correct usage: /oj delete <jumpNumber>");
                    return true;
                } else {
                    if (!onejump.jumpNames.containsKey(Integer.valueOf(args[1]))) {
                        sender.sendMessage("§cThat jump does not exist! Perhaps try creating it via /oj create <difficulty> <name>?");
                        return true;
                    }
                    sender.sendMessage(String.format("§aSuccessfully deleted Jump %s!", args[1]));
                    onejump.delete(Integer.valueOf(args[1]));
                    return true;
                }

            } else if (args[0].equalsIgnoreCase("checkpoint")) {
                if (!sender.hasPermission("rank.set")) {
                    sender.sendMessage("§cYou have no permission to do that!");
                    return true;
                }

                if (args.length != 1) {
                    sender.sendMessage("§cInvalid arguments! Usage: /oj checkpoint");
                    return true;
                }

                Player player = ((Player) sender).getPlayer();

                if (player == null) {
                    return false;
                }

                onejump.checkpointCreate(player);
                sender.sendMessage("§aSuccessfully created a checkpoint at your location!");
            } else if (args[0].equalsIgnoreCase("finish")) {
                if (!sender.hasPermission("rank.set")) {
                    sender.sendMessage("§cYou have no permission to do that!");
                    return true;
                }

                if (args.length != 2) {
                    sender.sendMessage("§cInvalid arguments! Usage: /oj finish <jumpNumber>");
                    return true;
                }

                Player player = ((Player) sender).getPlayer();

                if (player == null) {
                    return false;
                }

                if (Integer.parseInt(args[1]) < 1 || Integer.parseInt(args[1]) > onejump.jump - 1) {
                    sender.sendMessage("§cInvalid jump number! Correct usage: /oj finish <jumpNumber>");
                    return true;
                } else {
                    Location location = player.getLocation();
                    onejump.finishCreate(location, Integer.valueOf(args[1]), player);
                }
            } else if (args[0].equalsIgnoreCase("warp")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cOnly players can use this command!");
                    return true;
                }

                if (args.length != 2) {
                    player.sendMessage("§cInvalid arguments! Usage: /oj warp <jumpNumber>");
                    return true;
                }

                int jumpNo;

                try {
                    jumpNo = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cJump number must be a number!");
                    return true;
                }

                Location location = onejump.jumpLocations.get(jumpNo);

                if (location == null) {
                    player.sendMessage("§cThat jump does not exist!");
                    return true;
                }

                player.teleport(location);
                player.sendMessage("§aWarped to Jump " + jumpNo + "!");
                return true;
            } else if (args[0].equalsIgnoreCase("complete")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cOnly players can use this command!");
                    return true;
                }

                if (args.length != 3) {
                    player.sendMessage("§cInvalid arguments! Usage: /oj complete <player>");
                    return true;
                }

                int jumpNo;

                try {
                    jumpNo = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cJump number must be a number!");
                    return true;
                }

                if (jumpNo < 1 || jumpNo > onejump.jump - 1) {
                    player.sendMessage("§cInvalid jump!");
                    return true;
                }

                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);

                Set<Integer> completed = onejump.completed.get(targetPlayer.getUniqueId());
                if (completed == null) {
                    player.sendMessage("§cInvalid player!");
                    return true;
                }

                if (completed.contains(jumpNo)) {
                    player.sendMessage("§cThis player has already completed this jump!");
                    return true;
                }


                player.sendMessage(String.format("§aSuccessfully completed Jump %s for %s!", String.valueOf(jumpNo), targetPlayer.getName()));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                completed.add(jumpNo);
                onejump.completed.put(targetPlayer.getUniqueId(), completed);
                onejump.recalculateOjp(targetPlayer.getUniqueId());
                if (targetPlayer.isOnline()) {
                    Player onlinePlayer = Bukkit.getPlayer(Objects.requireNonNull(targetPlayer.getPlayer()).getUniqueId());
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ITEM_TOTEM_USE, 100, 0);
                    onlinePlayer.sendMessage(String.format("§aCompleted Jump %s!", String.valueOf(jumpNo)));

                    ranks.tablogic(onlinePlayer);
                }

            } else if (args[0].equalsIgnoreCase("uncomplete")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cOnly players can use this command!");
                    return true;
                }

                if (!sender.hasPermission("rank.set")) {
                    sender.sendMessage("§cYou have no permission to do that!");
                    return true;
                }

                if (args.length != 3) {
                    player.sendMessage("§cInvalid arguments! Usage: /oj uncomplete <player>");
                    return true;
                }

                int jumpNo;

                try {
                    jumpNo = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cJump number must be a number!");
                    return true;
                }

                if (jumpNo < 1 || jumpNo > onejump.jump - 1) {
                    player.sendMessage("§cInvalid jump!");
                    return true;
                }

                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);

                Set<Integer> completed = onejump.completed.get(targetPlayer.getUniqueId());
                if (completed == null) {
                    player.sendMessage("§cInvalid player!");
                    return true;
                }

                if (!completed.contains(jumpNo)) {
                    player.sendMessage("§cThis player hasn't completed this jump!");
                    return true;
                }

                player.sendMessage(String.format("§aSuccessfully uncompleted Jump %s for %s!", String.valueOf(jumpNo), targetPlayer.getName()));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                completed.remove(jumpNo);
                onejump.completed.put(targetPlayer.getUniqueId(), completed);
                onejump.recalculateOjp(targetPlayer.getUniqueId());
                if (targetPlayer.isOnline()) {
                    Player onlinePlayer = Bukkit.getPlayer(Objects.requireNonNull(targetPlayer.getPlayer()).getUniqueId());

                    ranks.tablogic(onlinePlayer);
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    onejump.recalculateOjp(offlinePlayer.getUniqueId());
                    if (offlinePlayer.isOnline()) {
                        Player onlinePlayer = offlinePlayer.getPlayer();
                        if (onlinePlayer == null) {
                            continue;
                        }
                        ranks.tablogic(onlinePlayer);
                        onlinePlayer.sendMessage(String.format("§aSuccessfully recalculated ojp! Your new total is: §b%s", onejump.ojp.get(onlinePlayer.getUniqueId())));
                    }
                }

            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        onejump.saveData();
        punishments.saveData();
        ranks.saveRanks();
    }
}
