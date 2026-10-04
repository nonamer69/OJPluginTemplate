package net.noname18_.gayjumptest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SuggestionCompleter implements TabCompleter {
    private final OneJump onejump;

    public SuggestionCompleter(OneJump onejump) {
        this.onejump = onejump;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        switch (command.getName().toLowerCase()) {
            case "oj" -> {
                if (args.length == 1) {
                    suggestions.addAll(List.of("create", "delete", "checkpoint", "finish", "warp", "complete", "uncomplete"));
                } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
                    suggestions.addAll(List.of("all"));
                    for (Integer jumpNumber : onejump.jumpNames.keySet()) {
                        suggestions.add(String.valueOf(jumpNumber));
                    }
                } else if (args.length == 2 && args[0].equalsIgnoreCase("finish")) {
                    for (Integer jumpNumber : onejump.jumpNames.keySet()) {
                        suggestions.add(String.valueOf(jumpNumber));
                    }
                } else if (args.length == 2 && args[0].equalsIgnoreCase("warp")) {
                    for (Integer jumpNumber : onejump.jumpNames.keySet()) {
                        suggestions.add(String.valueOf(jumpNumber));
                    }
                } else if (args.length == 2 && args[0].equalsIgnoreCase("complete")) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        suggestions.add(onlinePlayer.getName());
                    }
                } else if (args.length == 2 && args[0].equalsIgnoreCase("uncomplete")) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        suggestions.add(onlinePlayer.getName());
                    }
                } else if (args.length == 3 & args[0].equalsIgnoreCase("complete")) {
                    for (Integer jumpNumber : onejump.jumpNames.keySet()) {
                        suggestions.add(String.valueOf(jumpNumber));
                    }
                } else if (args.length == 3 & args[0].equalsIgnoreCase("uncomplete")) {
                    for (Integer jumpNumber : onejump.jumpNames.keySet()) {
                        suggestions.add(String.valueOf(jumpNumber));
                    }
                }
            }

            case "kick", "mute", "unmute", "ban", "unban" -> {
                if (args.length == 1) {
                    Bukkit.getOnlinePlayers().forEach(
                            player -> suggestions.add(player.getName())
                    );
                } else if (args.length == 2) {
                    suggestions.addAll(List.of("1s", "2m", "3h", "4d", "5w", "6mo"));
                }
            }

            case "rank" -> {
                if (args.length == 1) {
                    Bukkit.getOnlinePlayers().forEach(
                            player -> suggestions.add(player.getName())
                    );
                } else if (args.length == 2) {
                    suggestions.addAll(List.of("member", "jrmod", "mod", "admin", "owner"));
                }
            }

            case "stats" -> {
                if (args.length == 1) {
                    Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
                }
            }
        }

        String current = args[args.length - 1].toLowerCase();

        return suggestions.stream().filter(s -> s.toLowerCase().startsWith(current)).toList();
    };

}
