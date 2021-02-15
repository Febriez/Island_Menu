package com.splendor.menu.commands;

import com.splendor.menu.MenuMain;
import com.splendor.menu.guis.Menu;
import com.splendor.menu.guis.MenuSetting;
import com.splendor.menu.manager.MenuManager;
import com.splendor.menu.utils.Converter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private final MenuManager manager = MenuMain.getInstance().getManager();
    private final Converter con = new Converter();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player p = (Player) sender;

        if (!p.isOp()) {
            p.openInventory(new Menu().getInv());
            return false;
        }

        if (args.length == 0) {
            p.sendMessage("/메뉴 열기 : 메뉴를 엽니다");
            p.sendMessage("/메뉴 추가 <이름>");
            p.sendMessage("/메뉴 배치 <이름> : 메뉴에 아이템을 배치할 수 있습니다");
            p.sendMessage("/메뉴 설정 <이름> : 메뉴 아이템을 배치 후 설정할 수 있습니다");
            p.sendMessage("/메뉴 줄 <이름> <크기> : 메뉴 줄수를 설정합니다");
            p.sendMessage("왼 클릭으로 아이템을 우클릭으로 속성을 설정합니다");
            return true;
        }

        if (args[0].equals("열기")) {
            p.openInventory(new Menu().getInv());
            return false;
        }

        if (args[0].equals("줄")) {
            if (args.length != 3) {
                p.sendMessage("/메뉴 줄 <이름> <크기> : 메뉴 줄수를 설정합니다");
                return false;
            }
            if (!manager.isMenu(args[1])) {
                p.sendMessage(args[1] + " §c메뉴는 존재하지 않습니다");
                return false;
            }
            int a;
            try {
                a = Integer.parseInt(args[2]);
            } catch (RuntimeException e) {
                p.sendMessage("줄 수는 숫자만 가능합니다");
                return false;
            }
            if (a > 6 || a < 1) {
                p.sendMessage("줄 수는 1~6만 가능합니다");
                return false;
            }
            manager.getMenu(args[1]).setRow(a);
            p.sendMessage(args[1] + "메뉴의 줄 수가" + args[2] + "(으)로 설정되었습니다");
            return false;
        }

        if (args.length != 2) {
            p.sendMessage("/메뉴 추가 <이름>");
            p.sendMessage("/메뉴 배치 <이름> : 메뉴에 아이템을 배치할 수 있습니다");
            p.sendMessage("/메뉴 설정 <이름> : 메뉴 아이템을 배치 후 설정할 수 있습니다");
            return true;
        }

        if (args[0].equals("추가")) {
            if (manager.isMenu(args[1])) {
                p.sendMessage(args[1] + " §c메뉴는 이미 생성되어 있습니다");
                return false;
            }
            manager.registerMenu(args[1]);
            p.sendMessage(args[1] + " §c메뉴를 생성 하였습니다");
            return false;
        }

        if (args[0].equals("배치")) {
            if (!manager.isMenu(args[1])) {
                p.sendMessage(args[1] + " §c메뉴는 존재하지 않습니다");
                return false;
            }
            p.openInventory(new MenuSetting().getItemLoc(args[1]));
            return false;
        }

        if (args[0].equals("설정")) {
            if (!manager.isMenu(args[1])) {
                p.sendMessage(args[1] + " §c메뉴는 존재하지 않습니다");
                return false;
            }
            p.openInventory(new MenuSetting().getItemSetting(args[1]));
            return false;
        }

        return false;
    }
}
