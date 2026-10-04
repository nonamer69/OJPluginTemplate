package net.noname18_.gayjumptest;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Stats implements Listener {
    private final OneJump onejump;

    public Stats(OneJump onejump) {
        this.onejump = onejump;
    }

    public int getPlayerOjp(OfflinePlayer player) {
        return onejump.ojp.getOrDefault(player.getUniqueId(), 0);
    }
}
