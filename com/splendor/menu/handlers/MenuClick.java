package com.splendor.menu.handlers;

import com.splendor.menu.MenuMain;
import com.splendor.menu.manager.Menu;
import com.splendor.menu.manager.MenuManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class MenuClick implements Listener {

    private final MenuManager manager = MenuMain.getInstance().getManager();

    @EventHandler
    public void Swap(PlayerSwapHandItemsEvent e) {
        if (e.getPlayer().isSneaking())
            e.getPlayer().openInventory(new com.splendor.menu.guis.Menu().getInv());
    }

    @EventHandler
    public void MainMenuClick(InventoryClickEvent e) {

        if (e.getClickedInventory() == null)
            return;

        String s = e.getView().getTopInventory().getTitle();
        Player p = (Player) e.getWhoClicked();

        if (!manager.isMenu(s.equals("아일랜드 메뉴") ? "main" : s))
            return;

        if (!isItem(e.getCurrentItem()))
            return;

        e.setCancelled(true);

        if (!e.getClickedInventory().getTitle().equals(s))
            return;

        Menu menu = manager.getMenu(s.equals("아일랜드 메뉴") ? "main" : s);
        menu.getItem(e.getSlot()).run(p);
    }

    private boolean isItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

}
