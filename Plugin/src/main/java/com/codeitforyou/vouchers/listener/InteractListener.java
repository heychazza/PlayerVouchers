package com.codeitforyou.vouchers.listener;

import com.codeitforyou.vouchers.api.Voucher;
import com.codeitforyou.vouchers.api.VoucherRedeemEvent;
import com.codeitforyou.vouchers.api.VoucherSlot;
import com.codeitforyou.vouchers.hook.WorldGuardHook;
import com.codeitforyou.vouchers.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVoucherInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack itemStack = e.getItem();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        Voucher voucher = Voucher.getVoucher(itemStack);
        if (voucher == null) return;

        e.setCancelled(true);

        VoucherSlot voucherSlot = VoucherSlot.MAIN_HAND;

        if (!Bukkit.getVersion().contains("1.8")) {
            if (e.getHand() == EquipmentSlot.OFF_HAND)
                voucherSlot = VoucherSlot.OFF_HAND;
        }

        if (!voucher.hasPermission(player)) {
            Lang.ERROR_NO_PERMISSION_VOUCHER.send(player, Lang.PREFIX.asString(), voucher.getId().toLowerCase());
            return;
        }

        if (voucher.getBlacklistedWorlds().contains(player.getWorld().getName())) {
            Lang.CANNOT_USE_IN_WORLD.send(player, Lang.PREFIX.asString());
            return;
        }

        if (WorldGuardHook.isEnabled()) {
            for (final String region : voucher.getBlacklistedRegions()) {
                if (WorldGuardHook.checkIfPlayerInRegion(player, region)) {
                    Lang.CANNOT_USE_IN_REGION.send(player, Lang.PREFIX.asString());
                    return;
                }
            }
        }

        VoucherRedeemEvent voucherRedeemEvent = new VoucherRedeemEvent(player, voucher, itemStack, voucherSlot);
        Bukkit.getServer().getPluginManager().callEvent(voucherRedeemEvent);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onVoucherRedeem(VoucherRedeemEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Voucher voucher = e.getVoucher();
        ItemStack voucherItem = e.getItem();

        if (e.getSlot() == VoucherSlot.MAIN_HAND) {
            if (voucherItem.getAmount() == 1) player.setItemInHand(null);
            else player.getItemInHand().setAmount(voucherItem.getAmount() - 1);
        } else {
            if (voucherItem.getAmount() == 1) player.getInventory().setItemInOffHand(null);
            else player.getInventory().getItemInOffHand().setAmount(voucherItem.getAmount() - 1);
        }

        voucher.runRewards(player);
    }
}
