package com.splendor.menu.data;

import com.splendor.menu.MenuMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MysqlGetter {

    private static MysqlGetter instance;
    private MysqlSetup mysql;

    private MysqlGetter() {
        instance = this;
        mysql = MenuMain.getInstance().getMysql();
    }

    public static MysqlGetter getInstance() {
        return instance == null ? new MysqlGetter() : instance;
    }

    public Map<String, String> getMenu(String name) {
        try {
            Map<String, String> hashmap = new HashMap<>();
            String sql = "SELECT data,crow FROM menuTbl WHERE name=?";
            PreparedStatement s = getStatement(sql);
            s.setString(1, name);
            ResultSet set = s.executeQuery();
            while (set.next()) {
                hashmap.put("data", set.getString("data"));
                hashmap.put("crow", set.getString("crow"));
            }
            if (hashmap.size() < 1)
                return null;
            return hashmap;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PreparedStatement getStatement(String sql) throws SQLException {
        mysql.checkConnection();
        return mysql.getConnection().prepareStatement(sql);
    }
}
