package ore;

import ore.configs.ConfigData;
import ore.configs.MainConfig;
import ore.nms.VersionControl;
import org.bukkit.plugin.java.JavaPlugin;

public final class OreEnchants extends JavaPlugin {

    private static OreEnchants instance;
    private static VersionControl versionControl;
    private static MainConfig mainConfig;
    private static CustomEnchant customEnchant;
    private static boolean worldGuardEnabled = false;


    @Override
    public void onEnable() {
        instance = this;

        versionControl = new VersionControl();
        mainConfig = new MainConfig();
        ConfigData.reload();
        customEnchant = new CustomEnchant();
        new PickaxeListener();

        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardEnabled = true;
            getLogger().info("WorldGuard найден! Защита регионов активирована.");
        } else {
            getLogger().warning("WorldGuard не найден! Защита регионов отключена.");
        }

        getCommand("orepickaxe").setExecutor(new ore.GiveEnchantedPickaxeCommand());


    }

    @Override
    public void onDisable() {

    }

    public static OreEnchants getInstance() {
        return instance;
    }

    public static VersionControl getVersionControl() {
        return versionControl;
    }

    public static MainConfig getMainConfig() {
        return mainConfig;
    }

    public static CustomEnchant getCustomEnchant() {
        return customEnchant;
    }

    public static boolean isWorldGuardEnabled() {
        return worldGuardEnabled;
    }
}
