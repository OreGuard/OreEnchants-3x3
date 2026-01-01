package ore.nms;

import org.bukkit.Bukkit;

import java.util.Arrays;

public class VersionControl {

    private String version = null;

    public VersionControl() {
        this.getVersion();
    }

    public String getVersion() {
        if (this.version == null) {
            String testPaper = Bukkit.getServer().getVersion();
            if (testPaper.contains("1.20.6") && !testPaper.contains("Spigot")) {
                this.version = "v1_20_R4";
            } else if (testPaper.contains("1.21") && !testPaper.contains("Spigot")) {
                this.version = "v1_21_R1";
            } else {
                this.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            }
        }

        return this.version;
    }

    public boolean isHexColor() {
        return Arrays.asList("v1_12_R1", "v1_13_R1", "v1_13_R2", "v1_14_R1", "v1_15_R1", "v1_16_R1", "v1_16_R2", "v1_16_R3").stream().noneMatch((v) -> {
            return this.version.equals(v);
        });
    }

    public boolean isLegacy() {
        return this.version.equals("v1_12_R1");
    }

}
