import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by artyom on 17.10.15.
 */
public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        ISXImpl tst = new ISXImpl();
        //tst.print();

        String[] lol = {"Харичкин","Никита","Евгеньевич","315","none","25.07.2013"};
        //System.out.println(tst.freeStdNum);
        //tst.find("Tsanda");
        //tst.add(lol);
        //tst.print();
        //tst.find("Sob");
        tst.print();
        tst.edit("1","groupnum","314");
    }
}
