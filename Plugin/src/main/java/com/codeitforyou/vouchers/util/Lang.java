package com.codeitforyou.vouchers.util;

import com.codeitforyou.vouchers.Vouchers;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public enum Lang {
    PREFIX("&b[&lV&b]"),

    MAIN_COMMAND("{0} &7You're running &f{1} &7version &bv{2} &7by &f{3}&7."),

    HELP_COMMAND_HEADER("", "{0} &7Listing commands:", "&7"),
    HELP_COMMAND_FORMAT(" &b/vouchers {1} &8- &7{2}"),
    HELP_COMMAND_FOOTER("", "{0} &7A total of &f{1} &7added."),

    LIST_COMMAND_HEADER("", "{0} &7Listing vouchers:", "&7"),
    LIST_COMMAND_FORMAT(" &8- &b{1}"),
    LIST_COMMAND_FOOTER("", "{0} &7A total of &f{1} &7added."),

    RELOAD_COMMAND("{0} &7Successfully reloaded plugin with &b{1} &7loaded."),
    CREATE_COMMAND("{0} &7Successfully created the &b{1} &7voucher."),

    GIVE_COMMAND_SELF("{0} &7You've given yourself {2}x &b{1} &7voucher."),
    GIVE_COMMAND_OTHER("{0} &7You've given &f{1} &7{3}x &b{2} &7voucher."),

    GIVE_ALL_COMMAND("{0} &7You've given &f{1} &7online player(s) &7{3}x &b{2} &7voucher."),

    ERROR_NO_PERMISSION_COMMAND("{0} &7You don't have permission to do that."),
    ERROR_NO_PERMISSION_VOUCHER("{0} &7You need &bvouchers.use.{1} &7to redeem that voucher."),
    ERROR_PLAYER_ONLY("{0} &7That's a player only command."),
    ERROR_INVALID_COMMAND("{0} &7That's an invalid command, use &f/voucher help&7."),
    ERROR_INVALID_PLAYER("{0} &7That player couldn't be found."),
    ERROR_INVALID_VOUCHER("{0} &7That voucher couldn't be found."),
    ERROR_ALREADY_OPENING("{0} &7You're already opening a voucher."),
    ERROR_ALREADY_EXISTS("{0} &7That voucher already exists."),
    ERROR_NO_VOUCHERS_EXIST("{0} &7No vouchers exist, try &b/voucher create <id>&7."),

    CANNOT_USE_IN_REGION("{0} &7This voucher cannot be used in that region."),
    CANNOT_USE_IN_WORLD("{0} &7This voucher cannot be used in that world."),

    COMMAND_USAGE("{0} &7Usage: &b/voucher {1}");

    private static FileConfiguration c;
    private String message;

    Lang(final String... def) {
        this.message = String.join("\n", def);
    }

    public static String format(String s, final Object... objects) {
        for (int i = 0; i < objects.length; ++i) {
            s = s.replace("{" + i + "}", String.valueOf(objects[i]));
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean init(Vouchers vouchers) {
        Lang.c = vouchers.getConfig();
        for (final Lang value : values()) {
            if (value.getMessage().split("\n").length == 1) {
                Lang.c.addDefault(value.getPath().toLowerCase(), value.getMessage());
            } else {
                Lang.c.addDefault(value.getPath().toLowerCase(), value.getMessage().split("\n"));
            }
        }
        Lang.c.options().copyDefaults(true);
        vouchers.saveConfig();
        return true;
    }

    private String getMessage() {
        return this.message;
    }

    public String getPath() {
        return "message." + this.name().toLowerCase().toLowerCase();
    }

    public void send(final Player player, final Object... args) {
        final String message = this.asString(args);
        Arrays.stream(message.split("\n")).forEach(player::sendMessage);
    }

    public void send(final CommandSender sender, final Object... args) {
        if (sender instanceof Player) {
            this.send((Player) sender, args);
        } else {
            Arrays.stream(this.asString(args).split("\n")).forEach(sender::sendMessage);
        }
    }

    public String asString(final Object... objects) {
        Optional<String> opt = Optional.empty();
        if (Lang.c.contains(this.getPath())) {
            if (Lang.c.isList(getPath())) {
                opt = Optional.of(String.join("\n", Lang.c.getStringList(this.getPath())));
            } else if (Lang.c.isString(this.getPath())) {
                opt = Optional.ofNullable(Lang.c.getString(this.getPath()));
            }
        }
        return this.format(opt.orElse(this.message), objects);
    }
}

