import dao.MySQLQuotesDAO;
import models.Quote;

import java.util.List;

public class QuotesTest {

    public static void main(String[] args) {
        MySQLQuotesDAO quotesDAO = new MySQLQuotesDAO();
        quotesDAO.createConnection();
        List<Quote> quotesFromDB = quotesDAO.getQuotes();
        for (Quote quote : quotesFromDB){
            System.out.println(quote);
        }
        quotesDAO.closeConnection();
    }

}
