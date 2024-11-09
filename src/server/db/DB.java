package server.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

public class DB {

    private static LinkedList<Connection> connectionPool;
    private static final int MAX_POOL_SIZE = 10;

    static {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {

            throw new DBException("Não foi possível conectar-se ao banco MySQL!");

        }

        connectionPool = new LinkedList<>();

        Properties props = loadProperties();
        String url = props.getProperty("dburl");

        try {

            for (int i = 0; i < MAX_POOL_SIZE; i++) {

                Connection conn = DriverManager.getConnection(url, props);
                connectionPool.add(conn);

            }

        } catch (SQLException e) {

            throw new DBException("Falha ao inicializar pool de conexões com o DB...");

        }

        System.out.println("CONNECTION::POOL::SUCCESSFULLY::CREATED");

    }

    private DB() {}

    private static Properties loadProperties() {

        try(FileInputStream fis = new FileInputStream("src/server/db/db.properties")) {

            Properties props = new Properties();
            props.load(fis);
            return props;

        } catch (IOException e) {

            throw new DBException(e.getMessage());

        }

    }

    public static synchronized Connection getConnection() {

        if (connectionPool.isEmpty()) throw new DBException("Não há conexões disponíveis na pool.");
        else return connectionPool.removeFirst();

    }

    public static synchronized void releaseConnection(Connection conn) {

        if (conn != null) connectionPool.addLast(conn);

    }

    public static void closeConnectionPool() {

        try {

            for (Connection conn : connectionPool) conn.close();

        } catch (SQLException e) {

            throw new DBException("ERROR::CLOSING::CONNECTION::POOL");

        }

        System.out.println("CONNECTION::POOL::CLOSED");

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
