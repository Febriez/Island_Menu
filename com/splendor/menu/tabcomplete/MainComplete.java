package com.splendor.menu.tabcomplete;

import com.splendor.menu.MenuMain;
import com.splendor.menu.manager.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainComplete implements TabCompleter {

    private final MenuManager manager = MenuMain.getInstance().getManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg, String[] args) {

        if (!(sender instanceof Player))
            return null;

        Player p = (Player) sender;

        if (!p.isOp())
            return null;

        if (args.length == 1) {
            List<String> tab = new ArrayList<>();
            tab.add("열기");
            tab.add("추가");
            tab.add("설정");
            Collections.sort(tab);
            return tab;
        }

        if (args.length == 2) {
            List<String> tab = new ArrayList<>(MenuManager.getMap().keySet());
            Collections.sort(tab);
            return tab;
        }
        return null;
    }
}
