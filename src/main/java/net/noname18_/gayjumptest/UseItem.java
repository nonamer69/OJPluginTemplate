package net.noname18_.gayjumptest;
import net.kyori.adventure.text.BlockNBTComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.common.value.qual.EnumVal;

public class UseItem implements Listener {
    private final OneJump onejump;

    public UseItem(OneJump onejump) {
        this.onejump = onejump;
    }

    public final Map<Player, Location> checkpoints = new HashMap<>();

    public Location setCheckpoint(Player player) {
        Location location = player.getLocation();
        return location;
    }

    public void teleportPlayer(Location coordinates, Player player) {
        player.teleport(coordinates);
    }

    public void addJumpToMenu(String name, String difficulty, boolean completed, int jumpNo, Inventory menu) {
        int intDifficulty;
        String colour = "";
        if (difficulty.endsWith("+") || difficulty.endsWith("-")) {
            intDifficulty = Integer.valueOf(difficulty.substring(0, difficulty.length() - 1));
        } else {
            intDifficulty = Integer.valueOf(difficulty);
        }
        ItemStack jumpItem = new ItemStack(Material.AIR);

        if (!completed) {
            if(intDifficulty == 0) {
                jumpItem = new ItemStack(Material.WHITE_STAINED_GLASS);
                colour = "§f";
            }
            if (intDifficulty == 1) {
                jumpItem = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS);
                colour = "§b";
            }
            if (intDifficulty == 2) {
                jumpItem = new ItemStack(Material.BLUE_STAINED_GLASS);
                colour = "§1";
            }
            if (intDifficulty == 3) {
                jumpItem = new ItemStack(Material.LIME_STAINED_GLASS);
                colour = "§a";
            }
            if (intDifficulty == 4) {
                jumpItem = new ItemStack(Material.YELLOW_STAINED_GLASS);
                colour = "§e";
            }
            if (intDifficulty == 5) {
                jumpItem = new ItemStack(Material.ORANGE_STAINED_GLASS);
                colour = "§6";
            }
            if (intDifficulty == 6) {
                jumpItem = new ItemStack(Material.RED_STAINED_GLASS);
                colour = "§4";
            }
            if (intDifficulty == 7) {
                jumpItem = new ItemStack(Material.PINK_STAINED_GLASS);
                colour = "§d";
            }
            if (intDifficulty == 8) {
                jumpItem = new ItemStack(Material.PURPLE_STAINED_GLASS);
                colour = "§5";
            }
            if (intDifficulty == 9) {
                jumpItem = new ItemStack(Material.GRAY_STAINED_GLASS);
                colour = "§8";
            }
            if (intDifficulty == 10) {
                jumpItem = new ItemStack(Material.BLACK_STAINED_GLASS);
                colour = "§0";
            }
            if (intDifficulty == 11) {
                jumpItem = new ItemStack(Material.MAGENTA_STAINED_GLASS);
                colour = "§5";
            }
            if (intDifficulty == 12) {
                jumpItem = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS);
                colour = "§7";
            }
        } else {
            if(intDifficulty == 0) {
                jumpItem = new ItemStack(Material.WHITE_WOOL);
                colour = "§f";
            }
            if (intDifficulty == 1) {
                jumpItem = new ItemStack(Material.LIGHT_BLUE_WOOL);
                colour = "§b";
            }
            if (intDifficulty == 2) {
                jumpItem = new ItemStack(Material.BLUE_WOOL);
                colour = "§1";
            }
            if (intDifficulty == 3) {
                jumpItem = new ItemStack(Material.LIME_WOOL);
                colour = "§a";
            }
            if (intDifficulty == 4) {
                jumpItem = new ItemStack(Material.YELLOW_WOOL);
                colour = "§e";
            }
            if (intDifficulty == 5) {
                jumpItem = new ItemStack(Material.ORANGE_WOOL);
                colour = "§6";
            }
            if (intDifficulty == 6) {
                jumpItem = new ItemStack(Material.RED_WOOL);
                colour = "§4";
            }
            if (intDifficulty == 7) {
                jumpItem = new ItemStack(Material.PINK_WOOL);
                colour = "§d";
            }
            if (intDifficulty == 8) {
                jumpItem = new ItemStack(Material.PURPLE_WOOL);
                colour = "§5";
            }
            if (intDifficulty == 9) {
                jumpItem = new ItemStack(Material.GRAY_WOOL);
                colour = "§8";
            }
            if (intDifficulty == 10) {
                jumpItem = new ItemStack(Material.BLACK_WOOL);
                colour = "§0";
            }
            if (intDifficulty == 11) {
                jumpItem = new ItemStack(Material.MAGENTA_WOOL);
                colour = "§5";
            }
            if (intDifficulty == 12) {
                jumpItem = new ItemStack(Material.LIGHT_GRAY_WOOL);
                colour = "§7";
            }
        }

        ItemMeta meta = jumpItem.getItemMeta();

        meta.setDisplayName(String.format("§aJump %s", String.valueOf(jumpNo)));
        meta.setLore(List.of(
                "§a-------[]-------",
                "§aName: §e" + name,
                "§aDifficulty: " + colour + difficulty,
                "§a-------[]-------"
        ));
        jumpItem.setItemMeta(meta);
        menu.addItem(jumpItem);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (item.getType() == Material.BLAZE_ROD) {
            Location coordinates = onejump.playerCheckpoints.get(event.getPlayer());

            if (coordinates == null) {
                return;
            }

            teleportPlayer(coordinates, event.getPlayer());
        } else if (item.getType() == Material.ECHO_SHARD) {
            onejump.playerCheckpoints.put(event.getPlayer(), event.getPlayer().getLocation());
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 200, 1);
            CommandSender sender = event.getPlayer();
            sender.sendMessage("§7§l[§b§lO§3§lJ§7§l] §bSet §3checkpoint§b§7!");
        } else if (item.getType() == Material.NETHER_STAR) {
            Inventory menu = Bukkit.createInventory(event.getPlayer(), 54, "§dONE JUMP");

            for (Map.Entry<Integer,String> names : onejump.jumpNames.entrySet()) {

                int jumpNo = names.getKey();
                String name = onejump.jumpNames.get(jumpNo);
                String difficulty = onejump.jumpDifficulties.get(jumpNo);
                Location jumpLocation = onejump.jumpLocations.get(jumpNo);

                Set<Integer> playerCompleted = onejump.completed.get(event.getPlayer().getUniqueId());

                boolean completed = playerCompleted != null && playerCompleted.contains(jumpNo);

                addJumpToMenu(name, difficulty, completed, jumpNo, menu);
            }
            event.getPlayer().openInventory(menu);
        }

    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§dONE JUMP")) {
            event.setCancelled(true);

            int jumpNo = event.getSlot() + 1;

            Location location = onejump.jumpLocations.get(jumpNo);

            if (location != null) {
                Player player = (Player) event.getWhoClicked();
                teleportPlayer(location, player);
                player.sendMessage(String.format("Entering §bJump %s§f...", String.valueOf(jumpNo)));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 100, 1);
                return;
            }
        }
    }

}
