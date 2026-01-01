package ore.configs;

import ore.OreEnchants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.Iterator;

public class MainConfig {

    private YamlConfiguration config;
    private File configFile;

    public MainConfig() {
        this.initConfig();
        this.update();
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public void reload() {
        this.initConfig();
        ConfigData.reload();
    }

    private void initConfig() {
        if (!OreEnchants.getInstance().getDataFolder().exists()) {
            OreEnchants.getInstance().getDataFolder().mkdir();
        }

        this.configFile = new File(OreEnchants.getInstance().getDataFolder(), "config.yml");
        if (!this.configFile.exists()) {
            try {
                Files.copy(OreEnchants.getInstance().getResource("config.yml"), this.configFile.toPath(), new CopyOption[0]);
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    private void update() {
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(OreEnchants.getInstance().getResource("config.yml"), StandardCharsets.UTF_8));
        String oldVersion = this.config.getString("Version");
        String newVersion = newConfig.getString("Version");
        if (!oldVersion.equals(newVersion)) {
            Iterator var4 = newConfig.getKeys(true).iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                if (!this.config.contains(key)) {
                    this.config.set(key, newConfig.get(key));
                }
            }

            this.config.set("Version", newVersion);

            try {
                this.config.save(this.configFile);
            } catch (IOException var6) {
                throw new RuntimeException(var6);
            }
        }

    }

}
