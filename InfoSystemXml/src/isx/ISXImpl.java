package isx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;

import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.Map;
import java.util.TreeSet;
import java.util.LinkedList;




/**
 * Created by artyom on 17.10.15.
 */
public class ISXImpl implements ISX {

    private DocumentBuilder dBuilder;
    private Document dataBase;
    private Element root;
    private Deque<Integer> freeStdNum;//used for supplying uniqueness of identifiers
    private String lastFind = "*";
    private DataBaseProperties properties;

    public ISXImpl() throws Exception {
        this(new DataBaseProperties());
    }

    public ISXImpl(DataBaseProperties properties) throws Exception {
        this.properties  = properties;
        File xmlFile = new File(properties.DATABASE_PATH);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();

        root = (Element) dataBase.getElementsByTagName(properties.DATA_NAME).item(0);
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
                stdNum = new Integer(element.getAttribute("id"));
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
        printHeader();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE ) {
                Element element = (Element) node;

                String id = element.getAttribute("id");
                String searchAttrVal = element.getElementsByTagName(properties.SEARCH_UNIT_NAME).item(0).getTextContent();
                if(!searchAttrVal.matches(".*" + toBeFound + ".*")){
                    continue;
                }

                printUnit(element);
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

                String unitId = element.getAttribute("id");
                if(unitId.equals(id)) {
                    element.getElementsByTagName(attribute).item(0).setTextContent(value);

                    String[] toBeChecked = new String[properties.ATTRIBUTES.length];
                    for(int j=0; j < properties.ATTRIBUTES.length; j++){
                        toBeChecked[j] = element.getElementsByTagName(properties.ATTRIBUTES[j].getName()).item(0).getTextContent();
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

        Map<String, String> outputProperties = properties.OUTPUT_PROPERTIES;

        for (Map.Entry entry : outputProperties.entrySet()) {
            transformer.setOutputProperty((String) entry.getKey(), (String) entry.getValue());
        }


        DOMSource source = new DOMSource(dataBase);
        File xmlFile = new File(properties.DATABASE_PATH);
        StreamResult result = new StreamResult(xmlFile);

        transformer.transform(source, result);

        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();
        root = (Element) dataBase.getElementsByTagName(properties.DATA_NAME).item(0);
    }

    @Override
    public void add(String[] newAttributes) throws IllegalArgumentException, TransformerException, IOException, SAXException {
        if(!isValid(newAttributes))
            throw new IllegalArgumentException();
        //!TODO trst wether exists

        Element unit = dataBase.createElement(properties.UNIT_NAME);
        Attr id = dataBase.createAttribute("id");
        id.setValue(getStdtId().toString());
        unit.setAttributeNode(id);
        root.appendChild(unit);

        for(int i=0; i < properties.ATTRIBUTES.length; i++){
            Element element = dataBase.createElement(properties.ATTRIBUTES[i].getName());
            element.appendChild(dataBase.createTextNode(newAttributes[i]));
            unit.appendChild(element);
        }

        saveChanges();

        find(lastFind);
    }

    /**
     * Checks whether string array of ATTRIBUTES is valid
     * @param atributes
     */
    private boolean isValid(String[] atributes){
        if(atributes.length != properties.ATTRIBUTES.length) {
            return false;
        }

        boolean result = true;

        for(int i=0; i < properties.ATTRIBUTES.length; i++){
            result |= atributes[i].matches(properties.ATTRIBUTES[i].getCheckRgx());
        }
        return result;
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

                String unitId = element.getAttribute("id");
                if (unitId.equals(id)) {
                    root.removeChild(element);
                }
            }
        }

        saveChanges();
    }

    private void printUnit(Element e){
        String unitId = e.getAttribute("id");
        System.out.format("%5s|",unitId);

        for(int i=0; i < properties.ATTRIBUTES.length; i++){
            String attrFormat = "%" + properties.ATTRIBUTES[i].getMaxLenth() + "s|";
            String attr = e.getElementsByTagName(properties.ATTRIBUTES[i].getName()).item(0).getTextContent();
            System.out.format(attrFormat, attr);
        }
        System.out.println();
    }

    private void printHeader(){
        System.out.format("%5s|","id");

        for(int i=0; i < properties.ATTRIBUTES.length; i++){
            String attrFormat = "%" + properties.ATTRIBUTES[i].getMaxLenth() + "s|";
            System.out.format(attrFormat, properties.ATTRIBUTES[i].getName());
        }
        System.out.println();

        System.out.format("%5s|","-----");
        for(int i=0; i < properties.ATTRIBUTES.length; i++){
            int attrLenght = properties.ATTRIBUTES[i].getMaxLenth();
            String attrFormat = "%" + attrLenght + "s|";
            StringBuffer filler = new StringBuffer();
            for(int j=0; j<attrLenght; j++) {
                filler.append('-');
            }
            System.out.format(attrFormat,filler.toString());
        }
        System.out.println();
    }
}
