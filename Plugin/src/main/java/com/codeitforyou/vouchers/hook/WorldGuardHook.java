package com.codeitforyou.vouchers.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;

public class WorldGuardHook {

    private static boolean enabled;
    private static WorldGuardWrapper worldGuardWrapper;

    public static void register() {
        enabled = Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
        if (enabled) worldGuardWrapper = WorldGuardWrapper.getInstance();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static boolean checkIfPlayerInRegion(final Player player, final String region) {
        return worldGuardWrapper.getRegions(player.getLocation()).stream().anyMatch(iWrappedRegion -> iWrappedRegion.getId().equalsIgnoreCase(region));
    }

}
