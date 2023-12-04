package dao;

import com.mysql.cj.jdbc.Driver;
import config.Config;
import models.Quote;

import java.sql.*;

public class MySQLAlbumsDAO {
    // initialize the connection to null so we know whether or not to close it when done
    private Connection connection = null;

    public void createConnection() throws MySQLAlbumsException {
        System.out.print("Trying to connect... ");
        try {
            System.out.println("Opening DB connection ...");
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(
                    Config.getUrl(),
                    Config.getUser(),
                    Config.getPassword()
            );
            System.out.println("connection created.");
        } catch (SQLException e) {
            throw new MySQLAlbumsException("connection failed!!!");
        }
    }

    public int getTotalAlbums() throws MySQLAlbumsException {
        int count = 0;
        try {
            //TODO: fetch the total number of albums from the albums table and assign it to the local variable
            Statement statement = connection.createStatement();

            ResultSet resultCount = statement.executeQuery("SELECT count(*) as total FROM albums");
            resultCount.next();
            count = resultCount.getInt("total");

        } catch (SQLException e) {
            throw new MySQLAlbumsException("Error executing query: " + e.getMessage() + "!!!");
        }
        return count;
    }

    public void closeConnection() {
        if(connection == null) {
            System.out.println("Connection aborted.");
            return;
        }
        try {
            //TODO: close the connection
            connection.close();
            System.out.println("Connection closed.");
        } catch(SQLException e) {
            // ignore this
        }
    }

}
