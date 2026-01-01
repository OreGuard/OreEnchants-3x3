package ore.configs;

import ore.OreEnchants;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigData {

    private static FileConfiguration config;
    private static double CHANCE_TO_GET_ENCHANT;
    private static List<Material> ENABLED_BLOCKS = new ArrayList();
    private static String YOU_ARE_NOT_THE_PLAYER;
    private static String YOU_DONT_HAVE_THE_PERMISSION;
    private static String ENCHANTED_MESSAGE;
    private static String PLUGIN_RELOADED;
    private static String ITS_NOT_A_PICKAXE;
    private static List<String> MESSAGE_TO_LORE = new ArrayList();

    public static void reload() {
        config = OreEnchants.getMainConfig().getConfig();
        CHANCE_TO_GET_ENCHANT = config.getDouble("Chance to get this enchant in the table");
        ENABLED_BLOCKS = (List)config.getStringList("Enabled blocks").stream().map((it) -> {
            return Material.getMaterial(it);
        }).filter((it) -> {
            return it != null;
        }).collect(Collectors.toList());
        YOU_ARE_NOT_THE_PLAYER = config.getString("You can not the player");
        YOU_DONT_HAVE_THE_PERMISSION = config.getString("You dont have the permission");
        ENCHANTED_MESSAGE = config.getString("Enchanted message");
        PLUGIN_RELOADED = config.getString("Plugin reloaded");
        ITS_NOT_A_PICKAXE = config.getString("Its not a pickaxe in your main hand");
        MESSAGE_TO_LORE = config.getStringList("Enchant message to lore");
    }

    public static double CHANCE_TO_GET_ENCHANT() {
        return CHANCE_TO_GET_ENCHANT;
    }

    public static List<Material> ENABLED_BLOCKS() {
        return ENABLED_BLOCKS;
    }

    public static String YOU_ARE_NOT_THE_PLAYER() {
        return YOU_ARE_NOT_THE_PLAYER;
    }

    public static String YOU_DONT_HAVE_THE_PERMISSION() {
        return YOU_DONT_HAVE_THE_PERMISSION;
    }

    public static String ENCHANTED_MESSAGE() {
        return ENCHANTED_MESSAGE;
    }

    public static String PLUGIN_RELOADED() {
        return PLUGIN_RELOADED;
    }

    public static String ITS_NOT_A_PICKAXE() {
        return ITS_NOT_A_PICKAXE;
    }

    public static List<String> MESSAGE_TO_LORE() {
        return MESSAGE_TO_LORE;
    }

}
