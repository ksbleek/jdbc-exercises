package contacts_manager.dao;

import com.mysql.cj.jdbc.Driver;
import config.Config;
import contacts_manager.models.Contact;
import dao.MySQLAlbumsException;
import models.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLContactsDAO implements ContactsDAO{

    private Connection connection = null;

    @Override
    public List<Contact> fetchContacts() {
        List<Contact> contacts = new ArrayList<>();
        Contact contact = null;
        // TODO: write your code here

        Statement statement = null;
        try {
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM Contact");
            while (rs.next()){
                contact = new Contact();
                contact.setId(rs.getLong("id"));
                contact.setFullName(rs.getString("fullName"));
                contact.setPhoneNumber(rs.getString("phoneNumber"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contacts;
    }

    @Override
    public long insertContact(Contact contact) {
        long id = 0;

        // TODO: write your code here
        PreparedStatement st = null;
        String fn = contact.getFullName();
        String pn = contact.getPhoneNumber();

        try {
            st = connection.prepareStatement("insert into Contact " +
                    " (fullName, phoneNumber) " +
                    " values(?, ?) ", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, contact.getFullName());
            st.setString(2, contact.getPhoneNumber());


            int numInserted = st.executeUpdate();

            ResultSet keys = st.getGeneratedKeys();
            keys.next();

            id = keys.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    @Override
    public void deleteByName(String name) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("delete from Contact " +
                    " where fullName like ? ");
            st.setString(1, name);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Contact> searchContacts(String term) {
        List<Contact> contacts = fetchContacts();
        List<Contact> results = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.getFullName().toLowerCase().contains(term.toLowerCase())) {
                results.add(contact);
            }
        }

        return results;
    }

    @Override
    public void open() {

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

    @Override
    public void close() {

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
