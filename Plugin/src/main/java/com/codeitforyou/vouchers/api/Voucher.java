package com.codeitforyou.vouchers.api;

import com.codeitforyou.lib.api.general.PAPIUtil;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.nbt.NBT;
import com.codeitforyou.vouchers.Vouchers;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Voucher {
    private static final Map<String, Voucher> vouchers = Maps.newConcurrentMap();
    private final Vouchers VOUCHERS = Vouchers.getInstance();
    private String id;
    private ItemStack item;
    private boolean permission;
    private List<Reward> rewards;
    private List<String> blacklistedRegions;
    private List<String> blacklistedWorlds;

    public Voucher(String id) {
        this.id = id;
    }

    public Voucher(String id, ItemStack item) {
        this.id = id;
        this.item = item;
    }

    public Voucher(String id, ItemStack item, boolean permission, List<Reward> rewards, List<String> blacklistedRegions, List<String> blacklistedWorlds) {
        this.id = id;
        this.item = item;
        this.permission = permission;
        this.rewards = rewards;
        this.blacklistedRegions = blacklistedRegions;
        this.blacklistedWorlds = blacklistedWorlds;
    }

    public static Voucher getVoucher(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        NBT nbt = NBT.get(itemStack);
        if (nbt != null && nbt.hasNBTData() && nbt.hasKey("vouchers-id") && Vouchers.getInstance().getVoucherManager().getVoucher(nbt.getString("vouchers-id")) != null)
            return Vouchers.getInstance().getVoucherManager().getVoucher(nbt.getString("vouchers-id"));

        return null;
    }

    public static Map<String, Voucher> getVouchers() {
        return vouchers;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemStack getItem(Player player) {
        ItemStack itemStack = item.clone();
        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta.hasDisplayName()) {
                itemMeta.setDisplayName(StringUtil.translate(PAPIUtil.parse(player, itemMeta.getDisplayName())));
            }

            if (itemMeta.hasLore()) {
                List<String> updatedLore = itemMeta.getLore();
                updatedLore.replaceAll(lore -> StringUtil.translate(PAPIUtil.parse(player, lore)));
                itemMeta.setLore(updatedLore);
            }

            itemStack.setItemMeta(itemMeta);

        }
        return itemStack;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public boolean requiresPerm() {
        return permission;
    }

    public List<Reward> getRewards() {
        return new ArrayList<>(rewards);
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public List<String> getBlacklistedRegions() {
        return blacklistedRegions;
    }

    public void setBlacklistedRegions(List<String> blacklistedRegions) {
        this.blacklistedRegions = blacklistedRegions;
    }

    public List<String> getBlacklistedWorlds() {
        return blacklistedWorlds;
    }

    public void setBlacklistedWorlds(List<String> blacklistedWorlds) {
        this.blacklistedWorlds = blacklistedWorlds;
    }

    public boolean hasPermission(Player player) {
        if (requiresPerm()) return player.hasPermission("vouchers.use." + getId().toLowerCase());
        return true;
    }

    public void runRewards(Player player) {
        List<Reward> rewards = getRewards();
        rewards.sort(Comparator.comparing(Reward::getChance));

        boolean hasReward = false;
        while (!hasReward) {
            double randomChance = ThreadLocalRandom.current().nextDouble(100);
//            VOUCHERS.getLogger().info(" ");
//            VOUCHERS.getLogger().info("Random Chance: " + randomChance + "%");
//            VOUCHERS.getLogger().info(" ");
            for (Reward reward : rewards) {
//                VOUCHERS.getLogger().info("Reward Chance: " + reward.getChance() + "%");
//                VOUCHERS.getLogger().info("Reward Actions: " + StringUtils.join(reward.getActions(), ";"));
                boolean lessOrEqual = reward.canExecute(randomChance);
//                VOUCHERS.getLogger().info("Reward Calculation: " + reward.getChance() + "<=" + randomChance + ": " + (lessOrEqual ? "Yes!" : "No.."));
                if (lessOrEqual) {
//                    VOUCHERS.getLogger().info("!! EXECUTED REWARD WITH CHANCE " + reward.getChance() + "% !!");
                    hasReward = true;
                    reward.execute(player, this);
                    break;
                }

//                VOUCHERS.getLogger().info(" ");
            }
//            VOUCHERS.getLogger().info("- - -");
        }
    }

//                StringBuilder stringBuilder = new StringBuilder();
//
//                String unrevealed = number == 0 ? "" : numberStr.substring(0, number);
//                String revealed = number == 0 ? numberStr : numberStr.split(unrevealed, 2)[1];
//
//                stringBuilder.append(getUnrevealedFirstFormat());
//                stringBuilder.append(unrevealed);
//
//                stringBuilder.append(getUnrevealedSecondFormat());
//                number--;
//                stringBuilder.append(revealed);
//                stringBuilder.append("&r");
//
//                JSONObject titleObj = new JSONObject();
//                titleObj.put("text", stringBuilder.toString());
//
//                TitleRegisterable.getTitle().sendTitle(player, StringUtil.translate(titleObj.toJSONString()));
//
//                JSONObject subTitleObj = new JSONObject();
//                subTitleObj.put("text", getUnrevealedSubtitle());
//
//                TitleRegisterable.getTitle().sendSubtitle(player, StringUtil.translate(subTitleObj.toJSONString()));
}
