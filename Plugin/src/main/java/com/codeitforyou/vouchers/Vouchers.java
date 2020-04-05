package com.codeitforyou.vouchers;

import com.codeitforyou.lib.api.actions.ActionManager;
import com.codeitforyou.vouchers.hook.WorldGuardHook;
import com.codeitforyou.vouchers.manager.FileManager;
import com.codeitforyou.vouchers.manager.VoucherManager;
import com.codeitforyou.vouchers.maven.LibraryLoader;
import com.codeitforyou.vouchers.maven.MavenLibrary;
import com.codeitforyou.vouchers.maven.Repository;
import com.codeitforyou.vouchers.registerable.CommandRegisterable;
import com.codeitforyou.vouchers.registerable.ListenerRegisterable;
import com.codeitforyou.vouchers.registerable.TitleRegisterable;
import com.codeitforyou.vouchers.util.Lang;
import com.codeitforyou.vouchers.util.VoucherMapper;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

@MavenLibrary(groupId = "com.github.CodeMC.WorldGuardWrapper", artifactId = "worldguardwrapper", version = "master-5e50edd862-1", repo = @Repository(url = "https://jitpack.io"))
public class Vouchers extends JavaPlugin {
    private static Vouchers instance;
    private FileManager fileManager;
    private VoucherManager voucherManager;
    private ActionManager actionManager;

    public static Vouchers getInstance() {
        return instance;
    }

    public VoucherManager getVoucherManager() {
        return voucherManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        actionManager = new ActionManager(this);
        actionManager.addDefaults();

        saveDefaultConfig();

        LibraryLoader.loadAll(Vouchers.class);
        TitleRegisterable.register();

        loadVouchers();

        CommandRegisterable.register();
        ListenerRegisterable.register();
        WorldGuardHook.register();
    }

    public void loadVouchers() {
        fileManager = new FileManager(instance);
        voucherManager = new VoucherManager(fileManager.getVoucherConfigs().stream().map(VoucherMapper::voucherMap).collect(Collectors.toList()));
        Lang.init(this);
    }

}
