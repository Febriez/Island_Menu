package com.splendor.menu.data;

import com.splendor.menu.MenuMain;
import com.splendor.menu.manager.Menu;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlSetter {

    private static MysqlSetter instance;
    private final MysqlSetup mysql;

    private MysqlSetter() {
        instance = this;
        mysql = MenuMain.getInstance().getMysql();
    }

    public static MysqlSetter getInstance() {
        return instance == null ? new MysqlSetter() : instance;
    }

    private boolean registerMenu(Menu menu) {
        try {
            String sql = "INSERT INTO menuTbl (name,data,crow) values (?,?,?)";
            PreparedStatement s = mysql.getConnection().prepareStatement(sql);
            s.setString(1, menu.getName());
            s.setString(2, menu.getData());
            s.setInt(3, menu.getRow());
            s.execute();
            s.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("=====================================");
            System.out.println(menu.getName() + " failed!");
            return false;
        }
    }

    public boolean unregisterMenu(String name) {
        try {
            String sql = "DELETE FROM menuTbl WHERE name=?";
            PreparedStatement s = mysql.getConnection().prepareStatement(sql);
            s.setString(1, name);
            s.execute();
            s.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("=====================================");
            System.out.println(name + " failed!");
            return false;
        }
    }

    public void saveMenu(Menu menu) {
        if (menu.getData().equals("none"))
            return;
        if (MenuMain.getInstance().getMysqlGetter().getMenu(menu.getName()) == null) {
            registerMenu(menu);
            return;
        }
        try {
            String sql = "UPDATE menuTbl SET data=?,crow=? WHERE name=?";
            PreparedStatement s = mysql.getConnection().prepareStatement(sql);
            s.setString(1, menu.getData());
            s.setInt(2, menu.getRow());
            s.setString(3, menu.getName());
            s.execute();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
