import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by artyom on 17.10.15.
 */
public class ISXImpl implements ISX {

    Document dataBase;

    public ISXImpl() throws ParserConfigurationException, SAXException, IOException{
        File xmlFile = new File("University.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();
    }

    @Override
    public void print() {
        Element root = (Element) dataBase.getElementsByTagName("students").item(0);
        NodeList nodeList = root.getChildNodes();

        String stdno;
        String lastname;
        String firstname;
        String middlename;
        String groupnum;
        String schlrshptype;
        String admissiondate;

        System.out.format("|%5s|%10s|%10s|%12s|%5s|%12s|%13s|\n","stdno","lastname","firstname","middlename","grp N","schlrshptype","admissiondate");
        System.out.format("|%5s|%10s|%10s|%12s|%5s|%12s|%13s|\n","-----","----------","----------","------------","-----","------------","-------------");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE ) {
                Element element = (Element) node;

                stdno           = element.getAttribute("stdno");
                lastname        = element.getElementsByTagName("lastname"       ).item(0).getTextContent();
                firstname       = element.getElementsByTagName("firstname"      ).item(0).getTextContent();
                middlename      = element.getElementsByTagName("middlename"     ).item(0).getTextContent();
                groupnum        = element.getElementsByTagName("groupnum"       ).item(0).getTextContent();
                schlrshptype    = element.getElementsByTagName("schlrshptype"   ).item(0).getTextContent();
                admissiondate   = element.getElementsByTagName("admissiondate"  ).item(0).getTextContent();

                System.out.format("|%5s|%10s|%10s|%12s|%5s|%12s|%13s|\n",stdno,lastname,firstname,middlename,groupnum,schlrshptype,admissiondate);
            }
        }

    }

    @Override
    public void find(String toBeFound) {

    }

    @Override
    public void edit(String object, String attribute) {

    }

    @Override
    public void add(String toBeAdded) {

    }

    @Override
    public void delete(String toBeDeleted) {

    }
}
