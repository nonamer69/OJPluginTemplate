package net.noname18_.gayjumptest;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ChatPrefixes implements Listener {
    private final Ranks ranks;
    private final Punishments punishments;
    private final OneJump onejump;
    private final Gayjumptest plugin;
    private final UseItem useitem;

    public ChatPrefixes(Ranks ranks, Punishments punishments, OneJump onejump, Gayjumptest plugin, UseItem useitem) {
        this.ranks = ranks;
        this.punishments = punishments;
        this.onejump = onejump;
        this.plugin = plugin;
        this.useitem = useitem;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getName());
        OfflinePlayer playerr = Bukkit.getOfflinePlayer(event.getPlayer().getName());

        Ranks.Rank rank = ranks.getRank(player);

        String prefix = rank.getPrefix();

        if (punishments.ismuted(playerr) > System.currentTimeMillis()) {
            long timeLeft = punishments.ismuted(playerr) - System.currentTimeMillis();
            if (player.isOnline()) {
                player.sendMessage(String.format("§c§8§l[§c§lPUNISHMENTS§8§l] §cUh oh! Looks like you are §4muted§c! This means that you cannot talk §4at all §cwhile in this state.\n \n §cTime left until unmute: §4%s", punishments.formatDurations(timeLeft)));
                event.setCancelled(true);
                return;
            }
        } else {
            event.setCancelled(false);
        }

        event.renderer(((source, sourceDisplayName, message, viewer) ->
                Component.text(prefix + playerr.getName() + ": ")
                        .append(message)
                )
        );

        if (plugin.nukeConf.contains(player.getUniqueId())) {
            event.setCancelled(true);
            String message = PlainTextComponentSerializer.plainText().serialize(event.message());
            if (message.equals(
                    "Yes, I agree to NUKE the entire server and make it a barren wasteland nobody will visit anymore."
            )) {
                plugin.nukeConf.add(player.getUniqueId());
                onejump.jumpNames.clear();
                onejump.jumpDifficulties.clear();
                onejump.jumpLocations.clear();
                onejump.completed.clear();
                onejump.jump = 1;
                player.sendMessage("§aSuccessfully deleted all jumps!");
            } else {
                plugin.nukeConf.remove(player.getUniqueId());
                if (player.isOnline()) {

                    player.sendMessage("§aNuke cancelled. Try again!");
                }
            }
        }

        if (player != null && onejump.waitingForCoordinates.contains(player)) {
            event.setCancelled(true);

            String message = PlainTextComponentSerializer.plainText()
                    .serialize(event.message());
            player.sendMessage("§aReceived: " + message);

            String[] args = message.split(" ");

            if (args.length != 5) {
                player.sendMessage("§cInvalid arguments! Usage: <X> <Y> <Z> <Yaw> <Pitch>");
                return;
            }

            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);

            float yaw = Float.parseFloat(args[3]);
            float pitch = Float.parseFloat(args[4]);

            Location checkpoint = onejump.editingCheckpoint.get(player);
            Location location = new Location(player.getWorld(), x, y, z, yaw, pitch);

            if (checkpoint == null) {
                onejump.waitingForStrategy.remove(player);
                player.sendMessage("§cCheckpoint not found.");
                return;
            }

            onejump.checkpoints.put(checkpoint, location);

            player.sendMessage("Successfully set checkpoint coordinates!");

            onejump.waitingForCoordinates.remove(player);
            onejump.editingCheckpoint.remove(player);
        }
        if (player != null && onejump.waitingForStrategy.contains(player)) {
            event.setCancelled(true);

            String strategy = PlainTextComponentSerializer.plainText()
                    .serialize(event.message());

            Location checkpoint = onejump.editingCheckpoint.get(player);

            onejump.jumpStrats.put(checkpoint, strategy);

            onejump.waitingForStrategy.remove(player);
            onejump.editingCheckpoint.remove(player);

            player.sendMessage("§aSuccessfully set the jump strategy!");
        }


    }
}
