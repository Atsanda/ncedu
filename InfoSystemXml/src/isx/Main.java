package isx;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by artyom on 17.10.15.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ISXImpl isx = new ISXImpl();
        try{
            if(args.length == 0){
                throw new IllegalArgumentException("Error: please enter your command");
            }

            switch(args[0]){
                case("find") : {
                    if(args.length != 2){
                        throw new IllegalArgumentException("\nError: find function must have only one argument\n");
                    }
                    isx.find(args[1]);
                    break;
                }
                case("print") : {
                    if(args.length != 1){
                        throw new IllegalArgumentException("\nError: print function must have no arguments\n");
                    }
                    isx.print();
                    break;
                }
                case("edit") : {
                    if(args.length != 4){
                        throw new IllegalArgumentException("\nError: find function must have only three arguments\n");
                    }
                    isx.edit(args[1],args[2],args[3]);
                    break;
                }
                case("add") : {
                    isx.add(Arrays.asList(args).subList(1,args.length).toArray(new String[0]));

                    break;
                }
                case("delete") : {
                    if(args.length != 2){
                        throw new IllegalArgumentException("\nError: find function must have only one argument\n");
                    }
                    isx.delete(args[1]);
                    break;
                }
                case("help") : {
                    if(args.length != 1){
                        throw new IllegalArgumentException("\nError: help function must have no arguments\n");
                    }
                    isx.help();
                    break;
                }
                default : {
                    throw new IllegalArgumentException("\nError: no command matching " + args[0] + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            isx.help();
        }
    }
}
