package net.noname18_.gayjumptest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class OneJump implements Listener {

    Map<Integer, String> jumpNames = new HashMap<>();
    Map<Integer, String> jumpDifficulties = new HashMap<>();
    Map<Integer, Location> jumpLocations = new HashMap<>();

    Map<UUID, Set<Integer>> completed = new HashMap<>();

    Set<Player> waitingForCoordinates = new HashSet<>();
    Set<Player> waitingForStrategy = new HashSet<>();

    Map<Player, Location> editingCheckpoint = new HashMap<>();

    Map<Location, Location> checkpoints = new HashMap<>();

    Map<Location, String> jumpStrats = new HashMap<>();

    Map<Player, Location> playerCheckpoints = new HashMap<>();

    Map<Player, Location> playersOnCheckpoint = new HashMap<>();

    Map<Location, Integer> completionPlates = new HashMap<>();

    Map<UUID, Integer> ojp = new HashMap<>();

    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration data;

    private Ranks ranks;

    public int jump = 1;

    public OneJump(JavaPlugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(plugin.getDataFolder(), "onejump.yml");
        data = YamlConfiguration.loadConfiguration(file);

        loadData();
    }

    public void setRanks(Ranks ranks) {
        this.ranks = ranks;
    }

    public void saveData() {
        data.set("jump", jump);

        data.set("jumps", null);

        for (Map.Entry<Integer, String> entry : jumpNames.entrySet()) {
            int jumpNo = entry.getKey();

            data.set("jumps." + jumpNo + ".name", entry.getValue());
            data.set("jumps." + jumpNo + ".difficulty", jumpDifficulties.get(jumpNo));
            data.set("jumps." + jumpNo + ".location", jumpLocations.get(jumpNo));
        }

        data.set("ojp", null);

        for (Map.Entry<UUID, Integer> entry : ojp.entrySet()) {
            data.set(
                    "ojp." + entry.getKey(),
                    entry.getValue()
            );
        }

        data.set("completed", null);

        for (Map.Entry<UUID, Set<Integer>> entry : completed.entrySet()) {
            data.set(
                    "completed." + entry.getKey(),
                    new ArrayList<>(entry.getValue())
            );
        }

        data.set("checkpoints", null);

        int checkpointId = 0;

        for (Map.Entry<Location, Location> entry : checkpoints.entrySet()) {
            data.set(
                    "checkpoints." + checkpointId + ".plate",
                    entry.getKey()
            );

            data.set(
                    "checkpoints." + checkpointId + ".location",
                    entry.getValue()
            );

            checkpointId++;
        }

        data.set("jumpStrats", null);

        int strategyId = 0;

        for (Map.Entry<Location, String> entry : jumpStrats.entrySet()) {
            data.set(
                    "jumpStrats." + strategyId + ".location",
                    entry.getKey()
            );

            data.set(
                    "jumpStrats." + strategyId + ".strategy",
                    entry.getValue()
            );

            strategyId++;
        }

        data.set("completionPlates", null);

        int plateId = 0;

        for (Map.Entry<Location, Integer> entry : completionPlates.entrySet()) {
            data.set(
                    "completionPlates." + plateId + ".location",
                    entry.getKey()
            );

            data.set(
                    "completionPlates." + plateId + ".jump",
                    entry.getValue()
            );

            plateId++;
        }

        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save onejump.yml!");
            e.printStackTrace();
        }
    }

    public void loadData() {
        jump = data.getInt("jump", 1);

        if (data.getConfigurationSection("jumps") != null) {
            for (String key : data.getConfigurationSection("jumps").getKeys(false)) {
                int jumpNo = Integer.parseInt(key);

                String name = data.getString(
                        "jumps." + key + ".name"
                );

                String difficulty = data.getString(
                        "jumps." + key + ".difficulty"
                );

                Location location = data.getLocation(
                        "jumps." + key + ".location"
                );

                if (name != null) {
                    jumpNames.put(jumpNo, name);
                }

                if (difficulty != null) {
                    jumpDifficulties.put(jumpNo, difficulty);
                }

                if (location != null) {
                    jumpLocations.put(jumpNo, location);
                }
            }
        }

        if (data.getConfigurationSection("ojp") != null) {
            for (String uuidString : data.getConfigurationSection("ojp").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);

                    ojp.put(
                            uuid,
                            data.getInt("ojp." + uuidString)
                    );
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        if (data.getConfigurationSection("completed") != null) {
            for (String uuidString : data.getConfigurationSection("completed").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);

                    List<Integer> jumps = data.getIntegerList(
                            "completed." + uuidString
                    );

                    completed.put(
                            uuid,
                            new HashSet<>(jumps)
                    );
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        if (data.getConfigurationSection("checkpoints") != null) {
            for (String key : data.getConfigurationSection("checkpoints").getKeys(false)) {
                Location plate = data.getLocation(
                        "checkpoints." + key + ".plate"
                );

                Location location = data.getLocation(
                        "checkpoints." + key + ".location"
                );

                if (plate != null && location != null) {
                    checkpoints.put(plate, location);
                }
            }
        }

        if (data.getConfigurationSection("jumpStrats") != null) {
            for (String key : data.getConfigurationSection("jumpStrats").getKeys(false)) {
                Location location = data.getLocation(
                        "jumpStrats." + key + ".location"
                );

                String strategy = data.getString(
                        "jumpStrats." + key + ".strategy"
                );

                if (location != null && strategy != null) {
                    jumpStrats.put(location, strategy);
                }
            }
        }

        if (data.getConfigurationSection("completionPlates") != null) {
            for (String key : data.getConfigurationSection("completionPlates").getKeys(false)) {
                Location location = data.getLocation(
                        "completionPlates." + key + ".location"
                );

                int jumpNo = data.getInt(
                        "completionPlates." + key + ".jump"
                );

                if (location != null) {
                    completionPlates.put(location, jumpNo);
                }
            }
        }
    }

    public void awardPoints(Player player, String difficulty, int jumpNo) {
        int actualDifficulty;
        int points;

        UUID uuid = player.getUniqueId();

        if (ojp.get(uuid) == null) {
            points = 0;
            ojp.put(uuid, points);
        } else {
            points = ojp.get(uuid);
        }

        if (difficulty.endsWith("+") || difficulty.endsWith("-")) {
            actualDifficulty = Integer.valueOf(
                    difficulty.substring(0, difficulty.length() - 1)
            );
        } else {
            actualDifficulty = Integer.valueOf(difficulty);
        }

        if (actualDifficulty == 0) {
            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    100,
                    1
            );

            return;
        }

        if (actualDifficulty == 1) {
            points += 1;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    100,
                    1
            );

        } else if (actualDifficulty == 2) {
            points += 2;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    100,
                    1
            );

        } else if (actualDifficulty == 3) {
            points += 4;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    100,
                    1
            );

        } else if (actualDifficulty == 4) {
            points += 5;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    100,
                    1
            );

        } else if (actualDifficulty == 5) {
            points += 6;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    100,
                    1
            );

        } else if (actualDifficulty == 6) {
            points += 8;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    100,
                    1
            );

        } else if (actualDifficulty == 7) {
            points += 13;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    100,
                    1
            );

        } else if (actualDifficulty == 8) {
            points += 19;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(
                        onlinePlayer.getLocation(),
                        Sound.ITEM_TOTEM_USE,
                        100,
                        0
                );
            }

            Bukkit.broadcastMessage(
                    String.format(
                            "§3----------[]----------\n\n" +
                                    "§9%s §bhas completed Jump %s!\n\n" +
                                    "§bDifficulty: §5%s\n\n" +
                                    "§3----------[]----------",
                            player.getName(),
                            String.valueOf(jumpNo),
                            difficulty
                    )
            );

        } else if (actualDifficulty == 9) {
            points += 23;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(
                        onlinePlayer.getLocation(),
                        Sound.ENTITY_WITHER_SPAWN,
                        100,
                        1
                );
            }

            Bukkit.broadcastMessage(
                    String.format(
                            "§3----------[]----------\n\n" +
                                    "§9%s §bhas completed Jump %s!\n\n" +
                                    "§bDifficulty: §8%s\n\n" +
                                    "§3----------[]----------",
                            player.getName(),
                            String.valueOf(jumpNo),
                            difficulty
                    )
            );

        } else if (actualDifficulty == 10) {
            points += 26;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(
                        onlinePlayer.getLocation(),
                        Sound.ENTITY_WITHER_DEATH,
                        100,
                        1
                );
            }

            Bukkit.broadcastMessage(
                    String.format(
                            "§3----------[]----------\n\n" +
                                    "§9%s §bhas completed Jump %s!\n\n" +
                                    "§bDifficulty: §0%s\n\n" +
                                    "§3----------[]----------",
                            player.getName(),
                            String.valueOf(jumpNo),
                            difficulty
                    )
            );

        } else if (actualDifficulty == 11) {
            points += 54;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(
                        onlinePlayer.getLocation(),
                        Sound.ENTITY_ENDER_DRAGON_DEATH,
                        100,
                        1
                );
            }

            Bukkit.broadcastMessage(
                    String.format(
                            "§3----------[]----------\n\n" +
                                    "§9%s §bhas completed Jump %s!\n\n" +
                                    "§bDifficulty: §5%s\n\n" +
                                    "§3----------[]----------",
                            player.getName(),
                            String.valueOf(jumpNo),
                            difficulty
                    )
            );

        } else if (actualDifficulty == 12) {
            points += 121;
            ojp.put(uuid, points);

            player.sendMessage(
                    String.format(
                            "§aJump %s §fcompleted!",
                            String.valueOf(jumpNo)
                    )
            );

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(
                        onlinePlayer.getLocation(),
                        Sound.ENTITY_BLAZE_DEATH,
                        100,
                        0
                );
            }

            Bukkit.broadcastMessage(
                    String.format(
                            "§3----------[]----------\n\n" +
                                    "§9%s §bhas completed Jump %s!\n\n" +
                                    "§bDifficulty: §7%s\n\n" +
                                    "§3----------[]----------",
                            player.getName(),
                            String.valueOf(jumpNo),
                            difficulty
                    )
            );
        }

        saveData();
    }

    public void recalculateOjp(UUID uuid) {
        Set<Integer> completedJumps = completed.get(uuid);

        if (completedJumps == null) {
            ojp.put(uuid, 0);
            return;
        }

        int points = 0;

        for (int completedJump : completedJumps) {
            String difficulty = jumpDifficulties.get(completedJump);

            if (difficulty == null) {
                continue;
            }

            int actualDifficulty;

            if (difficulty.endsWith("+") || difficulty.endsWith("-")) {
                actualDifficulty = Integer.parseInt(
                        difficulty.substring(0, difficulty.length() - 1)
                );
            } else {
                actualDifficulty = Integer.parseInt(difficulty);
            }

            if (actualDifficulty == 1) {
                points += 1;
            } else if (actualDifficulty == 2) {
                points += 2;
            } else if (actualDifficulty == 3) {
                points += 4;
            } else if (actualDifficulty == 4) {
                points += 5;
            } else if (actualDifficulty == 5) {
                points += 6;
            } else if (actualDifficulty == 6) {
                points += 8;
            } else if (actualDifficulty == 7) {
                points += 13;
            } else if (actualDifficulty == 8) {
                points += 19;
            } else if (actualDifficulty == 9) {
                points += 23;
            } else if (actualDifficulty == 10) {
                points += 26;
            } else if (actualDifficulty == 11) {
                points += 54;
            } else if (actualDifficulty == 12) {
                points += 121;
            }
        }

        ojp.put(uuid, points);
    }

    public void create(Player player, String diff, String nam) {
        String difficulty = diff;
        String name = nam;
        Location location = player.getLocation();

        jumpNames.put(jump, name);
        jumpDifficulties.put(jump, difficulty);
        jumpLocations.put(jump, location);

        jump += 1;

        saveData();
    }

    public void delete(int jumpNo) {
        for (Map.Entry<Location, Integer> entry : completionPlates.entrySet()) {
            if (entry.getValue() == jumpNo) {
                entry.getKey().getBlock().setType(Material.AIR);
            }
        }

        Map<Location, Integer> shiftedCompletionPlates = new HashMap<>();

        for (Map.Entry<Location, Integer> entry : completionPlates.entrySet()) {
            int plateJump = entry.getValue();

            if (plateJump == jumpNo) {
                continue;
            }

            if (plateJump > jumpNo) {
                plateJump--;
            }

            shiftedCompletionPlates.put(entry.getKey(), plateJump);
        }

        completionPlates = shiftedCompletionPlates;

        jumpNames.remove(jumpNo);
        jumpDifficulties.remove(jumpNo);
        jumpLocations.remove(jumpNo);

        for (int i = jumpNo + 1; i < jump; i++) {
            jumpNames.put(i - 1, jumpNames.get(i));
            jumpDifficulties.put(i - 1, jumpDifficulties.get(i));
            jumpLocations.put(i - 1, jumpLocations.get(i));
        }

        jumpNames.remove(jump - 1);
        jumpDifficulties.remove(jump - 1);
        jumpLocations.remove(jump - 1);

        for (Set<Integer> completedJumps : completed.values()) {
            Set<Integer> shifted = new HashSet<>();

            for (int completedJump : completedJumps) {
                if (completedJump == jumpNo) {
                    continue;
                }

                if (completedJump > jumpNo) {
                    shifted.add(completedJump - 1);
                } else {
                    shifted.add(completedJump);
                }
            }

            completedJumps.clear();
            completedJumps.addAll(shifted);
        }

        for (Map.Entry<UUID, Set<Integer>> entry : completed.entrySet()) {
            UUID uuid = entry.getKey();
            int points = 0;

            for (int completedJump : entry.getValue()) {
                recalculateOjp(uuid);

                Player player = Bukkit.getPlayer(uuid);

                if (player != null && ranks != null) {
                    ranks.tablogic(player);
                }
        }

        jump--;

        saveData();
        }
    }

    public void finishCreate(Location location, int jumpNo, Player player) {
        Location plate = location.getBlock().getLocation();

        player.sendMessage("§eWarning! Countdown has initiated. Please step off the block where the plate will be, otherwise you'll get an autocompletion which is punishable.");

        new BukkitRunnable() {
            int seconds = 5;

            @Override
            public void run() {
                if (seconds <= 0) {
                    cancel();
                    plate.getBlock().setType(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
                    completionPlates.put(plate, jumpNo);
                    player.sendMessage(String.format("§aSuccessfully added a finish plate to Jump %s!", jumpNo));

                    saveData();
                    return;
                }

                player.sendMessage("§a" + seconds);
                seconds--;
            }
        }.runTaskTimer(plugin, 20L, 20L);

        saveData();
    }

    public void checkpointCreate(Player player) {
        Location plate = player.getLocation().getBlock().getLocation();

        plate.getBlock().setType(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);

        saveData();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!event.getPlayer().isSneaking()) {
            return;
        }

        if (!event.getPlayer().hasPermission("rank.set")) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if (block.getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            Inventory menu = Bukkit.createInventory(
                    event.getPlayer(),
                    9,
                    "§bCHECK§3POINT"
            );

            ItemStack coordinates = new ItemStack(Material.COMPASS);
            ItemMeta coordMeta = coordinates.getItemMeta();
            coordMeta.setDisplayName("§aClick to set coordinates!");
            coordinates.setItemMeta(coordMeta);
            menu.setItem(0, coordinates);

            ItemStack strat = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta stratMeta = strat.getItemMeta();
            stratMeta.setDisplayName("§eClick to set jump strategy!");
            strat.setItemMeta(stratMeta);
            menu.setItem(4, strat);

            ItemStack delete = new ItemStack(Material.RED_WOOL);
            ItemMeta deleteMeta = delete.getItemMeta();
            deleteMeta.setDisplayName("§cClick to delete this checkpoint!");
            delete.setItemMeta(deleteMeta);
            menu.setItem(8, delete);

            event.getPlayer().openInventory(menu);

            Location checkpoint = block.getLocation();
            editingCheckpoint.put(event.getPlayer(), checkpoint);
        }

        if (block.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE
                && completionPlates.containsKey(block.getLocation())) {

            block.setType(Material.AIR);
            completionPlates.remove(block.getLocation());
            saveData();

            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals("§bCHECK§3POINT")) {
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (item.getType() == Material.COMPASS) {

            waitingForCoordinates.add(player);

            player.closeInventory();

            player.sendMessage(
                    "§aSend the coordinates in chat: X Y Z Yaw Pitch"
            );

        } else if (item.getType() == Material.WRITABLE_BOOK) {

            waitingForStrategy.add(player);

            player.closeInventory();

            player.sendMessage(
                    "§aSend the jump strategy in chat: "
            );

        } else if (item.getType() == Material.RED_WOOL) {

            Location checkpoint = editingCheckpoint.get(player);

            if (checkpoint == null) {
                return;
            }

            checkpoints.remove(checkpoint);
            jumpStrats.remove(checkpoint);
            editingCheckpoint.remove(player);

            checkpoint.getBlock().setType(Material.AIR);

            player.closeInventory();

            player.sendMessage("§cCheckpoint deleted!");

            saveData();
        }
    }

    @EventHandler
    public void onPlateStep(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if (block.getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            Location plate = block.getLocation();

            if (plate.equals(playersOnCheckpoint.get(player))) {
                return;
            }

            playersOnCheckpoint.put(player, plate);

            Location destination = checkpoints.get(plate);

            if (destination == null) {
                return;
            }

            playerCheckpoints.put(player, destination);

            String strategy = jumpStrats.get(plate);

            if (strategy != null) {

                player.sendMessage(
                        "§3----------[]----------\n\n" +
                                "§bStrategy: §f" + strategy + "§b.\n\n" +
                                "§3----------[]----------"
                );

                player.playSound(
                        player.getLocation(),
                        Sound.ENTITY_PLAYER_LEVELUP,
                        100,
                        1
                );
            }
        }

        if (block.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
            Location plate = block.getLocation();

            if (!completionPlates.containsKey(plate)) {
                return;
            }

            int jumpNo = completionPlates.get(plate);

            UUID uuid = player.getUniqueId();

            Set<Integer> playerCompleted = completed.get(uuid);

            if (playerCompleted == null || !playerCompleted.contains(jumpNo)) {

                awardPoints(
                        player,
                        jumpDifficulties.get(jumpNo),
                        jumpNo
                );

                completed
                        .computeIfAbsent(uuid, p -> new HashSet<>())
                        .add(jumpNo);

                ranks.tablogic(player);

                saveData();

            } else {

                player.sendMessage(
                        String.format(
                                "§aJump %s §fcompleted!",
                                String.valueOf(jumpNo)
                        )
                );

                player.playSound(
                        player.getLocation(),
                        Sound.ENTITY_PLAYER_LEVELUP,
                        100,
                        1
                );
            }

            Bukkit.getScheduler().runTask(
                    plugin,
                    () -> {
                        Location location = jumpLocations.get(jumpNo);

                        if (location == null) {
                            return;
                        }

                        player.teleport(location);

                        player.playSound(
                                player.getLocation(),
                                Sound.ENTITY_PLAYER_TELEPORT,
                                100,
                                1
                        );
                    }
            );
        }
    }
}