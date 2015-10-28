package isx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;

import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Set    ;


/**
 * Created by artyom on 17.10.15.
 */
public class ISXImpl implements ISX {

    private DocumentBuilder dBuilder;
    private Document dataBase;
    private Element root;
    private Deque<Integer> freeStdNum;//used for supplying uniqueness of identifiers
    private String lastFind = "*";
    private Map attributes = new HashMap< Integer, String>();

    public ISXImpl() throws ParserConfigurationException, SAXException, IOException{
        File xmlFile = new File("University.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();

        root = (Element) dataBase.getElementsByTagName("students").item(0);
        NodeList nodeList = root.getChildNodes();

        freeStdNum = getFreeStdNum(nodeList);

        attributes.put(Integer.valueOf(0),"lastname");
        attributes.put(Integer.valueOf(1),"firstname");
        attributes.put(Integer.valueOf(2),"middlename");
        attributes.put(Integer.valueOf(3),"groupnum");
        attributes.put(Integer.valueOf(4),"schlrshptype");
        attributes.put(Integer.valueOf(5),"admissiondate");
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

        for(int i=1;;i++){
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
        System.out.println(freeStdNum);
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
        String attr[] = new String[attributes.size()];

        System.out.format("|%5s|%10s|%10s|%12s|%5s|%12s|%13s|\n","stdno","lastname","firstname","middlename","grp N","schlrshptype","admissiondate");
        System.out.format("|%5s|%10s|%10s|%12s|%5s|%12s|%13s|\n","-----","----------","----------","------------","-----","------------","-------------");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE ) {
                Element element = (Element) node;

                stdno           = element.getAttribute("stdno");
                String lastname        = element.getElementsByTagName("lastname"       ).item(0).getTextContent();
                if(!lastname.matches(".*" + toBeFound + ".*")){
                    continue;
                }

                for (Map.Entry entry : (Set<Map.Entry>) attributes.entrySet()) {
                    int num = ((Integer) entry.getKey()).intValue();
                    if(num == 0) {
                        continue;
                    }
                    attr[num] = element.getElementsByTagName((String) entry.getValue()).item(0).getTextContent();
                }

                System.out.format("|%5s|%10s|%10s|%12s|%5s|%12s|%13s|\n",stdno,lastname,attr[1],attr[2],attr[3],attr[4],attr[5]);
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

                    String[] toBeChecked = new String[attributes.size()];
                    for (Map.Entry entry : (Set<Map.Entry>) attributes.entrySet()) {
                        int num = ((Integer) entry.getKey()).intValue();
                        toBeChecked[num] = element.getElementsByTagName((String) entry.getValue()).item(0).getTextContent();
                    }

                    if (!isValid(toBeChecked))
                        throw new IllegalArgumentException("Invalid arguments for edit function");

                    saveChanges();
                }
            }
        }

    }

    private void saveChanges() throws TransformerException, IOException, SAXException{
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

    @Override
    public void add(String[] newAttributes) throws IllegalArgumentException, TransformerException, IOException, SAXException {
        if(!isValid(newAttributes))
            throw new IllegalArgumentException();
        //!TODO trst wether exists

        Element student = dataBase.createElement("student");
        Attr attr = dataBase.createAttribute("stdno");
        attr.setValue(getStdtId().toString());
        student.setAttributeNode(attr);
        root.appendChild(student);


        for (Map.Entry entry : (Set<Map.Entry>) attributes.entrySet()) {
            Element element = dataBase.createElement((String) entry.getValue());
            int i = ((Integer) entry.getKey()).intValue();
            element.appendChild(dataBase.createTextNode(newAttributes[i]));
            student.appendChild(element);
        }

        saveChanges();

        find(lastFind);
    }

    /**
     * Checks whether string array of ATTRIBUTES is valid
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

        saveChanges();
    }
}
