package me.missigdrift.missinghub.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

public enum ColourUtils {

    BLACK("BLACK", "&0", ChatColor.BLACK, Color.BLACK),
    DARK_BLUE("DARK_BLUE", "&1", ChatColor.DARK_BLUE, Color.BLUE),
    DARK_GREEN("DARK_GREEN", "&2", ChatColor.DARK_GREEN, Color.GREEN),
    DARK_AQUA("DARK_AQUA", "&3", ChatColor.DARK_AQUA, Color.NAVY),
    DARK_RED("DARK_RED", "&4", ChatColor.DARK_RED, Color.RED),
    DARK_PURPLE("DARK_PURPLE", "&5", ChatColor.DARK_PURPLE, Color.PURPLE),
    GOLD("GOLD", "&6", ChatColor.GOLD, Color.YELLOW),
    GRAY("GRAY", "&7", ChatColor.GRAY, Color.GRAY),
    DARK_GRAY("DARK_GRAY", "&8", ChatColor.DARK_GRAY, Color.GRAY),
    BLUE("BLUE", "&9", ChatColor.BLUE, Color.BLUE),
    GREEN("GREEN", "&a", ChatColor.GREEN, Color.GREEN),
    AQUA("AQUA", "&b", ChatColor.AQUA, Color.AQUA),
    RED("RED", "&c", ChatColor.RED, Color.MAROON),
    LIGHT_PURPLE("LIGHT_PURPLE", "&d", ChatColor.LIGHT_PURPLE, Color.FUCHSIA),
    YELLOW("YELLOW", "&e", ChatColor.YELLOW, Color.YELLOW),
    WHITE("WHITE", "&f", ChatColor.WHITE, Color.WHITE),;

    private final String name;
    private final String input;
    private final ChatColor minecraftColor;
    private final Color dyecolor;

    ColourUtils(String name, String input, ChatColor minecraftColor, Color dyecolor) {
        this.name = name;
        this.input = input;
        this.minecraftColor = minecraftColor;
        this.dyecolor = dyecolor;
    }

    public String getName() {
        return name;
    }

    public String getInput() {
        return input;
    }

    public ChatColor getMinecraftColor() {
        return minecraftColor;
    }

    public Color getDyecolor() {
        return dyecolor;
    }

    public static ChatColor format(String message) {
        for (ColourUtils c : values()) {
            if(message.equalsIgnoreCase(c.getName()) || message.equalsIgnoreCase(c.getInput())){
                return c.getMinecraftColor();
            }
        }
        return null;
    }

    public static Color getDyeColor(ChatColor chatColor) {
        for (ColourUtils c : values()) {
            if(chatColor == c.getMinecraftColor()){
                return c.getDyecolor();
            }
        }
        return Color.GRAY;
    }
}