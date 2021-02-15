package com.splendor.menu.guis;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ItemSetting {

    private Inventory inv;

    public Inventory getInv() {
        inv = Bukkit.createInventory(null, InventoryType.DROPPER,"아이템 기능 설정");
        return inv;
    }
}
