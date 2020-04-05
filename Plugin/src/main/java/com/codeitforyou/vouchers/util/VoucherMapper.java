package com.codeitforyou.vouchers.util;

import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.nbt.NBT;
import com.codeitforyou.lib.api.xseries.XMaterial;
import com.codeitforyou.vouchers.api.Reward;
import com.codeitforyou.vouchers.api.Voucher;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoucherMapper {
    public static Voucher voucherMap(FileConfiguration data) {
        Voucher voucher = new Voucher(data.getString("id", "invalid"));
        voucher.setItem(itemstackMap(data));
        voucher.setPermission(data.getBoolean("settings.permission", false));

        ConfigurationSection rewards = data.getConfigurationSection("rewards");
        List<Reward> rewardList = new ArrayList<>();
        for (String reward : rewards.getKeys(false)) {
            double rewardChance = Double.parseDouble(reward);
            List<String> rewardActions = rewards.getStringList(reward + ".actions");
            rewardList.add(new Reward(rewardChance, rewardActions));
        }
        voucher.setRewards(rewardList);

        voucher.setBlacklistedRegions(data.getStringList("settings.blacklist.region"));
        voucher.setBlacklistedWorlds(data.getStringList("settings.blacklist.world"));
        return voucher;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack itemstackMap(FileConfiguration data) {
        if (data == null) return null;
        XMaterial voucherMaterial = XMaterial.matchXMaterial(data.getString("item.material", "PAPER")).orElse(null);

        if (voucherMaterial == null)
            throw new RuntimeException("Material " + data.getString("item.material", "PAPER") + " is invalid!");

        ItemStack itemStack = new ItemStack(voucherMaterial.parseMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();

        String voucherId = data.getString("id", "unknown");

        if (itemMeta != null) {
            itemMeta.setDisplayName(StringUtil.translate(data.getString("item.name", "&b" + voucherId + " Voucher")));
            itemMeta.setLore(StringUtil.translate(data.getStringList("item.lore")));

            if (data.getBoolean("item.glow", false)) {
                itemMeta.addEnchant(Enchantment.WATER_WORKER, 1, false);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            }
        }

        itemStack.setItemMeta(itemMeta);
        itemStack.setDurability((short) data.getInt("item.data", 0));

        if (data.getString("item.texture") != null && !data.getString("item.texture").isEmpty()) {
            if (XMaterial.PLAYER_HEAD.isOneOf(Collections.singletonList(data.getString("item.material", "PAPER")))) {
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                SkullUtils.getSkullByValue(skullMeta, data.getString("item.texture", ""));
                itemStack.setItemMeta(skullMeta);
            }
        }

        NBT nbt = NBT.get(itemStack);
        if (nbt != null) {
            nbt.setString("vouchers-id", voucherId);
            return nbt.apply(itemStack);
        }

        return null;
    }
}
