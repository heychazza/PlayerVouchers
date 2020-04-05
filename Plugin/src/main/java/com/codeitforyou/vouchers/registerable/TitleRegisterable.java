package com.codeitforyou.vouchers.registerable;

import com.codeitforyou.vouchers.nms.Title;
import org.bukkit.Bukkit;

public class TitleRegisterable {
    private static Title title;

    public static Title getTitle() {
        return title;
    }

    public static void register() {
        try {
            String packageName = Title.class.getPackage().getName();
            String internalsName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            String classPath = packageName + "." + internalsName + ".Title";
            title = (Title) Class.forName(classPath).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException exception) {
            throw new RuntimeException("Could not find a valid implementation for this server version!");
        }
    }
}
