package isx;

import org.xml.sax.SAXException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * This is console application, which provides with interface of university students data base manipulation through XML.
 * @author Tsanda Artyom
 * @version 1.0.0
 */
public interface ISX {
    /**
     * Outputs data base into console.
     */
    void print();

    /**
     * Outputs those students whose last_name contains toBeFound string.
     * @param toBeFound substing, in case of * outputs the hole table
     */
    void find(String toBeFound);

    /**
     * Edits attribute of given object.
     * @param id
     * @param attribute
     * @param value
     */
    void edit(String id, String attribute, String value) throws TransformerException, IOException, SAXException;

    /**
     * Adds new object to data base.
     * @param atributes
     */
    void add(String[] atributes) throws TransformerException, IOException, SAXException;

    /**
     * Delets given object from data base and outputs the results of last find function.
     * @param id
     */
    void delete(String id) throws TransformerException, IOException, SAXException;

    /*
     * Prints help information
     */
    void help();
}
