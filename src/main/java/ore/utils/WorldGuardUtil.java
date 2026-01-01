package ore.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ore.OreEnchants;

public class WorldGuardUtil {

    /**
     * Проверяет, может ли игрок ломать блоки в данной локации
     */
    public static boolean canBreakBlock(Player player, Location location) {
        if (!OreEnchants.isWorldGuardEnabled()) {
            return true;
        }

        try {
            WorldGuardPlugin worldGuard = WorldGuardPlugin.inst();

            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));


            return regions.testState(worldGuard.wrapPlayer(player), Flags.BLOCK_BREAK);

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    /**
     * Проверяет, находится ли локация в регионе с флагом "no-build" или "build-denied"
     */
    public static boolean isInProtectedRegion(Location location) {
        if (!OreEnchants.isWorldGuardEnabled()) {
            return false;
        }

        try {
            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));


            return !regions.getRegions().isEmpty() && !regions.testState(null, Flags.BUILD);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Проверяет, может ли игрок строить в данной локации
     */
    public static boolean canBuild(Player player, Location location) {
        if (!OreEnchants.isWorldGuardEnabled()) {
            return true;
        }

        try {
            WorldGuardPlugin worldGuard = WorldGuardPlugin.inst();
            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));

            return regions.testState(worldGuard.wrapPlayer(player), Flags.BUILD);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}