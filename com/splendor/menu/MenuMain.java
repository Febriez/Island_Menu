package com.splendor.menu;

import com.splendor.menu.commands.MainCommand;
import com.splendor.menu.data.MysqlGetter;
import com.splendor.menu.data.MysqlSetter;
import com.splendor.menu.data.MysqlSetup;
import com.splendor.menu.handlers.MenuClick;
import com.splendor.menu.handlers.MenuSetting;
import com.splendor.menu.manager.Menu;
import com.splendor.menu.manager.MenuManager;
import com.splendor.menu.tabcomplete.MainComplete;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuMain extends JavaPlugin {

    private static MenuMain instance;
    private static MenuManager manager;

    private MysqlSetup mysql;
    private MysqlGetter mysqlGetter;
    private MysqlSetter mysqlSetter;

    public static MenuMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        init();
        registerEvents();
        registerCommands();
        registerDatas();
        saveResources();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new MenuClick(), this);
        Bukkit.getPluginManager().registerEvents(new MenuSetting(), this);
    }

    @Override
    public void onDisable() {
        for (Menu menu : MenuManager.getMap().values())
            mysqlSetter.saveMenu(menu);
    }

    public MenuManager getManager() {
        return manager;
    }

    private void init() {
        instance = this;
        manager = MenuManager.getInstance();
        mysql = MysqlSetup.getInstance(this);
        mysqlGetter = MysqlGetter.getInstance();
        mysqlSetter = MysqlSetter.getInstance();
    }

    private void saveResources() {
        if (!new File(getDataFolder() + "/config.yml").exists()) {
            saveResource("config.yml", false);
        }
    }

    private void registerCommands() {
        getCommand("메뉴").setExecutor(new MainCommand());
        getCommand("메뉴").setTabCompleter(new MainComplete());
    }

    private void registerDatas() {
        boolean hasdata = false;
        try {
            String sql = "SELECT * FROM menuTbl";
            PreparedStatement s = getStatement(sql);
            ResultSet set = s.executeQuery();
            while (set.next()) {
                hasdata = true;
            }
        } catch (SQLException ignored) {
        }
        if (hasdata) {
            try {
                List<String> names = new ArrayList<>();
                String sql = "SELECT name FROM menuTbl";
                PreparedStatement s1 = getStatement(sql);
                ResultSet set = s1.executeQuery();
                while (set.next()) {
                    names.add(set.getString("name"));
                }
                for (String name : names) {
                    if (name != null) {
                        Map<String, String> dataMap = mysqlGetter.getMenu(name);
                        getManager().loadMenu(name, dataMap.get("data"), Integer.parseInt(dataMap.get("crow")));
                    }
                }
            } catch (SQLException ignored) {
            }
        }
    }

    public MysqlGetter getMysqlGetter() {
        return mysqlGetter;
    }

    public MysqlSetter getMysqlSetter() {
        return mysqlSetter;
    }

    public MysqlSetup getMysql() {
        return mysql;
    }

    public PreparedStatement getStatement(String sql) throws SQLException {
        mysql.checkConnection();
        return mysql.getConnection().prepareStatement(sql);
    }
}
