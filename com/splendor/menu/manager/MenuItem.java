package com.splendor.menu.manager;

import com.splendor.menu.MenuMain;
import com.splendor.menu.utils.Converter;
import com.splendor.menu.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuItem implements Listener {

    private final MenuManager manager = MenuMain.getInstance().getManager();
    private final Converter con = new Converter();

    private Menu menu;

    private int slot;
    private ItemStack item;
    private Works work;
    private String data;

    private Inventory inv;
    private Inventory inInv;
    private Player cur;

    public MenuItem(ItemStack item, int slot, Menu menu) {
        this.slot = slot;
        this.item = item;
        this.menu = menu;
        this.work = Works.NONE;
        this.data = "";
        Bukkit.getPluginManager().registerEvents(this, MenuMain.getInstance());
    }

    public MenuItem(ItemStack item, int slot, Menu menu, String data, String work) {
        this.slot = slot;
        this.item = item;
        this.data = data;
        this.work = getWork(work);
        Bukkit.getPluginManager().registerEvents(this, MenuMain.getInstance());
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public boolean isBuildable() {
        return isItem(item) && work != Works.NONE && !data.equals("");
    }

    public void run(Player p) {
        switch (work) {
            case MENU:
                p.openInventory(manager.getMenu(data).getInv());
                return;
            case COMMAND:
                Bukkit.dispatchCommand(p, data);
                return;
            case TELEPORT:
                p.teleport(con.getLocation(data));
                return;
            case DECO:
                return;
            default:
                throw new RuntimeException("없는 데이터 형식 (Code:002)");
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Works getWork() {
        return work;
    }

    public void setWork(Works work) {
        this.work = work;
    }

    public Inventory getSetting() {
        if (inv != null)
            return inv;
        inv = Bukkit.createInventory(null, InventoryType.HOPPER, "아이템 설정");
        inv.setItem(1, new ItemBuilder(Material.BOOK).setDisplayName("§a기능 설정").build());
        inv.setItem(2, new ItemBuilder(Material.PAPER).setDisplayName("§b기능 값 설정").build());
        inv.setItem(3, new ItemBuilder(Material.BARRIER).setDisplayName("§c삭제").build());
        return inv;
    }

    private Inventory getFunc() {
        if (inInv != null)
            return inInv;
        inInv = Bukkit.createInventory(null, InventoryType.HOPPER, "기능을 선택해주세요");
        inInv.setItem(0, new ItemBuilder(Material.YELLOW_FLOWER).setDisplayName("§d장식품").build());
        inInv.setItem(1, new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§a텔레포트").build());
        inInv.setItem(2, new ItemBuilder(Material.BOOK_AND_QUILL).setDisplayName("§b명령어 실행").build());
        inInv.setItem(3, new ItemBuilder(Material.CHEST).setDisplayName("§6메뉴 열기").build());
        inInv.setItem(4, new ItemBuilder(Material.BARRIER).setDisplayName("§c뒤로가기").build());
        return inInv;
    }

    private Works getWork(String s) {
        for (Works w : Works.values()) {
            if (w.getSName().equals(s))
                return w;
        }
        return null;
    }

    public enum Works {
        TELEPORT("TELEPORT"), COMMAND("COMMAND"), MENU("MENU"), NONE("NONE"), DECO("DECO");

        private final String s;

        Works(String s) {
            this.s = s;
        }

        public String getSName() {
            return s;
        }
    }

    private boolean isItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    @EventHandler
    public void Click(InventoryClickEvent e) {

        if (!(e.getClickedInventory() == inv || e.getView().getTopInventory() == inv))
            return;

        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();

        switch (e.getSlot()) {
            case 1:
                p.sendMessage("설정할 기능을 선택해주세요");
                p.openInventory(getFunc());
                break;
            case 2:
                switch (work) {
                    case MENU:
                        cur = p;
                        p.closeInventory();
                        p.sendMessage("채팅창에 열릴 메뉴의 이름을 적어주세요");
                        break;
                    case COMMAND:
                        cur = p;
                        p.closeInventory();
                        p.sendMessage("채팅창에 실행할 명령어를 적어주세요");
                        break;
                    case TELEPORT:
                        cur = p;
                        p.closeInventory();
                        p.sendMessage("채팅창에 이동할 위치를 적어주세요");
                        p.sendMessage("예) world,1.5,5,2.5,90,0");
                        p.sendMessage(HoverMessage("[내 위치]", ChatColor.AQUA, getLocation(p.getLocation()), "클릭 시 현재 위치를 채팅창에 입력합니다"));
                        break;
                }
                break;
            case 3:
                menu.removeItem(slot);
                p.closeInventory();
                p.sendMessage("§c삭제 되었습니다");
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void ClickIn(InventoryClickEvent e) {

        if (!(e.getClickedInventory() == inInv || e.getView().getTopInventory() == inInv))
            return;

        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();

        switch (e.getSlot()) {
            case 0:
                work = Works.DECO;
                p.sendMessage("기능을 §d장식품§f으로 설정하였습니다");
                p.openInventory(getSetting());
                break;
            case 1:
                work = Works.TELEPORT;
                p.sendMessage("기능을 §a텔레포트§f로 설정하였습니다");
                p.openInventory(getSetting());
                break;
            case 2:
                work = Works.COMMAND;
                p.sendMessage("기능을 §b명령어 실행§f으로 설정하였습니다");
                p.openInventory(getSetting());
                break;
            case 3:
                work = Works.MENU;
                p.sendMessage("기능을 §6메뉴 열기§f로 설정하였습니다");
                p.openInventory(getSetting());
                break;
            case 4:
                p.openInventory(getSetting());
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void Chat(AsyncPlayerChatEvent e) {

        if (!e.getPlayer().equals(cur))
            return;

        Player p = e.getPlayer();
        String msg = e.getMessage();

        e.setCancelled(true);

        if (msg.equalsIgnoreCase("cancel")) {
            cur = null;
            return;
        }

        switch (work) {
            case MENU:
                if (!manager.isMenu(msg)) {
                    p.sendMessage("없는 메뉴입니다 [ \"Cancel\" 로 취소]");
                    break;
                }
                data = msg;
                p.sendMessage("열릴 메뉴가 " + data + " (으)로 설정되었습니다");
                cur = null;
                break;
            case COMMAND:
                data = msg;
                p.sendMessage("실행할 명령어가 " + data + " (으)로 설정되었습니다");
                cur = null;
                break;
            case TELEPORT:
                Location loc;
                try {
                    String[] arg = msg.split(",");
                    loc = new Location(Bukkit.getWorld(arg[0]), Double.parseDouble(arg[1]), Double.parseDouble(arg[2]), Double.parseDouble(arg[3]), Float.parseFloat(arg[4]), Float.parseFloat(arg[5]));
                } catch (RuntimeException e1) {
                    p.sendMessage("잘못된 위치 구문입니다 [ \"Cancel\" 로 취소]");
                    break;
                }
                data = msg;
                p.sendMessage("위치가 " + loc.toString() + " (으)로 설정되었습니다");
                cur = null;
                break;
        }
    }

    private TextComponent HoverMessage(String message, ChatColor color, String show, String hover) {
        TextComponent var = new TextComponent("");
        var.setText(message);
        var.setColor(color);
        var.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, show));
        var.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        return var;
    }

    private String getLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getPitch() + "," + loc.getYaw();
    }

}