package com.my_pls;
import java.sql.Connection;
import java.sql.DriverManager;
public class MySqlConnection {
    static Connection conn;

    public static Connection getConnection() {

        try {

            System.out.println("Connecting... ");

            Class.forName("com.mysql.cj.jdbc.Driver");

            // Database link, username and password
            conn = DriverManager.getConnection("jdbc:mysql://b8bfeaec94d3d2:b7e7427e@us-cdbr-east-02.cleardb.com/heroku_d47d83ae3d838cf?reconnect=true");
            System.out.println("Connection Succeeded");

        } catch (Exception e) {
            System.out.println(" Error at MySqlConnection.java: " + e);
        }

        return conn;
    }
}