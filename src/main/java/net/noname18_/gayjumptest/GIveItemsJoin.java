package net.noname18_.gayjumptest;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandSender;

public class GIveItemsJoin implements Listener{
    public void setName(String name, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        ItemStack checkpoint = new ItemStack(Material.BLAZE_ROD, 1);
        ItemStack setcheckpoint = new ItemStack(Material.ECHO_SHARD, 1);
        ItemStack menu = new ItemStack(Material.NETHER_STAR, 1);

        ItemMeta meta = checkpoint.getItemMeta();
        setName("§aCheckpoint (Right click)",checkpoint);

        ItemMeta setcheckpointmeta = setcheckpoint.getItemMeta();
        setName("§bSet Checkpoint (Right click)",setcheckpoint);

        ItemMeta menumeta = menu.getItemMeta();
        setName("§dMenu", menu);


        player.getInventory().setItem(0, checkpoint);
        player.getInventory().setItem(2, setcheckpoint);
        player.getInventory().setItem(4, menu);
    }
}
