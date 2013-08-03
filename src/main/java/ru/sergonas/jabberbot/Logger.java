package ru.sergonas.jabberbot;

import java.sql.*;
import java.util.Date;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 23:38
 */
public class Logger{
    private Connection conn;
    public Logger() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:log");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log(String user, Date time, String msg) {
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("INSERT INTO LOGS(TIME, USER, MESSAGE) VALUES (?,?,?);");
            pstmt.setTimestamp(1, new Timestamp(time.getTime()));
            pstmt.setString(2, user);
            pstmt.setString(3, msg);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
