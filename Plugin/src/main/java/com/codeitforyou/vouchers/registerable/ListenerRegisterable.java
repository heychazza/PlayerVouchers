package com.codeitforyou.vouchers.registerable;

import com.codeitforyou.vouchers.Vouchers;
import com.codeitforyou.vouchers.listener.InteractListener;
import com.codeitforyou.vouchers.listener.PlaceListener;
import org.bukkit.event.Listener;

public class ListenerRegisterable {
    private static final Listener[] LISTENERS = {
            new InteractListener(),
            new PlaceListener()
    };

    public static void register() {
        Vouchers vouchers = Vouchers.getInstance();
        for (Listener listener : LISTENERS) {
            vouchers.getServer().getPluginManager().registerEvents(listener, vouchers);
        }
    }
}
