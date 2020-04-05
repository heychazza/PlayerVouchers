package com.codeitforyou.vouchers.command;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.vouchers.Vouchers;
import com.codeitforyou.vouchers.api.Voucher;
import com.codeitforyou.vouchers.util.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class CreateCommand {
    @Command(aliases = {"create"}, about = "Create a voucher by the id <id>.", permission = "vouchers.create", usage = "create <id>", requiredArgs = 1)
    public static void execute(final CommandSender sender, final Vouchers plugin, final String[] args) {
        final String voucherId = args[0];

        final Voucher targetVoucher = plugin.getVoucherManager().getVoucher(voucherId);
        if (targetVoucher != null) {
            // Already exists?!
            Lang.ERROR_ALREADY_EXISTS.send(sender, Lang.PREFIX.asString());
            return;
        }

        File voucherFile = new File(plugin.getDataFolder() + "/voucher/" + voucherId.toLowerCase() + ".yml");
        if (!voucherFile.exists()) {
            try {
                if (voucherFile.getParentFile().mkdir()) {

                    if (!voucherFile.createNewFile()) {
                        throw new RuntimeException("Failed to create voucher file at " + voucherFile.getPath() + "!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration voucherConfig = YamlConfiguration.loadConfiguration(voucherFile);
        voucherConfig.options().header(plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " Configuration" + "\n" + "Configure your '" + voucherId.toLowerCase() + "' voucher here.");
        voucherConfig.set("id", voucherId);
        voucherConfig.set("item.name", "&b" + voucherId + " Voucher");
        voucherConfig.set("item.lore", Arrays.asList("&7Go into the plugin folder", "&7to edit this voucher.", "&7", "&7plugins/CIFYVouchers/voucher/" + voucherId.toLowerCase() + ".yml"));
        voucherConfig.set("item.glow", true);
        voucherConfig.set("item.material", "PAPER");
        voucherConfig.set("item.data", 0);
        voucherConfig.set("rewards.80.actions", Arrays.asList("[broadcast] &b%player% &7won &f$%amount% &7from a 80% voucher!", "[console] eco give %player% 80"));
        voucherConfig.set("rewards.20.actions", Arrays.asList("[broadcast] &b%player% &7won &f$%amount% &7from a 20% voucher!", "[console] eco give %player% 20"));
        voucherConfig.set("settings.permission", true);
        voucherConfig.set("settings.blacklist.region", Collections.singletonList("bad_region"));
        voucherConfig.set("settings.blacklist.world", Collections.singletonList("bad_world"));

        try {
            voucherConfig.save(voucherFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Lang.CREATE_COMMAND.send(sender, Lang.PREFIX.asString(), voucherId.toLowerCase());
        plugin.loadVouchers();

    }
}
