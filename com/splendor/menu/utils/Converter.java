package com.splendor.menu.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Converter {

    public String getString(ItemStack item) {
        YamlConfiguration itemConfig = new YamlConfiguration();
        itemConfig.set("item", item);
        return itemConfig.saveToString();
    }

    public ItemStack getItem(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringBlob);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("item", null);
    }

    public String getString(Location l) {
        return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }

    public String getStringWithYaw(Location l) {
        return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "," + l.getYaw() +
                "," + l.getPitch();
    }

    public Location getLocation(final String s) {
        String[] parts = s.split(",");
        if (parts.length == 6) {
            World w = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            Bukkit.broadcastMessage(new Location(w, x, y, z, yaw, pitch).toString());
            return new Location(w, x, y, z, yaw, pitch);
        }
        if (parts.length != 4)
            return null;
        World w = Bukkit.getServer().getWorld(parts[0]);
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);
        return new Location(w, x, y, z);
    }

}
