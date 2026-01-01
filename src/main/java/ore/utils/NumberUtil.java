package ore.utils;

public class NumberUtil {

    public static double randomDouble(double min, double max) {
        return min + Math.floor(Math.random() * (max - min));
    }

    public static boolean getChance(double chance) {
        return chance >= randomDouble(0.0D, 100.0D);
    }

}
