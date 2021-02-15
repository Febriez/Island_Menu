package com.splendor.menu.manager;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {

    private static MenuManager instance;
    private static Map<String, Menu> map;

    private MenuManager() {
        instance = this;
        map = new HashMap<>();
    }

    public static MenuManager getInstance() {
        if (instance == null)
            new MenuManager();
        return instance;
    }

    public static Map<String, Menu> getMap() {
        return map;
    }

    public Menu getMenu(String name) {
        return map.get(name);
    }

    public boolean isMenu(String name) {
        return map.containsKey(name);
    }

    public void registerMenu(String name) {
        map.put(name, new Menu(this, name));
    }

    public void loadMenu(String name, String data, int row) {
        map.put(name, new Menu(this, name, row, data));
    }
}
