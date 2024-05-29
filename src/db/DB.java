package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection conn = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DB() {}

    private static Properties loadProperties() {

        try(FileInputStream fis = new FileInputStream("src/db/db.properties")) {

            Properties props = new Properties();
            props.load(fis);
            return props;

        } catch (IOException e) {

            throw new DBException(e.getMessage());

        }

    }

    public static Connection getConnection() {

        try {

            if (conn == null) {

                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);

            }

            return conn;

        } catch (DBException | SQLException e) {

            throw new DBException(e.getMessage());

        }

    }

    public static void closeConnection() {

        try {

            if (conn != null) conn.close();


        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        }

    }

    public static void closeStatement(Statement st) {

        if (st != null) {

            try {

                st.close();

            } catch (SQLException e) {

                throw new DBException(e.getMessage());

            }

        }

    }

    public static void closeResultSet(ResultSet rs) {

        if (rs != null) {

            try {

                rs.close();

            } catch (SQLException e) {

                throw new DBException(e.getMessage());

            }

        }

    }


}
