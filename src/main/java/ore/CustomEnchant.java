package ore;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomEnchant {

    public ItemStack createItemWithTag(ItemStack item, String tag, String value) {
        if (OreEnchants.getVersionControl().isLegacy()) {
            try {
                Class classCraftItemStack = Class.forName("org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack");
                Class classItemStack = Class.forName("net.minecraft.server.v1_12_R1.ItemStack");
                Class classNBTTagCompound = Class.forName("net.minecraft.server.v1_12_R1.NBTTagCompound");
                Object itemNMS = classCraftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke((Object)null, item);
                Object nbtTag = classItemStack.getMethod("getTag").invoke(itemNMS);
                if (nbtTag == null) {
                    nbtTag = classNBTTagCompound.getConstructor().newInstance();
                }

                classNBTTagCompound.getMethod("setString", String.class, String.class).invoke(nbtTag, tag, value);
                classItemStack.getMethod("setTag", classNBTTagCompound).invoke(itemNMS, nbtTag);
                ItemStack result = (ItemStack)classCraftItemStack.getMethod("asCraftMirror", classItemStack).invoke((Object)null, itemNMS);
                return result;
            } catch (Exception var10) {
                throw new RuntimeException(var10);
            }
        } else {
            NamespacedKey key = new NamespacedKey(OreEnchants.getInstance(), tag);
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(key, PersistentDataType.STRING, value);
            item.setItemMeta(meta);
            return item;
        }
    }

    public String getTag(ItemStack item, String tag) {
        if (item != null && !item.getType().equals(Material.AIR)) {
            if (OreEnchants.getVersionControl().isLegacy()) {
                try {
                    Object itemNMS = Class.forName("org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke((Object)null, item);
                    Object nbtTag = Class.forName("net.minecraft.server.v1_12_R1.ItemStack").getMethod("getTag").invoke(itemNMS);
                    if (nbtTag == null) {
                        return null;
                    } else {
                        Object result = Class.forName("net.minecraft.server.v1_12_R1.NBTTagCompound").getMethod("getString", String.class).invoke(nbtTag, tag);
                        return result == null ? null : (String)result;
                    }
                } catch (Exception var6) {
                    throw new RuntimeException(var6);
                }
            } else {
                NamespacedKey key = new NamespacedKey(OreEnchants.getInstance(), tag.toLowerCase().replace(" ", ""));
                ItemMeta meta = item.getItemMeta();
                if (meta == null) {
                    return null;
                }

                PersistentDataContainer container = meta.getPersistentDataContainer();

                if (container.has(key, PersistentDataType.STRING)) {
                    return container.get(key, PersistentDataType.STRING);
                }
                return null;


            }
        } else {
            return null;
        }
    }

}
