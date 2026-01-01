package ore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ore.configs.ConfigData;
import ore.utils.ColorUtil;
import ore.utils.NumberUtil;
import ore.utils.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PickaxeListener implements Listener {

    public PickaxeListener() {
        Bukkit.getPluginManager().registerEvents(this, OreEnchants.getInstance());
    }

    @EventHandler
    public void enchantNew(EnchantItemEvent e) {
        if (e.getItem() != null && e.getExpLevelCost() == 30 && NumberUtil.getChance(ConfigData.CHANCE_TO_GET_ENCHANT())) {
            ItemStack item = e.getItem();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore() == null ? new ArrayList() : meta.getLore();
            ((List)lore).addAll((Collection) ConfigData.MESSAGE_TO_LORE().stream().map((it) -> {
                return ColorUtil.getColor(it);
            }).collect(Collectors.toList()));
            meta.setLore((List)lore);
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            item.setItemMeta(meta);
            ItemStack result = OreEnchants.getCustomEnchant().createItemWithTag(item.clone(), "Pickaxe3x3", "Pickaxe3x3");
            Bukkit.getScheduler().scheduleSyncDelayedTask(OreEnchants.getInstance(), () -> {
                item.setAmount(0);
                e.getEnchanter().closeInventory();
                e.getEnchanter().getInventory().addItem(new ItemStack[]{result});
            }, 2L);
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void checkPickAxeBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        // Проверяем, что игрок держит кирку
        if (player.getInventory().getItemInMainHand() != null &&
                player.getInventory().getItemInMainHand().getType().toString().toUpperCase().contains("PICKAXE") &&
                !player.getGameMode().equals(GameMode.CREATIVE) &&
                player.getInventory().getItemInMainHand().getItemMeta() != null) {

            // Проверяем наличие зачарования
            String tag = OreEnchants.getCustomEnchant().getTag(player.getInventory().getItemInMainHand(), "Pickaxe3x3");
            if (tag == null || tag.length() == 0) {
                return;
            }

            // Проверяем взгляд игрока
            List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks((Set)null, 10);
            if (lastTwoTargetBlocks.size() != 2) {
                return;
            }

            Block targetBlock = (Block)lastTwoTargetBlocks.get(1);
            Block adjacentBlock = (Block)lastTwoTargetBlocks.get(0);
            BlockFace face = targetBlock.getFace(adjacentBlock);

            // Получаем все блоки для разрушения
            List<Block> blocksToBreak = this.getBlocks(e.getBlock(), face.toString());

            // Проверяем WorldGuard для каждого блока
            List<Block> allowedBlocks = new ArrayList<>();

            for (Block block : blocksToBreak) {
                // Проверяем разрешено ли разрушение
                if (WorldGuardUtil.canBreakBlock(player, block.getLocation())) {
                    allowedBlocks.add(block);
                }
            }

            // Ломаем только разрешенные блоки
            if (!allowedBlocks.isEmpty()) {
                this.breakBlocks(allowedBlocks);
            }
        }
    }

    private void breakBlocks(List<Block> blocks) {
        Iterator<Block> iterator = blocks.iterator();

        while(iterator.hasNext()) {
            Block b = iterator.next();
            Material material = b.getType();
            boolean isEnabled = ConfigData.ENABLED_BLOCKS().contains(material);
            if (isEnabled) {
                b.breakNaturally();
            }
        }
    }

    private List<Block> getBlocks(Block block, String face) {
        List<Block> blocks = new ArrayList();
        Location loc = block.getLocation();
        String var5 = face.toString();
        byte var6 = -1;
        switch(var5.hashCode()) {
            case 2715:
                if (var5.equals("UP")) {
                    var6 = 0;
                }
                break;
            case 2104482:
                if (var5.equals("DOWN")) {
                    var6 = 1;
                }
                break;
            case 2120701:
                if (var5.equals("EAST")) {
                    var6 = 2;
                }
                break;
            case 2660783:
                if (var5.equals("WEST")) {
                    var6 = 3;
                }
                break;
            case 74469605:
                if (var5.equals("NORTH")) {
                    var6 = 4;
                }
                break;
            case 79090093:
                if (var5.equals("SOUTH")) {
                    var6 = 5;
                }
        }

        switch(var6) {
            case 0:
                blocks.add(block);
                blocks.add(loc.clone().add(0.0D, 0.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 0.0D, -1.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, 0.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, 0.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, 0.0D, -1.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 0.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 0.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 0.0D, -1.0D).getBlock());
                break;
            case 1:
                blocks.add(block);
                blocks.add(loc.clone().add(0.0D, 0.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 0.0D, -1.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, 0.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, 0.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, 0.0D, -1.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 0.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 0.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 0.0D, -1.0D).getBlock());
                break;
            case 2:
                blocks.add(block);
                blocks.add(loc.clone().add(0.0D, 0.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 0.0D, -1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 1.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 1.0D, -1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, -1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, -1.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, -1.0D, -1.0D).getBlock());
                break;
            case 3:
                blocks.add(block);
                blocks.add(loc.clone().add(0.0D, 0.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 0.0D, -1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 1.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 1.0D, -1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, -1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, -1.0D, 1.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, -1.0D, -1.0D).getBlock());
                break;
            case 4:
                blocks.add(block);
                blocks.add(loc.clone().add(1.0D, 0.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 0.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, 1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, -1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, -1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, -1.0D, 0.0D).getBlock());
                break;
            case 5:
                blocks.add(block);
                blocks.add(loc.clone().add(1.0D, 0.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 0.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, 1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, 1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, 1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(0.0D, -1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(1.0D, -1.0D, 0.0D).getBlock());
                blocks.add(loc.clone().add(-1.0D, -1.0D, 0.0D).getBlock());
        }

        return blocks;
    }
}