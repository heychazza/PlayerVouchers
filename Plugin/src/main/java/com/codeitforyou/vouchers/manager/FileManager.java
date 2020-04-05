package com.codeitforyou.vouchers.manager;

import com.codeitforyou.vouchers.Vouchers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private List<FileConfiguration> voucherData;

    public FileManager(Vouchers vouchers) {
        voucherData = new ArrayList<>();
        File voucherFolder = new File(vouchers.getDataFolder(), "voucher");

        File[] files;
        if (!voucherFolder.exists()) {
            if (!voucherFolder.mkdirs()) {
                throw new RuntimeException("Failed to create " + voucherFolder.getPath() + " directory!");
            }
        }

        files = voucherFolder.listFiles();
        if (files != null) voucherData = map(files);
    }

    public static List<FileConfiguration> map(File[] files) {
        List<FileConfiguration> fileData = new ArrayList<>();
        for (File file : files) fileData.add(YamlConfiguration.loadConfiguration(file));
        return fileData;
    }

    public List<FileConfiguration> getVoucherConfigs() {
        return voucherData;
    }
}
