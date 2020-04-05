package com.codeitforyou.vouchers.command;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.vouchers.Vouchers;
import com.codeitforyou.vouchers.api.Voucher;
import com.codeitforyou.vouchers.util.Lang;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand {
    @Command(aliases = {"list"}, about = "List loaded vouchers.", permission = "vouchers.list", usage = "list")
    public static void execute(final CommandSender sender, final Vouchers plugin, final String[] args) {
        List<Voucher> voucherList = plugin.getVoucherManager().getVouchers();

        if (voucherList.isEmpty()) {
            Lang.ERROR_NO_VOUCHERS_EXIST.send(sender, Lang.PREFIX.asString());
            return;
        }

        Lang.LIST_COMMAND_HEADER.send(sender, Lang.PREFIX.asString(), voucherList.size());
        for (Voucher voucher : voucherList) Lang.LIST_COMMAND_FORMAT.send(sender, Lang.PREFIX.asString(), voucher.getId());
        Lang.LIST_COMMAND_FOOTER.send(sender, Lang.PREFIX.asString(), voucherList.size());
    }
}
