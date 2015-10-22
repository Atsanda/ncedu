import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

/**
 * Created by artyom on 17.10.15.
 */
public class ISXImpl implements ISX {

    private DocumentBuilder dBuilder;
    private Document dataBase;
    private Element root;
    private Deque<Integer> freeStdNum;//used for supplying uniqueness of identifiers
    private String lastFind = "*";

    public ISXImpl() throws ParserConfigurationException, SAXException, IOException{
        File xmlFile = new File("University.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();

        root = (Element) dataBase.getElementsByTagName("students").item(0);
        NodeList nodeList = root.getChildNodes();

        freeStdNum = getFreeStdNum(nodeList);
    }

    private Deque<Integer> getFreeStdNum(NodeList nodeList){
        TreeSet<Integer> usedStdNum = new TreeSet<Integer>();
        Deque<Integer> unusedStdNum = new LinkedList<Integer>();
        int stdNum;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE ) {
                Element element = (Element) node;
                stdNum = new Integer(element.getAttribute("stdno"));
                usedStdNum.add(stdNum);
            }
        }

        for(int i=1;i<10;i++){
            if(usedStdNum.isEmpty()) {
                unusedStdNum.add(i);
                break;
            }else if(!usedStdNum.first().equals(new Integer(i))){
                unusedStdNum.add(i);
            }else{
                usedStdNum.pollFirst();
            }
        }

        return unusedStdNum;
    }

    private Integer getStdtId(){
        int ret = freeStdNum.getFirst();
        if(freeStdNum.isEmpty())
            freeStdNum.add(ret+1);
        return new Integer(ret);
    }

    @Override
    public void print() {
        find("*");
    }

    @Override
    public void find(String toBeFound) {
        if(toBeFound == null)
            return;
        toBeFound = (toBeFound.equals("*"))?(".*"):toBeFound;

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
                if(!lastname.matches(".*" + toBeFound + ".*")){
                    continue;
                }

                firstname       = element.getElementsByTagName("firstname"      ).item(0).getTextContent();
                middlename      = element.getElementsByTagName("middlename"     ).item(0).getTextContent();
                groupnum        = element.getElementsByTagName("groupnum"       ).item(0).getTextContent();
                schlrshptype    = element.getElementsByTagName("schlrshptype"   ).item(0).getTextContent();
                admissiondate   = element.getElementsByTagName("admissiondate"  ).item(0).getTextContent();

                System.out.format("|%5s|%10s|%10s|%12s|%5s|%12s|%13s|\n",stdno,lastname,firstname,middlename,groupnum,schlrshptype,admissiondate);
            }
        }

        lastFind = toBeFound;
    }

    @Override
    public void edit(String id, String attribute, String value) throws IllegalArgumentException, TransformerException, IOException, SAXException {
        if(id == null || attribute == null || value == null)
            throw new IllegalArgumentException("Invalid arguments for edit function");

        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE ) {
                Element element = (Element) node;

                String stdno = element.getAttribute("stdno");
                if(stdno.equals(id)) {
                    element.getElementsByTagName(attribute).item(0).setTextContent(value);

                    String[] toBeChecked = {
                            element.getElementsByTagName("lastname").item(0).getTextContent(),
                            element.getElementsByTagName("firstname").item(0).getTextContent(),
                            element.getElementsByTagName("middlename").item(0).getTextContent(),
                            element.getElementsByTagName("groupnum").item(0).getTextContent(),
                            element.getElementsByTagName("schlrshptype").item(0).getTextContent(),
                            element.getElementsByTagName("admissiondate").item(0).getTextContent()
                    };

                    if (!isValid(toBeChecked))
                        throw new IllegalArgumentException("Invalid arguments for edit function");

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    DOMSource source = new DOMSource(dataBase);
                    File xmlFile = new File("University.xml");
                    StreamResult result = new StreamResult(xmlFile);

                    transformer.transform(source, result);

                    dataBase = dBuilder.parse(xmlFile);
                    dataBase.getDocumentElement().normalize();
                    root = (Element) dataBase.getElementsByTagName("students").item(0);
                }
            }
        }

    }

    @Override
    public void add(String[] atributes) throws IllegalArgumentException, TransformerException, IOException, SAXException {
        if(!isValid(atributes))
            throw new IllegalArgumentException();
        //!TODO trst wether exists
        Element student = dataBase.createElement("student");
        Attr attr = dataBase.createAttribute("stdno");
        attr.setValue(getStdtId().toString());
        student.setAttributeNode(attr);
        root.appendChild(student);



        Element lastname = dataBase.createElement("lastname");
        lastname.appendChild(dataBase.createTextNode(atributes[0]));
        student.appendChild(lastname);

        Element firstname = dataBase.createElement("firstname");
        firstname.appendChild(dataBase.createTextNode(atributes[1]));
        student.appendChild(firstname);

        Element middlename = dataBase.createElement("middlename");
        middlename.appendChild(dataBase.createTextNode(atributes[2]));
        student.appendChild(middlename);

        Element groupnum = dataBase.createElement("groupnum");
        groupnum.appendChild(dataBase.createTextNode(atributes[3]));
        student.appendChild(groupnum);

        Element schlrshptype = dataBase.createElement("schlrshptype");
        schlrshptype.appendChild(dataBase.createTextNode(atributes[4]));
        student.appendChild(schlrshptype);

        Element admissiondate = dataBase.createElement("admissiondate");
        admissiondate.appendChild(dataBase.createTextNode(atributes[5]));
        student.appendChild(admissiondate);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(dataBase);
        File xmlFile = new File("University.xml");
        StreamResult result = new StreamResult(xmlFile);

        transformer.transform(source, result);

        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();
        root = (Element) dataBase.getElementsByTagName("students").item(0);

        find(lastFind);
    }

    /**
     * Checks whether string array of attributes is valid
     * @param atributes
     */
    private boolean isValid(String[] atributes){
        if(atributes.length != 6) {
            return false;
        }
        return  atributes[0].matches("^\\p{javaUpperCase}\\p{javaLowerCase}*( \\p{IsAlphabetic}+)*")    && // Kim chen is valid for eg
                atributes[1].matches("^\\p{javaUpperCase}\\p{javaLowerCase}*( \\p{IsAlphabetic}+)*")    &&
                atributes[2].matches("^\\p{javaUpperCase}\\p{javaLowerCase}*( \\p{IsAlphabetic}+)*")    &&
                atributes[3].matches("^[\\p{IsAlphabetic}0-9]+")                                        &&
                atributes[4].matches("^(increased)|(normal)|(none)")                                    &&
                atributes[5].matches("^(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.((19|20)\\d\\d)");//must be dd.mm.yyyy bad implemention !TODO
    }

    @Override
    public void delete(String id) throws IllegalArgumentException, TransformerException, IOException, SAXException {
        if(id == null)
            throw new IllegalArgumentException();

        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String stdno = element.getAttribute("stdno");
                if (stdno.equals(id)) {
                    root.removeChild(element);
                }
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(dataBase);
        File xmlFile = new File("University.xml");
        StreamResult result = new StreamResult(xmlFile);

        transformer.transform(source, result);

        dataBase = dBuilder.parcse(xmlFile);
        dataBase.getDocumentElement().normalize();
        root = (Element) dataBase.getElementsByTagName("students").item(0);
    }
}
