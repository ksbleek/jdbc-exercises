package dao;

import com.mysql.cj.jdbc.Driver;
import config.Config;
import models.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Album> fetchAlbums() {
        List<Album> albums = new ArrayList<>();
        Album album = null;
        // TODO: write your code here

        Statement statement = null;
        try {
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM albums");
            while (rs.next()){
                album = new Album();
                album.setId(rs.getLong("id"));
                album.setArtist(rs.getString("artist"));
                album.setName(rs.getString("name"));
                album.setReleaseDate(rs.getInt("release_date"));
                album.setSales(rs.getDouble("sales"));
                album.setGenre(rs.getString("genre"));
                albums.add(album);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return albums;
    }


    public Album fetchAlbumById(long id) {
        Album album = null;

        // TODO: write your code here

        try {
            PreparedStatement st = connection.prepareStatement("select * from albums " +
                    " where id = ? ");
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            rs.next();

                album = new Album();
                album.setId(rs.getLong("id"));
                album.setArtist(rs.getString("artist"));
                album.setName(rs.getString("name"));
                album.setReleaseDate(rs.getInt("release_date"));
                album.setSales(rs.getDouble("sales"));
                album.setGenre(rs.getString("genre"));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return album;
    }

    // Note that insertAlbum should return the id that MySQL creates for the new inserted album record
    public long insertAlbum(Album album) throws MySQLAlbumsException {
        long id = 0;

        // TODO: write your code here
        PreparedStatement st = null;
        String artist = album.getArtist();
        String name = album.getName();
        String release_date = String.valueOf(album.getReleaseDate());
        String sales = String.valueOf(album.getSales());
        String genre = album.getGenre();

        try {
            st = connection.prepareStatement("insert into albums " +
                    " (artist, name, release_date, sales, genre) " +
                    " values(?, ?, ?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, artist);
            st.setString(2, name);
            st.setString(3, release_date);
            st.setString(4, sales);
            st.setString(5, genre);

            int numInserted = st.executeUpdate();

            ResultSet keys = st.getGeneratedKeys();
            keys.next();

            id = keys.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    public void updateAlbum(Album album) throws MySQLAlbumsException {

        // TODO: write your code here
        try {
            PreparedStatement st = connection.prepareStatement("update albums " +
                    " set artist = ? " +
                    " , name = ? " +
                    " , release_date = ? " +
                    " , sales = ? " +
                    " , genre = ? " +
                    " where id = ? ");
            st.setString(1, album.getArtist());
            st.setString(2, album.getName());
            st.setInt(3, album.getReleaseDate());
            st.setDouble(4, album.getSales());
            st.setString(5, album.getGenre());
            st.setLong(6, album.getId());
            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAlbumById(long id) throws MySQLAlbumsException {

        // TODO: write your code here
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("delete from albums " +
                    " where id = ? ");
            st.setLong(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
