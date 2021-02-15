package com.splendor.menu.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlSetup {

    private Plugin main;
    private Connection con;
    private static MysqlSetup instance;

    private MysqlSetup(Plugin pl) {
        this.main = pl;
        instance = this;
    }

    public static MysqlSetup getInstance(Plugin pl) {
        return instance == null ? new MysqlSetup(pl) : instance;
    }

    public Connection getConnection() {
        return con;
    }

    public void init() throws SQLException {
        setMysql();
        checkConnection();
    }

    private void setMysql() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (con != null && !con.isClosed()) {
                        con.createStatement().execute("SELECT 1");
                    }
                } catch (SQLException e) {
                    con = getNewConnection(main);
                }
            }
        }.runTaskTimerAsynchronously(main, 1200, 1200);
    }

    private Connection getNewConnection(Plugin main) {
        FileConfiguration config = main.getConfig();
        String host = config.getString("mysql.host");
        String port = config.getString("mysql.port");
        String database = config.getString("mysql.db");
        String user = config.getString("mysql.user");
        String pw = config.getString("mysql.pw");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?allowPublicKeyRetrieval=true&useSSL=false";
            return DriverManager.getConnection(url, user, pw);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() throws SQLException {
        if (con == null)
            return;
        con.close();
    }

    public boolean excute(String sql) throws SQLException {
        if (con == null || con.isClosed())
            return false;
        return con.createStatement().execute(sql);
    }

    public boolean checkConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            con = getNewConnection(main);
            if (con == null || con.isClosed())
                return false;
        }
        excute("CREATE TABLE IF NOT EXISTS menuTbl (" + "name VARCHAR(64) NOT NULL,"
                + "data LONGTEXT NOT NULL," + "crow INT NOT NULL," + "PRIMARY KEY (`name`)" + ")");
        return true;
    }
}
