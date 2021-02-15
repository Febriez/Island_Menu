package com.splendor.menu.guis;

import com.splendor.menu.MenuMain;
import com.splendor.menu.manager.MenuItem;
import com.splendor.menu.manager.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Menu {

    private final MenuManager manager = MenuMain.getInstance().getManager();

    private Inventory inv;

    public Inventory getInv() {
        if (!manager.isMenu("main")) {
            manager.registerMenu("main");
        }
        com.splendor.menu.manager.Menu menu = manager.getMenu("main");
        inv = Bukkit.createInventory(null, menu.getRow() * 9, "아일랜드 메뉴");
        for (MenuItem item : menu.getItems())
            inv.setItem(item.getSlot(), item.getItem());
        return inv;
    }

    public Inventory getInv(String s) {
        if (!manager.isMenu(s)) {
            manager.registerMenu(s);
        }
        com.splendor.menu.manager.Menu menu = manager.getMenu(s);
        inv = Bukkit.createInventory(null, menu.getRow() * 9, s);
        for (MenuItem item : menu.getItems())
            inv.setItem(item.getSlot(), item.getItem());
        return inv;
    }
}
