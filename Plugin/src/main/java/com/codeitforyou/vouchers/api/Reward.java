package com.codeitforyou.vouchers.api;

import com.codeitforyou.vouchers.Vouchers;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Reward {

    private final double chance;
    private final List<String> actions;
    public Reward(double chance, List<String> actions) {
        this.chance = chance;
        this.actions = actions;
    }

    public double getChance() {
        return chance;
    }

    public List<String> getActions() {
        return actions;
    }

    public void execute(Player p, Voucher voucher) {
        List<String> rewards = new ArrayList<>(actions);
        rewards.replaceAll(item -> item.replace("%player%", p.getName()));
        rewards.replaceAll(item -> item.replace("%uuid%", p.getUniqueId().toString()));
        rewards.replaceAll(item -> item.replace("%voucher%", voucher.getId()));
        Vouchers.getInstance().getActionManager().runActions(p, rewards);
    }

    public boolean canExecute(double random) {
        int compare = Double.compare(random, chance);
        return compare <= 0;
    }
}
