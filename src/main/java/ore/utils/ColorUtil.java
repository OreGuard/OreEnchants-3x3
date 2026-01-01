package ore.utils;

import net.md_5.bungee.api.ChatColor;
import ore.OreEnchants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    private static boolean isHexColor = OreEnchants.getVersionControl().isHexColor();
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String getColor(String text) {
        if (isHexColor) {
            for(Matcher matcher = pattern.matcher(text); matcher.find(); matcher = pattern.matcher(text)) {
                String color = text.substring(matcher.start(), matcher.end());
                text = text.replace(color, ChatColor.of(color) + "");
            }
        }

        return org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
    }

}
