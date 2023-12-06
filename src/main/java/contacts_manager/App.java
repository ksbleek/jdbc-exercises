package contacts_manager;

import contacts_manager.dao.ContactsDAO;
import contacts_manager.dao.FileContactsDAO;
import contacts_manager.dao.MySQLContactsDAO;
import contacts_manager.models.Contact;
import contacts_manager.utils.Input;

import java.io.IOException;
import java.util.List;

import static contacts_manager.dao.FileContactsDAO.printContacts;

public class App {
    public static void main(String[] args) throws IOException {
        Input input = new Input();
        ContactsDAO contactsDAO = new MySQLContactsDAO();
        contactsDAO.open();
        while(true){
            System.out.println("1. View contacts.\n" +
                    "2. Add a new contact.\n" +
                    "3. Search a contact by name.\n" +
                    "4. Delete an existing contact by name.\n" +
                    "5. Exit.\n" +
                    "Enter an option (1, 2, 3, 4 or 5):");
            int option = input.getInt(1, 5);
            switch (option){
                case 1:
                    List<Contact> contacts = contactsDAO.fetchContacts();
                    printContacts(contacts);
                    break;
                case 2:
                    String fn = input.getString("Give me the full name");
                    String phone = input.getString("Give me the phone number");
                    Contact contact = new Contact(fn, phone);
                    contactsDAO.insertContact(contact);
                    break;
                case 3:
                    String term = input.getString("Give me the name to search");
                    List<Contact> searchResults = contactsDAO.searchContacts(term);
                    printContacts(searchResults);
                    break;
                case 4:
                    String name = input.getString("Give me the name to delete");
                    contactsDAO.deleteByName(name);
                    break;
                case 5:
                    contactsDAO.close();
                    System.exit(0);
            }

        }


    }
}
