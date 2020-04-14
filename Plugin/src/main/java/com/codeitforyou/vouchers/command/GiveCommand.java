package com.codeitforyou.vouchers.command;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.vouchers.Vouchers;
import com.codeitforyou.vouchers.api.Voucher;
import com.codeitforyou.vouchers.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand {
    @Command(aliases = {"give"}, about = "Give a player a voucher.", permission = "vouchers.give", usage = "give <player> <voucher> [amount]", requiredArgs = 2)
    public static void execute(final CommandSender sender, final Vouchers plugin, final String[] args) {
        final int amount = args.length > 2 ? Integer.parseInt(args[2]) : 1;

        if (plugin.getVoucherManager().getVouchers().isEmpty()) {
            Lang.ERROR_NO_VOUCHERS_EXIST.send(sender, Lang.PREFIX.asString());
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Lang.ERROR_INVALID_PLAYER.send(sender, Lang.PREFIX.asString());
            return;
        }

        final Voucher voucher = Vouchers.getInstance().getVoucherManager().getVoucher(args[1]);
        if (voucher == null) {
            Lang.ERROR_INVALID_VOUCHER.send(sender, Lang.PREFIX.asString());
            return;
        }
        ItemStack voucherItem = voucher.getItem(target);
        voucherItem.setAmount(amount);
        target.getInventory().addItem(voucherItem);

        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (player.getUniqueId() == target.getUniqueId()) {
                // Self
                Lang.GIVE_COMMAND_SELF.send(sender, Lang.PREFIX.asString(), voucher.getId(), amount);
            } else {
                // Another player
                Lang.GIVE_COMMAND_OTHER.send(sender, Lang.PREFIX.asString(), target.getName(), voucher.getId(), amount);
            }
            return;
        }

        // Console
        Lang.GIVE_COMMAND_OTHER.send(sender, Lang.PREFIX.asString(), target.getName(), voucher.getId(), amount);
    }
}
