package ore;

import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ore.OreEnchants;
import ore.utils.ColorUtil;

import java.util.ArrayList;
import java.util.List;

public class GiveEnchantedPickaxeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player && !sender.hasPermission("ore.pickaxe.give.3x3")) {
            sender.sendMessage(ColorUtil.getColor("&cУ вас нет прав на использование этой команды!"));
            return true;
        }

        Player targetPlayer = null;


        if (args.length == 0) {

            if (sender instanceof Player) {
                targetPlayer = (Player) sender;
            } else {
                sender.sendMessage("Использование: /orepickaxe <игрок>");
                return true;
            }
        } else if (args.length == 1) {

            targetPlayer = Bukkit.getPlayer(args[0]);

            if (targetPlayer == null) {
                sender.sendMessage(ColorUtil.getColor("&cИгрок " + args[0] + " не найден или не в сети!"));
                return true;
            }
        } else {

            if (sender instanceof Player) {
                sender.sendMessage(ColorUtil.getColor("&cИспользование: /orepickaxe [игрок]"));
            } else {
                sender.sendMessage("Использование: /orepickaxe <игрок>");
            }
            return true;
        }

        try {

            ItemStack pickaxe = ItemsAdder.getCustomItem("items:testpickaxe");

            if (pickaxe == null) {
                sender.sendMessage("§cПредмет не найден! Проверьте название предмета в ItemsAdder.");
                return true;
            }


            ItemStack enchantedPickaxe = pickaxe.clone();


            ItemMeta meta = enchantedPickaxe.getItemMeta();
            if (meta == null) {
                sender.sendMessage("§cНе удалось получить мету предмета!");
                return true;
            }


            enchantedPickaxe = OreEnchants.getCustomEnchant().createItemWithTag(
                    enchantedPickaxe, "Pickaxe3x3", "enabled");


            meta = enchantedPickaxe.getItemMeta();


            meta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

            List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

            ore.configs.ConfigData.MESSAGE_TO_LORE().forEach(message -> {
                lore.add(ColorUtil.getColor(message));
            });

            lore.add("");
            lore.add(ColorUtil.getColor("&7Бур 3x3"));
            lore.add(ColorUtil.getColor("&8Добывает блоки 3x3"));

            meta.setLore(lore);
            enchantedPickaxe.setItemMeta(meta);


            targetPlayer.getInventory().addItem(enchantedPickaxe);


            if (!sender.equals(targetPlayer)) {
                sender.sendMessage(ColorUtil.getColor("&aВы выдали кирку 3x3 игроку " + targetPlayer.getName()));
            }
            targetPlayer.sendMessage(ColorUtil.getColor("&aВы получили зачарованную кирку 3x3!"));

        } catch (Exception e) {
            sender.sendMessage("§cОшибка при выдаче предмета: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}