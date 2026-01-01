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

        // Проверка разрешения для игроков
        if (sender instanceof Player && !sender.hasPermission("ore.pickaxe.give.3x3")) {
            sender.sendMessage(ColorUtil.getColor("&cУ вас нет прав на использование этой команды!"));
            return true;
        }

        Player targetPlayer = null;

        // Определение целевого игрока
        if (args.length == 0) {
            // Если аргументов нет и отправитель - игрок, выдать себе
            if (sender instanceof Player) {
                targetPlayer = (Player) sender;
            } else {
                sender.sendMessage("Использование: /orepickaxe <игрок>");
                return true;
            }
        } else if (args.length == 1) {
            // Если указано имя игрока
            targetPlayer = Bukkit.getPlayer(args[0]);

            if (targetPlayer == null) {
                sender.sendMessage(ColorUtil.getColor("&cИгрок " + args[0] + " не найден или не в сети!"));
                return true;
            }
        } else {
            // Неправильное количество аргументов
            if (sender instanceof Player) {
                sender.sendMessage(ColorUtil.getColor("&cИспользование: /orepickaxe [игрок]"));
            } else {
                sender.sendMessage("Использование: /orepickaxe <игрок>");
            }
            return true;
        }

        try {
            // Получаем предмет из ItemsAdder
            ItemStack pickaxe = ItemsAdder.getCustomItem("items:testpickaxe");

            if (pickaxe == null) {
                sender.sendMessage("§cПредмет не найден! Проверьте название предмета в ItemsAdder.");
                return true;
            }

            // Клонируем предмет, чтобы не изменять оригинал
            ItemStack enchantedPickaxe = pickaxe.clone();

            // Получаем мету предмета
            ItemMeta meta = enchantedPickaxe.getItemMeta();
            if (meta == null) {
                sender.sendMessage("§cНе удалось получить мету предмета!");
                return true;
            }

            // Добавляем PDC-тег для зачарования
            enchantedPickaxe = OreEnchants.getCustomEnchant().createItemWithTag(
                    enchantedPickaxe, "Pickaxe3x3", "enabled");

            // Получаем обновленную мету
            meta = enchantedPickaxe.getItemMeta();

            // Добавляем эффект блеска (LURE с флагом скрытия)
            meta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

            // Получаем существующий лор или создаем новый
            List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

            // Добавляем лор для зачарования
            ore.configs.ConfigData.MESSAGE_TO_LORE().forEach(message -> {
                lore.add(ColorUtil.getColor(message));
            });

            // Добавляем дополнительную информацию
            lore.add("");
            lore.add(ColorUtil.getColor("&7Бур 3x3"));
            lore.add(ColorUtil.getColor("&8Добывает блоки 3x3"));

            meta.setLore(lore);
            enchantedPickaxe.setItemMeta(meta);

            // Выдаем предмет игроку
            targetPlayer.getInventory().addItem(enchantedPickaxe);

            // Отправляем сообщения
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