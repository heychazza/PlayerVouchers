package com.codeitforyou.vouchers.registerable;

import com.codeitforyou.lib.api.command.CommandManager;
import com.codeitforyou.vouchers.Vouchers;
import com.codeitforyou.vouchers.command.*;
import com.codeitforyou.vouchers.util.Lang;

import java.util.Arrays;

public class CommandRegisterable {
    private static final Vouchers POUCHES = Vouchers.getInstance();
    private static CommandManager commandManager;

    public static void register() {
        commandManager = new CommandManager(
                Arrays.asList(
                        HelpCommand.class,
                        GiveCommand.class,
                        GiveAllCommand.class,
                        ReloadCommand.class,
                        CreateCommand.class,
                        ListCommand.class
                ), POUCHES.getDescription().getName().toLowerCase(), POUCHES);

        commandManager.setMainCommand(MainCommand.class);
        CommandManager.Locale locale = commandManager.getLocale();

        locale.setUnknownCommand(Lang.ERROR_INVALID_COMMAND.asString(Lang.PREFIX.asString()));
        locale.setPlayerOnly(Lang.ERROR_PLAYER_ONLY.asString(Lang.PREFIX.asString()));
        locale.setUsage(Lang.COMMAND_USAGE.asString(Lang.PREFIX.asString(), "{usage}"));
        locale.setNoPermission(Lang.ERROR_NO_PERMISSION_COMMAND.asString(Lang.PREFIX.asString()));

        Arrays.asList("voucher", "vouchers").forEach(commandManager::addAlias);
        commandManager.register();
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}
