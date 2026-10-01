package net.noname18_.gayjumptest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Ranks implements Listener {

    private final JavaPlugin plugin;
    private final File rankFile;
    private final FileConfiguration rankConfig;
    private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();

    private final OneJump onejump;

    public Ranks(JavaPlugin plugin, OneJump onejump) {
        this.plugin = plugin;
        this.onejump = onejump;

        rankFile = new File(plugin.getDataFolder(), "ranks.yml");

        if (!rankFile.exists()) {
            try {
                rankFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        rankConfig = YamlConfiguration.loadConfiguration(rankFile);
        loadRanks();
    }

    public enum Rank {
        MEMBER(5, "§8[§dMember§8] §d"),
        JRMOD(4, "§8[§bJr Mod§8] §b "),
        MOD(3, "§8[§9Mod§8] §9"),
        ADMIN(2, "§8[§cAdmin§8] §c"),
        OWNER(1, "§8[§4Owner§8] §4");

        private final String prefix;
        private final int order;

        Rank(int order, String prefix) {
            this.order = order;
            this.prefix = prefix;
        }

        public int getOrder() {
            return order;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    Map<UUID, Rank> ranks = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!ranks.containsKey(event.getPlayer().getUniqueId())) {
            ranks.put(event.getPlayer().getUniqueId(), Rank.MEMBER);

            rankConfig.set("ranks." + event.getPlayer().getUniqueId(), Rank.MEMBER.name());
            saveRanks();
        }

        applyPerms(event.getPlayer(), getRank(event.getPlayer()));

        tablogic(event.getPlayer());
    }

    public void setRank(OfflinePlayer player, Rank rank) {

        ranks.put(player.getUniqueId(), rank);

        rankConfig.set("ranks." + player.getUniqueId(), rank.name());
        saveRanks();

        if (player.isOnline()) {
            Player onlinePlayer = player.getPlayer();

            if (onlinePlayer != null) {
                applyPerms(onlinePlayer, rank);
            }
        }

    }

    private void applyPerms(Player player, Rank rank) {
        PermissionAttachment attachment = attachments.get(player.getUniqueId());
        if (attachment == null) {
            attachment = player.addAttachment(plugin);
            attachments.put(player.getUniqueId(), attachment);
        }

        if (rank.getOrder() <= 4) {
            attachment.setPermission("mute.use", true);
            attachment.setPermission("unmute.use", true);
            attachment.setPermission("kick.use", true);
        } else {
            attachment.setPermission("mute.use", false);
            attachment.setPermission("unmute.use", false);
            attachment.setPermission("kick.use", false);
        }

        if (rank.getOrder() <= 3) {
            attachment.setPermission("ban.use", true);
            attachment.setPermission("unban.use", true);
        } else {
            attachment.setPermission("ban.use", false);
            attachment.setPermission("unban.use", false);
        }

        if (rank.getOrder() <= 2) {
            attachment.setPermission("rank.set", true);
        } else {
            attachment.setPermission("rank.set", false);
        }

        if (player.isOp()) {
            attachment.setPermission("rank.set", true);
            attachment.setPermission("unban.use", true);
            attachment.setPermission("ban.use", true);
            attachment.setPermission("kick.use", true);
            attachment.setPermission("mute.use", true);
            attachment.setPermission("unmute.use", true);
        } else {
            attachment.setPermission("rank.set", false);
            attachment.setPermission("unban.use", false);
            attachment.setPermission("ban.use", false);
            attachment.setPermission("kick.use", false);
            attachment.setPermission("mute.use", false);
            attachment.setPermission("unmute.use", false);
        }
    }

    public void saveRanks() {
        try {
            rankConfig.save(rankFile);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadRanks() {
        ConfigurationSection section = rankConfig.getConfigurationSection("ranks");

        if (section == null) {
            return;
        }

        for (String uuidstring : section.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidstring);
                String rankname = section.getString(uuidstring);

                Rank rank = Rank.valueOf(rankname);

                ranks.put(uuid,rank);
            } catch (IllegalArgumentException e){
                plugin.getLogger().warning("Invalid rank data for UUID: " + uuidstring);
            }
        }
    }

    public Rank getRank(OfflinePlayer player) {
        return ranks.get(player.getUniqueId());
    }

    public void tablogic(Player player) {
        Rank rank = getRank(player);

        if (rank == null) {
            return;
        }

        Integer points = onejump.ojp.get(player.getUniqueId());

        if (points == null) {
            points = 0;
        }

        player.setPlayerListOrder(rank.getOrder());
        player.setPlayerListName("§8[§c" + points + "§8] " + rank.getPrefix() + player.getName());

        Component header = Component.text(
                "§d§lONE §5§lJUMP\n" +
                        "§cYour rank: " + rank.getPrefix() +
                        "\n"
        );

        player.sendPlayerListHeaderAndFooter(
                header,
                Component.text("\n§dgayjump.aternos.me")
        );
    }
}
