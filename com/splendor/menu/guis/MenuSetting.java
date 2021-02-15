package com.splendor.menu.guis;

import com.splendor.menu.MenuMain;
import com.splendor.menu.manager.Menu;
import com.splendor.menu.manager.MenuItem;
import com.splendor.menu.manager.MenuManager;
import com.splendor.menu.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;

public class MenuSetting {

    private final MenuManager manager = MenuMain.getInstance().getManager();

    private Inventory inv;

    public Inventory getItemLoc() {
        inv = Bukkit.createInventory(null, 54, "메인메뉴 아이템 설정");

        return inv;
    }

    public Inventory getItemLoc(String name) {
        if (!manager.isMenu(name))
            return null;
        Menu menu = manager.getMenu(name);
        inv = Bukkit.createInventory(null, menu.getRow() * 9, name + " 아이템 배치");
        for (MenuItem item : menu.getItems())
            inv.setItem(item.getSlot(), item.getItem());
        return inv;
    }

    public Inventory getItemSetting(String name) {
        if (!manager.isMenu(name))
            return null;
        Menu menu = manager.getMenu(name);
        inv = Bukkit.createInventory(null, menu.getRow() * 9, name + " 아이템 설정");
        for (MenuItem item : menu.getItems()) {
            if (item.isBuildable())
                inv.setItem(item.getSlot(), item.getItem().clone());
            else if (item.getItem().hasItemMeta() && item.getItem().getItemMeta().hasDisplayName())
                inv.setItem(item.getSlot(), new ItemBuilder(item.getItem().clone()).HideECHANTMENT(true).addEnchants(Enchantment.LUCK, 1).setDisplayName("§r" + item.getItem().getItemMeta().getDisplayName()).build());
            else
                inv.setItem(item.getSlot(), new ItemBuilder(item.getItem().clone()).HideECHANTMENT(true).addEnchants(Enchantment.LUCK, 1).build());

        }
        return inv;
    }
}
