package com.codeitforyou.vouchers.listener;

import com.codeitforyou.vouchers.api.Voucher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceListener implements Listener {

    @EventHandler
    public void onVoucherInteract(BlockPlaceEvent e) {
        if (Voucher.getVoucher(e.getItemInHand()) != null) e.setCancelled(true);
    }
}
