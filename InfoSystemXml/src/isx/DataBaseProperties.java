package isx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by artyom on 27.10.15.
 */
public class DataBaseProperties {

    public final String DATABASE_NAME;
    public final String DATABASE_PATH;
    public final String DATA_NAME;
    public final String UNIT_NAME;
    public final String SEARCH_UNIT_NAME;
    public final Attribute[] ATTRIBUTES;
    public final Map<String,String> OUTPUT_PROPERTIES;
    
    DataBaseProperties() throws Exception {
        File xmlFile = new File("conf.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder;
        Document dataBase;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            dataBase = dBuilder.parse(xmlFile);
        }catch(Exception e){
            throw new Exception("Problem with configuration file");
        }

        dataBase.getDocumentElement().normalize();

        DATABASE_NAME = setDataBaseName(dataBase);
        DATABASE_PATH = setDataBasePath(dataBase);
        DATA_NAME = setDataName(dataBase);
        UNIT_NAME = setUnitName(dataBase);
        ATTRIBUTES = setAttributes(dataBase);
        SEARCH_UNIT_NAME = ATTRIBUTES[searchUnitNum(dataBase) - 1].getName();
        OUTPUT_PROPERTIES = setOutputProperties(dataBase);
    }

    private String setDataBaseName(Document dataBase) {
        Element e;
        e = (Element) dataBase.getElementsByTagName("name").item(0);
        String dataBazeName = e.getTextContent();
        return dataBazeName;
    }

    private String setDataBasePath(Document dataBase) {
        Element e;
        e = (Element) dataBase.getElementsByTagName("path").item(0);
        String dataBazePath = e.getTextContent();
        return dataBazePath;
    }

    private String setDataName(Document dataBase) {
        Element e;
        e = (Element) dataBase.getElementsByTagName("dataname").item(0);
        String dataNamme = e.getTextContent();
        return dataNamme;
    }

    private Attribute[] setAttributes(Document dataBase) throws Exception {
        Element e;
        Attribute[] attributez = null;
        e = (Element) dataBase.getElementsByTagName("attributes").item(0);
        NodeList nodeList = e.getChildNodes();
        for (int i = 0, j = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE ) {
                Element element = (Element) node;

                if(element.getNodeName().equals("number")){
                    int num = Integer.parseInt(element.getTextContent());
                    attributez = new Attribute[num];
                }else if(element.getNodeName().equals("attribute")){
                    attributez[j] = new Attribute();
                    attributez[j].setName(element.getElementsByTagName("name").item(0).getTextContent());
                    attributez[j].setMaxLenth(Integer.parseInt(element.getElementsByTagName("max_lenght").item(0).getTextContent()));
                    attributez[j].setCheckRgx(element.getElementsByTagName("check_rgx").item(0).getTextContent());
                    j++;
                }else if(!element.getNodeName().equals("search_attribute")){
                    throw new Exception("Problem with configuration file");
                }
            }
        }

        return attributez;
    }

    private String setUnitName(Document dataBase) {
        Element e;
        e = (Element) dataBase.getElementsByTagName("unit").item(0);
        e = (Element) e.getElementsByTagName("name").item(0);
        String unitNamme = e.getTextContent();

        return unitNamme;
    }

    private Map<String, String> setOutputProperties(Document dataBase) {
        Element e;
        Map<String,String> outputPropertiez = null;
        e = (Element) dataBase.getElementsByTagName("outputpropetries").item(0);
        outputPropertiez = new HashMap<String, String>();
        NodeList nodeList = e.getChildNodes();
        for (int i = 0, j = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE ) {
                Element element = (Element) node;
                outputPropertiez.put(element.getElementsByTagName("key").item(0).getTextContent(),
                        element.getElementsByTagName("value").item(0).getTextContent());
            }
        }

        return outputPropertiez;
    }

    private int searchUnitNum(Document dataBase) {
        Element e;
        e = (Element) dataBase.getElementsByTagName("attributes").item(0);
        e = (Element) e.getElementsByTagName("search_attribute").item(0);
        return Integer.parseInt(e.getTextContent());
    }

    public void printDataBaseProperties(){
        System.out.print("=================================DATABASE_PROPERTIES=======================================\n" +
                "DATABASE_NAME  = " + DATABASE_NAME + " \n" +
                "DATABASE_PATH  = " + DATABASE_PATH + " \n" +
                "DATA_NAME      = " + DATA_NAME + " \n" +
                "UNIT_NAME      = " + UNIT_NAME + " \n" +
                "UNIT_ATTRIBUTES:\n");
        int nameFieldLenth = -1;
        for(Attribute attr: ATTRIBUTES){
            nameFieldLenth = (nameFieldLenth < attr.getMaxLenth()) ? attr.getMaxLenth() : nameFieldLenth;
        }
        System.out.format("\t%" + nameFieldLenth + "s\t%s\t%s\n","NAME","MAX_LENGHT","CHECKING_RGX");
        for(Attribute attr: ATTRIBUTES){
            System.out.format("\t%" + nameFieldLenth + "s%s", attr.getName(),"\t" + attr.getMaxLenth() + "\t\t\t" + attr.getCheckRgx() + "\n");
        }
        System.out.print("===========================================================================================");
    }
}
