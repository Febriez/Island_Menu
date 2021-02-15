package com.splendor.menu.manager;

import com.splendor.menu.utils.Converter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Menu {

    private final MenuManager manager;
    private final Converter con = new Converter();

    private String name;
    private int row;
    private Map<Integer, MenuItem> map;

    public Menu(MenuManager manager, String name) {
        this.manager = manager;
        this.name = name;
        this.row = 1;
        map = new HashMap<>();
    }

    public Menu(MenuManager manager, String name, int row, String data) {
        this.manager = manager;
        this.name = name;
        this.row = row;
        map = new HashMap<>();
        Deserialization(data);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<MenuItem> getItems() {
        return map.values();
    }

    public MenuManager getManager() {
        return manager;
    }

    public boolean isItem(int slot) {
        return map.containsKey(slot);
    }

    public void removeItem(int slot) {
        map.remove(slot);
    }

    public void addItem(int slot, ItemStack item) {
        map.put(slot, new MenuItem(item, slot, this));
    }

    public MenuItem getItem(int slot) {
        return map.get(slot);
    }

    public String getData() {
        return Serialization();
    }

    public Inventory getInv() {
        Inventory inv = Bukkit.createInventory(null, 9 * row, name + " 메뉴");
        for (MenuItem item : map.values()) {
            inv.setItem(item.getSlot(), item.getItem());
        }
        return inv;
    }

    private void Deserialization(String s) {
        if (s.equals("none"))
            return;
        if (s.contains(" ::: "))
            for (String item : s.split(" ::: ")) {
                String[] data = item.split(" :: ");
                map.put(Integer.parseInt(data[1]), new MenuItem(con.getItem(data[0]), Integer.parseInt(data[1]), this, data[2], data[3]));
            }
        else {
            String[] data = s.split(" :: ");
            map.put(Integer.parseInt(data[1]), new MenuItem(con.getItem(data[0]), Integer.parseInt(data[1]), this, data[2], data[3]));
        }
    }

    private String Serialization() {
        StringBuilder s = new StringBuilder();
        for (MenuItem item : map.values())
            if (item.isBuildable())
                s.append(con.getString(item.getItem())).append(" :: ").append(item.getSlot()).append(" :: ").append(item.getData()).append(" :: ").append(item.getWork()).append(" ::: ");
        return s.length() > 5 ? s.substring(0, s.length() - 5) : "none";
    }
}
