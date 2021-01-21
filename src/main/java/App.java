import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static final String UNIC_BINDING_NAME = "server.bookcase.validator";

    public static void main(String[] args) throws Exception
    {
        //создание реестра расшареных объетов
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2099);

        //получаем объект (на самом деле это proxy-объект)
        BookcaseValidator service = (BookcaseValidator) registry.lookup(UNIC_BINDING_NAME);


        List<Element> books = getBooks();
        for (int i = 0; i < books.size(); i++) {
            Element book = books.get(i);

            //Вызываем удаленный метод
            int result = service.getCountPageInBook(book);

            String title = book.getAttribute("title");
            System.out.println(title + ": " + result);
        }

    }

    private static List<Element> getBooks() {
        List<Element> booksResult = new ArrayList<Element>();
        DocumentBuilder documentBuilder;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true);

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            File file = new File("src/main/resources/bookcase.xml");

            Document document = documentBuilder.parse(file);

            NodeList bookshelves = document.getElementsByTagName("bookshelf");

            for (int i = 0; i < bookshelves.getLength(); i++) {
                Element bookshelf = (Element) bookshelves.item(i);

                NodeList books = bookshelf.getElementsByTagName("book");
                for (int j = 0; j < books.getLength(); j++) {
                    Element book = (Element) books.item(j);

                    booksResult.add(book);
                }
            }

            } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return booksResult;
    }
}
