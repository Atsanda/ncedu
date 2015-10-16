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
     * Outputs data base which lines contains toBeFound string.
     * @param toBeFound
     */
    void find(String toBeFound);

    /**
     * Edits attribute of given object.
     * @param object
     * @param attribute
     */
    void edit(String object, String attribute);

    /**
     * Parses string and adds new object to data base.
     * @param toBeAdded
     */
    void add(String toBeAdded);

    /**
     * Delets goven object from data base and outputs the results of last find function.
     * @param toBeDeleted
     */
    void delete(String toBeDeleted);
}
