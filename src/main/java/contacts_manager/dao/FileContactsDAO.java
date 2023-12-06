package contacts_manager.dao;

import contacts_manager.dao.ContactsDAO;
import contacts_manager.models.Contact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FileContactsDAO implements ContactsDAO {

    private final static String directory = "data";
    private final static String filename = "contacts.txt";

    private final static Path dataDirectory = Paths.get(directory);
    private final static Path dataFile = Paths.get(directory, filename);

    @Override
    public List<Contact> fetchContacts() {
        List<Contact> contacts = new ArrayList<>();
        try  {

            List<String> contactsFromFile = Files.readAllLines(dataFile);

            Iterator<String> contactsIterator = contactsFromFile.iterator();
            while (contactsIterator.hasNext()) {
                String[] parts = contactsIterator.next().split("\\|");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String phoneNumber = parts[1].trim();
                    Contact contact = new Contact(name, phoneNumber);
                    contacts.add(contact);
                }
            };

        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }

        return contacts;
    }

    public static void printContacts(List<Contact> contacts){
        if (!contacts.isEmpty()) {
            System.out.printf("-----------------------------------------%n");
            System.out.printf("| %-17s | %-17s |%n", "Name", "Phone number");
            System.out.printf("-----------------------------------------%n");
            for (Contact contact : contacts) {
                System.out.printf("| %-17s | %-17s |%n", contact.getFullName(), contact.getPhoneNumber());
            }
            System.out.printf("-----------------------------------------%n");
        } else {
            System.out.println("Contact not found.");
        }
    }

    @Override
    public long insertContact(Contact contact) {
        String contactLine = String.format("%s|%s", contact.getFullName(), contact.getPhoneNumber());
        try {
            Files.write(
                    dataFile,
                    Arrays.asList(contactLine), // list with one item
                    StandardOpenOption.APPEND
            );
        } catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public void deleteByName(String name){
    try {
        List<Contact> contacts = fetchContacts();
        List<String> writeLines = new ArrayList<>();

        for (Contact contact: contacts) {
            if(!contact.getFullName().toUpperCase().startsWith(name.toUpperCase())){
                writeLines.add(contact.toString());
            }
        }

        Files.write(dataFile, writeLines);
    } catch (IOException e) {
        e.printStackTrace();
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

    }

    @Override
    public void close() {

    }
}
