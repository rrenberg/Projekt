/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatechat;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author jakobarnoldsson
 */
public class XMLParser {
    
    private DocumentBuilderFactory DBFactory;
    private DocumentBuilder DBuilder;
  
    public XMLParser(){
        DBFactory = DocumentBuilderFactory.newInstance();
        try {
            DBuilder = DBFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String requestParser (String inText) throws Exception{
        InputSource is = new InputSource(new StringReader(inText));
        return DBuilder.parse(is).getDocumentElement().getTextContent();
    }
    
}
