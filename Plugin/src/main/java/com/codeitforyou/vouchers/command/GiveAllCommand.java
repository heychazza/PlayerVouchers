package com.codeitforyou.vouchers.command;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.vouchers.Vouchers;
import com.codeitforyou.vouchers.api.Voucher;
import com.codeitforyou.vouchers.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveAllCommand {
    @Command(aliases = {"giveall"}, about = "Give online players a voucher.", permission = "vouchers.giveall", usage = "giveall <voucher> [amount]", requiredArgs = 1)
    public static void execute(final CommandSender sender, final Vouchers plugin, final String[] args) {
        final int amount = args.length > 1 ? Integer.parseInt(args[1]) : 1;

        if (plugin.getVoucherManager().getVouchers().isEmpty()) {
            Lang.ERROR_NO_VOUCHERS_EXIST.send(sender, Lang.PREFIX.asString());
            return;
        }

        final Voucher voucher = Vouchers.getInstance().getVoucherManager().getVoucher(args[0]);
        if (voucher == null) {
            Lang.ERROR_INVALID_VOUCHER.send(sender, Lang.PREFIX.asString());
            return;
        }

        int onlineCount = 0;
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            ItemStack voucherItem = voucher.getItem(player);
            voucherItem.setAmount(amount);
            player.getInventory().addItem(voucherItem);
            onlineCount++;
        }

        Lang.GIVE_ALL_COMMAND.send(sender, Lang.PREFIX.asString(), onlineCount, voucher.getId(), amount);
    }
}
