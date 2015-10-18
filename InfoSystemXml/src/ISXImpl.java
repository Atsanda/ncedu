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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by artyom on 17.10.15.
 */
public class ISXImpl implements ISX {

    private Document dataBase;
    private Deque<Integer> freeStdNum;//used for supplying uniqueness of identifiers

    public ISXImpl() throws ParserConfigurationException, SAXException, IOException{
        File xmlFile = new File("University.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();

        freeStdNum = new LinkedList<Integer>();

        Element root = (Element) dataBase.getElementsByTagName("students").item(0);
        NodeList nodeList = root.getChildNodes();

        int tmpStdNum = -1;

        for(int j=1,tmpNodeInd = 0;;) {
            if(tmpNodeInd == nodeList.getLength()){
                freeStdNum.add(tmpStdNum+1);
                break;
            }

            Node node = nodeList.item(tmpNodeInd);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                tmpStdNum = Integer.parseInt(element.getAttribute("stdno"));
                if(tmpStdNum == j){
                    tmpNodeInd++;
                    j++;
                }
                if(tmpStdNum > j) {
                    freeStdNum.add(j);
                    j++;
                }
            }else {
                tmpNodeInd++;
            }
        }
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
    public void add(String[] atributes) {

    }

    @Override
    public void delete(String toBeDeleted) {

    }
}
