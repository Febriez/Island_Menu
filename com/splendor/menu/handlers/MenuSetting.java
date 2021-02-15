package com.splendor.menu.handlers;

import com.splendor.menu.MenuMain;
import com.splendor.menu.manager.Menu;
import com.splendor.menu.manager.MenuItem;
import com.splendor.menu.manager.MenuManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuSetting implements Listener {

    private final MenuManager manager = MenuMain.getInstance().getManager();

    @EventHandler
    public void SettingMenu(InventoryCloseEvent e) {

        Player p = (Player) e.getPlayer();
        Inventory inv = e.getView().getTopInventory();

        if (!inv.getTitle().contains("아이템 배치"))
            return;

        Menu menu = manager.getMenu(inv.getTitle().replace(" 아이템 배치", ""));

        for (int a = 0; a < inv.getSize(); a++) {

            ItemStack invItem = inv.getItem(a);

            if (!isItem(invItem)) {
                if (menu.isItem(a)) {
                    menu.removeItem(a);
                }
                continue;
            }
            if (!menu.isItem(a)) {
                menu.addItem(a, invItem.clone());
                continue;
            }
            if (isSame(invItem, menu.getItem(a).getItem())) {
                continue;
            }
            menu.removeItem(a);
            menu.addItem(a, invItem.clone());
        }
        p.sendMessage("성공적으로 반영되었습니다 /메뉴 " + "설정 " + menu.getName());
    }

    @EventHandler
    public void SettingItem(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getView().getTopInventory();

        if (!inv.getTitle().contains("아이템 설정"))
            return;

        e.setCancelled(true);

        if (!e.getClickedInventory().getTitle().equals(inv.getTitle()))
            return;

        if (e.getClick() != ClickType.RIGHT)
            return;

        if (!isItem(e.getCurrentItem()))
            return;

        Menu menu = manager.getMenu(inv.getTitle().replace(" 아이템 설정", ""));
        MenuItem item = menu.getItem(e.getSlot());
        p.openInventory(item.getSetting());
    }

    private boolean isItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    private boolean isSame(ItemStack a, ItemStack b) {
        return a.isSimilar(b) && a.getAmount() == b.getAmount();
    }
}