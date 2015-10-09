package ru.ncedu.java.tasks;

import org.w3c.dom.*;
import javax.xml.xpath.*;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import java.io.*;
import org.xml.sax.SAXException;

/**
 * Created by artyom on 09.10.15.
 */
public class XPathCallerImpl implements  XPathCaller {

    private Element[] conversion(NodeList nodeList){
        if(nodeList == null)
            return null;

        int length = nodeList.getLength();
        Element[] ret = new Element[length];
        Node tmp;
        for (int n = 0; n < length; ++n) {
            tmp = nodeList.item(n);
            if(tmp.getNodeType() == Node.ELEMENT_NODE)
                ret[n] = (Element) tmp;
        }

        return ret;
    };

    @Override
    public Element[] getEmployees(Document src, String deptno, String docType) {

        XPath xPath =  XPathFactory.newInstance().newXPath();

        String expression = ".//employee[@deptno ='" + deptno +"']";
        NodeList nodeList = null;

        try {
            nodeList = (NodeList) xPath.compile(expression).evaluate(src, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return conversion(nodeList);
    }

    @Override
    public String getHighestPayed(Document src, String docType) {
        XPath xPath =  XPathFactory.newInstance().newXPath();

        String expression = ".//employee[sal = (//sal[not(. < //sal)])[1]]";
        Node node = null;

        try {
            node = (Node) xPath.compile(expression).evaluate(src, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return ((Element) node).getElementsByTagName("ename").item(0).getTextContent();
    }

    @Override
    public String getHighestPayed(Document src, String deptno, String docType) {
        return null;
    }

    @Override
    public Element[] getTopManagement(Document src, String docType) {
        return new Element[0];
    }

    @Override
    public Element[] getOrdinaryEmployees(Document src, String docType) {
        return new Element[0];
    }

    @Override
    public Element[] getCoworkers(Document src, String empno, String docType) {
        return new Element[0];
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream("./src/emp-hier.xml"));

        XPathCaller tst = new XPathCallerImpl();
        Element[] ret = tst.getEmployees(document, "30", "olol");
//        for(Element i: ret)
//            System.out.println(i.getAttribute("empno"));
        System.out.println(tst.getHighestPayed(document,"olol"));

    }
}
