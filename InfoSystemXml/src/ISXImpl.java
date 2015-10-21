//Bad practice #1: lacking package. Default package is bad.
//Bad practice #2: don't import classes with package.name.*
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

    public ISXImpl() throws ParserConfigurationException, SAXException, IOException{
    /*
        Make this class more universal. Create a constructor which receives absolute path to the 
    database-file and use it instead of hardcoded "University.xml"
    */
        File xmlFile = new File("University.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();

        freeStdNum = new LinkedList<Integer>();

        root = (Element) dataBase.getElementsByTagName("students").item(0);
        NodeList nodeList = root.getChildNodes();

        for(int j=1,tmpNodeInd = 0,tmpStdNum = -1;;) {
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

    private Integer getStdtId(){
        int ret = freeStdNum.getFirst();
        if(freeStdNum.isEmpty())
            freeStdNum.add(ret+1);
        return new Integer(ret);
    }

    @Override
    public void print() {
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
    public void add(String[] atributes) throws IllegalArgumentException, TransformerException, IOException, SAXException {
        if(!isValid(atributes))
            throw new IllegalArgumentException();
        //!TODO trst wether exists
        Element student = dataBase.createElement("student");
        Attr attr = dataBase.createAttribute("stdno");
        attr.setValue(getStdtId().toString());
        student.setAttributeNode(attr);
        root.appendChild(student);


    /*
        The following 20 lines are doing the same thing. So it would be more effective if you create
    a map of attributes and then go through this list in order to avoid reproducing code.
    Having 5 attributes this approach is solvable, but annoying. Imagine having 100 attributes, which are changing every 
    month. You'll suffer from such approach.
    What do I mean: create configurational map like Map<Integer,String> with contents:
    Integer.valueOf(0) : "lastname";
    Integer.valueOf(1) : "firstName"; and so on
    Configuration of the database can be contained in separate XML-file :)
    */
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
        /*
            It's a bad practice to contain strings inside code. It's better to create 
        private String field in the beginning of the class, or also put string into XML-config
        of the project.
        */
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(dataBase);
        File xmlFile = new File("University.xml");
        StreamResult result = new StreamResult(xmlFile);

        transformer.transform(source, result);

        dataBase = dBuilder.parse(xmlFile);
        dataBase.getDocumentElement().normalize();
        root = (Element) dataBase.getElementsByTagName("students").item(0);
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
    public void delete(String toBeDeleted) {

    }
}
