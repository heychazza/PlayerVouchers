package com.codeitforyou.vouchers.command;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.vouchers.Vouchers;
import com.codeitforyou.vouchers.util.Lang;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
    @Command(aliases = {"reload"}, about = "Reload the configuration files.", permission = "vouchers.reload", usage = "reload")
    public static void execute(final CommandSender sender, final Vouchers plugin, final String[] args) {
        plugin.reloadConfig();
        plugin.loadVouchers();
        Lang.RELOAD_COMMAND.send(sender, Lang.PREFIX.asString(), plugin.getVoucherManager().getVouchers().size());
    }
}
